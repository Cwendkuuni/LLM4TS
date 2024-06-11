package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.junit.Test;

/**
 * Test class for {@link ReferenceType}.
 */
public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    // Test methods that return the same instance when no change is made

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new JavaType(String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertEquals(referenceType, newReferenceType);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new JavaType(String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
        assertEquals(referenceType, newReferenceType);
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new JavaType(String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertEquals(referenceType, newReferenceType);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new JavaType(String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
        assertEquals(referenceType, newReferenceType);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertEquals(referenceType, newReferenceType);
    }

    // Methods that return new instances

    @Test
    public void testNarrow() {
        JavaType refType1 = new JavaType(String.class);
        JavaType refType2 = new JavaType(Integer.class);
        ReferenceType referenceType = new ReferenceType(Long.class, refType1, null, null, false);
        ReferenceType narrowed = referenceType.narrow(Integer.class);
        assertNotSame(referenceType, narrowed);
        assertEquals(Integer.class, narrowed.getRawClass());
        assertEquals(refType2, narrowed.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Long.class, refType, null, null, false);
        referenceType.narrow(Double.class);
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        assertTrue(canonicalName.contains("Integer<"));
        assertTrue(canonicalName.contains("String>"));
    }
}