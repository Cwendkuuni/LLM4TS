package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        // Typical Use Case: Construct a ReferenceType object with valid parameters
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertNotNull(referenceType);
        assertEquals(cls, referenceType._class);
        assertEquals(refType.hashCode(), referenceType._hashCode);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }
    
    @Test
    public void testWithStaticTyping() {
        // Typical Use Case: Return a new ReferenceType object with static typing
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        
        assertNotNull(newReferenceType);
        assertEquals(cls, newReferenceType._class);
        assertEquals(refType.withStaticTyping(), newReferenceType._referencedType);
        assertEquals(valueHandler, newReferenceType._valueHandler);
        assertEquals(typeHandler, newReferenceType._typeHandler);
        assertTrue(newReferenceType._asStatic);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        // Typical Use Case: Return a new ReferenceType object with a new content type handler
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        Object valueHandler = null;
        Object typeHandler = null;
        Object newContentTypeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
        
        assertNotNull(newReferenceType);
        assertEquals(cls, newReferenceType._class);
        
        JavaType expectedRefType = refType.withTypeHandler(newContentTypeHandler);
        assertEquals(expectedRefType, newReferenceType._referencedType);
        
        assertEquals(valueHandler, newReferenceType._valueHandler);
        assertEquals(typeHandler, newReferenceType._typeHandler);
        assertFalse(newReferenceType._asStatic);
    }
    
    @Test
    public void testWithContentValueHandler() {
        // Typical Use Case: Return a new ReferenceType object with a new content value handler
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        Object valueHandler = null;
        Object typeHandler = null;
        Object newContentValueHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
        
        assertNotNull(newReferenceType);
        assertEquals(cls, newReferenceType._class);
        
        JavaType expectedRefType = refType.withValueHandler(newContentValueHandler);
        assertEquals(expectedRefType, newReferenceType._referencedType);
        
        assertEquals(valueHandler, newReferenceType._valueHandler);
        assertEquals(typeHandler, newReferenceType._typeHandler);
        assertFalse(newReferenceType._asStatic);
    }
    
    @Test
    public void testWithParameterSource() {
        // Typical Use Case: Return the parameter source class of the ReferenceType object
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        Class<?> parameterSource = referenceType.getParameterSource();
        
        assertEquals(cls, parameterSource);
    }
    
    @Test
    public void testContainedTypeCount() {
        // Typical Use Case: Return the number of contained types in the ReferenceType object
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        int containedTypeCount = referenceType.containedTypeCount();
        
        assertEquals(1, containedTypeCount);
    }
    
    @Test
    public void testContainedType() {
        // Typical Use Case: Return the contained type at a given index of the ReferenceType object
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        Object valueHandler = null;
        Object typeHandler = null;
        int index = 0;
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        JavaType containedType = referenceType.containedType(index);
        
        assertEquals(refType, containedType);
    }
    
    @Test
    public void testToString() {
        // Typical Use Case: Return a string representation of the ReferenceType object
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        String stringRepresentation = referenceType.toString();
        
        String expectedString = "[reference type, class " + referenceType.buildCanonicalName() + "<" + refType.toString() + ">]";
        assertEquals(expectedString, stringRepresentation);
    }
    
    @Test
    public void testEquals() {
        // Typical Use Case: Return true if the given object is equal to the ReferenceType object, otherwise return false
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(cls);
        Object valueHandler = null;
        Object typeHandler = null;
        
        ReferenceType referenceType1 = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType referenceType2 = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType referenceType3 = new ReferenceType(Object.class, refType, valueHandler, typeHandler, false);
        ReferenceType referenceType4 = new ReferenceType(cls, refType, valueHandler, typeHandler, true);
        
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals("string"));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType4));
    }

}