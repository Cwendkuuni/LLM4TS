package com.fasterxml.jackson.databind.type; // include this package declaration if needed

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;

public class SimpleTypeTest {

    

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperclass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInterfaces() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.constructType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{TypeFactory.constructType(List.class)};
        SimpleType simpleType = new SimpleType(stringClass, bindings, superClass, superInterfaces);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperclass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(originalSimpleType.getBindings(), copiedSimpleType.getBindings());
        assertEquals(originalSimpleType.getSuperclass(), copiedSimpleType.getSuperclass());
        assertArrayEquals(originalSimpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertEquals(originalSimpleType.isStatic(), copiedSimpleType.isStatic());
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, handler, false);
        assertNull(simpleType.getWithContentTypeHandler());
        assertEquals(handler, simpleType.withContentTypeHandler(handler));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructUnsafeWithNullClass() {
        Class<?> raw = null;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithMapClass() {
        Class<Map<String, Integer>> mapClass = new HashMap<String, Integer>().getClass();
        SimpleType simpleType = new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithCollectionClass() {
        Class<List<Integer>> collectionClass = new ArrayList<Integer>().getClass();
        SimpleType simpleType = new SimpleType(collectionClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithArrayClass() {
        Class<int[]> arrayClass = int[].class;
        SimpleType simpleType = new SimpleType(arrayClass);
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, handler, null, false);
        assertNull(simpleType.getValueHandler());
        assertEquals(handler, simpleType.withValueHandler(handler));
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, handler, false);
        assertNull(simpleType.getTypeHandler());
        assertEquals(handler, simpleType.withTypeHandler(handler));
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertFalse(simpleType.isStaticTyping());
        assertTrue(simpleType.withStaticTyping().isStaticTyping());
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType = new SimpleType(integerClass, TypeBindings.emptyBindings(), null, null);
        assertNull(simpleType.refine(Integer[].class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testSimpleTypeCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
    }

    @Test
    public void testSimpleTypeToString() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<String> stringClass1 = String.class;
        Class<String> stringClass2 = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(stringClass2, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testSimpleTypeNotEqualsNullObject() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.equals(null));
    }

    @Test
    public void testSimpleTypeNotEqualsDifferentClass() {
        Class<String> stringClass1 = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType1 = new SimpleType(stringClass1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(integerClass, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testSimpleTypeNotEqualsDifferentBindings() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        TypeBindings bindings = TypeBindings.emptyBindings();
        bindings.put(0, new SimpleType(Integer.TYPE));
        SimpleType simpleType2 = new SimpleType(stringClass, bindings, null, null);
        assertFalse(simpleType1.equals(simpleType2));
    }

    
}