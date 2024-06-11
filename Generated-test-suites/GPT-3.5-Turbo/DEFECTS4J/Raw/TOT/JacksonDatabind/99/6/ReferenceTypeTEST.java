package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        JavaType refType = new SimpleType();
        referenceType = ReferenceType.construct(ReferenceType.class, refType);
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = new SimpleType();
        ReferenceType newType = referenceType.withContentType(newContentType);
        assertEquals(newContentType, newType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object newTypeHandler = new Object();
        ReferenceType newType = referenceType.withTypeHandler(newTypeHandler);
        assertEquals(newTypeHandler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object newContentTypeHandler = new Object();
        ReferenceType newType = referenceType.withContentTypeHandler(newContentTypeHandler);
        assertEquals(newContentTypeHandler, newType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType newType = referenceType.withStaticTyping();
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType();
        JavaType[] superInterfaces = new JavaType[0];
        ReferenceType newType = referenceType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, newType.getRawClass());
        assertEquals(bindings, newType.getTypeBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(superInterfaces, newType.getInterfaces());
    }

    @Test
    public void testGetContentType() {
        JavaType contentType = referenceType.getContentType();
        assertTrue(contentType instanceof SimpleType);
    }

    @Test
    public void testGetReferencedType() {
        JavaType referencedType = referenceType.getReferencedType();
        assertTrue(referencedType instanceof SimpleType);
    }

    @Test
    public void testHasContentType() {
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType anchorType = referenceType.getAnchorType();
        assertEquals(referenceType, anchorType);
    }

    @Test
    public void testIsAnchorType() {
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        String expected = "[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<SimpleType>]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType = new SimpleType();
        ReferenceType sameType = ReferenceType.construct(ReferenceType.class, refType);
        assertTrue(referenceType.equals(sameType));
        assertFalse(referenceType.equals(refType));
        assertFalse(referenceType.equals(null));
    }
}