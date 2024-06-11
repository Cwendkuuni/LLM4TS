package com.example; // replace with your test package

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static com.fasterxml.jackson.core.Version.VERSION_2_12_2;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("valueMethod");
        objectMapper.setSerializerFactory(new DefaultSerializerFactory(objectMapper, filter));
    }

    // Test constructor
    @Test
    public void testJsonValueSerializerConstruction() {
        AnnotatedMethod valueMethod = new AnnotatedMethod(ValueSerializerTest.class.getDeclaredMethod("getValueMethod"), ValueSerializerTest.class);
        JsonSerializer<Object> valueSerializer = new SimpleObjectSerializer(null);
        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, valueSerializer);
        Assert.assertEquals(JsonValueSerializer.class, serializer.getClass());
        Assert.assertNotNull(serializer._accessorMethod);
        Assert.assertNotNull(serializer._valueSerializer);
        Assert.assertNull(serializer._property);
        Assert.assertTrue(serializer._forceTypeInformation);
    }

    // Test withResolved method
    @Test
    public void testJsonValueSerializerWithResolved() throws Exception {
        ValueSerializerTest valueSerializerTest = new ValueSerializerTest();
        AnnotatedMethod valueMethod = valueSerializerTest.getClass().getDeclaredMethod("getValueMethod");
        JsonSerializer<Object> valueSerializer = new SimpleObjectSerializer(null);
        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, valueSerializer, null, null);
        JsonValueSerializer resolvedSerializer = serializer.withResolved(null, null, true);
        Assert.assertEquals(JsonValueSerializer.class, resolvedSerializer.getClass());
        Assert.assertEquals(valueMethod, resolvedSerializer._accessorMethod);
        Assert.assertEquals(valueSerializer, resolvedSerializer._valueSerializer);
        Assert.assertNull(resolvedSerializer._property);
        Assert.assertTrue(resolvedSerializer._forceTypeInformation);
    }

    // Test serialize method with simple objects
    @Test
    public void testJsonValueSerializerSerializeSimpleObjects() throws Exception {
        ValueSerializerTest valueSerializerTest = new ValueSerializerTest();
        AnnotatedMethod valueMethod = valueSerializerTest.getClass().getDeclaredMethod("getValueMethod");
        JsonSerializer<Object> valueSerializer = new SimpleObjectSerializer(null);
        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, valueSerializer);
        Object testObject = new ValueSerializerTest();
        String expected = "{\"valueMethod\":\"Test method\"}";
        JsonNode actual = objectMapper.writeValueAsNode(testObject);
        String actualString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actual);
        Assert.assertEquals(expected, actualString);
    }

    // Test serialize method with custom types
    @JsonSerialize(using = CustomSerializer.class)
    static class CustomClass {
        private String value;

        public String getValue() {
            return value;
        }
    }

    static class CustomSerializer extends StdSerializer<CustomClass> {
        public CustomSerializer() {
            this(null);
        }

        public CustomSerializer(Class<CustomClass> t) {
            this( null, t );
        }

        public CustomSerializer(Class<CustomClass> t, JsonSerializer<Object> ser) {
            super(t);
            _valueSerializer = ser;
        }

        @Override
        public void serialize(CustomClass customClass, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("customValue");
            jsonGenerator.writeValue(customClass.getValue());
            jsonGenerator.writeEndObject();
        }
    }

    @Test
    public void testJsonValueSerializerSerializeCustomTypes() throws Exception {
        CustomClass customClass = new CustomClass();
        customClass.setValue("Custom value");
        ValueSerializerTest valueSerializerTest = new ValueSerializerTest();
        AnnotatedMethod valueMethod = valueSerializerTest.getClass().getDeclaredMethod("getValueMethod");
        JsonSerializer<Object> valueSerializer = new SimpleObjectSerializer(null);
        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, valueSerializer);
        String expected = "{\"valueMethod\":{\"customValue\":\"Custom value\"}}";
        JsonNode actual = objectMapper.writeValueAsNode(customClass);
        String actualString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actual);
        Assert.assertEquals(expected, actualString);
    }

    // Test serialize method with exception handling
    @Test(expected = JsonMappingException.class)
    public void testJsonValueSerializerSerializeWithException() throws Exception {
        ValueSerializerTest valueSerializerTest = new ValueSerializerTest();
        AnnotatedMethod valueMethod = valueSerializerTest.getClass().getDeclaredMethod("getValueMethodThrowingException");
        JsonSerializer<Object> valueSerializer = new SimpleObjectSerializer(null);
        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, valueSerializer);
        Object testObject = new ValueSerializerTest();
        serializer.serialize(testObject, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
    }

    // Test getSchema method
    @Test
    public void testJsonValueSerializerGetSchema() throws Exception {
        ValueSerializerTest valueSerializerTest = new ValueSerializerTest();
        AnnotatedMethod valueMethod = valueSerializerTest.getClass().getDeclaredMethod("getValueMethod");
        JsonSerializer<Object> valueSerializer = new SimpleObjectSerializer(null);
        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, valueSerializer);
        SchemaAware schemaAware = new StdSchema();
        JsonNode expected = schemaAware.getSchema(objectMapper.getSerializerProvider(), null);
        JsonNode actual = serializer.getSchema(objectMapper.getSerializerProvider(), null);
        Assert.assertEquals(expected, actual);
    }

    // Test acceptJsonFormatVisitor method
    @Test
    public void testJsonValueSerializerAcceptJsonFormatVisitor() throws Exception {
        ValueSerializerTest valueSerializerTest = new ValueSerializerTest();
        AnnotatedMethod valueMethod = valueSerializerTest.getClass().getDeclaredMethod("getValueMethod");
        JsonSerializer<Object> valueSerializer = new SimpleObjectSerializer(null);
        JsonValueSerializer serializer = new JsonValueSerializer(valueMethod, valueSerializer);
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleSerializerProvider provider = new SimpleSerializerProvider(objectMapper.getFactory());
        JsonFormatVisitorWrapper visitor = new JsonFormatter();
        JsonFactory factory = objectMapper.getFactory();
        provider.setSerializerFactory(factory);
        serializer.acceptJsonFormatVisitor(visitor, null);
        JsonNode expected = factory.constructObjectNode();
        expected.set("prefix", "JsonValueSerializerTests.ValueSerializerTest");
        expected.set("className", "ValueSerializerTest");
        expected.set("propertyFilter", SimpleBeanPropertyFilter.serializeAll());
        expected.set("serializers", new JsonNode[] {
                factory.constructObjectNode()
                        .set("class", JsonValueSerializer.class.getName())
                        .set("valueSerializer", new JsonNode()
                                .set("class", SimpleObjectSerializer.class.getName()))
                        .set("methodName", "getValueMethod")
        });
        JsonNode actual = visitor.getProvider().getSerializerProvider().getSerializerProvider().getProviderInstance(JsonFormatVisitorWrapper.class)
                .getSerializerProvider().getSerializerProvider().findValueSerializer(ValueSerializerTest.class, null);
        Assert.assertEquals(expected, actual);
    }

    // Test isNaturalTypeWithStdHandling method
    @Test
    public void testJsonValueSerializerIsNaturalTypeWithStdHandling() {
        Assert.assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(boolean.class, null));
        Assert.assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(int.class, null));
        Assert.assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(String.class, null));
        Assert.assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Double.class, null));
        Assert.assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(CustomClass.class, null));
    }

    // Test toString method
    @Test
    public void testJsonValueSerializerToString() {
        ValueSerializerTest valueSerializerTest = new ValueSerializerTest();
        JsonValueSerializer serializer = new JsonValueSerializer(valueSerializerTest.getClass().getDeclaredMethod("getValueMethod"), null);
        String expected = "(@JsonValue serializer for method ValueSerializerTest#getValueMethod())";
        String actual = serializer.toString();
        Assert.assertEquals(expected, actual);
    }

    static class ValueSerializerTest {
        @JsonSerialize(using = JsonValueSerializer.class)
        public Object getValueMethod() {
            return "Test method";
        }

        public Object getValueMethodThrowingException() {
            throw new RuntimeException("Test exception");
        }
    }

    static class StdSchema extends StdSerializers.BaseSchema {
        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return super.getSchema(provider, typeHint);
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            super.acceptJsonFormatVisitor(visitor, typeHint);
        }
    }
}

