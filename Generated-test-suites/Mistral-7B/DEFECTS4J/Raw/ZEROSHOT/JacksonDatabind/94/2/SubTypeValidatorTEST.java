package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for {@link SubTypeValidator}.
 */
public class SubTypeValidatorTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Test method for {@link SubTypeValidator#DEFAULT_NO_DESER_CLASS_NAMES}.
     */
    @Test
    public void testDefaultNoDeserClassNameSet() {
        final Set<String> set = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
        final int initialSize = set.size();
        // add a new class name to the set
        final String newClassName = "com.example.MyCustomClass";
        SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES.add(newClassName);
        // check that the set now contains the new class name
        final boolean containsNewClassName = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES.contains(newClassName);
        assertTrue("New class name should be contained in the set after adding it", containsNewClassName);
        // check that the set size is one greater than before
        final int finalSize = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES.size();
        assertEquals("Set size should be one greater than before", initialSize + 1, finalSize);
        // remove the class name from the set and check that it is no longer contained
        SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES.remove(newClassName);
        final boolean containsRemovedClassName = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES.contains(newClassName);
        assertFalse("Removed class name should not be contained in the set anymore", containsRemovedClassName);
    }

    /**
     * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}.
     */
    @Test
    public void testValidateSubType() throws JsonMappingException {
        final JavaType type = TypeFactory.defaultInstance().constructType(Object.class);
        final DeserializationContext context = mapper.getDeserializationContext();
        final SubTypeValidator validator = SubTypeValidator.instance();

        // test with a valid type
        validator.validateSubType(context, type);

        // test with a "nasty" class
        final JavaType nastyType = mapper.getTypeFactory()
                .constructType(com.apache.commons.collections.functors.InstantiateTransformer.class);
        try {
            validator.validateSubType(context, nastyType);
            fail("Expected JsonMappingException for nasty type");
        } catch (JsonMappingException e) {
            // expected
        }

        // test with a Spring class
        final JavaType springType = mapper.getTypeFactory()
                .constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
        try {
            validator.validateSubType(context, springType);
            fail("Expected JsonMappingException for Spring type");
        } catch (JsonMappingException e) {
            // expected
        }

        // test with a custom class
        final JavaType customType = TypeFactory.defaultInstance()
                .constructType(com.example.MyCustomClass.class);
        try {
            validator.validateSubType(context, customType);
            fail("Expected JsonMappingException for custom type");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}