package com.example.objectidinfo; // replace with your test package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;

public class ObjectIdInfoTest {

    // Test constructor with required arguments
    @Test
    public void testObjectIdInfoConstructorRequiredArgs() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.IntSequenceGenerator.class);
        assertNotNull(objectIdInfo);
        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(Object.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerators.IntSequenceGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test constructor with optional arguments
    @Test
    public void testObjectIdInfoConstructorOptionalArgs() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.IntSequenceGenerator.class, false, SimpleObjectIdResolver.class);
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    // Test constructor with String name
    @Test
    public void testObjectIdInfoConstructorString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.IntSequenceGenerator.class);
        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
    }

    // Test method getAlwaysAsId with default value
    @Test
    public void testGetAlwaysAsIdDefaultValue() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.IntSequenceGenerator.class);
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test method getAlwaysAsId with custom value
    @Test
    public void testGetAlwaysAsIdCustomValue() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.IntSequenceGenerator.class, true);
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    // Test method getPropertyName with null propertyName
    @Test(expected = NullPointerException.class)
    public void testGetPropertyNameNullPropertyName() {
        new ObjectIdInfo(null, Object.class, ObjectIdGenerators.IntSequenceGenerator.class);
    }

    // Test method getScope with null scope
    @Test(expected = NullPointerException.class)
    public void testGetScopeNullScope() {
        new ObjectIdInfo(new PropertyName("testProperty"), null, ObjectIdGenerators.IntSequenceGenerator.class);
    }

    // Test method getGeneratorType with null generatorType
    @Test(expected = NullPointerException.class)
    public void testGetGeneratorTypeNullGeneratorType() {
        new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null);
    }

    // Test method getResolverType with null resolverType
    @Test(expected = NullPointerException.class)
    public void testGetResolverTypeNullResolverType() {
        new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.IntSequenceGenerator.class, false, null);
    }
}