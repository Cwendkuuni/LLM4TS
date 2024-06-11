package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isArrayType());
        assertFalse(simpleType.isMapLikeType());
        assertFalse(simpleType.isCollectionLikeType());
        assertTrue(simpleType.isSimpleType());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(java.util.Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(java.util.List.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Integer[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        SimpleType narrowedType = (SimpleType) simpleType.narrow(Integer.class);
        assertEquals(Integer.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertNull(simpleType.withContentType(SimpleType.construct(Integer.class)));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertNull(simpleType.withContentTypeHandler(new Object()));
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertNull(simpleType.withValueHandler(new Object()));
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertNull(simpleType.withContentValueHandler(new Object()));
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.withContentTypeHandler(new Object()).isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertNull(simpleType.refine(Integer.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals("Ljava/lang/Object;", simpleType.getErasedSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals("Ljava/lang/Object;", simpleType.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(Integer.class);
        SimpleType simpleType2 = SimpleType.construct(Integer.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}