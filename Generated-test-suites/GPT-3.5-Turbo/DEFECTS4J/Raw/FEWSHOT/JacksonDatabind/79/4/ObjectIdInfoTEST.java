package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Assert;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);

        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(false, objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithLegacyName() {
        String propertyName = "test";
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        Assert.assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(alwaysAsId);

        Assert.assertEquals(alwaysAsId, newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);

        String expected = "ObjectIdInfo: propName=test, scope=java.lang.Object, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true";
        Assert.assertEquals(expected, objectIdInfo.toString());
    }
}