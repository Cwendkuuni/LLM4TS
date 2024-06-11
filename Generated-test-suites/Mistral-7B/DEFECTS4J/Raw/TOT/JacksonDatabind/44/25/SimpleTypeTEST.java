package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals(Integer.class, intType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), intType.getBindings());
        assertEquals(null, intType.getSuperClass());
        assertEquals(null, intType.getSuperInterfaces());
        assertFalse(intType.isContainerType());
    }

    @Test
    public void testSimpleTypeCopiedConstruction() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        SimpleType copiedIntType = new SimpleType(intType);
        assertEquals(intType, copiedIntType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalConstructionWithMapClass() {
        Map<String, Integer> map = new HashMap<>();
        SimpleType mapType = new SimpleType(map.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalConstructionWithCollectionClass() {
        List<String> list = new ArrayList<>();
        SimpleType listType = new SimpleType(list.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalConstructionWithArrayClass() {
        int[] ints = new int[10];
        SimpleType arrayType = new SimpleType(ints.getClass());
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        // Simple types have no content types
        SimpleType intType = new SimpleType(Integer.class);
        try {
            intType.withContentTypeHandler(new Object());
            fail();
        } catch (IllegalArgumentException expected) {
            // Expected
        }
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        SimpleType intType = new SimpleType(Integer.class);
        Object handler = new Object();
        SimpleType newIntType = intType.withValueHandler(handler);
        assertNotEquals(intType, newIntType);
        assertEquals(handler, newIntType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalNarrowingToNonSubType() {
        SimpleType intType = new SimpleType(Integer.class);
        SimpleType stringType = new SimpleType(String.class);
        intType.narrow(String.class);
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        SimpleType intType = new SimpleType(Integer.class);
        SimpleType staticIntType = intType.withStaticTyping();
        assertTrue(staticIntType.isStaticTyping());
        assertFalse(intType.isStaticTyping());
    }

    @Test
    public void testTypeFactoryConstructTypeMethod() {
        Class<Integer> intClass = Integer.class;
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);
        SimpleType simpleIntType = (SimpleType) javaIntType;
        assertEquals(Integer.class, simpleIntType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleIntType.getBindings());
        assertEquals(null, simpleIntType.getSuperClass());
        assertEquals(null, simpleIntType.getSuperInterfaces());
        assertFalse(simpleIntType.isContainerType());
    }

    @Test
    public void testTypeFactoryConstructTypeWithBindingsMethod() {
        Class<Integer> intClass = Integer.class;
        TypeBindings bindings = new TypeBindings();
        bindings.putBinding(Integer.class, String.class);
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass, bindings);
        SimpleType simpleIntType = (SimpleType) javaIntType;
        assertEquals(Integer.class, simpleIntType.getRawClass());
        assertEquals(bindings, simpleIntType.getBindings());
        assertEquals(null, simpleIntType.getSuperClass());
        assertEquals(null, simpleIntType.getSuperInterfaces());
        assertFalse(simpleIntType.isContainerType());
    }

    @Test
    public void testTypeFactoryConstructTypeWithSuperClassAndBindingsMethod() {
        Class<Integer> intClass = Integer.class;
        TypeBindings bindings = new TypeBindings();
        bindings.putBinding(Integer.class, String.class);
        JavaType javaIntType = TypeFactory.defaultInstance()
                .constructType(intClass, bindings, Object.class);
        SimpleType simpleIntType = (SimpleType) javaIntType;
        assertEquals(Integer.class, simpleIntType.getRawClass());
        assertEquals(bindings, simpleIntType.getBindings());
        assertEquals(Object.class, simpleIntType.getSuperClass());
        assertArrayEquals(new Class[0], simpleIntType.getSuperInterfaces());
        assertFalse(simpleIntType.isContainerType());
    }

    @Test
    public void testTypeFactoryConstructTypeWithSuperClassSuperInterfacesAndBindingsMethod() {
        Class<Integer> intClass = Integer.class;
        TypeBindings bindings = new TypeBindings();
        bindings.putBinding(Integer.class, String.class);
        JavaType javaIntType = TypeFactory.defaultInstance()
                .constructType(intClass, bindings, Object.class, String[].class);
        SimpleType simpleIntType = (SimpleType) javaIntType;
        assertEquals(Integer.class, simpleIntType.getRawClass());
        assertEquals(bindings, simpleIntType.getBindings());
        assertEquals(Object.class, simpleIntType.getSuperClass());
        assertArrayEquals(new Class[]{String[].class}, simpleIntType.getSuperInterfaces());
        assertFalse(simpleIntType.isContainerType());
    }
}