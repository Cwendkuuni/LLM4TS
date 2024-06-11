package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaListStringType = TypeFactory.defaultInstance().constructCollectionType(List.class, javaStringType);

        ReferenceType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, javaStringType);
        assertSame(stringClass, referenceType.getRawClass());
        assertEquals(JavaType.TypeFlags.CLASS, referenceType.getTypeFlags());
        assertNull(referenceType.getContentType());
        assertEquals(javaStringType, referenceType.getReferencedType());
        assertFalse(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
        assertEquals(javaListStringType.toString(), referenceType.toString());
    }

    @Test
    public void testUpgradeFrom() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(stringClass);

        JavaType javaListStringType = TypeFactory.defaultInstance().constructCollectionType(List.class, javaStringType);
        JavaType javaReferenceType = ReferenceType.upgradeFrom(javaStringType, javaListStringType);

        assertInstanceOf(ReferenceType.class, javaReferenceType);
        assertEquals(stringClass, javaReferenceType.getRawClass());
        assertEquals(JavaType.TypeFlags.CLASS, javaReferenceType.getTypeFlags());
        assertNull(javaReferenceType.getContentType());
        assertEquals(javaListStringType, javaReferenceType.getReferencedType());
        assertFalse(javaReferenceType.hasContentType());
        assertTrue(javaReferenceType.isReferenceType());
        assertEquals(javaListStringType.toString(), javaReferenceType.toString());
    }

    @Test
    public void testWithContentType() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaListStringType = TypeFactory.defaultInstance().constructCollectionType(List.class, javaStringType);

        ReferenceType referenceType = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, javaStringType, null, null, null, false);
        JavaType javaStringWithContentType = javaStringType.withContentType(javaListStringType);
        JavaType javaReferenceTypeWithContentType = referenceType.withContentType(javaStringWithContentType);

        assertSame(javaReferenceType, javaReferenceTypeWithContentType);
        assertEquals(javaListStringType, javaReferenceType.getContentType());
        assertSame(javaStringType, javaReferenceType.getReferencedType());
    }

    @Test
    public void testGetAnchorType() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaListStringType = TypeFactory.defaultInstance().constructCollectionType(List.class, javaStringType);

        ReferenceType referenceType = new ReferenceType(javaStringType, null, null, null, null, referenceType, null, null, false);
        assertSame(referenceType, referenceType.getAnchorType());

        ReferenceType subReferenceType = new ReferenceType(String[].class, null, null, null, javaStringType, referenceType, null, null, false);
        assertSame(referenceType, subReferenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaListStringType = TypeFactory.defaultInstance().constructCollectionType(List.class, javaStringType);

        ReferenceType referenceType = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, javaStringType, referenceType, null, null, false);
        assertTrue(referenceType.isAnchorType());

        ReferenceType subReferenceType = new ReferenceType(String[].class, null, null, null, javaStringType, referenceType, null, null, false);
        assertTrue(referenceType.isAnchorType());

        ReferenceType superReferenceType = new ReferenceType(Object.class, null, null, null, javaStringType, referenceType, null, null, false);
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testEquality() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaListStringType = TypeFactory.defaultInstance().constructCollectionType(List.class, javaStringType);

        ReferenceType referenceType1 = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, javaStringType, referenceType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, javaStringType, referenceType2, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(String[].class, null, null, null, javaStringType, referenceType1, null, null, false);
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType3.equals(referenceType1));
    }
}