package com.fasterxml.jackson.databind.type; // Import necessary classes for testing

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeReference;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeWithTypeHandlers() {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = TypeReference.type(intType);
        JavaType referencedType = TypeReference.type(String.class);

        ReferenceType referenceType = ReferenceType.construct(intType, referencedType, null, null);

        // Test withTypeHandler
        Object newTypeHandler = new Object();
        ReferenceType withTypeHandler = referenceType.withTypeHandler(newTypeHandler);
        assertEquals(newTypeHandler, withTypeHandler.getTypeHandler());

        // Test withContentTypeHandler
        Object newContentTypeHandler = new Object();
        ReferenceType withContentTypeHandler = withTypeHandler.withContentTypeHandler(newContentTypeHandler);
        assertEquals(newContentTypeHandler, withContentTypeHandler.getReferencedType().getTypeHandler());

        // Test withValueHandler
        Object newValueHandler = new Object();
        ReferenceType withValueHandler = withContentTypeHandler.withValueHandler(newValueHandler);
        assertEquals(newValueHandler, withValueHandler.getValueHandler());

        // Test withContentValueHandler
        Object newContentValueHandler = new Object();
        ReferenceType withContentValueHandler = withValueHandler.withContentValueHandler(newContentValueHandler);
        assertEquals(newContentValueHandler, withContentValueHandler.getReferencedType().getValueHandler());

        // Test withStaticTyping
        ReferenceType withStaticTyping = withContentValueHandler.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());

        // Test equals
        ReferenceType sameType = withStaticTyping;
        assertTrue(sameType.equals(withStaticTyping));

        // Test not equals with different classes
        ReferenceType other = new ReferenceType(Integer.class, javaIntType, null, null);
        assertFalse(sameType.equals(other));

        // Test not equals with different referenced types
        JavaType differentRefType = TypeReference.type(Double.class);
        ReferenceType otherWithDifferentRefType = new ReferenceType(intType, differentRefType, null, null);
        assertFalse(sameType.equals(otherWithDifferentRefType));
    }

    @Test
    public void testNarrow() {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = TypeReference.type(intType);
        JavaType referencedType = TypeReference.type(String.class);

        ReferenceType intTypeRef = ReferenceType.construct(intType, referencedType, null, null);

        Class<Long> longType = Long.class;
        JavaType longTypeRef = new ReferenceType(longType, javaIntType, null, null);

        ReferenceType narrowedType = intTypeRef._narrow(longType);
        assertEquals(longTypeRef, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<Integer> intType = Integer.class;
        JavaType javaIntType = TypeReference.type(intType);
        JavaType referencedType = TypeReference.type(String.class);

        ReferenceType intTypeRef = ReferenceType.construct(intType, referencedType, null, null);

        Class<Double> doubleType = Double.class;
        ReferenceType narrowedType = intTypeRef._narrow(doubleType);
    }
}