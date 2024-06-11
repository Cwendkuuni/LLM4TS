package com.example.objectidinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

public class ObjectIdInfoTest {

    // Test constructor with valid arguments
    @Test
    public void testConstructorValidArgs() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, objectIdResolver);
        assertNotNull(objectIdInfo);
    }

    // Test constructor with PropertyName argument
    @Test
    public void testConstructorPropertyName() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator);
        assertNotNull(objectIdInfo);
    }

    // Test constructor with String and Class arguments
    @Test
    public void testConstructorStringAndClass() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName.getId(), scope, objectIdGenerator);
        assertNotNull(objectIdInfo);
    }

    // Test constructor with PropertyName, Class, Class and boolean arguments
    @Test
    public void testConstructorPropertyNameClassNameBoolean() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, false, objectIdResolver);
        assertNotNull(objectIdInfo);
    }

    // Test constructor with PropertyName, Class, Class and Class arguments
    @Test
    public void testConstructorPropertyNameClassNameClass() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, null, objectIdResolver);
        assertNotNull(objectIdInfo);
    }

    // Test getter methods
    @Test
    public void testGetterMethods() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, objectIdResolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(objectIdGenerator, objectIdInfo.getGeneratorType());
        assertEquals(objectIdResolver, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    // Test toString method
    @Test
    public void testToStringMethod() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, objectIdResolver);

        String toString = objectIdInfo.toString();
        String expectedToString = "ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedToString, toString);
    }

    // Test withAlwaysAsId method
    @Test
    public void testWithAlwaysAsIdMethod() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, objectIdResolver);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertNotSame(objectIdInfo, objectIdInfoWithAlwaysAsId);
    }

    // Test exception when constructor with invalid arguments (null PropertyName)
    @Test(expected = NullPointerException.class)
    public void testConstructorNullPropertyName() {
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;
        new ObjectIdInfo(null, scope, objectIdGenerator, objectIdResolver);
    }

    // Test exception when constructor with null scope argument
    @Test(expected = NullPointerException.class)
    public void testConstructorNullScope() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;
        new ObjectIdInfo(propertyName, null, objectIdGenerator, objectIdResolver);
    }

    // Test exception when constructor with null generator argument
    @Test(expected = NullPointerException.class)
    public void testConstructorNullGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;
        new ObjectIdInfo(propertyName, scope, null, objectIdResolver);
    }

    // Test exception when constructor with null resolver argument
    @Test(expected = NullPointerException.class)
    public void testConstructorNullResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        new ObjectIdInfo(propertyName, scope, objectIdGenerator, null);
    }

    // Test private constructor
    @Test(expected = IllegalAccessException.class)
    public void testPrivateConstructor() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor<ObjectIdInfo> constructor = ObjectIdInfo.class.getDeclaredConstructor(PropertyName.class, Class.class, Class.class, boolean.class, Class.class);
        constructor.setAccessible(true);
        constructor.newInstance(new PropertyName("testProperty"), String.class, MyObjectIdGenerator.class, false, SimpleObjectIdResolver.class);
    }

}