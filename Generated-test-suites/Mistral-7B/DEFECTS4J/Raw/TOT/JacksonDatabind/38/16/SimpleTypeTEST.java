package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        assertEquals(simpleClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new Class[]{}, simpleType.getSuperTypes());
        assertFalse(simpleType.isContainerType());
        assertEquals(simpleType.toString(), "[simple type, class " + simpleClass.getName() + "]");
    }

    @Test
    public void testSimpleTypeConstructionWithBindings() {
        Class<?> listClass = List.class;
        Class<?> elementClass = Integer.class;
        TypeBindings bindings = TypeBindings.builder()
                .addBinding(new JavaTypeReference(List.class), new JavaTypeReference(JavaType.construct(Collections.class)))
                .addBinding(new JavaTypeReference(List.class), new JavaTypeReference(JavaType.construct(List.class, JavaType.construct(elementClass))))
                .build();
        SimpleType simpleType = new SimpleType(listClass, bindings, null, null);
        assertEquals(listClass, simpleType.getRawClass());
        assertEquals(JavaType.construct(Collections.class), simpleType.getSuperClass());
        assertArrayEquals(new JavaType[] { JavaType.construct(elementClass) }, simpleType.getSuperTypes());
        assertTrue(simpleType.isContainerType());
        assertEquals(simpleType.toString(), "[simple type, class java.util.List, superclass class java.util.Collections, super interfaces [], element type class int]");
    }

    @Test
    public void testSimpleTypeUnsafeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(simpleClass);
        assertEquals(simpleClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new Class[]{}, simpleType.getSuperTypes());
        assertFalse(simpleType.isContainerType());
        assertEquals(simpleType.toString(), "[simple type, class " + simpleClass.getName() + "]");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<?> mapClass = Map.class;
        new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidSuperClass() {
        Class<?> stringClass = String.class;
        Class<?> arrayClass = Object[].class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), arrayClass, null);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertEquals(simpleType1.getRawClass(), simpleType2.getRawClass());
        assertEquals(simpleType1.getSuperClass(), simpleType2.getSuperClass());
        assertArrayEquals(simpleType1.getSuperTypes(), simpleType2.getSuperTypes());
        assertEquals(simpleType1.isContainerType(), simpleType2.isContainerType());
        assertEquals(simpleType1.toString(), simpleType2.toString());
    }

    @Test
    public void testSimpleTypeRefinement() {
        Class<?> stringClass = String.class;
        Class<?> subclass = String.class; // same class, but for demonstration purposes
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType simpleType = new SimpleType(stringClass, bindings, null, null);
        JavaType refinedType = simpleType.refine(subclass, bindings, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testSimpleTypeCanonicalName() {
        Class<?> listClass = List.class;
        Class<?> elementClass = Integer.class;
        TypeBindings bindings = TypeBindings.builder()
                .addBinding(new JavaTypeReference(List.class), new JavaTypeReference(JavaType.construct(Collections.class)))
                .addBinding(new JavaTypeReference(List.class), new JavaTypeReference(JavaType.construct(List.class, JavaType.construct(elementClass))))
                .build();
        SimpleType simpleType = new SimpleType(listClass, bindings, null, null);
        assertEquals("[class java.util.List, element type class int]", simpleType.getCanonicalName());
    }
}