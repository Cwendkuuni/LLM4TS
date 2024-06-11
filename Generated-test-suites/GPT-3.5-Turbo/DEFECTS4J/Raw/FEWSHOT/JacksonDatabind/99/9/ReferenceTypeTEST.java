package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {
    @Test
    public void testConstructor() {
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);
        
        assertEquals(TestClass.class, referenceType.getRawClass());
        assertNull(referenceType.getBindings());
        assertNull(referenceType.getSuperClass());
        assertNull(referenceType.getInterfaces());
        assertNull(referenceType.getContentType());
        assertNull(referenceType.getTypeHandler());
        assertNull(referenceType.getValueHandler());
        assertFalse(referenceType.isContainerType());
        assertFalse(referenceType.isArrayType());
        assertFalse(referenceType.isEnumType());
        assertFalse(referenceType.isPrimitive());
        assertFalse(referenceType.isInterface());
        assertFalse(referenceType.isFinal());
        assertFalse(referenceType.hasContentType());
        assertFalse(referenceType.isReferenceType());
    }

    @Test
    public void testWithContentType() {
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);
        JavaType contentType = new SimpleType(TestClass.class);
        ReferenceType updatedType = referenceType.withContentType(contentType);
        
        assertEquals(contentType, updatedType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);
        Object typeHandler = new Object();
        ReferenceType updatedType = referenceType.withTypeHandler(typeHandler);
        
        assertEquals(typeHandler, updatedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);
        Object contentTypeHandler = new Object();
        ReferenceType updatedType = referenceType.withContentTypeHandler(contentTypeHandler);
        
        assertEquals(contentTypeHandler, updatedType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);
        ReferenceType updatedType = referenceType.withStaticTyping();
        
        assertTrue(updatedType.isStatic());
    }

    @Test
    public void testRefine() {
        ReferenceType baseType = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);
        ReferenceType refinedType = (ReferenceType) baseType.refine(TestClass.class, null, null, null);
        
        assertEquals(TestClass.class, refinedType.getRawClass());
    }
    
    @Test
    public void testGetContentType() {
        JavaType contentType = new SimpleType(TestClass.class);
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, contentType, null, null, null, false);
        
        assertEquals(contentType, referenceType.getContentType());
    }
    
    @Test
    public void testGetReferencedType() {
        JavaType referencedType = new SimpleType(TestClass.class);
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, referencedType, null, null, null, false);
        
        assertEquals(referencedType, referenceType.getReferencedType());
    }
    
    @Test
    public void testHasContentType() {
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);
        
        assertFalse(referenceType.hasContentType());
    }
    
    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, null, new SimpleType(TestClass.class), null, null, false);
        
        assertEquals(TestClass.class, referenceType.getAnchorType().getRawClass());
    }
    
    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);
        
        assertTrue(referenceType.isAnchorType());
    }
    
    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, new SimpleType(TestClass.class), null, null, null, false);
        
        assertEquals("com.fasterxml.jackson.databind.type.ReferenceType<com.fasterxml.jackson.databind.type.SimpleType>", referenceType.buildCanonicalName());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(TestClass.class, null, null, null, new SimpleType(TestClass.class), null, null, null, false);
        
        assertEquals("[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<com.fasterxml.jackson.databind.type.SimpleType>]", referenceType.toString());
    }
    
    @Test
    public void testEquals() {
        ReferenceType referenceType1 = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertTrue(referenceType1.equals(referenceType2));
        
        referenceType1 = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);
        referenceType2 = new ReferenceType(SubTestClass.class, null, null, null, null, null, null, null, false);
        assertFalse(referenceType1.equals(referenceType2));
        
        referenceType1 = new ReferenceType(TestClass.class, null, null, null, null, null, null, null, false);
        referenceType2 = new ReferenceType(TestClass.class, new TypeBindings(), null, null, null, null, null, null, false);
        assertFalse(referenceType1.equals(referenceType2));
        
        referenceType1 = new ReferenceType(TestClass.class, null, null, null, new SimpleType(TestClass.class), null, null, null, false);
        referenceType2 = new ReferenceType(TestClass.class, null, null, null, new SimpleType(SubTestClass.class), null, null, null, false);
        assertFalse(referenceType1.equals(referenceType2));
    }
    
    private static class TestClass { }
    
    private static class SubTestClass extends TestClass { }
}