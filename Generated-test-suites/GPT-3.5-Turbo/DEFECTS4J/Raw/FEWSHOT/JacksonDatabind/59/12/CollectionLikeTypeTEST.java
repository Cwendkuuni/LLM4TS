package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CollectionLikeTypeTest {
    @Test
    public void testConstruct() {
        // Test construct method
        CollectionLikeType type = CollectionLikeType.construct(LinkedList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        assertEquals(LinkedList.class, type.getRawClass());
        assertTrue(type.isTrueCollectionType());
        assertEquals(SimpleType.construct(String.class), type.getContentType());

        // Test deprecated construct method
        CollectionLikeType deprecatedType = CollectionLikeType.construct(LinkedList.class, SimpleType.construct(String.class));
        assertEquals(LinkedList.class, deprecatedType.getRawClass());
        assertTrue(deprecatedType.isTrueCollectionType());
        assertEquals(SimpleType.construct(String.class), deprecatedType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        // Test upgradeFrom method
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(baseType, SimpleType.construct(User.class));
        assertEquals(ArrayList.class, upgradedType.getRawClass());
        assertTrue(upgradedType.isTrueCollectionType());
        assertEquals(SimpleType.construct(User.class), upgradedType.getContentType());
    }

    @Test
    public void testWithContentType() {
        // Test withContentType method
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        CollectionLikeType type = baseType.withContentType(SimpleType.construct(User.class));
        assertEquals(ArrayList.class, type.getRawClass());
        assertTrue(type.isTrueCollectionType());
        assertEquals(SimpleType.construct(User.class), type.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        // Test withTypeHandler method
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        CollectionLikeType type = baseType.withTypeHandler(new TypeHandler());
        assertEquals(ArrayList.class, type.getRawClass());
        assertTrue(type.isTrueCollectionType());
        assertEquals(new TypeHandler(), type.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Test withContentTypeHandler method
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        CollectionLikeType type = baseType.withContentTypeHandler(new TypeHandler());
        assertEquals(ArrayList.class, type.getRawClass());
        assertTrue(type.isTrueCollectionType());
        assertEquals(new TypeHandler(), type.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        // Test withValueHandler method
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        CollectionLikeType type = baseType.withValueHandler(new ValueHandler());
        assertEquals(ArrayList.class, type.getRawClass());
        assertTrue(type.isTrueCollectionType());
        assertEquals(new ValueHandler(), type.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        // Test withContentValueHandler method
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        CollectionLikeType type = baseType.withContentValueHandler(new ValueHandler());
        assertEquals(ArrayList.class, type.getRawClass());
        assertTrue(type.isTrueCollectionType());
        assertEquals(new ValueHandler(), type.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // Test withStaticTyping method
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        CollectionLikeType type = baseType.withStaticTyping();
        assertEquals(ArrayList.class, type.getRawClass());
        assertTrue(type.isTrueCollectionType());
        assertTrue(type.isStaticTyping());
    }

    @Test
    public void testRefine() {
        // Test refine method
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        CollectionLikeType type = baseType.refine(LinkedList.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(LinkedList.class, type.getRawClass());
        assertTrue(type.isTrueCollectionType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        assertTrue(baseType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType type1 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        CollectionLikeType type2 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        assertEquals(type1, type2);
    }

    @Test
    public void testToString() {
        CollectionLikeType type = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        assertEquals("[collection-like type; class java.util.ArrayList, contains java.lang.String]", type.toString());
    }
}