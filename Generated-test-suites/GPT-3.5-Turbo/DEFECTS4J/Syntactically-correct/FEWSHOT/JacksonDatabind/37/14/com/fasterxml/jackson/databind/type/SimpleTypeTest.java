package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertFalse(type.isReferenceType());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        
        assertEquals(Integer.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertFalse(type.isReferenceType());
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
    public void testNarrow() {
        SimpleType base = SimpleType.construct(Number.class);
        SimpleType narrowed = (SimpleType) base.narrow(Integer.class);

        assertEquals(base.getBindings(), narrowed.getBindings());
        assertEquals(Integer.class, narrowed.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.construct(List.class);
        SimpleType withContentType = (SimpleType) type.withContentType(Integer.class);

        assertEquals(type.getBindings(), withContentType.getBindings());
        assertEquals(Integer.class, withContentType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeThrowsException() {
        SimpleType type = SimpleType.construct(String.class);
        type.withContentType(Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerThrowsException() {
        SimpleType type = SimpleType.construct(Integer.class);
        type.withContentTypeHandler(null);
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.construct(List.class);
        SimpleType withContentValueHandler = (SimpleType) type.withContentValueHandler(null);

        assertEquals(type.getBindings(), withContentValueHandler.getBindings());
        assertEquals(null, withContentValueHandler.getContentValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerThrowsException() {
        SimpleType type = SimpleType.construct(Integer.class);
        type.withContentValueHandler(null);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.construct(List.class);
        SimpleType withValueHandler = (SimpleType) type.withValueHandler(null);

        assertEquals(type.getBindings(), withValueHandler.getBindings());
        assertEquals(null, withValueHandler.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerThrowsException() {
        SimpleType type = SimpleType.construct(Integer.class);
        type.withValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.construct(List.class);
        SimpleType withStaticTyping = (SimpleType) type.withStaticTyping();

        assertEquals(type.getBindings(), withStaticTyping.getBindings());
        assertEquals(true, withStaticTyping.isJavaLangObject());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.construct(List.class);
        SimpleType refined = (SimpleType) type.refine(ArrayList.class, null, null, null);

        assertEquals(null, refined);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.construct(List.class);
        StringBuilder sb = type.getErasedSignature(new StringBuilder());

        assertEquals("Ljava/util/List;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.construct(List.class);
        StringBuilder sb = type.getGenericSignature(new StringBuilder());

        assertEquals("Ljava/util/List<*>;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.construct(List.class);

        assertEquals("[simple type, class java.util.List]", type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(List.class);
        SimpleType type2 = SimpleType.construct(List.class);
        SimpleType type3 = SimpleType.construct(ArrayList.class);

        assertEquals(true, type1.equals(type2));
        assertEquals(false, type1.equals(type3));
    }
}