package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;
    
    @Before
    public void setUp() {
        collectionType = new CollectionType(ArrayList.class, null, null, null, null,
                null, null, false);
    }
    
    @Test
    public void testConstructor() {
        // Test constructor with all parameters
        CollectionType ct1 = new CollectionType(LinkedList.class, new TypeBindings(),
                new SimpleType(), new SimpleType[]{new SimpleType()}, new SimpleType(),
                null, null, false);
        assertEquals(LinkedList.class, ct1.getRawClass());
        assertNull(ct1.getBindings());
        assertEquals(new SimpleType(), ct1.getSuperClass());
        assertArrayEquals(new SimpleType[]{new SimpleType()}, ct1.getSuperInterfaces());
        assertEquals(new SimpleType(), ct1.getContentType());
        assertNull(ct1.getValueHandler());
        assertNull(ct1.getTypeHandler());
        assertFalse(ct1.isStatic());
        
        // Test constructor with only base and contentType
        CollectionType ct2 = new CollectionType(new SimpleType(), new SimpleType());
        assertEquals(new SimpleType(), ct2.getContentType());
        assertNull(ct2.getBindings());
        assertNull(ct2.getSuperClass());
        assertNull(ct2.getSuperInterfaces());
        assertNull(ct2.getValueHandler());
        assertNull(ct2.getTypeHandler());
        assertFalse(ct2.isStatic());
    }

    @Test
    public void testConstruct() {
        CollectionType ct1 = CollectionType.construct(ArrayList.class, null, null, null, null);
        assertEquals(ArrayList.class, ct1.getRawClass());
        assertNull(ct1.getBindings());
        assertNull(ct1.getSuperClass());
        assertNull(ct1.getSuperInterfaces());
        assertNull(ct1.getContentType());
        assertNull(ct1.getValueHandler());
        assertNull(ct1.getTypeHandler());
        assertFalse(ct1.isStatic());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        CollectionType ct1 = collectionType.withTypeHandler(handler);
        assertEquals(ArrayList.class, ct1.getRawClass());
        assertNull(ct1.getBindings());
        assertNull(ct1.getSuperClass());
        assertNull(ct1.getSuperInterfaces());
        assertNull(ct1.getContentType());
        assertNull(ct1.getValueHandler());
        assertEquals(handler, ct1.getTypeHandler());
        assertFalse(ct1.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        CollectionType ct1 = collectionType.withContentTypeHandler(handler);
        assertEquals(ArrayList.class, ct1.getRawClass());
        assertNull(ct1.getBindings());
        assertNull(ct1.getSuperClass());
        assertNull(ct1.getSuperInterfaces());
        assertNull(ct1.getContentType());
        assertNull(ct1.getValueHandler());
        assertNull(ct1.getTypeHandler());
        assertFalse(ct1.isStatic());
        assertEquals(handler, ct1.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        CollectionType ct1 = collectionType.withValueHandler(handler);
        assertEquals(ArrayList.class, ct1.getRawClass());
        assertNull(ct1.getBindings());
        assertNull(ct1.getSuperClass());
        assertNull(ct1.getSuperInterfaces());
        assertNull(ct1.getContentType());
        assertEquals(handler, ct1.getValueHandler());
        assertNull(ct1.getTypeHandler());
        assertFalse(ct1.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        CollectionType ct1 = collectionType.withContentValueHandler(handler);
        assertEquals(ArrayList.class, ct1.getRawClass());
        assertNull(ct1.getBindings());
        assertNull(ct1.getSuperClass());
        assertNull(ct1.getSuperInterfaces());
        assertNull(ct1.getContentType());
        assertNull(ct1.getValueHandler());
        assertNull(ct1.getTypeHandler());
        assertFalse(ct1.isStatic());
        assertEquals(handler, ct1.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType ct1 = collectionType.withStaticTyping();
        assertEquals(ArrayList.class, ct1.getRawClass());
        assertNull(ct1.getBindings());
        assertNull(ct1.getSuperClass());
        assertNull(ct1.getSuperInterfaces());
        assertNull(ct1.getContentType());
        assertNull(ct1.getValueHandler());
        assertNull(ct1.getTypeHandler());
        assertTrue(ct1.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = LinkedList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType();
        JavaType[] superInterfaces = new JavaType[]{new SimpleType()};
        JavaType elemT = new SimpleType();
        
        CollectionType ct1 = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(LinkedList.class, ct1.getRawClass());
        assertEquals(bindings, ct1.getBindings());
        assertEquals(superClass, ct1.getSuperClass());
        assertArrayEquals(superInterfaces, ct1.getSuperInterfaces());
        assertEquals(elemT, ct1.getContentType());
        assertNull(ct1.getValueHandler());
        assertNull(ct1.getTypeHandler());
        assertFalse(ct1.isStatic());
    }

    @Test
    public void testToString() {
        String str = collectionType.toString();
        assertEquals("[collection type; class java.util.ArrayList, contains null]", str);
    }
}