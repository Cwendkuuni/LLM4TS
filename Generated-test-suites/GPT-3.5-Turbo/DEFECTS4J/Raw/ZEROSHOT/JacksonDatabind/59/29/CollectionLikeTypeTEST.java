package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {
    
    private CollectionLikeType collectionLikeType;
    
    @Before
    public void setUp() {
        collectionLikeType = new CollectionLikeType(ArrayList.class, new TypeBindings(), null, null, new SimpleType(Integer.TYPE), null, null, false);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertTrue(collectionLikeType.getBindings().isEmpty());
        assertNull(collectionLikeType.getSuperClass());
        assertNull(collectionLikeType.getSuperInterfaces());
        assertEquals(SimpleType.class, collectionLikeType.getElementType().getClass());
    }
    
    @Test
    public void testConstruct() {
        CollectionLikeType constructedType = CollectionLikeType.construct(ArrayList.class, new TypeBindings(), null, null, new SimpleType(Integer.TYPE));
        assertEquals(ArrayList.class, constructedType.getRawClass());
        assertTrue(constructedType.getBindings().isEmpty());
        assertNull(constructedType.getSuperClass());
        assertNull(constructedType.getSuperInterfaces());
        assertEquals(SimpleType.class, constructedType.getElementType().getClass());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(ArrayList.class);
        JavaType elementType = new SimpleType(Integer.TYPE);
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertEquals(ArrayList.class, upgradedType.getRawClass());
        assertTrue(upgradedType.getBindings().isEmpty());
        assertNull(upgradedType.getSuperClass());
        assertNull(upgradedType.getSuperInterfaces());
        assertEquals(SimpleType.class, upgradedType.getElementType().getClass());
    }
    
    @Test
    public void testNarrow() {
        JavaType narrowedType = collectionLikeType._narrow(LinkedList.class);
        assertEquals(LinkedList.class, narrowedType.getRawClass());
        assertTrue(narrowedType.getBindings().isEmpty());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertEquals(SimpleType.class, narrowedType.getElementType().getClass());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(String.class);
        JavaType withContentType = collectionLikeType.withContentType(contentType);
        assertEquals(ArrayList.class, withContentType.getRawClass());
        assertTrue(withContentType.getBindings().isEmpty());
        assertNull(withContentType.getSuperClass());
        assertNull(withContentType.getSuperInterfaces());
        assertEquals(SimpleType.class, withContentType.getElementType().getClass());
        assertEquals(String.class, withContentType.getElementType().getRawClass());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionLikeType withTypeHandler = collectionLikeType.withTypeHandler(typeHandler);
        assertEquals(ArrayList.class, withTypeHandler.getRawClass());
        assertTrue(withTypeHandler.getBindings().isEmpty());
        assertNull(withTypeHandler.getSuperClass());
        assertNull(withTypeHandler.getSuperInterfaces());
        assertEquals(SimpleType.class, withTypeHandler.getElementType().getClass());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        JavaType withContentTypeHandler = collectionLikeType.withContentTypeHandler(contentTypeHandler);
        assertEquals(ArrayList.class, withContentTypeHandler.getRawClass());
        assertTrue(withContentTypeHandler.getBindings().isEmpty());
        assertNull(withContentTypeHandler.getSuperClass());
        assertNull(withContentTypeHandler.getSuperInterfaces());
        assertEquals(SimpleType.class, withContentTypeHandler.getElementType().getClass());
        assertEquals(contentTypeHandler, withContentTypeHandler.getElementType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionLikeType withValueHandler = collectionLikeType.withValueHandler(valueHandler);
        assertEquals(ArrayList.class, withValueHandler.getRawClass());
        assertTrue(withValueHandler.getBindings().isEmpty());
        assertNull(withValueHandler.getSuperClass());
        assertNull(withValueHandler.getSuperInterfaces());
        assertEquals(SimpleType.class, withValueHandler.getElementType().getClass());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        JavaType withContentValueHandler = collectionLikeType.withContentValueHandler(contentValueHandler);
        assertEquals(ArrayList.class, withContentValueHandler.getRawClass());
        assertTrue(withContentValueHandler.getBindings().isEmpty());
        assertNull(withContentValueHandler.getSuperClass());
        assertNull(withContentValueHandler.getSuperInterfaces());
        assertEquals(SimpleType.class, withContentValueHandler.getElementType().getClass());
        assertEquals(contentValueHandler, withContentValueHandler.getElementType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType withStaticTyping = collectionLikeType.withStaticTyping();
        assertEquals(ArrayList.class, withStaticTyping.getRawClass());
        assertTrue(withStaticTyping.getBindings().isEmpty());
        assertNull(withStaticTyping.getSuperClass());
        assertNull(withStaticTyping.getSuperInterfaces());
        assertTrue(withStaticTyping.getElementType().isJavaLangObject());
        assertTrue(withStaticTyping.isStatic());
    }
    
    @Test
    public void testRefine() {
        JavaType refinedType = collectionLikeType.refine(LinkedList.class, new TypeBindings(), null, null);
        assertEquals(LinkedList.class, refinedType.getRawClass());
        assertTrue(refinedType.getBindings().isEmpty());
        assertNull(refinedType.getSuperClass());
        assertNull(refinedType.getSuperInterfaces());
        assertEquals(SimpleType.class, refinedType.getElementType().getClass());
    }
    
    @Test
    public void testIsContainerType() {
        assertTrue(collectionLikeType.isContainerType());
    }
    
    @Test
    public void testIsCollectionLikeType() {
        assertTrue(collectionLikeType.isCollectionLikeType());
    }
    
    @Test
    public void testGetContentType() {
        assertEquals(SimpleType.class, collectionLikeType.getContentType().getClass());
        assertEquals(Integer.TYPE, collectionLikeType.getContentType().getRawClass());
    }
    
    @Test
    public void testGetContentValueHandler() {
        assertNull(collectionLikeType.getContentValueHandler());
    }
    
    @Test
    public void testGetContentTypeHandler() {
        assertNull(collectionLikeType.getContentTypeHandler());
    }
    
    @Test
    public void testHasHandlers() {
        assertFalse(collectionLikeType.hasHandlers());
    }
    
    @Test
    public void testGetErasedSignature() {
        assertEquals("java.util.ArrayList", collectionLikeType.getErasedSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        assertEquals("java.util.ArrayList<java.lang.Integer>;", collectionLikeType.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testIsTrueCollectionType() {
        assertTrue(collectionLikeType.isTrueCollectionType());
    }
    
    @Test
    public void testEquals() {
        CollectionLikeType other = new CollectionLikeType(ArrayList.class, new TypeBindings(), null, null, new SimpleType(Integer.TYPE), null, null, false);
        assertTrue(collectionLikeType.equals(other));
        
        other = new CollectionLikeType(LinkedList.class, new TypeBindings(), null, null, new SimpleType(Integer.TYPE), null, null, false);
        assertFalse(collectionLikeType.equals(other));
        
        other = new CollectionLikeType(ArrayList.class, new TypeBindings(), null, null, new SimpleType(String.class), null, null, false);
        assertFalse(collectionLikeType.equals(other));
    }
    
    @Test
    public void testToString() {
        assertEquals("[collection-like type; class java.util.ArrayList, contains int]", collectionLikeType.toString());
    }
}