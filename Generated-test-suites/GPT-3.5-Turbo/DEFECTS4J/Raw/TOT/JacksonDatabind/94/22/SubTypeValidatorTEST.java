package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;
    private DeserializationContext deserializationContext;
    private JavaType javaType;
    private String className;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
        deserializationContext = new DeserializationContext(null, null, null);
        javaType = JavaType.fromClass(String.class);
        className = javaType.getRawClass().getName();
    }

    @Test
    public void testValidateSubType_ValidSubType_NoExceptionThrown()  {
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            assertEquals("Unexpected exception thrown", true, false);
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidSubType_JsonMappingExceptionThrown()  {
        subTypeValidator.validateSubType(deserializationContext, new JavaTypeImpl(InvalidClass.class));
    }

    @Test
    public void testValidateSubType_InterfaceSubType_NoExceptionThrown()  {
        try {
            subTypeValidator.validateSubType(deserializationContext, new JavaTypeImpl(Interface.class));
        } catch (JsonMappingException e) {
            assertEquals("Unexpected exception thrown", true, false);
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_AbstractPointcutAdvisorSubType_JsonMappingExceptionThrown()  {
        subTypeValidator.validateSubType(deserializationContext, new JavaTypeImpl(AbstractPointcutAdvisor.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_AbstractApplicationContextSubType_JsonMappingExceptionThrown()  {
        subTypeValidator.validateSubType(deserializationContext, new JavaTypeImpl(AbstractApplicationContext.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType_JsonMappingExceptionThrown()  {
        subTypeValidator.validateSubType(deserializationContext, new JavaTypeImpl(IllegalClass.class));
    }

    private class JavaTypeImpl extends JavaType {

        public JavaTypeImpl(Class<?> rawClass) {
            super(rawClass);
        }
    }

    private interface Interface {}

    private abstract class AbstractPointcutAdvisor {}
    
    private abstract class AbstractApplicationContext {}

    private class InvalidClass {}
    
    private class IllegalClass {}

}