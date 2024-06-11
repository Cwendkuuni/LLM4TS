package com.fasterxml.jackson.databind.deser;

import org.junit.Assert;
import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testGetName() {
        // Arrange
        PropertyName name = new PropertyName("test");
        JavaType type = new JavaType(getClass());
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        // Act
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);

        // Assert
        Assert.assertEquals("test", creatorProperty.getName().getSimpleName());
    }

    @Test
    public void testWithType() {
        // Arrange
        PropertyName name = new PropertyName("test");
        JavaType type = new JavaType(getClass());
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        // Act
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);

        // Assert
        Assert.assertEquals(getClass(), creatorProperty.getType().getRawClass());
    }

    @Test
    public void testWithNullProvider() {
        // Arrange
        PropertyName name = new PropertyName("test");
        JavaType type = new JavaType(getClass());
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        // Act
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        creatorProperty.withNullProvider(new NullValueProvider());

        // Assert
        Assert.assertNotNull(creatorProperty.getNullValueProvider());
    }

    @Test
    public void testToString() {
        // Arrange
        PropertyName name = new PropertyName("test");
        JavaType type = new JavaType(getClass());
        PropertyName wrapperName = new PropertyName("wrapper");
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = new Object();
        PropertyMetadata metadata = new PropertyMetadata();

        // Act
        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        String result = creatorProperty.toString();

        // Assert
        Assert.assertEquals("[creator property, name 'test'; inject id 'java.lang.Object@1']", result);
    }
}