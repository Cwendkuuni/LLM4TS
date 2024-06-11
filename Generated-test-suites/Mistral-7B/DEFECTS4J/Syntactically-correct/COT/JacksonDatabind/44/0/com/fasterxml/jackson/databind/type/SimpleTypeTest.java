package com.fasterxml.jackson.databind.type.test; // note: make sure to import the necessary classes and dependencies

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    private Class<?> stringClass;
    private Class<?> intClass;
    private Class<?> listClass;
    private JavaType stringType;
    private JavaType intType;
    private JavaType listType;

    @Before
    public void setUp() {
        stringClass = String.class;
        intClass = Integer.class;
        listClass = new ArrayList<Integer>().getClass();
        stringType = TypeFactory.defaultInstance().constructType(stringClass);
        intType = TypeFactory.defaultInstance().constructType(intClass);
        listType = TypeFactory.defaultInstance().constructType(listClass);
    }

    @Test
    public void testSimpleTypeConstructorWithClass() {
        SimpleType simpleType = new SimpleType(stringClass);
        assertSame(String.class, simpleType.getRawClass());
        assertEquals(String.class.getName(), simpleType.getName());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType, copiedSimpleType);
    }

    @Test
    public void testConstructUnsafeMethod() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertSame(String.class, simpleType.getRawClass());
        assertEquals(String.class.getName(), simpleType.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithArray() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testNarrowMethodWithSameClass() {
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType stringSimpleTypeNarrow = stringSimpleType.narrow(String.class);
        assertEquals(stringSimpleType, stringSimpleTypeNarrow);
    }

    @Test
    public void testNarrowMethodWithSubclass() {
        SimpleType intSimpleType = new SimpleType(intClass);
        SimpleType stringSimpleType = new SimpleType(String.class);
        SimpleType stringSimpleTypeNarrow = stringSimpleType.narrow(intClass);
        assertEquals(intSimpleType, stringSimpleTypeNarrow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithUnrelatedClass() {
        SimpleType stringSimpleType = new SimpleType(String.class);
        SimpleType listSimpleType = new SimpleType(listClass);
        SimpleType listSimpleTypeNarrow = stringSimpleType.narrow(listClass);
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType stringSimpleTypeWithContentTypeHandler = stringSimpleType.withContentTypeHandler(new Object());
        assertNotSame(stringSimpleType, stringSimpleTypeWithContentTypeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeMethod() {
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType stringSimpleTypeWithContentType = stringSimpleType.withContentType(new JavaType(Object.class));
    }

    @Test
    public void testWithTypeHandlerMethod() {
        SimpleType intSimpleType = new SimpleType(intClass);
        SimpleType intSimpleTypeWithTypeHandler = intSimpleType.withTypeHandler(new Object());
        assertNotSame(intSimpleType, intSimpleTypeWithTypeHandler);
    }

    @Test
    public void testWithValueHandlerMethod() {
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType stringSimpleTypeWithValueHandler = stringSimpleType.withValueHandler(new Object());
        assertNotSame(stringSimpleType, stringSimpleTypeWithValueHandler);
    }

    @Test
    public void testIsContainerTypeMethod() {
        assertFalse(new SimpleType(String.class).isContainerType());
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        SimpleType stringSimpleType = new SimpleType(String.class);
        assertEquals("java.lang.String", stringSimpleType.buildCanonicalName());

        TypeBindings bindings = new TypeBindings();
        bindings.putBinding(intType);
        SimpleType intWithBindingSimpleType = new SimpleType(intClass, bindings);
        assertEquals("java.lang.Integer", intWithBindingSimpleType.buildCanonicalName());
        assertEquals("[Ljava.lang.Integer;", intWithBindingSimpleType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        SimpleType stringSimpleType = new SimpleType(String.class);
        assertEquals("Ljava/lang/String;", stringSimpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        SimpleType stringSimpleType = new SimpleType(String.class);
        assertEquals("Ljava/lang/String;", stringSimpleType.getGenericSignature(new StringBuilder()).toString());

        TypeBindings bindings = new TypeBindings();
        bindings.putBinding(intType);
        SimpleType intWithBindingSimpleType = new SimpleType(intClass, bindings);
        assertEquals("Ljava/lang/Integer;", intWithBindingSimpleType.getGenericSignature(new StringBuilder()).toString());
        assertEquals("[Ljava.lang.Integer;", intWithBindingSimpleType.getGenericSignature(new StringBuilder()).toString());
    }
}