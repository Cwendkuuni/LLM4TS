package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() {
        // Test case for forProperty() method

        // Create test objects
        JavaType baseType = JavaTypeTestHelper.createJavaType("test");
        TypeIdResolver idRes = TypeIdResolverTestHelper.createTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaTypeTestHelper.createJavaType("default");

        // Create TypeDeserializerBase object
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        // Call forProperty() method
        deserializerBase.forProperty(null);

        // Assert the result
        // TODO: Assert the result
    }

    @Test
    public void testGetTypeInclusion() {
        // Test case for getTypeInclusion() method

        // Create test objects
        JavaType baseType = JavaTypeTestHelper.createJavaType("test");
        TypeIdResolver idRes = TypeIdResolverTestHelper.createTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaTypeTestHelper.createJavaType("default");

        // Create TypeDeserializerBase object
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        // Call getTypeInclusion() method
        deserializerBase.getTypeInclusion();

        // Assert the result
        // TODO: Assert the result
    }

    @Test
    public void testBaseTypeName() {
        // Test case for baseTypeName() method

        // Create test objects
        JavaType baseType = JavaTypeTestHelper.createJavaType("test");
        TypeIdResolver idRes = TypeIdResolverTestHelper.createTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaTypeTestHelper.createJavaType("default");

        // Create TypeDeserializerBase object
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        // Call baseTypeName() method
        deserializerBase.baseTypeName();

        // Assert the result
        // TODO: Assert the result
    }

    @Test
    public void testGetPropertyName() {
        // Test case for getPropertyName() method

        // Create test objects
        JavaType baseType = JavaTypeTestHelper.createJavaType("test");
        TypeIdResolver idRes = TypeIdResolverTestHelper.createTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaTypeTestHelper.createJavaType("default");

        // Create TypeDeserializerBase object
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        // Call getPropertyName() method
        deserializerBase.getPropertyName();

        // Assert the result
        // TODO: Assert the result
    }

    @Test
    public void testGetTypeIdResolver() {
        // Test case for getTypeIdResolver() method

        // Create test objects
        JavaType baseType = JavaTypeTestHelper.createJavaType("test");
        TypeIdResolver idRes = TypeIdResolverTestHelper.createTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaTypeTestHelper.createJavaType("default");

        // Create TypeDeserializerBase object
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        // Call getTypeIdResolver() method
        deserializerBase.getTypeIdResolver();

        // Assert the result
        // TODO: Assert the result
    }

    @Test
    public void testGetDefaultImpl() {
        // Test case for getDefaultImpl() method

        // Create test objects
        JavaType baseType = JavaTypeTestHelper.createJavaType("test");
        TypeIdResolver idRes = TypeIdResolverTestHelper.createTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaTypeTestHelper.createJavaType("default");

        // Create TypeDeserializerBase object
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        // Call getDefaultImpl() method
        deserializerBase.getDefaultImpl();

        // Assert the result
        // TODO: Assert the result
    }

    @Test
    public void testToString() {
        // Test case for toString() method

        // Create test objects
        JavaType baseType = JavaTypeTestHelper.createJavaType("test");
        TypeIdResolver idRes = TypeIdResolverTestHelper.createTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaTypeTestHelper.createJavaType("default");

        // Create TypeDeserializerBase object
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        // Call toString() method
        deserializerBase.toString();

        // Assert the result
        // TODO: Assert the result
    }

}