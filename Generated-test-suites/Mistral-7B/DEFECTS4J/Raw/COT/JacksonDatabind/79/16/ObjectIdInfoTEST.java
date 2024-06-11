package com.yourpackagename.test;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

/**
 * Test class for ObjectIdInfo
 */
public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorAndResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = IdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.yourpackagename.IdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = IdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.yourpackagename.IdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullPropertyName() {
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = IdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, generator, false, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null, false, resolver);
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = IdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertNotEquals(objectIdInfo, withAlwaysAsId);
        assertTrue(withAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testGetters() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Constructor<ObjectIdInfo> constructor = ObjectIdInfo.class.getConstructor(PropertyName.class, Class.class, Class.class, Class.class, Class.class);
        ObjectIdInfo objectIdInfo = constructor.newInstance(new PropertyName("testProperty"), Object.class, IdGenerator.class, SimpleObjectIdResolver.class, null);

        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(Object.class, objectIdInfo.getScope());
        assertEquals(IdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

}