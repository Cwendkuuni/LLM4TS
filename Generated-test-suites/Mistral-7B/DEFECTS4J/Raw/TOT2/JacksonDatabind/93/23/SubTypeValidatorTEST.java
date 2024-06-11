
package com.example.junit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validClass() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubType_invalidClass() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        when(type.getRawClass().getName()).thenReturn("org.apache.commons.collections4.functors.InstantiateTransformer");

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testValidateSubType_nullDeserializationContext() {
        JavaType type = mock(JavaType.class);
        assertThrows(NullPointerException.class, () -> SubTypeValidator.instance().validateSubType(null, type));
    }

    @Test
    public void testValidateSubType_nullJavaType() {
        DeserializationContext context = mock(DeserializationContext.class);
        assertThrows(NullPointerException.class, () -> SubTypeValidator.instance().validateSubType(context, null));
    }

    @Test
    public void testValidateSubType_emptyJavaType() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        when(type.isEmpty()).thenReturn(true);

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubType_nullRawClass() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        when(type.getRawClass()).thenReturn(null);

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(NullPointerException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testValidateSubType_validClass_customPrefix() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        when(type.getRawClass().getName()).thenReturn("com.mycompany.nasty.NastyClass");

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, type);
    }
}
