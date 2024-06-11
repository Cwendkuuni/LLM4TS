package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor
        SimpleType simpleType1 = new SimpleType(String.class);
        assertEquals(String.class, simpleType1.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType1.getBindings());
        assertEquals(null, simpleType1.getSuperClass());
        assertEquals(null, simpleType1.getSuperInterfaces());
        assertEquals(0, simpleType1.getGenericTypes().length);
        assertEquals(null, simpleType1.getValueHandler());
        assertEquals(null, simpleType1.getTypeHandler());
        assertFalse(simpleType1.isStatic());

        // Test constructor with bindings
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(String.class, simpleType2.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType2.getBindings());
        assertEquals(null, simpleType2.getSuperClass());
        assertEquals(null, simpleType2.getSuperInterfaces());
        assertEquals(0, simpleType2.getGenericTypes().length);
        assertEquals(null, simpleType2.getValueHandler());
        assertEquals(null, simpleType2.getTypeHandler());
        assertFalse(simpleType2.isStatic());

        // Test copy constructor
        SimpleType simpleType3 = new SimpleType(simpleType1);
        assertEquals(String.class, simpleType3.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType3.getBindings());
        assertEquals(null, simpleType3.getSuperClass());
        assertEquals(null, simpleType3.getSuperInterfaces());
        assertEquals(0, simpleType3.getGenericTypes().length);
        assertEquals(null, simpleType3.getValueHandler());
        assertEquals(null, simpleType3.getTypeHandler());
        assertFalse(simpleType3.isStatic());

        // Test constructor with superclass and bindings
        SimpleType simpleType4 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), TypeFactory.uncheckedRawType(List.class), null);
        assertEquals(Integer.class, simpleType4.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType4.getBindings());
        assertEquals(List.class, simpleType4.getSuperClass());
        assertEquals(null, simpleType4.getSuperInterfaces());
        assertEquals(new Class[0], simpleType4.getGenericTypes());
        assertEquals(null, simpleType4.getValueHandler());
        assertEquals(null, simpleType4.getTypeHandler());
        assertFalse(simpleType4.isStatic());

        // Test constructor with value and type handlers
        SimpleType simpleType5 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, new MyValueHandler(), new MyTypeHandler(), false);
        assertEquals(String.class, simpleType5.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType5.getBindings());
        assertEquals(null, simpleType5.getSuperClass());
        assertEquals(null, simpleType5.getSuperInterfaces());
        assertEquals(0, simpleType5.getGenericTypes().length);
        assertEquals(new MyValueHandler(), simpleType5.getValueHandler());
        assertEquals(new MyTypeHandler(), simpleType5.getTypeHandler());
        assertFalse(simpleType5.isStatic());
    }

    @Test
    public void testSimpleTypeMethods() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);

        // test isContainerType
        assertFalse(simpleType.isContainerType());

        // test getErasedSignature
        StringBuilder sb = new StringBuilder(50);
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());

        // test getGenericSignature
        sb.setLength(0);
        simpleType.getGenericSignature(sb);
        assertEquals("<java.lang.String>", sb.toString());

        // test withContentType
        try {
            simpleType.withContentType(Arrays.asList(int.class).getType());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test withTypeHandler
        SimpleType simpleTypeNew = simpleType.withTypeHandler(new MyTypeHandler());
        assertNotSame(simpleType, simpleTypeNew);
        assertEquals(simpleType.getRawClass(), simpleTypeNew.getRawClass());
        assertEquals(simpleType.getBindings(), simpleTypeNew.getBindings());
        assertNotSame(simpleType.getTypeHandler(), simpleTypeNew.getTypeHandler());
        assertFalse(simpleTypeNew.isStatic());

        // test withContentTypeHandler
        try {
            simpleType.withContentTypeHandler(new MyContentTypeHandler());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test withValueHandler
        SimpleType simpleTypeNew2 = simpleType.withValueHandler(new MyValueHandler());
        assertNotSame(simpleType, simpleTypeNew2);
        assertEquals(simpleType.getRawClass(), simpleTypeNew2.getRawClass());
        assertEquals(simpleType.getBindings(), simpleTypeNew2.getBindings());
        assertNotSame(simpleType.getValueHandler(), simpleTypeNew2.getValueHandler());
        assertFalse(simpleTypeNew2.isStatic());

        // test withContentValueHandler
        try {
            simpleType.withContentValueHandler(new MyContentValueHandler());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test withStaticTyping
        SimpleType simpleTypeStatic = simpleType.withStaticTyping();
        assertTrue(simpleTypeStatic.isStatic());
    }

    // Add more test cases as needed
}