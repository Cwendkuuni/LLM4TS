package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionLikeTypeTest {
    
    @Test
    public void testConstruct() {
        // Test with null values
        CollectionLikeType type = CollectionLikeType.construct(null, null, null, null, null);
        assertNull(type.getContentType());
        assertFalse(type.isTrueCollectionType());
        
        // Test with non-null values
        JavaType elemT = new SimpleType(Object.class);
        type = CollectionLikeType.construct(Collection.class, null, null, null, elemT);
        assertEquals(elemT, type.getContentType());
        assertTrue(type.isTrueCollectionType());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(Collection.class);
        JavaType elementType = new SimpleType(String.class);
        
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertEquals(baseType, type.getSuperClass());
        assertEquals(elementType, type.getContentType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = new SimpleType(SimpleType.class);
        JavaType elementType = new SimpleType(String.class);
        
        CollectionLikeType.upgradeFrom(baseType, elementType);
    }
    
    @Test
    public void testConstructor() {
        // Test with non-null values
        JavaType elemT = new SimpleType(Object.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elemT, null, null, false);
        assertEquals(elemT, type.getContentType());
        assertTrue(type.isTrueCollectionType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType baseType = new SimpleType(Collection.class);
        JavaType elementType = new SimpleType(String.class);
        
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, null);
        assertEquals(baseType, type.getSuperClass());
        
        type = type.withContentType(elementType);
        assertEquals(elementType, type.getContentType());
    }
    
    @Test
    public void testWithHandlers() {
        JavaType elemType = new SimpleType(Object.class);
        
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elemType);
        
        assertNull(type.getContentValueHandler());
        assertNull(type.getContentTypeHandler());
        
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        type = type.withValueHandler(valueHandler);
        type = type.withContentTypeHandler(typeHandler);
        
        assertEquals(valueHandler, type.getContentValueHandler());
        assertEquals(typeHandler, type.getContentTypeHandler());
    }
    
    @Test
    public void testEquals() {
        CollectionLikeType type1 = CollectionLikeType.construct(Collection.class, null, null, null, null);
        CollectionLikeType type2 = CollectionLikeType.construct(Collection.class, null, null, null, null);
        
        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(""));
        
        JavaType elemT = new SimpleType(String.class);
        type2 = CollectionLikeType.construct(Collection.class, null, null, null, elemT);
        assertFalse(type1.equals(type2));
        assertFalse(type1.hashCode() == type2.hashCode());
    }
    
    @Test
    public void testToString() {
        JavaType elemT = new SimpleType(Object.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elemT);
        
        assertEquals("[collection-like type; class java.util.Collection, contains SimpleType for class java.lang.Object]", type.toString());
    }
    
}