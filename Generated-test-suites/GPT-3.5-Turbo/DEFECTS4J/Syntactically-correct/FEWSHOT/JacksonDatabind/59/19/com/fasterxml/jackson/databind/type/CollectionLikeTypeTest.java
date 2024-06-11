package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {
    
    // Test cases for construct()
    @Test
    public void testConstruct() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertEquals(Collection.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNull(type.getElementType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
    }
    
    // Test cases for upgradeFrom()
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType();
        JavaType elementType = new SimpleType();

        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertEquals(baseType, type.getBaseType());
        assertEquals(elementType, type.getElementType());
    }
    
    // Test cases for _narrow()
    @Test
    public void testNarrow() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        JavaType narrowedType = type._narrow(Collection.class);
        assertEquals(Collection.class, narrowedType.getRawClass());
        assertNull(narrowedType.getBindings());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertNull(narrowedType.getElementType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }
    
    // Test cases for withContentType()
    @Test
    public void testWithContentType() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        JavaType contentType = new SimpleType();
        
        CollectionLikeType newType = type.withContentType(contentType);
        assertEquals(contentType, newType.getContentType());
    }
    
    // Test cases for withTypeHandler()
    @Test
    public void testWithTypeHandler() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        Object typeHandler = new Object();
        
        CollectionLikeType newType = type.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newType.getTypeHandler());
    }
    
    // Test cases for withContentTypeHandler()
    @Test
    public void testWithContentTypeHandler() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        Object typeHandler = new Object();
        
        CollectionLikeType newType = type.withContentTypeHandler(typeHandler);
        assertEquals(typeHandler, newType.getContentTypeHandler());
    }
    
    // Test cases for withValueHandler()
    @Test
    public void testWithValueHandler() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        Object valueHandler = new Object();
        
        CollectionLikeType newType = type.withValueHandler(valueHandler);
        assertEquals(valueHandler, newType.getValueHandler());
    }
    
    // Test cases for withContentValueHandler()
    @Test
    public void testWithContentValueHandler() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        Object valueHandler = new Object();
        
        CollectionLikeType newType = type.withContentValueHandler(valueHandler);
        assertEquals(valueHandler, newType.getContentValueHandler());
    }
    
    // Test cases for withStaticTyping()
    @Test
    public void testWithStaticTyping() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        
        CollectionLikeType newType = type.withStaticTyping();
        assertTrue(newType.isStatic());
    }
    
    // Test cases for refine()
    @Test
    public void testRefine() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        JavaType refinedType = new SimpleType();
        
        CollectionLikeType newType = type.refine(Collection.class, null, null, null);
        assertEquals(Collection.class, newType.getRawClass());
        assertNull(newType.getBindings());
        assertNull(newType.getSuperClass());
        assertNull(newType.getSuperInterfaces());
        assertNull(newType.getElementType());
        assertNull(newType.getValueHandler());
        assertNull(newType.getTypeHandler());
        assertFalse(newType.isStatic());
    }
    
    // Test cases for isTrueCollectionType()
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = new CollectionLikeType(null, null, null, null, null, null, null, false);
        assertTrue(type.isTrueCollectionType());
    }
    
    // Test cases for equals()
    @Test
    public void testEquals() {
        CollectionLikeType type1 = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType type2 = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType type3 = new CollectionLikeType(null, null, null, null, null, null, null, true);
        CollectionLikeType type4 = new CollectionLikeType(null, null, null, null, null, null, null, true);
        
        assertTrue(type1.equals(type1));
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(new String()));
        assertTrue(type1.equals(type2));
        assertTrue(type2.equals(type1));
        
        type2 = new CollectionLikeType(null, null, null, null, null, null, null, true);
        assertTrue(type3.equals(type4));
        assertTrue(type4.equals(type3));
        assertFalse(type1.equals(type3));
        assertFalse(type3.equals(type1));
        assertFalse(type2.equals(type3));
        assertFalse(type3.equals(type2));
        
        type1 = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertFalse(type1.equals(type2));
    }
    
    // Test cases for toString()
    @Test
    public void testToString() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection, contains null]", type.toString());
    }
}