package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CollectionTypeTest {
    
    private CollectionType collectionType;
    
    @Before
    public void setUp() {
        collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
    }
    
    @Test
    public void testConstruct() {
        CollectionType type = CollectionType.construct(HashSet.class, new TypeBindings(), null, null, Integer.class);
        
        assertEquals(HashSet.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertEquals(Integer.class, type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
    }
    
    @Test
    public void testNarrow() {
        JavaType narrowedType = collectionType._narrow(Vector.class);
        
        assertEquals(Vector.class, narrowedType.getRawClass());
        assertEquals(collectionType.getBindings(), narrowedType.getBindings());
        assertEquals(collectionType.getSuperClass(), narrowedType.getSuperClass());
        assertEquals(collectionType.getSuperInterfaces(), narrowedType.getSuperInterfaces());
        assertEquals(collectionType.getContentType(), narrowedType.getContentType());
        assertEquals(collectionType.getValueHandler(), narrowedType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), narrowedType.getTypeHandler());
        assertEquals(collectionType.isStatic(), narrowedType.isStatic());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(Long.class);
        CollectionType updatedType = collectionType.withContentType(contentType);
        
        assertEquals(updatedType.getRawClass(), collectionType.getRawClass());
        assertEquals(updatedType.getBindings(), collectionType.getBindings());
        assertEquals(updatedType.getSuperClass(), collectionType.getSuperClass());
        assertEquals(updatedType.getSuperInterfaces(), collectionType.getSuperInterfaces());
        assertEquals(contentType, updatedType.getContentType());
        assertEquals(updatedType.getValueHandler(), collectionType.getValueHandler());
        assertEquals(updatedType.getTypeHandler(), collectionType.getTypeHandler());
        assertEquals(updatedType.isStatic(), collectionType.isStatic());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType updatedType = collectionType.withTypeHandler(typeHandler);
        
        assertEquals(updatedType.getRawClass(), collectionType.getRawClass());
        assertEquals(updatedType.getBindings(), collectionType.getBindings());
        assertEquals(updatedType.getSuperClass(), collectionType.getSuperClass());
        assertEquals(updatedType.getSuperInterfaces(), collectionType.getSuperInterfaces());
        assertEquals(updatedType.getContentType(), collectionType.getContentType());
        assertEquals(updatedType.getValueHandler(), collectionType.getValueHandler());
        assertEquals(typeHandler, updatedType.getTypeHandler());
        assertEquals(updatedType.isStatic(), collectionType.isStatic());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        CollectionType updatedType = collectionType.withContentTypeHandler(typeHandler);
        
        assertEquals(updatedType.getRawClass(), collectionType.getRawClass());
        assertEquals(updatedType.getBindings(), collectionType.getBindings());
        assertEquals(updatedType.getSuperClass(), collectionType.getSuperClass());
        assertEquals(updatedType.getSuperInterfaces(), collectionType.getSuperInterfaces());
        assertEquals(updatedType.getContentType().getTypeHandler(), typeHandler);
        assertEquals(updatedType.getValueHandler(), collectionType.getValueHandler());
        assertEquals(updatedType.getTypeHandler(), collectionType.getTypeHandler());
        assertEquals(updatedType.isStatic(), collectionType.isStatic());
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType updatedType = collectionType.withValueHandler(valueHandler);
        
        assertEquals(updatedType.getRawClass(), collectionType.getRawClass());
        assertEquals(updatedType.getBindings(), collectionType.getBindings());
        assertEquals(updatedType.getSuperClass(), collectionType.getSuperClass());
        assertEquals(updatedType.getSuperInterfaces(), collectionType.getSuperInterfaces());
        assertEquals(updatedType.getContentType(), collectionType.getContentType());
        assertEquals(valueHandler, updatedType.getValueHandler());
        assertEquals(updatedType.getTypeHandler(), collectionType.getTypeHandler());
        assertEquals(updatedType.isStatic(), collectionType.isStatic());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        CollectionType updatedType = collectionType.withContentValueHandler(valueHandler);
        
        assertEquals(updatedType.getRawClass(), collectionType.getRawClass());
        assertEquals(updatedType.getBindings(), collectionType.getBindings());
        assertEquals(updatedType.getSuperClass(), collectionType.getSuperClass());
        assertEquals(updatedType.getSuperInterfaces(), collectionType.getSuperInterfaces());
        assertEquals(updatedType.getContentType().getValueHandler(), valueHandler);
        assertEquals(updatedType.getValueHandler(), collectionType.getValueHandler());
        assertEquals(updatedType.getTypeHandler(), collectionType.getTypeHandler());
        assertEquals(updatedType.isStatic(), collectionType.isStatic());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType updatedType = collectionType.withStaticTyping();
        
        assertEquals(updatedType.getRawClass(), collectionType.getRawClass());
        assertEquals(updatedType.getBindings(), collectionType.getBindings());
        assertEquals(updatedType.getSuperClass(), collectionType.getSuperClass());
        assertEquals(updatedType.getSuperInterfaces(), collectionType.getSuperInterfaces());
        assertEquals(updatedType.getContentType().withStaticTyping(), collectionType.getContentType());
        assertEquals(updatedType.getValueHandler(), collectionType.getValueHandler());
        assertEquals(updatedType.getTypeHandler(), collectionType.getTypeHandler());
        assertTrue(updatedType.isStatic());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = LinkedList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = collectionType.getContentType();
        Object valueHandler = collectionType.getValueHandler();
        Object typeHandler = collectionType.getTypeHandler();
        boolean asStatic = collectionType.isStatic();
        
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInterfaces, refinedType.getSuperInterfaces());
        assertEquals(elementType, refinedType.getContentType());
        assertEquals(valueHandler, refinedType.getValueHandler());
        assertEquals(typeHandler, refinedType.getTypeHandler());
        assertEquals(asStatic, refinedType.isStatic());
    }
    
    @Test
    public void testToString() {
        assertEquals("[collection type; class java.util.ArrayList, contains java.lang.String]", collectionType.toString());
    }   
}