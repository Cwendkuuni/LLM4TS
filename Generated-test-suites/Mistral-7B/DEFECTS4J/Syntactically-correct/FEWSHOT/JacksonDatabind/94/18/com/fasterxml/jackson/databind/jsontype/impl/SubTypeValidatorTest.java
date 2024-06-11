package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test cases for SubTypeValidator class.
 */
@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

    private static final DeserializationContext context = Mockito.spy(DeserializationContext.instance);
    private static final TypeFactory typeFactory = Mockito.spy(TypeFactory.defaultInstance);

    @Test
    public void testValidSubType() throws JsonMappingException, JsonProcessingException {
        JavaType validType = typeFactory.constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, validType);
        // No exception thrown, so test is successful
    }

    @Test
    public void testInvalidSubTypeWithPrefixSpring() throws JsonMappingException {
        JavaType invalidType = typeFactory.constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
        assertThrows(JsonMappingException.class, () -> SubTypeValidator.instance().validateSubType(context, invalidType));
    }

    @Test
    public void testInvalidSubTypeWithSpringSuperclass() throws JsonMappingException {
        JavaType invalidType = typeFactory.constructType(org.springframework.beans.factory.AbstractApplicationContext.class);
        assertThrows(JsonMappingException.class, () -> SubTypeValidator.instance().validateSubType(context, invalidType));
    }

    @Test
    public void testInvalidSubTypeWithOtherPackage() throws JsonMappingException {
        JavaType invalidType = typeFactory.constructType(java.sql.Connection.class);
        assertThrows(JsonMappingException.class, () -> SubTypeValidator.instance().validateSubType(context, invalidType));
    }

    @Test
    public void testDefaultIllegalClassNames() {
        Set<String> illegalClassNames = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
        assertEquals(23, illegalClassNames.size());
        assertEquals("org.apache.commons.collections.functors.InvokerTransformer", illegalClassNames.iterator().next());
    }
}