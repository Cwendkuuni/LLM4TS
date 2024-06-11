package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferenceProperty() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo);
        assertNull(property.getAnnotation(JsonIdentityInfo.class));
        assertNull(property.getMember());
        assertEquals(0, property.getCreatorIndex());
        assertNull(property.getForward());
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
        assertNull(property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyWithForward() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, property.getForward());
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
        assertEquals(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyWithDeserializer() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        JsonDeserializer<String> deserializer = new StdDeserializer<>();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo, deserializer);
        assertEquals(deserializer, property.getValueDeserializer());
        assertNull(property.getNullProvider());
        assertNull(property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyWithNullProvider() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        NullValueProvider nullValueProvider = new StandardNullValueProvider();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo, null, nullValueProvider);
        assertEquals(nullValueProvider, property.getNullProvider());
        assertNull(property.getValueDeserializer());
        assertNull(property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyWithName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null, null, null, "testName");
        assertEquals("testName", property.getName());
        assertEquals(forward, property.getForward());
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
        assertNull(property._objectIdInfo);
    }

    @Test
    public void testObjectIdReferencePropertyFixAccess() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance = new Object();
        property.fixAccess(new DeserializationConfig());
        assertNotNull(property.getForward());
        property.getForward().set(testInstance, "testValue");
        assertEquals("testValue", testInstance.getClass().getField("name").get(testInstance));
    }

    @Test
    public void testObjectIdReferencePropertyGetAnnotation() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Annotation annotation = new JsonIdentityInfo.Name("name");
        property.setAnnotation(annotation);
        assertEquals(annotation, property.getAnnotation(JsonIdentityInfo.class));
    }

    @Test
    public void testObjectIdReferencePropertyGetMember() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance = new Object();
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        when(member.getMember()).thenReturn(testInstance.getClass().getField("name"));
        property.setMember(member);
        assertEquals(member, property.getMember());
    }

    @Test
    public void testObjectIdReferencePropertyGetCreatorIndex() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeAndSet() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance = new Object();
        String testValue = "testValue";
        JsonParser parser = new JsonFactory().createParser("{\"name\": " + testValue + "}");
        Object result = property.deserializeAndSet(parser, new DeserializationContext(), testInstance);
        assertEquals(testValue, testInstance.getClass().getField("name").get(testInstance));
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance = new Object();
        String testValue = "testValue";
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("name", testValue);
        Object result = property.deserializeSetAndReturn(new JsonParser(node), new DeserializationContext(), testInstance);
        assertEquals(testValue, testInstance.getClass().getField("name").get(testInstance));
    }

    @Test
    public void testObjectIdReferencePropertySet() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance = new Object();
        String testValue = "testValue";
        property.set(testInstance, testValue);
        assertEquals(testValue, testInstance.getClass().getField("name").get(testInstance));
    }

    @Test
    public void testObjectIdReferencePropertySetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance = new Object();
        String testValue = "testValue";
        Object result = property.setAndReturn(testInstance, testValue);
        assertEquals(testValue, testInstance.getClass().getField("name").get(testInstance));
        assertEquals(testValue, result);
    }

    @Test
    public void testObjectIdReferencePropertyWithUnresolvedForwardReference() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance1 = new Object();
        Object testInstance2 = new Object();
        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        when(reference.getId()).thenReturn("testId");
        property.deserializeSetAndReturn(new JsonParser(new ObjectMapper().writeValueAsString(testInstance1)), new DeserializationContext(), testInstance2);
        verify(reference).getRoid().appendReferring(isInstanceOf(PropertyReferring.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdReferencePropertyWithoutIdentityInfo() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.IntIdGenerator.class, "id");
        SettableBeanProperty forward = new SettableBeanProperty(Object.class, "name");
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object testInstance = new Object();
        String testValue = "testValue";
        property.deserializeSetAndReturn(new JsonParser(new ObjectMapper().writeValueAsString(testValue)), new DeserializationContext(), testInstance);
    }
}