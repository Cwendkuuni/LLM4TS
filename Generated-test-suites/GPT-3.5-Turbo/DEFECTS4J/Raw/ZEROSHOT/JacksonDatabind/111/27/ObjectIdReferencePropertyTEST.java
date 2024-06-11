package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty objectIdReferenceProperty;
    
    @Mock
    private SettableBeanProperty forward;

    @Mock
    private ObjectIdInfo objectIdInfo;

    @Mock
    private JsonDeserializer<?> deserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private DeserializationContext context;

    @Mock
    private Object instance;

    @Mock
    private JsonParser parser;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");

        SettableBeanProperty result = objectIdReferenceProperty.withName(newName);

        assertNotNull(result);
        assertEquals(newName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        SettableBeanProperty result = objectIdReferenceProperty.withValueDeserializer(deserializer);

        assertNotNull(result);
        assertEquals(deserializer, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty result = objectIdReferenceProperty.withNullProvider(nullValueProvider);

        assertNotNull(result);
        assertEquals(nullValueProvider, result.getNullValueProvider());
    }
    
    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);

        objectIdReferenceProperty.fixAccess(config);

        verify(forward, times(1)).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = mock(Class.class);

        when(forward.getAnnotation(acls)).thenReturn(null);

        Annotation result = objectIdReferenceProperty.getAnnotation(acls);

        assertNull(result);
        
        verify(forward, times(1)).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);

        when(forward.getMember()).thenReturn(member);

        AnnotatedMember result = objectIdReferenceProperty.getMember();

        assertNotNull(result);
        assertEquals(member, result);
        
        verify(forward, times(1)).getMember();
    }
    
    @Test
    public void testGetCreatorIndex() {
        when(forward.getCreatorIndex()).thenReturn(1);

        int result = objectIdReferenceProperty.getCreatorIndex();

        assertEquals(1, result);

        verify(forward, times(1)).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(forward.deserialize(p, ctxt)).thenReturn("value");

        objectIdReferenceProperty.deserializeAndSet(parser, context, instance);

        verify(forward, times(1)).deserialize(parser, context);
        verify(forward, times(1)).set(instance, "value");
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(forward.setAndReturn(instance, "value")).thenReturn("result");

        Object result = objectIdReferenceProperty.deserializeSetAndReturn(parser, context, instance);

        assertEquals("result", result);

        verify(forward, times(1)).setAndReturn(instance, "value");
    }

    @Test
    public void testDeserializeSetAndReturnWithUnresolvedForwardReference() throws IOException {
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        ObjectIdReferenceProperty.PropertyReferring referring = mock(ObjectIdReferenceProperty.PropertyReferring.class);
        
        when(context.isEnabled(JsonReadFeature.IGNORE_UNDEFINED)).thenReturn(true);
        when(forward.deserialize(parser, context)).thenThrow(new UnresolvedForwardReference(reference));
        when(reference.getRoid()).thenReturn(referring);
        when(reference.getRoid().appendReferring(any())).thenReturn(null);

        objectIdReferenceProperty.deserializeSetAndReturn(parser, context, instance);

        verify(forward, times(1)).deserialize(parser, context);
        verify(reference.getRoid(), times(1)).appendReferring(any());
        verify(forward, never()).set(instance, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyReferringHandleResolvedForwardReferenceWithInvalidId() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, null, String.class, instance);

        referring.handleResolvedForwardReference("id", "value");
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, null, String.class, instance);

        referring.setId("id");
        referring.handleResolvedForwardReference("id", "value");

        verify(forward, times(1)).set(instance, "value");
    }
}