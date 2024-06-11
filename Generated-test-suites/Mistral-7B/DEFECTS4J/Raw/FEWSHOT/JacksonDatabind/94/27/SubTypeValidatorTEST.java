package com.fasterxml.jackson.databind.jsontype.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonTest;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Test cases for SubTypeValidator class
 */
@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest extends JsonTest {

    private static final String PREFIX_SPRING = "org.springframework.";
    private static final Set<String> DEFAULT_NO_DESER_CLASS_NAMES;

    static {
        Set<String> s = new HashSet<>();
        // Courtesy of [https://github.com/kantega/notsoserial]:
        // (and wrt [databind#1599])
        s.add("org.apache.commons.collections.functors.InvokerTransformer");
        s.add("org.apache.commons.collections.functors.InstantiateTransformer");
        s.add("org.apache.commons.collections4.functors.InvokerTransformer");
        s.add("org.apache.commons.collections4.functors.InstantiateTransformer");
        s.add("org.codehaus.groovy.runtime.ConvertedClosure");
        s.add("org.codehaus.groovy.runtime.MethodClosure");
        s.add("org.springframework.beans.factory.ObjectFactory");
        s.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        s.add("org.apache.xalan.xsltc.trax.TemplatesImpl");
        // [databind#1680]: may or may not be problem, take no chance
        s.add("com.sun.rowset.JdbcRowSetImpl");
        // [databind#1737]; JDK provided
        s.add("java.util.logging.FileHandler");
        s.add("java.rmi.server.UnicastRemoteObject");
        // [databind#1737]; 3rd party
        s.add("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");
        s.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        s.add("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
        s.add("com.sun.org.apache.bcel.internal.util.ClassLoader");

        DEFAULT_NO_DESER_CLASS_NAMES = Collections.unmodifiableSet(s);
    }

    private SubTypeValidator _validator;

    @Before
    public void setUp() throws Exception {
        _validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubTypeWithValidType() throws Exception {
        DeserializationContext ctx = context();
        JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
        _validator.validateSubType(ctx, validType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithDisallowedType() throws Exception {
        DeserializationContext ctx = context();
        JavaType disallowedType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
        _validator.validateSubType(ctx, disallowedType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithSpringType() throws Exception {
        DeserializationContext ctx = context();
        JavaType springType = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.ObjectFactory.class);
        _validator.validateSubType(ctx, springType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithSpringApplicationContext() throws Exception {
        DeserializationContext ctx = context();
        JavaType springApplicationContextType = TypeFactory.defaultInstance().constructType(org.springframework.context.ApplicationContext.class);
        _validator.validateSubType(ctx, springApplicationContextType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWith3rdPartyType() throws Exception {
        DeserializationContext ctx = context();
        JavaType thirdPartyType = TypeFactory.defaultInstance().constructType(com.mchange.v2.c3p0.ComboPooledDataSource.class);
        _validator.validateSubType(ctx, thirdPartyType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithNested3rdPartyType() throws Exception {
        DeserializationContext ctx = context();
        JavaType nested3rdPartyType = TypeFactory.defaultInstance().constructType(com.mchange.v2.c3p0.debug.AfterCloseLoggingComboPooledDataSource.class);
        _validator.validateSubType(ctx, nested3rdPartyType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithCustomDisallowedType() throws Exception {
        DeserializationContext ctx = context();
        JavaType customDisallowedType = TypeFactory.defaultInstance().constructType(CustomDisallowedType.class);
        _validator.validateSubType(ctx, customDisallowedType);
    }

    @Test
    public void testGetDefaultNoDeserClassNameset() {
        assertEquals(DEFAULT_NO_DESER_CLASS_NAMES, SubTypeValidator.instance()._cfgIllegalClassNames);
    }

    private static class CustomDisallowedType {
    }
}