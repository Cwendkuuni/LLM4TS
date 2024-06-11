package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(SimpleType.class, simpleType.getClass());

        SimpleType simpleTypeWithBindings = new SimpleType(stringClass, TypeBindings.emptyBindings());
        assertEquals(String.class, simpleTypeWithBindings.getRawClass());
        assertEquals(SimpleType.class, simpleTypeWithBindings.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithBindings.getBindings());

        SimpleType simpleTypeWithSuperclass = new SimpleType(String.class, TypeBindings.emptyBindings(),
                TypeFactory.untypedClass(Object.class), null);
        assertEquals(String.class, simpleTypeWithSuperclass.getRawClass());
        assertEquals(SimpleType.class, simpleTypeWithSuperclass.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithSuperclass.getBindings());
        assertNull(simpleTypeWithSuperclass.getSuperclass());
        assertNull(simpleTypeWithSuperclass.getSuperInterfaces());

        SimpleType simpleTypeWithSuperclassAndInterfaces = new SimpleType(String.class, TypeBindings.emptyBindings(),
                TypeFactory.untypedClass(Object.class), new JavaType[] { TypeFactory.stringArrayType() });
        assertEquals(String.class, simpleTypeWithSuperclassAndInterfaces.getRawClass());
        assertEquals(SimpleType.class, simpleTypeWithSuperclassAndInterfaces.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithSuperclassAndInterfaces.getBindings());
        assertEquals(TypeFactory.untypedClass(Object.class), simpleTypeWithSuperclassAndInterfaces.getSuperclass());
        assertEquals(new JavaType[] { TypeFactory.stringArrayType() }, simpleTypeWithSuperclassAndInterfaces.getSuperInterfaces());

        SimpleType simpleTypeCopy = new SimpleType(simpleType);
        assertEquals(String.class, simpleTypeCopy.getRawClass());
        assertEquals(SimpleType.class, simpleTypeCopy.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeCopy.getBindings());
        assertEquals(simpleType.getSuperclass(), simpleTypeCopy.getSuperclass());
        assertArrayEquals(simpleType.getSuperInterfaces(), simpleTypeCopy.getSuperInterfaces());
    }

    @Test
    public void testConstructStaticMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = SimpleType.construct(stringClass);
        assertEquals(String.class, simpleType1.getRawClass());
        assertEquals(SimpleType.class, simpleType1.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType1.getBindings());

        SimpleType simpleType2 = SimpleType.construct(String[].class);
        assertEquals(String[].class, simpleType2.getRawClass());
        assertEquals(SimpleType.class, simpleType2.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType2.getBindings());

        SimpleType simpleType3 = SimpleType.construct(List.class);
        assertEquals(List.class, simpleType3.getRawClass());
        assertEquals(SimpleType.class, simpleType3.getClass());
        assertTrue(simpleType3.getBindings().isEmpty());
        assertNull(simpleType3.getSuperclass());
        assertNull(simpleType3.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidType() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArrayType() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testNarrowMethod() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String[].class);

        assertNull(simpleType1.narrow(String.class));
        assertNull(simpleType1.narrow(String[].class));

        assertEquals(simpleType1, simpleType1.narrow(SimpleType.class));

        SimpleType narrowedType1 = simpleType2.narrow(String.class);
        assertEquals(String.class, narrowedType1.getRawClass());
        assertEquals(SimpleType.class, narrowedType1.getClass());
        assertEquals(TypeBindings.emptyBindings(), narrowedType1.getBindings());
        assertNull(narrowedType1.getSuperclass());
        assertNull(narrowedType1.getSuperInterfaces());

        SimpleType narrowedType2 = simpleType2.narrow(SimpleType.class);
        assertEquals(String[].class, narrowedType2.getRawClass());
        assertEquals(SimpleType.class, narrowedType2.getClass());
        assertEquals(TypeBindings.emptyBindings(), narrowedType2.getBindings());
        assertNull(narrowedType2.getSuperclass());
        assertArrayEquals(new JavaType[] {}, narrowedType2.getSuperInterfaces());
    }

    @Test
    public void testWithContentTypeMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType
                .withContentType(JavaType.constructParametricType(ArrayList.class, JavaType.ofClass(Integer.class))));
    }

    @Test
    public void testWithTypeHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(new MyTypeHandler());
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(String.class, simpleTypeWithHandler.getRawClass());
        assertEquals(SimpleType.class, simpleTypeWithHandler.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithHandler.getBindings());
        assertNull(simpleTypeWithHandler.getSuperclass());
        assertNull(simpleTypeWithHandler.getSuperInterfaces());
        assertNotSame(simpleType.getTypeHandler(), simpleTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType
                .withContentTypeHandler(new MyContentTypeHandler()));
    }

    @Test
    public void testWithValueHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithHandler = simpleType.withValueHandler(new MyValueHandler());
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(String.class, simpleTypeWithHandler.getRawClass());
        assertEquals(SimpleType.class, simpleTypeWithHandler.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithHandler.getBindings());
        assertNull(simpleTypeWithHandler.getSuperclass());
        assertNull(simpleTypeWithHandler.getSuperInterfaces());
        assertNotSame(simpleType.getValueHandler(), simpleTypeWithHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType
                .withContentValueHandler(new MyContentValueHandler()));
    }

    @Test
    public void testWithStaticTypingMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithStaticTyping = simpleType.withStaticTyping();
        assertNotSame(simpleType, simpleTypeWithStaticTyping);
        assertEquals(String.class, simpleTypeWithStaticTyping.getRawClass());
        assertEquals(SimpleType.class, simpleTypeWithStaticTyping.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithStaticTyping.getBindings());
        assertNull(simpleTypeWithStaticTyping.getSuperclass());
        assertNull(simpleTypeWithStaticTyping.getSuperInterfaces());
        assertTrue(simpleTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testRefineMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String[].class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("class java.lang.String", simpleType.buildCanonicalName());

        SimpleType simpleTypeWithGenericType = new SimpleType(List.class);
        assertEquals("class java.util.List<>", simpleTypeWithGenericType.buildCanonicalName());
    }

    @Test
    public void testIsContainerTypeMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expectedSignature = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actualSignature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expectedSignature = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actualSignature = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());

        SimpleType simpleTypeWithGenericType = new SimpleType(List.class);
        StringBuilder expectedSignatureList = new StringBuilder("[Ljava.util.List;<T:Ljava/lang/Object;>;");
        StringBuilder actualSignatureList = simpleTypeWithGenericType.getGenericSignature(new StringBuilder());
        assertEquals(expectedSignatureList.toString(), actualSignatureList.toString());
    }

    private JavaType containedType(int index, SimpleType simpleType) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        TypeBindings bindings = simpleType.getBindings();
        JavaType javaType = bindings.get(index);
        if (javaType == null) {
            throw new IllegalArgumentException("No generic type binding at index: " + index);
        }
        return javaType;
    }

    private void assertArrayEquals(JavaType[] expected, JavaType[] actual) {
        if (expected.length != actual.length) {
            fail("Array sizes do not match. Expected: " + expected.length + ", Actual: " + actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Element at index [" + i + "] does not match.", expected[i], actual[i]);
        }
    }

    private static class MyTypeHandler {
        // Empty placeholder for type handler test
    }

    private static class MyContentTypeHandler {
        // Empty placeholder for content type handler test
    }

    private static class MyValueHandler {
        // Empty placeholder for value handler test
    }

    private static class MyContentValueHandler {
        // Empty placeholder for content value handler test
    }
}