package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.api.TestNames.COMMON_NAME_FIELD_NAME;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithValidClasses() {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType validType = JavaType.construct(int[].class);
        validator.validateSubType(new DeserializationContext(null, null), validType);
    }

    @Test
    public void testValidateSubTypeWithSpringClasses() {
        SubTypeValidator validator = SubTypeValidator.instance();

        Class<?> springClass1 = Class.forName("org.springframework.context.ApplicationContext");
        JavaType springType1 = JavaType.construct(springClass1);
        validator.validateSubType(new DeserializationContext(null, null), springType1);

        Class<?> springClass2 = Class.forName("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        JavaType springType2 = JavaType.construct(springClass2);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null, null), springType2));
    }

    @Test
    public void testValidateSubTypeWithNastyClasses() {
        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with a nasty class name from the list
        Class<?> nastyClass = Class.forName("org.apache.commons.collections4.functors.InstantiateTransformer");
        JavaType nastyType = JavaType.construct(nastyClass);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null, null), nastyType));

        // Test with a class name not in the list
        Class<?> notNastyClass = Class.forName("java.lang.String");
        JavaType notNastyType = JavaType.construct(notNastyClass);
        validator.validateSubType(new DeserializationContext(null, null), notNastyType);
    }

    @Test
    public void testValidateSubTypeWithInterfaces() {
        SubTypeValidator validator = SubTypeValidator.instance();

        Class<?> interfaceClass = Class.forName("java.io.Serializable");
        JavaType interfaceType = JavaType.construct(interfaceClass.asSubType(Object.class));
        validator.validateSubType(new DeserializationContext(null, null), interfaceType);
    }

    @Test
    public void testValidateSubTypeWithSuperClasses() {
        SubTypeValidator validator = SubTypeValidator.instance();

        Class<?> superClass = Class.forName("java.lang.Object");
        Class<?> subClass1 = Class.forName("java.lang.String");
        Class<?> subClass2 = Class.forName("java.lang.Number");

        JavaType superType = JavaType.construct(superClass);
        JavaType subType1 = JavaType.construct(subClass1);
        JavaType subType2 = JavaType.construct(subClass2);

        validator.validateSubType(new DeserializationContext(null, null), superType);
        validator.validateSubType(new DeserializationContext(null, null), subType1);
        validator.validateSubType(new DeserializationContext(null, null), subType2);
    }

    @Test
    public void testValidateSubTypeWithCustomSet() {
        Set<String> customSet = new HashSet<>();
        customSet.add("com.example.CustomNastyClass");

        SubTypeValidator validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = Collections.unmodifiableSet(customSet);
        };

        Class<?> customNastyClass = Class.forName("com.example.CustomNastyClass");
        JavaType customNastyType = JavaType.construct(customNastyClass);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null, null), customNastyType));
    }
}