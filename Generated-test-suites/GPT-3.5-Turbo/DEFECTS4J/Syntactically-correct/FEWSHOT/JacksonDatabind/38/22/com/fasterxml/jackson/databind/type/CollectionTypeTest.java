package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {
    
    @Test
    public void testConstruct() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType elemT = new JavaType();
        JavaType[] superInts = {new JavaType(), new JavaType()};
        CollectionType collectionType = CollectionType.construct(CollectionType.class, bindings, superClass, superInts, elemT);

        assertEquals(CollectionType.class, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }
    
    @Test
    public void testConstructDeprecated() {
        JavaType elemT = new JavaType();
        CollectionType collectionType = CollectionType.construct(CollectionType.class, elemT);

        assertEquals(CollectionType.class, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }
    
    @Test
    public void testConstructor() {
        Class<?> collT = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType elemT = new JavaType();
        JavaType[] superInts = {new JavaType(), new JavaType()};
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);

        assertEquals(collT, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }
    
    @Test
    public void testConstructorWithBase() {
        TypeBase base = new TypeBase();
        JavaType elemT = new JavaType();
        
        CollectionType collectionType = new CollectionType(base, elemT);

        assertEquals(CollectionType.class, collectionType.getRawClass());
        assertEquals(base, collectionType.getBaseType());
        assertEquals(elemT, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType elemT = new JavaType();
        JavaType[] superInts = {new JavaType(), new JavaType()};
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(subclass, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        JavaType narrowedType = collectionType.narrow(SubCollectionType.class);

        assertEquals(SubCollectionType.class, narrowedType.getRawClass());
        assertEquals(bindings, narrowedType.getBindings());
        assertEquals(superClass, narrowedType.getSuperClass());
        assertEquals(superInts, narrowedType.getSuperInterfaces());
        assertEquals(elemT, narrowedType.getElementType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }
    
    @Test
    public void testWithContentType() {
        Class<?> collT = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType elemT = new JavaType();
        JavaType[] superInts = {new JavaType(), new JavaType()};
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        JavaType newType = new JavaType();
        JavaType typeWithContentType = collectionType.withContentType(newType);
        
        assertEquals(collT, typeWithContentType.getRawClass());
        assertEquals(bindings, typeWithContentType.getBindings());
        assertEquals(superClass, typeWithContentType.getSuperClass());
        assertEquals(superInts, typeWithContentType.getSuperInterfaces());
        assertEquals(newType, typeWithContentType.getElementType());
        assertNull(typeWithContentType.getValueHandler());
        assertNull(typeWithContentType.getTypeHandler());
        assertFalse(typeWithContentType.isStatic());
    }
    
    @Test
    public void testWithTypeHandler() {
        Class<?> collT = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType elemT = new JavaType();
        JavaType[] superInts = {new JavaType(), new JavaType()};
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Object newTypeHandler = new Object();
        CollectionType typeWithTypeHandler = collectionType.withTypeHandler(newTypeHandler);
        
        assertEquals(collT, typeWithTypeHandler.getRawClass());
        assertEquals(bindings, typeWithTypeHandler.getBindings());
        assertEquals(superClass, typeWithTypeHandler.getSuperClass());
        assertEquals(superInts, typeWithTypeHandler.getSuperInterfaces());
        assertEquals(elemT, typeWithTypeHandler.getElementType());
        assertNull(typeWithTypeHandler.getValueHandler());
        assertEquals(newTypeHandler, typeWithTypeHandler.getTypeHandler());
        assertFalse(typeWithTypeHandler.isStatic());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> collT = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType elemT = new JavaType();
        JavaType[] superInts = {new JavaType(), new JavaType()};
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Object newTypeHandler = new Object();
        CollectionType typeWithContentTypeHandler = collectionType.withContentTypeHandler(newTypeHandler);
        
        assertEquals(collT, typeWithContentTypeHandler.getRawClass());
        assertEquals(bindings, typeWithContentTypeHandler.getBindings());
        assertEquals(superClass, typeWithContentTypeHandler.getSuperClass());
        assertEquals(superInts, typeWithContentTypeHandler.getSuperInterfaces());
        assertEquals(elemT.withTypeHandler(newTypeHandler), typeWithContentTypeHandler.getElementType());
        assertNull(typeWithContentTypeHandler.getValueHandler());
        assertNull(typeWithContentTypeHandler.getTypeHandler());
        assertFalse(typeWithContentTypeHandler.isStatic());
    }
    
    @Test
    public void testWithValueHandler() {
        Class<?> collT = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType elemT = new JavaType();
        JavaType[] superInts = {new JavaType(), new JavaType()};
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Object newValueHandler = new Object();
        CollectionType typeWithValueHandler = collectionType.withValueHandler(newValueHandler);
        
        assertEquals(collT, typeWithValueHandler.getRawClass());
        assertEquals(bindings, typeWithValueHandler.getBindings());
        assertEquals(superClass, typeWithValueHandler.getSuperClass());
        assertEquals(superInts, typeWithValueHandler.getSuperInterfaces());
        assertEquals(elemT, typeWithValueHandler.getElementType());
        assertEquals(newValueHandler, typeWithValueHandler.getValueHandler());
        assertNull(typeWithValueHandler.getTypeHandler());
        assertFalse(typeWithValueHandler.isStatic());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Class<?> collT = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType elemT = new JavaType();
        JavaType[] superInts = {new JavaType(), new JavaType()};
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Object newValueHandler = new Object();
        CollectionType typeWithContentValueHandler = collectionType.withContentValueHandler(newValueHandler);
        
        assertEquals(collT, typeWithContentValueHandler.getRawClass());
        assertEquals(bindings, typeWithContentValueHandler.getBindings());
        assertEquals(superClass, typeWithContentValueHandler.getSuperClass());
        assertEquals(superInts, typeWithContentValueHandler.getSuperInterfaces());
        assertEquals(elemT.withValueHandler(newValueHandler), typeWithContentValueHandler.getElementType());
        assertNull(typeWithContentValueHandler.getValueHandler());
        assertNull(typeWithContentValueHandler.getTypeHandler());
        assertFalse(typeWithContentValueHandler.isStatic());
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> collT = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType elemT = new JavaType();
        JavaType[] superInts = {new JavaType(), new JavaType()};
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        CollectionType typeWithStaticTyping = collectionType.withStaticTyping();
        
        assertEquals(collT, typeWithStaticTyping.getRawClass());
        assertEquals(bindings, typeWithStaticTyping.getBindings());
        assertEquals(superClass, typeWithStaticTyping.getSuperClass());
        assertEquals(superInts, typeWithStaticTyping.getSuperInterfaces());
        assertEquals(elemT.withStaticTyping(), typeWithStaticTyping.getElementType());
        assertNull(typeWithStaticTyping.getValueHandler());
        assertNull(typeWithStaticTyping.getTypeHandler());
        assertTrue(typeWithStaticTyping.isStatic());
    }
    
    @Test
    public void testRefine() {
        Class<?> collT = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType elemT = new JavaType();
        JavaType[] superInts = {new JavaType(), new JavaType()};
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
        Class<?> rawType = SubCollectionType.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType newSuperClass = new JavaType();
        JavaType newElemT = new JavaType();
        JavaType[] newSuperInts = {new JavaType(), new JavaType()};
        
        JavaType refinedType = collectionType.refine(rawType, newBindings, newSuperClass, newSuperInts);
        
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(newBindings, refinedType.getBindings());
        assertEquals(newSuperClass, refinedType.getSuperClass());
        assertEquals(newSuperInts, refinedType.getSuperInterfaces());
        assertEquals(newElemT, refinedType.getElementType());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertFalse(refinedType.isStatic());
    }
    
    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType();
        String toString = collectionType.toString();
        
        assertEquals("[collection type; class "+ CollectionType.class.getName() +", contains null]", toString);
    }
    
    private static class SubCollectionType extends CollectionType {
        
    }
}