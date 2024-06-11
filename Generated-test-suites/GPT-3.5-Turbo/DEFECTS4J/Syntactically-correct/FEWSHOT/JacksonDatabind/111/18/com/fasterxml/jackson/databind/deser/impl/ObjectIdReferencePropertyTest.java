
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.mockito.Mockito.*;

public class ObjectIdReferencePropertyTest {

    @Mock
    private SettableBeanProperty forward;

    @Mock
    private ObjectIdInfo objectIdInfo;

    @Mock
    private JsonDeserializer jsonDeserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private Object instance;

    @Captor
    private ArgumentCaptor<ObjectIdReferenceProperty.PropertyReferring> propertyReferringCaptor;

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(forward, objectIdReferenceProperty._forward);
        Assert.assertEquals(objectIdInfo, objectIdReferenceProperty._objectIdInfo);
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdReferenceProperty copy = new ObjectIdReferenceProperty(objectIdReferenceProperty, jsonDeserializer, nullValueProvider);
        Assert.assertEquals(objectIdReferenceProperty._forward, copy._forward);
        Assert.assertEquals(objectIdReferenceProperty._objectIdInfo, copy._objectIdInfo);
        Assert.assertEquals(jsonDeserializer, copy._valueDeserializer);
        Assert.assertEquals(nullValueProvider, copy._nullProvider);
    }

    @Test
    public void testCopyConstructorWithNewName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdReferenceProperty copy = new ObjectIdReferenceProperty(objectIdReferenceProperty, newName);
        Assert.assertEquals(objectIdReferenceProperty._forward, copy._forward);
        Assert.assertEquals(objectIdReferenceProperty._objectIdInfo, copy._objectIdInfo);
        Assert.assertEquals(newName, copy.getFullName());
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdReferenceProperty copy = (ObjectIdReferenceProperty) objectIdReferenceProperty.withName(newName);
        Assert.assertEquals(objectIdReferenceProperty.getClass(), copy.getClass());
        Assert.assertEquals(objectIdReferenceProperty._forward, copy._forward);
        Assert.assertEquals(objectIdReferenceProperty._objectIdInfo, copy._objectIdInfo);
        Assert.assertEquals(newName, copy.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty copy = (ObjectIdReferenceProperty) objectIdReferenceProperty.withValueDeserializer(jsonDeserializer);
        Assert.assertEquals(objectIdReferenceProperty.getClass(), copy.getClass());
        Assert.assertEquals(objectIdReferenceProperty._forward, copy._forward);
        Assert.assertEquals(objectIdReferenceProperty._objectIdInfo, copy._objectIdInfo);
        Assert.assertEquals(jsonDeserializer, copy._valueDeserializer);
        Assert.assertEquals(objectIdReferenceProperty._nullProvider, copy._nullProvider);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty copy = (ObjectIdReferenceProperty) objectIdReferenceProperty.withNullProvider(nullValueProvider);
        Assert.assertEquals(objectIdReferenceProperty.getClass(), copy.getClass());
        Assert.assertEquals(objectIdReferenceProperty._forward, copy._forward);
        Assert.assertEquals(objectIdReferenceProperty._objectIdInfo, copy._objectIdInfo);
        Assert.assertEquals(objectIdReferenceProperty._valueDeserializer, copy._valueDeserializer);
        Assert.assertEquals(nullValueProvider, copy._nullProvider);
    }

    @Test
    public void testFixAccess() {
        objectIdReferenceProperty.fixAccess(deserializationContext);
        verify(forward).fixAccess(deserializationContext);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> annotationClass = Annotation.class;
        when(forward.getAnnotation(annotationClass)).thenReturn(null);
        Assert.assertNull(objectIdReferenceProperty.getAnnotation(annotationClass));
        verify(forward).getAnnotation(annotationClass);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(forward.getMember()).thenReturn(annotatedMember);
        Assert.assertEquals(annotatedMember, objectIdReferenceProperty.getMember());
        verify(forward).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        when(forward.getCreatorIndex()).thenReturn(0);
        Assert.assertEquals(0, objectIdReferenceProperty.getCreatorIndex());
        verify(forward).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        int rawId = 1;
        int rawValue = 2;
        propertyReferringCaptor = ArgumentCaptor.forClass(ObjectIdReferenceProperty.PropertyReferring.class);
        
        when(jsonParser.readValueAs(int.class)).thenReturn(rawId);
        when(forward.getMember()).thenReturn(mock(AnnotatedMember.class));
        doAnswer(invocation -> {
            ObjectIdReferenceProperty.PropertyReferring propertyReferring = invocation.getArgument(0);
            propertyReferring.handleResolvedForwardReference(rawId, rawValue);
            return null;
        }).when(forward).set(isA(Object.class), eq(rawValue));
        
        objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(forward).set(eq(instance), eq(rawValue));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithException() throws IOException {
        when(jsonParser.readValueAs(int.class)).thenThrow(new IOException());
        objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        int rawId = 1;
        int rawValue = 2;
        propertyReferringCaptor = ArgumentCaptor.forClass(ObjectIdReferenceProperty.PropertyReferring.class);
        
        when(jsonParser.readValueAs(int.class)).thenReturn(rawId);
        when(forward.getMember()).thenReturn(mock(AnnotatedMember.class));
        doAnswer(invocation -> {
            ObjectIdReferenceProperty.PropertyReferring propertyReferring = invocation.getArgument(0);
            propertyReferring.handleResolvedForwardReference(rawId, rawValue);
            return null;
        }).when(forward).setAndReturn(isA(Object.class), eq(rawValue));
        
        objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(forward).setAndReturn(eq(instance), eq(rawValue));
    }

    @Test
    public void testSet() throws IOException {
        Object value = new Object();
        objectIdReferenceProperty.set(instance, value);
        verify(forward).set(eq(instance), eq(value));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = new Object();
        objectIdReferenceProperty.setAndReturn(instance, value);
        verify(forward).setAndReturn(eq(instance), eq(value));
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReferenceWithMatchingId() throws IOException {
        int id = 1;
        int value = 2;
        int anotherValue = 3;
        ObjectIdReferenceProperty.PropertyReferring propertyReferring =
                new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, mock(UnresolvedForwardReference.class), instance.getClass(), instance);

        propertyReferring.appendReferring(propertyReferring);
        propertyReferring.addToMap(id, value);
        propertyReferring.handleResolvedForwardReference(id, anotherValue);

        verify(forward).set(eq(instance), eq(anotherValue));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyReferringHandleResolvedForwardReferenceWithNonMatchingId() throws IOException {
        int id = 1;
        int value = 2;
        ObjectIdReferenceProperty.PropertyReferring propertyReferring =
                new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, mock(UnresolvedForwardReference.class), instance.getClass(), instance);

        propertyReferring.appendReferring(propertyReferring);
        propertyReferring.addToMap(id, value);

        propertyReferring.handleResolvedForwardReference(2, value);
    }

}
