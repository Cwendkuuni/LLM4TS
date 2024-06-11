package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType elemT = new JavaType();
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, elemT);
        
        assertEquals(Collection.class, type.getRawClass());
        assertEquals(elemT, type.getContentType());
        assertTrue(type.isTrueCollectionType());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType();
        JavaType elemT = new JavaType();
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elemT);
        
        assertEquals(baseType, type.getSuperClass());
        assertEquals(elemT, type.getContentType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        CollectionLikeType type = new CollectionLikeType();
        type = type.withContentType(contentType);
        
        assertEquals(contentType, type.getContentType());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        CollectionLikeType type = new CollectionLikeType();
        type = type.withContentTypeHandler(handler);
        
        assertEquals(handler, type.getContentTypeHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionLikeType type = new CollectionLikeType();
        type = type.withStaticTyping();
        
        assertTrue(type.isStaticTyping());
    }
    
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = new CollectionLikeType();
        type.setRawClass(Collection.class);
        
        assertTrue(type.isTrueCollectionType());
    }
    
    @Test
    public void testEquals() {
        JavaType elemT1 = new JavaType();
        JavaType elemT2 = new JavaType();
        CollectionLikeType type1 = CollectionLikeType.construct(Collection.class, elemT1);
        CollectionLikeType type2 = CollectionLikeType.construct(Collection.class, elemT1);
        CollectionLikeType type3 = CollectionLikeType.construct(Collection.class, elemT2);
        
        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
    }
    
    @Test
    public void testToString() {
        JavaType elemT = new JavaType();
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, elemT);
        
        String expected = "[collection-like type; class java.util.Collection, contains " + elemT + "]";
        assertEquals(expected, type.toString());
    }

}