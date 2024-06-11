package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ObjectIdReferencePropertyTest {

    @Mock
    private SettableBeanProperty forward;

    private ObjectIdInfo objectIdInfo;

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectIdInfo = new ObjectIdInfo(PropertyName.NO_NAME, null, null);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void withNameTest() {
        PropertyName newName = PropertyName.construct("newName");
        SettableBeanProperty result = objectIdReferenceProperty.withName(newName);
        assertEquals("newName", result.getName().getSimpleName());
    }

    @Test
    public void withValueDeserializerTest() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        SettableBeanProperty result = objectIdReferenceProperty.withValueDeserializer(deser);
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void withNullProviderTest() {
        NullValueProvider nvp = mock(NullValueProvider.class);
        SettableBeanProperty result = objectIdReferenceProperty.withNullProvider(nvp);
        assertEquals(nvp, result.getNullValueProvider());
    }

    @Test
    public void fixAccessTest() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        objectIdReferenceProperty.fixAccess(config);
    }

    @Test
    public void getAnnotationTest() {
        Class<Annotation> acls = Annotation.class;
        Annotation result = objectIdReferenceProperty.getAnnotation(acls);
        assertEquals(forward.getAnnotation(acls), result);
    }

    @Test
    public void getMemberTest() {
        AnnotatedMember result = objectIdReferenceProperty.getMember();
        assertEquals(forward.getMember(), result);
    }

    @Test
    public void getCreatorIndexTest() {
        int result = objectIdReferenceProperty.getCreatorIndex();
        assertEquals(forward.getCreatorIndex(), result);
    }

    @Test
    public void deserializeAndSetTest() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        objectIdReferenceProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void deserializeSetAndReturnTest() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        assertNull(objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void setTest() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdReferenceProperty.set(instance, value);
    }

    @Test
    public void setAndReturnTest() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        assertEquals(value, objectIdReferenceProperty.setAndReturn(instance, value));
    }

    @Test
    public void propertyReferringTest() throws IOException {
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = 
            new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, ref, type, ob);
        propertyReferring.handleResolvedForwardReference("id", "value");
        assertEquals(ob, propertyReferring._pojo);
    }
}