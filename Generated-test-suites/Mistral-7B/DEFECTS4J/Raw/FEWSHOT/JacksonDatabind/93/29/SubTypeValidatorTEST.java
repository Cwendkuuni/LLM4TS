package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithValidClasses() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        JavaType validType1 = mapper.getTypeFactory().constructType(Integer.class);
        JavaType validType2 = mapper.getTypeFactory().constructType(String.class);
        JavaType validType3 = mapper.getTypeFactory().constructType(Double.class);

        validator.validateSubType(mapper.getDeserializationContext(), validType1);
        validator.validateSubType(mapper.getDeserializationContext(), validType2);
        validator.validateSubType(mapper.getDeserializationContext(), validType3);
    }

    @Test
    public void testValidateSubTypeWithNastyClasses() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        JavaType nastyType1 = mapper.getTypeFactory().constructType(org.apache.commons.collections.functors.InvokerTransformer.class);
        JavaType nastyType2 = mapper.getTypeFactory().constructType(com.mchange.v2.c3p0.WrapperConnectionPoolDataSource.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType1));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), nastyType2));

        // Test checking Spring classes
        Class<?> springClass1 = Class.forName("org.springframework.beans.factory.ObjectFactory");
        JavaType springType1 = mapper.getTypeFactory().constructType(springClass1);
        validator.validateSubType(mapper.getDeserializationContext(), springType1);

        Class<?> springClass2 = Class.forName("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        JavaType springType2 = mapper.getTypeFactory().constructType(springClass2);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), springType2));
    }

    @Test
    public void testValidateSubTypeWithUnknownClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        String unknownClassName = "com.unknown.UnknownClass";
        JavaType unknownType = mapper.getTypeFactory().constructType(Class.forName(unknownClassName));

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getDeserializationContext(), unknownType));
    }

    @Test
    public void testValidateSubTypeWithArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        JavaType arrayType = mapper.getTypeFactory().constructType(int[].class);

        validator.validateSubType(mapper.getDeserializationContext(), arrayType);
    }

    @Test
    public void testValidateSubTypeWithGenericType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        JavaType genericType = mapper.getTypeFactory()
                .constructType(List.class, Integer.class);

        validator.validateSubType(mapper.getDeserializationContext(), genericType);
    }

    @Test
    public void testValidateSubTypeWithNestedClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        Class<?> nestedClass = NestedClass.class;
        JavaType nestedClassType = mapper.getTypeFactory().constructType(nestedClass);

        validator.validateSubType(mapper.getDeserializationContext(), nestedClassType);
    }

    static class NestedClass {
    }

    @Test(expected = IllegalAccessException.class)
    public void testPrivateConstructorAccess() throws Exception {
        SubTypeValidator validator = new SubTypeValidator(); // private constructor
        JavaType type = null;

        Method validateSubTypeMethod = SubTypeValidator.class.getDeclaredMethod("validateSubType", DeserializationContext.class, JavaType.class);
        validateSubTypeMethod.setAccessible(true);

        assertThrows(IllegalAccessException.class, () -> validateSubTypeMethod.invoke(validator, new DeserializationContext(new ObjectMapper()), type));
    }
}