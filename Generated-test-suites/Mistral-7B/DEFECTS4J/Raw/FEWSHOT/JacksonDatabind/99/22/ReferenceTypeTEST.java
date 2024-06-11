package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType);
        assertEquals(Integer.class, refType.getRawClass());
        assertEquals(String.class, refType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = ReferenceType.upgradeFrom(baseType, referencedType);
        assertEquals(Integer.class, refType.getRawClass());
        assertEquals(String.class, refType.getContentType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = new ReferenceType(baseType.getClass(), TypeBindings.emptyBindings(), null, null, referencedType);
        JavaType newContentType = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType newRefType = refType.withContentType(newContentType);
        assertEquals(Long.class, newRefType.getContentType().getRawClass());
        assertEquals(Integer.class, refType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = new ReferenceType(baseType.getClass(), TypeBindings.emptyBindings(), null, null, referencedType);
        assertEquals(referencedType, refType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = new ReferenceType(baseType.getClass(), TypeBindings.emptyBindings(), null, null, referencedType);
        assertTrue(refType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = new ReferenceType(baseType.getClass(), TypeBindings.emptyBindings(), null, null, referencedType);
        assertTrue(refType.hasContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = new ReferenceType(baseType.getClass(), TypeBindings.emptyBindings(), null, null, referencedType);
        assertEquals(referencedType, refType.getReferencedType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = new ReferenceType(baseType.getClass(), TypeBindings.emptyBindings(), null, null, referencedType);
        assertEquals(refType, refType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = new ReferenceType(baseType.getClass(), TypeBindings.emptyBindings(), null, null, referencedType);
        assertFalse(refType.isAnchorType());
    }

    @Test
    public void testEquals() {
        JavaType baseType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType1 = new ReferenceType(baseType1.getClass(), TypeBindings.emptyBindings(), null, null, referencedType1);

        JavaType baseType2 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType2 = new ReferenceType(baseType2.getClass(), TypeBindings.emptyBindings(), null, null, referencedType2);

        JavaType baseType3 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType3 = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType refType3 = new ReferenceType(baseType1.getClass(), TypeBindings.emptyBindings(), null, null, referencedType3);

        assertTrue(refType1.equals(refType1));
        assertTrue(refType1.equals(refType1.hashCode()));
        assertFalse(refType1.equals(baseType1));
        assertFalse(refType1.equals(referencedType1));
        assertFalse(refType1.equals(refType2));
        assertFalse(refType1.equals(refType3));
    }
}