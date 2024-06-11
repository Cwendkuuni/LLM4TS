package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    
    private ResolvedRecursiveType resolvedRecursiveType;
    private JavaType referencedType;
    
    @Before
    public void setUp() {
        referencedType = new JavaType() {
            
            @Override
            public StringBuilder getGenericSignature(StringBuilder sb) {
                return null;
            }
            
            @Override
            public StringBuilder getErasedSignature(StringBuilder sb) {
                return null;
            }
            
            @Override
            public JavaType withValueHandler(Object h) {
                return null;
            }
            
            @Override
            public JavaType withStaticTyping() {
                return null;
            }
            
            @Override
            public JavaType withTypeHandler(Object h) {
                return null;
            }
            
            @Override
            public JavaType withContentTypeHandler(Object h) {
                return null;
            }
            
            @Override
            public JavaType withContentType(JavaType contentType) {
                return null;
            }
            
            @Override
            public boolean isCollectionLikeType() {
                return false;
            }
            
            @Override
            public boolean isArrayType() {
                return false;
            }
        };
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }
    
    @Test
    public void testSetReference() {
        resolvedRecursiveType.setReference(referencedType);
        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        resolvedRecursiveType.setReference(referencedType);
        resolvedRecursiveType.setReference(new JavaType() {
            
            @Override
            public StringBuilder getGenericSignature(StringBuilder sb) {
                return null;
            }
            
            @Override
            public StringBuilder getErasedSignature(StringBuilder sb) {
                return null;
            }
            
            @Override
            public JavaType withValueHandler(Object h) {
                return null;
            }
            
            @Override
            public JavaType withStaticTyping() {
                return null;
            }
            
            @Override
            public JavaType withTypeHandler(Object h) {
                return null;
            }
            
            @Override
            public JavaType withContentTypeHandler(Object h) {
                return null;
            }
            
            @Override
            public JavaType withContentType(JavaType contentType) {
                return null;
            }
            
            @Override
            public boolean isCollectionLikeType() {
                return false;
            }
            
            @Override
            public boolean isArrayType() {
                return false;
            }
        });
    }
    
    @Test
    public void testGetGenericSignature() {
        assertNull(resolvedRecursiveType.getGenericSignature(new StringBuilder()));
    }
    
    @Test
    public void testGetErasedSignature() {
        assertNull(resolvedRecursiveType.getErasedSignature(new StringBuilder()));
    }
    
    @Test
    public void testWithContentType() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(referencedType));
    }
    
    @Test
    public void testWithTypeHandler() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(new Object()));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(new Object()));
    }
    
    @Test
    public void testWithValueHandler() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(new Object()));
    }
    
    @Test
    public void testWithStaticTyping() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }
    
    @Test
    public void testEqualsSameObject() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }
    
    @Test
    public void testEqualsNull() {
        assertFalse(resolvedRecursiveType.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClasses() {
        assertFalse(resolvedRecursiveType.equals(new Object()));
    }
    
    @Test
    public void testEqualsDifferentReferencedType() {
        assertFalse(resolvedRecursiveType.equals(new ResolvedRecursiveType(null, null)));
    }
    
    @Test
    public void testEqualsSameReferencedType() {
        resolvedRecursiveType.setReference(referencedType);
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        other.setReference(referencedType);
        assertTrue(resolvedRecursiveType.equals(other));
    }
}