package com.example.subtypevalidator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testInstance() {
        SubTypeValidator validator = SubTypeValidator.instance();
        assertFalse(validator == null);
    }

    @Test
    public void testValidateSubType_validType() throws JsonMappingException {
        JavaType validType = mock(JavaType.class);
        DeserializationContext context = mock(DeserializationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, validType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalType() throws JsonMappingException {
        JavaType illegalType = mock(JavaType.class);
        DeserializationContext context = mock(DeserializationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        String illegalTypeName = "java.lang.RuntimeException";
        String fullIllegalTypeName = illegalTypeName.replace(".", "/") + ".class";
        illegaltType.setRawClass(Class.forName(fullIllegalTypeName));
        validator.validateSubType(context, illegalType);
    }

    @Test
    public void testValidateSubType_emptyContext() throws JsonMappingException {
        JavaType validType = mock(JavaType.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(NullPointerException.class, () -> validator.validateSubType(null, validType));
    }

    @Test
    public void testValidateSubType_nullJavaType() throws JsonMappingException {
        DeserializationContext context = mock(DeserializationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(NullPointerException.class, () -> validator.validateSubType(context, null));
    }

    @Test
    public void testValidateSubType_emptyIllegalClasses() throws JsonMappingException {
        Set<String> emptySet = Collections.emptySet();
        SubTypeValidator validator = new SubTypeValidator();
        validator._cfgIllegalClassNames = emptySet;
        JavaType validType = mock(JavaType.class);
        DeserializationContext context = mock(DeserializationContext.class);
        validator.validateSubType(context, validType);
    }
}