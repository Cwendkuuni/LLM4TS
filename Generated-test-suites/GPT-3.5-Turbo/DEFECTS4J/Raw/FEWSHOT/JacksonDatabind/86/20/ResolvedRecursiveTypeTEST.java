package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    @Test
    public void testConstructor() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, bindings);
        
        assertEquals(ResolvedRecursiveType.class, type.getRawClass());
        assertEquals(bindings, type.getTypeBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getInterfaces());
        assertEquals(0, type.getModifiers());
        assertNull(type.getGenericSignature(new StringBuilder()));
        assertNull(type.getErasedSignature(new StringBuilder()));
        assertNull(type.withContentType(null));
        assertNull(type.withTypeHandler(null));
        assertNull(type.withContentTypeHandler(null));
        assertNull(type.withValueHandler(null));
        assertNull(type.withContentValueHandler(null));
        assertNull(type.withStaticTyping());
        assertNull(type._narrow(String.class));
        assertNull(type.refine(null, null, null, null));
        assertFalse(type.isContainerType());
        assertEquals("[recursive type; UNRESOLVED]", type.toString());
        assertTrue(type.equals(type));
        assertFalse(type.equals(null));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceWithExistingReference() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, bindings);
        
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        
        type.setReference(ref1);
        type.setReference(ref2);
    }
    
    @Test
    public void testGetSelfReferencedType() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, bindings);
        
        JavaType ref = new JavaType();
        type.setReference(ref);
        
        assertEquals(ref, type.getSelfReferencedType());
    }
}