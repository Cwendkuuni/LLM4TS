package com.fasterxml.jackson.databind.type; // make sure the package is correct

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    // Test constructor with a valid Class
    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test construct method (deprecated since 2.7)
    @Test(expected = IllegalArgumentException.class)
    public void testConstructDeprecatedMethod() {
        Class<String> stringClass = String.class;
        SimpleType.construct(stringClass);
    }

    // Test constructUnsafe method (not for application code)
    @Test
    public void testConstructUnsafeMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test copy constructor
    @Test
    public void testCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType original = new SimpleType(stringClass);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
    }

    // Test getErasedSignature method
    @Test
    public void testGetErasedSignatureMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    // Test getGenericSignature method
    @Test
    public void testGetGenericSignatureMethod() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        SimpleType simpleType = new SimpleType(listStringClass);
        StringBuilder expected = new StringBuilder("java.util.List<java.lang.String>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    // Test equals method
    @Test
    public void testEqualsMethod() {
        Class<String> stringClass1 = String.class;
        Class<String> stringClass2 = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass1);
        SimpleType simpleType2 = new SimpleType(stringClass2);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertNotNull(simpleType1.equals(null));
        assertNotNull(simpleType1.equals(simpleType3));
    }

    // Test isContainerType method
    @Test
    public void testIsContainerTypeMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse(simpleType.isContainerType());
    }

    
}