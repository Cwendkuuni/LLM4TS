package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        assertNotNull("SimpleType constructUnsafe returns null", simpleType);
        assertEquals("SimpleType class does not match", String.class, simpleType.getRawClass());
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
        SimpleType.constructUnsafe(Object[].class);
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);

        assertNotNull("SimpleType construct returns null", simpleType);
        assertEquals("SimpleType class does not match", String.class, simpleType.getRawClass());
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
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        SimpleType narrowedType = (SimpleType) simpleType.narrow(String.class);

        assertNotNull("SimpleType narrow returns null", narrowedType);
        assertEquals("SimpleType class does not match", String.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType withContentTypeType = (SimpleType) simpleType.withContentType(simpleType);

        assertNull("SimpleType withContentType returns not null", withContentTypeType);
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        Throwable exception = null;
        try {
            simpleType.withContentTypeHandler(null);
        } catch (IllegalArgumentException e) {
            exception = e;
        }

        assertNotNull("Expected exception was not thrown", exception);
        assertEquals("IllegalArgumentException message does not match", "Simple types have no content types; can not call withContenTypeHandler()", exception.getMessage());
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType withValueHandlerType = (SimpleType) simpleType.withValueHandler(simpleType);

        assertNotNull("SimpleType withValueHandler returns null", withValueHandlerType);
        assertEquals("SimpleType class does not match", String.class, withValueHandlerType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType withStaticTypingType = (SimpleType) simpleType.withStaticTyping();

        assertNotNull("SimpleType withStaticTyping returns null", withStaticTypingType);
        assertTrue("SimpleType asStatic does not match", withStaticTypingType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);

        assertNull("SimpleType refine does not return null", refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        assertFalse("SimpleType isContainerType does not match", simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder signature = new StringBuilder();
        simpleType.getErasedSignature(signature);

        assertEquals("SimpleType erased signature does not match", "class: java.lang.String", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder signature = new StringBuilder();
        simpleType.getGenericSignature(signature);

        assertEquals("SimpleType generic signature does not match", "class: java.lang.String;", signature.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        assertEquals("SimpleType toString does not match", "[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType3 = SimpleType.constructUnsafe(Integer.class);

        assertTrue("SimpleType equals does not match", simpleType1.equals(simpleType2));
        assertFalse("SimpleType equals does not match", simpleType1.equals(simpleType3));
    }

}