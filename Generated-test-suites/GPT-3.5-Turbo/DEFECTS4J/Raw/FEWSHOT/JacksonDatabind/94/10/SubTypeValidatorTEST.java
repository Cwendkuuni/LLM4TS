package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class SubTypeValidatorTest {

    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setup() {
        ctxt = new DeserializationContext();
        type = new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return null;
            }

            @Override
            public Class<?> getParameterSource() {
                return null;
            }

            @Override
            public Object getTypeHandler() {
                return null;
            }

            @Override
            public boolean hasRawClass(Class<?> clazz) {
                return false;
            }

            @Override
            public boolean hasGenericTypes() {
                return false;
            }
        };
    }

    @Test
    public void testInstance() {
        assertNotNull(SubTypeValidator.instance());
    }

    @Test
    public void testValidateSubType_ValidType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_ExceptionMessage() {
        SubTypeValidator validator = SubTypeValidator.instance();
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testIllegalClassNames() {
        SubTypeValidator validator = SubTypeValidator.instance();
        Set<String> illegalClassNames = validator._cfgIllegalClassNames;
        assertNotNull(illegalClassNames);
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