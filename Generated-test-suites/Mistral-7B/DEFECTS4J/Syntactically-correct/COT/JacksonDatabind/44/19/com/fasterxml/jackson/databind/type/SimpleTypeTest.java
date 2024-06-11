package com.fasterxml.jackson.databind.type; // Make sure the test package is the same as the class package

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    // Testing the constructors

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertTrue(stringType.isStatic());
        assertEquals(TypeBindings.emptyBindings(), stringType.getBindings());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertNull(stringType.getValueHandler());
        assertNull(stringType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeConstructorWithBindings() {
        Class<List<Integer>> listIntClass = List.class.getTypeParameters()[0].getType();
        TypeBindings bindings = TypeBindings.emptyBindings();
        bindings.put(listIntClass, new SimpleType(Integer.TYPE));
        SimpleType listIntType = new SimpleType(listIntClass, bindings, null, null);
        assertEquals(List.class, listIntType.getRawClass());
        assertTrue(listIntType.isStatic());
        assertEquals(bindings, listIntType.getBindings());
        assertNull(listIntType.getSuperClass());
        assertNull(listIntType.getSuperInterfaces());
        assertNull(listIntType.getValueHandler());
        assertNull(listIntType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalStringType = new SimpleType(stringClass);
        SimpleType copiedStringType = new SimpleType(originalStringType);
        assertEquals(originalStringType.getRawClass(), copiedStringType.getRawClass());
        assertEquals(originalStringType.isStatic(), copiedStringType.isStatic());
        assertEquals(originalStringType.getBindings(), copiedStringType.getBindings());
        assertEquals(originalStringType.getSuperClass(), copiedStringType.getSuperClass());
        assertArrayEquals(originalStringType.getSuperInterfaces(), copiedStringType.getSuperInterfaces());
        assertEquals(originalStringType.getValueHandler(), copiedStringType.getValueHandler());
        assertEquals(originalStringType.getTypeHandler(), copiedStringType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType stringType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertFalse(stringType.isStatic());
        assertNull(stringType.getBindings());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertNull(stringType.getValueHandler());
        assertNull(stringType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructDeprecated() {
        Class<List<Integer>> listIntClass = List.class.getTypeParameters()[0].getType();
        SimpleType.construct(listIntClass);
    }

    // Testing the getter methods

    @Test
    public void testGetRawClass() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getRawClass());
    }

    @Test
    public void testIsStatic() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertTrue(stringType.isStatic());

        Class<List<Integer>> listIntClass = List.class.getTypeParameters()[0].getType();
        SimpleType listIntType = new SimpleType(listIntClass, TypeBindings.emptyBindings(), null, null);
        assertFalse(listIntType.isStatic());
    }

    @Test
    public void testGetBindings() {
        Class<List<Integer>> listIntClass = List.class.getTypeParameters()[0].getType();
        SimpleType listIntType = new SimpleType(listIntClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(TypeBindings.emptyBindings(), listIntType.getBindings());

        TypeBindings bindings = TypeBindings.emptyBindings();
        bindings.put(Integer.TYPE, new SimpleType(Integer.TYPE));
        SimpleType listIntTypeWithBindings = new SimpleType(listIntClass, bindings, null, null);
        assertEquals(bindings, listIntTypeWithBindings.getBindings());
    }

    @Test
    public void testGetSuperClass() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertNull(stringType.getSuperClass());

        Class<List<Integer>> listIntClass = List.class.getTypeParameters()[0].getType();
        SimpleType listIntType = new SimpleType(listIntClass, TypeBindings.emptyBindings(), null, null);
        Class<?> superClass = Object.class;
        SimpleType superClassType = new SimpleType(superClass);
        assertEquals(superClassType, listIntType.getSuperClass());
    }

    @Test
    public void testGetSuperInterfaces() {
        Class<List<Integer>> listIntClass = List.class.getTypeParameters()[0].getType();
        SimpleType listIntType = new SimpleType(listIntClass, TypeBindings.emptyBindings(), null, null);
        assertNull(listIntType.getSuperInterfaces());
    }

    @Test
    public void testgetValueHandler() {
        Class<String> stringClass = String.class;
        Object valueHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass, null, null, null, valueHandler, null, false);
        assertEquals(valueHandler, stringType.getValueHandler());

        Object otherValueHandler = new Object();
        SimpleType stringTypeWithDifferentValueHandler = new SimpleType(stringClass, null, null, null, valueHandler, otherValueHandler, false);
        assertNotEquals(valueHandler, stringTypeWithDifferentValueHandler.getValueHandler());
    }

    @Test
    public void testgetTypeHandler() {
        Class<String> stringClass = String.class;
        Object typeHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass, null, null, null, null, typeHandler, false);
        assertEquals(typeHandler, stringType.getTypeHandler());

        Object otherTypeHandler = new Object();
        SimpleType stringTypeWithDifferentTypeHandler = new SimpleType(stringClass, null, null, null, null, typeHandler, false);
        assertNotEquals(typeHandler, stringTypeWithDifferentTypeHandler.getTypeHandler());
    }

    @Test
    public void testAsStatic() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertFalse(stringType.isStatic());

        SimpleType stringTypeStatic = stringType.withStaticTyping();
        assertTrue(stringTypeStatic.isStatic());
    }

    // Testing the static methods

    @Test
    public void testConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType stringType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertFalse(stringType.isStatic());
        assertNull(stringType.getBindings());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertNull(stringType.getValueHandler());
        assertNull(stringType.getTypeHandler());
    }

    @Test
    public void testConstruct() {
        Class<List<Integer>> listIntClass = List.class.getTypeParameters()[0].getType();
        SimpleType listIntType = SimpleType.construct(listIntClass);
        assertEquals(List.class, listIntType.getRawClass());
        assertFalse(listIntType.isStatic());
        assertNull(listIntType.getBindings());
        assertNull(listIntType.getSuperClass());
        assertNull(listIntType.getSuperInterfaces());
        assertNull(listIntType.getValueHandler());
        assertNull(listIntType.getTypeHandler());
    }

    // Testing the methods that should not be used by application code

    @Test
    public void test_narrow() {
        Class<String> stringClass = String.class;
        Class<Object> objectClass = Object.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(stringType, stringType._narrow(stringClass));

        SimpleType objectType = new SimpleType(objectClass);
        assertNotNull(stringType._narrow(objectClass));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        stringType.withContentType(new SimpleType(Integer.TYPE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        Object typeHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass);
        stringType.withTypeHandler(typeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        Object contentTypeHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass);
        stringType.withContentTypeHandler(contentTypeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandler() {
        Class<String> stringClass = String.class;
        Object valueHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass);
        stringType.withValueHandler(valueHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        Class<String> stringClass = String.class;
        Object contentValueHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass);
        stringType.withContentValueHandler(contentValueHandler);
    }

    @Test
    public void testRefine() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertNull(stringType.refine(String.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<List<Integer>>> listListIntClass = List.class.getTypeParameters()[0].getType();
        TypeBindings bindings = TypeBindings.emptyBindings();
        bindings.put(List.class.getTypeParameters()[0].getType(), new SimpleType(Integer.TYPE));
        SimpleType listListIntType = new SimpleType(listListIntClass, bindings, null, null);
        assertEquals("java.util.List<java.util.List<java.lang.Integer>>", listListIntType.buildCanonicalName());
    }

    
}