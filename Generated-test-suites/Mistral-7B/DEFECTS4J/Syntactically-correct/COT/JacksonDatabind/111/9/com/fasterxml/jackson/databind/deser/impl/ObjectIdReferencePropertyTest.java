package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ObjectIdReferencePropertyTest {

    private static Object createTestObject() {
        return new TestObject();
    }

    private static class TestObject {
        private ObjectIdReferenceProperty idRefProperty;
        private Object idRefValue;

        public ObjectIdReferenceProperty getIdRefProperty() {
            return idRefProperty;
        }

        public void setIdRefProperty(ObjectIdReferenceProperty value) {
            this.idRefProperty = value;
        }

        public Object getIdRefValue() {
            return idRefValue;
        }

        public void setIdRefValue(Object value) {
            this.idRefValue = value;
        }
    }

    @org.junit.Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(TestObject.class, new TestObjectDeserializer());
        mapper.registerModule(module);
    }

    // Testing constructor with arguments
    @org.junit.Test
    public void testObjectIdReferencePropertyConstructorWithArguments() throws Exception {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(new SettableBeanProperty(null), new ObjectIdInfo(null));
        assertNotNull(objectIdReferenceProperty);
    }

    @org.junit.Test
    public void testObjectIdReferencePropertyWithName() throws Exception {
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty(null, "testProperty");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(settableBeanProperty, objectIdInfo);
        assertEquals("testProperty", objectIdReferenceProperty.getName());
    }

    @org.junit.Test
    public void testSetValueDeserializer() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();
        Object id = new Object();

        JsonParser jsonParser = mapper.parse(new ObjectMapper().writeValueAsString(new ObjectNode().put("$ref", "/test"))).getParser();
        DeserializationContext context = mapper.getDeserializationContext();
        Object result = objectIdReferenceProperty.deserializeAndSet(jsonParser, context, testObject);
        assertNull(testObject.getIdRefValue());
        assertEquals(new ObjectNode().get("$id"), result);
    }

    @org.junit.Test(expected = UnresolvedForwardReference.class)
    public void testUnresolvedForwardReference() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();
        Object id = new Object();

        JsonParser jsonParser = mapper.parse(new ObjectMapper().writeValueAsString(new ObjectNode().put("$ref", "/nonExistent"))).getParser();
        DeserializationContext context = mapper.getDeserializationContext();
        objectIdReferenceProperty.deserializeAndSet(jsonParser, context, testObject);
    }

    @org.junit.Test
    public void testSetAndReturn() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();
        Object id = new Object();

        Object result = objectIdReferenceProperty.setAndReturn(testObject, id);
        assertEquals(id, result);
        assertEquals(id, testObject.getIdRefValue());
    }

    @org.junit.Test(expected = IOException.class)
    public void testSetNullValueDeserializer() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();

        Object result = objectIdReferenceProperty.deserializeAndSet(null, null, testObject);
        assertNull(testObject.getIdRefValue());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testSetValueDeserializerNull() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();

        objectIdReferenceProperty.deserializeAndSet(null, null, testObject);
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeWithNullValueDeserializer() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();

        Object result = objectIdReferenceProperty.deserialize(null, null);
        assertNull(result);
    }

    @org.junit.Test
    public void testGetAnnotation() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();

        AnnexedClass annexedClassAnnotation = new AnnexedClass();
        Annotation annotation = annexedClassAnnotation;
        Object result = objectIdReferenceProperty.getAnnotation(AnnexedClass.class);
        assertEquals(annotation, result);
    }

    @org.junit.Test
    public void testGetMember() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();

        Field field = TestObject.class.getDeclaredField("idRefProperty");
        Object result = objectIdReferenceProperty.getMember();
        assertEquals(field, result);
    }

    @org.junit.Test
    public void testGetCreatorIndex() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();

        int result = objectIdReferenceProperty.getCreatorIndex();
        assertEquals(-1, result);
    }

    @org.junit.Test
    public void testFixAccess() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();

        DeserializationConfig config = mapper.getDeserializationConfig();
        objectIdReferenceProperty.fixAccess(config);
    }

    @org.junit.Test
    public void testWithValueDeserializer() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();

        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        ObjectIdReferenceProperty result = objectIdReferenceProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, result.getValueDeserializer());
    }

    @org.junit.Test
    public void testWithNullProvider() throws Exception {
        TestObject testObject = createTestObject();
        ObjectIdReferenceProperty objectIdReferenceProperty = testObject.getIdRefProperty();

        NullValueProvider nva = new StubNullValueProvider();
        ObjectIdReferenceProperty result = objectIdReferenceProperty.withNullProvider(nva);
        assertEquals(nva, result.getNullProvider());
    }

    static class TestObjectDeserializer extends JsonDeserializer<TestObject> {
        @Override
        public TestObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TestObject testObject = new TestObject();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.readValue(p, new TypeReference<ObjectNode>() {});
            ObjectIdReferenceProperty idRefProperty = node.findValue("idRefProperty");
            testObject.setIdRefProperty(idRefProperty);
            return testObject;
        }
    }

    static class StubJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    static class StubNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullProviderValue(DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    static class AnnexedClass {
    }
}