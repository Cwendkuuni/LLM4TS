package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_NastyClasses() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SubTypeValidator validator = SubTypeValidator.instance();

        JavaType type1 = mapper.getTypeFactory().constructType(String.class);
        JavaType type2 = mapper.getTypeFactory().constructType(new ClassIdType(String.class, "namespace.NastyClass1"));
        JavaType type3 = mapper.getTypeFactory().constructType(new ClassIdType(String.class, "namespace.NastyClass2"));

        validator.validateSubType(ctxt, type1);
        validator.validateSubType(ctxt, type2);
        validator.validateSubType(ctxt, type3);
    }

    @Test
    public void testValidateSubType_SpringClasses() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SubTypeValidator validator = SubTypeValidator.instance();

        JavaType typeAbstractApplicationContext = mapper.getTypeFactory().constructType(AbstractApplicationContext.class);
        JavaType typeAbstractPointcutAdvisor = mapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);

        validator.validateSubType(ctxt, typeAbstractApplicationContext);
        validator.validateSubType(ctxt, typeAbstractPointcutAdvisor);

        // Test for Spring classes with long class names
        String springClassLongName1 = "com.example.myproject.config.MySpringConfiguration$MyBean1";
        JavaType typeSpringClassLongName1 = mapper.getTypeFactory().constructType(springClassLongName1);
        validator.validateSubType(ctxt, typeSpringClassLongName1);

        String springClassLongName2 = "com.example.myproject.config.MySpringConfiguration$MyBean2$NestedBean1";
        JavaType typeSpringClassLongName2 = mapper.getTypeFactory().constructType(springClassLongName2);
        validator.validateSubType(ctxt, typeSpringClassLongName2);
    }

    @Test
    public void testValidateSubType_ThrowsJsonMappingException() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SubTypeValidator validator = SubTypeValidator.instance();

        JavaType typeIllegalClass = mapper.getTypeFactory().constructType(Object.class, "org.apache.commons.collections4.functors.InstantiateTransformer");

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, typeIllegalClass));
    }
}