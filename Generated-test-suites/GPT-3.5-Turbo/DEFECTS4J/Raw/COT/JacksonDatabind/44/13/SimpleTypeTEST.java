
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertTrue(simpleType.isTypeOrSubTypeOf(String.class));
        assertFalse(simpleType.isContainerType());
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        assertEquals(ArrayList.class, simpleType.getRawClass());
        assertTrue(simpleType.isTypeOrSubTypeOf(ArrayList.class));
        assertFalse(simpleType.isContainerType());
        assertEquals("[simple type, class java.util.ArrayList]", simpleType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType.construct(ArrayList[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType base = SimpleType.constructUnsafe(ArrayList.class);
        SimpleType subType = (SimpleType) base.narrow(ArrayList.class);
        assertEquals(ArrayList.class, subType.getRawClass());
        assertTrue(subType.isTypeOrSubTypeOf(ArrayList.class));
        assertFalse(subType.isContainerType());
        assertEquals("[simple type, class java.util.ArrayList]", subType.toString());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(ArrayList.class);
        try {
            simpleType.withContentType(Integer.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(ArrayList.class);
        Object dummyHandler = new Object();
        SimpleType newType = simpleType.withTypeHandler(dummyHandler);
        assertEquals(dummyHandler, newType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(ArrayList.class);
        Object dummyHandler = new Object();
        simpleType.withContentTypeHandler(dummyHandler);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(ArrayList.class);
        Object dummyHandler = new Object();
        SimpleType newType = simpleType.withValueHandler(dummyHandler);
        assertEquals(dummyHandler, newType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(ArrayList.class);
        Object dummyHandler = new Object();
        simpleType.withContentValueHandler(dummyHandler);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(ArrayList.class);
        SimpleType newType = simpleType.withStaticTyping();
        assertTrue(newType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(ArrayList.class);
        JavaType refinedType = simpleType.refine(ArrayList.class, null, null, null);
        assertEquals(null, refinedType);
    }
}
