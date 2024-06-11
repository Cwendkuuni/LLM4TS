package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidInput() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Valid class, should not throw exception
        type = new JavaType(String.class);
        subTypeValidator.validateSubType(ctxt, type);

        // Valid class, should not throw exception
        type = new JavaType(Integer.class);
        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidInput() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Invalid class, should throw exception
        type = new JavaType(org.apache.commons.collections.functors.InvokerTransformer.class);
        subTypeValidator.validateSubType(ctxt, type);

        // Invalid class, should throw exception
        type = new JavaType(org.springframework.beans.factory.ObjectFactory.class);
        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test
    public void test_Get_cfgIllegalClassNames() {
        Set<String> expected = new HashSet<String>();
        expected.add("org.apache.commons.collections.functors.InvokerTransformer");
        expected.add("org.apache.commons.collections.functors.InstantiateTransformer");
        expected.add("org.apache.commons.collections4.functors.InvokerTransformer");
        expected.add("org.apache.commons.collections4.functors.InstantiateTransformer");
        expected.add("org.codehaus.groovy.runtime.ConvertedClosure");
        expected.add("org.codehaus.groovy.runtime.MethodClosure");
        expected.add("org.springframework.beans.factory.ObjectFactory");
        expected.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        expected.add("org.apache.xalan.xsltc.trax.TemplatesImpl");
        expected.add("com.sun.rowset.JdbcRowSetImpl");
        expected.add("java.util.logging.FileHandler");
        expected.add("java.rmi.server.UnicastRemoteObject");
        expected.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        expected.add("com.mchange.v2.c3p0.JndiRefForwardingDataSource");
        expected.add("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource");
        expected.add("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
        expected.add("com.sun.org.apache.bcel.internal.util.ClassLoader");

        Set<String> actual = subTypeValidator._cfgIllegalClassNames;

        assertEquals(expected, actual);
    }

    @Test
    public void test_Get_DefaultNoDeserClassNames() {
        Set<String> expected = new HashSet<String>();
        expected.add("org.apache.commons.collections.functors.InvokerTransformer");
        expected.add("org.apache.commons.collections.functors.InstantiateTransformer");
        expected.add("org.apache.commons.collections4.functors.InvokerTransformer");
        expected.add("org.apache.commons.collections4.functors.InstantiateTransformer");
        expected.add("org.codehaus.groovy.runtime.ConvertedClosure");
        expected.add("org.codehaus.groovy.runtime.MethodClosure");
        expected.add("org.springframework.beans.factory.ObjectFactory");
        expected.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        expected.add("org.apache.xalan.xsltc.trax.TemplatesImpl");
        expected.add("com.sun.rowset.JdbcRowSetImpl");
        expected.add("java.util.logging.FileHandler");
        expected.add("java.rmi.server.UnicastRemoteObject");
        expected.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        expected.add("com.mchange.v2.c3p0.JndiRefForwardingDataSource");
        expected.add("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource");
        expected.add("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
        expected.add("com.sun.org.apache.bcel.internal.util.ClassLoader");

        Set<String> actual = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;

        assertEquals(expected, actual);
    }
}