
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType.constructUnsafe(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType.constructUnsafe(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType.constructUnsafe(int[].class);
    }
    
    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(int[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType base = SimpleType.construct(Object.class);
        SimpleType type = base._narrow(String.class);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(Collection.class);
        try {
            type.withContentType(String.class);
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType newType = type.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(Collection.class);
        try {
            type.withContentTypeHandler(new Object());
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType newType = type.withValueHandler(handler);
        assertEquals(handler, newType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(Collection.class);
        try {
            type.withContentValueHandler(new Object());
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        SimpleType newType = type.withStaticTyping();
        assertTrue(newType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = base.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
    
    @Test
    public void testBuildCanonicalName() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        String canonicalName = type.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = type.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/lang/String;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(Collection.class);
        StringBuilder sb = type.getGenericSignature(new StringBuilder());
        assertEquals("L"+Collection.class.getName()+";", sb.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        assertTrue(type1.equals(type2));
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }
    
}
