
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {
    
    private DeserializationContext ctxtMock;
    private JavaType typeMock;
    
    @Before
    public void setUp() {
        ctxtMock = mock(DeserializationContext.class);
        typeMock = mock(JavaType.class);
    }
    
    @After
    public void tearDown() {
        ctxtMock = null;
        typeMock = null;
    }

    @Test
    public void testValidateSubType_ValidClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxtMock, typeMock);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        when(typeMock.getRawClass()).thenReturn(String.class);
        validator.validateSubType(ctxtMock, typeMock);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InterfaceClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        when(typeMock.getRawClass()).thenReturn(Runnable.class);
        validator.validateSubType(ctxtMock, typeMock);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_SpringAbstractPointcutAdvisorClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        when(typeMock.getRawClass()).thenReturn(org.springframework.aop.support.AbstractPointcutAdvisor.class);
        validator.validateSubType(ctxtMock, typeMock);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_SpringAbstractApplicationContextClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        when(typeMock.getRawClass()).thenReturn(org.springframework.context.support.AbstractApplicationContext.class);
        validator.validateSubType(ctxtMock, typeMock);
    }
}

