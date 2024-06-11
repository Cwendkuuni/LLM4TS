package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithValidTypes() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());
        JavaType javaType1 = new TypeFactory().constructType(String.class);
        JavaType javaType2 = new TypeFactory().constructType(Integer.class);
        JavaType javaType3 = new TypeFactory().constructType(SubTypeValidator.class);

        validator.validateSubType(ctxt, javaType1);
        validator.validateSubType(ctxt, javaType2);
        validator.validateSubType(ctxt, javaType3);
    }

    @Test
    public void testValidateSubTypeWithNastyClasses() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        for (String className : SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES) {
            try {
                JavaType javaType = new TypeFactory().constructType(className);
                validator.validateSubType(ctxt, javaType);
                // This should not reach here
                assertFalse("Should not reach here for " + className, true);
            } catch (JsonMappingException e) {
                // Expected
            }
        }
    }

    @Test
    public void testValidateSubTypeWithSpringApplicationContext() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaType = new TypeFactory().constructType("org.springframework.context.ApplicationContext");

        try {
            validator.validateSubType(ctxt, javaType);
            // This should not reach here
            assertFalse("Should not reach here for ApplicationContext", true);
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.context.ApplicationContext) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubTypeWithSpringAbstractApplicationContext() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaType = new TypeFactory().constructType("org.springframework.context.AbstractApplicationContext");

        try {
            validator.validateSubType(ctxt, javaType);
            // This should not reach here
            assertFalse("Should not reach here for AbstractApplicationContext", true);
        } catch (JsonMappingException e) {
            // Expected, but check the message
            assertEquals("Illegal type (org.springframework.context.AbstractApplicationContext) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubTypeWithSpringAbstractPointcutAdvisor() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaType = new TypeFactory().constructType("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");

        try {
            validator.validateSubType(ctxt, javaType);
            // This should not reach here
            assertFalse("Should not reach here for AbstractBeanFactoryPointcutAdvisor", true);
        } catch (JsonMappingException e) {
            // Expected, but check the message
            assertEquals("Illegal type (org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubTypeWithSpringPropertyPathFactoryBean() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaType = new TypeFactory().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");

        try {
            validator.validateSubType(ctxt, javaType);
            // This should not reach here
            assertFalse("Should not reach here for PropertyPathFactoryBean", true);
        } catch (JsonMappingException e) {
            // Expected, but check the message
            assertEquals("Illegal type (org.springframework.beans.factory.config.PropertyPathFactoryBean) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubTypeWithJndiRefForwardingDataSource() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaType = new TypeFactory().constructType("com.mchange.v2.c3p0.JndiRefForwardingDataSource");

        try {
            validator.validateSubType(ctxt, javaType);
            // This should not reach here
            assertFalse("Should not reach here for JndiRefForwardingDataSource", true);
        } catch (JsonMappingException e) {
            // Expected, but check the message
            assertEquals("Illegal type (com.mchange.v2.c3p0.JndiRefForwardingDataSource) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubTypeWithComboPooledDataSource() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaType = new TypeFactory().constructType("com.mchange.v2.c3p0.ComboPooledDataSource");

        try {
            validator.validateSubType(ctxt, javaType);
            // This should not reach here
            assert False("Should not reach here for ComboPooledDataSource", true);
        } catch (JsonMappingException e) {
            // Expected, but check the message
            assertEquals("Illegal type (com.mchange.v2.c3p0.ComboPooledDataSource) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubTypeWithNull() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaType = null;

        try {
            validator.validateSubType(ctxt, javaType);
            fail("Should throw JsonMappingException for null type");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testValidateSubTypeWithEmptyArray() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaType = new TypeFactory().constructType(Collections.emptyList().getClass().getComponentType());

        try {
            validator.validateSubType(ctxt, javaType);
            fail("Should throw JsonMappingException for empty array type");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testValidateSubTypeWithPrimitiveTypes() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaTypeInt = new TypeFactory().constructType(int.class);
        JavaType javaTypeBoolean = new TypeFactory().constructType(boolean.class);

        validator.validateSubType(ctxt, javaTypeInt);
        validator.validateSubType(ctxt, javaTypeBoolean);
    }

    @Test
    public void testValidateSubTypeWithGenericType() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaTypeListOfInt = new TypeFactory().constructType(Collections.list(int.class).getTypeName());

        validator.validateSubType(ctxt, javaTypeListOfInt);
    }

    @Test
    public void testValidateSubTypeWithCustomClass() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaTypeCustom = new TypeFactory().constructType("com.example.MyCustomClass");

        try {
            validator.validateSubType(ctxt, javaTypeCustom);
            // This should not reach here
            assertFalse("Should not reach here for MyCustomClass", true);
        } catch (JsonMappingException e) {
            // Expected, but check the message
            assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, javaTypeCustom));
            assertEquals("Illegal type (com.example.MyCustomClass) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubTypeWithInvalidClassHierarchy() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(new TestContextResolver(), new ObjectMapper());

        JavaType javaTypeBase = new TypeFactory().constructType("com.example.MyBaseClass");
        JavaType javaTypeDerived = new TypeFactory().constructType("com.example.MyDerivedClass");

        try {
            validator.validateSubType(ctxt, javaTypeDerived);
            // This should not reach here
            assertFalse("Should not reach here for MyDerivedClass", true);
        } catch (JsonMappingException e) {
            // Expected, but check the message
            assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, javaTypeDerived));
            assertEquals("Illegal type (com.example.MyDerivedClass) to deserialize: prevented for security reasons", e.getMessage());
        }

        try {
            validator.validateSubType(ctxt, javaTypeBase);
            // This should not reach here
            assertFalse("Should not reach here for MyBaseClass", true);
        } catch (JsonMappingException e) {
            // Expected, but check the message
            assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, javaTypeBase));
            assertEquals("Illegal type (com.example.MyBaseClass) to deserialize: prevented for security reasons", e.getMessage());
        }

        try {
            validator.validateSubType(ctxt, javaTypeBase.getRawType());
            fail("Should throw JsonMappingException for raw type MyBaseClass");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            validator.validateSubType(ctxt, javaTypeDerived.getComponentType());
            fail("Should throw JsonMappingException for component type");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            validator.validateSubType(ctxt, javaTypeDerived.getGenericSuperclass());
            fail("Should throw JsonMappingException for generic superclass");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            validator.validateSubType(ctxt, javaTypeBase.getGenericSuperclass());
            fail("Should throw JsonMappingException for generic superclass");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}