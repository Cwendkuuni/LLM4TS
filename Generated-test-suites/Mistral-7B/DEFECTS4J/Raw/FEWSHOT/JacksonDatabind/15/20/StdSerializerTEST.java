package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.AbstractNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StdSerializerTest {

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class, true);
        JsonSerializer<Number> numberSerializer = new StdSerializer<Number>(int[].class) {
            @Override
            public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeNumber(value.intValue());
            }
        };

        assertTrue(StdSerializer.class.isInstance(stringSerializer));
        assertTrue(StdSerializer.class.isInstance(objectSerializer));
        assertFalse(StdSerializer.class.isInstance(numberSerializer));
        assertFalse(StdSerializer.class.isInstance(new StdSerializer<String>(String[].class) {}));

        // Test with custom serializer
        @JsonSerialize(using = CustomSerializer.class)
        static class CustomClass {}

        StdSerializer<CustomClass> customSerializer = new StdSerializer<CustomClass>(CustomClass.class);

        assertFalse(StdSerializer.class.isInstance(customSerializer));
        assertTrue(CustomSerializer.class.isInstance(customSerializer));
    }

    @Test
    public void testHandleType() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class, true);

        assertEquals(String.class, stringSerializer.handledType());
        assertEquals(Object.class, objectSerializer.handledType());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);

        ObjectNode schema = stringSerializer.getSchema(new SerializerProvider(), null);

        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);

        // Default implementation specifies no format
        stringSerializer.acceptJsonFormatVisitor(null, null);
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        SerializerProvider provider = null;
        Object bean = new Object();
        String fieldName = "field";

        try {
            throw new RuntimeException("Test Exception");
        } catch (Throwable t) {
            StdSerializer.wrapAndThrow(provider, t, bean, fieldName);
            fail("Expected JsonMappingException to be thrown");
        }

        try {
            throw new IOException("Test IOException");
        } catch (Throwable t) {
            StdSerializer.wrapAndThrow(provider, t, bean, fieldName);
            fail("Expected JsonMappingException to be thrown");
        }

        try {
            throw new JsonMappingException("Test JsonMappingException");
        } catch (Throwable t) {
            StdSerializer.wrapAndThrow(provider, t, bean, fieldName);
            fail("Expected JsonMappingException to be thrown as is");
        }

        try {
            throw new Error("Test Error");
        } catch (Throwable t) {
            StdSerializer.wrapAndThrow(provider, t, bean, fieldName);
            fail("Expected Error to be thrown as is");
        }

        try {
            throw new StackOverflowError("Test StackOverflowError");
        } catch (Throwable t) {
            StdSerializer.wrapAndThrow(provider, t, bean, fieldName);
            fail("Expected Error to be thrown as is");
        }

        try {
            throw new InvocationTargetException(new RuntimeException("Test Exception"));
        } catch (Throwable t) {
            StdSerializer.wrapAndThrow(provider, t, bean, fieldName);
            fail("Expected RuntimeException to be thrown");
        }

        try {
            throw new InvocationTargetException(new IOException("Test IOException"));
        } catch (Throwable t) {
            StdSerializer.wrapAndThrow(provider, t, bean, fieldName);
            fail("Expected IOException to be thrown");
        }

        try {
            throw new InvocationTargetException(new JsonMappingException("Test JsonMappingException"));
        } catch (Throwable t) {
            StdSerializer.wrapAndThrow(provider, t, bean, fieldName);
            fail("Expected JsonMappingException to be thrown as is");
        }
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("foo", "bar");

        SerializerProvider provider = new SerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        DefaultSerializerProvider defaultProvider = new DefaultSerializerProvider();

        StdSerializer<ObjectNode> nodeSerializer = new StdSerializer<ObjectNode>(ObjectNode.class) {
            @Override
            public void serialize(ObjectNode value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeObject(value);
            }
        };

        Converter<ObjectNode, String> converter = mapper.getTypeFactory().constructValueConverter(ObjectNode.class, String.class);
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString(value);
            }
        };

        JsonSerializer<ObjectNode> existingSerializer = provider.getFactory().findValueSerializer(ObjectNode.class);
        JsonSerializer<String> resultSerializer = nodeSerializer.findConvertingContentSerializer(provider, null, node);

        assertEquals(stringSerializer, resultSerializer);
        assertEquals(existingSerializer, nodeSerializer.findConvertingContentSerializer(provider, resultSerializer, node));

        existingSerializer = defaultProvider.getFactory().findValueSerializer(ObjectNode.class);
        resultSerializer = nodeSerializer.findConvertingContentSerializer(defaultProvider, null, node);

        assertEquals(stringSerializer, resultSerializer);
        assertEquals(existingSerializer, nodeSerializer.findConvertingContentSerializer(defaultProvider, resultSerializer, node));
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        FilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.setFilterID("myId", new MyPropertyFilter());

        SerializerProvider provider = new SerializerProvider() {
            @Override
            public FilterProvider getFilterProvider() {
                return filterProvider;
            }
        };

        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString(value);
            }
        };

        Object valueToFilter = "value";
        PropertyFilter resultFilter = stringSerializer.findPropertyFilter(provider, "myId", valueToFilter);

        assertNotNull(resultFilter);
        assertTrue(MyPropertyFilter.class.isInstance(resultFilter));
    }

    static class MyPropertyFilter implements PropertyFilter {
        @Override
        public void serializeAsValue(SerializerProvider provider, Object valueToFilter, ObjectNode result) throws IOException {
            result.put("filteredValue", valueToFilter);
        }

        @Override
        public void serializeAsFieldName(SerializerProvider provider, Object valueToFilter, ObjectNode result) throws IOException {
            result.put("filteredName", "filteredName");
        }

        @Override
        public void serializeEmptyAsFieldName(SerializerProvider provider, Object valueToFilter, ObjectNode result) throws IOException {
            result.put("filteredNameEmpty", "filteredNameEmpty");
        }
    }
}