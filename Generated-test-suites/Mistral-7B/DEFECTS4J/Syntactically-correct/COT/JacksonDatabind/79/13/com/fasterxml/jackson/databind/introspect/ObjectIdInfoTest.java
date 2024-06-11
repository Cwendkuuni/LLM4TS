package com.example.objectidinfo;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

    // Test constructor with all arguments
    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false, resolver);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.IdentityInfoGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    // Test default constructor
    @Test
    public void testObjectIdInfoDefaultConstruction() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        assertTrue(objectIdInfo == null);
    }

    // Test constructor with deprecated arguments
    @Test
    public void testObjectIdInfoDeprecatedConstruction() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.IdentityInfoGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    // Test constructor with all arguments and null resolver
    @Test
    public void testObjectIdInfoConstructionWithNullResolver() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false, null);
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo expectedObjectIdInfo = new ObjectIdInfo(name, scope, generator, false, resolver);
        assertEquals(expectedObjectIdInfo.toString(), objectIdInfo.toString());
    }

    // Test getter methods
    @Test
    public void testGetters() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false, SimpleObjectIdResolver.class);
        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(IdentityInfoGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    // Test withAlwaysAsId method
    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false, SimpleObjectIdResolver.class);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertEquals(false, objectIdInfo.getAlwaysAsId());
        assertEquals(true, newObjectIdInfo.getAlwaysAsId());
    }

    // Test null argument exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoNullGenerator() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<?> generator = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false, SimpleObjectIdResolver.class);
    }

    // Test null argument exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoNullScope() {
        PropertyName name = new PropertyName("testProperty");
        Class<?> scope = null;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, false, SimpleObjectIdResolver.class);
    }

    // Test null argument exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoNullPropertyName() {
        PropertyName propertyName = null;
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        try {
            new ObjectIdInfo(propertyName, scope, generator, false, SimpleObjectIdResolver.class);
        } catch (NullPointerException e) {
            throw new AssertionError("Expected IllegalArgumentException but got NullPointerException", e);
        }
    }

    // Test reflection constructor
    @Test
    public void testObjectIdInfoReflectionConstruction() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor<ObjectIdInfo> constructor = ObjectIdInfo.class.getDeclaredConstructor(PropertyName.class, Class.class, Class.class, boolean.class, Class.class);
        constructor.setAccessible(true);
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoGenerator> generator = IdentityInfoGenerator.class;
        ObjectIdInfo objectIdInfo = constructor.newInstance(name, scope, generator, false, SimpleObjectIdResolver.class);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.IdentityInfoGenerator, alwaysAsId=false", objectIdInfo.toString());
    }
}