
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        // test when class is in DEFAULT_NO_DESER_CLASS_NAMES set
        try {
            SubTypeValidator.instance().validateSubType(null, new JavaTypeStub("org.apache.commons.collections.functors.InvokerTransformer"));
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }

        // test when class is not in DEFAULT_NO_DESER_CLASS_NAMES set and is not from Spring framework
        try {
            SubTypeValidator.instance().validateSubType(null, new JavaTypeStub("com.fasterxml.jackson.databind.JavaType"));
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.fasterxml.jackson.databind.JavaType) to deserialize: prevented for security reasons", e.getMessage());
        }

        // test when class is from Spring framework and is an interface
        SubTypeValidator.instance().validateSubType(null, new JavaTypeStub("org.springframework.beans.factory.ObjectFactory"));

        // test when class is from Spring framework and is not an interface and has superclass in Spring framework
        try {
            SubTypeValidator.instance().validateSubType(null, new JavaTypeStub("org.springframework.beans.factory.config.PropertyPathFactoryBean"));
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.config.PropertyPathFactoryBean) to deserialize: prevented for security reasons", e.getMessage());
        }

        // test when class is from Spring framework and is not an interface and does not have superclass in Spring framework
        SubTypeValidator.instance().validateSubType(null, new JavaTypeStub("org.springframework.stereotype.Controller"));
    }
}

// Stub JavaType class used for testing
class JavaTypeStub extends JavaType {
    public JavaTypeStub(String className) {
        super(null, null, null, null, null);
        _class = this.getClassType(className);
    }

    private Class<?> getClassType(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
