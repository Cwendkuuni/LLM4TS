package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for SubTypeValidator
 *
 * @author John Doe
 */
public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        JavaType validType = mapper.getTypeFactory().constructType(String.class);
        validator.validateSubType(new DeserializationContext(new TestContext()), validType);
    }

    @Test
    public void testValidateSubType_NastyClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType nastyType = mapper.getTypeFactory().constructType(org.apache.commons.collections.functors.InvokerTransformer.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(new TestContext()), nastyType));
    }

    @Test
    public void testValidateSubType_SpringApplicationContext() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        JavaType applicationContextType = mapper.getTypeFactory().constructType(org.springframework.context.ApplicationContext.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(new TestContext()), applicationContextType));
    }

    @Test
    public void testValidateSubType_CustomIllegalType() throws JsonMappingException {
        SubTypeValidator validator = new SubTypeValidator();
        Set<String> customIllegalTypes = new HashSet<String>();
        customIllegalTypes.add("com.example.CustomIllegalType");
        validator._cfgIllegalClassNames = customIllegalTypes;
        JavaType illegalType = mapper.getTypeFactory().constructType(Object.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(new TestContext()), illegalType));
    }

    @Test
    public void testValidateSubType_EmptyClassNamesSet() throws JsonMappingException {
        SubTypeValidator validator = new SubTypeValidator();
        validator._cfgIllegalClassNames = Collections.emptySet();
        JavaType emptyType = mapper.getTypeFactory().constructType(Object.class);
        validator.validateSubType(new DeserializationContext(new TestContext()), emptyType);
    }

    private static class TestContext extends DeserializationContext {
        TestContext() {
            super(null, new ObjectMapper());
        }
    }
}