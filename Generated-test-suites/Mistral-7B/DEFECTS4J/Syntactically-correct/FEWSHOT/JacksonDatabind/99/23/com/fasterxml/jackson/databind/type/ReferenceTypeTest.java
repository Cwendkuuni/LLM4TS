package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(List.class, referenceType.getContentType().getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(listType, null);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(List.class, referenceType.getContentType().getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getContentType().getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        assertTrue(listType.isReferenceType());
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(stringType.isReferenceType());
    }

    @Test
    public void testGetContentType() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, listType);
        assertEquals(List.class, referenceType.getContentType().getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getContentType().getRawClass());
    }

    @Test
    public void testGetReferencedType() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, listType);
        assertEquals(List.class, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, listType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, listType);
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, listType);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testEquals() {
        JavaType listType1 = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JavaType listType2 = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JavaType referenceType1 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, listType1);
        JavaType referenceType2 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, listType2);
        assertTrue(referenceType1.equals(referenceType2));

        JavaType differentReferenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, new TypeReference<List<String>>(){}.getType());
        assertFalse(referenceType1.equals(differentReferenceType));

        JavaType nonReferenceType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(referenceType1.equals(nonReferenceType));
    }

    // Add more tests if necessary for other methods
}