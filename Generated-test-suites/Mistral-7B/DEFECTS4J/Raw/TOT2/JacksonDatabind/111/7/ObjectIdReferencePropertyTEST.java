package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonFactory;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectId;

public class ObjectIdReferencePropertyTest {
    @Test
    public void testConstructorWithThreeArguments() {
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedMember(null, null), new JsonDeserializer<>(), null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(objectIdReferenceProperty);
        assertEquals(forward, objectIdReferenceProperty._forward);
        assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentsInConstructor() {
        SettableBeanProperty forward = null;
        ObjectIdInfo objectIdInfo = null;
        new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testSetterMethods() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, null, null);
        assertNotNull(objectIdReferenceProperty);
        DeserializationConfig config = new DeserializationConfig();
        objectIdReferenceProperty.fixAccess(config);
        Object annotation = new Object() {};
        assertEquals(annotation.getClass(), objectIdReferenceProperty.getAnnotation(annotation.getClass()));
        AnnotatedMember annotatedMember = new AnnotatedMember(null, null);
        assertEquals(annotatedMember, objectIdReferenceProperty.getMember());
        int creatorIndex = 1;
        assertEquals(creatorIndex, objectIdReferenceProperty.getCreatorIndex());
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{}");
        Object instance = new Object() {};
        objectIdReferenceProperty.deserializeAndSet(parser, null, instance);
        Object deserializedValue = new Object() {};
        assertEquals(deserializedValue, objectIdReferenceProperty.deserializeSetAndReturn(parser, null, instance));
        Object value = new Object() {};
        objectIdReferenceProperty.set(instance, value);
        assertEquals(value, instance.getClass().getDeclaredField("").get(instance));
        Object returnedValue = objectIdReferenceProperty.setAndReturn(instance, value);
        assertEquals(value, returnedValue);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentsInSetterMethods() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(null, null, null);
        DeserializationConfig config = null;
        objectIdReferenceProperty.fixAccess(config);
        Object annotation = null;
        assertEquals(annotation.getClass(), objectIdReferenceProperty.getAnnotation(annotation.getClass()));
        AnnotatedMember annotatedMember = null;
        assertEquals(annotatedMember, objectIdReferenceProperty.getMember());
        int creatorIndex = 0;
        assertEquals(creatorIndex, objectIdReferenceProperty.getCreatorIndex());
        JsonFactory factory = null;
        JsonParser parser = null;
        Object instance = null;
        objectIdReferenceProperty.deserializeAndSet(parser, null, instance);
        Object deserializedValue = null;
        assertEquals(deserializedValue, objectIdReferenceProperty.deserializeSetAndReturn(parser, null, instance));
        Object value = null;
        objectIdReferenceProperty.set(instance, value);
        assertNull(instance.getClass().getDeclaredField("").get(instance));
        Object returnedValue = objectIdReferenceProperty.setAndReturn(instance, value);
        assertNull(returnedValue);
    }

    @Test
    public void testPropertyReferring() {
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedMember(null, null), new JsonDeserializer<>(), null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object pojo = new Object() {};
        UnresolvedForwardReference reference = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, reference, type, pojo);
        Object id = new ObjectId();
        Object value = new Object() {};
        propertyReferring.handleResolvedForwardReference(id, value);
        assertEquals(value, pojo.getClass().getDeclaredField("").get(pojo));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArgumentsInPropertyReferringHandleResolvedForwardReference() {
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedMember(null, null), new JsonDeserializer<>(), null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object pojo = new Object() {};
        UnresolvedForwardReference reference = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, reference, type, pojo);
        Object id = null;
        Object value = new Object() {};
        propertyReferring.handleResolvedForwardReference(id, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullIdInPropertyReferringHandleResolvedForwardReference() {
        SettableBeanProperty forward = new SettableBeanProperty(new AnnotatedMember(null, null), new JsonDeserializer<>(), null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        Object pojo = new Object() {};
        UnresolvedForwardReference reference = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, reference, type, pojo);
        Object id = new ObjectId();
        Object value = null;
        propertyReferring.handleResolvedForwardReference(id, value);
    }
}