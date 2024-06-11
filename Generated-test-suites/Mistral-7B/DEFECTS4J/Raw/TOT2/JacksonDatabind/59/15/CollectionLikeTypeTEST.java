package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {
    // Test for constructor with no arguments
    @Test
    public void testCollectionLikeTypeWithNoArguments() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = null;
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elemT);
        assertEquals("java.util.Collection", collectionLikeType.getCanonicalName());
    }

    // Test for constructor with arguments
    @Test
    public void testCollectionLikeTypeWithArguments() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elemT);
        assertEquals("java.util.ArrayList<java.lang.Integer>", collectionLikeType.getCanonicalName());
    }

    // Test for upgradeFrom method
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(List.class);
        JavaType elementType = Integer.TYPE;
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertEquals("com.fasterxml.jackson.databind.type.CollectionLikeType", collectionLikeType.getClass().getName());
    }

    // Test for isTrueCollectionType method
    @Test
    public void testIsTrueCollectionType() {
        Class<?> rawType = ArrayList.class;
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), _bogusSuperClass(rawType), null, Integer.TYPE);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    // Test for equals method
    @Test
    public void testEquals() {
        Class<?> rawType1 = ArrayList.class;
        Class<?> rawType2 = LinkedList.class;
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        JavaType superClass1 = ArrayList.class;
        JavaType superClass2 = LinkedList.class;
        JavaType elemT1 = Integer.TYPE;
        JavaType elemT2 = Integer.TYPE;
        CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(rawType1, bindings1, superClass1, null, elemT1);
        CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(rawType2, bindings2, superClass2, null, elemT2);
        assertFalse(collectionLikeType1.equals(collectionLikeType2));
        CollectionLikeType collectionLikeType3 = new CollectionLikeType(collectionLikeType1);
        assertTrue(collectionLikeType1.equals(collectionLikeType3));
    }
}