
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {
    
    private SubTypeValidator subTypeValidator;
    private DeserializationContext deserializationContext;
    
    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
        deserializationContext = new DeserializationContext(null);
    }
    
    @Test
    public void testValidateSubType_validType() throws JsonMappingException {
        JavaType validType = subTypeValidator.validateSubType(deserializationContext, JavaTypeParser.parse("java.lang.String"));
        assertEquals("java.lang.String", validType.getRawClass().getName());
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_invalidType() throws JsonMappingException {
        subTypeValidator.validateSubType(deserializationContext, JavaTypeParser.parse("org.apache.commons.collections.functors.InvokerTransformer"));
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_nullType() throws JsonMappingException {
        subTypeValidator.validateSubType(deserializationContext, null);
    }
}

