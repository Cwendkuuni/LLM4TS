package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    
    private SimpleType simpleType;
    
    @Before
    public void setUp() {
        simpleType = new SimpleType(String.class);
    }
    
    @Test
    public void testConstructor() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        SimpleType type = new SimpleType(String.class, bindings, superClass, superInterfaces);
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertArrayEquals(superInterfaces, type.getInterfaces());
        assertEquals(valueHandler, type.getValueHandler());
        assertEquals(typeHandler, type.getTypeHandler());
        assertEquals(asStatic, type.isStatic());
    }
    
    @Test
    public void testCopyConstructor() {
        TypeBase base = new SimpleType(String.class);
        SimpleType type = new SimpleType(base);
        assertEquals(base, type);
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(String[].class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
        
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(String.class, type.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), type.getBindings());
        assertNotNull(type.getSuperClass());
        assertEquals(0, type.getInterfaces().length);
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        JavaType narrowType = simpleType._narrow(subclass);
        assertEquals(subclass, narrowType.getRawClass());
        assertNotEquals(simpleType, narrowType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        simpleType.withContentType(null);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        SimpleType type = simpleType.withContentTypeHandler(handler);
        assertEquals(simpleType, type);
    }
    
    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        SimpleType type = simpleType.withValueHandler(handler);
        assertEquals(handler, type.getValueHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        simpleType.withContentValueHandler(null);
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = simpleType.withStaticTyping();
        assertTrue(type.isStatic());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType refineType = simpleType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(refineType);
    }
    
    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", result.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", result.toString());
    }
    
    @Test
    public void testEquals() {
        assertTrue(simpleType.equals(simpleType));
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals(new String()));
        
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        TypeBindings bindings2 = new TypeBindings();
        SimpleType type1 = new SimpleType(String.class, bindings1, null, null);
        SimpleType type2 = new SimpleType(String.class, bindings1, null, null);
        SimpleType type3 = new SimpleType(String.class, bindings2, null, null);
        SimpleType type4 = new SimpleType(Integer.class, bindings1, null, null);
        
        assertTrue(type1.equals(type2));
        assertTrue(type2.equals(type1));
        assertFalse(type1.equals(type3));
        assertFalse(type3.equals(type1));
        assertFalse(type1.equals(type4));
        assertFalse(type4.equals(type1));
    }
}