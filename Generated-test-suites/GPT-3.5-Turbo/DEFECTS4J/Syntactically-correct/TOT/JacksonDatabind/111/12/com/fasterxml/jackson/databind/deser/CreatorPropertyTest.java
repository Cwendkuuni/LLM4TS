package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        JavaType type = new JavaType(String.class);
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser,
                contextAnnotations, param, index, injectableValueId, metadata);

        assertNotNull(property);
        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertEquals(wrapperName, property.getWrapperName());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(param, property.getMember());
        assertEquals(index, property.getCreatorIndex());
        assertEquals(injectableValueId, property.getInjectableValueId());
        assertEquals(metadata, property.getMetadata());
    }

    @Test
    public void testDeserializeAndSet() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();

        assertThrows(IOException.class, () -> {
            property.deserializeAndSet(parser, context, instance);
        });
    }

    @Test
    public void testDeserializeSetAndReturn() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, null, null);
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();

        assertThrows(IOException.class, () -> {
            property.deserializeSetAndReturn(parser, context, instance);
        });
    }

    @Test
    public void testSet() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, null, null);
        Object instance = new Object();
        Object value = "testValue";

        assertThrows(IOException.class, () -> {
            property.set(instance, value);
        });
    }

    @Test
    public void testSetAndReturn() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, null, null);
        Object instance = new Object();
        Object value = "testValue";

        assertThrows(IOException.class, () -> {
            property.setAndReturn(instance, value);
        });
    }

    @Test
    public void testFindInjectableValue() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, new Object(), null);
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();

        assertThrows(JsonMappingException.class, () -> {
            property.findInjectableValue(context, beanInstance);
        });
    }

    @Test
    public void testInject() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, new Object(), null);
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();

        assertThrows(IOException.class, () -> {
            property.inject(context, beanInstance);
        });
    }

    @Test
    public void testGetAnnotation() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, new Object(), null);
        Class<A> acls = A.class;

        assertNull(property.getAnnotation(acls));
    }

    @Test
    public void testWithNullProvider() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, new Object(), null);
        NullValueProvider nva = new NullValueProvider();

        SettableBeanProperty newProperty = property.withNullProvider(nva);

        assertNotNull(newProperty);
        assertNotEquals(property, newProperty);
        assertEquals(nva, newProperty.getNullValueProvider());
    }

    @Test
    public void testWithNullProviderSameInstance() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, new Object(), null);
        NullValueProvider nva = new NullValueProvider();

        SettableBeanProperty newProperty = property.withNullProvider(nva);
        SettableBeanProperty sameProperty = newProperty.withNullProvider(nva);

        assertNotNull(sameProperty);
        assertEquals(newProperty, sameProperty);
    }

    @Test
    public void testFixAccess() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, new Object(), null);
        DeserializationConfig config = new DeserializationConfig();

        property.fixAccess(config);

        // No assertions, expect to execute without any exceptions
    }

    @Test
    public void testSetFallbackSetter() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, new Object(), null);
        SettableBeanProperty fallbackSetter = new SettableBeanProperty();

        property.setFallbackSetter(fallbackSetter);

        assertEquals(fallbackSetter, property.getFallbackSetter());
    }

    @Test
    public void testMarkAsIgnorable() {
        CreatorProperty property = new CreatorProperty(new PropertyName("test"), new JavaType(String.class),
                null, null, new Annotations(), new AnnotatedParameter(), 0, new Object(), null);

        property.markAsIgnorable();

        assertTrue(property.isIgnorable());
    }
}