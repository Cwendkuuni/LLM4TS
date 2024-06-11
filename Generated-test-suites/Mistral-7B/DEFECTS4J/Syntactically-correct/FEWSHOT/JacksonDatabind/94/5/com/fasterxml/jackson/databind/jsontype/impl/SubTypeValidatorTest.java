package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

import org.junit.Test;

public class SubTypeValidatorTest {

    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testValidateSubType() {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType type = mapper.getTypeFactory().constructType(String.class);

        // Test valid types
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, type);

        // Test Spring classes
        Class<?> springClass1 = Class.forName("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        JavaType springType1 = mapper.getTypeFactory().constructType(springClass1);
        validator.validateSubType(ctxt, springType1);

        // Test known dangerous classes
        String[] dangerousClassNames = {"org.apache.commons.collections.functors.InvokerTransformer",
                                        "org.apache.commons.collections4.functors.InvokerTransformer",
                                        "org.springframework.beans.factory.ObjectFactory",
                                        "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",
                                        "org.apache.xalan.xsltc.trax.TemplatesImpl",
                                        "com.sun.rowset.JdbcRowSetImpl",
                                        "java.util.logging.FileHandler",
                                        "java.rmi.server.UnicastRemoteObject",
                                        "org.apache.tomcat.dbcp.dbcp2.BasicDataSource",
                                        "com.sun.org.apache.bcel.internal.util.ClassLoader"};

        for (String dangerousClassName : dangerousClassNames) {
            try {
                Class<?> clazz = Class.forName(dangerousClassName);
                JavaType javaType = mapper.getTypeFactory().constructType(clazz);
                validator.validateSubType(ctxt, javaType);
                // This should not reach here
                assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, javaType));
            } catch (ClassNotFoundException e) {
                // Ignore, this exception is expected
            }
        }

        // Test interface
        Class<?> interfaceClass = Object.class;
        JavaType interfaceType = mapper.getTypeFactory().constructType(interfaceClass);
        validator.validateSubType(ctxt, interfaceType);
    }

    @Test
    public void testValidateSubTypeWithIllegalClassName() {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType type = mapper.getTypeFactory().constructType(String.class);

        String illegalClassName = "com.illegal.ClassName";
        Class<?> clazz = null;
        try {
            clazz = Class.forName(illegalClassName);
        } catch (ClassNotFoundException e) {
            // Ignore, this exception is expected
        }

        JavaType javaType = null;
        if (clazz != null) {
            javaType = mapper.getTypeFactory().constructType(clazz);
        }

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> {
            validator.validateSubType(ctxt, javaType);
        });
    }
}