package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

    private static final SubTypeValidator VALIDATOR = SubTypeValidator.instance();

    @Test
    public void testValidClasses() throws JsonMappingException {
        // Test with valid classes
        JavaType validType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType validType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType validType3 = TypeFactory.defaultInstance().constructType(Double.class);

        VALIDATOR.validateSubType(null, validType1);
        VALIDATOR.validateSubType(null, validType2);
        VALIDATOR.validateSubType(null, validType3);
    }

    @Test
    public void testSpringClasses() throws JsonMappingException {
        // Test with Spring classes
        Class<?> springClass1 = org.springframework.core.io.support.Resource.class;
        JavaType springType1 = TypeFactory.defaultInstance().constructType(springClass1);

        VALIDATOR.validateSubType(null, springType1);

        Class<?> springClass2 = org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.class;
        JavaType springType2 = TypeFactory.defaultInstance().constructType(springClass2);

        VALIDATOR.validateSubType(null, springType2);
    }

    @Test
    public void testIllegalClasses() {
        // Test with illegal classes
        assertThrows(JsonMappingException.class, () -> {
            Class<?> illegalClass1 = org.apache.commons.collections4.functors.InstantiateTransformer.class;
            JavaType illegalType1 = TypeFactory.defaultInstance().constructType(illegalClass1);

            SubTypeValidator validator = SubTypeValidator.instance();
            validator.validateSubType(null, illegalType1);
        });

        assertThrows(JsonMappingException.class, () -> {
            Class<?> illegalClass2 = java.rmi.server.UnicastRemoteObject.class;
            JavaType illegalType2 = TypeFactory.defaultInstance().constructType(illegalClass2);

            SubTypeValidator validator = SubTypeValidator.instance();
            validator.validateSubType(null, illegalType2);
        });
    }

    @Test
    public void testSpringSubclasses() {
        // Test with Spring subclasses
        Class<?> springSubclass1 = org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class;
        JavaType springSubType1 = TypeFactory.defaultInstance().constructType(springSubclass1);

        assertThrows(JsonMappingException.class, () -> {
            SubTypeValidator validator = SubTypeValidator.instance();
            validator.validateSubType(null, springSubType1);
        });

        Class<?> springSubclass2 = org.springframework.beans.factory.config.PropertyPathFactoryBean.class;
        JavaType springSubType2 = TypeFactory.defaultInstance().constructType(springSubclass2);

        assertThrows(JsonMappingException.class, () -> {
            SubTypeValidator validator = SubTypeValidator.instance();
            validator.validateSubType(null, springSubType2);
        });
    }

    @Test
    public void testSpringInterfaces() {
        // Test with Spring interfaces
        Class<?> springInterface1 = org.springframework.beans.factory.FactoryBean.class;
        JavaType springInterfaceType1 = TypeFactory.defaultInstance().constructType(springInterface1);

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, springInterfaceType1);
    }
}