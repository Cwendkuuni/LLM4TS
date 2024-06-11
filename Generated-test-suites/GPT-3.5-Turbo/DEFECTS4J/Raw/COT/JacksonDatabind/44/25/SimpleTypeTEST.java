
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.isContainerType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidArray() {
        SimpleType.construct(Integer[].class);
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(List.class).withContentType(SimpleType.constructUnsafe(String.class));
        assertEquals(List.class, type.getRawClass());
        assertEquals(SimpleType.constructUnsafe(String.class), type.getContentType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeException() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        type.withContentType(SimpleType.constructUnsafe(String.class));
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class).withContentValueHandler(new ValueHandler());
        assertEquals(List.class, type.getRawClass());
        assertEquals(new ValueHandler(), type.getContentValueHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerException() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        type.withContentValueHandler(new ValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(String.class).withStaticTyping();
        assertEquals(String.class, type.getRawClass());
        assertTrue(type.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(List.class).refine(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(ArrayList.class, type.getRawClass());
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("Ljava/util/List;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava/util/List<>;", sb.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        assertTrue(type1.equals(type2));
    }
    
    private static class ValueHandler {}
    
}
