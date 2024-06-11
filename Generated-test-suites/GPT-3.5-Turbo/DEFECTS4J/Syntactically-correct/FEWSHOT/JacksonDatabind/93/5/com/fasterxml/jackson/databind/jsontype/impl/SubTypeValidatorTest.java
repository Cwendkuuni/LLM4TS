package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {
    private static final SubTypeValidator validator = SubTypeValidator.instance();
    
    @Test
    public void testValidateSubType_ValidType() {
        try {
            validator.validateSubType(null, JavaType.Foo.class);
        } catch (JsonMappingException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testValidateSubType_IllegalType() {
        try {
            validator.validateSubType(null, JavaType.Bar.class);
            fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            String expectedMessage = "Illegal type (com.fasterxml.jackson.databind.jsontype.impl.JavaType$Bar) to deserialize: prevented for security reasons";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_SpringFrameworkType() {
        try {
            validator.validateSubType(null, JavaType.AbstractApplicationContext.class);
            fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            String expectedMessage = "Illegal type (org.springframework.context.support.AbstractApplicationContext) to deserialize: prevented for security reasons";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}


public class JavaType {
    public static class Foo {}
    public static class Bar {}
}