package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType refTypeRef = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertEquals(refType, refTypeRef.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType listType = TypeFactory.defaultInstance().constructType(String[].class);
        ReferenceType refType = ReferenceType.upgradeFrom(stringType, listType);
        assertEquals(listType, refType.getContentType());
    }

    @Test
    public void testGetContentType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], stringType, null, null, null, false);
        assertEquals(stringType, refType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], stringType, null, null, null, false);
        assertTrue(refType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType refType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], stringType, null, null, null, false);
        assertTrue(refType.hasContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType refType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], stringType, null, null, null, false);
        assertEquals(stringType, refType.getReferencedType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType refType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], stringType, null, null, null, false);
        assertFalse(refType.isAnchorType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType refType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], stringType, refType, null, null, false);
        assertEquals(refType, refType.getAnchorType());
    }

    @Test
    public void testToString() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType refType = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], stringType, null, null, null, false);
        String result = refType.toString();
        String expected = "[reference type, class [Ljava.lang.String;<>" +
                " + java.lang.String>]";
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        JavaType stringType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringType2 = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType refType1 = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], stringType1, null, null, null, false);
        ReferenceType refType2 = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], stringType2, null, null, null, false);
        assertTrue(refType1.equals(refType1));
        assertTrue(refType1.equals(refType1));
        assertTrue(refType1.equals(refType2));
        assertTrue(refType2.equals(refType1));
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType refType3 = new ReferenceType(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0], intType, null, null, null, false);
        assertFalse(refType1.equals(refType3));
        assertFalse(refType3.equals(refType1));
    }
}