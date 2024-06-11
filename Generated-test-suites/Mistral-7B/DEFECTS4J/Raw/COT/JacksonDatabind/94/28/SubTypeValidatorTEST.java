package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

    private static final JsonMapper MAPPER = new JsonMapper();

    @Test
    public void testValidateSubType_validType() throws Exception {
        Set<String> illegalClassNames = SubTypeValidator.instance()._cfgIllegalClassNames;
        JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
        SubTypeValidator validator = new SubTypeValidator();

        validator.validateSubType(createContext(), validType);
        // No exception should be thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalType() throws Exception {
        Set<String> illegalClassNames = SubTypeValidator.instance()._cfgIllegalClassNames;
        JavaType illegalType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
        SubTypeValidator validator = new SubTypeValidator();

        validator.validateSubType(createContext(), illegalType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_springType() throws Exception {
        Set<String> illegalClassNames = SubTypeValidator.instance()._cfgIllegalClassNames;
        JavaType springType = TypeFactory.defaultInstance().constructType(Object[].class);

        SubTypeValidator validator = new SubTypeValidator();
        validator.validateSubType(createContext(), springType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_nastyClass() throws Exception {
        Set<String> illegalClassNames = SubTypeValidator.instance()._cfgIllegalClassNames;
        JavaType nastyClass = TypeFactory.defaultInstance().constructType(InvokerTransformer.class);

        SubTypeValidator validator = new SubTypeValidator();
        validator.validateSubType(createContext(), nastyClass);
    }

    @Test
    public void testValidateSubType_interfaceType() throws Exception {
        Set<String> illegalClassNames = SubTypeValidator.instance()._cfgIllegalClassNames;
        JavaType interfaceType = TypeFactory.defaultInstance().constructType(Comparable.class);

        SubTypeValidator validator = new SubTypeValidator();
        validator.validateSubType(createContext(), interfaceType);
    }

    @Test
    public void testValidateSubType_prefixSpringType() throws Exception {
        Set<String> illegalClassNames = SubTypeValidator.instance()._cfgIllegalClassNames;
        JavaType prefixSpringType = TypeFactory.defaultInstance().constructType(AbstractApplicationContext.class);

        SubTypeValidator validator = new SubTypeValidator();
        validator.validateSubType(createContext(), prefixSpringType);
    }

    @Test
    public void testValidateSubType_nullType() throws Exception {
        Set<String> illegalClassNames = SubTypeValidator.instance()._cfgIllegalClassNames;
        JavaType nullType = null;

        SubTypeValidator validator = new SubTypeValidator();

        try {
            validator.validateSubType(createContext(), nullType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testValidateSubType_emptyType() throws Exception {
        Set<String> illegalClassNames = SubTypeValidator.instance()._cfgIllegalClassNames;
        JavaType emptyType = TypeFactory.defaultInstance().constructType(Collections.emptySet().getClass());

        SubTypeValidator validator = new SubTypeValidator();

        try {
            validator.validateSubType(createContext(), emptyType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testValidateSubType_hashSetType() throws Exception {
        Set<String> illegalClassNames = SubTypeValidator.instance()._cfgIllegalClassNames;
        JavaType hashSetType = TypeFactory.defaultInstance().constructType(HashSet.class);

        SubTypeValidator validator = new SubTypeValidator();
        validator.validateSubType(createContext(), hashSetType);
    }

    private DeserializationContext createContext() {
        return new TestContext();
    }

    private static class TestContext extends DeserializationContext {
        private TestContext() {
            super(null, null, null, null, null, null, null);
        }

        @Override
        public JsonMapper _rootInstance() {
            return MAPPER;
        }

        @Override
        public ObjectMapper _config() {
            return MAPPER;
        }

        @Override
        public JavaType _type(Class<?> type) throws JsonMappingException {
            return null;
        }

        @Override
        public JavaType _handleUnknownProperty(String name, JavaType type, PropertyName propertyName, String propertyAsString) throws JsonMappingException {
            return null;
        }

        @Override
        public JavaType _handleMissingValue(Class<?> type) throws JsonMappingException {
            return null;
        }

        @Override
        public JavaType _findContentType(Class<?> parameterType) {
            return null;
        }

        @Override
        public JavaType _constructType(JavaType baseType, Class<?> rawClass, String name) throws JsonMappingException {
            return null;
        }

        @Override
        public void _reportError(JsonMappingException e) throws JsonProcessingException {
            throw e;
        }

        @Override
        public void _reportError(String msg) throws JsonProcessingException {
            throw new JsonMappingException(msg);
        }

        @Override
        public void _close() throws IOException {
            // no-op
        }
    }
}