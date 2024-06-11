package com.fasterxml.jackson.databind.type;

import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(String[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.construct(Number.class);
        JavaType narrowedType = simpleType.narrow(Integer.class);
        assertNotNull(narrowedType);
        assertEquals(Integer.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        JavaType withContentType = simpleType.withContentType(Integer.class);
        assertNull(withContentType);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentTypeHandler(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType withValueHandler = simpleType.withValueHandler(Object.class);
        assertNotNull(withValueHandler);
        assertEquals(Object.class, withValueHandler.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentValueHandler(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(String.class);
        JavaType refinedType = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
    
    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = SimpleType.construct(String.class);
        String canonicalName = simpleType.buildCanonicalName();
        assertNotNull(canonicalName);
        assertEquals("java.lang.String", canonicalName);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder signature = simpleType.getErasedSignature(new StringBuilder());
        assertNotNull(signature);
        assertEquals("Ljava/lang/String;", signature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder signature = simpleType.getGenericSignature(new StringBuilder());
        assertNotNull(signature);
        assertEquals("Ljava/lang/String;", signature.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(String.class);
        String toString = simpleType.toString();
        assertNotNull(toString);
        assertEquals("[simple type, class java.lang.String]", toString);
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}