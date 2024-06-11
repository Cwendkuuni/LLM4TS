package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.Class;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testIsInstance() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        assertTrue(ReferenceType.class.isInstance(referenceType));
        assertFalse(SimpleType.class.isInstance(referenceType));
        assertFalse(JavaType.class.isInstance(referenceType));
    }

    @Test
    public void testGetContentType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        assertEquals(intType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        assertEquals(intType, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());

        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        StringBuilder expected = new StringBuilder("[Ljava/lang/String;<T extends java.lang.Number>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());

        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType longType = TypeFactory.defaultInstance().constructType(Long.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        JavaType newReferenceType = referenceType.withContentType(longType);
        assertEquals(longType, newReferenceType.getContentType());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        JavaType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotNull(newReferenceType.getTypeHandler());
        assertSame(handler, newReferenceType.getTypeHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType longType = TypeFactory.defaultInstance().constructType(Long.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        JavaType newReferenceType = referenceType.withContentTypeHandler(handler);
        assertEquals(handler, newReferenceType.<Object>getContentTypeHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithValueHandler() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        JavaType newReferenceType = referenceType.withValueHandler(handler);
        assertNotNull(newReferenceType.getValueHandler());
        assertSame(handler, newReferenceType.getValueHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        JavaType newReferenceType = referenceType.withContentValueHandler(handler);
        assertEquals(handler, newReferenceType.<Object>getValueHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        JavaType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testRefine() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType subStringType = TypeFactory.defaultInstance().constructType(SubString.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        JavaType newReferenceType = referenceType.refine(subStringType.getType(), TypeBindings.emptyBindings(), null, new JavaType[0]);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(subStringType, newReferenceType.getContentType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        assertNull(referenceType.getAnchorType());

        JavaType anchorType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType anchorReferenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);
        ReferenceType newReferenceType = new ReferenceType(referenceType, anchorReferenceType);

        assertEquals(anchorReferenceType, newReferenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);
        ReferenceType anchorReferenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);
        ReferenceType newReferenceType = new ReferenceType(referenceType, anchorReferenceType);

        assertFalse(newReferenceType.isAnchorType());

        ReferenceType anchorReferenceType2 = new ReferenceType(anchorReferenceType, referenceType);

        assertTrue(anchorReferenceType2.isAnchorType());
    }

    @Test
    public void testGetClass() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);

        assertEquals(ReferenceType.class, referenceType.getClass());
    }

    @Test
    public void testEquals() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType longType = TypeFactory.defaultInstance().constructType(Long.class);
        ReferenceType referenceType1 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);
        ReferenceType referenceType2 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);
        ReferenceType referenceType3 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], longType);

        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(""));
    }
}