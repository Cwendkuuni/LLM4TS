package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Collection;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(Map.class);
        assertFalse(type.isContainerType());
        assertEquals(Map.class.getName(), type.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForArray() {
        SimpleType.construct(Object[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType type = new SimpleType(Object.class);
        SimpleType narrowedType = (SimpleType) type._narrow(String.class);
        assertEquals(String.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = new SimpleType(Object.class);
        assertNull(type.withContentType(String.class));
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(Object.class);
        SimpleType staticType = type.withStaticTyping();
        assertTrue(staticType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = new SimpleType(Object.class);
        assertNull(type.refine(String.class, null, null, null));
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = new SimpleType(Object.class);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        sb = type.getErasedSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        sb = type.getGenericSignature(sb);
        assertEquals("Ljava/lang/Object;;", sb.toString());
    }
    
    @Test
    public void testBuildCanonicalName() {
        SimpleType type = new SimpleType(Object.class);
        assertEquals(Object.class.getName(), type.buildCanonicalName());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(Object.class);
        SimpleType type2 = new SimpleType(Object.class);
        assertTrue(type1.equals(type2));
    }
}