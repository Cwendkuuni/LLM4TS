package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.DefaultSerializers;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.JavaType;
import com.fasterxml.jackson.databind.util.Converter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StdSerializerTest {

    private ObjectMapper objectMapper;

    @BeforeClass
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testHandleType() {
        Class<String> stringType = String.class;
        StdSerializer<String> stringSerializer = new StdSerializer<String>(stringType);
        Assert.assertEquals(stringSerializer.handledType(), stringType);

        JavaType stringJavaType = objectMapper.getTypeFactory().constructType(String.class);
        StdSerializer<String> stringJavaSerializer = new StdSerializer<String>(stringJavaType);
        Assert.assertEquals(stringJavaSerializer.handledType(), stringType);

        Class<Integer> integerType = Integer.class;
        StdSerializer<Integer> integerSerializer = new StdSerializer<Integer>(integerType);
        Assert.assertEquals(integerSerializer.handledType(), integerType);

        JavaType integerJavaType = objectMapper.getTypeFactory().constructType(Integer.class);
        StdSerializer<Integer> integerJavaSerializer = new StdSerializer<Integer>(integerJavaType);
        Assert.assertEquals(integerJavaSerializer.handledType(), integerType);
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        String value = "example";
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        ObjectWriter objectWriter = objectMapper.writerWithView(JsonView.ANY);

        JsonNode expected = createSchemaNode("string");
        JsonNode actual = stringSerializer.getSchema(objectMapper.getSerializerProvider(), String.class);
        Assert.assertEquals(actual, expected);

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        stringSerializer.serialize(value, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();

        Assert.assertEquals(objectWriter.writeValueAsString(value), stringWriter.toString());
    }

    @Test
    public void testCreateSchemaNode() {
        String type = "number";
        ObjectNode expected = createSchemaNode(type);
        ObjectNode actual = new StdSerializer<Object>(Object.class).createSchemaNode(type);
        Assert.assertEquals(actual, expected);

        String typeWithOptional = "number";
        boolean isOptional = true;
        ObjectNode expectedWithOptional = createSchemaNode(type, isOptional);
        ObjectNode actualWithOptional = new StdSerializer<Object>(Object.class).createSchemaNode(type, isOptional);
        Assert.assertEquals(actualWithOptional, expectedWithOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        JsonFormatVisitorWrapper visitor = objectMapper.getSerializerProvider().getJsonFormatVisitorFactory()
                .findRootValueSerializer(Object.class);

        serializer.acceptJsonFormatVisitor(visitor, Object.class);
    }

    @Test
    public void testWrapAndThrow() throws JsonMappingException {
        SerializerProvider provider = objectMapper.getSerializerProvider();
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);

        try {
            serializer.wrapAndThrow(provider, new IOException("test"), null, null);
            Assert.fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            serializer.wrapAndThrow(provider, new IllegalArgumentException("test"), null, null);
            Assert.fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            serializer.wrapAndThrow(provider, new RuntimeException("test"), null, null);
            Assert.fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            serializer.wrapAndThrow(provider, new IOException("test"), null, "test");
            Assert.fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            serializer.wrapAndThrow(provider, new IOException("test"), null, 0);
            Assert.fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        StdSerializer<Number> numberSerializer = new StdSerializer<Number>(Number.class);

        Assert.assertTrue(StdSerializer.class.isInstance(serializer));
        Assert.assertTrue(StdSerializer.class.isInstance(stringSerializer));
        Assert.assertTrue(StdSerializer.class.isInstance(numberSerializer));

        Assert.assertTrue(serializer.isDefaultSerializer(serializer));
        Assert.assertFalse(serializer.isDefaultSerializer(stringSerializer));
        Assert.assertFalse(serializer.isDefaultSerializer(numberSerializer));

        Assert.assertTrue(stringSerializer.isDefaultSerializer(stringSerializer));
        Assert.assertFalse(stringSerializer.isDefaultSerializer(serializer));
        Assert.assertFalse(stringSerializer.isDefaultSerializer(numberSerializer));

        Assert.assertTrue(numberSerializer.isDefaultSerializer(numberSerializer));
        Assert.assertFalse(numberSerializer.isDefaultSerializer(serializer));
        Assert.assertFalse(numberSerializer.isDefaultSerializer(stringSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper objectMapperWithConverters = new ObjectMapper();
        objectMapperWithConverters.registerModule(new SimpleModule()
                .addSerializer(String[].class, new StringArraySerializer()));

        SerializerProvider provider = objectMapperWithConverters.getSerializerProvider();
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);

        JsonSerializer<String[]> expected = objectMapperWithConverters.findValueSerializer(String[].class);
        JsonSerializer<Object> actual = serializer.findConvertingContentSerializer(provider, null, new String[]{"test"});

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = objectMapper.getSerializerProvider();

        PropertyFilter expected = new StringFilter();
        String propertyName = "test";
        Object valueToFilter = new Object();

        PropertyFilter actual = new StdSerializer<Object>(Object.class).findPropertyFilter(provider, "testId", valueToFilter);

        Assert.assertEquals(expected, actual);
    }

    private JsonNode createSchemaNode(String type, boolean isOptional) {
        ObjectMapper objectMapper = new ObjectMapper();
        AnnotationIntrospector introspector = objectMapper.getAnnotationIntrospector();
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);

        return serializer.createSchemaNode(type, isOptional);
    }

    private ObjectNode createSchemaNode(String type) {
        ObjectMapper objectMapper = new ObjectMapper();
        AnnotationIntrospector introspector = objectMapper.getAnnotationIntrospector();
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);

        return serializer.createSchemaNode(type);
    }

    private ObjectNode createObjectNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.getNodeFactory().objectNode();
    }

    private static class StringArraySerializer extends StdSerializer<String[]> {
        public StringArraySerializer() {
            this(null);
        }

        public StringArraySerializer(Class<String[]> t) {
            super(t);
        }

        @Override
        public void serialize(String[] value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
            DefaultSerializers.findStringSerializer(provider).serialize(value, jgen, provider);
        }
    }

    private static class StringFilter implements PropertyFilter {
        @Override
        public void serializeAsSchema(JsonFormatVisitorWrapper visitor, SerializerProvider provider, Type typeHint)
                throws JsonMappingException {
            visitor.writeSchemaField("test", JsonSchema.ValueType.STRING);
        }

        @Override
        public void serializeAsValue(JsonGenerator jgen, SerializerProvider provider, Object value)
                throws IOException, JsonProcessingException {
            jgen.writeString("filtered value");
        }

        @Override
        public boolean shouldFilterProperty(Object bean, String propertyName) {
            return true;
        }
    }
}