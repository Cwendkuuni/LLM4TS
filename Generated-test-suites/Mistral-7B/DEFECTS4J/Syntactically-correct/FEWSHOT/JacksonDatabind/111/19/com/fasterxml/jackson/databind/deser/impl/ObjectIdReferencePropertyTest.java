package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testObjectIdReferencePropertyConstruction() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        assertNull(property._forward);
        assertNull(property._objectIdInfo);
        assertEquals(0, property.hashCode());

        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectIdInfo(new AnnotatedClass(Object.class));
        ObjectIdReferenceProperty propertyWithObjectIdInfo = new ObjectIdReferenceProperty(null, objectIdInfo);
        assertNull(propertyWithObjectIdInfo._forward);
        assertNotNull(propertyWithObjectIdInfo._objectIdInfo);
        assertEquals(System.identityHashCode(propertyWithObjectIdInfo), propertyWithObjectIdInfo.hashCode());

        SettableBeanProperty forward = new SimpleValueProperty("forward", String.class);
        ObjectIdReferenceProperty propertyWithForward = new ObjectIdReferenceProperty(forward, null);
        assertEquals(forward, propertyWithForward._forward);
        assertNull(propertyWithForward._objectIdInfo);
        assertEquals(System.identityHashCode(propertyWithForward), propertyWithForward.hashCode());

        ObjectIdReferenceProperty propertyWithForwardAndObjectIdInfo = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertEquals(forward, propertyWithForwardAndObjectIdInfo._forward);
        assertNotNull(propertyWithForwardAndObjectIdInfo._objectIdInfo);
        assertEquals(System.identityHashCode(propertyWithForwardAndObjectIdInfo), propertyWithForwardAndObjectIdInfo.hashCode());
    }

    @Test
    public void testObjectIdReferencePropertyFixAccess() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectIdInfo(new AnnotatedClass(Object.class));
        SettableBeanProperty forward = new SimpleValueProperty("forward", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object obj = new Object();
        JsonParser parser = createMockJsonParser();
        DeserializationContext context = createMockContext();

        property.fixAccess(context);
        verify(context).findBeanProperty(eq(property.getName()));
    }

    @Test
    public void testObjectIdReferencePropertyGetAnnotation() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectIdInfo(new AnnotatedClass(Object.class));
        SettableBeanProperty forward = new SimpleValueProperty("forward", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Class<Annotation> annotationType = Mockito.mock(Annotation.class);
        assertEquals(forward.getAnnotation(annotationType), property.getAnnotation(annotationType));
    }

    @Test
    public void testObjectIdReferencePropertyGetMember() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectIdInfo(new AnnotatedClass(Object.class));
        SettableBeanProperty forward = new SimpleValueProperty("forward", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        assertEquals(forward.getMember(), property.getMember());
    }

    @Test
    public void testObjectIdReferencePropertyGetCreatorIndex() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectIdInfo(new AnnotatedClass(Object.class));
        SettableBeanProperty forward = new SimpleValueProperty("forward", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        assertEquals(forward.getCreatorIndex(), property.getCreatorIndex());
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeAndSet() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectIdInfo(new AnnotatedClass(Object.class));
        SettableBeanProperty forward = new SimpleValueProperty("forward", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object obj = new Object();
        JsonParser parser = createMockJsonParser();
        DeserializationContext context = createMockContext();

        Object value = "value";
        when(parser.getValueAsString()).thenReturn((String) value);
        property.deserializeAndSet(parser, context, obj);
        verify(obj).setValue(value);
    }

    @Test
    public void testObjectIdReferencePropertyDeserializeSetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectIdInfo(new AnnotatedClass(Object.class));
        SettableBeanProperty forward = new SimpleValueProperty("forward", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object obj = new Object();
        JsonParser parser = createMockJsonParser();
        DeserializationContext context = createMockContext();

        Object value = "value";
        when(parser.getValueAsString()).thenReturn((String) value);
        Object result = property.deserializeSetAndReturn(parser, context, obj);
        verify(obj).setValue(value);
        assertEquals(value, result);
    }

    @Test
    public void testObjectIdReferencePropertySet() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectIdInfo(new AnnotatedClass(Object.class));
        SettableBeanProperty forward = new SimpleValueProperty("forward", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object obj = new Object();
        Object value = "value";

        property.set(obj, value);
        assertEquals(value, obj.getValue());
    }

    @Test
    public void testObjectIdReferencePropertySetAndReturn() throws IOException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectIdInfo(new AnnotatedClass(Object.class));
        SettableBeanProperty forward = new SimpleValueProperty("forward", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

        Object obj = new Object();
        Object value = "value";

        Object result = property.setAndReturn(obj, value);
        assertEquals(value, obj.getValue());
        assertEquals(value, result);
    }

    @Test
    public void testObjectIdReferencePropertyWithNullProvider() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectIdInfo(new AnnotatedClass(Object.class));
        SettableBeanProperty forward = new SimpleValueProperty("forward", String.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null, null);

        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        ObjectIdReferenceProperty propertyWithNullProvider = new ObjectIdReferenceProperty(property, null, nva);

        assertEquals(property, propertyWithNullProvider);
    }

    @Test
    public void testObjectIdReferencePropertyWithValueDeserializer() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator().generateObjectIdInfo(new AnnotatedClass(Object.class));
        SettableBeanProperty forward = new SimpleValueProperty("forward", String.class);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, null, null, deserializer);

        JsonDeserializer<Object> deserializerWithSameClass = Mockito.mock(JsonDeserializer.class);
        when(deserializerWithSameClass.getValueClass()).thenReturn(deserializer.getValueClass());
        assertEquals(deserializerWithSameClass, property.getDeserializer());
    }

    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty property = Mockito.mock(ObjectIdReferenceProperty.class);
        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        Class<?> type = Mockito.mock(Class.class);
        Object obj = new Object();

        PropertyReferring referencing = new ObjectIdReferenceProperty.PropertyReferring(property, reference, type, obj);

        Object id = Mockito.mock(Object.class);
        Object value = "value";

        when(reference.getId()).thenReturn(id);
        referencing.handleResolvedForwardReference(id, value);
        assertEquals(value, property.get(obj));
    }
}