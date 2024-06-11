package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

/**
 * Test class for {@link ObjectIdInfo} class
 */
public class ObjectIdInfoTest {

    /**
     * Test construction with valid arguments
     */
    @Test
    public void testValidConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.HashIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    /**
     * Test construction with null propertyName
     */
    @Test(expected = NullPointerException.class)
    public void testNullPropertyName() {
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        new ObjectIdInfo(null, scope, generator, false, resolver);
    }

    /**
     * Test construction with null scope
     */
    @Test(expected = NullPointerException.class)
    public void testNullScope() {
        PropertyName propertyName = new PropertyName("name");
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        new ObjectIdInfo(propertyName, null, generator, false, resolver);
    }

    /**
     * Test construction with null generator
     */
    @Test(expected = NullPointerException.class)
    public void testNullGenerator() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        new ObjectIdInfo(propertyName, scope, null, false, resolver);
    }

    /**
     * Test construction with null resolver
     */
    @Test
    public void testNullResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        Class<? extends ObjectIdResolver> expectedResolver = SimpleObjectIdResolver.class;
        assertEquals(expectedResolver, objectIdInfo.getResolverType());
    }

    /**
     * Test withAlwaysAsId method
     */
    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    /**
     * Test getter methods
     */
    @Test
    public void testGetterMethods() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    /**
     * Test equals method
     */
    @Test
    public void testEquals() {
        PropertyName propertyName1 = new PropertyName("name1");
        PropertyName propertyName2 = new PropertyName("name2");
        Class<?> scope1 = String.class;
        Class<?> scope2 = Integer.class;
        Class<? extends ObjectIdGenerator<?>> generator1 = HashIdGenerator.class;
        Class<? extends ObjectIdGenerator<?>> generator2 = HashIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver1 = SimpleObjectIdResolver.class;
        Class<? extends ObjectIdResolver> resolver2 = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propertyName1, scope1, generator1, false, resolver1);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propertyName2, scope2, generator2, false, resolver2);
        assertFalse(objectIdInfo1.equals(objectIdInfo2));

        ObjectIdInfo objectIdInfo3 = new ObjectIdInfo(propertyName1, scope1, generator1, false, resolver1);
        assertTrue(objectIdInfo1.equals(objectIdInfo3));
    }
}