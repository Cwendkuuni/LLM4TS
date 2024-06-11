package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor1() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = new IdentityObjectIdGenerator();
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        assertNotNull(objectIdInfo);
        assertEquals("name", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(IdentityObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor2() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertNotNull(objectIdInfo);
        assertEquals("id", objectIdInfo.getPropertyName().getName());
        assertEquals(Object.class, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor3() {
        String name = "id";
        Class<?> scope = Long.class;
        Class<? extends ObjectIdGenerator<?>> generator = new IdentityObjectIdGenerator();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);
        assertNotNull(objectIdInfo);
        assertEquals("id", objectIdInfo.getPropertyName().getName());
        assertEquals(Long.class, objectIdInfo.getScope());
        assertEquals(IdentityObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor4() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Long.class;
        Class<? extends ObjectIdGenerator<?>> generator = new IdentityObjectIdGenerator();
        boolean alwaysAsId = true;
        Class<? extends ObjectIdResolver> resolver = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);
        assertNotNull(objectIdInfo);
        assertEquals("id", objectIdInfo.getPropertyName().getName());
        assertEquals(Long.class, objectIdInfo.getScope());
        assertEquals(IdentityObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor5() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Long.class;
        Class<? extends ObjectIdGenerator<?>> generator = new IdentityObjectIdGenerator();
        boolean alwaysAsId = false;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);
        assertNotNull(objectIdInfo);
        assertEquals("id", objectIdInfo.getPropertyName().getName());
        assertEquals(Long.class, objectIdInfo.getScope());
        assertEquals(IdentityObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Long.class;
        Class<? extends ObjectIdGenerator<?>> generator = new IdentityObjectIdGenerator();
        boolean alwaysAsId = true;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(alwaysAsId);
        assertNotNull(newObjectIdInfo);
        assertEquals(objectIdInfo, newObjectIdInfo);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }
}