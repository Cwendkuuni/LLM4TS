package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType superClass = new TypeReference<Object>() {}.getType();
        JavaType referencedType = new TypeReference<String>() {}.getType();
        JavaType anchorType = new TypeReference<Integer>() {}.getType();

        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], referencedType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertEquals(new JavaType[0], referenceType.getSuperInterfaces());
        assertEquals(referencedType, referenceType.getContentType());
        assertEquals(null, referenceType.getAnchorType());
        assertEquals(false, referenceType.isAnchorType());
    }

    @Test
    public void testReferenceTypeUpgrade() {
        JavaType baseType = new TypeReference<Integer>() {}.getType();
        JavaType referencedType = new TypeReference<String>() {}.getType();

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, referencedType);

        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        assertEquals(null, referenceType.getSuperClass());
        assertEquals(new JavaType[0], referenceType.getSuperInterfaces());
        assertEquals(referencedType, referenceType.getContentType());
        assertEquals(referenceType, referenceType.getAnchorType());
        assertEquals(true, referenceType.isAnchorType());
    }

    @Test
    public void testReferenceTypeWithContentType() {
        JavaType superClass = new TypeReference<Object>() {}.getType();
        JavaType referencedType = new TypeReference<String>() {}.getType();
        JavaType newContentType = new TypeReference<Integer>() {}.getType();

        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], referencedType);

        JavaType newReferenceType = referenceType.withContentType(newContentType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertEquals(new JavaType[0], referenceType.getSuperInterfaces());
        assertEquals(referencedType, referenceType.getContentType());
        assertEquals(referenceType, referenceType.getAnchorType());
        assertEquals(true, referenceType.isAnchorType());

        assertEquals(Integer.class, newReferenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newReferenceType.getTypeBindings());
        assertEquals(superClass, newReferenceType.getSuperClass());
        assertEquals(new JavaType[0], newReferenceType.getSuperInterfaces());
        assertEquals(newContentType, newReferenceType.getContentType());
        assertEquals(newReferenceType, newReferenceType.getAnchorType());
        assertEquals(true, newReferenceType.isAnchorType());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        JavaType superClass = new TypeReference<Object>() {}.getType();
        JavaType referencedType = new TypeReference<String>() {}.getType();
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], referencedType);

        JavaType newReferenceType = referenceType.withTypeHandler(typeHandler);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertEquals(new JavaType[0], referenceType.getSuperInterfaces());
        assertEquals(referencedType, referenceType.getContentType());
        assertEquals(referenceType, referenceType.getAnchorType());
        assertEquals(true, referenceType.isAnchorType());

        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newReferenceType.getTypeBindings());
        assertEquals(superClass, newReferenceType.getSuperClass());
        assertEquals(new JavaType[0], newReferenceType.getSuperInterfaces());
        assertEquals(referencedType, newReferenceType.getContentType());
        assertEquals(newReferenceType, newReferenceType.getAnchorType());
        assertEquals(true, newReferenceType.isAnchorType());
        assertEquals(typeHandler, newReferenceType.<Object>getTypeHandler());
    }

    @Test
    public void testGetContentType() {
        JavaType superClass = new TypeReference<Object>() {}.getType();
        JavaType referencedType = new TypeReference<String>() {}.getType();

        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], referencedType);

        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType superClass = new TypeReference<Object>() {}.getType();
        JavaType referencedType = new TypeReference<String>() {}.getType();

        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], referencedType);

        assertTrue(referenceType.isReferenceType());
    }
}