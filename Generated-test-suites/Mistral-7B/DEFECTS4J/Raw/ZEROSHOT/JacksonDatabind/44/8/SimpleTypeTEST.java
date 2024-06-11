package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        JavaType constructedType = simpleType.getRawType();
        assertEquals(simpleClass, constructedType.getRawClass());
    }

    @Test
    public void testSimpleTypeWithTypeBindings() {
        Class<?> listClass = ArrayList.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
        SimpleType simpleType = new SimpleType(javaType);
        JavaType constructedType = simpleType.getRawType();
        assertEquals(listClass, constructedType.getRawClass());

        // Test with bindings
        TypeBindings typeBindings = new TypeBindings();
        SimpleType simpleTypeWithBindings = new SimpleType(simpleClass, typeBindings);
        JavaType constructedTypeWithBindings = simpleTypeWithBindings.getRawType();
        assertEquals(simpleClass, constructedTypeWithBindings.getRawClass());
        assertEquals(typeBindings, simpleTypeWithBindings.getTypeBindings());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        SimpleType copiedType = new SimpleType(simpleType);
        assertEquals(simpleType.getRawType().getRawClass(), copiedType.getRawType().getRawClass());
    }

    @Test
    public void testUnsafeSimpleTypeConstruction() {
        Class<?> unsafeSimpleClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(unsafeSimpleClass);
        JavaType constructedType = simpleType.getRawType();
        assertEquals(unsafeSimpleClass, constructedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalSimpleTypeConstruction() {
        Class<?> arrayClass = int[].class;
        SimpleType simpleType = SimpleType.construct(arrayClass);
        fail("Should not be able to construct SimpleType for an array");
    }

    @Test
    public void testNarrowMethod() {
        Class<?> subclass = Integer.class;
        SimpleType simpleType = new SimpleType(String.class);
        JavaType narrowedType = simpleType.narrow(subclass);
        assertTrue(narrowedType instanceof SimpleType);
        assertEquals(subclass, narrowedType.getRawClass());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder signature = new StringBuilder();
        simpleType.getErasedSignature(signature);
        assertEquals("java.lang.String", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> listClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass);
        StringBuilder signature = new StringBuilder();
        simpleType.getGenericSignature(signature);
        assertTrue(signature.toString().contains("List<E>"));
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> genericClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(genericClass);
        assertEquals("java.util.ArrayList<E>", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testRefineMethod() {
        Class<?> rawType = ArrayList.class;
        SimpleType simpleType = new SimpleType(rawType);
        JavaType refinedType = simpleType.refine(LinkedList.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
}