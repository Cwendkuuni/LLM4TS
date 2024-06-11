package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = new ReferenceType(String.class, JavaType.empty(), null, null, false);
    }

    @Test
    public void testConstruct() {
        ReferenceType constructed = ReferenceType.construct(Integer.class, JavaType.empty(), null, null);
        assertNotNull(constructed);
        assertEquals(Integer.class, constructed.getRawClass());
        assertEquals(JavaType.empty(), constructed.getReferencedType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        ReferenceType withContentTypeHandler = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(referenceType, withContentTypeHandler);
        assertEquals(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        ReferenceType withValueHandler = referenceType.withValueHandler(valueHandler);
        assertNotSame(referenceType, withValueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        ReferenceType withContentValueHandler = referenceType.withContentValueHandler(contentValueHandler);
        assertNotSame(referenceType, withContentValueHandler);
        assertEquals(contentValueHandler, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType withStaticTyping = referenceType.withStaticTyping();
        assertNotSame(referenceType, withStaticTyping);
        assertTrue(withStaticTyping.getStaticTyping());
    }

    @Test
    public void testGetReferencedType() {
        assertEquals(JavaType.empty(), referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        assertEquals(JavaType.empty(), referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("class java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("class java.lang.String<;>", sb.toString());
    }

    @Test
    public void testToString() {
        String expected = "[reference type, class java.lang.String<empty>]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType equalType = new ReferenceType(String.class, JavaType.empty(), null, null, false);
        assertTrue(referenceType.equals(equalType));

        ReferenceType unequalType = new ReferenceType(Integer.class, JavaType.empty(), null, null, false);
        assertFalse(referenceType.equals(unequalType));
    }
}