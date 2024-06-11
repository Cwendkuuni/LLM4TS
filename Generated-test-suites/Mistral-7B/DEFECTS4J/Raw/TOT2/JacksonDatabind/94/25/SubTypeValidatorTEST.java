package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;

public class SubTypeValidatorTest {

    @Test
    public void testValidType() throws JsonMappingException {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaTypeTypeFactory typeFactory = mock(JavaTypeFactory.class);
        JavaType type = typeFactory.constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();

        validator.validateSubType(context, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidType() throws JsonMappingException {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaTypeFactory typeFactory = mock(JavaTypeFactory.class);
        JavaType type = typeFactory.constructType(SubTypeValidator.class);
        SubTypeValidator validator = SubTypeValidator.instance();

        validator.validateSubType(context, type);
    }

    @Test
    public void testNastyClassNameContainedInDefaultSet() {
        SubTypeValidator validator = SubTypeValidator.instance();
        String nastyClassName = "org.apache.commons.collections.functors.InvokerTransformer";
        assertTrue(validator._cfgIllegalClassNames.contains(nastyClassName));
    }

    @Test
    public void testNastyClassNameNotContainedInDefaultSet() {
        SubTypeValidator validator = SubTypeValidator.instance();
        String notNastyClassName = "com.example.MyClass";
        assertFalse(validator._cfgIllegalClassNames.contains(notNastyClassName));
    }

    @Test(expected = JsonMappingException.class)
    public void testSpringClassName() throws JsonMappingException {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaTypeFactory typeFactory = mock(JavaTypeFactory.class);
        JavaType type = typeFactory.constructType(AbstractApplicationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();

        validator.validateSubType(context, type);
    }

    @Test
    public void testSpringInterface() {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType type = typeFactory.constructType(AbstractPointcutAdvisor.class);
        validator.validateSubType(mock(DeserializationContext.class), type);
    }
}