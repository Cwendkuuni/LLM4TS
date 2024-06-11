package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Test class for SubTypeValidator
 *
 * @author John Doe
 */
public class SubTypeValidatorTest {

    private static final SubTypeValidator validator = SubTypeValidator.instance();

    /**
     * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
     * when given a valid class name
     */
    @Test
    public void testValidateSubTypeValidClass() throws JsonMappingException, JsonProcessingException {
        JavaType validType = new JavaType(String.class);
        validator.validateSubType(new TestContext(), validType);
    }

    /**
     * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
     * when given an invalid class name that is in the default no deserialize class names set
     */
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeInvalidClassInDefaultSet() throws JsonMappingException, JsonProcessingException {
        JavaType invalidType = new JavaType(InvokerTransformer.class);
        validator.validateSubType(new TestContext(), invalidType);
    }

    /**
     * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
     * when given an invalid class name that is a Spring framework type
     */
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeInvalidSpringType() throws JsonMappingException, JsonProcessingException {
        JavaType invalidType = new JavaType(AbstractBeanFactoryPointcutAdvisor.class);
        validator.validateSubType(new TestContext(), invalidType);
    }

    /**
     * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
     * when given an interface as a class name
     */
    @Test
    public void testValidateSubTypeInterface() throws JsonMappingException, JsonProcessingException {
        JavaType invalidType = new JavaType(List.class);
        validator.validateSubType(new TestContext(), invalidType);
    }

    /**
     * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
     * when given an abstract class as a class name
     */
    @Test
    public void testValidateSubTypeAbstractClass() throws JsonMappingException, JsonProcessingException {
        JavaType invalidType = new JavaType(AbstractApplicationContext.class);
        validator.validateSubType(new TestContext(), invalidType);
    }

    /**
     * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
     * when given a class name starting with the given prefix
     */
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeSpringPrefix() throws JsonMappingException, JsonProcessingException {
        JavaType invalidType = new JavaType(AbstractBeanFactory.class);
        validator.validateSubType(new TestContext(), invalidType);
    }

    /**
     * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
     * when given a class name that is not in the default no deserialize class names set and does not match any Spring framework type
     */
    @Test
    public void testValidateSubTypeValidClassOutsideSpring() throws JsonMappingException, JsonProcessingException {
        JavaType validType = new JavaType(MyValidClass.class);
        validator.validateSubType(new TestContext(), validType);
    }

    /**
     * Test helper class for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
     */
    private static class TestContext extends DeserializationContext {
        TestContext() {
            super(null, Collections.emptyMap(), null, null, null, null, null);
        }
    }

    /**
     * A valid class name for testing purposes
     */
    private static class MyValidClass {}

}