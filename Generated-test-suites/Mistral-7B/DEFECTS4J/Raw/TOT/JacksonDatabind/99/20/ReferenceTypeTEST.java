package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstructorUpgradeFrom() {
        JavaType baseType = SimpleType.construct(String.class);
        JavaType refType = TypeReference.construct(int[].class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(int[].class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testConstructorReferenceType() {
        JavaType refType = TypeReference.construct(String[].class);
        ReferenceType referenceType = ReferenceType.construct(String.class,
                TypeBindings.emptyBindings(), null, null, refType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(String[].class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType referenceType = ReferenceType.construct(String.class,
                TypeBindings.emptyBindings(), null, null, TypeReference.construct(int.class));
        JavaType newContentType = TypeReference.construct(long.class);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);

        assertNotSame(referenceType, newReferenceType);
        assertEquals(newContentType, newReferenceType.getContentType());
    }

    @Test
    public void testGetContentType() {
        JavaType referenceType = ReferenceType.construct(String.class,
                TypeBindings.emptyBindings(), null, null, TypeReference.construct(int.class));
        JavaType contentType = referenceType.getContentType();

        assertEquals(int.class, contentType.getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType referenceType = ReferenceType.construct(String.class,
                TypeBindings.emptyBindings(), null, null, TypeReference.construct(int.class));
        JavaType simpleType = SimpleType.construct(int.class);

        assertTrue(referenceType.isReferenceType());
        assertFalse(simpleType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType referenceType = ReferenceType.construct(String.class,
                TypeBindings.emptyBindings(), null, null, TypeReference.construct(int.class));

        assertTrue(referenceType.hasContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromMissingReferencedType() {
        JavaType baseType = SimpleType.construct(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithoutRefType() {
        ReferenceType.construct(String.class, TypeBindings.emptyBindings(),
                null, null, null);
    }

    @Test
    public void testGetReferencedType() {
        JavaType referenceType = ReferenceType.construct(String.class,
                TypeBindings.emptyBindings(), null, null, TypeReference.construct(int.class));
        JavaType referencedType = referenceType.getReferencedType();

        assertEquals(int.class, referencedType.getRawClass());
    }

    @Test
    public void testConstructWithNullBindings() {
        JavaType referenceType = ReferenceType.construct(String.class, null,
                null, null, TypeReference.construct(int.class));

        assertNotNull(referenceType);
    }

    @Test
    public void testConstructWithNullSuperInterfaces() {
        JavaType referenceType = ReferenceType.construct(String.class,
                TypeBindings.emptyBindings(), null, null, null,
                TypeReference.construct(int.class));

        assertNotNull(referenceType);
    }

    @Test
    public void testConstructWithNullSuperClass() {
        JavaType referenceType = ReferenceType.construct(String.class,
                TypeBindings.emptyBindings(), null, null, null,
                TypeReference.construct(int.class));

        assertNotNull(referenceType);
    }

    @Test
    public void testConstructWithNullValueHandler() {
        JavaType referenceType = ReferenceType.construct(String.class,
                TypeBindings.emptyBindings(), null, null, null, null, null,
                null, false);

        assertNotNull(referenceType);
    }

    @Test
    public void testConstructWithNullTypeHandler() {
        JavaType referenceType = ReferenceType.construct(String.class,
                TypeBindings.emptyBindings(), null, null, null, null, null,
                null, false);

        assertNotNull(referenceType);
    }

    @Test
    public void testConstructWithNullAsStatic() {
        JavaType referenceType = ReferenceType.construct(String.class,
                TypeBindings.emptyBindings(), null, null, null, null, null,
                null, (TypeBase) null);

        assertNotNull(referenceType);
    }
}