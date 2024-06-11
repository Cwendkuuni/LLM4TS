package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertTrue(!simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertTrue(sb.toString().equals("Ljava/lang/String;"));
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertTrue(sb.toString().equals("Ljava/lang/String;"));
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType javaType = simpleType.withStaticTyping();
        assertTrue(javaType.toCanonical().equals("java.lang.String"));
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Exception exception = null;
        try {
            simpleType.withContentValueHandler(new Object());
        } catch (IllegalArgumentException e){
            exception = e;
        }
        assertTrue(exception != null);
    }
}