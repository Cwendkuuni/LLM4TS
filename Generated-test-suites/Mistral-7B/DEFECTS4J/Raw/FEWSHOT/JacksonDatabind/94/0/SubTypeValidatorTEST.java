package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithValidType() throws JsonProcessingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithInvalidType() throws JsonProcessingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        Set<String> illegalClassNames = new HashSet<>();
        illegalClassNames.add("org.apache.commons.collections.functors.InvokerTransformer");
        SubTypeValidator validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        validator.validateSubType(ctxt, type);

        // Following classes should throw JsonMappingException
        illegalClassNames.add("org.apache.commons.collections4.functors.InvokerTransformer");
        validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        validator.validateSubType(ctxt, type);

        illegalClassNames.add("org.codehaus.groovy.runtime.ConvertedClosure");
        validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        validator.validateSubType(ctxt, type);

        illegalClassNames.add("org.springframework.beans.factory.ObjectFactory");
        validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        validator.validateSubType(ctxt, type);

        illegalClassNames.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        validator.validateSubType(ctxt, type);

        illegalClassNames.add("org.apache.xalan.xsltc.trax.TemplatesImpl");
        validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        validator.validateSubType(ctxt, type);

        illegalClassNames.add("com.sun.rowset.JdbcRowSetImpl");
        validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        validator.validateSubType(ctxt, type);

        illegalClassNames.add("java.util.logging.FileHandler");
        validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        validator.validateSubType(ctxt, type);

        illegalClassNames.add("java.rmi.server.UnicastRemoteObject");
//      illegalClassNames.add("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");
        validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        validator.validateSubType(ctxt, type);

//      illegalClassNames.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");
//      illegalClassNames.add("com.mchange.v2.c3p0.JndiRefForwardingDataSource");
//      illegalClassNames.add("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource");
        illegalClassNames.add("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
        validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        validator.validateSubType(ctxt, type);

//      illegalClassNames.add("com.mchange.v2.c3p0.ComboPooledDataSource");
//      illegalClassNames.add("com.mchange.v2.c3p0.debug.AfterCloseLoggingComboPooledDataSource");

        String invalidTypeName = "org.apache.commons.collections.functors.InvokerTransformer";
        type = JavaType.construct(invalidTypeName, ctxt.getTypeFactory());
        validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }
}