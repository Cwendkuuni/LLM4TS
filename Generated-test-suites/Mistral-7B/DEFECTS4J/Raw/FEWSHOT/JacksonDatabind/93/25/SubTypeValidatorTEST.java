package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithValidType() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
        validator.validateSubType(mockContext(), validType);
    }

    @Test
    public void testValidateSubTypeWithIllegalBaseType() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType illegalBaseType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), illegalBaseType));
    }

    @Test
    public void testValidateSubTypeWithIllegalClass() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        String illegalClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
        JavaType illegalType = TypeFactory.defaultInstance().constructType(Class.forName(illegalClassName));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), illegalType));
    }

    @Test
    public void testValidateSubTypeWithSpringApplicationContext() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        String applicationContextClassName = "org.springframework.context.ApplicationContext";
        JavaType applicationContextType = TypeFactory.defaultInstance().constructType(Class.forName(applicationContextClassName));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), applicationContextType));
    }

    @Test
    public void testValidateSubTypeWithSpringPointcutAdvisor() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        String pointcutAdvisorClassName = "org.springframework.aop.support.AbstractPointcutAdvisor";
        JavaType pointcutAdvisorType = TypeFactory.defaultInstance().constructType(Class.forName(pointcutAdvisorClassName));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), pointcutAdvisorType));
    }

    @Test
    public void testValidateSubTypeWithSpringFileSystemXmlApplicationContext() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        String fileSystemXmlApplicationContextClassName = "org.springframework.context.support.FileSystemXmlApplicationContext";
        JavaType fileSystemXmlApplicationContextType = TypeFactory.defaultInstance().constructType(Class.forName(fileSystemXmlApplicationContextClassName));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), fileSystemXmlApplicationContextType));
    }

    @Test
    public void testValidateSubTypeWithC3P0DataSource() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        String c3p0DataSourceClassName = "com.mchange.v2.c3p0.WrapperConnectionPoolDataSource";
        JavaType c3p0DataSourceType = TypeFactory.defaultInstance().constructType(Class.forName(c3p0DataSourceClassName));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), c3p0DataSourceType));
    }

    @Test
    public void testValidateSubTypeWithTomcatBasicDataSource() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        String tomcatBasicDataSourceClassName = "org.apache.tomcat.dbcp.dbcp2.BasicDataSource";
        JavaType tomcatBasicDataSourceType = TypeFactory.defaultInstance().constructType(Class.forName(tomcatBasicDataSourceClassName));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), tomcatBasicDataSourceType));
    }

    private DeserializationContext mockContext() {
        return new TestDeserializationContext();
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public JsonParser _parser() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ObjectReader _objectReader() {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaType _typeDescriptor() {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaType _constructType(Class<?> rawType) throws JsonMappingException {
            try {
                return TypeFactory.defaultInstance().constructType(rawType);
            } catch (JsonProcessingException e) {
                throw new JsonMappingException(e.getMessage(), e.getLocation(), e);
            }
        }

        @Override
        public JavaType _constructType(Class<?> rawType, TypeSerializer valueTypeSer) throws JsonMappingException {
            try {
                return TypeFactory.defaultInstance().constructType(rawType, valueTypeSer);
            } catch (JsonProcessingException e) {
                throw new JsonMappingException(e.getMessage(), e.getLocation(), e);
            }
        }

        @Override
        public SerializerProvider _serializerProvider() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeDeserializer _findContentDeserializer(Class<?> type, DeserializationConfig config) throws JsonMappingException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object _handleUnknownProperty(String name, Object value, BeanProperty property) throws JsonMappingException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object _handleMissingValue(PropertyName name, JavaType typeHint, BeanProperty property) throws JsonMappingException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void _reportError(String msg) throws JsonMappingException {
            throw new JsonMappingException(msg);
        }

        @Override
        public void _reportError(String msg, Location location) throws JsonMappingException {
            throw new JsonMappingException(msg, location);
        }

        @Override
        public void _reportError(String msg, Throwable t) throws JsonMappingException {
            throw new JsonMappingException(msg, t);
        }
    }

}