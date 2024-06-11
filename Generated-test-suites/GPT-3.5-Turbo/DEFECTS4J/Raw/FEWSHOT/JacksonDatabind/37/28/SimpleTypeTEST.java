package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class SimpleTypeTest {
    private SimpleType simpleType;
    
    @Before
    public void setup() {
        simpleType = new SimpleType(TypeBase.class);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(TypeBase.class, simpleType.getRawClass());
        assertFalse(simpleType.hasContentType());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(String.class);
        JavaType newType = simpleType.withContentType(contentType);
        assertEquals(contentType, newType.getContentType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeException() {
        simpleType.withContentTypeHandler(new Object());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerException() {
        simpleType.withContentTypeHandler(new Object());
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType newType = simpleType.withStaticTyping();
        assertEquals(true, newType.isJavaLangObjectType());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        JavaType newType = simpleType.withContentTypeHandler(new Object());
        assertEquals(new Object(), newType.getContentTypeHandler());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerException() {
        simpleType.withContentValueHandler(new Object());
    }
    
    @Test
    public void testWithGenericType() {
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[1];
        JavaType[] newSuperInterfaces = simpleType.withGenericType(superClass, superInterfaces);
        assertEquals(superClass, newSuperInterfaces[0]);
    }
    
    @Test
    public void testEquals() {
        JavaType type1 = new SimpleType(TypeBase.class);
        JavaType type2 = new SimpleType(TypeBase.class);
        assertEquals(true, type1.equals(type2));
    }
    
    @Test
    public void testBuildCanonicalName() {
        JavaType type = new SimpleType(String.class);
        assertEquals("java.util.String", type.buildCanonicalName());
    }
    
    @Test
    public void testToString() {
        JavaType type = new SimpleType(String.class);
        assertEquals("[simple type, class java.util.String]", type.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        
        assertEquals("Lcom/fasterxml/jackson/databind/type/deser/impl/TypeBase;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        
        assertEquals("Lcom/fasterxml/jackson/databind/type/deser/impl/TypeBase;;", sb.toString());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(TypeBase.class);
        assertEquals(TypeBase.class, type.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructMapException() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollectionException() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructArrayException() {
        SimpleType.construct(Object[].class);
    }
}