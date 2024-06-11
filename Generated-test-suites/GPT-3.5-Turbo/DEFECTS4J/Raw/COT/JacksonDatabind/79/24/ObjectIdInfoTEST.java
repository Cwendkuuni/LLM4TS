
package com.fasterxml.jackson.databind.introspect;

import org.junit.Assert;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        objectIdInfo = objectIdInfo.withAlwaysAsId(true);

        Assert.assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testGetPropertyName() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
    }

    @Test
    public void testGetScope() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        Assert.assertEquals(scope, objectIdInfo.getScope());
    }

    @Test
    public void testGetGeneratorType() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
    }

    @Test
    public void testGetResolverType() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
    }

    @Test
    public void testGetAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        String expected = "ObjectIdInfo: propName=test, scope=java.lang.String, " +
                "generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";

        Assert.assertEquals(expected, objectIdInfo.toString());
    }
}
