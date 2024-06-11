package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectIdInfoTest {
    
    @Test
    public void testObjectIdInfoConstructorWithResolver() {
        // Given
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        
        // When
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        // Then
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        // Given
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        // When
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
        
        // Then
        assertEquals(propertyName, updatedObjectIdInfo.getPropertyName());
        assertEquals(scope, updatedObjectIdInfo.getScope());
        assertEquals(generator, updatedObjectIdInfo.getGeneratorType());
        assertEquals(resolver, updatedObjectIdInfo.getResolverType());
        assertTrue(!updatedObjectIdInfo.getAlwaysAsId());
    }
}

class MyObjectIdGenerator implements ObjectIdGenerator<Object> {
}

class MyObjectIdResolver implements ObjectIdResolver {
}