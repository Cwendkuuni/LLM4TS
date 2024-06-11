package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithNullContextAndType() {
        SubTypeValidator validator = SubTypeValidator.instance();

        assertNull(validator.validateSubType(null, null));
    }

    @Test
    public void testValidateSubTypeWithValidType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType javaType = new JavaType(String.class);

        validator.validateSubType(new DeserializationContext(null), javaType);
    }

    @Test
    public void testValidateSubTypeWithInvalidType() {
        SubTypeValidator validator = SubTypeValidator.instance();

        String invalidClassName = "org.apache.commons.collections.functors.InvokerTransformer";
        JavaType javaType = new JavaType(Class.forName(invalidClassName));

        DeserializationContext context = new DeserializationContext(null);

        assertThrows(JsonMappingException.class, () -> {
            validator.validateSubType(context, javaType);
        });
    }

    @Test
    public void testValidateSubTypeWithClassStartsWithPrefixString() {
        SubTypeValidator validator = SubTypeValidator.instance();

        String classWithPrefix = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
        JavaType javaType = new JavaType(Class.forName(classWithPrefix));

        DeserializationContext context = new DeserializationContext(null);

        assertThrows(JsonMappingException.class, () -> {
            validator.validateSubType(context, javaType);
        });
    }

    @Test
    public void testValidateSubTypeWithClassNotStartsWithPrefixString() {
        SubTypeValidator validator = SubTypeValidator.instance();

        String classNotWithPrefix = "java.util.ArrayList";
        JavaType javaType = new JavaType(Class.forName(classNotWithPrefix));

        DeserializationContext context = new DeserializationContext(null);

        validator.validateSubType(context, javaType);
    }

    @Test
    public void testValidateSubTypeWithIllegalClassList() {
        SubTypeValidator validator = SubTypeValidator.instance();
        Set<String> illegalClasses = new HashSet<>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);

        for (String illegalClass : illegalClasses) {
            JavaType javaType = new JavaType(Class.forName(illegalClass));

            DeserializationContext context = new DeserializationContext(null);

            assertThrows(JsonMappingException.class, () -> {
                validator.validateSubType(context, javaType);
            });
        }
    }

    @Test
    public void testValidateSubTypeWithIllegalPackageName() throws JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();

        String illegalPackageName = "com.illegal.package";
        String illegalClassName = "IllegalClass";

        JavaType javaType = new JavaType(Class.forName(illegalPackageName + "." + illegalClassName));

        DeserializationContext context = new DeserializationContext(null);

        assertThrows(JsonMappingException.class, () -> {
            validator.validateSubType(context, javaType);
        });
    }
}