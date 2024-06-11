package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructor() {
        SimpleType type = new SimpleType(String.class);
        
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isContainerType());
        assertTrue(type.getBindings().isEmpty());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isJavaLangObject());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
        assertFalse(type.isTypeOrSubTypeOf(String.class));
        assertTrue(type.isTypeOrSubTypeOf(Object.class));
    }
    
    @Test
    public void testConstructorWithBindings() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBoundType(String.class, bindings.createCollectionType(List.class));
        
        SimpleType type = new SimpleType(String.class, bindings, null, null);
        
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isContainerType());
        assertEquals(bindings, type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isJavaLangObject());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
        assertFalse(type.isTypeOrSubTypeOf(String.class));
        assertTrue(type.isTypeOrSubTypeOf(Object.class));
    }
    
    @Test
    public void testCopyConstructor() {
        SimpleType baseType = new SimpleType(String.class);
        SimpleType type = new SimpleType(baseType);
        
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isContainerType());
        assertTrue(type.getBindings().isEmpty());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isJavaLangObject());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
        assertFalse(type.isTypeOrSubTypeOf(String.class));
        assertTrue(type.isTypeOrSubTypeOf(Object.class));
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isContainerType());
        assertTrue(type.getBindings().isEmpty());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isJavaLangObject());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
        assertFalse(type.isTypeOrSubTypeOf(String.class));
        assertTrue(type.isTypeOrSubTypeOf(Object.class));
    }
    
    @Test
    @Deprecated
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isContainerType());
        assertTrue(type.getBindings().isEmpty());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isJavaLangObject());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
        assertFalse(type.isTypeOrSubTypeOf(String.class));
        assertTrue(type.isTypeOrSubTypeOf(Object.class));
    }
    
    @Test
    public void testNarrow() {
        SimpleType type = new SimpleType(Object.class);
        SimpleType narrowedType = (SimpleType) type.narrow(String.class);
        
        assertEquals(String.class, narrowedType.getRawClass());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isContainerType());
        assertTrue(narrowedType.getBindings().isEmpty());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertFalse(narrowedType.isJavaLangObject());
        assertFalse(narrowedType.isCollectionLikeType());
        assertFalse(narrowedType.isMapLikeType());
        assertFalse(narrowedType.isReferenceType());
        assertFalse(narrowedType.isTypeOrSubTypeOf(Object.class));
        assertTrue(narrowedType.isTypeOrSubTypeOf(String.class));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        SimpleType type = new SimpleType(Object.class);
        type.narrow(Integer.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType type = new SimpleType(String.class);
        type.withContentType(new SimpleType(Integer.class));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType type = new SimpleType(String.class);
        type.withContentTypeHandler(new Object());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType type = new SimpleType(String.class);
        type.withContentValueHandler(new Object());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(String.class);
        SimpleType staticType = type.withStaticTyping();
        
        assertEquals(String.class, staticType.getRawClass());
        assertNull(staticType.getValueHandler());
        assertNull(staticType.getTypeHandler());
        assertFalse(staticType.isContainerType());
        assertTrue(staticType.getBindings().isEmpty());
        assertNull(staticType.getSuperClass());
        assertNull(staticType.getSuperInterfaces());
        assertFalse(staticType.isJavaLangObject());
        assertFalse(staticType.isCollectionLikeType());
        assertFalse(staticType.isMapLikeType());
        assertFalse(staticType.isReferenceType());
        assertFalse(staticType.isTypeOrSubTypeOf(String.class));
        assertTrue(staticType.isTypeOrSubTypeOf(Object.class));
        assertTrue(staticType.isStaticTyping());
        assertFalse(type.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = new SimpleType(String.class);
        JavaType refinedType = type.refine(Object.class, null, null, null);
        
        assertNull(refinedType);
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = type.getErasedSignature(sb);
        
        assertEquals("Ljava/lang/String;", signature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = type.getGenericSignature(sb);
        
        assertEquals("Ljava/lang/String;", signature.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = new SimpleType(String.class);
        
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(String.class);
        SimpleType type2 = new SimpleType(String.class);
        
        assertTrue(type1.equals(type2));
        assertTrue(type2.equals(type1));
        
        type1 = new SimpleType(String.class);
        type2 = new SimpleType(Integer.class);
        
        assertFalse(type1.equals(type2));
        assertFalse(type2.equals(type1));
    }
}