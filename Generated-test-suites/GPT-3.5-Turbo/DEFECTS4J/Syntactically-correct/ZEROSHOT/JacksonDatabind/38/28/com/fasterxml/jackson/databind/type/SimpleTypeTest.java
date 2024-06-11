package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType st = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, st.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        SimpleType st = SimpleType.construct(Object.class);
        assertEquals(Object.class, st.getRawClass());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstruct_Map() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstruct_Collection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstruct_Array() {
        SimpleType.construct(String[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType st = SimpleType.construct(Object.class);
        SimpleType narrower = st.narrow(String.class);
        assertEquals(String.class, narrower.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType st = SimpleType.construct(Object.class);
        try {
            st.withContentType(SimpleType.construct(String.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType st = SimpleType.construct(Object.class);
        Object handler = new Object();
        SimpleType withHandler = st.withTypeHandler(handler);
        assertEquals(handler, withHandler.getTypeHandler());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType st = SimpleType.construct(Object.class);
        st.withContentTypeHandler(new Object());
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType st = SimpleType.construct(Object.class);
        Object handler = new Object();
        SimpleType withHandler = st.withValueHandler(handler);
        assertEquals(handler, withHandler.getValueHandler());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType st = SimpleType.construct(Object.class);
        st.withContentValueHandler(new Object());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType st = SimpleType.construct(Object.class);
        SimpleType withStaticTyping = st.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType st = SimpleType.construct(Object.class);
        assertNull(st.refine(String.class, TypeBindings.emptyBindings(), null, null));
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType st = SimpleType.construct(Object.class);
        assertFalse(st.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType st = SimpleType.construct(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = st.getErasedSignature(sb);
        assertTrue(result instanceof StringBuilder);
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType st = SimpleType.construct(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = st.getGenericSignature(sb);
        assertTrue(result instanceof StringBuilder);
    }
    
}