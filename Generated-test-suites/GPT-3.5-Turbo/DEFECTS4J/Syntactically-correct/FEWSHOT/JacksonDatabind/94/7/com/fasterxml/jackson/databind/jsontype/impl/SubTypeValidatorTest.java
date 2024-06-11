package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {
    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_WithValidType_ShouldNotThrowException() throws JsonMappingException {
        JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
        subTypeValidator.validateSubType(null, validType); // Using null for the DeserializationContext since it is not used in this test
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_WithInvalidType_ShouldThrowException() throws JsonMappingException {
        JavaType invalidType = TypeFactory.defaultInstance().constructType(Object.class);
        subTypeValidator.validateSubType(null, invalidType); // Using null for the DeserializationContext since it is not used in this test
    }

    @Test
    public void testCfgIllegalClassNames_DefaultValue_ShouldContainExpectedClassNames() {
        Set<String> expectedClassNames = createDefaultIllegalClassNamesSet();
        Set<String> actualClassNames = subTypeValidator._cfgIllegalClassNames;
        assertEquals(expectedClassNames.size(), actualClassNames.size());
        assertTrue(actualClassNames.containsAll(expectedClassNames));
    }

    @Test
    public void testCfgIllegalClassNames_SetValue_ShouldContainExpectedClassNames() {
        Set<String> expectedClassNames = createCustomIllegalClassNamesSet();
        subTypeValidator._cfgIllegalClassNames = expectedClassNames;
        Set<String> actualClassNames = subTypeValidator._cfgIllegalClassNames;
        assertEquals(expectedClassNames.size(), actualClassNames.size());
        assertTrue(actualClassNames.containsAll(expectedClassNames));
    }

    @Test
    public void testInstance_ShouldReturnSingletonInstance() {
        SubTypeValidator instance1 = SubTypeValidator.instance();
        SubTypeValidator instance2 = SubTypeValidator.instance();
        assertEquals(instance1, instance2);
    }

    private Set<String> createDefaultIllegalClassNamesSet() {
        Set<String> illegalClassNames = new HashSet<String>();

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

        return Collections.unmodifiableSet(illegalClassNames);
    }

    private Set<String> createCustomIllegalClassNamesSet() {
        Set<String> illegalClassNames = new HashSet<String>();

        illegalClassNames.add("org.codehaus.groovy.runtime.metaclass.PropertySite.Get");
        illegalClassNames.add("org.codehaus.groovy.runtime.metaclass.PropertySite.Set");
        illegalClassNames.add("org.springframework.beans.factory.support.DisposableBeanAdapter");

        return Collections.unmodifiableSet(illegalClassNames);
    }
}