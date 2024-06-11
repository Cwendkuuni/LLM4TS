package com.fasterxml.jackson.databind.deser;

import org.junit.Assert;
import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("testName");
        JavaType type = new JavaType();
        PropertyName wrapperName = new PropertyName("testWrapperName");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser,
                contextAnnotations, param, index, injectableValueId, metadata);

        Assert.assertNotNull(creatorProperty);
        Assert.assertEquals(name, creatorProperty.getName());
        Assert.assertEquals(type, creatorProperty.getType());
        Assert.assertEquals(wrapperName, creatorProperty.getWrapperName());
        Assert.assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        Assert.assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        Assert.assertEquals(param, creatorProperty.getMember());
        Assert.assertEquals(index, creatorProperty.getCreatorIndex());
        Assert.assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        Assert.assertEquals(metadata, creatorProperty.getMetadata());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        CreatorProperty creatorProperty = new CreatorProperty();

        creatorProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        CreatorProperty creatorProperty = new CreatorProperty();

        creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();

        CreatorProperty creatorProperty = new CreatorProperty();

        creatorProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();

        CreatorProperty creatorProperty = new CreatorProperty();

        creatorProperty.setAndReturn(instance, value);
    }

    @Test
    public void testFindInjectableValue() {
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();

        CreatorProperty creatorProperty = new CreatorProperty();

        try {
            creatorProperty.findInjectableValue(context, beanInstance);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected Exception
        }
    }

    @Test
    public void testInject() {
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();

        CreatorProperty creatorProperty = new CreatorProperty();

        try {
            creatorProperty.inject(context, beanInstance);
        } catch (IOException e) {
            Assert.fail("Unexpected IOException");
        }
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;

        CreatorProperty creatorProperty = new CreatorProperty();

        Assert.assertNull(creatorProperty.getAnnotation(acls));
    }

    @Test
    public void testToString() {
        CreatorProperty creatorProperty = new CreatorProperty();

        Assert.assertNotNull(creatorProperty.toString());
    }

    private class JavaType {}
    private class PropertyMetadata {}
    private class Annotations {}
    private class AnnotatedParameter {}
    private class AnnotatedMember {}
    private class DeserializationConfig {}
    private class NullValueProvider {}
    private class JsonDeserializer<T> {}
    private class NullValueProvider {}
}