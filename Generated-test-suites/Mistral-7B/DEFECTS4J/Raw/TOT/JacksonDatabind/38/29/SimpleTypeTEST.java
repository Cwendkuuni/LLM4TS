package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        JavaType simpleType = SimpleType.constructUnsafe(String.class);

        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.hasGenericParameters());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeConstruction() {
        JavaType invalidType = SimpleType.constructUnsafe(Map.class);
    }

    @Test
    public void testSimpleTypeWithBindings() {
        JavaType nameTypeWithIntBinding = SimpleType.constructUnsafe(String.class, TypeBindings.mapBindings(Integer.TYPE));

        assertEquals(String.class, nameTypeWithIntBinding.getRawClass());
        assertTrue(nameTypeWithIntBinding.hasGenericParameters());
        assertEquals(Integer.TYPE, nameTypeWithIntBinding.getGenericParameterTypes()[0]);
        assertNull(nameTypeWithIntBinding.getSuperClass());
        assertNull(nameTypeWithIntBinding.getSuperInterfaces());
        assertFalse(nameTypeWithIntBinding.isContainerType());
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        JavaType stringTypeWithListSuperclass = new SimpleType(String.class, TypeBindings.emptyBindings(),
                JavaType.constructUnsafe(List.class), null);

        assertEquals(String.class, stringTypeWithListSuperclass.getRawClass());
        assertFalse(stringTypeWithListSuperclass.hasGenericParameters());
        assertSame(List.class, stringTypeWithListSuperclass.getSuperClass());
        assertNull(stringTypeWithListSuperclass.getSuperInterfaces());
        assertFalse(stringTypeWithListSuperclass.isContainerType());
    }

    @Test
    public void testSimpleTypeWithSuperInterfaces() {
        JavaType stringTypeWithListSuperInterfaces = new SimpleType(String.class, TypeBindings.emptyBindings(),
                null, new JavaType[]{JavaType.constructUnsafe(List.class)});

        assertEquals(String.class, stringTypeWithListSuperInterfaces.getRawClass());
        assertFalse(stringTypeWithListSuperInterfaces.hasGenericParameters());
        assertNull(stringTypeWithListSuperInterfaces.getSuperClass());
        assertArrayEquals(new JavaType[]{List.class}, stringTypeWithListSuperInterfaces.getSuperInterfaces());
        assertFalse(stringTypeWithListSuperInterfaces.isContainerType());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        JavaType originalStringType = SimpleType.constructUnsafe(String.class);

        JavaType copiedStringType = new SimpleType(originalStringType);

        assertEquals(originalStringType.getRawClass(), copiedStringType.getRawClass());
        assertFalse(copiedStringType.hasGenericParameters());
        assertNull(copiedStringType.getSuperClass());
        assertNull(copiedStringType.getSuperInterfaces());
        assertFalse(copiedStringType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        JavaType stringTypeWithValueHandler = new SimpleType(String.class, TypeBindings.emptyBindings(),
                null, null, null, new Object(), false);

        assertEquals(String.class, stringTypeWithValueHandler.getRawClass());
        assertFalse(stringTypeWithValueHandler.hasGenericParameters());
        assertNull(stringTypeWithValueHandler.getSuperClass());
        assertNull(stringTypeWithValueHandler.getSuperInterfaces());
        assertFalse(stringTypeWithValueHandler.isContainerType());
        assertSame(new Object(), stringTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        JavaType stringTypeWithTypeHandler = new SimpleType(String.class, TypeBindings.emptyBindings(),
                null, null, null, null, false, new Object());

        assertEquals(String.class, stringTypeWithTypeHandler.getRawClass());
        assertFalse(stringTypeWithTypeHandler.hasGenericParameters());
        assertNull(stringTypeWithTypeHandler.getSuperClass());
        assertNull(stringTypeWithTypeHandler.getSuperInterfaces());
        assertFalse(stringTypeWithTypeHandler.isContainerType());
        assertSame(new Object(), stringTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        JavaType stringType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                null, null, null, null, false);
        JavaType stringTypeWithStatic = stringType.withStaticTyping();

        assertEquals(String.class, stringType.getRawClass());
        assertFalse(stringType.hasGenericParameters());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertFalse(stringType.isContainerType());
        assertFalse(stringTypeWithStatic.isStatic());
        assertTrue(stringTypeWithStatic.isStatic());
    }
}