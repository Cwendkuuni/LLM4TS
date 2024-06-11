package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testHandledType() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }
        };

        ObjectNode node = objectMapper.readValue("{\"field\":\"\"}", ObjectNode.class);
        AnnotatedField field = objectMapper.findAnnotatedMember("field", node);

        SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
        JsonSerializer<?> serializer = stringSerializer.findConvertingContentSerializer(provider, field, null);

        String value = "test";
        Object writeValueAsString = objectMapper.writeValueAsString(value);
        byte[] bytes = writeValueAsString.getBytes();

        serializer.serialize(value, objectMapper.getFactory().createGenerator(new ByteArrayOutputStream()), provider);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        String result = objectMapper.readValue(inputStream, String.class);

        assertEquals(value, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                visitor.expectStringFormat(typeHint);
            }
        };

        JsonNode schemaNode = stringSerializer.getSchema(objectMapper.getSerializationConfig().getDefaultSerializerProvider(), String.class);
        assertEquals("string", schemaNode.path("type").asText());
    }

    @Test
    public void testIsDefaultSerializer() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertTrue(StdSerializer.class.isInstance(stringSerializer));
        assertTrue(StdSerializer.class.isInstance(objectMapper.getValueSerializer(String.class)));
        assertFalse(StdSerializer.class.isInstance(new StringSerializer()));
    }

    @Test
    public void testFindPropertyFilter() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString("filtered_"+value);
            }
        };

        SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
        PropertyFilter filter = stringSerializer.findPropertyFilter(provider, "filter_id", "value");

        assertNull(filter);

        provider.setFilterProvider(new SimpleFilterProvider());
        FilterProvider filterProvider = provider.getFilterProvider();
        filter = filterProvider.findPropertyFilter("filter_id", "value");

        assertNotNull(filter);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithNullProvider() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                fail();
            }
        };

        String value = "test";

        stringSerializer.wrapAndThrow(null, new RuntimeException("Test"), null, "field");
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithNullBean() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                fail();
            }
        };

        String value = "test";

        stringSerializer.wrapAndThrow(objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
                new RuntimeException("Test"), null, "field");
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithNullIndex() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                fail();
            }
        };

        String value = "test";

        stringSerializer.wrapAndThrow(objectMapper.getSerializationConfig().getDefaultSerializerProvider(),
                new RuntimeException("Test"), null, (int) -1);
    }
}