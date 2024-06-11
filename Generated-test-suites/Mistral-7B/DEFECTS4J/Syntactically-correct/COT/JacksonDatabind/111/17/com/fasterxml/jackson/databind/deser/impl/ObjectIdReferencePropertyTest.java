package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    private ObjectIdInfo objectIdInfo;
    private SettableBeanProperty forward;
    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setup() {
        objectIdInfo = new ObjectIdInfo(null, null, null, null, null);
        forward = new StubSettableBeanProperty();
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testConstructorWithForwardAndObjectIdInfo() {
        // Basic test case
        ObjectIdReferenceProperty actualObjectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(actualObjectIdReferenceProperty);

        // Edge case: null forward
        assertNull(new ObjectIdReferenceProperty(null, objectIdInfo));

        // Edge case: null objectIdInfo
        assertNull(new ObjectIdReferenceProperty(forward, null));

        // Test exception handling when setting null forward
        try {
            new ObjectIdReferenceProperty(null, objectIdInfo);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value cannot be null", e.getMessage());
        }

        // Test exception handling when setting null objectIdInfo
        try {
            new ObjectIdReferenceProperty(forward, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value cannot be null", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithSrcAndDeserializer() {
        // Basic test case
        ObjectIdReferenceProperty actualObjectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, new StubJsonDeserializer<>(), null);
        assertEquals(objectIdReferenceProperty, actualObjectIdReferenceProperty._forward);

        // Edge case: null deserializer
        try {
            new ObjectIdReferenceProperty(objectIdReferenceProperty, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value cannot be null", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithSrcAndNewName() {
        // Basic test case
        PropertyName newName = PropertyName.construct("testProperty");
        ObjectIdReferenceProperty actualObjectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, newName);
        assertEquals(objectIdReferenceProperty, actualObjectIdReferenceProperty._forward);
        assertEquals(newName, actualObjectIdReferenceProperty.getName());

        // Edge case: null newName
        try {
            new ObjectIdReferenceProperty(objectIdReferenceProperty, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value cannot be null", e.getMessage());
        }
    }

    @Test
    public void testWithName() {
        // Basic test case
        PropertyName newName = PropertyName.construct("testProperty");
        ObjectIdReferenceProperty actualObjectIdReferenceProperty = objectIdReferenceProperty.withName(newName);
        assertEquals(newName, actualObjectIdReferenceProperty.getName());

        // Edge case: null newName
        try {
            objectIdReferenceProperty.withName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value cannot be null", e.getMessage());
        }
    }

    @Test
    public void testWithValueDeserializer() {
        // Basic test case
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer<>();
        ObjectIdReferenceProperty actualObjectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, actualObjectIdReferenceProperty._valueDeserializer);

        // Edge case: null deserializer
        try {
            objectIdReferenceProperty.withValueDeserializer(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value cannot be null", e.getMessage());
        }
    }

    @Test
    public void testWithNullProvider() {
        // Basic test case
        NullValueProvider nullValueProvider = new StubNullValueProvider();
        ObjectIdReferenceProperty actualObjectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, actualObjectIdReferenceProperty._nullProvider);

        // Edge case: null nullValueProvider
        try {
            objectIdReferenceProperty.withNullProvider(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value cannot be null", e.getMessage());
        }
    }

    @Test
    public void testFixAccess() {
        // Basic test case
        DeserializationConfig deserializationConfig = new DeserializationConfig();
        objectIdReferenceProperty.fixAccess(deserializationConfig);
        if (forward != null) {
            forward.fixAccess(deserializationConfig);
        }

        // Edge case: null deserializationConfig
        try {
            objectIdReferenceProperty.fixAccess(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value cannot be null", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotation() {
        // Basic test case
        Class<Annotation> annotationClass = Annotation.class;
        Annotation actualAnnotation = objectIdReferenceProperty.getAnnotation(annotationClass);
        assertNull(actualAnnotation);

        // Edge case: null annotationClass
        try {
            objectIdReferenceProperty.getAnnotation(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value cannot be null", e.getMessage());
        }
    }

    @Test
    public void testGetMember() {
        // Basic test case
        AnnotatedMember actualAnnotatedMember = objectIdReferenceProperty.getMember();
        assertNull(actualAnnotatedMember);

        // Edge case: null forward
        ObjectIdReferenceProperty objectIdReferencePropertyNullForward = new ObjectIdReferenceProperty(null, objectIdInfo);
        AnnotatedMember objectIdReferencePropertyNullForwardAnnotatedMember = objectIdReferencePropertyNullForward.getMember();
        assertNull(objectIdReferencePropertyNullForwardAnnotatedMember);
    }

    @Test
    public void testGetCreatorIndex() {
        // Basic test case
        int actualCreatorIndex = objectIdReferenceProperty.getCreatorIndex();
        assertEquals(-1, actualCreatorIndex);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectNode jsonNode = ObjectMapper.getDefaultInstance().createObjectNode();
        jsonNode.put("testProperty", "testValue");
        JsonParser jsonParser = ObjectMapper.getDefaultInstance().treeAsTokens(jsonNode);

        // Basic test case
        Object instance = new Object();
        objectIdReferenceProperty.deserializeAndSet(jsonParser, new DeserializationContext(), instance);
        assertEquals("testValue", instance.getClass().getDeclaredField(_forward.getName()).get(instance));

        // Edge case: null instance
        try {
            objectIdReferenceProperty.deserializeAndSet(jsonParser, new DeserializationContext(), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot find class name for null object", e.getMessage());
        }

        // Edge case: JsonMappingException
        try {
            ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
            ObjectMapper.DefaultTypingResolver typingResolver = new ObjectMapper.DefaultTypingResolver();
            ObjectIdInfo objectIdInfo = new ObjectIdInfo("testId", "testClass", null, null, null, null, null, null, null, null, null, typingResolver, null);
            ObjectIdReferenceProperty objectIdReferencePropertyWithObjectIdInfo = new ObjectIdReferenceProperty(forward, objectIdInfo);
            ObjectMapper objectMapperWithObjectIdReferenceProperty = new ObjectMapper();
            objectMapperWithObjectIdReferenceProperty.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
            JsonParser jsonParserWithObjectIdReferenceProperty = objectMapperWithObjectIdReferenceProperty.treeAsTokens(jsonNode);
            Object instanceWithObjectIdReferenceProperty = new Object();
            objectIdReferencePropertyWithObjectIdInfo.deserializeAndSet(jsonParserWithObjectIdReferenceProperty, new DeserializationContext(), instanceWithObjectIdReferenceProperty);
            ObjectId id = objectIdMapper.parseId("testId");
            throw new JsonMappingException("Failed to deserialize Id", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, id);
        } catch (JsonProcessingException e) {
            assertEquals("Failed to deserialize Id", e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectNode jsonNode = ObjectMapper.getDefaultInstance().createObjectNode();
        jsonNode.put("testProperty", "testValue");
        JsonParser jsonParser = ObjectMapper.getDefaultInstance().treeAsTokens(jsonNode);

        // Basic test case
        Object instance = new Object();
        Object actualObject = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, new DeserializationContext(), instance);
        assertNull(actualObject);
        assertEquals("testValue", instance.getClass().getDeclaredField(_forward.getName()).get(instance));

        // Edge case: null instance
        try {
            ObjectObjectIdReferenceProperty.deserializeSetAndReturn(jsonParser, new DeserializationContext(), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot find class name for null object", e.getMessage());
        }

        // Edge case: JsonMappingException
        try {
            ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
            ObjectMapper.DefaultTypingResolver typingResolver = new ObjectMapper.DefaultTypingResolver();
            ObjectIdInfo objectIdInfo = new ObjectIdInfo("testId", "testClass", null, null, null, null, null, null, null, null, null, typingResolver, null);
            ObjectIdReferenceProperty objectIdReferencePropertyWithObjectIdInfo = new ObjectIdReferenceProperty(forward, objectIdInfo);
            ObjectMapper objectMapperWithObjectIdReferenceProperty = new ObjectMapper();
            objectMapperWithObjectIdReferenceProperty.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
            JsonParser jsonParserWithObjectIdReferenceProperty = objectMapperWithObjectIdReferenceProperty.treeAsTokens(jsonNode);
            Object instanceWithObjectIdReferenceProperty = new Object();
            Object actualObjectWithObjectIdReferenceProperty = objectIdReferencePropertyWithObjectIdInfo.deserializeSetAndReturn(jsonParserWithObjectIdReferenceProperty, new DeserializationContext(), instanceWithObjectIdReferenceProperty);
            ObjectId id = objectIdMapper.parseId("testId");
            throw new JsonMappingException("Failed to deserialize Id", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, id);
        } catch (JsonProcessingException e) {
            assertEquals("Failed to deserialize Id", e.getMessage());
        }
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        // Basic test case
        objectIdReferenceProperty.set(instance, value);
        assertEquals(value, instance.getClass().getDeclaredField(_forward.getName()).get(instance));

        // Edge case: null instance
        try {
            objectIdReferenceProperty.set(null, value);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot find class name for null object", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        // Basic test case
        Object actualObject = objectIdReferenceProperty.setAndReturn(instance, value);
        assertNull(actualObject);
        assertEquals(value, instance.getClass().getDeclaredField(_forward.getName()).get(instance));

        // Edge case: null instance
        try {
            objectIdReferenceProperty.setAndReturn(null, value);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot find class name for null object", e.getMessage());
        }
    }
}

class StubJsonDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return null;
    }

}

class StubNullValueProvider implements NullValueProvider {

    @Override
    public Object getNullValue(DeserializationContext context, BeanProperty property, JsonValue value) throws IOException {
        return null;
    }

}

class StubSettableBeanProperty extends SettableBeanProperty {

    @Override
    public void set(Object instance, Object value) throws IOException {
    }

    @Override
    public Object setAndReturn(Object instance, Object value) throws IOException {
        return null;
    }

}