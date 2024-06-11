package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test constructing SimpleType using the constructor that takes a Class
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals(Integer.class, intType.getRawClass());
        assertNull(intType.getSuperClass());
        assertNull(intType.getSuperInterfaces());

        // Test constructing SimpleType using the constructor that takes a Class, TypeBindings, JavaType superClass, and JavaType[] superInterfaces
        JavaType intTypeJava = TypeFactory.defaultInstance().constructType(intClass);
        SimpleType intTypeWithBindings = new SimpleType(intClass, TypeBindings.emptyBindings(), intTypeJava, null);
        assertEquals(Integer.class, intTypeWithBindings.getRawClass());
        assertEquals(intTypeJava, intTypeWithBindings.getSuperClass());
        assertArrayEquals(new Class[0], intTypeWithBindings.getSuperInterfaces());

        // Test constructing SimpleType using the copy constructor
        SimpleType intTypeCopy = new SimpleType(intTypeWithBindings);
        assertEquals(intTypeWithBindings, intTypeCopy);

        // Test constructing SimpleType using the pass-through constructor
        SimpleType intTypePassThrough = new SimpleType(intTypeJava);
        assertEquals(intTypeWithBindings, intTypePassThrough);

        // Test constructing SimpleType using the unsafe constructor
        SimpleType intTypeUnsafe = SimpleType.constructUnsafe(intClass);
        assertEquals(intTypeWithBindings, intTypeUnsafe);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingMapType() {
        // Test IllegalArgumentException when constructing SimpleType for a Map type
        SimpleType mapType = new SimpleType(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingCollectionType() {
        // Test IllegalArgumentException when constructing SimpleType for a Collection type
        SimpleType listType = new SimpleType(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingArrayType() {
        // Test IllegalArgumentException when constructing SimpleType for an array type
        SimpleType intArrayType = new SimpleType(int[].class);
    }

    @Test
    public void testNarrow() {
        // Test _narrow method with the same class
        SimpleType intType = new SimpleType(Integer.class);
        SimpleType intTypeNarrowed = intType.narrow(Integer.class);
        assertEquals(intType, intTypeNarrowed);

        // Test _narrow method with a subclass
        SimpleType stringType = new SimpleType(String.class);
        SimpleType intTypeNarrowedToInt = stringType.narrow(Integer.class);
        assertNotNull(intTypeNarrowedToInt);
        assertEquals(Integer.class, intTypeNarrowedToInt.getRawClass());

        // Test _narrow method with a superclass
        SimpleType numberType = new SimpleType(Number.class);
        SimpleType intTypeNarrowedFromNumber = numberType.narrow(Integer.class);
        assertNotNull(intTypeNarrowedFromNumber);
        assertEquals(Integer.class, intTypeNarrowedFromNumber.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenCallingNarrowWithDifferentClass() {
        // Test IllegalArgumentException when calling _narrow with a different class
        SimpleType intType = new SimpleType(Integer.class);
        intType.narrow(String.class);
    }
}