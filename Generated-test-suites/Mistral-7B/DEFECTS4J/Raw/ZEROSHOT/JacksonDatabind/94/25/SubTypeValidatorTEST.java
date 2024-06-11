package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

    @InjectMocks
    private SubTypeValidator subTypeValidator = new SubTypeValidator();

    @Test
    public void testValidateSubTypeWithValidType() throws JsonProcessingException {
        JavaType javaType = Mockito.mock(JavaType.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        subTypeValidator.validateSubType(context, javaType);
    }

    @Test
    public void testValidateSubTypeWithInvalidType1() throws JsonProcessingException {
        JavaType javaType = Mockito.mock(JavaType.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        String fullClassName1 = "org.springframework.beans.factory.ObjectFactory";
        Mockito.when(javaType.getRawClass().getName()).thenReturn(fullClassName1);

        assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, javaType));
    }

    @Test
    public void testValidateSubTypeWithInvalidType2() throws JsonProcessingException {
        JavaType javaType = Mockito.mock(JavaType.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        String fullClassName2 = "java.util.logging.FileHandler";
        Mockito.when(javaType.getRawClass().getName()).thenReturn(fullClassName2);

        assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, javaType));
    }

    @Test
    public void testValidateSubTypeWithInterfaceType() throws JsonProcessingException {
        JavaType javaType = Mockito.mock(JavaType.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Class<?> rawType = Mockito.mock(Class.class);
        Mockito.when(javaType.getRawClass()).thenReturn(rawType);
        Mockito.when(rawType.isInterface()).thenReturn(true);

        subTypeValidator.validateSubType(context, javaType);
    }

    @Test
    public void testValidateSubTypeWithSpringClassHierarchy() throws JsonProcessingException {
        JavaType javaType = Mockito.mock(JavaType.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Class<?> springClass1 = Mockito.mock(Class.class);
        Class<?> springClass2 = Mockito.mock(Class.class);
        Class<?> springClass3 = Mockito.mock(Class.class);

        Mockito.when(javaType.getRawClass().getName()).thenReturn("org.springframework.beans.factory.AbstractApplicationContext");
        Mockito.when(javaType.getRawClass().getSuperclass()).thenReturn(springClass1);
        Mockito.when(springClass1.getSuperclass()).thenReturn(springClass2);
        Mockito.when(springClass2.getSuperclass()).thenReturn(springClass3);
        Mockito.when(springClass3.getSimpleName()).thenReturn("AbstractPointcutAdvisor");

        assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, javaType));
    }
}