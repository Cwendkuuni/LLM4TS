package com.example.junit4;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertEquals("java.lang.String", simpleType.toString());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<?> listClass = List.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        SimpleType simpleType = new SimpleType(listClass, bindings, null, null, null, null, false);
        assertEquals(List.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new Class[] { Integer.class }, simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertEquals("[Ljava.lang.Integer;", simpleType.toString());
        assertEquals(Integer.class, simpleType.containedType(0).getRawClass());
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<?> parentClass = Object.class;
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, null, new SimpleType(parentClass), null, null, null, false);
        assertEquals(String.class, simpleType.getRawClass());
        assertNotNull(simpleType.getSuperClass());
        assertEquals(Object.class, simpleType.getSuperClass().getRawClass());
        assertArrayEquals(new Class[] {}, simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertEquals("java.lang.String", simpleType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentException() {
        Class<?> mapClass = Map.class;
        new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentExceptionCollection() {
        Class<?> collectionClass = ArrayList.class;
        new SimpleType(collectionClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentExceptionArray() {
        Class<?> arrayClass = int[].class;
        new SimpleType(arrayClass);
    }

    @Test
    public void testNarrow() {
        Class<?> stringClass = String.class;
        Class<?> stringSubclass = StringBuffer.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType narrowedType = simpleType._narrow(StringSubclass);
        assertEquals(StringBuffer.class, narrowedType.getRawClass());
        assertNotSame(simpleType, narrowedType);
    }

    /**
     * Test for {@link SimpleType#construct(Class)} deprecated method.
     */
    @Test
    public void testDeprecatedConstruct() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertEquals("java.lang.String", simpleType.toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        throw new RuntimeException("Method not applicable for SimpleType");
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object() {};
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertEquals(handler, withHandler.getTypeHandler());
        assertNotSame(simpleType, withHandler);
    }

    @Test
    public void testWithContentTypeHandlerEmptyList() {
        throw new RuntimeException("Method not applicable for SimpleType");
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object() {};
        SimpleType withHandler = simpleType.withValueHandler(handler);
        assertEquals(handler, withHandler.getValueHandler());
        assertNotSame(simpleType, withHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        throw new RuntimeException("Method not applicable for SimpleType");
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withStatic = simpleType.withStaticTyping();
        assertTrue(withStatic.isStatic());
        assertNotSame(simpleType, withStatic);
    }

    @Test
    public void testRefine() {
        throw new RuntimeException("Method not applicable for SimpleType");
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> listClass = List.class;
        SimpleType simpleType = new SimpleType(listClass);
        assertEquals("[Ljava.lang.Object;", simpleType.buildCanonicalName());

        JavaType listOfIntegerType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        SimpleType listOfIntegerTypeSimple = (SimpleType) listOfIntegerType;
        assertEquals("[Ljava.lang.Integer;", listOfIntegerTypeSimple.buildCanonicalName());
    }

    @Test
    public void testToString() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }
}