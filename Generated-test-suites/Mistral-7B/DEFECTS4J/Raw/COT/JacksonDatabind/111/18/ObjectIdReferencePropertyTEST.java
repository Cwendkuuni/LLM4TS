package com.example.objectidreferenceproperty; // Replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdReferencePropertyTest {

    @SuppressWarnings("unchecked")
    private static ObjectIdReferenceProperty createObjectIdReferenceProperty(JsonDeserializer<?> deserializer) {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(TestPojo.class, "id");
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(
                new TestSettableBeanProperty(), objectIdInfo, deserializer);
        return objectIdReferenceProperty;
    }

    @JsonDeserialize(using = TestJsonDeserializer.class)
    static class TestPojo {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    static class TestJsonDeserializer extends JsonDeserializer<TestPojo> {
        @Override
        public TestPojo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String id = p.getValueAsString();
            TestPojo testPojo = new TestPojo();
            testPojo.setId(id);
            return testPojo;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testConstructorWithForward() {
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer);
        assertNotNull(objectIdReferenceProperty);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testConstructorWithSameParameters() {
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty1 = createObjectIdReferenceProperty(deserializer);
        ObjectIdReferenceProperty objectIdReferenceProperty2 = new ObjectIdReferenceProperty(objectIdReferenceProperty1, deserializer, null);
        assertEquals(objectIdReferenceProperty1, objectIdReferenceProperty2);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testConstructorWithNewName() {
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty1 = createObjectIdReferenceProperty(deserializer);
        PropertyName newName = PropertyName.construct("newName");
        ObjectIdReferenceProperty objectIdReferenceProperty2 = new ObjectIdReferenceProperty(objectIdReferenceProperty1, newName);
        assertNotSame(objectIdReferenceProperty1, objectIdReferenceProperty2);
        assertEquals(newName, objectIdReferenceProperty2.getName());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testWithName() {
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer);
        PropertyName newName = PropertyName.construct("newName");
        ObjectIdReferenceProperty objectIdReferencePropertyWithNewName = objectIdReferenceProperty.withName(newName);
        assertEquals(newName, objectIdReferencePropertyWithNewName.getName());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testWithValueDeserializer() {
        JsonDeserializer<TestPojo> deserializer1 = new TestJsonDeserializer();
        JsonDeserializer<TestPojo> deserializer2 = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer1);
        ObjectIdReferenceProperty objectIdReferencePropertyWithNewDeserializer = objectIdReferenceProperty.withValueDeserializer(deserializer2);
        assertNotSame(deserializer1, deserializer2);
        assertEquals(deserializer2, objectIdReferencePropertyWithNewDeserializer.getValueDeserializer());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider1 = new TestNullValueProvider();
        NullValueProvider nullValueProvider2 = new TestNullValueProvider();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(null);
        ObjectIdReferenceProperty objectIdReferencePropertyWithNewNullProvider = objectIdReferenceProperty.withNullProvider(nullValueProvider2);
        assertNotSame(nullValueProvider1, nullValueProvider2);
        assertEquals(nullValueProvider2, objectIdReferencePropertyWithNewNullProvider.getNullProvider());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testFixAccess() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser(new ObjectMapper().writeValueAsString(new TestPojo()));
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer);
        Object testPojo = new Object();
        objectIdReferenceProperty.fixAccess(new DeserializationConfig(parser.getCodec()));
        objectIdReferenceProperty.deserializeAndSet(parser, new DeserializationContext(parser), testPojo);
        assertEquals("", testPojo.getClass().getDeclaredField("id").get(testPojo).toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testGetAnnotation() {
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer);
        Class<JsonDeserializer<?>> jsonDeserializerClass = JsonDeserializer.class;
        assertEquals(jsonDeserializerClass, objectIdReferenceProperty.getAnnotation(jsonDeserializerClass));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testGetMember() {
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer);
        Field idField = TestPojo.class.getDeclaredField("id");
        assertEquals(idField, objectIdReferenceProperty.getMember());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testGetCreatorIndex() {
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer);
        assertEquals(0, objectIdReferenceProperty.getCreatorIndex());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(new ObjectMapper().writeValueAsString(new TestPojo("testId")));
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer);
        Object testPojo = new Object();
        objectIdReferenceProperty.deserializeAndSet(parser, new DeserializationContext(parser), testPojo);
        assertEquals("testId", testPojo.getClass().getDeclaredField("id").get(testPojo).toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(new ObjectMapper().writeValueAsString(new TestPojo("testId")));
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer);
        Object testPojo = new Object();
        Object result = objectIdReferenceProperty.deserializeSetAndReturn(parser, new DeserializationContext(parser), testPojo);
        assertEquals(testPojo, result);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = UnresolvedForwardReference.class)
    public void testDeserializeSetAndReturnWithUnresolvedForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(new ObjectMapper().writeValueAsString(new ObjectNode().put("testId", null)));
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer);
        Object testPojo = new Object();
        Object result = objectIdReferenceProperty.deserializeSetAndReturn(parser, new DeserializationContext(parser), testPojo);
        fail("Expected UnresolvedForwardReference exception");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectParser objectParser = mapper.getFactory().getParser("{\"id\": \"testId\"}");
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer);
        Object testPojo = new Object();
        objectIdReferenceProperty.set(testPojo, objectParser.readValueAs(TestPojo.class));
        assertEquals("testId", testPojo.getClass().getDeclaredField("id").get(testPojo).toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectParser objectParser = mapper.getFactory().getParser("{\"id\": \"testId\"}");
        JsonDeserializer<TestPojo> deserializer = new TestJsonDeserializer();
        ObjectIdReferenceProperty objectIdReferenceProperty = createObjectIdReferenceProperty(deserializer);
        Object testPojo = new Object();
        Object result = objectIdReferenceProperty.setAndReturn(testPojo, objectParser.readValueAs(TestPojo.class));
        assertEquals(testPojo, result);
    }

    static class TestSettableBeanProperty extends SettableBeanProperty {
        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            return super.setAndReturn(instance, value);
        }
    }

    static class TestNullValueProvider implements NullValueProvider {
    }
}