
package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructor1() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructor2() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructor3() {
        String propertyName = "id";
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        
        assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        
        assertEquals(propertyName, updatedObjectIdInfo.getPropertyName());
        assertEquals(scope, updatedObjectIdInfo.getScope());
        assertEquals(generator, updatedObjectIdInfo.getGeneratorType());
        assertEquals(resolver, updatedObjectIdInfo.getResolverType());
        assertTrue(updatedObjectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        String expectedToString = "ObjectIdInfo: propName=id, scope=java.lang.Object, generatorType=com.fasterxml.jackson.databind.ObjectIdGenerator, alwaysAsId=false";
        
        assertEquals(expectedToString, objectIdInfo.toString());
    }
}

