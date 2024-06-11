package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {
    
    @Test
    public void testConstruct() {
        JavaType elementType = new SimpleType(String.class);
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        CollectionLikeType type = CollectionLikeType.construct(CollectionLikeType.class, bindings, superClass, superInterfaces, elementType);
        
        assertEquals(CollectionLikeType.class, type.getRawClass());
        assertEquals(String.class, type.getContentType().getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(superInterfaces, type.getInterfaces());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(Collection.class);
        JavaType elementType = new SimpleType(String.class);
        
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);
        
        assertEquals(CollectionLikeType.class, type.getClass());
        assertEquals(Collection.class, type.getRawClass());
        assertEquals(String.class, type.getContentType().getRawClass());
    }
    
    @Test
    public void testNarrow() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);

        CollectionLikeType narrowedType = (CollectionLikeType) baseType._narrow(Collection.class);

        assertEquals(Collection.class, narrowedType.getRawClass());
        assertEquals(String.class, narrowedType.getContentType().getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);

        JavaType contentType = new SimpleType(Integer.class);
        CollectionLikeType type = (CollectionLikeType) baseType.withContentType(contentType);

        assertEquals(CollectionLikeType.class, type.getClass());
        assertEquals(CollectionLikeType.class, type.getRawClass());
        assertEquals(Integer.class, type.getContentType().getRawClass());
    }
    
    @Test
    public void testWithContentValueHandler() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        Object handler = new Object();

        CollectionLikeType type = (CollectionLikeType) baseType.withContentValueHandler(handler);

        assertEquals(CollectionLikeType.class, type.getClass());
        assertEquals(CollectionLikeType.class, type.getRawClass());
        assertEquals(handler, type.getContentType().getValueHandler());
    }
    
    @Test
    public void testIsCollectionLikeType() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        
        assertFalse(baseType.isCollectionLikeType());
    }
    
    @Test
    public void testGetContentType() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        
        assertEquals(String.class, baseType.getContentType().getRawClass());
    }
    
    @Test
    public void testGetContentValueHandler() {
        JavaType elementType = new SimpleType(String.class);
        Object handler = new Object();
        elementType.setValueHandler(handler);
        
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        
        assertEquals(handler, baseType.getContentValueHandler());
    }
    
    @Test
    public void testGetContentTypeHandler() {
        JavaType elementType = new SimpleType(String.class);
        Object handler = new Object();
        elementType.setTypeHandler(handler);
        
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        
        assertEquals(handler, baseType.getContentTypeHandler());
    }
    
    @Test
    public void testHasHandlers() {
        JavaType elementType = new SimpleType(String.class);
        Object valueHandler = new Object();
        
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, valueHandler, null, false);
        
        assertEquals(true, baseType.hasHandlers());
    }
    
    @Test
    public void testGetErasedSignature() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        
        assertEquals("Lcom/fasterxml/jackson/databind/type/CollectionLikeType;", baseType.getErasedSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        
        assertEquals("Lcom/fasterxml/jackson/databind/type/CollectionLikeType<Ljava/lang/String;>;", baseType.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testBuildCanonicalName() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        
        assertEquals("com.fasterxml.jackson.databind.type.CollectionLikeType<java.lang.String>", baseType.buildCanonicalName());
    }
    
    @Test
    public void testIsTrueCollectionType() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        
        assertEquals(false, baseType.isTrueCollectionType());
    }
    
    @Test
    public void testEquals() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        CollectionLikeType other = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        
        assertEquals(true, baseType.equals(other));
    }
    
    @Test
    public void testToString() {
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType baseType = new CollectionLikeType(CollectionLikeType.class, new TypeBindings(), null, null, elementType, null, null, false);
        
        assertEquals("[collection-like type; class com.fasterxml.jackson.databind.type.CollectionLikeType, contains java.lang.String]", baseType.toString());
    }
    
}