package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * JUnit test for {@link SubTypeValidator} class.
 */
public class SubTypeValidatorTest {

    private static final SubTypeValidator SUB_TYPE_VALIDATOR = SubTypeValidator.instance();

    private DeserializationContext context;
    private TypeFactory typeFactory;

    @Before
    public void setUp() {
        context = new DeserializationContext(new TestDeserializationConfig(), new TestObjectMapper());
        typeFactory = context.getTypeFactory();
    }

    @Test
    public void testValidateSubTypeWithValidTypes() throws JsonMappingException {
        // Test valid types
        JavaType validType1 = typeFactory.constructType(String.class);
        SUB_TYPE_VALIDATOR.validateSubType(context, validType1);

        JavaType validType2 = typeFactory.constructType(Integer.class);
        SUB_TYPE_VALIDATOR.validateSubType(context, validType2);
    }

    @Test
    public void testValidateSubTypeWithInvalidTypes() throws JsonMappingException {
        // Test invalid types
        JavaType invalidType1 = typeFactory.constructType(SubTypeValidator.class);
        assertThrows(JsonMappingException.class, () -> SUB_TYPE_VALIDATOR.validateSubType(context, invalidType1));

        JavaType invalidType2 = typeFactory.constructType(SubTypeValidatorTest.class);
        assertThrows(JsonMappingException.class, () -> SUB_TYPE_VALIDATOR.validateSubType(context, invalidType2));

        JavaType invalidType3 = typeFactory.constructType("java.util.logging.FileHandler");
        assertThrows(JsonMappingException.class, () -> SUB_TYPE_VALIDATOR.validateSubType(context, invalidType3));
    }

    @Test
    public void testValidateSubTypeWithSpringTypes() throws JsonMappingException {
        // Test Spring types
        JavaType springType1 = typeFactory.constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        assertThrows(JsonMappingException.class, () -> SUB_TYPE_VALIDATOR.validateSubType(context, springType1));

        JavaType springType2 = typeFactory.constructType("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
        assertThrows(JsonMappingException.class, () -> SUB_TYPE_VALIDATOR.validateSubType(context, springType2));
    }

    @Test
    public void testValidateSubTypeWithCustomIllegalTypes() throws JsonMappingException {
        Set<String> customIllegalClassNames = new HashSet<>();
        customIllegalClassNames.add("com.test.CustomIllegalType");
        SubTypeValidator customValidator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = customIllegalClassNames;
        };

        JavaType customIllegalType = typeFactory.constructType("com.test.CustomIllegalType");
        assertThrows(JsonMappingException.class, () -> customValidator.validateSubType(context, customIllegalType));
    }
}

class TestDeserializationConfig {
    // Empty deserialization config for JUnit test
}

class TestObjectMapper {
    // Empty ObjectMapper for JUnit test
}