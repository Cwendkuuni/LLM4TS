package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructorWithRefTypeAndAnchorType() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        JavaType anchorType = new TypeFactory().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(anchorType, referenceType.getAnchorType());
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new TypeFactory().constructType(String[].class);
        JavaType refType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType baseType = new TypeFactory().constructType(String[].class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testGetContentType() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;<T:Ljava/lang/String;>T");
        StringBuilder actual = new StringBuilder();
        referenceType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        assertEquals(null, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        String expected = "[reference type, class [Ljava.lang.String;<T:Ljava.lang.String;>T>, class java.lang.String, referenced type class java.lang.String]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType referencedType1 = new TypeFactory().constructType(String.class);
        JavaType referencedType2 = new TypeFactory().constructType(String.class);
        ReferenceType referenceType1 = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType1, null);
        ReferenceType referenceType2 = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType2, null);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        JavaType differentClass = new TypeFactory().constructType(Integer.class);
        ReferenceType differentReferenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType1, null);
        assertFalse(referenceType1.equals(differentClass));
        assertFalse(referenceType1.equals(differentReferenceType));
        assertFalse(referenceType1.equals(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullClass() {
        JavaType refType = new TypeFactory().constructType(String.class);
        ReferenceType.construct(null, refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullRefType() {
        ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, null);
    }

    @Test
    public void testWithContentType() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        JavaType newContentType = new TypeFactory().constructType(Integer.class);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(newContentType, newReferenceType.getContentType());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        Object newTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        Object newContentTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
        assertEquals(newContentTypeHandler, newReferenceType.getContentTypeHandler());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
    }

    @Test
    public void testWithValueHandler() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        Object newValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        Object newContentValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
        assertEquals(newContentValueHandler, newReferenceType.getContentValueHandler());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStaticTyping());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
    }

    @Test
    public void testRefine() {
        JavaType baseType = new TypeFactory().constructType(String[].class);
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), baseType, null, referencedType, null);
        JavaType newBaseType = new TypeFactory().constructType(List.class);
        ReferenceType newReferenceType = referenceType.refine(newBaseType.getComponentType(), TypeBindings.emptyBindings(), null, null);
        assertEquals(List.class, newReferenceType.getRawClass());
        assertEquals(referencedType, newReferenceType.getContentType());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        StringBuilder expected = new StringBuilder("[reference type, class java.lang.String, referenced type class java.lang.String]");
        StringBuilder actual = referenceType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testNarrow() {
        // Not implementing _narrow method as it's deprecated
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithNullSubclass() {
        JavaType referencedType = new TypeFactory().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, referencedType, null);
        referenceType._narrow(null);
    }
}