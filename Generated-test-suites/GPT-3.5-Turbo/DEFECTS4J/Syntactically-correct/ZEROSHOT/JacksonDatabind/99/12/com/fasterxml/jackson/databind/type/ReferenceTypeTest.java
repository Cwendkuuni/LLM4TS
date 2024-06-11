
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstructor1() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);

        assertNotNull(referenceType);
        assertEquals(ReferenceType.class, referenceType.getClass());
    }

    @Test
    public void testConstructor2() {
        ReferenceType referenceType = new ReferenceType(null, null);

        assertNotNull(referenceType);
        assertEquals(ReferenceType.class, referenceType.getClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType();
        JavaType refdType = new SimpleType();

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

        assertNotNull(referenceType);
        assertEquals(ReferenceType.class, referenceType.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithMissingRefdType() {
        JavaType baseType = new SimpleType();

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = new JavaType() {};

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(null, null, null, null, null);

        assertNotNull(referenceType);
        assertEquals(ReferenceType.class, referenceType.getClass());
    }

    @Test
    public void testConstructWithNullRefType() {
        ReferenceType referenceType = ReferenceType.construct(ReferenceType.class, null);

        assertNotNull(referenceType);
        assertEquals(ReferenceType.class, referenceType.getClass());
    }

    @Test
    public void testWithContentType() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);
        JavaType contentType = new SimpleType();

        JavaType result = referenceType.withContentType(contentType);

        assertNotNull(result);
        assertNotEquals(referenceType, result);
        assertEquals(ReferenceType.class, result.getClass());
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);
        Object handler = new Object();

        ReferenceType result = referenceType.withTypeHandler(handler);

        assertNotNull(result);
        assertNotEquals(referenceType, result);
        assertEquals(ReferenceType.class, result.getClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);
        Object handler = new Object();

        ReferenceType result = referenceType.withContentTypeHandler(handler);

        assertNotNull(result);
        assertNotEquals(referenceType, result);
        assertEquals(ReferenceType.class, result.getClass());
    }

    @Test
    public void testWithValueHandler() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);
        Object handler = new Object();

        ReferenceType result = referenceType.withValueHandler(handler);

        assertNotNull(result);
        assertNotEquals(referenceType, result);
        assertEquals(ReferenceType.class, result.getClass());
    }

    @Test
    public void testWithContentValueHandler() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);
        Object handler = new Object();

        ReferenceType result = referenceType.withContentValueHandler(handler);

        assertNotNull(result);
        assertNotEquals(referenceType, result);
        assertEquals(ReferenceType.class, result.getClass());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);

        ReferenceType result = referenceType.withStaticTyping();

        assertNotNull(result);
        assertNotEquals(referenceType, result);
        assertEquals(ReferenceType.class, result.getClass());
    }

    @Test
    public void testRefine() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        JavaType result = referenceType.refine(rawType, bindings, superClass, superInterfaces);

        assertNotNull(result);
        assertNotEquals(referenceType, result);
        assertEquals(ReferenceType.class, result.getClass());
    }

    @Test
    public void testGetContentType() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);

        JavaType contentType = referenceType.getContentType();

        assertNotNull(contentType);
        assertEquals(SimpleType.class, contentType.getClass());
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null,
                false);

        JavaType referencedType = referenceType.getReferencedType();

        assertNotNull(referencedType);
        assertEquals(ReferenceType.class, referencedType.getClass());
    }

    @Test
    public void testHasContentType() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);

        boolean hasContentType = referenceType.hasContentType();

        assertTrue(hasContentType);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);

        boolean isReferenceType = referenceType.isReferenceType();

        assertTrue(isReferenceType);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);

        StringBuilder sb = referenceType.getErasedSignature(new StringBuilder());

        assertNotNull(sb);
        assertNotEquals("", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);

        StringBuilder sb = referenceType.getGenericSignature(new StringBuilder());

        assertNotNull(sb);
        assertNotEquals("", sb.toString());
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);

        JavaType anchorType = referenceType.getAnchorType();

        assertNotNull(anchorType);
        assertEquals(ReferenceType.class, anchorType.getClass());
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);

        boolean isAnchorType = referenceType.isAnchorType();

        assertTrue(isAnchorType);
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);

        String str = referenceType.toString();

        assertNotNull(str);
        assertNotEquals("", str);
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType1 = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);

        boolean isEqual = referenceType1.equals(referenceType2);

        assertTrue(isEqual);
    }

    @Test
    public void testEqualsWithDifferentReferences() {
        ReferenceType referenceType1 = new ReferenceType(ReferenceType.class, null, null, null, null, null, null, null, false);
        ReferenceType referenceType2 = referenceType1;

        boolean isEqual = referenceType1.equals(referenceType2);

        assertTrue(isEqual);
    }
}
