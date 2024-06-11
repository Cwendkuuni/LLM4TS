package com.fasterxml.jackson.databind.deser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() throws IOException {
        PropertyName name = new PropertyName("test");
        JavaType type = JavaType.INT;
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        int index = 0;
        Object injectableValueId = "id";
        PropertyMetadata metadata = mock(PropertyMetadata.class);

        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser,
                contextAnnotations, param, index, injectableValueId, metadata);

        Assert.assertNotNull(creatorProperty);
        Assert.assertEquals(name, creatorProperty.getFullName());
        Assert.assertEquals(type, creatorProperty.getType());
        Assert.assertEquals(wrapperName, creatorProperty.getWrapperName());
        Assert.assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        Assert.assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        Assert.assertEquals(metadata, creatorProperty.getMetadata());
        Assert.assertEquals(param, creatorProperty.getMember());
        Assert.assertEquals(index, creatorProperty.getCreatorIndex());
        Assert.assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
    }

    @Test
    public void testWithNullProvider() throws IOException {
        CreatorProperty src = mock(CreatorProperty.class);
        JsonDeserializer deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        CreatorProperty creatorProperty = new CreatorProperty(src, deser, nva);

        Assert.assertNotNull(creatorProperty);
        Assert.assertEquals(deser, creatorProperty.getValueDeserializer());
        Assert.assertEquals(nva, creatorProperty.getNullValueProvider());
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        CreatorProperty creatorProperty = mock(CreatorProperty.class);
        Object beanInstance = mock(Object.class);

        when(creatorProperty.getInjectableValueId()).thenReturn("id");
        when(creatorProperty.getName()).thenReturn("name");
        when(creatorProperty.getClass()).thenReturn(CreatorProperty.class);

        Object result = creatorProperty.findInjectableValue(context, beanInstance);

        verify(context).findInjectableValue("id", creatorProperty, beanInstance);
        Assert.assertNull(result);
    }

    @Test
    public void testInject() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        CreatorProperty creatorProperty = mock(CreatorProperty.class);
        Object beanInstance = mock(Object.class);

        when(creatorProperty.findInjectableValue(context, beanInstance)).thenReturn("value");

        creatorProperty.inject(context, beanInstance);

        verify(creatorProperty).set(beanInstance, "value");
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        CreatorProperty creatorProperty = mock(CreatorProperty.class);
        Object instance = mock(Object.class);

        when(creatorProperty.getFallbackSetter()).thenReturn(mock(SettableBeanProperty.class));
        when(creatorProperty.deserialize(parser, context)).thenReturn("value");

        creatorProperty.deserializeAndSet(parser, context, instance);

        verify(creatorProperty).deserialize(parser, context);
        verify(creatorProperty.getFallbackSetter()).set(instance, "value");
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        CreatorProperty creatorProperty = mock(CreatorProperty.class);
        Object instance = mock(Object.class);

        when(creatorProperty.getFallbackSetter()).thenReturn(mock(SettableBeanProperty.class));
        when(creatorProperty.deserialize(parser, context)).thenReturn("value");

        Object result = creatorProperty.deserializeSetAndReturn(parser, context, instance);

        verify(creatorProperty).deserialize(parser, context);
        verify(creatorProperty.getFallbackSetter()).setAndReturn(instance, "value");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testSet() throws IOException {
        CreatorProperty creatorProperty = mock(CreatorProperty.class);
        Object instance = mock(Object.class);
        Object value = "value";

        when(creatorProperty.getFallbackSetter()).thenReturn(mock(SettableBeanProperty.class));

        creatorProperty.set(instance, value);

        verify(creatorProperty.getFallbackSetter()).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        CreatorProperty creatorProperty = mock(CreatorProperty.class);
        Object instance = mock(Object.class);
        Object value = "value";

        when(creatorProperty.getFallbackSetter()).thenReturn(mock(SettableBeanProperty.class));

        Object result = creatorProperty.setAndReturn(instance, value);

        verify(creatorProperty.getFallbackSetter()).setAndReturn(instance, value);
        Assert.assertEquals(value, result);
    }

}