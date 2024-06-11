package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test cases for SubTypeValidator class
 */
@RunWith(PowerMockRunner.class)
public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_allowedClasses() throws Exception {
        final DeserializationContext ctxt = new DeserializationContext(new JavaClassContext(), new StandardDeserializer.NullValueProvider(), JavaTypeFactory.defaultInstance());
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_disallowedClasses() throws Exception {
        final DeserializationContext ctxt = new DeserializationContext(new JavaClassContext(), new StandardDeserializer.NullValueProvider(), JavaTypeFactory.defaultInstance());

        Set<String> disallowedClassNames = new HashSet<String>() {{
            addAll(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
        }};
        SubTypeValidator validator = new SubTypeValidator() {{
            _cfgIllegalClassNames = disallowedClassNames;
        }};

        // Test cases for 'String' class and its super classes
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        validator.validateSubType(ctxt, stringType);

        Class<?> stringClass = String.class;
        JavaType stringRawType = JavaTypeFactory.defaultInstance().constructType(stringClass);
        validator.validateSubType(ctxt, stringRawType);

        Class<?> objectClass = Object.class;
        JavaType objectRawType = JavaTypeFactory.defaultInstance().constructType(objectClass);
        validator.validateSubType(ctxt, objectRawType);

        // Test cases for disallowed classes and their super classes
        Class<?> springBeanFactoryPointcutAdvisorClass = Class.forName("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");
        JavaType springBeanFactoryPointcutAdvisorType = JavaTypeFactory.defaultInstance().constructType(springBeanFactoryPointcutAdvisorClass);
        validator.validateSubType(ctxt, springBeanFactoryPointcutAdvisorType);

        Class<?> springAbstractApplicationContextClass = Class.forName("org.springframework.beans.factory.config.AbstractApplicationContext");
        JavaType springAbstractApplicationContextType = JavaTypeFactory.defaultInstance().constructType(springAbstractApplicationContextClass);
        validator.validateSubType(ctxt, springAbstractApplicationContextType);

        Class<?> javaUtilLoggingFileHandlerClass = Class.forName("java.util.logging.FileHandler");
        JavaType javaUtilLoggingFileHandlerType = JavaTypeFactory.defaultInstance().constructType(javaUtilLoggingFileHandlerClass);
        validator.validateSubType(ctxt, javaUtilLoggingFileHandlerType);

        Class<?> c3p0JndiRefForwardingDataSourceClass = Class.forName("com.mchange.v2.c3p0.JndiRefForwardingDataSource");
        JavaType c3p0JndiRefForwardingDataSourceType = JavaTypeFactory.defaultInstance().constructType(c3p0JndiRefForwardingDataSourceClass);
        validator.validateSubType(ctxt, c3p0JndiRefForwardingDataSourceType);

        Class<?> c3p0WrapperConnectionPoolDataSourceClass = Class.forName("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource");
        JavaType c3p0WrapperConnectionPoolDataSourceType = JavaTypeFactory.defaultInstance().constructType(c3p0WrapperConnectionPoolDataSourceClass);
        validator.validateSubType(ctxt, c3p0WrapperConnectionPoolDataSourceType);

        Class<?> apacheTomcatDbcpDataSourceClass = Class.forName("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
        JavaType apacheTomcatDbcpDataSourceType = JavaTypeFactory.defaultInstance().constructType(apacheTomcatDbcpDataSourceClass);
        validator.validateSubType(ctxt, apacheTomcatDbcpDataSourceType);

        // Test case for 'java.lang.Class' and its subclasses
        Class<?> classClass = Class.class;
        JavaType classRawType = JavaTypeFactory.defaultInstance().constructType(classClass);
        validator.validateSubType(ctxt, classRawType);

        Class<?> classloaderClass = ClassLoader.class;
        JavaType classloaderRawType = JavaTypeFactory.defaultInstance().constructType(classloaderClass);
        validator.validateSubType(ctxt, classloaderRawType);

        // Test case for 'java.lang.reflect.Method' and its subclasses
        Class<?> methodClass = Method.class;
        JavaType methodRawType = JavaTypeFactory.defaultInstance().constructType(methodClass);
        validator.validateSubType(ctxt, methodRawType);

        Class<?> methodClassWithGenericTypeArgument = Method.class.getGenericSuperclass();
        JavaType methodWithGenericTypeArgumentRawType = JavaTypeFactory.defaultInstance().constructType(methodClassWithGenericTypeArgument);
        validator.validateSubType(ctxt, methodWithGenericTypeArgumentRawType);

        // Test case for JavaType and its subclasses
        Class<?> javaTypeClass = JavaType.class;
        JavaType javaTypeRawType = JavaTypeFactory.defaultInstance().constructType(javaTypeClass);
        validator.validateSubType(ctxt, javaTypeRawType);

        Class<?> typeFactoryClass = TypeFactory.class;
        JavaType typeFactoryRawType = JavaTypeFactory.defaultInstance().constructType(typeFactoryClass);
        validator.validateSubType(ctxt, typeFactoryRawType);

        // Test case for 'com.fasterxml.jackson.databind.JsonMappingException'
        Class<?> jsonMappingExceptionClass = JsonMappingException.class;
        JavaType jsonMappingExceptionRawType = JavaTypeFactory.defaultInstance().constructType(jsonMappingExceptionClass);
        validator.validateSubType(ctxt, jsonMappingExceptionRawType);
    }

    @Test
    public void testValidateSubType_throwsJsonMappingException() throws Exception {
        final DeserializationContext ctxt = new DeserializationContext(new JavaClassContext(), new StandardDeserializer.NullValueProvider(), JavaTypeFactory.defaultInstance());
        SubTypeValidator validator = SubTypeValidator.instance();

        Class<?> disallowedClassName = Class.forName("org.apache.commons.cli.Util");
        JavaType disallowedType = JavaTypeFactory.defaultInstance().constructType(disallowedClassName);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, disallowedType));
    }
}