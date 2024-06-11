package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        // Test with valid class
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getContentType());
        assertEquals("java.lang.String", type.toCanonical());
        
        // Test with invalid class
        try {
            SimpleType.constructUnsafe(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
    }
    
    @Test
    public void testConstruct() {
        // Test with valid class
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getContentType());
        assertEquals("java.lang.String", type.toCanonical());
        
        // Test with invalid class
        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        // Test with array class
        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }
    
    @Test
    public void testNarrow() {
        SimpleType baseType = SimpleType.constructUnsafe(Object.class);
        
        // Test with same class
        SimpleType narrowedType = (SimpleType) baseType._narrow(Object.class);
        assertSame(narrowedType, baseType);
        
        // Test with subclass
        narrowedType = (SimpleType) baseType._narrow(String.class);
        assertEquals(String.class, narrowedType.getRawClass());
        assertNull(narrowedType.getContentType());
        
        // Test with invalid class
        try {
            baseType._narrow(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Class java.util.Map not sub-type of java.lang.Object", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        
        // Test with null content type
        try {
            type.withContentType(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        // Test with same type handler
        SimpleType newType = type.withTypeHandler(type.getTypeHandler());
        assertSame(newType, type);
        
        // Test with different type handler
        Object typeHandler = new Object();
        newType = type.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        
        // Test with null content type handler
        try {
            type.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        // Test with same value handler
        SimpleType newType = type.withValueHandler(type.getValueHandler());
        assertSame(newType, type);
        
        // Test with different value handler
        Object valueHandler = new Object();
        newType = type.withValueHandler(valueHandler);
        assertEquals(valueHandler, newType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        
        // Test with null content value handler
        try {
            type.withContentValueHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        // Test with static typing
        SimpleType newType = type.withStaticTyping();
        assertTrue(newType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        // Test with valid raw type
        JavaType newType = type.refine(Object.class, null, null, null);
        assertNull(newType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        StringBuilder sb = type.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/util/List;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        StringBuilder sb = type.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/util/List<*>;", sb.toString());
    }
    
    @Test
    public void testBuildCanonicalName() {
        SimpleType type = SimpleType.constructUnsafe(Map.class);
        String canonicalName = type.buildCanonicalName();
        assertEquals("java.util.Map<?, ?>", canonicalName);
    }
    
    @Test
    public void testEquals() {
        // Test with different object types
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        assertFalse(type1.equals(new Object()));
        
        // Test with same object
        assertTrue(type1.equals(type1));
        
        // Test with different simple types
        SimpleType type2 = SimpleType.constructUnsafe(Object.class);
        assertFalse(type1.equals(type2));
        
        // Test with same raw class but different bindings
        SimpleType type3 = SimpleType.constructUnsafe(Object.class);
        assertFalse(type2.equals(type3));
        
        // Test with same raw class and bindings
        SimpleType type4 = SimpleType.constructUnsafe(Object.class);
        assertTrue(type3.equals(type4));
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        String str = type.toString();
        assertEquals("[simple type, class java.lang.String]", str);
    }
}