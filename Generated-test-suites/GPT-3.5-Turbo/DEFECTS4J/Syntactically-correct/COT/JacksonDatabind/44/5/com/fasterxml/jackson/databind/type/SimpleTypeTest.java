package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    // Test constructor
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
    }

    // Test constructUnsafe method
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    // Test construct method
    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType simpleType = SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType simpleType = SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType simpleType = SimpleType.construct(String[].class);
    }

    // Test _narrow method
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType narrowedType = simpleType._narrow(Object.class);
        assertEquals(narrowedType, new SimpleType(Object.class));
    }

    // Test withTypeHandler and withValueHandler methods
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType modifiedType = simpleType.withTypeHandler("handler");
        assertEquals(modifiedType.getTypeHandler(), "handler");

        SimpleType valueTypeModifiedType = simpleType.withValueHandler("valueHandler");
        assertEquals(valueTypeModifiedType.getValueHandler(), "valueHandler");
    }

    // Test withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType modifiedType = simpleType.withStaticTyping();
        assertTrue(modifiedType.isStatic());
    }

    // Test refine method
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(Object.class, null, null, null);
        assertNull(refinedType);
    }

    // Test isContainerType method
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    // Test getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        String erasedSignature = simpleType.getErasedSignature(new StringBuilder()).toString();
        assertEquals(erasedSignature, "Ljava/lang/String;");
    }

    // Test getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        String genericSignature = simpleType.getGenericSignature(new StringBuilder()).toString();
        assertEquals(genericSignature, "Ljava/lang/String;");
    }

    // Test toString method
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        String typeString = simpleType.toString();
        assertEquals(typeString, "[simple type, class java.lang.String]");
    }

    // Test equals method
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }

}