
package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructorWithContentType() {
        // Test content as per all experts

        // Edge case test: empty TypeBindings
        ReferenceType referenceTypeWithEmptyBindings = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        assertEquals(String.class, referenceTypeWithEmptyBindings.getRawClass());
        assertNull(referenceTypeWithEmptyBindings.getReferencedType());

        // Exception handling test: missing referencedType
        try {
            ReferenceType referenceTypeMissingReferencedType = new ReferenceType(String.class, bindings, null, null, null, null, null, null, false);
            fail("Should not reach this point");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test the getContentType() method
        JavaType contentType = JavaType.construct(Integer[].class);
        ReferenceType referenceTypeWithContentType = new ReferenceType(String.class, bindings, null, null, null, null, null, null, false).withContentType(contentType);
        assertEquals(contentType, referenceTypeWithContentType.getContentType());

        // Test the withValueHandler() method
        Object valueHandler = new Object() {};
        ReferenceType referenceTypeWithValueHandler = new ReferenceType(String.class, bindings, null, null, null, null, null, null, false).withValueHandler(valueHandler);
        assertEquals(valueHandler, referenceTypeWithValueHandler.getValueHandler());

        // Test the withTypeHandler() method
        Object typeHandler = new Object() {};
        ReferenceType referenceTypeWithTypeHandler = new ReferenceType(String.class, bindings, null, null, null, null, null, null, false).withTypeHandler(typeHandler);
        assertEquals(typeHandler, referenceTypeWithTypeHandler.getTypeHandler());

        // Test the withContentTypeHandler() method
        Object contentTypeHandler = new Object() {};
        ReferenceType referenceTypeWithContentTypeHandler = new ReferenceType(String.class, bindings, null, null, null, null, null, null, false).withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, referenceTypeWithContentTypeHandler.getReferencedType().getValueHandler());

        // Test the isAnchorType() method
        ReferenceType anchorType = new ReferenceType(String.class, bindings, null, null, null, null, null, null, false);
        assertFalse(anchorType.isAnchorType());

        ReferenceType anchorTypeReferenceType = new ReferenceType(String.class, bindings, null, null, null, anchorType, null, null, false);
        assertTrue(anchorTypeReferenceType.isAnchorType());

        // Test the upgradeFrom() method
        JavaType baseType = JavaType.construct(Integer.class);
        JavaType refdType = JavaType.construct(String.class);
        ReferenceType referenceTypeUpgraded = ReferenceType.upgradeFrom(baseType, refdType);
        assertEquals(String.class, referenceTypeUpgraded.getRawClass());
        assertEquals(refdType, referenceTypeUpgraded.getReferencedType());
        assertFalse(referenceTypeUpgraded.hasContentType());
        assertTrue(referenceTypeUpgraded.isReferenceType());

        // Test the construct() method
        JavaType refdType2 = JavaType.construct(List.class);
        ReferenceType referenceTypeConstructed = ReferenceType.construct(String.class, bindings, null, null, refdType2);
        assertEquals(String.class, referenceTypeConstructed.getRawClass());
        assertEquals(refdType2, referenceTypeConstructed.getReferencedType());
        assertFalse(referenceTypeConstructed.hasContentType());
        assertTrue(referenceTypeConstructed.isReferenceType());

        // Test the refine() method
        Class<?> rawType = Integer[].class;
        TypeBindings newBindings = TypeBindings.emptyBindings();
        JavaType[] newSuperInterfaces = new JavaType[] {Integer[].class};
        ReferenceType referenceTypeRefined = new ReferenceType(rawType, bindings, null, null).refine(rawType, newBindings, null, newSuperInterfaces);
        assertEquals(rawType, referenceTypeRefined.getRawClass());
        assertEquals(JavaType.construct(Integer[].class), referenceTypeRefined.getReferencedType());
        assertFalse(referenceTypeRefined.hasContentType());
        assertTrue(referenceTypeRefined.isReferenceType());
    }
}