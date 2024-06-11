package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    private ObjectIdInfo objectIdInfo;
    private SettableBeanProperty forward;
    private ObjectIdReferenceProperty objectIdReferenceProperty;
    private JsonDeserializer<?> jsonDeserializer;
    private NullValueProvider nullValueProvider;

    @Before
    public void setup() {
        objectIdInfo = new ObjectIdInfo("test", "TestClass");
        forward = new SettableBeanProperty(null, null, null);
        jsonDeserializer = new JsonDeserializer<Object>() {};
        nullValueProvider = new JsonNull();
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testConstructorWithForwardAndObjectIdInfo() {
        ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals("Expected ObjectIdReferenceProperty instances to be equal", objectIdReferenceProperty, actual);
    }

    @Test
    public void testConstructorWithSrcDeserAndNva() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, nullValueProvider);
        ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(src, null, null);
        assertEquals("Expected ObjectIdReferenceProperty instances to be equal", objectIdReferenceProperty, actual);
    }

    @Test
    public void testConstructorWithSrcAndNewName() {
        PropertyName newName = PropertyName.construct("testName");
        ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(objectIdReferenceProperty, newName);
        assertEquals("Expected ObjectIdReferenceProperty instances to be equal", objectIdReferenceProperty, actual);
    }

    @Test
    public void testWithName() {
        PropertyName newName = PropertyName.construct("testName");
        ObjectIdReferenceProperty actual = objectIdReferenceProperty.withName(newName);
        assertEquals("Expected ObjectIdReferenceProperty instances to be equal", objectIdReferenceProperty, actual);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<Object>() {};
        ObjectIdReferenceProperty actual = objectIdReferenceProperty.withValueDeserializer(deser);
        assertEquals("Expected ObjectIdReferenceProperty instances to be equal", objectIdReferenceProperty, actual);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new JsonNull();
        ObjectIdReferenceProperty actual = objectIdReferenceProperty.withNullProvider(nva);
        assertEquals("Expected ObjectIdReferenceProperty instances to be equal", objectIdReferenceProperty, actual);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        ObjectIdReferenceProperty actual = objectIdReferenceProperty.fixAccess(config);
        assertEquals("Expected ObjectIdReferenceProperty instances to be equal", objectIdReferenceProperty, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAcls() {
        objectIdReferenceProperty.getAnnotation(null);
        fail("Expected IllegalArgumentException but it didn't throw.");
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = new MyAnnotation();
        AnnotatedMember member = new AnnotatedMember() {};
        when(member.getAnnotation(Annotation.class)).thenReturn(annotation);
        ObjectIdReferenceProperty actual = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals("Expected annotations to be equal", annotation, actual.getAnnotation(Annotation.class));
        verify(member).getAnnotation(Annotation.class);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember actual = objectIdReferenceProperty.getMember();
        assertNotNull("Expected AnnotatedMember not to be null", actual);
    }

    @Test
    public void testGetCreatorIndex() {
        int actual = objectIdReferenceProperty.getCreatorIndex();
        assertEquals("Expected creator index to be 0", 0, actual);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        when(parser.getValueAsToken()).thenReturn(ContextualSerializationTest.createContextualObjectMapper().readTree( "{ \"value\": " + value + " }" ));
        when(context.findBeanProperty(eq(objectIdInfo.getMemberName()))).thenReturn(objectIdReferenceProperty);
        when(objectIdReferenceProperty.deserializeAndSet(parser, context, instance)).thenReturn(value);

        Object result = objectIdReferenceProperty.deserializeAndSet(parser, context, instance);

        assertEquals("Expected value to be set on instance", value, result);
        verify(parser).nextToken();
        verify(parser).getValueAsToken();
        verify(context).findBeanProperty(eq(objectIdInfo.getMemberName()));
        verify(objectIdReferenceProperty).deserializeAndSet(parser, context, instance);
    }

    @Test (expected = UnresolvedForwardReference.class)
    public void testDeserializeSetAndReturnWithUnresolvedForwardReference() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        when(parser.getValueAsToken()).thenReturn(ContextualSerializationTest.createContextualObjectMapper().readTree("{ \"id\": 1 }"));
        when(context.findBeanProperty(eq(objectIdInfo.getMemberName()))).thenReturn(objectIdReferenceProperty);
        ObjectIdReferenceProperty objectIdReferencePropertySpy = spy(objectIdReferenceProperty);
        when(objectIdReferencePropertySpy.deserialize(parser, context)).thenThrow(new UnresolvedForwardReference());

        Object result = objectIdReferenceProperty.deserializeSetAndReturn(parser, context, instance);

        fail("Expected UnresolvedForwardReference but it didn't throw.");
        verify(parser).nextToken();
        verify(parser).getValueAsToken();
        verify(context).findBeanProperty(eq(objectIdInfo.getMemberName()));
        verify(objectIdReferenceProperty).deserialize(parser, context);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdReferenceProperty.set(instance, value);
        assertEquals("Expected value to be set on instance", value, instance);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = objectIdReferenceProperty.setAndReturn(instance, value);
        assertEquals("Expected value to be set on instance and returned", value, result);
    }

    @Test
    public void testPropertyReferring() {
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty parent = objectIdReferenceProperty;
        PropertyReferring actual = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);

        assertSame("Expected PropertyReferring instances to be equal", actual, new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob));
        assertEquals("Expected parent references to be equal", parent, actual._parent);
        assertEquals("Expected ref to be equal", ref, actual._ref);
        assertEquals("Expected type to be equal", type, actual._type);
        assertEquals("Expected ob to be equal", ob, actual._pojo);
    }

    @Test
    public void testHandleResolvedForwardReferenceWithId() throws IOException {
        Object id = new Object();
        Object value = new Object();
        ObjectIdReferenceProperty parent = objectIdReferenceProperty;
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(parent, null, Object.class, new Object());
        when(referring.getId()).thenReturn(id);

        Object result = referring.handleResolvedForwardReference(id, value);

        assertEquals("Expected value to be set on referencing instance", value, parent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleResolvedForwardReferenceWithNullId() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, null, Object.class, new Object());

        referring.handleResolvedForwardReference(null, null);

        fail("Expected IllegalArgumentException but it didn't throw.");
    }
}

class MyAnnotation {}