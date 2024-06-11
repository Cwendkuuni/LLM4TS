package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeDefinition;

public class ReferenceTypeTest {

    private JavaType simpleType;
    private JavaType referencedType;
    private JavaType referenceType;

    @Before
    public void setUp() throws Exception {
        Class<Integer> intClass = Integer.class;
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();

        simpleType = TypeDefinition.simple(intClass, bindings);
        referencedType = TypeDefinition.simple(String.class, bindings);
    }

    @Test
    public void testUpgradeFrom() throws Exception {
        JavaType referenceTypeFromUpgrade = ReferenceType.upgradeFrom(simpleType, referencedType);
        assertEquals("ReferenceType for [class java.lang.Integer, class java.lang.String]", referenceTypeFromUpgrade.toString());
        assertEquals(referencedType, referenceTypeFromUpgrade.getContentType());
    }

    @Test
    public void testConstruct() throws Exception {
        JavaType referenceTypeFromConstruct = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), simpleType, new JavaType[0], referencedType);
        assertEquals("ReferenceType for [class java.lang.Integer, class java.lang.String]", referenceTypeFromConstruct.toString());
        assertEquals(referencedType, referenceTypeFromConstruct.getContentType());
    }

    @Test
    public void testWithContentType() throws Exception {
        JavaType newReferencedType = TypeDefinition.simple(Double.class, TypeBindings.emptyBindings());
        referenceType = ReferenceType.upgradeFrom(simpleType, referencedType);
        JavaType newReferenceType = referenceType.withContentType(newReferencedType);
        assertEquals("ReferenceType for [class java.lang.Integer, class java.lang.Double]", newReferenceType.toString());
        assertEquals(newReferencedType, newReferenceType.getContentType());
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testGetContentType() throws Exception {
        referenceType = ReferenceType.upgradeFrom(simpleType, referencedType);
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() throws Exception {
        assertTrue(simpleType.isSimpleType());
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() throws Exception {
        assertFalse(simpleType.hasContentType());
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() throws Exception {
        StringBuilder expected = new StringBuilder("[class java.lang.Integer]");
        assertEquals(expected.toString(), referenceType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() throws Exception {
        StringBuilder expected = new StringBuilder("[reference type, class java.lang.Integer, class java.lang.String]");
        assertEquals(expected.toString(), referenceType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetAnchorType() throws Exception {
        assertEquals(referenceType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() throws Exception {
        assertTrue(referenceType.isAnchorType());
    }
}