
package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstructWithRawTypeAndJavaType() {
        // Typical use case
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, SimpleType.construct(String.class));
        Assert.assertNotNull(collectionLikeType);
        
        // Edge case: rawType is null
        collectionLikeType = CollectionLikeType.construct(null, SimpleType.construct(String.class));
        Assert.assertNull(collectionLikeType.getClass());
        
        // Edge case: elemT is null
        collectionLikeType = CollectionLikeType.construct(Collection.class, null);
        Assert.assertNull(collectionLikeType.getContentType());
    }
    
    @Test
    public void testConstructWithRawTypeAndTypeBindings() {
        // Typical use case
        TypeBindings bindings = TypeBindings.create(Collection.class, SimpleType.construct(String.class));
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, bindings);
        Assert.assertNotNull(collectionLikeType);
        
        // Edge case: bindings is null
        collectionLikeType = CollectionLikeType.construct(Collection.class, null);
        Assert.assertNotNull(collectionLikeType.getBindings());
        
        // Edge case: rawType is null
        collectionLikeType = CollectionLikeType.construct(null, bindings);
        Assert.assertNull(collectionLikeType.getClass());
    }
    
    @Test
    public void testUpgradeFrom() {
        // Typical use case
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(SimpleType.construct(Collection.class), SimpleType.construct(String.class));
        Assert.assertNotNull(collectionLikeType);
        
        // Edge case: baseType is not TypeBase
        collectionLikeType = CollectionLikeType.upgradeFrom(new TypeReference<Collection<String>>() {}, SimpleType.construct(String.class));
        Assert.assertNotNull(collectionLikeType.getClass());
        
        // Edge case: baseType is null
        try {
            collectionLikeType = CollectionLikeType.upgradeFrom(null, SimpleType.construct(String.class));
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Can not upgrade from an instance of null", e.getMessage());
        }
    }

    @Test
    public void testIsTrueCollectionType() {
        // Typical use case
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, SimpleType.construct(String.class));
        Assert.assertTrue(collectionLikeType.isTrueCollectionType());
        
        // Edge case: rawType is not assignable from Collection
        collectionLikeType = CollectionLikeType.construct(Object.class, SimpleType.construct(String.class));
        Assert.assertFalse(collectionLikeType.isTrueCollectionType());
    }
    
}
