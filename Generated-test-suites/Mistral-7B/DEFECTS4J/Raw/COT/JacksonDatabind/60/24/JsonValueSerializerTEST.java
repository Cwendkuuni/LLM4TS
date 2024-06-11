package com.example.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;

/**
 * Test file for JsonValueSerializer class
 */
public class JsonValueSerializerTest {

    private ObjectMapper mapper;
    private JsonSerializer<Object> valueSerializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        valueSerializer = mapper.findValueSerializer(String.class, null);
    }

    /**
     * Test constructor with AnnotatedMethod, JsonSerializer and BeanProperty
     */
    @Test
    public void testJsonValueSerializerConstructor() {
        AnnotatedMethod accessorMethod = createTestAnnotatedMethod();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer, null, false);
        Assert.assertEquals(JsonValueSerializer.class, jsonValueSerializer.getClass());
        Assert.assertNotNull(jsonValueSerializer._accessorMethod);
        Assert.assertNotNull(jsonValueSerializer._valueSerializer);
        Assert.assertNull(jsonValueSerializer._property);
    }

    /**
     * Test constructor with JsonValueSerializer and BeanProperty
     */
    @Test
    public void testJsonValueSerializerWithResolvedConstructor() {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        JsonValueSerializer resolvedJsonValueSerializer = jsonValueSerializer.withResolved(null, valueSerializer, false);
        Assert.assertEquals(jsonValueSerializer.getClass(), resolvedJsonValueSerializer.getClass());
        Assert.assertEquals(jsonValueSerializer._accessorMethod, resolvedJsonValueSerializer._accessorMethod);
        Assert.assertEquals(jsonValueSerializer._valueSerializer, resolvedJsonValueSerializer._valueSerializer);
        Assert.assertNull(jsonValueSerializer._property);
        Assert.assertFalse(resolvedJsonValueSerializer._forceTypeInformation);
    }

    /**
     * Test createContextual method
     */
    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        JsonSerializer<Object> ser = jsonValueSerializer.createContextual(mapper.getSerializerProvider(), null);
        Assert.assertNotNull(ser);
        Assert.assertTrue(ser instanceof JsonValueSerializer);
    }

    /**
     * Test serialize method with null object
     */
    @Test
    public void testSerializeNullObject() throws IOException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        StringWriter writer = new StringWriter(new OutputStreamWriter(System.out));
        jsonValueSerializer.serialize(null, mapper.getJsonFactory().createJsonGenerator(writer), mapper.getSerializerProvider());
        Assert.assertEquals("null", writer.toString().trim());
    }

    /**
     * Test serialize method with a non-null object and a valid JsonSerializer
     */
    @Test
    public void testSerializeWithValidJsonSerializer() throws IOException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        Object testObject = new TestSerializedObject();
        StringWriter writer = new StringWriter(new OutputStreamWriter(System.out));
        jsonValueSerializer.serialize(testObject, mapper.getJsonFactory().createJsonGenerator(writer), mapper.getSerializerProvider());
        Assert.assertEquals("testString", writer.toString().trim());
    }

    /**
     * Test serialize method with a non-null object and no JsonSerializer
     */
    @Test
    public void testSerializeWithoutJsonSerializer() throws IOException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        Object testObject = new TestSerializedObject();
        StringWriter writer = new StringWriter(new OutputStreamWriter(System.out));
        jsonValueSerializer.serialize(testObject, mapper.getJsonFactory().createJsonGenerator(writer), mapper.getSerializerProvider());
        Assert.assertEquals("{\"value\":null}", writer.toString().trim());
    }

    /**
     * Test serialize method with a non-null object and a JsonSerializer that throws an exception
     */
    @Test(expected = JsonMappingException.class)
    public void testSerializeWithExceptionThrowingJsonSerializer() throws IOException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        Object testObject = new TestSerializedObject();
        JsonSerializer<Object> exceptionThrowingJsonSerializer = mapper.findValueSerializer(String[].class, null);
        jsonValueSerializer._valueSerializer = exceptionThrowingJsonSerializer;
        StringWriter writer = new StringWriter(new OutputStreamWriter(System.out));
        jsonValueSerializer.serialize(testObject, mapper.getJsonFactory().createJsonGenerator(writer), mapper.getSerializerProvider());
    }

    /**
     * Test serialize method with a non-null object and a JsonSerializer that returns null
     */
    @Test
    public void testSerializeWithNullJsonSerializer() throws IOException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        Object testObject = new TestSerializedObject();
        JsonSerializer<Object> nullJsonSerializer = null;
        StringWriter writer = new StringWriter(new OutputStreamWriter(System.out));
        jsonValueSerializer.serialize(testObject, mapper.getJsonFactory().createJsonGenerator(writer), mapper.getSerializerProvider());
        Assert.assertEquals("{\"value\":null}", writer.toString().trim());
    }

    /**
     * Test serializeWithType method with null object
     */
    @Test
    public void testSerializeWithTypeNullObject() throws IOException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        StringWriter writer = new StringWriter(new OutputStreamWriter(System.out));
        jsonValueSerializer.serializeWithType(null, mapper.getJsonFactory().createJsonGenerator(writer), mapper.getSerializerProvider(), typeSerializer);
        Assert.assertEquals("null", writer.toString().trim());
    }

    /**
     * Test serializeWithType method with a non-null object and valid JsonSerializer
     */
    @Test
    public void testSerializeWithTypeValidJsonSerializer() throws IOException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        Object testObject = new TestSerializedObject();
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        StringWriter writer = new StringWriter(new OutputStreamWriter(System.out));
        jsonValueSerializer.serializeWithType(testObject, mapper.getJsonFactory().createJsonGenerator(writer), mapper.getSerializerProvider(), typeSerializer);
        Assert.assertEquals("{\"value\":\"testString\"}", writer.toString().trim());
    }

    /**
     * Test serializeWithType method with a non-null object and JsonSerializer that throws an exception
     */
    @Test(expected = JsonMappingException.class)
    public void testSerializeWithTypeExceptionThrowingJsonSerializer() throws IOException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        Object testObject = new TestSerializedObject();
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        JsonSerializer<Object> exceptionThrowingJsonSerializer = mapper.findValueSerializer(String[].class, null);
        jsonValueSerializer._valueSerializer = exceptionThrowingJsonSerializer;
        StringWriter writer = new StringWriter(new OutputStreamWriter(System.out));
        jsonValueSerializer.serializeWithType(testObject, mapper.getJsonFactory().createJsonGenerator(writer), mapper.getSerializerProvider(), typeSerializer);
    }

    /**
     * Test serializeWithType method with a non-null object and JsonSerializer that returns null
     */
    @Test
    public void testSerializeWithTypeNullJsonSerializer() throws IOException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        Object testObject = new TestSerializedObject();
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        JsonSerializer<Object> nullJsonSerializer = null;
        StringWriter writer = new StringWriter(new OutputStreamWriter(System.out));
        jsonValueSerializer.serializeWithType(testObject, mapper.getJsonFactory().createJsonGenerator(writer), mapper.getSerializerProvider(), typeSerializer);
        Assert.assertEquals("{\"value\":null}", writer.toString().trim());
    }

    /**
     * Test getSchema method
     */
    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        SchemaStdImpl schema = mapper.getFactory().getSchemaInstance();
        JsonNode schemaNode = jsonValueSerializer.getSchema(mapper.getSerializerProvider(), jsonValueSerializer.getClass());
        Assert.assertNotNull(schemaNode);
        Assert.assertEquals(JsonSchema.Type.OBJECT, schemaNode.getType());
        Assert.assertFalse(schemaNode.isDefined());
    }

    /**
     * Test acceptJsonFormatVisitor method
     */
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonValueSerializer jsonValueSerializer = createTestJsonValueSerializer();
        JsonSerializerProvider serializerProvider = mapper.getSerializerProvider();
        JsonFormatVisitorWrapper visitor = serializerProvider.getFormatVisitorFactory().findFormatVisitor(JsonFormatVisitorWrapper.class, Object.class, null);
        JsonSchema schema = mapper.getFactory().getSchemaInstance();
        JsonNode schemaNode = jsonValueSerializer.getSchema(serializerProvider, jsonValueSerializer.getClass());
        jsonValueSerializer.acceptJsonFormatVisitor(visitor, schemaNode.getType());
        Assert.assertEquals(JsonFormatVisitorWrapper.WRITE_TYPE_NAME, visitor.getFormatWriter().getFormatFeature().getFeatureName());
    }

    /**
     * Helper method to create a test JsonValueSerializer instance
     */
    private JsonValueSerializer createTestJsonValueSerializer() {
        Class<TestSerializedObject> testClass = TestSerializedObject.class;
        Method method = testClass.getMethod("getTestString");
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(method, valueSerializer, null, false);
        return jsonValueSerializer;
    }

    /**
     * Helper method to create a test AnnotatedMethod instance
     */
    private AnnotatedMethod createTestAnnotatedMethod() {
        Class<TestSerializedObject> testClass = TestSerializedObject.class;
        Method method = testClass.getMethod("getTestString");
        AnnotatedMethod annotatedMethod = mapper.getAnnotationIntrospector().findAnnotated(method, JsonSerialize.class);
        return annotatedMethod;
    }

    /**
     * Test object used to test JsonValueSerializer methods
     */
    public static class TestSerializedObject {
        private String testString;

        public String getTestString() {
            return testString;
        }

        public void setTestString(String testString) {
            this.testString = testString;
        }
    }
}