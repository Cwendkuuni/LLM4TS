package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeModifier;

public class ReferenceTypeTest {

    // Basic test cases for constructors

    @Test
    public void testReferenceTypeConstruction() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, intType);
        assertNotNull(referenceType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(intType, referenceType.getContentType());
        assertFalse(referenceType.isReferenceType());
        assertTrue(referenceType.isContainerType());
    }

    @Test
    public void testReferenceTypeUpgradeFrom() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = ReferenceType.upgradeFrom(intType, null);
        assertNotNull(referenceType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(intType, referenceType.getContentType());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeUpgradeFromNullRefType() {
        JavaType intType = TypeReference.intType();
        ReferenceType.upgradeFrom(intType, null);
    }

    // Test methods from ReferenceType class

    @Test
    public void testGetContentType() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        assertEquals(intType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        assertEquals(intType, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        StringBuilder expected = new StringBuilder("[I");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        StringBuilder expected = new StringBuilder("[Ljava/lang/Integer;>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        assertFalse(referenceType.isAnchorType());
    }

    // Test factory methods

    @Test
    public void testConstruct() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, intType);
        assertNotNull(referenceType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(intType, referenceType.getContentType());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.isContainerType());
    }

    @Test
    public void testUpgradeFromNullBaseType() {
        JavaType refType = TypeReference.intType();
        assertThrows(IllegalArgumentException.class, () -> ReferenceType.upgradeFrom(null, refType));
    }

    @Test
    public void testUpgradeFromNonReferenceType() {
        JavaType intType = TypeReference.intType();
        JavaType nonReferenceType = TypeReference.STRING_TYPE;
        assertThrows(IllegalArgumentException.class, () -> ReferenceType.upgradeFrom(intType, nonReferenceType));
    }

    @Test
    public void testWithContentType() {
        JavaType intType = TypeReference.intType();
        JavaType refType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        JavaType newContentType = TypeReference.longType();
        JavaType newReferenceType = refType.withContentType(newContentType);
        assertNotNull(newReferenceType);
        assertEquals(newContentType, newReferenceType.getContentType());
        assertEquals(intType, refType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType intType = TypeReference.intType();
        JavaType refType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        Object typeHandler = new Object();
        JavaType newReferenceType = refType.withTypeHandler(typeHandler);
        assertNotNull(newReferenceType);
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
        assertNull(refType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType intType = TypeReference.intType();
        JavaType refType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        Object contentTypeHandler = new Object();
        JavaType newReferenceType = refType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(newReferenceType);
        assertEquals(contentTypeHandler, newReferenceType.<Object>getContentTypeHandler());
        assertNull(refType.<Object>getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType intType = TypeReference.intType();
        JavaType refType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        Object valueHandler = new Object();
        JavaType newReferenceType = refType.withValueHandler(valueHandler);
        assertNotNull(newReferenceType);
        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertNull(refType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType intType = TypeReference.intType();
        JavaType refType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        Object contentValueHandler = new Object();
        JavaType newReferenceType = refType.withContentValueHandler(contentValueHandler);
        assertNotNull(newReferenceType);
        assertEquals(contentValueHandler, newReferenceType.<Object>getContentValueHandler());
        assertNull(refType.<Object>getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType intType = TypeReference.intType();
        JavaType refType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        JavaType newReferenceType = refType.withStaticTyping();
        assertNotNull(newReferenceType);
        assertTrue(newReferenceType.isStaticTyping());
        assertFalse(refType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType intType = TypeReference.intType();
        JavaType refType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        JavaType subclass = TypeReference.intSubType();
        JavaType newReferenceType = refType.refine(subclass.getRawClass(), TypeBindings.emptyBindings(), null, null);
        assertNotNull(newReferenceType);
        assertTrue(newReferenceType.isSubTypeOf(refType));
    }

    // Test toString method

    @Test
    public void testToString() {
        JavaType intType = TypeReference.intType();
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, intType, null);
        String expected = "[reference type, class java.lang.Integer<I>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }
}