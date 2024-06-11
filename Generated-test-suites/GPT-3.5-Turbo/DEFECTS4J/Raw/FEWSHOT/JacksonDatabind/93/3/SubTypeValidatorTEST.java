package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    private DeserializationContext mockDeserializationContext;
    private JavaType mockJavaType;
    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        mockDeserializationContext = mock(DeserializationContext.class);
        mockJavaType = mock(JavaType.class);
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_withValidClass() throws JsonMappingException {
        when(mockJavaType.getRawClass()).thenReturn(String.class);

        subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);

        verify(mockJavaType, times(1)).getRawClass();
        verifyNoMoreInteractions(mockJavaType);
        verifyNoInteractions(mockDeserializationContext);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_withIllegalClass() throws JsonMappingException {
        when(mockJavaType.getRawClass()).thenReturn(org.springframework.beans.factory.ObjectFactory.class);

        subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);
    }
}