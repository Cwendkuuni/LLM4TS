package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyScope> scope = MyScope.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, false, objectIdResolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(objectIdGenerator, objectIdInfo.getGeneratorType());
        assertEquals(objectIdResolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test edge cases and exceptions for ObjectIdInfo constructor with four arguments
    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullPropertyName() {
        Class<MyScope> scope = MyScope.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;

        new ObjectIdInfo(null, scope, objectIdGenerator, false, objectIdResolver);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullScope() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;

        new ObjectIdInfo(propertyName, null, objectIdGenerator, false, objectIdResolver);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullGeneratorType() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyScope> scope = MyScope.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;

        new ObjectIdInfo(propertyName, scope, null, false, objectIdResolver);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullResolverType() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyScope> scope = MyScope.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;

        new ObjectIdInfo(propertyName, scope, objectIdGenerator, false, null);
    }

    // Test for ObjectIdInfo constructor with three arguments
    @Test
    public void testObjectIdInfoConstructorWithThreeArguments() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyScope> scope = MyScope.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, false);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(objectIdGenerator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test edge cases and exceptions for ObjectIdInfo constructor with three arguments
    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullPropertyNameThreeArguments() {
        Class<MyScope> scope = MyScope.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;

        new ObjectIdInfo(null, scope, objectIdGenerator, false);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullScopeThreeArguments() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;

        new ObjectIdInfo(propertyName, null, objectIdGenerator, false);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullGeneratorTypeThreeArguments() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyScope> scope = MyScope.class;

        new ObjectIdInfo(propertyName, scope, null, false);
    }

    // Test for ObjectIdInfo constructor with two arguments
    @Test
    public void testObjectIdInfoConstructorWithTwoArguments() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyScope> scope = MyScope.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, MyObjectIdGenerator.class);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test edge cases and exceptions for ObjectIdInfo constructor with two arguments
    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullPropertyNameTwoArguments() {
        Class<MyScope> scope = MyScope.class;

        new ObjectIdInfo(null, scope, MyObjectIdGenerator.class);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullScopeTwoArguments() {
        PropertyName propertyName = new PropertyName("testProperty");

        new ObjectIdInfo(propertyName, null, MyObjectIdGenerator.class);
    }

    // Test for ObjectIdInfo method withAlwaysAsId()
    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyScope> scope = MyScope.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, false, objectIdResolver);

        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertTrue(newObjectIdInfo.getAlwaysAsId());
        assertEquals(objectIdInfo, newObjectIdInfo.getGeneratorType());
    }

    // Test edge cases and exceptions for ObjectIdInfo method withAlwaysAsId()
    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithAlwaysAsIdIllegalArgument() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyScope> scope = MyScope.class;
        Class<MyObjectIdGenerator> objectIdGenerator = MyObjectIdGenerator.class;
        Class<MyObjectIdResolver> objectIdResolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, true, objectIdResolver);

        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
    }
}