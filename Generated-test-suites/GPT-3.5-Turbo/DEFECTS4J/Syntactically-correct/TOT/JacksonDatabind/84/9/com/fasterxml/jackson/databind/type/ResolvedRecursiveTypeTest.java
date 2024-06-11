package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(getClass());
        
        // Normal case
        rrt.setReference(ref);
        assertEquals(ref, rrt.getSelfReferencedType());
        
        // Test re-set self reference
        try {
            rrt.setReference(ref);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + ref, e.getMessage());
        }
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        // Test toString when referencedType is null
        String expectedString = "[recursive type; UNRESOLVED]";
        assertEquals(expectedString, rrt.toString());
        
        JavaType ref = new JavaType(getClass());
        rrt.setReference(ref);
        
        // Test toString when referencedType is not null
        expectedString = "[recursive type; " + getClass().getName() + "]";
        assertEquals(expectedString, rrt.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        // Test when other object is null
        assertFalse(rrt1.equals(null));
        
        // Test when other object is not an instance of ResolvedRecursiveType
        assertFalse(rrt1.equals(new Object()));
        
        // Test when referencedType is null for both objects
        assertFalse(rrt1.equals(rrt2));
        
        // Test when referencedType is the same for both objects
        JavaType ref = new JavaType(getClass());
        rrt1.setReference(ref);
        rrt2.setReference(ref);
        assertTrue(rrt1.equals(rrt2));
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(getClass());
        rrt.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedString = new StringBuilder();
        
        // Test getGenericSignature when referencedType is not null
        ref.getGenericSignature(expectedString);
        assertEquals(expectedString, rrt.getGenericSignature(sb));
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(getClass());
        rrt.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedString = new StringBuilder();
        
        // Test getErasedSignature when referencedType is not null
        ref.getErasedSignature(expectedString);
        assertEquals(expectedString, rrt.getErasedSignature(sb));
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(getClass());
        rrt.setReference(ref);
        
        // Test withContentType method returns the same instance of ResolvedRecursiveType
        assertEquals(rrt, rrt.withContentType(null));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(getClass());
        rrt.setReference(ref);
        
        // Test withTypeHandler method returns the same instance of ResolvedRecursiveType
        assertEquals(rrt, rrt.withTypeHandler(null));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(getClass());
        rrt.setReference(ref);
        
        // Test withContentTypeHandler method returns the same instance of ResolvedRecursiveType
        assertEquals(rrt, rrt.withContentTypeHandler(null));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(getClass());
        rrt.setReference(ref);
        
        // Test withContentValueHandler method returns the same instance of ResolvedRecursiveType
        assertEquals(rrt, rrt.withContentValueHandler(null));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(getClass());
        rrt.setReference(ref);
        
        // Test withStaticTyping method returns the same instance of ResolvedRecursiveType
        assertEquals(rrt, rrt.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(getClass());
        rrt.setReference(ref);
        
        // Test narrow method returns the same instance of ResolvedRecursiveType
        assertEquals(rrt, rrt._narrow(Object.class));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(getClass());
        rrt.setReference(ref);
        
        // Test refine method returns null
        assertNull(rrt.refine(Object.class, new TypeBindings(), null, null));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(getClass());
        rrt.setReference(ref);
        
        // Test isContainerType method returns false
        assertFalse(rrt.isContainerType());
    }

}