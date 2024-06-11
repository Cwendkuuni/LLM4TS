package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = { new JavaType() };
        JavaType elemT = new JavaType();
        CollectionType result = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        assertSame(rawType, result.getRawClass());
        assertSame(bindings, result.getTypeBindings());
        assertSame(superClass, result.getSuperClass());
        assertSame(superInts, result.getInterfaces());
        assertSame(elemT, result.getElementType());
        assertEquals(null, result.getValueHandler());
        assertEquals(null, result.getTypeHandler());
        assertEquals(false, result.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        CollectionType collectionType = new CollectionType();
        
        CollectionType result = collectionType.withContentType(contentType);
        
        assertSame(collectionType.getRawClass(), result.getRawClass());
        assertSame(collectionType.getTypeBindings(), result.getTypeBindings());
        assertSame(collectionType.getSuperClass(), result.getSuperClass());
        assertSame(collectionType.getInterfaces(), result.getInterfaces());
        assertSame(contentType, result.getElementType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType collectionType = new CollectionType();
        
        CollectionType result = collectionType.withTypeHandler(typeHandler);
        
        assertSame(collectionType.getRawClass(), result.getRawClass());
        assertSame(collectionType.getTypeBindings(), result.getTypeBindings());
        assertSame(collectionType.getSuperClass(), result.getSuperClass());
        assertSame(collectionType.getInterfaces(), result.getInterfaces());
        assertSame(collectionType.getElementType(), result.getElementType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertSame(typeHandler, result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType contentType = new JavaType();
        CollectionType collectionType = new CollectionType().withContentType(contentType);
        
        CollectionType result = collectionType.withContentTypeHandler(typeHandler);
        
        assertSame(collectionType.getRawClass(), result.getRawClass());
        assertSame(collectionType.getTypeBindings(), result.getTypeBindings());
        assertSame(collectionType.getSuperClass(), result.getSuperClass());
        assertSame(collectionType.getInterfaces(), result.getInterfaces());
        assertSame(contentType.withTypeHandler(typeHandler), result.getElementType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType collectionType = new CollectionType();
        
        CollectionType result = collectionType.withValueHandler(valueHandler);
        
        assertSame(collectionType.getRawClass(), result.getRawClass());
        assertSame(collectionType.getTypeBindings(), result.getTypeBindings());
        assertSame(collectionType.getSuperClass(), result.getSuperClass());
        assertSame(collectionType.getInterfaces(), result.getInterfaces());
        assertSame(collectionType.getElementType(), result.getElementType());
        assertSame(valueHandler, result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        JavaType contentType = new JavaType();
        CollectionType collectionType = new CollectionType().withContentType(contentType);
        
        CollectionType result = collectionType.withContentValueHandler(valueHandler);
        
        assertSame(collectionType.getRawClass(), result.getRawClass());
        assertSame(collectionType.getTypeBindings(), result.getTypeBindings());
        assertSame(collectionType.getSuperClass(), result.getSuperClass());
        assertSame(collectionType.getInterfaces(), result.getInterfaces());
        assertSame(contentType.withValueHandler(valueHandler), result.getElementType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType();
        
        CollectionType result = collectionType.withStaticTyping();
        
        assertSame(collectionType.getRawClass(), result.getRawClass());
        assertSame(collectionType.getTypeBindings(), result.getTypeBindings());
        assertSame(collectionType.getSuperClass(), result.getSuperClass());
        assertSame(collectionType.getInterfaces(), result.getInterfaces());
        assertSame(collectionType.getElementType().withStaticTyping(), result.getElementType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertEquals(true, result.isStatic());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = { new JavaType() };
        JavaType elemT = new JavaType();
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Class<?> rawTypeRefine = CollectionType.class;
        TypeBindings bindingsRefine = new TypeBindings();
        JavaType superClassRefine = new JavaType();
        JavaType[] superIntsRefine = { new JavaType() };
        
        CollectionType result = collectionType.refine(rawTypeRefine, bindingsRefine, superClassRefine, superIntsRefine);
        
        assertSame(rawTypeRefine, result.getRawClass());
        assertSame(bindingsRefine, result.getTypeBindings());
        assertSame(superClassRefine, result.getSuperClass());
        assertSame(superIntsRefine, result.getInterfaces());
        assertSame(collectionType.getElementType(), result.getElementType());
        assertEquals(collectionType.getValueHandler(), result.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), result.getTypeHandler());
        assertEquals(collectionType.isStatic(), result.isStatic());
    }
}