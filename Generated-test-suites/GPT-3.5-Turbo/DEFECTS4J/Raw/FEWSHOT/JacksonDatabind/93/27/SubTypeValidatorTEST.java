package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;


public class SubTypeValidatorTest {
    
    @Test
    public void testValidateSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = null;
        
        // Test valid class
        type = JavaType.construct(SubTypeValidator.class);
        validator.validateSubType(ctxt, type);
        
        // Test a class with a normal package
        type = JavaType.construct(java.util.ArrayList.class);
        validator.validateSubType(ctxt, type);
        
        // Test classes in org.springframework package
        type = JavaType.construct(org.springframework.beans.factory.ObjectFactory.class);
        validator.validateSubType(ctxt, type);
        
        type = JavaType.construct(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);
        validator.validateSubType(ctxt, type);
        
        type = JavaType.construct(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
        validator.validateSubType(ctxt, type);
        
        type = JavaType.construct(com.mchange.v2.c3p0.JndiRefForwardingDataSource.class);
        validator.validateSubType(ctxt, type);
        
        type = JavaType.construct(com.mchange.v2.c3p0.WrapperConnectionPoolDataSource.class);
        validator.validateSubType(ctxt, type);
        
        type = JavaType.construct(org.apache.tomcat.dbcp.dbcp2.BasicDataSource.class);
        validator.validateSubType(ctxt, type);
        
        // Test a class from the nasty classs set
        type = JavaType.construct(org.apache.commons.collections.functors.InvokerTransformer.class);
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            String expectedMessage = "Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}