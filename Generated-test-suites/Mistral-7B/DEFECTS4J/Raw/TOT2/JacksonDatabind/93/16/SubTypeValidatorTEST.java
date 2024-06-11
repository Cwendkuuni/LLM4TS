package com.yourpackage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.fasterxml.jackson.core.util.TestUtil.convertValue;
import static com.fasterxml.jackson.databind.JsonMapping.Builder.stdDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

    private ObjectMapper mapper;
    private SubTypeValidator validator;

    @org.junit.Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testGetDefaultNoDeserClassNames() {
        Set<String> defaultNoDeserClassNames = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
        assertNotNull(defaultNoDeserClassNames);
        assertEquals(33, defaultNoDeserClassNames.size());
    }

    @Test
    public void testInstance() {
        SubTypeValidator validator1 = SubTypeValidator.instance();
        SubTypeValidator validator2 = SubTypeValidator.instance();
        assertSame(validator1, validator2);
    }

    @Test
    public void testValidateSubType() throws Exception {
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType type = mapper.getTypeFactory().constructType(SubTypeValidatorTest.class);

        String unsupportedClass = "org.apache.commons.collections.functors.InvokerTransformer";

        // Test valid JavaType
        validator.validateSubType(context, type);

        // Test an unsupported class
        when(context.findContextualType(unsupportedClass)).thenReturn(JavaType.valueOf(unsupportedClass));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, JavaType.valueOf(unsupportedClass)));
    }

    @Test
    public void testValidateSubTypeEmptyContext() {
        DeserializationContext context = null;
        JavaType type = null;
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testValidateSubTypeNullContext() {
        DeserializationContext context = null;
        JavaType type = mapper.getTypeFactory().constructType(SubTypeValidatorTest.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testValidateSubTypeEmptyClassName() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mapper.getTypeFactory().constructType(SubTypeValidatorTest.class);
        when(context.findContextualType(type.getRawClass().getName())).thenReturn(null);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testValidateSubTypeNullClassName() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mapper.getTypeFactory().constructType(SubTypeValidatorTest.class);
        when(context.findContextualType(null)).thenReturn(type);
        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubTypeWithEmptyClassName() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mapper.getTypeFactory().constructType(SubTypeValidatorTest.class);
        when(context.findContextualType(type.getRawClass().getName())).thenReturn(type);
        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubTypeWithNullClassName() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mapper.getTypeFactory().constructType(SubTypeValidatorTest.class);
        when(context.findContextualType(null)).thenReturn(type);
        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubTypeWithEmptyContext() {
        DeserializationContext context = null;
        JavaType type = mapper.getTypeFactory().constructType(SubTypeValidatorTest.class);
        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubTypeWithNullType() {
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType type = null;
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }
}