package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    private ObjectNode node;
    private JsonMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new JsonMapper();
        node = JsonNodeFactory.instance.objectNode();
    }

    @Test
    public void testHandleType() throws Exception {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, serializer.handledType());

        StdSerializer<Integer> integerSerializer = new StdSerializer<Integer>(Integer.class);
        assertEquals(Integer.class, integerSerializer.handledType());
    }

    @Test
    public void testSerialize() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        String value = "Hello World!";
        ObjectNode result = mapper.writeValueAsTree(value, stringSerializer);
        assertEquals(value, result.getTextValue());

        StdSerializer<Integer> integerSerializer = new StdSerializer<Integer>(Integer.class);
        Integer intValue = 42;
        result = mapper.writeValueAsTree(intValue, integerSerializer);
        assertEquals(intValue.toString(), result.getTextValue());
    }

    @Test
    public void testGetSchema() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        ObjectNode schema = stringSerializer.getSchema(mapper.getSerializerProvider(), String.class);
        ObjectNode expectedSchema = createSchemaNode("string");
        assertEquals(expectedSchema, schema);

        StdSerializer<Integer> integerSerializer = new StdSerializer<Integer>(Integer.class);
        schema = integerSerializer.getSchema(mapper.getSerializerProvider(), Integer.class);
        expectedSchema = createSchemaNode("number", true);
        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mapper.getTypeFactory().constructType(String.class);
        stringSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectAnyFormat(typeHint);

        StdSerializer<Integer> integerSerializer = new StdSerializer<Integer>(Integer.class);
        typeHint = mapper.getTypeFactory().constructType(Integer.class);
        integerSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectAnyFormat(typeHint);
    }

    @Test
    public void testWrapAndThrow() throws Exception {
        SerializerProvider provider = mapper.getSerializerProvider();
        Object bean = new Object();

        // Test with regular IOException
        IOException ioException = new IOException();
        try {
            StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
            stringSerializer.wrapAndThrow(provider, ioException, bean, "fieldName");
            fail();
        } catch (JsonMappingException e) {
            // Pass
        }

        // Test with JsonMappingException
        JsonMappingException jsonMappingException = new JsonMappingException("Test");
        try {
            StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
            stringSerializer.wrapAndThrow(provider, jsonMappingException, bean, "fieldName");
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Test", e.getOriginalMessage());
            assertNull(e.getPath());
        }

        // Test with error
        Error error = new Error();
        try {
            StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
            stringSerializer.wrapAndThrow(provider, error, bean, "fieldName");
            fail();
        } catch (Error e) {
            // Pass
        }

        // Test with unchecked exception
        RuntimeException runtimeException = new RuntimeException();
        try {
            StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
            stringSerializer.wrapAndThrow(provider, runtimeException, bean, "fieldName");
            fail();
        } catch (RuntimeException e) {
            // Pass
        }
    }

    @Test
    public void testIsDefaultSerializer() throws Exception {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertTrue(StdSerializer.isDefaultSerializer(stringSerializer));

        MyCustomSerializer myCustomSerializer = new MyCustomSerializer();
        assertFalse(StdSerializer.isDefaultSerializer(myCustomSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        Object nestedValue = new Object();
        BeanProperty property = new BeanProperty("propertyName", null, null, null, null, null);
        SerializerProvider provider = mapper.getSerializerProvider();

        // Test with no existing serializer
        Converter<Object, Object> converter = mock(Converter.class);
        JsonSerializer<?> existingSerializer = null;
        StdSerializer<List<Object>> listSerializer = new StdSerializer<List<Object>>(List.class) {
            @Override
            protected JsonSerializer<?> findConvertingContentSerializer(SerializerProvider provider,
                    BeanProperty prop, JsonSerializer<?> existingSerializer)
                    throws JsonMappingException {
                return new StdDelegatingSerializer(converter, Object.class, existingSerializer);
            }
        };
        ObjectNode result = mapper.writeValueAsTree(Collections.singletonList(nestedValue), listSerializer);
        assertEquals(1, result.size());
        verify(converter).writeValue(provider, nestedValue);

        // Test with existing serializer
        JsonSerializer<Object> existingObjectSerializer = new StdSerializer<Object>(Object.class);
        StdSerializer<List<Object>> listSerializerWithExistingSerializer = new StdSerializer<List<Object>>(List.class) {
            @Override
            protected JsonSerializer<?> findConvertingContentSerializer(SerializerProvider provider,
                    BeanProperty prop, JsonSerializer<?> existingSerializer)
                    throws JsonMappingException {
                return new StdDelegatingSerializer(converter, Object.class, existingObjectSerializer);
            }
        };
        result = mapper.writeValueAsTree(Collections.singletonList(nestedValue), listSerializerWithExistingSerializer);
        assertEquals(1, result.size());
        verify(converter).writeValue(provider, nestedValue);
        verify(existingObjectSerializer).writeValue(provider, nestedValue);
    }

    @Test
    public void testFindPropertyFilter() throws Exception {
        Object valueToFilter = new Object();
        SerializerProvider provider = mapper.getSerializerProvider();
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public void serializeAsField(ObjectFilterProvider filterProvider, JsonGenerator jgen,
                    SerializerProvider provider, Object bean, PropertyName propertyName)
                    throws IOException {
                // Empty implementation
            }

            @Override
            public void serializeAsProperty(ObjectFilterProvider filterProvider, JsonGenerator jgen,
                    SerializerProvider provider, Object bean, PropertyName propertyName)
                    throws IOException {
                // Empty implementation
            }
        };
        String filterId = "filterId";
        PropertyFilter foundFilter = new StdSerializer<Object>(Object.class).findPropertyFilter(provider, filterId, valueToFilter);
        assertEquals(filter, foundFilter);
    }

    private ObjectNode createSchemaNode(String type, boolean isOptional) {
        ObjectNode schema = mapper.createObjectNode();
        schema.put("type", type);
        if (!isOptional) {
            schema.put("required", true);
        }
        return schema;
    }

    @After
    public void tearDown() throws Exception {
        node = null;
        mapper = null;
    }
}

class MyCustomSerializer extends StdSerializer<String> {
    protected MyCustomSerializer() {
        super(String.class);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        // Custom serialization logic
    }
}