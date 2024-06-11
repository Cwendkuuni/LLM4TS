package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

    @InjectMocks
    private SubTypeValidator subTypeValidator;

    @Test
    public void testValidateSubType_validType() {
        JavaType validType = TypeFactory.defaultInstance()
                .constructType(String.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        subTypeValidator.validateSubType(context, validType);
    }

    @Test
    public void testValidateSubType_invalidType() {
        JavaType invalidType = TypeFactory.defaultInstance()
                .constructType(SubTypeValidator.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        assertThrows(JsonMappingException.class, () ->
                subTypeValidator.validateSubType(context, invalidType));

        String expectedErrorMessage = "Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator) to deserialize: prevented for security reasons";
        Mockito.verify(context).throwAbove(eq(expectedErrorMessage));
    }

    @Test
    public void testValidateSubType_springClass() {
        JavaType springType = TypeFactory.defaultInstance()
                .constructType(SpringBean.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        subTypeValidator.validateSubType(context, springType);

        verify(context).findAnnotation(eq(springAnnotation.class));
    }

    @Test
    public void testValidateSubType_interfaces() {
        JavaType interfaceType = TypeFactory.defaultInstance()
                .constructType(Interface.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        subTypeValidator.validateSubType(context, interfaceType);
    }

    @Test
    public void testValidateSubType_springSuperClass() {
        JavaType springSuperClass = TypeFactory.defaultInstance()
                .constructType(SpringSuperclass.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        subTypeValidator.validateSubType(context, springSuperClass);

        verify(context).findAnnotation(eq(springAnnotation.class));
        verify(context).findAnnotation(eq(AnotherAnnotation.class));
    }

    @Test
    public void testValidateSubType_thirdPartyType() {
        JavaType thirdPartyType = TypeFactory.defaultInstance()
                .constructType(ThirdPartyType.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        subTypeValidator.validateSubType(context, thirdPartyType);

        // no specific check, as it depends on the implementation of ThirdPartyType
    }

    @Test
    public void testValidateSubType_jdkType() {
        JavaType jdkType = TypeFactory.defaultInstance()
                .constructType(java.util.logging.Logger.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        subTypeValidator.validateSubType(context, jdkType);

        // no specific check, as it's a JDK type and not blocked by default
    }

    static class Interface {}
    static class SpringBean {}
    static class SpringSuperclass implements org.springframework.core.annotation.AnnotationAware {}
    static class SpringAnnotation {
        // placeholder for Spring annotations
    }
    static class AnotherAnnotation {
        // placeholder for other annotations
    }
    static class ThirdPartyType {
        // placeholder for third-party types
    }
}