package com.fasterxml.jackson.databind.type;

import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        assertNotNull(type);
        assertEquals(Object.class, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        SimpleType narrowedType = (SimpleType) type._narrow(String.class);
        assertNotNull(narrowedType);
        assertEquals(String.class, narrowedType.getRawClass());
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
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.construct(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType type = SimpleType.construct(String.class);
        type.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType type = SimpleType.construct(String.class);
        type.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.construct(String.class);
        String str = type.toString();
        assertEquals("[simple type, class java.lang.String]", str);
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(String.class);
        SimpleType type2 = SimpleType.construct(String.class);
        assertTrue(type1.equals(type2));
    }
}