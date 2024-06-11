package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * JUnit 4 test file for the SubTypeValidator class.
 */
public class SubTypeValidatorTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() {
        mapper = new ObjectMapper();
    }

    // -----------------------------------------------------------------------

    /**
     * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}.
     */
    @Test
    public void testValidateSubType() throws Exception {
        String validType = "java.lang.String";
        String invalidType1 = "com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator";
        String invalidType2 = "org.springframework.beans.factory.config.PropertyPathFactoryBean";

        JavaType validTypeJavaType = TypeFactory.defaultInstance().constructType(validType);
        JavaType invalidType1JavaType = TypeFactory.defaultInstance().constructType(invalidType1);
        JavaType invalidType2JavaType = TypeFactory.defaultInstance().constructType(invalidType2);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, validTypeJavaType);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, invalidType1JavaType));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, invalidType2JavaType));
    }

    // -----------------------------------------------------------------------

    /**
     * Test method for edge case: check if the validation correctly handles a class name starting with the prefix.
     */
    @Test
    public void testEdgeCaseValidateSubTypeWithPrefix() throws Exception {
        String validType = "org.springframework.beans.FooBean";

        JavaType validTypeJavaType = TypeFactory.defaultInstance().constructType(validType);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, validTypeJavaType);
    }

    // -----------------------------------------------------------------------

    /**
     * Test method for edge case: check if the validation correctly handles an empty class name.
     */
    @Test
    public void testEdgeCaseValidateSubTypeWithEmptyClassName() throws Exception {
        String emptyClassName = "";

        JavaType emptyClassType = TypeFactory.defaultInstance().constructType(emptyClassName);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, emptyClassType));
    }

    // -----------------------------------------------------------------------

    /**
     * Test method for edge case: check if the validation correctly handles a null JavaType.
     */
    @Test
    public void testEdgeCaseValidateSubTypeWithNullJavaType() throws Exception {
        DeserializationContext ctxt = mapper.getDeserializationContext();

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonProcessingException.class, () -> validator.validateSubType(ctxt, null));
    }

    // -----------------------------------------------------------------------

    /**
     * Test method for exception handling: check if an IllegalArgumentException is thrown when an invalid class name is passed.
     */
    @Test(expected = JsonMappingException.class)
    public void testExceptionHandleInvalidClassName() throws Exception {
        String invalidClassName = "java.util.IllegalArgumentException";

        JavaType invalidClassType = TypeFactory.defaultInstance().constructType(invalidClassName);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, invalidClassType);
    }

    // -----------------------------------------------------------------------

    /**
     * Test method for exception handling: check if a JsonMappingException is thrown when a class is in the blacklist.
     */
    @Test(expected = JsonMappingException.class)
    public void testExceptionHandleBlacklistedClassName() throws Exception {
        String blacklistedClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";

        JavaType blacklistedClassType = TypeFactory.defaultInstance().constructType(blacklistedClassName);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, blacklistedClassType);
    }

    // -----------------------------------------------------------------------

    /**
     * Test method for exception handling: check if a JsonMappingException is thrown when a class name starts with the prefix and is a problematic type.
     */
    @Test(expected = JsonMappingException.class)
    public void testExceptionHandleBlacklistedClassNameWithPrefix() throws Exception {
        String blacklistedclassNameWithPrefix = "org.springframework.beans.factory.ObjectFactory";

        JavaType blacklistedClassType = TypeFactory.defaultInstance().constructType(blacklistedclassNameWithPrefix);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, blacklistedClassType);
    }

    // -----------------------------------------------------------------------

    /**
     * Test method for exception handling: check if a JsonMappingException is thrown when a class name starts with the prefix but is not a problematic type.
     */
    @Test
    public void testNoExceptionHandleBlacklistedClassNameWithPrefix() throws Exception {
        String blacklistedClassNameWithPrefix = "org.springframework.beans.FooBean";

        JavaType blacklistedClassType = TypeFactory.defaultInstance().constructType(blacklistedClassNameWithPrefix);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, blacklistedClassType);

        assertEquals(blacklistedClassNameWithPrefix, validator.getLastValidatedClassName());
    }

    // -----------------------------------------------------------------------

    /**
     * Getter for the last validated class name.
     */
    private String getLastValidatedClassName() {
        SubTypeValidator validator = SubTypeValidator.instance();
        return validator._lastValidatedClassName;
    }

    // -----------------------------------------------------------------------

    /**
     * Test method for exception handling: check if a JsonMappingException is thrown when an IllegalArgumentException is thrown while processing the class hierarchy.
     */
    @Test(expected = JsonMappingException.class)
    public void testExceptionHandleIllegalArgumentException() throws Exception {
        String illegalArgumentClassName = "java.lang.reflect.IllegalArgumentException";

        JavaType illegalArgumentClassType = TypeFactory.defaultInstance().constructType(illegalArgumentClassName);

        DeserializationContext ctxt = mapper.getDeserializationContext();

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, illegalArgumentClassType);
    }
}