package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SubTypeValidatorTest {

    private static final SubTypeValidator instance = SubTypeValidator.instance();

    @Test
    public void testValidateSubTypeWithAllowedClass() throws JsonMappingException {
        JavaType allowedClassType = new ClassIdType(getClass().getClassLoader(), "java.lang.String");
        instance.validateSubType(new TestContext(), allowedClassType);
    }

    @Test
    public void testValidateSubTypeWithDisallowedClass() throws JsonMappingException {
        JavaType disallowedClassType = new ClassIdType(getClass().getClassLoader(), "org.apache.commons.collections4.functors.InstantiateTransformer");
        assertThrows(JsonMappingException.class, () -> instance.validateSubType(new TestContext(), disallowedClassType));
    }

    @Test
    public void testValidateSubTypeWithSpringClass() throws JsonMappingException {
        JavaType springClassType = new ClassIdType(getClass().getClassLoader(), "org.springframework.beans.factory.config.PropertyPathFactoryBean");
        assertThrows(JsonMappingException.class, () -> instance.validateSubType(new TestContext(), springClassType));
    }

    @Test
    public void testValidateSubTypeWithSpringInterface() throws JsonMappingException {
        JavaType springInterfaceType = new ClassIdType(getClass().getClassLoader(), "org.springframework.beans.factory.config.ApplicationContextAware");
        instance.validateSubType(new TestContext(), springInterfaceType);
    }

    @Test
    public void testValidateSubTypeWithSubclassOfAllowedClass() throws JsonMappingException {
        JavaType subclassType = new ClassIdType(getClass().getClassLoader(), "com.example.MyStringType");
        instance.validateSubType(new TestContext(), subclassType);
    }

    @Test
    public void testValidateSubTypeWithCustomSet() throws JsonMappingException {
        Set<String> customIllegalClassNames = new HashSet<String>() {{
            add("com.example.MyCustomNastyClass");
        }};
        SubTypeValidator customValidator = new SubTypeValidator() {{
            _cfgIllegalClassNames = customIllegalClassNames;
        }};
        JavaType customNastyClassType = new ClassIdType(getClass().getClassLoader(), "com.example.MyCustomNastyClass");
        assertThrows(JsonMappingException.class, () -> customValidator.validateSubType(new TestContext(), customNastyClassType));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public BeanDescription _currentTypeDescription() {
            return null;
        }

        @Override
        public ObjectNode _createObjectNode() {
            return null;
        }

        @Override
        public JsonNode getRootValue() {
            return null;
        }

        @Override
        public Object readValueAs(Class<?> clazz) throws IOException {
            return null;
        }
    }
}