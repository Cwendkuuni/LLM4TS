package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdReferencePropertyTest {

    @Mock
    private SettableBeanProperty _forward;

    @Mock
    private ObjectIdReferenceProperty.PropertyReferring _referring;

    @Mock
    private JsonDeserializer<?> _deserializer;

    @Mock
    private NullValueProvider _nullValueProvider;

    @Mock
    private PropertyName _newName;

    @Mock
    private ObjectIdInfo _objectIdInfo;

    @Mock
    private AnnotatedMember _annotatedMember;

    @Mock
    private DeserializationContext _deserializationContext;

    @Mock
    private JsonParser _jsonParser;

    @Mock
    private UnresolvedForwardReference _unresolvedForwardReference;

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
    }

    @Test
    public void testWithNullProvider() {
        objectIdReferenceProperty = objectIdReferenceProperty.withNullProvider(_nullValueProvider);
        assertEquals(_nullValueProvider, objectIdReferenceProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        objectIdReferenceProperty.fixAccess(mock(DeserializationConfig.class));
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(_forward.getAnnotation(Annotation.class)).thenReturn(annotation);
        assertEquals(annotation, objectIdReferenceProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        when(_forward.getMember()).thenReturn(_annotatedMember);
        assertEquals(_annotatedMember, objectIdReferenceProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        when(_forward.getCreatorIndex()).thenReturn(1);
        assertEquals(1, objectIdReferenceProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        objectIdReferenceProperty.deserializeAndSet(_jsonParser, _deserializationContext, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        objectIdReferenceProperty.deserializeSetAndReturn(_jsonParser, _deserializationContext, instance);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdReferenceProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdReferenceProperty.setAndReturn(instance, value);
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReference() throws IOException {
        Object id = new Object();
        Object value = new Object();
        _referring.handleResolvedForwardReference(id, value);
    }
}