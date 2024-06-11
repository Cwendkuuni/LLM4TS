
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Collection.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new SimpleType(String.class);

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);

        assertEquals(rawType, collectionLikeType.getRawClass());
        assertEquals(bindings, collectionLikeType.getBindings());
        assertEquals(superClass, collectionLikeType.getSuperClass());
        assertEquals(elemT, collectionLikeType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(ArrayList.class);
        JavaType elementType = new SimpleType(String.class);

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(elementType, collectionLikeType.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType baseType = new SimpleType(List.class);
        JavaType elementType = new SimpleType(Integer.class);

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        Object handler = new Object();

        CollectionLikeType result = collectionLikeType.withContentTypeHandler(handler);

        assertEquals(handler, result.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType baseType = new SimpleType(Set.class);
        JavaType elementType = new SimpleType(Long.class);
        Object handler = new Object();

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        collectionLikeType = collectionLikeType.withTypeHandler(handler);

        assertTrue(collectionLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType baseType = new SimpleType(Queue.class);
        JavaType elementType = new SimpleType(Double.class);

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType baseType = new SimpleType(Map.class);
        JavaType elementType = new SimpleType(Float.class);

        CollectionLikeType collectionLikeType1 = CollectionLikeType.upgradeFrom(baseType, elementType);
        CollectionLikeType collectionLikeType2 = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertTrue(collectionLikeType1.equals(collectionLikeType2));
    }

}

