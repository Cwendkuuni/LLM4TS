package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdReferencePropertyTest {

    @Mock
    private ObjectIdInfo objectIdInfo;

    @Mock
    private SettableBeanProperty forward;

    @Mock
    private JsonDeserializer<?> deserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private Object instance;

    @Mock
    private Object value;

    @Mock
    private Object id;

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setup() {
        when(forward.getMember()).thenReturn(Mockito.mock(AnnotatedMember.class));
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testConstructorWithObjectIdInfo() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
        assertNotNull(objectIdReferenceProperty);
        assertSame(forward, objectIdReferenceProperty._forward);
        assertSame(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testConstructorWithForwardReference() {
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, deserializer, nullValueProvider);
        assertNotNull(objectIdReferenceProperty);
        assertSame(forward, objectIdReferenceProperty._forward);
        assertSame(deserializer, objectIdReferenceProperty._valueDeserializer);
        assertSame(nullValueProvider, objectIdReferenceProperty._nullProvider);
    }

    @Test
    public void testConstructorWithNewName() {
        PropertyName newName = Mockito.mock(PropertyName.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(objectIdReferenceProperty, newName);
        assertNotNull(objectIdReferenceProperty);
        assertSame(objectIdReferenceProperty, objectIdReferenceProperty._forward);
        assertSame(newName, objectIdReferenceProperty.getName());
    }

    @Test
    public void testGetAnnotation() {
        when(forward.getAnnotation(Mockito.any(Class.class))).thenReturn(Mockito.mock(Annotation.class));
        Annotation annotation = objectIdReferenceProperty.getAnnotation(Mockito.mock(Class.class));
        assertNotNull(annotation);
        assertSame(forward.getAnnotation(Mockito.mock(Class.class)), annotation);
    }

    @Test
    public void testFixAccess() {
        objectIdReferenceProperty.fixAccess(Mockito.mock(DeserializationConfig.class));
        verify(forward).fixAccess(Mockito.mock(DeserializationConfig.class));
    }

    @Test
    public void testWithName() {
        PropertyName newName = Mockito.mock(PropertyName.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = objectIdReferenceProperty.withName(newName);
        assertNotNull(objectIdReferenceProperty);
        assertNotSame(objectIdReferenceProperty, objectIdReferenceProperty._forward);
        assertSame(newName, objectIdReferenceProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = objectIdReferenceProperty.withValueDeserializer(deserializer);
        assertNotNull(objectIdReferenceProperty);
        assertNotSame(objectIdReferenceProperty, objectIdReferenceProperty._forward);
        assertSame(deserializer, objectIdReferenceProperty._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        ObjectIdReferenceProperty objectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(nullValueProvider);
        assertNotNull(objectIdReferenceProperty);
        assertNotSame(objectIdReferenceProperty, objectIdReferenceProperty._forward);
        assertSame(nullValueProvider, objectIdReferenceProperty._nullProvider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullInstance() throws IOException {
        objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, null);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(jsonParser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
        ObjectNode objectNode = Mockito.mock(ObjectNode.class);
        when(jsonParser.readValueAsTree(Mockito.anyInt().eq(0))).thenReturn(objectNode);
        when(deserializationContext.findBeanProperty(Mockito.any(Class.class), Mockito.any(PropertyName.class)))
                .thenReturn(forward);
        when(forward.deserializer()).thenReturn(deserializer);
        when(deserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value);
        objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        verify(instance).set(value);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
        when(jsonParser.getValueAsToken()).thenReturn(Mockito.mock(JsonToken.class));
        when(deserializationContext.findBeanProperty(Mockito.any(Class.class), Mockito.any(PropertyName.class)))
                .thenReturn(objectIdReferenceProperty);
        when(jsonParser.getValueAsId()).thenReturn(id);
        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        when(deserializationContext.handleUnresolvedForwardReference(Mockito.any(ObjectIdInfo.class), Mockito.any(Object.class), Mockito.any(ObjectIdInfo.class), Mockito.any(Class.class), Mockito.any(PropertyName.class), Mockito.any(JsonParser.class)))
                .thenThrow(reference);
        objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        verify(instance).set(Mockito.never());
        PropertyReferring propertyReferring = new PropertyReferring(objectIdReferenceProperty, reference, instance.getClass(), instance);
        propertyReferring.handleResolvedForwardReference(id, value);
        verify(instance).set(value);
    }

    @Test
    public void testSet() throws IOException {
        objectIdReferenceProperty.set(instance, value);
        verify(forward).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        when(forward.setAndReturn(instance, value)).thenReturn(null);
        Object result = objectIdReferenceProperty.setAndReturn(instance, value);
        assertNull(result);
    }
}