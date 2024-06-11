package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        boolean alwaysAsId = true;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNotNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testDeprecatedConstructor() {
        String propertyName = "id";
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertNotNull(objectIdInfo);
        assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        boolean alwaysAsId = true;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator).withAlwaysAsId(alwaysAsId);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        boolean alwaysAsId = true;
        boolean notAlwaysAsId = false;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propertyName, scope, generator, notAlwaysAsId);

        assertEquals("ObjectIdInfo: propName=id, scope=java.lang.Object, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
        assertEquals("ObjectIdInfo: propName=id, scope=java.lang.Object, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo2.toString());
    }
}