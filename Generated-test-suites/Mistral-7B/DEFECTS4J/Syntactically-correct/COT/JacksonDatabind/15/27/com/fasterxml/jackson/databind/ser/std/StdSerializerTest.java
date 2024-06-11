package com.fasterxml.jackson.databind.ser.std; // make sure the test class is in the same package as the class under test

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;
import com.fasterxml.jackson.databind.util.ConverterUtils;
import com.fasterxml.jackson.databind.util.JsonUtil;

import org.junit.Before;
import org.junit.Test;

public class StdSerializerTest {

    private ObjectMapper objectMapper;
    private StdSerializer<String> stringSerializer;
    private StdSerializer<Integer> integerSerializer;
    private StdSerializer<MyBean> myBeanSerializer;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        stringSerializer = objectMapper.findValueSerializer(String.class);
        integerSerializer = objectMapper.findValueSerializer(Integer.class);
        MyBean myBean = new MyBean("Test");
        myBeanSerializer = objectMapper.findValueSerializer(MyBean.class);
    }

    

    // Test constructor
    @Test
    public void testStdSerializerConstructor() throws Exception {
        assertNotNull(stringSerializer);
        assertEquals(String.class, stringSerializer.handledType());
    }

    @Test
    public void testStdSerializerConstructorType() throws Exception {
        assertNotNull(integerSerializer);
        assertEquals(Integer.class, integerSerializer.handledType());
    }

    @Test
    public void testStdSerializerConstructorDummy() throws Exception {
        assertNotNull(myBeanSerializer);
        assertEquals(MyBean.class, myBeanSerializer.handledType());
    }

    // Test abstract methods

    @Test(expected = AbstractMethodError.class)
    public void testAbstractSerializeMethod() throws Exception {
        stringSerializer.serialize(null, null, null);
    }

    // Test getSchema methods

    @Test
    public void testGetSchemaString() throws Exception {
        ObjectNode schema = stringSerializer.getSchema(null, String.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaInt() throws Exception {
        ObjectNode schema = integerSerializer.getSchema(null, Integer.class);
        assertEquals("number", schema.get("type").asText());
        assertEquals("int32", schema.get("type").get("items").asText());
    }

    // Test acceptJsonFormatVisitor methods

    @Test
    public void testAcceptJsonFormatVisitorIsNull() throws Exception {
        stringSerializer.acceptJsonFormatVisitor(null, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorInteger() throws Exception {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper().getSerializationConfig());
        integerSerializer.acceptJsonFormatVisitor(visitor, null);
        visitor.expectAnyFormat(Integer.class);
    }

    // Test wrapAndThrow methods

    @Test
    public void testWrapAndThrowNull() throws Exception {
        try {
            stringSerializer.wrapAndThrow(null, new RuntimeException("Test"), null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testWrapAndThrowIOException() throws Exception {
        try {
            byte[] bytes = new byte[1024 * 1024];
            JsonGenerator generator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(bytes));
            stringSerializer.wrapAndThrow(objectMapper.getSerializerProvider(), new IOException("Test"), null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testWrapAndThrowJsonMappingException() throws Exception {
        try {
            stringSerializer.wrapAndThrow(objectMapper.getSerializerProvider(), new JsonMappingException("Test"), null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testWrapAndThrowCustomException() throws Exception {
        try {
            stringSerializer.wrapAndThrow(objectMapper.getSerializerProvider(), new RuntimeException("Test"), null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    // Test isDefaultSerializer method

    @Test
    public void testIsDefaultSerializer() throws Exception {
        assertTrue(stringSerializer.isDefaultSerializer(stringSerializer));
        assertFalse(stringSerializer.isDefaultSerializer(new ObjectSerializer() {}));
    }

    // Test findConvertingContentSerializer method

    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        AnnotatedClass annotatedClass = objectMapper.getTypeFactory().constructType(MyBean.class);
        JsonSerializer<Object> defaultSerializer = objectMapper.findValueSerializer(Object.class);
        PropertyFilter filter = objectMapper.getSerializerProvider().getFilterProvider().findPropertyFilter(JsonFilter.FilterName.FAIL_ON_EMPTY_BEANS, null);

        // Test null existingSerializer
        Converter<Object, ObjectNode> converter = ConverterUtils.valueToTreeAsObjectNodeConverter(JsonNodeFactory.instance);
        ObjectNode node = converter.writeValueAsTree(new Object());
        JsonSerializer<ObjectNode> jsonSerializer = objectMapper.findValueSerializer(ObjectNode.class);
        JsonSerializer<Object> serializedSerializer = myBeanSerializer.findConvertingContentSerializer(objectMapper.getSerializerProvider(), annotatedClass.getAnnotations(), null);
        assertNotNull(serializedSerializer);
        assertEquals(JsonSerializer.class, serializedSerializer.getClass());
        assertEquals(ObjectNode.class, serializedSerializer.handledType());

        // Test existingSerializer
        PropertyFilter existingFilter = objectMapper.getSerializerProvider().getFilterProvider().findPropertyFilter(JsonFilter.FilterName.FAIL_ON_EMPTY_BEANS, null);
        JsonSerializer<ObjectNode> existingSerializer = new StdDelegatingSerializer(converter, ObjectNode.class, defaultSerializer);
        JsonSerializer<ObjectNode> serializedSerializer2 = myBeanSerializer.findConvertingContentSerializer(objectMapper.getSerializerProvider(), annotatedClass.getAnnotations(), existingSerializer);
        assertNotNull(serializedSerializer2);
        assertEquals(JsonSerializer.class, serializedSerializer2.getClass());
        assertEquals(ObjectNode.class, serializedSerializer2.handledType());
        assertNull(serializedSerializer2.delegate);

        // Test no converter
        AnnotatedMember member = annotatedClass.findMember("myField");
        Converter<?, ?> converter2 = converter;
        ObjectNode node2 = converter.writeValueAsTree(new Object());
        JsonSerializer<ObjectNode> serializedSerializer3 = myBeanSerializer.findConvertingContentSerializer(objectMapper.getSerializerProvider(), member.getAnnotations(), null);
        assertNull(serializedSerializer3);

        // Test filter
        FilterProvider filters = objectMapper.getSerializerProvider().getFilterProvider();
        PropertyFilter filter2 = new PropertyFilter() {
            @Override
            public void serializeFields(ObjectBean bean, JsonGenerator generator, SerializerProvider serializers, PropertyWriter propertyWriter)
                    throws IOException, JsonProcessingException {
                if (propertyWriter.getName().equals("myField")) {
                    generator.writeNull();
                } else {
                    propertyWriter.serializeAsNormal();
                }
            }

            @Override
            public void serializeWithFilter(MapperFactory mf, SerializerProvider serializers, Object bean, JsonGenerator generator)
                    throws IOException, JsonProcessingException {
                generator.writeStartObject();
                JsonSerializer<ObjectNode> serializer = myBeanSerializer.findConvertingContentSerializer(mf.getSerializerProvider(), null, null);
                serializer.serializeValues(bean, generator, serializers);
                generator.writeEndObject();
            }
        };
        filters.addFilter(filter2);
        ObjectNode node3 = JsonUtil.createEmptyObjectNode(objectMapper);
        JsonSerializer<ObjectNode> serializedSerializer4 = myBeanSerializer.findConvertingContentSerializer(objectMapper.getSerializerProvider(), null, node3);
        assertNotNull(serializedSerializer4);
        assertEquals(JsonSerializer.class, serializedSerializer4.getClass());
        assertEquals(ObjectNode.class, serializedSerializer4.handledType());
        assertNull(serializedSerializer4.delegate);

        // Test custom serializer
        MyCustomSerializer customSerializer = new MyCustomSerializer();
        JsonSerializer<ObjectNode> serializedSerializer5 = myBeanSerializer.findConvertingContentSerializer(objectMapper.getSerializerProvider(), annotatedClass.getAnnotations(), customSerializer);
        assertNull(serializedSerializer5);

        // Test non-static field
        MyBeanMyField myBeanMyField = new MyBeanMyField();
        AnnotatedMember member2 = annotatedClass.findMember("myBeanMyField");
        JsonSerializer<ObjectNode> serializedSerializer6 = myBeanSerializer.findConvertingContentSerializer(objectMapper.getSerializerProvider(), member2.getAnnotations(), null);
        assertNull(serializedSerializer6);

        // Test static field
        MyBeanMyStatic myBeanMyStatic = new MyBeanMyStatic();
        AnnotatedMember member3 = annotatedClass.findMember("myBeanMyStatic");
        JsonSerializer<ObjectNode> serializedSerializer7 = myBeanSerializer.findConvertingContentSerializer(objectMapper.getSerializerProvider(), member3.getAnnotations(), null);
        assertNotNull(serializedSerializer7);
        assertEquals(JsonSerializer.class, serializedSerializer7.getClass());
        assertEquals(ObjectNode.class, serializedSerializer7.handledType());
        assertNull(serializedSerializer7.delegate);
    }

    // Test findPropertyFilter method

    @Test
    public void testFindPropertyFilter() throws Exception {
        FilterProvider filters = objectMapper.getSerializerProvider().getFilterProvider();
        PropertyFilter filter = filters.findPropertyFilter("filterId", new Object());
        assertNull(filter);

        filter = filters.findPropertyFilter("filterId", new ObjectNode(objectMapper.getFactory(), null));
        assertNull(filter);

        filter = filters.findPropertyFilter("filterId", null);
        assertNull(filter);

        MyBeanMyFilter myBeanMyFilter = new MyBeanMyFilter();
        Filters filters2 = new Filters();
        filters2.addFilter(myBeanMyFilter);
        filters = new FilterProvider();
        filters.setFilters(filters2);
        filter = filters.findPropertyFilter("filterId", null);
        assertNotNull(filter);
        assertEquals(MyBeanMyFilter.class, filter.getClass());
    }

    

    static class MyBean {
        @JsonSerialize(using = StdSerializer.class)
        private String myField;

        @JsonSerialize(using = StdSerializer.class)
        private MyBeanMyField myBeanMyField;

        @JsonSerialize(using = MyCustomSerializer.class)
        private MyBeanMyStatic myBeanMyStatic;

        public MyBean() {}

        public MyBean(String myField) {
            this.myField = myField;
        }

        public MyBean(MyBeanMyField myBeanMyField) {
            this.myBeanMyField = myBeanMyField;
        }

        public MyBean(MyBeanMyStatic myBeanMyStatic) {
            this.myBeanMyStatic = myBeanMyStatic;
        }

        static class MyBeanMyField {
            private String myField;
        }

        static class MyBeanMyStatic {
            private static int myStaticField;
        }

        static class MyBeanMyFilter implements PropertyFilter {
            @Override
            public void serializeFields(ObjectBean bean, JsonGenerator generator, SerializerProvider serializers, PropertyWriter propertyWriter)
                    throws IOException, JsonProcessingException {
                if (propertyWriter.getName().equals("myField")) {
                    generator.writeNull();
                } else {
                    propertyWriter.serializeAsNormal();
                }
            }

            @Override
            public void serializeWithFilter(MapperFactory mf, SerializerProvider serializers, Object bean, JsonGenerator generator)
                    throws IOException, JsonProcessingException {
                generator.writeStartObject();
                JsonSerializer<ObjectNode> serializer = mf.findValueSerializer(ObjectNode.class);
                serializer.serializeValues(bean, generator, serializers);
                generator.writeEndObject();
            }
        }
    }

    static class MyCustomSerializer extends StdSerializer<ObjectNode> {
        public MyCustomSerializer() {
            this(null);
        }

        public MyCustomSerializer(Class<ObjectNode> t) {
            super(t);
        }

        @Override
        public void serialize(ObjectNode value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
            // custom serialization logic
        }
    }
}