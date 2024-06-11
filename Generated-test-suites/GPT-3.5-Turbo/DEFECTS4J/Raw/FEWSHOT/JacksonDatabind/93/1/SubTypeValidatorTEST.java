package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class SubTypeValidatorTest {

    @Mock
    private DeserializationContext mockCtxt;

    @Mock
    private JavaType mockType;

    private SubTypeValidator validator;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidClass() throws JsonMappingException {
        when(mockType.getRawClass()).thenReturn(String.class);
        validator.validateSubType(mockCtxt, mockType);
    }

    @Test
    public void testValidateSubType_IllegalClass() {
        when(mockType.getRawClass()).thenReturn(SubTypeValidator.class);
        Throwable exception = assertThrows(JsonMappingException.class,
                () -> validator.validateSubType(mockCtxt, mockType));

        String expectedMessage = "Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator) to deserialize: prevented for security reasons";
        String actualMessage = exception.getMessage();

        assert(actualMessage.contains(expectedMessage));
    }
}