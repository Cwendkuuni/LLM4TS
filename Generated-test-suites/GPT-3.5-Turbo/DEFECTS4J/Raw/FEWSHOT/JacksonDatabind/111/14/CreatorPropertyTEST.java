package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

public class CreatorPropertyTest {
    private CreatorProperty creatorProperty;

    @Before
    public void setup() {
        PropertyName name = new PropertyName("test");
        JavaType type = mock(JavaType.class);
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 1;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals("test", creatorProperty.getName().getSimpleName());
        assertNull(creatorProperty.getWrapperName().getSimpleName());
        assertEquals(1, creatorProperty.getCreatorIndex());
        assertEquals(Object.class, creatorProperty.getType().getRawClass());
        assertEquals(Object.class, creatorProperty.getType().getContentType().getRawClass());
        assertEquals(0, creatorProperty.getMetadata().getMergeableValue());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        creatorProperty.setFallbackSetter(mock(SettableBeanProperty.class));
        creatorProperty.fixAccess(config);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testFindInjectableValue() throws JsonMappingException {
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = new Object();
        creatorProperty.findInjectableValue(context, beanInstance);
    }

    @Test
    public void testMarkAsIgnorable() {
        creatorProperty.markAsIgnorable();
        assertEquals(true, creatorProperty.isIgnorable());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        creatorProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        creatorProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        creatorProperty.setAndReturn(instance, value);
    }

    @Test
    public void testGetInjectableValueId() {
        assertEquals(creatorProperty.getInjectableValueId(), new Object());
    }

    @Test
    public void testToString() {
        creatorProperty.toString();
    }

    @Test(expected = IOException.class)
    public void testVerifySetter() throws IOException {
        creatorProperty.setFallbackSetter(null);
        creatorProperty.deserializeAndSet(null, null, null);
    }

    @Test(expected = IOException.class)
    public void testReportMissingSetter() throws IOException {
        creatorProperty._reportMissingSetter(null, null);
    }
}