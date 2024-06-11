package com.fasterxml.jackson.databind.introspect;


import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void ObjectIdInfoTest_constructor_valid() {
        // Test constructor with valid arguments
    }

    @Test
    public void ObjectIdInfoTest_getPropertyName() {
        // Test getPropertyName() method
    }

    @Test
    public void ObjectIdInfoTest_getScope() {
        // Test getScope() method
    }

    @Test
    public void ObjectIdInfoTest_getGeneratorType() {
        // Test getGeneratorType() method
    }

    @Test
    public void ObjectIdInfoTest_getResolverType() {
        // Test getResolverType() method
    }

    @Test
    public void ObjectIdInfoTest_getAlwaysAsId() {
        // Test getAlwaysAsId() method
    }

    @Test
    public void ObjectIdInfoTest_withAlwaysAsId() {
        // Test withAlwaysAsId(boolean) method
    }

    @Test
    public void ObjectIdInfoTest_constructor_null() {
        // Test constructors with null arguments
    }

    @Test
    public void ObjectIdInfoTest_constructor_invalid() {
        // Test constructors with invalid arguments
    }

    @Test(expected = NullPointerException.class)
    public void ObjectIdInfoTest_getPropertyName_null() {
        // Test getPropertyName() method with null PropertyName
    }

    @Test(expected = NullPointerException.class)
    public void ObjectIdInfoTest_getScope_null() {
        // Test getScope() method with null scope
    }

    @Test(expected = NullPointerException.class)
    public void ObjectIdInfoTest_getGeneratorType_null() {
        // Test getGeneratorType() method with null generatorType
    }

    @Test(expected = NullPointerException.class)
    public void ObjectIdInfoTest_getResolverType_null() {
        // Test getResolverType() method with null resolverType
    }

    @Test(expected = IllegalArgumentException.class)
    public void ObjectIdInfoTest_withAlwaysAsId_invalid() {
        // Test withAlwaysAsId(boolean) method with invalid boolean value
    }

    
}