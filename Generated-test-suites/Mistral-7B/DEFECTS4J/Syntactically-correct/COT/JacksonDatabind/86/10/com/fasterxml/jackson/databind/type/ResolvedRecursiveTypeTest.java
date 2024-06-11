package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import org.junit.Test;

/**
 * Test class for ResolvedRecursiveType
 */
public class ResolvedRecursiveTypeTest {

    // Test constructor
    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertSame(erasedType, type.getRawClass());
        assertNull(type.getSelfReferencedType());
    }

    // Test setReference method
    @Test
    public void testSetReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String[].class);
        type.setReference(refType);
        assertSame(refType, type.getSelfReferencedType());
    }

    // Test getSelfReferencedType method
    @Test
    public void testGetSelfReferencedType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new SimpleType(String[].class);
        type.setReference(refType);
        JavaType selfReferencedType = type.getSelfReferencedType();
        assertSame(refType, selfReferencedType);
    }

    // Test getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        // TODO: Verify the generated signature as per the class implementation
    }

    // Test getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        // TODO: Verify the generated signature as per the class implementation
    }

    // Test setReference with existing reference
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceWithExistingReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType1 = new SimpleType(String[].class);
        JavaType refType2 = new SimpleType(Integer[].class);
        type.setReference(refType1);
        type.setReference(refType2);
    }

}