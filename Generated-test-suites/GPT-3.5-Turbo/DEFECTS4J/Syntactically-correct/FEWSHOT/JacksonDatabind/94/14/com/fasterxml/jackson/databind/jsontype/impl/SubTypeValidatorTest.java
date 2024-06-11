package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;
    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
        ctxt = new DeserializationContext();
        type = JavaType.fromClass(String.class);
    }

    @Test
    public void testInstanceReturnsSingletonInstance() {
        SubTypeValidator instance1 = SubTypeValidator.instance();
        SubTypeValidator instance2 = SubTypeValidator.instance();
        assertEquals(instance1, instance2);
    }

    @Test
    public void testValidateSubTypeThrowsExceptionForIllegalClassNames() {
        Set<String> illegalClassNames = new HashSet<>();
        illegalClassNames.add("org.apache.commons.collections.functors.InvokerTransformer");
        illegalClassNames.add("org.apache.commons.collections.functors.InstantiateTransformer");
        illegalClassNames.add("org.apache.commons.collections4.functors.InvokerTransformer");
        illegalClassNames.add("org.apache.commons.collections4.functors.InstantiateTransformer");
        illegalClassNames.add("org.codehaus.groovy.runtime.ConvertedClosure");
        illegalClassNames.add("org.codehaus.groovy.runtime.MethodClosure");
        illegalClassNames.add("org.springframework.beans.factory.ObjectFactory");
        illegalClassNames.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        illegalClassNames.add("org.apache.xalan.xsltc.trax.TemplatesImpl");
        illegalClassNames.add("com.sun.rowset.JdbcRowSetImpl");
        illegalClassNames.add("java.util.logging.FileHandler");
        illegalClassNames.add("java.rmi.server.UnicastRemoteObject");
        illegalClassNames.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        illegalClassNames.add("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
        illegalClassNames.add("com.sun.org.apache.bcel.internal.util.ClassLoader");

        subTypeValidator._cfgIllegalClassNames = illegalClassNames;

        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Illegal type"));
        }
    }

    @Test
    public void testValidateSubTypeDoesNotThrowExceptionForValidClassNames() {
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }

}