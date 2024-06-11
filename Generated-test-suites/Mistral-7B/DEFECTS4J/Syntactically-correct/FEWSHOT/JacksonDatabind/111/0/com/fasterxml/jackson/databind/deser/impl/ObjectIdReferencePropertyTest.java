package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.Field;

public class ObjectIdReferencePropertyTest {
    private static final ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
    private static final JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
    private static final NullValueProvider nullValueProvider = mock(NullValueProvider.class);
    private static final SettableBeanProperty forward = spy(SettableBeanProperty.class);
    private static final ObjectIdReferenceProperty objectIdReferenceProperty;

    static {
        try {
            Field field = SettableBeanProperty.class.getDeclaredField("_forward");
            field.setAccessible(true);
            field.set(forward, null);
            objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConstructorWithObjectIdInfo() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertSame(objectIdInfo, property._objectIdInfo);
    }

    @Test
    public void testConstructorWithJsonDeserializer() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, nullValueProvider);
        assertSame(objectIdReferenceProperty, property._forward);
        assertSame(jsonDeserializer, property._valueDeserializer);
        assertSame(nullValueProvider, property._nullProvider);
    }

    @Test
    public void testConstructorWithPropertyName() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(objectIdReferenceProperty, new PropertyName("name"));
        assertSame(objectIdReferenceProperty, property._forward);
        assertSame("name", property.getName());
    }

    @Test
    public void testWithName() {
        ObjectIdReferenceProperty property = objectIdReferenceProperty.withName(new PropertyName("name"));
        assertSame(objectIdReferenceProperty, property);
        assertSame("name", property.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty property = objectIdReferenceProperty.withValueDeserializer(jsonDeserializer);
        assertSame(jsonDeserializer, property._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = objectIdReferenceProperty.withNullProvider(nullValueProvider);
        assertSame(nullValueProvider, property._nullProvider);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationContext.class);
        objectIdReferenceProperty.fixAccess(config);
        verify(forward).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        assertSame(annotation, objectIdReferenceProperty.getAnnotation(annotation.getClass()));
        verify(forward).getAnnotation(annotation.getClass());
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        assertSame(member, objectIdReferenceProperty.getMember());
        verify(forward).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        assertSame(-1, objectIdReferenceProperty.getCreatorIndex());
        verify(forward).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonDeserializer.deserialize(parser, context)).thenReturn(value);

        objectIdReferenceProperty.deserializeAndSet(parser, context, instance);
        verify(forward).set(instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonDeserializer.deserialize(parser, context)).thenReturn(value);

        Object result = objectIdReferenceProperty.deserializeSetAndReturn(parser, context, instance);
        assertSame(value, result);
        verify(forward).setAndReturn(instance, value);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        Object id = new Object();
        Object value = new Object();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, jsonDeserializer, nullValueProvider) {
            @Override
            public void handleResolvedForwardReference(Object id, Object value) throws IOException {
                super.handleResolvedForwardReference(id, value);
            }
        };

        property.handleResolvedForwardReference(id, value);
        property.set(_pojo, value);
    }
}