package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
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
        SimpleType.construct(Array.class);
    }

    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.construct(Number.class);
        SimpleType narrowedType = (SimpleType) type.narrow(Integer.class);
        assertEquals(Integer.class, narrowedType.getRawClass());
        assertTrue(type.isTypeOrSubTypeOf(Number.class));
        assertFalse(narrowedType.isTypeOrSubTypeOf(Number.class));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType type = SimpleType.construct(String.class);
        type.withContentType(String.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.construct(String.class);
        type.withTypeHandler(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.construct(String.class);
        type.withContentTypeHandler(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandler() {
        SimpleType type = SimpleType.construct(String.class);
        type.withValueHandler(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.construct(String.class);
        type.withContentValueHandler(null);
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.construct(String.class);
        SimpleType withStaticTypingType = type.withStaticTyping();
        assertEquals(type.getRawClass(), withStaticTypingType.getRawClass());
        assertTrue(withStaticTypingType.hasRawClass(String.class));
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.construct(String.class);
        JavaType refinedType = type.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(null, refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.construct(String.class);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = type.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", erasedSignature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = type.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", genericSignature.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.construct(String.class);
        String toStringValue = type.toString();
        assertEquals("[simple type, class java.lang.String]", toStringValue);
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(Number.class);
        SimpleType type2 = SimpleType.construct(Number.class);
        SimpleType type3 = SimpleType.construct(Integer.class);
        SimpleType type4 = SimpleType.constructUnsafe(Integer.class);
        
        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
        assertFalse(type1.equals(type4));
    }
}