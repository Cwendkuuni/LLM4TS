package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class SubTypeValidatorTest {

    @Test
    public void testValidClasses() {
        // Test valid classes that should not trigger an error
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctx = new DeserializationContext(new TypeIdResolver() {
            @Override
            public IdTypeId idFromValueAndType(String name, JavaType type) {
                return null;
            }

            @Override
            public Class<?> resolvedClass(IdTypeId id) {
                return null;
            }
        }, new ClassIdResolver() {
            @Override
            public Class<?> findClass(String name) {
                return Class.forName(name);
            }
        }, null);

        JavaType validType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType validType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType validType3 = TypeFactory.defaultInstance().constructType(Long.class);

        validator.validateSubType(ctx, validType1);
        validator.validateSubType(ctx, validType2);
        validator.validateSubType(ctx, validType3);
    }

    @Test
    public void testNastyClasses() {
        // Test classes that are considered dangerous and should not be deserialized
        SubTypeValidator validator = SubTypeValidator.instance();

        DeserializationContext ctx = new DeserializationContext(new TypeIdResolver() {
            @Override
            public IdTypeId idFromValueAndType(String name, JavaType type) {
                return null;
            }

            @Override
            public Class<?> resolvedClass(IdTypeId id) {
                return null;
            }
        }, new ClassIdResolver() {
            @Override
            public Class<?> findClass(String name) {
                return Class.forName(name);
            }
        }, null);

        JavaType nastyType1 = TypeFactory.defaultInstance().constructType("org.apache.commons.collections4.functors.InstantiateTransformer");
        JavaType nastyType2 = TypeFactory.defaultInstance().constructType("org.springframework.beans.factory.ObjectFactory");
        JavaType nastyType3 = TypeFactory.defaultInstance().constructType("java.rmi.server.UnicastRemoteObject");

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctx, nastyType1));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctx, nastyType2));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctx, nastyType3));
    }

    @Test
    public void testSpringFrameworkClasses() {
        // Test Spring framework classes that could cause problems via default typing
        SubTypeValidator validator = SubTypeValidator.instance();

        DeserializationContext ctx = new DeserializationContext(new TypeIdResolver() {
            @Override
            public IdTypeId idFromValueAndType(String name, JavaType type) {
                return null;
            }

            @Override
            public Class<?> resolvedClass(IdTypeId id) {
                return null;
            }
        }, new ClassIdResolver() {
            @Override
            public Class<?> findClass(String name) {
                return Class.forName(name);
            }
        }, null);

        JavaType springType1 = TypeFactory.defaultInstance().constructType("org.springframework.beans.factory.PropertyPathFactoryBean");
        JavaType springType2 = TypeFactory.defaultInstance().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        JavaType springType3 = TypeFactory.defaultInstance().constructType("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctx, springType1));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctx, springType2));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctx, springType3));
    }
}