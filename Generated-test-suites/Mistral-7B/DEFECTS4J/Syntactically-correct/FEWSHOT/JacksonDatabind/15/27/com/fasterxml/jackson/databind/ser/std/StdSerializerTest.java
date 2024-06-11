package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {

    @Test
    public void testStdSerializerAbstractClass() {
        assertTrue(StdSerializer.class.isAbstract());
    }

    @Test
    public void testStdSerializerHandledType() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, stringSerializer.handledType());
    }

    @Test
    public void testStdSerializerGetter() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, stringSerializer.getHandledType());
    }

    @Test
    public void testStdSerializerSerialize() throws IOException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }
        };
        String value = "test";
        ObjectWriter writer = new ObjectMapper().writerWithRootValueSerializer(stringSerializer);
        String result = writer.writeValueAsString(value);
        assertEquals("test", result);
    }

    @Test
    public void testStdSerializerGetSchema() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                    throws JsonMappingException {
                return createSchemaNode("string");
            }
        };
        assertEquals("string", stringSerializer.getSchema(null, null).path("type").asText());
    }

    @Test
    public void testStdSerializerAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                visitor.expectStringFormat(typeHint);
            }
        };
        JsonFormatVisitorWrapper visitor = new JsonSerializableJsonFormatVisitorWrapper(null);
        stringSerializer.acceptJsonFormatVisitor(visitor, String.class);
    }

    @Test
    public void testStdSerializerIsDefaultSerializer() {
        assertTrue(StdSerializer.class.isInstance(new BaseAnnotationSerializer()));
        assertFalse(StdSerializer.class.isInstance(new CustomSerializer()));
    }

    @Test(expected = JsonMappingException.class)
    public void testStdSerializerWrapAndThrowNull() throws IOException {
        SerializerProvider provider = null;
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        throw stringSerializer.wrapAndThrow(provider, new IOException(), null, null);
    }

    @Test(expected = IOException.class)
    public void testStdSerializerWrapAndThrowIOException() throws IOException {
        SerializerProvider provider = new SerializerProvider();
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        throw stringSerializer.wrapAndThrow(provider, new IOException(), null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testStdSerializerWrapAndThrowJsonMappingException() throws IOException {
        SerializerProvider provider = new SerializerProvider();
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        throw stringSerializer.wrapAndThrow(provider, new JsonMappingException(), null, null);
    }

    @Test
    public void testStdSerializerFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<List<Integer>> listIntSerializer = new StdSerializer<List<Integer>>(List.class) {
            @Override
            public void serialize(List<Integer> value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeStartArray();
                for (Integer intValue : value) {
                    jgen.writeNumber(intValue);
                }
                jgen.writeEndArray();
            }
        };
        StdSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);
        StdSerializer<List<Integer>> listSerializerWithIntSerializer = new StdSerializer<List<Integer>>(List.class) {
            @Override
            public void serialize(List<Integer> value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                for (Integer intValue : value) {
                    intSerializer.serialize(intValue, jgen, provider);
                    jgen.writeComma();
                }
            }
        };
        StdSerializer<List<Integer>> listSerializerWithListIntSerializer = new StdSerializer<List<Integer>>(List.class) {
            @Override
            public void serialize(List<Integer> value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                StdSerializer<List<Integer>> listSerializer = findConvertingContentSerializer(provider, null, value);
                listSerializer.serialize(value, jgen, provider);
            }
        };
        ObjectNode node = mapper.createObjectNode();
        node.put("property", "value");
        List<Integer> listValue = Arrays.asList(1, 2, 3);
        StdSerializer<ObjectNode> nodeSerializer = mapper.findValueSerializer(ObjectNode.class);
        StdSerializer<List<Integer>> listSerializer = findConvertingContentSerializer(provider, null, listValue);
        assertEquals(listSerializerWithListIntSerializer.getClass(), listSerializer.getClass());
    }

    @Test
    public void testStdSerializerFindPropertyFilter() throws JsonMappingException {
        FilterProvider filterProvider = new SimpleFilterProvider();
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        PropertyFilter propertyFilter = new PropertyFilter() {
            @Override
            public void serializeProperty(String propertyName, Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException, JsonProcessingException {
                if (value instanceof String && value.equals("test")) {
                    jsonGenerator.writeString("filtered");
                } else {
                    jsonGenerator.writeValue(value);
                }
            }
        };
        filterProvider.addFilter("propertyFilter", propertyFilter);
        StdSerializer<ObjectNode> nodeSerializer = mapper.findValueSerializer(ObjectNode.class);
        ObjectNode nodeValue = mapper.createObjectNode();
        nodeValue.put("property", "test");
        PropertyFilter resultFilter = stringSerializer.findPropertyFilter(filterProvider, "propertyFilter", nodeValue);
        ObjectNode resultNode = mapper.createObjectNode();
        resultNode.put("property", "filtered");
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(stringWriter);
        stringSerializer.serialize(nodeValue, jsonGenerator, filterProvider);
        assertEquals(resultNode.toString(), stringWriter.toString());
    }

    private JsonNode createSchemaNode(String type) {
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", type);
        return schema;
    }
}

class BaseAnnotationSerializer extends StdSerializer<Object> {
    public BaseAnnotationSerializer() {
        this(Object.class);
    }

    protected BaseAnnotationSerializer(Class<Object> t) {
        super(t);
    }
}

class CustomSerializer extends StdSerializer<Object> {
    public CustomSerializer() {
        this(Object.class, false);
    }

    protected CustomSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }
}