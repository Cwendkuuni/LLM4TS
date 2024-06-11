package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Modifier;

import org.junit.Test;

import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() {
        // Create a simple JavaType instance
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);

        // Check properties
        assertEquals("java.lang.String", javaType.getRawClass().getName());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isConcrete());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());

        // Check getTypeHandlers
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());

        // Test methods
        JavaType stringType = javaType.narrowBy(String[].class);
        assertEquals("java.lang.String[]", stringType.getRawClass().getName());
        assertFalse(stringType.isAbstract());
        assertFalse(stringType.isInterface());
        assertFalse(stringType.isPrimitive());
        assertFalse(stringType.isConcrete());
        assertFalse(stringType.isThrowable());
        assertFalse(stringType.isArrayType());
        assertFalse(stringType.isEnumType());
        assertFalse(stringType.isFinal());
        assertFalse(stringType.isContainerType());
        assertFalse(stringType.isCollectionLikeType());
        assertFalse(stringType.isMapLikeType());
        assertFalse(stringType.useStaticType());

        JavaType intType = javaType.widenBy(Integer.class);
        assertEquals("java.lang.Integer", intType.getRawClass().getName());
        assertFalse(intType.isAbstract());
        assertFalse(intType.isInterface());
        assertFalse(intType.isPrimitive());
        assertFalse(intType.isConcrete());
        assertFalse(intType.isThrowable());
        assertFalse(intType.isArrayType());
        assertFalse(intType.isEnumType());
        assertFalse(intType.isFinal());
        assertFalse(intType.isContainerType());
        assertFalse(intType.isCollectionLikeType());
        assertFalse(intType.isMapLikeType());
        assertFalse(intType.useStaticType());

        JavaType numberType = javaType.narrowBy(Number.class);
        assertEquals("java.lang.Number", numberType.getRawClass().getName());
        assertFalse(numberType.isAbstract());
        assertFalse(numberType.isInterface());
        assertFalse(numberType.isPrimitive());
        assertFalse(numberType.isConcrete());
        assertFalse(numberType.isThrowable());
        assertFalse(numberType.isArrayType());
        assertFalse(numberType.isEnumType());
        assertFalse(numberType.isFinal());
        assertFalse(numberType.isContainerType());
        assertFalse(numberType.isCollectionLikeType());
        assertFalse(numberType.isMapLikeType());
        assertFalse(numberType.useStaticType());

        // Test exception when impossible to narrow
        try {
            JavaType invalidNarrow = javaType.narrowBy(java.lang.Boolean.class);
            fail("Expected IllegalArgumentException when narrowing to boolean");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testJavaTypeTypeHandlers() {
        // Create a JavaType instance with type handlers
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(new TypeDeserializerBase() {});

        // Set value handler
        TypeDeserializer valueHandler = new TypeDeserializerBase();
        JavaType javaTypeWithValueHandler = javaType.withValueHandler(valueHandler);
        assertEquals(valueHandler, javaTypeWithValueHandler.getValueHandler());

        // Set type handler
        TypeDeserialator typeHandler = new TypeDeserialator() {
            @Override
            public JavaType getType(DeserializationContext c, BeanProperty property, TypeDeserializer valueHandler)
                    throws IOException {
                return null;
            }
        };
        JavaType javaTypeWithTypeHandler = javaType
                .withTypeHandler(typeHandler);
        assertEquals(typeHandler, javaTypeWithTypeHandler.getTypeHandler());

        // Test methods
        JavaType typeHandlerJavaType = javaTypeWithTypeHandler
                .withTypeHandler(JavaType.constructUnresolved(int[].class));
        assertEquals(int[].class, typeHandlerJavaType.getRawClass());
        assertFalse(typeHandlerJavaType.isAbstract());
        assertFalse(typeHandlerJavaType.isInterface());
        assertFalse(typeHandlerJavaType.isPrimitive());
        assertFalse(typeHandlerJavaType.isConcrete());
        assertFalse(typeHandlerJavaType.isThrowable());
        assertFalse(typeHandlerJavaType.isArrayType());
        assertFalse(typeHandlerJavaType.isEnumType());
        assertFalse(typeHandlerJavaType.isFinal());
        assertFalse(typeHandlerJavaType.isContainerType());
        assertFalse(typeHandlerJavaType.isCollectionLikeType());
        assertFalse(typeHandlerJavaType.isMapLikeType());
        assertFalse(typeHandlerJavaType.useStaticType());
        assertNull(typeHandlerJavaType.getValueHandler());
        assertEquals(typeHandler, typeHandlerJavaType.getTypeHandler());

        // Test narrowing
        JavaType intType = javaType.narrowBy(int[].class);
        assertEquals(int[].class, intType.getRawClass());
        assertFalse(intType.isAbstract());
        assertFalse(intType.isInterface());
        assertFalse(intType.isPrimitive());
        assertFalse(intType.isConcrete());
        assertFalse(intType.isThrowable());
        assertFalse(intType.isArrayType());
        assertFalse(intType.isEnumType());
        assertFalse(intType.isFinal());
        assertFalse(intType.isContainerType());
        assertFalse(intType.isCollectionLikeType());
        assertFalse(intType.isMapLikeType());
        assertFalse(intType.useStaticType());
        assertNull(intType.getValueHandler());
        assertNull(intType.getTypeHandler());

        // Test widening
        JavaType numberType = javaType.widenBy(Number[].class);
        assertEquals(Number[].class, numberType.getRawClass());
        assertFalse(numberType.isAbstract());
        assertFalse(numberType.isInterface());
        assertFalse(numberType.isPrimitive());
        assertFalse(numberType.isConcrete());
        assertFalse(numberType.isThrowable());
        assertFalse(numberType.isArrayType());
        assertFalse(numberType.isEnumType());
        assertFalse(numberType.isFinal());
        assertFalse(numberType.isContainerType());
        assertFalse(numberType.isCollectionLikeType());
        assertFalse(numberType.isMapLikeType());
        assertFalse(numberType.useStaticType());
        assertNull(numberType.getValueHandler());
        assertNull(numberType.getTypeHandler());

        // Test narrowing with type handler
        JavaType numberIntType = javaTypeWithTypeHandler
                .narrowBy(int[].class);
        assertEquals(int[].class, numberIntType.getRawClass());
        assertFalse(numberIntType.isAbstract());
        assertFalse(numberIntType.isInterface());
        assertFalse(numberIntType.isPrimitive());
        assertFalse(numberIntType.isConcrete());
        assertFalse(numberIntType.isThrowable());
        assertFalse(numberIntType.isArrayType());
        assertFalse(numberIntType.isEnumType());
        assertFalse(numberIntType.isFinal());
        assertFalse(numberIntType.isContainerType());
        assertFalse(numberIntType.isCollectionLikeType());
        assertFalse(numberIntType.isMapLikeType());
        assertFalse(numberIntType.useStaticType());
        assertNull(numberIntType.getValueHandler());
        assertEquals(typeHandler, numberIntType.getTypeHandler());
    }
}