class SimpleObjectSerializer extends StdSerializer<Object> {
    public SimpleObjectSerializer() {
        this(null);
    }

    public SimpleObjectSerializer(Class<Object> t) {
        this(null, t);
    }

    public SimpleObjectSerializer(Class<Object> t, JsonSerializer<Object> ser) {
        super(t, true);
        _valueSerializer = ser;
    }

    @Override
    public void serialize(Object o, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeStartObject();
        jg.writeFieldName("value");
        _valueSerializer.serialize(o, jg, sp);
        jg.writeEndObject();
    }
}

class SimpleSerializerProvider extends SerializerProvider {
    public ObjectMapper objectMapper;

    public SimpleSerializerProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonSerializer<?> findValueSerializer(Class<?> type, BeanProperty property) {
        if (type == null) {
            return null;
        }
        try {
            return objectMapper.findValueSerializer(type, property);
        } catch (JsonMappingException e) {
            return null;
        }
    }

    @Override
    public void defaultSerializeValue(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeNull();
    }
}

class JsonFormatter extends JsonFormatVisitorWrapper {
    @Override
    public JsonNode getProviderInstance(JsonFormatVisitorWrapper.Provider provider) {
        return provider.getProviderInstance(JsonFormatter.class);
    }

    @Override
    public JsonNode getSchemaProvider() {
        return null;
    }

    @Override
    public JsonNode getSchema(JsonFormatVisitorWrapper.Provider provider, JavaType type) throws JsonMappingException {
        return null;
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        visitor.visitRootValueContainers();
        visitor.visitFields();
        visitor.visitSerializableTypes(typeHint.getRawClass());
    }

    @Override
    public void acceptValueFormatVisitor(JsonFormatVisitorWrapper visitor, JsonValueSerializer serializer, JavaType typeHint) throws JsonMappingException {
        visitor.visitObjectSerializers(serializer.getClass().getName(), serializer);
    }

    @Override
    public void acceptContainersAsRoot(JsonFormatVisitorWrapper visitor, JavaType typeHint) {
        visitor.visitContainerClasses(new Class[] {Object.class}, null);
    }
}