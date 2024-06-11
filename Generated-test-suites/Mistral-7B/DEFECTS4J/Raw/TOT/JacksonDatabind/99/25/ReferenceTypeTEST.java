package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link ReferenceType}.
 */
public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, new SimpleType.ConstructorReference(int[].class));
        ReferenceType referenceType = (ReferenceType) javaType;

        assertNotNull(referenceType);
        assertNotNull(referenceType.getReferencedType());
        assertEquals(int[].class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType.ConstructorReference(stringClass);
        JavaType referencedType = new SimpleType.ConstructorReference(int[].class);
        JavaType referenceType = ReferenceType.upgradeFrom(javaType, referencedType);

        assertNotNull(referenceType);
        assertNotNull(referenceType.getReferencedType());
        assertEquals(int[].class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType.ConstructorReference(stringClass);
        JavaType referencedType = new SimpleType.ConstructorReference(int[].class);
        JavaType referenceType = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        JavaType newReferenceType = referenceType.withContentType(new SimpleType.ConstructorReference(float[].class));

        assertNotNull(newReferenceType);
        assertNotNull(newReferenceType.getReferencedType());
        assertEquals(float[].class, newReferenceType.getReferencedType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType.ConstructorReference(stringClass);
        ReferenceType.upgradeFrom(javaType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        Class<String> stringClass = String.class;
        JavaType javaType = new SimpleType.ConstructorReference(stringClass);
        JavaType refType = new SimpleType.ConstructorReference(int.class);
        ReferenceType.upgradeFrom(javaType, refType);
    }
}