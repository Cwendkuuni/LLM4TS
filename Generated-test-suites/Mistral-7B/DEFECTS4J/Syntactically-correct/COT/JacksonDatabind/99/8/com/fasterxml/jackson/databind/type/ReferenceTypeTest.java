package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ReferenceTypeTest {

    // Basic test cases for each method

    @Test
    public void testConstruct() {
        JavaType refType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class));
        assertInstanceOf(ReferenceType.class, refType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(String.class, TypeBindings.emptyBindings());
        JavaType refdType = new JavaType(Integer.class);
        JavaType refType = ReferenceType.upgradeFrom(baseType, refdType);
        assertInstanceOf(ReferenceType.class, refType);
        assertEquals(refdType, refType.getReferencedType());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        assertEquals(new JavaType(Integer.class), refType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        assertEquals(new JavaType(Integer.class), refType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        assertTrue(refType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        assertTrue(refType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("Ljava/lang/String;");
        assertEquals(expected.toString(), refType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("Lcom/fasterxml/jackson/databind/type/ReferenceType;<Ljava/lang/String;Ljava/lang/Integer;>");
        assertEquals(expected.toString(), refType.getGenericSignature().toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        assertNull(refType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        assertFalse(refType.isAnchorType());
    }

    @Test
    public void testToString() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        String expected = "[reference type, class [Ljava/lang/String;<Ljava/lang/Integer;>]";
        assertEquals(expected, refType.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        JavaType refType2 = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        assertTrue(refType1.equals(refType2));
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefdType() {
        JavaType baseType = new SimpleType(String.class, TypeBindings.emptyBindings());
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNonReferenceType() {
        JavaType baseType = new SimpleType(String.class, TypeBindings.emptyBindings());
        JavaType refdType = new JavaType(Long.class);
        ReferenceType.upgradeFrom(baseType, refdType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullRefType() {
        JavaType refType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullAnchorType() {
        JavaType refType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeNullContentType() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        JavaType newContentType = null;
        JavaType result = refType.withContentType(newContentType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandlerNullTypeHandler() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        Object newTypeHandler = null;
        JavaType result = refType.withTypeHandler(newTypeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerNullContentTypeHandler() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        Object newContentTypeHandler = null;
        JavaType result = refType.withContentTypeHandler(newContentTypeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerNullValueHandler() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        Object newValueHandler = null;
        JavaType result = refType.withValueHandler(newValueHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerNullContentValueHandler() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        Object newContentValueHandler = null;
        JavaType result = refType.withContentValueHandler(newContentValueHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithStaticTypingNull() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        JavaType result = refType.withStaticTyping();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineNullRawType() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        JavaType result = refType.refine(null, TypeBindings.emptyBindings(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineNullSuperInterfaces() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        JavaType[] newSuperInterfaces = null;
        JavaType result = refType.refine(null, TypeBindings.emptyBindings(), null, newSuperInterfaces);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineNullSuperClass() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        JavaType newSuperClass = null;
        JavaType result = refType.refine(newSuperClass, TypeBindings.emptyBindings(), null, null);
    }

    @Test
    public void testRefineSameClass() {
        JavaType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        JavaType newRefType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, new JavaType(Integer.class), null, null, null, false);
        JavaType result = refType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(newRefType, result);
    }

    @Test
    public void testRefineSubclass() {
        JavaType baseType = new SimpleType(String.class, TypeBindings.emptyBindings());
        JavaType refType = ReferenceType.upgradeFrom(baseType, new JavaType(Integer.class));
        JavaType subclassType = new SimpleType(StringSubclass.class, TypeBindings.emptyBindings());
        JavaType result = refType.refine(subclassType.getType(), TypeBindings.emptyBindings(), null, null);
        assertTrue(result instanceof ReferenceType);
        assertTrue(result.getClass().isAssignableFrom(StringSubclass.class));
    }

    private static class StringSubclass extends String {
        // empty class
    }
}