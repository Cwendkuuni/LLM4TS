package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for SubTypeValidator
 *
 * @author John Doe
 */
public class SubTypeValidatorTest {

    private static ObjectMapper objectMapper;
    private static SubTypeValidator validator;

    @BeforeClass
    public static void setup() {
        objectMapper = new ObjectMapper();
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_NullType() {
        // Test case for null type
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, null));
    }

    @Test
    public void testValidateSubType_ValidType() {
        // Test case for valid type
        JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
        validator.validateSubType(objectMapper.getDeserializationContext(), validType);
    }

    @Test
    public void testValidateSubType_NastyClassNames() {
        // Test case for nasty class names
        JavaType nastyType1 = TypeFactory.defaultInstance().constructType(org.apache.commons.collections.functors.InvokerTransformer.class);
        JavaType nastyType2 = TypeFactory.defaultInstance().constructType(com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.class);
        JavaType nastyType3 = TypeFactory.defaultInstance().constructType(com.sun.rowset.JdbcRowSetImpl.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), nastyType1));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), nastyType2));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), nastyType3));
    }

    @Test
    public void testValidateSubType_SpringFrameworkClasses() {
        // Test case for Spring framework classes
        JavaType springType1 = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
        JavaType springType2 = TypeFactory.defaultInstance().constructType(org.apache.tomcat.dbcp.dbcp2.BasicDataSource.class);

        // Check for AbstractPointcutAdvisor and AbstractApplicationContext
        JavaType springType3 = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.AbstractBeanFactoryPointcutAdvisor.class);
        JavaType springType4 = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.AbstractApplicationContext.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springType1));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springType2));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springType3));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springType4));

        // Check for other Spring framework classes
        JavaType springType5 = TypeFactory.defaultInstance().constructType(com.mchange.v2.c3p0.ComboPooledDataSource.class);
        JavaType springType6 = TypeFactory.defaultInstance().constructType(com.mchange.v2.c3p0.debug.AfterCloseLoggingComboPooledDataSource.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springType5));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springType6));
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() {
        // Test case for illegal type
        JavaType illegalType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
        validator.validateSubType(objectMapper.getDeserializationContext(), illegalType);
    }

}