package com.fasterxml.jackson.databind.type; // Make sure to import all necessary classes

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    // Test constructor withClass
    @Test
    public void testSimpleTypeWithClass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test constructor withClass, TypeBindings, JavaType superClass, JavaType[] superInterfaces
    @Test
    public void testSimpleTypeWithClassTypeBindingsSuperClassSuperInterfaces() {
        Class<String> stringClass = String.class;
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(stringClass, typeBindings, superClass, superInterfaces);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(typeBindings, simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleType.getSuperInterfaces());
    }

    // Test copy constructor
    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType source = new SimpleType(stringClass, typeBindings, superClass, superInterfaces);
        SimpleType target = new SimpleType(source);
        assertEquals(source, target);
    }

    // Test deprecated construct method
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructMethodDeprecated() {
        SimpleType.construct(String.class);
    }

    // Test narrow method
    @Test
    public void testSimpleTypeNarrowMethod() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType intType = new SimpleType(integerClass);
        SimpleType result = stringType.narrow(integerClass);
        assertEquals(intType, result);
    }

    // Test isContainerType method
    @Test
    public void testSimpleTypeIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse(simpleType.isContainerType());
    }

    // Test getErasedSignature method
    @Test
    public void testSimpleTypeGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder expected = new StringBuilder();
        expected.append("[simple type, class ");
        expected.append(stringClass.getName());
        expected.append(']');
        StringBuilder actual = new StringBuilder();
        simpleType.getErasedSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    // Test getGenericSignature method
    @Test
    public void testSimpleTypeGetGenericSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder expected = new StringBuilder();
        expected.append("[simple type, class ");
        expected.append(stringClass.getName());
        expected.append(']');
        StringBuilder actual = new StringBuilder();
        simpleType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    // Test equals method
    @Test
    public void testSimpleTypeEquals() {
        Class<String> stringClass = String.class;
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType1 = new SimpleType(stringClass, typeBindings, superClass, superInterfaces);
        SimpleType simpleType2 = new SimpleType(stringClass, typeBindings, superClass, superInterfaces);
        SimpleType simpleType3 = new SimpleType(String[].class, typeBindings, superClass, superInterfaces);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    
}