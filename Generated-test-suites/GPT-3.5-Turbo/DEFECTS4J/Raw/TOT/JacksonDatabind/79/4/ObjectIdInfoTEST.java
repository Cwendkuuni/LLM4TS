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
    public void testConstructorWithAllParameters() {
        PropertyName propertyName = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        boolean alwaysAsId = false;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver, alwaysAsId);

        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        boolean alwaysAsId = false;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver, alwaysAsId);

        boolean newAlwaysAsIdState = !alwaysAsId;
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(newAlwaysAsIdState);

        Assert.assertEquals(propertyName, newObjectIdInfo.getPropertyName());
        Assert.assertEquals(scope, newObjectIdInfo.getScope());
        Assert.assertEquals(generator, newObjectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, newObjectIdInfo.getResolverType());
        Assert.assertEquals(newAlwaysAsIdState, newObjectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testDeprecatedConstructor1() {
        PropertyName propertyName = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertNull(objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testDeprecatedConstructor2() {
        String propertyName = "testName";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName().getSimpleName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertNull(objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver, alwaysAsId);

        String expectedToString = "ObjectIdInfo: propName=testName, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true";
        
        Assert.assertEquals(expectedToString, objectIdInfo.toString());
    }

}