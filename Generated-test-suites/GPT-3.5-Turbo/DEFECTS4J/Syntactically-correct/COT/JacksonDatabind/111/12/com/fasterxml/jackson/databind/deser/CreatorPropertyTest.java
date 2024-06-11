package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName propertyName = new PropertyName("test");
        JavaType javaType = new JavaType();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, wrapperName, typeDeserializer,
                contextAnnotations, param, index, injectableValueId, metadata);

        assertEquals(propertyName, creatorProperty.getFullName());
        assertEquals(javaType, creatorProperty.getType());
        assertEquals(wrapperName, creatorProperty.getWrapperName());
        assertEquals(typeDeserializer, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        assertEquals(param, creatorProperty.getMember());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertEquals(metadata, creatorProperty.getMetadata());
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testWithMethods() {
        PropertyName propertyName = new PropertyName("test");
        JavaType javaType = new JavaType();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, wrapperName, typeDeserializer,
                contextAnnotations, param, index, injectableValueId, metadata);

        PropertyName newName = new PropertyName("new");
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();
        SettableBeanProperty result = creatorProperty.withName(newName);
        assertNotEquals(creatorProperty, result);
        assertEquals(newName, result.getFullName());
        assertEquals(javaType, result.getType());
        assertEquals(wrapperName, result.getWrapperName());
        assertEquals(typeDeserializer, result.getTypeDeserializer());
        assertEquals(contextAnnotations, result.getContextAnnotations());
        assertEquals(param, result.getMember());
        assertEquals(index, result.getCreatorIndex());
        assertEquals(injectableValueId, result.getInjectableValueId());
        assertEquals(metadata, result.getMetadata());
        
        result = creatorProperty.withValueDeserializer(deser);
        assertNotEquals(creatorProperty, result);
        assertEquals(propertyName, result.getFullName());
        assertEquals(javaType, result.getType());
        assertEquals(wrapperName, result.getWrapperName());
        assertEquals(deser, result.getValueDeserializer());
        assertEquals(nva, result.getNullValueProvider());
        assertEquals(contextAnnotations, result.getContextAnnotations());
        assertEquals(param, result.getMember());
        assertEquals(index, result.getCreatorIndex());
        assertEquals(injectableValueId, result.getInjectableValueId());
        assertEquals(metadata, result.getMetadata());

        result = creatorProperty.withNullProvider(nva);
        assertNotEquals(creatorProperty, result);
        assertEquals(propertyName, result.getFullName());
        assertEquals(javaType, result.getType());
        assertEquals(wrapperName, result.getWrapperName());
        assertEquals(deser, result.getValueDeserializer());
        assertEquals(nva, result.getNullValueProvider());
        assertEquals(contextAnnotations, result.getContextAnnotations());
        assertEquals(param, result.getMember());
        assertEquals(index, result.getCreatorIndex());
        assertEquals(injectableValueId, result.getInjectableValueId());
        assertEquals(metadata, result.getMetadata());

        DeserializationConfig config = new DeserializationConfig();
        creatorProperty.fixAccess(config);
        assertNotNull(creatorProperty.getFallbackSetter());

        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());

        DeserializationContext context = new DeserializationContext();
        Object beanInstance = new Object();
        creatorProperty.findInjectableValue(context, beanInstance);

        creatorProperty.inject(context, beanInstance);

        assertNull(creatorProperty.getAnnotation(Annotation.class));
        assertNotNull(creatorProperty.getMember());

        creatorProperty.deserializeAndSet(null, context, beanInstance);

        assertNull(creatorProperty.deserializeSetAndReturn(null, context, beanInstance));

        creatorProperty.set(beanInstance, new Object());

        assertNull(creatorProperty.setAndReturn(beanInstance, new Object()));

        assertNotNull(creatorProperty.getInjectableValueId());
        
        assertNotNull(creatorProperty.toString());
    }

    @Test
    public void testMissingSetter() {
        PropertyName propertyName = new PropertyName("test");
        JavaType javaType = new JavaType();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, wrapperName, typeDeserializer,
                contextAnnotations, param, index, injectableValueId, metadata);

        try {
            creatorProperty._verifySetter();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("No fallback setter/field defined for creator property 'test'", e.getMessage());
        }
    }

    @Test
    public void testReportMissingSetterWithParser() {
        PropertyName propertyName = new PropertyName("test");
        JavaType javaType = new JavaType();
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty creatorProperty = new CreatorProperty(propertyName, javaType, wrapperName, typeDeserializer,
                contextAnnotations, param, index, injectableValueId, metadata);

        JsonParser parser = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        try {
            creatorProperty._reportMissingSetter(parser, ctxt);
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (InvalidDefinitionException e) {
            assertEquals("No fallback setter/field defined for creator property 'test'", e.getMessage());
            assertEquals(JavaType.class, e.getType());
        }
    }
}