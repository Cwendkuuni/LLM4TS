
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.Assert;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Assert.assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        // Basic test
        SimpleType simpleType = SimpleType.construct(Integer.class);
        Assert.assertEquals(Integer.class, simpleType.getRawClass());
        
        // Exception handling scenario
        try {
            SimpleType.construct(Map.class);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            SimpleType.construct(List.class);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            SimpleType.construct(String[].class);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        
        // Exception handling scenario
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        
        // Basic test
        Object handler = new Object();
        SimpleType newType = simpleType.withTypeHandler(handler);
        Assert.assertEquals(handler, newType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        
        // Exception handling scenario
        try {
            simpleType.withContentTypeHandler(new Object());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        
        // Basic test
        SimpleType newType = simpleType.withStaticTyping();
        Assert.assertTrue(newType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        
        // SimpleType cannot be refined
        Assert.assertNull(simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null));
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        
        // SimpleType is not a container type
        Assert.assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        
        // Basic test
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        Assert.assertTrue(sb.toString().contains("java.lang.Integer"));
        Assert.assertEquals(sb, erasedSignature);
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        
        // Basic test
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        Assert.assertTrue(sb.toString().contains("java.lang.Integer"));
        Assert.assertEquals(sb, genericSignature);
    }
    
}
