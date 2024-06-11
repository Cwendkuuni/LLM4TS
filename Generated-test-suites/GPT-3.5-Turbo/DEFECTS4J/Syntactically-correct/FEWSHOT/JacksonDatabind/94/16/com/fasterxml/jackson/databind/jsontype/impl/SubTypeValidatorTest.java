package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubTypeValidatorTest {

    private static SubTypeValidator validator;

    @BeforeClass
    public static void setup() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_validSubtypeType() {
        try {
            validator.validateSubType(mockDeserializationContext(), mockJavaType(Object.class));
        } catch (JsonMappingException e) {
            fail("This should not throw an exception");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_invalidSubtypeType() throws JsonMappingException {
        validator.validateSubType(mockDeserializationContext(), mockJavaType(SubTypeValidator.class));
    }

    @Test
    public void testValidateSubType_interfaceSubtypeType() {
        try {
            validator.validateSubType(mockDeserializationContext(), mockJavaType(Runnable.class));
        } catch (JsonMappingException e) {
            fail("This should not throw an exception");
        }
    }

    @Test
    public void testValidateSubType_springSubtypeType() {
        try {
            validator.validateSubType(mockDeserializationContext(), mockJavaType(AbstractBeanFactoryPointcutAdvisor.class));
        } catch (JsonMappingException e) {
            fail("This should not throw an exception");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_blockedSpringSubtypeType() throws JsonMappingException {
        validator.validateSubType(mockDeserializationContext(), mockJavaType(FileSystemXmlApplicationContext.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_blockedClassType() throws JsonMappingException {
        validator.validateSubType(mockDeserializationContext(), mockJavaType(org.apache.commons.collections.functors.InstantiateTransformer.class));
    }

    private DeserializationContext mockDeserializationContext() {
        return null;
    }

    private JavaType mockJavaType(Class<?> clazz) {
        return null;
    }
}