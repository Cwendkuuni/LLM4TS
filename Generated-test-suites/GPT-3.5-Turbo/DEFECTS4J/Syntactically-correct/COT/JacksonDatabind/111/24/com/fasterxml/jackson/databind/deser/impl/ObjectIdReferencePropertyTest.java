
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ObjectIdReferencePropertyTest {
    @Mock
    private SettableBeanProperty forward;

    private ObjectIdInfo objectIdInfo;

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        objectIdInfo = new ObjectIdInfo(null, null, null, null);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void withName() throws Exception {
        PropertyName newName = new PropertyName();
        SettableBeanProperty result = objectIdReferenceProperty.withName(newName);
        verify(forward).withName(newName);
    }

    @Test
    public void withValueDeserializer() throws Exception {
        JsonDeserializer deser = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        when(forward.withValueDeserializer(any(JsonDeserializer.class))).thenReturn(forward);
        SettableBeanProperty result = objectIdReferenceProperty.withValueDeserializer(deser);
        verify(forward).withValueDeserializer(deser);
    }

    @Test
    public void withNullProvider() throws Exception {
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        SettableBeanProperty result = objectIdReferenceProperty.withNullProvider(nva);
        verify(forward).withNullProvider(nva);
    }

    @Test
    public void fixAccess() throws Exception {
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        objectIdReferenceProperty.fixAccess(config);
        verify(forward).fixAccess(config);
    }

    @Test
    public void getAnnotation() throws Exception {
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };
        when(forward.getAnnotation(any(Class.class))).thenReturn(annotation);
        Annotation result = objectIdReferenceProperty.getAnnotation(Annotation.class);
        verify(forward).getAnnotation(Annotation.class);
    }

    @Test
    public void getMember() throws Exception {
        AnnotatedMember annotationMember = new AnnotatedMember() {
            @Override
            public Class<?> getDeclaringClass() {
                return null;
            }
        };
        when(forward.getMember()).thenReturn(annotationMember);
        AnnotatedMember result = objectIdReferenceProperty.getMember();
        verify(forward).getMember();
    }

    @Test
    public void getCreatorIndex() throws Exception {
        when(forward.getCreatorIndex()).thenReturn(0);
        int result = objectIdReferenceProperty.getCreatorIndex();
        verify(forward).getCreatorIndex();
    }

    @Test
    public void deserializeAndSet() throws Exception {
        JsonParser p = new ObjectMapper().getJsonFactory().createParser("");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object instance = new Object();
        objectIdReferenceProperty.deserializeAndSet(p, ctxt, instance);
        verify(forward).deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void deserializeSetAndReturn() throws Exception {
        JsonParser p = new ObjectMapper().getJsonFactory().createParser("");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object instance = new Object();
        when(forward.setAndReturn(instance, null)).thenReturn(null);
        Object result = objectIdReferenceProperty.deserializeSetAndReturn(p, ctxt, instance);
        verify(forward).setAndReturn(instance, null);
    }

    @Test
    public void set() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        objectIdReferenceProperty.set(instance, value);
        verify(forward).set(instance, value);
    }

    @Test
    public void setAndReturn() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        when(forward.setAndReturn(instance, value)).thenReturn(value);
        Object result = objectIdReferenceProperty.setAndReturn(instance, value);
        verify(forward).setAndReturn(instance, value);
    }

    @Test
    public void handleResolvedForwardReference() throws Exception {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring =
                new ObjectIdReferenceProperty.PropertyReferring(null, null, null, null);
        propertyReferring.handleResolvedForwardReference(null, null);
    }
}
