package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType elemType = JavaType.constructCollectionType(List.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(List.class, null, null, elemType);
        assertEquals(List.class, collectionLikeType.getRawClass());
        assertEquals(elemType, collectionLikeType.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.constructRawType(ArrayList.class);
        JavaType elemType = JavaType.constructCollectionType(List.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(elemType, collectionLikeType.getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = JavaType.constructRawType(Integer.class);
        JavaType elemType = JavaType.constructCollectionType(List.class, String.class);
        CollectionLikeType.upgradeFrom(baseType, elemType);
    }

    // Add test cases for other methods here

    // Add edge case and exception handling tests here

    // Merge other test cases and add their names here
}