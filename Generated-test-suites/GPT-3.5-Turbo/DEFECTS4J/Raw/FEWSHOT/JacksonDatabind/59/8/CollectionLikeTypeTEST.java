package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CollectionLikeTypeTest {
    @Test
    public void testConstruct() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new SimpleType(Object.class);
        CollectionLikeType type = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        
        assertEquals(rawType, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(superInts, type.getInterfaces());
        assertEquals(elemT, type.getElementType());
        assertFalse(type.hasValueHandler());
        assertFalse(type.hasTypeHandler());
        assertFalse(type.isCanonical());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(Object.class);
        JavaType elementType = new SimpleType(Object.class);
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);
        
        assertEquals(baseType, type.getBaseType());
        assertEquals(elementType, type.getElementType());
        assertFalse(type.hasValueHandler());
        assertFalse(type.hasTypeHandler());
        assertFalse(type.isCanonical());
    }

    @Test
    public void testNarrow() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new SimpleType(Object.class);
        CollectionLikeType base = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        JavaType subclass = new SimpleType(Object.class);
        
        CollectionLikeType type = base.narrow(subclass);
        
        assertEquals(subclass, type.getSubClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(superInts, type.getInterfaces());
        assertEquals(elemT, type.getElementType());
        assertFalse(type.hasValueHandler());
        assertFalse(type.hasTypeHandler());
        assertFalse(type.isCanonical());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new SimpleType(Object.class);
        CollectionLikeType base = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        JavaType contentType = new SimpleType(Object.class);
        
        CollectionLikeType type = base.withContentType(contentType);
        
        assertEquals(rawType, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(superInts, type.getInterfaces());
        assertEquals(contentType, type.getElementType());
        assertFalse(type.hasValueHandler());
        assertFalse(type.hasTypeHandler());
        assertFalse(type.isCanonical());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new SimpleType(Object.class);
        CollectionLikeType base = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        Object handler = new Object();
        
        CollectionLikeType type = base.withContentTypeHandler(handler);
        
        assertEquals(rawType, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(superInts, type.getInterfaces());
        assertTrue(type.hasContentTypeHandler());
        assertEquals(handler, type.getContentTypeHandler());
        assertFalse(type.hasValueHandler());
        assertFalse(type.hasTypeHandler());
        assertFalse(type.isCanonical());
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new SimpleType(Object.class);
        CollectionLikeType base = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        
        CollectionLikeType type = base.withStaticTyping();
        
        assertEquals(rawType, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(superInts, type.getInterfaces());
        assertEquals(elemT.withStaticTyping(), type.getElementType());
        assertFalse(type.hasValueHandler());
        assertFalse(type.hasTypeHandler());
        assertTrue(type.isStaticTyping());
        assertFalse(type.isCanonical());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new SimpleType(Object.class);
        CollectionLikeType base = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        Class<?> refinedType = Object.class;
        TypeBindings refinedBindings = new TypeBindings();
        JavaType refinedSuperClass = new SimpleType(Object.class);
        JavaType[] refinedSuperInterfaces = new JavaType[0];
        
        CollectionLikeType type = base.refine(refinedType, refinedBindings, refinedSuperClass, refinedSuperInterfaces);
        
        assertEquals(refinedType, type.getRawClass());
        assertEquals(refinedBindings, type.getBindings());
        assertEquals(refinedSuperClass, type.getSuperClass());
        assertEquals(refinedSuperInterfaces, type.getInterfaces());
        assertEquals(elemT, type.getElementType());
        assertFalse(type.hasValueHandler());
        assertFalse(type.hasTypeHandler());
        assertFalse(type.isCanonical());
    }

    @Test
    public void testIsTrueCollectionType() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new SimpleType(Object.class);
        CollectionLikeType type = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        
        assertTrue(type.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new SimpleType(Object.class);
        CollectionLikeType type1 = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionLikeType type2 = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionLikeType type3 = CollectionLikeType.construct(Collection.class, bindings, superClass, superInts, elemT);
        CollectionLikeType type4 = CollectionLikeType.construct(rawType, null, superClass, superInts, elemT);
        CollectionLikeType type5 = CollectionLikeType.construct(rawType, bindings, null, superInts, elemT);
        CollectionLikeType type6 = CollectionLikeType.construct(rawType, bindings, superClass, null, elemT);
        CollectionLikeType type7 = CollectionLikeType.construct(rawType, bindings, superClass, superInts, null);
        
        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(new Object()));
        assertFalse(type1.equals(type4));
        assertFalse(type1.equals(type5));
        assertFalse(type1.equals(type6));
        assertFalse(type1.equals(type7));
    }

    @Test
    public void testToString() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new SimpleType(Object.class);
        CollectionLikeType type = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        
        String expected = "[collection-like type; class java.util.Collection, contains "+elemT+"]";
        assertEquals(expected, type.toString());
    }
}