package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor1() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor2() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolverType = ObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorType, objectIdInfo.getGeneratorType());
        assertEquals(resolverType, objectIdInfo.getResolverType()); // Using default resolver type
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor3() {
        String propertyName = "test";
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolverType = ObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType);
        
        assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorType, objectIdInfo.getGeneratorType());
        assertEquals(resolverType, objectIdInfo.getResolverType()); // Using default resolver type
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolverType = ObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType, resolverType);
        
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
        
        newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
        assertFalse(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolverType = ObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType, resolverType);
        
        String expectedToString = "ObjectIdInfo: propName=test, scope=java.lang.Object, generatorType=com.fasterxml.jackson.databind.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedToString, objectIdInfo.toString());
    }

}