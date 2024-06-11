package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(intClass);
        assertEquals(intClass, simpleType.getRawClass());
        assertEquals(JavaType.construct(intClass), simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
        assertEquals(simpleType.toString(), "[simple type, class java.lang.Integer]");
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<Map<String, Integer>> mapClass = Map.class.getGenericType();
        SimpleType simpleType = new SimpleType(mapClass, TypeBindings.construct(), null, null);
        assertEquals(mapClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        TypeBindings bindings = simpleType.getBindings();
        assertEquals(1, bindings.size());
        assertEquals(Map.class, bindings.getBindingForKey(Key.construct("K")).getType().getRawClass());
        assertEquals(Integer.class, bindings.getBindingForKey(Key.construct("V")).getType().getRawClass());
        assertFalse(simpleType.isContainerType());
        assertEquals(simpleType.toString(), "[simple type, class java.util.Map, bindings=[K->java.lang.Class, V->class java.lang.Integer]]");
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalSimpleType = new SimpleType(stringClass);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(originalSimpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertEquals(originalSimpleType.getBindings(), copiedSimpleType.getBindings());
        assertEquals(originalSimpleType.getTypeHandler(), copiedSimpleType.getTypeHandler());
        assertEquals(originalSimpleType.isStaticTyping(), copiedSimpleType.isStaticTyping());
    }

    @Test
    public void testConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertEquals(stringClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentException() {
        JavaType javaType = JavaType.construct(Map.class);
        SimpleType.construct(Map.class);
    }

    @Test
    public void testTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object typeHandler = new TypeDeserializerBase() {
            @Override
            public JavaType getContentType(TypeDeserializer deserializer) {
                return JavaType.construct(String[].class);
            }

            @Override
            public void setContentTypeHandler(TypeResolverBuilder builder) {
                // no-op
            }
        };
        SimpleType withTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertNotSame(simpleType.getTypeHandler(), withTypeHandler.getTypeHandler());
        assertEquals(simpleType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(simpleType.getSuperClass(), withTypeHandler.getSuperClass());
        assertEquals(simpleType.getBindings(), withTypeHandler.getBindings());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertEquals(simpleType.isStaticTyping(), withTypeHandler.isStaticTyping());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertFalse(simpleType.isStaticTyping());
        assertTrue(withStaticTyping.isStaticTyping());
        assertEquals(simpleType.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(simpleType.getSuperClass(), withStaticTyping.getSuperClass());
        assertEquals(simpleType.getBindings(), withStaticTyping.getBindings());
        assertEquals(simpleType.getTypeHandler(), withStaticTyping.getTypeHandler());
    }
}