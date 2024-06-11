package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

    private static final SubTypeValidator instance = SubTypeValidator.instance();

    @Test
    public void testValidTypes() throws JsonMappingException {
        // Test valid types
        JavaType validType1 = TypeFactory.defaultInstance().constructType(String.class);
        instance.validateSubType(null, validType1);

        JavaType validType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        instance.validateSubType(null, validType2);

        JavaType validType3 = TypeFactory.defaultInstance().constructType(java.util.List.class);
        instance.validateSubType(null, validType3);
    }

    @Test
    public void testSpringTypes() throws JsonMappingException {
        // Test Spring types
        Class<?> springType1 = Class.forName("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        JavaType springType1Type = TypeFactory.defaultInstance().constructType(springType1);
        assertThrows(JsonMappingException.class, () -> instance.validateSubType(null, springType1Type));

        Class<?> springType2 = Class.forName("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
        JavaType springType2Type = TypeFactory.defaultInstance().constructType(springType2);
        assertThrows(JsonMappingException.class, () -> instance.validateSubType(null, springType2Type));
    }

    @Test
    public void testNastyClasses() throws JsonMappingException {
        // Test nasty classes
        Class<?> nastyClass1 = Class.forName("org.apache.commons.collections4.functors.InstantiateTransformer");
        JavaType nastyClass1Type = TypeFactory.defaultInstance().constructType(nastyClass1);
        assertThrows(JsonMappingException.class, () -> instance.validateSubType(null, nastyClass1Type));

        Class<?> nastyClass2 = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        JavaType nastyClass2Type = TypeFactory.defaultInstance().constructType(nastyClass2);
        assertThrows(JsonMappingException.class, () -> instance.validateSubType(null, nastyClass2Type));
    }

    @Test
    public void testInterfaceTypes() throws JsonMappingException {
        // Test interface types
        Class<?> interfaceType = Class.forName("java.io.Serializable");
        JavaType interfaceTypeJavaType = TypeFactory.defaultInstance().constructType(interfaceType);
        instance.validateSubType(null, interfaceTypeJavaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidType() throws JsonMappingException {
        // Test invalid type
        Class<?> invalidType = Class.forName("java.lang.Runtime");
        JavaType invalidTypeJavaType = TypeFactory.defaultInstance().constructType(invalidType);
        instance.validateSubType(null, invalidTypeJavaType);
    }
}