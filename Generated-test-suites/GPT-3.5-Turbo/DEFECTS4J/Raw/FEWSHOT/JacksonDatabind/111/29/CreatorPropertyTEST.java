package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorPropertyTest {
    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("testProperty");
        JavaType type = new JavaType(String.class);
        PropertyName wrapperName = new PropertyName("testWrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);

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
    public void testWithMethods() {
        PropertyName name = new PropertyName("testProperty");
        JavaType type = new JavaType(String.class);
        PropertyName newName = new PropertyName("newTestProperty");
        PropertyName wrapperName = new PropertyName("testWrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        property.setFallbackSetter(new SettableBeanProperty());

        CreatorProperty property2 = property.withName(newName);
        assertEquals(newName, property2.getName());

        CreatorProperty property3 = property.withValueDeserializer(deser);
        assertEquals(deser, property3.getValueDeserializer());

        CreatorProperty property4 = property.withNullProvider(nva);
        assertEquals(nva, property4.getNullValueProvider());

        property.fixAccess(new DeserializationConfig());

        assertTrue(property2.isIgnorable());

        assertEquals(deser, property3.getValueDeserializer());
        assertEquals(nva, property4.getNullValueProvider());
    }

    @Test
    public void testInjectionMethods() throws IOException {
        CreatorProperty property = new CreatorProperty(new PropertyName("testProperty"), new JavaType(String.class), new PropertyName("testWrapper"), new TypeDeserializer(), new Annotations(), new AnnotatedParameter(), 0, new Object(), PropertyMetadata.STD_REQUIRED);
        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();

        assertNull(property.findInjectableValue(context, beanInstance));
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
        property.deserializeAndSet(null, context, beanInstance);
        assertNull(property.deserializeSetAndReturn(null, context, beanInstance));
        property.set(beanInstance, new Object());
        assertNull(property.setAndReturn(beanInstance, new Object()));
        assertEquals(new Object(), property.getInjectableValueId());
    }

    @Test
    public void testToStringMethod() {
        CreatorProperty property = new CreatorProperty(new PropertyName("testProperty"), new JavaType(String.class), new PropertyName("testWrapper"), new TypeDeserializer(), new Annotations(), new AnnotatedParameter(), 0, new Object(), PropertyMetadata.STD_REQUIRED);
        assertEquals("[creator property, name 'testProperty'; inject id 'java.lang.Object@1']", property.toString());
    }

    @Test(expected = IOException.class)
    public void testVerifySetterMethod() throws IOException {
        CreatorProperty property = new CreatorProperty(new PropertyName("testProperty"), new JavaType(String.class), new PropertyName("testWrapper"), new TypeDeserializer(), new Annotations(), new AnnotatedParameter(), 0, new Object(), PropertyMetadata.STD_REQUIRED);
        property.setFallbackSetter(null);
        property._verifySetter();
    }

    @Test(expected = IOException.class)
    public void testReportMissingSetterMethod() throws IOException {
        CreatorProperty property = new CreatorProperty(new PropertyName("testProperty"), new JavaType(String.class), new PropertyName("testWrapper"), new TypeDeserializer(), new Annotations(), new AnnotatedParameter(), 0, new Object(), PropertyMetadata.STD_REQUIRED);
        property.setFallbackSetter(null);
        property._reportMissingSetter(null, null);
    }
}