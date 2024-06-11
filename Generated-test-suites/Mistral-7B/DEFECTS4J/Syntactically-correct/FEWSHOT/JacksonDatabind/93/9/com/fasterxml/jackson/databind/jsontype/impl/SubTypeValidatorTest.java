package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class SubTypeValidatorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testValidateSubTypeWithAllowedClass() throws Exception {
        JavaType allowedType = MAPPER.getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(MAPPER.getDeserializationContext(), allowedType);
    }

    @Test
    public void testValidateSubTypeWithDisallowedClass() throws Exception {
        String disallowedClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
        JavaType disallowedType = MAPPER.getTypeFactory().constructType(ClassIdType.class, disallowedClassName);
        SubTypeValidator validator = SubTypeValidator.instance();

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(MAPPER.getDeserializationContext(), disallowedType));
    }

    @Test
    public void testValidateSubTypeWithSpringClass() throws Exception {
        String springClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
        JavaType springType = MAPPER.getTypeFactory().constructType(ClassIdType.class, springClassName);
        SubTypeValidator validator = SubTypeValidator.instance();

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(MAPPER.getDeserializationContext(), springType));
    }

    @Test
    public void testValidateSubTypeWithSpringSuperClass() throws Exception {
        String springSuperClassName = "java.lang.Object";
        JavaType springSuperType = MAPPER.getTypeFactory().constructType(ClassIdType.class, springSuperClassName);
        JavaType springSubType = MAPPER.getTypeFactory().constructType(ClassIdType.class, "org.springframework.beans.factory.config.PropertyPathFactoryBean");

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(MAPPER.getDeserializationContext(), springSubType.getType(springSuperType));
    }

    @Test
    public void testValidateSubTypeWithSpringInterface() throws Exception {
        String springInterfaceClassName = "org.springframework.beans.factory.config.BeanDefinitionVisitor";
        JavaType springInterfaceType = MAPPER.getTypeFactory().constructType(ClassIdType.class, springInterfaceClassName);
        JavaType springImplementingType = MAPPER.getTypeFactory().constructType(ClassIdType.class, "org.springframework.beans.factory.config.DefaultSingletonBeanRegistry");

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(MAPPER.getDeserializationContext(), springImplementingType.getType(springInterfaceType));
    }
}