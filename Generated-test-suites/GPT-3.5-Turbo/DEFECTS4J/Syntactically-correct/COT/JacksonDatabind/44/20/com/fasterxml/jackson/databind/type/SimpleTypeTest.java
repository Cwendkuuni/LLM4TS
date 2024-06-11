
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, type.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(String.class, type.getRawClass());
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
        SimpleType.construct(String[].class);
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        assertThrows(IllegalArgumentException.class, () -> type.withContentType(SimpleType.construct(String.class)));
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        Object handler = new Object();
        SimpleType modifiedType = type.withTypeHandler(handler);
        assertSame(handler, modifiedType.getTypeHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        Object handler = new Object();
        type.withContentTypeHandler(handler);
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        Object handler = new Object();
        SimpleType modifiedType = type.withValueHandler(handler);
        assertSame(handler, modifiedType.getValueHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        Object handler = new Object();
        type.withContentValueHandler(handler);
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        SimpleType modifiedType = type.withStaticTyping();
        assertTrue(modifiedType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(Set.class);
        assertNull(type.refine(HashSet.class, null, null, null));
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(Set.class);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("Ljava/lang/Integer;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava/lang/Integer;;", sb.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(Long.class);
        assertEquals("[simple type, class java.lang.Long]", type.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(Short.class);
        SimpleType type2 = SimpleType.constructUnsafe(Short.class);
        assertTrue(type1.equals(type2));
        assertTrue(type2.equals(type1));
    }

}
