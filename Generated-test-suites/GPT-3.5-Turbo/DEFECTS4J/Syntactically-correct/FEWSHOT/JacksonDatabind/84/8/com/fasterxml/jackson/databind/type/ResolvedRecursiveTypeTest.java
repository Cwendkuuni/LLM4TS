
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        JavaType javaType = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(javaType.getSelfReferencedType());
        javaType.setReference(null);
        assertNull(javaType.getSelfReferencedType());
        
        JavaType referencedType = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        javaType.setReference(referencedType);
        assertEquals(referencedType, javaType.getSelfReferencedType());
        
        try {
            JavaType duplicateReferencedType = new ResolvedRecursiveType(Float.class, new TypeBindings());
            javaType.setReference(duplicateReferencedType);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Trying to re-set self reference; old value = " + referencedType + ", new = " + duplicateReferencedType, e.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        // TODO: Test getGenericSignature method
    }
    
    @Test
    public void testGetErasedSignature() {
        // TODO: Test getErasedSignature method
    }
    
    @Test
    public void testWithContentType() {
        JavaType javaType = new ResolvedRecursiveType(Long.class, new TypeBindings());
        assertEquals(javaType, javaType.withContentType(null));
        assertEquals(javaType, javaType.withContentType(new ResolvedRecursiveType(Double.class, new TypeBindings())));        
    }
    
    @Test
    public void testWithTypeHandler() {
        JavaType javaType = new ResolvedRecursiveType(Boolean.class, new TypeBindings());
        assertEquals(javaType, javaType.withTypeHandler(null));
        assertEquals(javaType, javaType.withTypeHandler(new Object()));        
    }
    
    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = new ResolvedRecursiveType(Byte.class, new TypeBindings());
        assertEquals(javaType, javaType.withContentTypeHandler(null));
        assertEquals(javaType, javaType.withContentTypeHandler(new Object()));        
    }
    
    @Test
    public void testWithValueHandler() {
        JavaType javaType = new ResolvedRecursiveType(Short.class, new TypeBindings());
        assertEquals(javaType, javaType.withValueHandler(null));
        assertEquals(javaType, javaType.withValueHandler(new Object()));        
    }
    
    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = new ResolvedRecursiveType(Character.class, new TypeBindings());
        assertEquals(javaType, javaType.withContentValueHandler(null));
        assertEquals(javaType, javaType.withContentValueHandler(new Object()));        
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new ResolvedRecursiveType(Void.class, new TypeBindings());
        assertEquals(javaType, javaType.withStaticTyping());        
    }
    
    @Test
    public void testNarrow() {
        JavaType javaType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals(javaType, javaType._narrow(String.class));        
    }
    
    @Test
    public void testRefine() {
        JavaType javaType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertNull(javaType.refine(Integer.class, new TypeBindings(), null, new JavaType[0]));        
    }
    
    @Test
    public void testIsContainerType() {
        JavaType javaType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertFalse(javaType.isContainerType());       
    }
    
    @Test
    public void testToString() {
        // TODO: Test toString method
    }
    
    @Test
    public void testEquals() {
        // TODO: Test equals method
    }
}
