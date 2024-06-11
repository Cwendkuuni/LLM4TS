package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithAllowedClass() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType allowedClassType = TypeFactory.defaultInstance().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, allowedClassType);
    }

    @Test
    public void testValidateSubTypeWithDisallowedClass() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType disallowedClassType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
        Set<String> illegalClassNames = new HashSet<>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
        SubTypeValidator validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = illegalClassNames;
        };
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, disallowedClassType));
    }

    @Test
    public void testValidateSubTypeWithSpringClass() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType springClassType = TypeFactory.defaultInstance().constructType(AbstractApplicationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, springClassType);
    }

    @Test
    public void testValidateSubTypeWithSpringInterface() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType springInterfaceType = TypeFactory.defaultInstance().constructType(AbstractPointcutAdvisor.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, springInterfaceType);
    }

    @Test
    public void testValidateSubTypeWithCustomIllegalClassNames() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        Set<String> customIllegalClassNames = new HashSet<>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
        customIllegalClassNames.add("com.custom.NastyClass");
        SubTypeValidator validator = new SubTypeValidator() {
            @Override
            protected Set<String> _cfgIllegalClassNames = customIllegalClassNames;
        };
        JavaType customNastyClassType = TypeFactory.defaultInstance().constructType(Object.class); // any class will trigger the test
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, customNastyClassType));
    }

    @Test
    public void testValidateSubTypeWithSubclassOfSpringClass() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType subclassOfSpringClassType = TypeFactory.defaultInstance().constructType(MyCustomApplicationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, subclassOfSpringClassType);
    }

    static class MyCustomApplicationContext extends AbstractApplicationContext {
        // empty class
    }
}