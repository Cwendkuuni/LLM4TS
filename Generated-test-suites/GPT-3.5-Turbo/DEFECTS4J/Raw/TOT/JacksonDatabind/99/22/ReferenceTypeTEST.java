
package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;
    private JavaType referencedType;

    @Before
    public void setUp() {
        referencedType = new SimpleType(String.class);
        referenceType = ReferenceType.construct(String.class, referencedType);
    }

    @Test
    public void testConstructorWithTypes() {
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(referencedType, referenceType.getContentType());
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testConstructorWithRawTypeAndReferencedType() {
        ReferenceType constructedReferenceType = ReferenceType.construct(String.class, referencedType);
        assertEquals(referenceType, constructedReferenceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullReferencedType() {
        ReferenceType.construct(String.class, null);
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = new SimpleType(Integer.class);
        referenceType = referenceType.withContentType(newContentType);
        assertEquals(newContentType, referenceType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object newTypeHandler = new Object();
        referenceType = referenceType.withTypeHandler(newTypeHandler);
        assertEquals(newTypeHandler, referenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object newContentTypeHandler = new Object();
        referenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
        assertEquals(newContentTypeHandler, referenceType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        referenceType = referenceType.withStaticTyping();
        assertTrue(referenceType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        ReferenceType refinedType = (ReferenceType) referenceType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(Object.class, refinedType.getRawClass());
        assertEquals(referenceType.getReferencedType(), refinedType.getReferencedType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = referenceType.getErasedSignature(new StringBuilder());
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = referenceType.getGenericSignature(new StringBuilder());
        assertEquals("java.lang.String<Ljava/lang/Object;>;", sb.toString());
    }

    @Test
    public void testGetAnchorType() {
        assertTrue(referenceType.getAnchorType().isReferenceType());
    }

    @Test
    public void testIsAnchorType() {
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        assertEquals("[reference type, class java.lang.String<java.lang.String>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType equalReferenceType = ReferenceType.construct(String.class, referencedType);
        assertEquals(referenceType, equalReferenceType);
    }
}
