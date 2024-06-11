package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setUp() {
        ctxt = new DeserializationContext(null, null, null);
        type = null;
    }

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        type = new JavaType(SubTypeValidator.class);
        SubTypeValidator.instance().validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() throws JsonMappingException {
        type = new JavaType(org.apache.commons.collections.functors.InvokerTransformer.class);
        SubTypeValidator.instance().validateSubType(ctxt, type);
    }

    @Test
    public void testCfgIllegalClassNames() {
        Set<String> illegalClassNames = SubTypeValidator.instance().getCfgIllegalClassNames();
        assertTrue(illegalClassNames.contains("org.apache.commons.collections.functors.InvokerTransformer"));
        assertTrue(illegalClassNames.contains("org.apache.commons.collections.functors.InstantiateTransformer"));
        assertTrue(illegalClassNames.contains("org.apache.commons.collections4.functors.InvokerTransformer"));
        assertTrue(illegalClassNames.contains("org.apache.commons.collections4.functors.InstantiateTransformer"));
        assertTrue(illegalClassNames.contains("org.codehaus.groovy.runtime.ConvertedClosure"));
        assertTrue(illegalClassNames.contains("org.codehaus.groovy.runtime.MethodClosure"));
        assertTrue(illegalClassNames.contains("org.springframework.beans.factory.ObjectFactory"));
        assertTrue(illegalClassNames.contains("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl"));
        assertTrue(illegalClassNames.contains("org.apache.xalan.xsltc.trax.TemplatesImpl"));
        assertTrue(illegalClassNames.contains("com.sun.rowset.JdbcRowSetImpl"));
        assertTrue(illegalClassNames.contains("java.util.logging.FileHandler"));
        assertTrue(illegalClassNames.contains("java.rmi.server.UnicastRemoteObject"));
        assertTrue(illegalClassNames.contains("org.springframework.beans.factory.config.PropertyPathFactoryBean"));
        assertTrue(illegalClassNames.contains("org.apache.tomcat.dbcp.dbcp2.BasicDataSource"));
        assertTrue(illegalClassNames.contains("com.sun.org.apache.bcel.internal.util.ClassLoader"));
    }
}