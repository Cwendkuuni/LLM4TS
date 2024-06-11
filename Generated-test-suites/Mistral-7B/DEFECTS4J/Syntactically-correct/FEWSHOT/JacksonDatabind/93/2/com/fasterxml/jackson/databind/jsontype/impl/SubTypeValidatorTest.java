package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

    private static DeserializationContext context;
    private static JavaType type;

    static {
        context = new TestContext();
        try {
            type = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testValidateSubType_validType() throws JsonMappingException {
        new SubTypeValidator().validateSubType(context, type);
    }

    @Test
    public void testValidateSubType_invalidType() throws JsonMappingException {
        String invalidClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
        JavaType invalidType = TypeFactory.defaultInstance().constructType(invalidClassName);

        assertThrows(JsonMappingException.class, () -> new SubTypeValidator().validateSubType(context, invalidType));
    }

    @Test
    public void testValidateSubType_springClassWithSubclass() throws JsonMappingException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String springBaseClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
        Class<?> springBaseClass = Class.forName(springBaseClassName);
        JavaType springBaseType = TypeFactory.defaultInstance().constructType(springBaseClass);

        String springSubclassName = "com.mchange.v2.c3p0.WrapperConnectionPoolDataSource";
        Class<?> springSubclass = Class.forName(springSubclassName);

        JavaType springSubType = TypeFactory.defaultInstance().constructType(springSubclass);

        // Test with a valid subclass
        new SubTypeValidator().validateSubType(context, springSubType);

        // Test with an invalid subclass
        String invalidSubclassName = "com.sun.org.apache.bcel.internal.util.ClassLoader";
        Class<?> invalidSubclass = Class.forName(invalidSubclassName);
        JavaType invalidSubType = TypeFactory.defaultInstance().constructType(invalidSubclass);

        assertThrows(JsonMappingException.class, () -> new SubTypeValidator().validateSubType(context, invalidSubType));
    }
}

class TestContext extends DeserializationContext {
    public TestContext() {
        super(null, null, null, null, null, Collections.emptyMap(), null);
    }
}