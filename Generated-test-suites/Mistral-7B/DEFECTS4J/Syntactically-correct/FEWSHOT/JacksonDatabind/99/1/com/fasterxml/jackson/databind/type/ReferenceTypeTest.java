package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertNotNull(referenceType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(stringType, intType);
        assertNotNull(referenceType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType());
    }

    @Test
    public void testGetContentType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(intType, null);
        referenceType.setContentType(stringType);
        assertEquals(stringType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(intType, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(intType, null);
        assertTrue(referenceType.hasContentType());
        referenceType.setContentType(stringType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(intType, null);
        StringBuilder expected = new StringBuilder("I");
        assertEquals(expected.toString(), referenceType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(intType, null);
        StringBuilder expected = new StringBuilder("Ljava/lang/Integer;<T:Ljava/lang/String;>");
        assertEquals(expected.toString(), referenceType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testIsAnchorType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(intType, null);
        assertFalse(referenceType.isAnchorType());
        ReferenceType anchorType = new ReferenceType(referenceType, null);
        assertTrue(anchorType.isAnchorType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(intType, null);
        ReferenceType anchorType = new ReferenceType(referenceType, null);
        assertEquals(referenceType, anchorType.getAnchorType());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(intType, null, null, null, null, null, valueHandler, null, false);
        assertEquals(valueHandler, referenceType.getContentTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object newValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(intType, null, null, null, null, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newValueHandler);
        assertEquals(newValueHandler, newReferenceType.getContentTypeHandler());
    }

    @Test
    public void testGetTypeHandler() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(intType, null, null, null, null, null, null, valueHandler, false);
        assertEquals(valueHandler, referenceType.getTypeHandler());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object newTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(intType, null, null, null, null, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testToString() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(intType, null);
        StringBuilder expected = new StringBuilder("[reference type, class java.lang.Integer<T:Ljava/lang/String;>]");
        assertEquals(expected.toString(), referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType intType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType1 = new ReferenceType(intType1, null);
        ReferenceType referenceType2 = new ReferenceType(intType2, null);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(""));
        assertFalse(referenceType1.equals(intType1));
        assertFalse(referenceType1.equals(intType2));
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
    }
}