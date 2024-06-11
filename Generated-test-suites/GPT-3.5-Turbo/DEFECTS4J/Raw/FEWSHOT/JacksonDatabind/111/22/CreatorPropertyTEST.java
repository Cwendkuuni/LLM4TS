package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.impl.NullsConstantProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {
    private CreatorProperty creatorProperty;

    private PropertyName name;
    private JavaType type;
    private PropertyName wrapperName;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedParameter param;
    private int index;
    private Object injectableValueId;
    private PropertyMetadata metadata;

    @Before
    public void setup() {
        name = new PropertyName("testProperty");
        type = mock(JavaType.class);
        wrapperName = new PropertyName("testWrapper");
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        param = mock(AnnotatedParameter.class);
        index = 0;
        injectableValueId = new Object();
        metadata = PropertyMetadata.STD_REQUIRED;

        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(name, creatorProperty.getName());
        assertEquals(type, creatorProperty.getType());
        assertEquals(wrapperName, creatorProperty.getWrapperName());
        assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        assertEquals(param, creatorProperty.getMember());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertEquals(metadata, creatorProperty.getMetadata());
    }

    @Test
    public void testWithMethods() {
        PropertyName newName = new PropertyName("newName");
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullsConstantProvider nva = mock(NullsConstantProvider.class);

        CreatorProperty newCreatorProperty = creatorProperty.withName(newName);
        assertEquals(newName, newCreatorProperty.getName());
        assertEquals(type, newCreatorProperty.getType());
        assertEquals(wrapperName, newCreatorProperty.getWrapperName());
        assertEquals(typeDeser, newCreatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, newCreatorProperty.getContextAnnotations());
        assertEquals(param, newCreatorProperty.getMember());
        assertEquals(index, newCreatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, newCreatorProperty.getInjectableValueId());
        assertEquals(metadata, newCreatorProperty.getMetadata());

        CreatorProperty newCreatorProperty2 = creatorProperty.withValueDeserializer(deser);
        assertEquals(name, newCreatorProperty2.getName());
        assertEquals(type, newCreatorProperty2.getType());
        assertEquals(wrapperName, newCreatorProperty2.getWrapperName());
        assertEquals(typeDeser, newCreatorProperty2.getTypeDeserializer());
        assertEquals(contextAnnotations, newCreatorProperty2.getContextAnnotations());
        assertEquals(param, newCreatorProperty2.getMember());
        assertEquals(index, newCreatorProperty2.getCreatorIndex());
        assertEquals(injectableValueId, newCreatorProperty2.getInjectableValueId());
        assertEquals(metadata, newCreatorProperty2.getMetadata());

        CreatorProperty newCreatorProperty3 = creatorProperty.withNullProvider(nva);
        assertEquals(name, newCreatorProperty3.getName());
        assertEquals(type, newCreatorProperty3.getType());
        assertEquals(wrapperName, newCreatorProperty3.getWrapperName());
        assertEquals(typeDeser, newCreatorProperty3.getTypeDeserializer());
        assertEquals(contextAnnotations, newCreatorProperty3.getContextAnnotations());
        assertEquals(param, newCreatorProperty3.getMember());
        assertEquals(index, newCreatorProperty3.getCreatorIndex());
        assertEquals(injectableValueId, newCreatorProperty3.getInjectableValueId());
        assertEquals(metadata, newCreatorProperty3.getMetadata());
    }

    @Test
    public void testFixAccess() throws Exception {
        DeserializationConfig config = mock(DeserializationConfig.class);
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(fallbackSetter);

        creatorProperty.fixAccess(config);

        verify(fallbackSetter).fixAccess(config);
    }

    @Test
    public void testMarkAsIgnorable() {
        creatorProperty.markAsIgnorable();

        assertEquals(true, creatorProperty.isIgnorable());
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;
        A annotation = mock(A.class);
        creatorProperty._annotated = mock(AnnotatedMember.class);

        assertEquals(annotation, creatorProperty.getAnnotation(acls));
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);

        creatorProperty._fallbackSetter = fallbackSetter;

        creatorProperty.deserializeAndSet(p, ctxt, instance);

        verify(fallbackSetter).set(instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);

        creatorProperty._fallbackSetter = fallbackSetter;

        creatorProperty.deserializeSetAndReturn(p, ctxt, instance);

        verify(fallbackSetter).setAndReturn(instance, value);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);

        creatorProperty._fallbackSetter = fallbackSetter;

        creatorProperty.set(instance, value);

        verify(fallbackSetter).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);

        creatorProperty._fallbackSetter = fallbackSetter;

        creatorProperty.setAndReturn(instance, value);

        verify(fallbackSetter).setAndReturn(instance, value);
    }

    private interface A {

    }
}