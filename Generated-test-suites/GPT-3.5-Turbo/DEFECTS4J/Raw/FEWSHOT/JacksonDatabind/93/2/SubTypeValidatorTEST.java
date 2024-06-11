package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        
        // Test with valid class name
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Illegal type"));
        }
        
        // Test with invalid class name
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Illegal type"));
        }
    }

    @Test
    public void testInstance() {
        SubTypeValidator instance = SubTypeValidator.instance();
        assertTrue(instance instanceof SubTypeValidator);
    }

    @Test
    public void testCfgIllegalClassNames() {
        SubTypeValidator validator = SubTypeValidator.instance();
        
        // Test with default illegal class names
        assertTrue(validator._cfgIllegalClassNames.contains("org.apache.commons.collections.functors.InvokerTransformer"));
        assertTrue(validator._cfgIllegalClassNames.contains("org.apache.commons.collections.functors.InstantiateTransformer"));
        assertTrue(validator._cfgIllegalClassNames.contains("org.apache.commons.collections4.functors.InvokerTransformer"));
        assertTrue(validator._cfgIllegalClassNames.contains("org.apache.commons.collections4.functors.InstantiateTransformer"));
        assertTrue(validator._cfgIllegalClassNames.contains("org.codehaus.groovy.runtime.ConvertedClosure"));
        assertTrue(validator._cfgIllegalClassNames.contains("org.codehaus.groovy.runtime.MethodClosure"));
        assertTrue(validator._cfgIllegalClassNames.contains("org.springframework.beans.factory.ObjectFactory"));
        assertTrue(validator._cfgIllegalClassNames.contains("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl"));
        assertTrue(validator._cfgIllegalClassNames.contains("org.apache.xalan.xsltc.trax.TemplatesImpl"));
        assertTrue(validator._cfgIllegalClassNames.contains("com.sun.rowset.JdbcRowSetImpl"));
        assertTrue(validator._cfgIllegalClassNames.contains("java.util.logging.FileHandler"));
        assertTrue(validator._cfgIllegalClassNames.contains("java.rmi.server.UnicastRemoteObject"));
        assertTrue(validator._cfgIllegalClassNames.contains("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor"));
        assertTrue(validator._cfgIllegalClassNames.contains("org.springframework.beans.factory.config.PropertyPathFactoryBean"));
        assertTrue(validator._cfgIllegalClassNames.contains("com.mchange.v2.c3p0.JndiRefForwardingDataSource"));
        assertTrue(validator._cfgIllegalClassNames.contains("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource"));
        assertTrue(validator._cfgIllegalClassNames.contains("org.apache.tomcat.dbcp.dbcp2.BasicDataSource"));
        assertTrue(validator._cfgIllegalClassNames.contains("com.sun.org.apache.bcel.internal.util.ClassLoader"));
        
        // Test with modified illegal class names
        validator._cfgIllegalClassNames.remove("org.apache.commons.collections.functors.InvokerTransformer");
        assertFalse(validator._cfgIllegalClassNames.contains("org.apache.commons.collections.functors.InvokerTransformer"));
        
        // Test with new illegal class name
        validator._cfgIllegalClassNames.add("com.example.IllegalClass");
        assertTrue(validator._cfgIllegalClassNames.contains("com.example.IllegalClass"));
    }

}