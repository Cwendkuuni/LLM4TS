package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = new JavaTypeStub();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null, refType);

        assertEquals(String.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertEquals(refType, type.getContentType());
        assertNotNull(type.getAnchorType());
        assertFalse(type.isAnchorType());
        assertEquals(refType, type.getReferencedType());
        assertTrue(type.hasContentType());
        assertTrue(type.isReferenceType());
        assertNotNull(type.getErasedSignature(new StringBuilder()));
        assertNotNull(type.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaTypeStub();
        JavaType refdType = new JavaTypeStub();
        ReferenceType type = ReferenceType.upgradeFrom(baseType, refdType);

        assertEquals(baseType.getRawClass(), type.getRawClass());
        assertEquals(baseType.getBindings(), type.getBindings());
        assertEquals(baseType.getSuperClass(), type.getSuperClass());
        assertEquals(baseType.getSuperInterfaces(), type.getSuperInterfaces());
        assertEquals(refdType, type.getReferencedType());
    }

    @Test
    public void testWithContentType() {
        JavaType refType = new JavaTypeStub();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null, refType);
        JavaType contentType = new JavaTypeStub();
        ReferenceType newType = (ReferenceType) type.withContentType(contentType);

        assertEquals(contentType, newType.getContentType());
        assertEquals(refType, type.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new JavaTypeStub();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null, refType);
        Object handler = new Object();
        ReferenceType newType = type.withTypeHandler(handler);

        assertEquals(handler, newType.getTypeHandler());
        assertEquals(type.getTypeHandler(), null);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new JavaTypeStub();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null, refType);
        Object handler = new Object();
        ReferenceType newType = type.withContentTypeHandler(handler);

        assertEquals(handler, newType.getContentTypeHandler());
        assertEquals(type.getContentTypeHandler(), null);
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new JavaTypeStub();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null, refType);
        Object handler = new Object();
        ReferenceType newType = type.withValueHandler(handler);

        assertEquals(handler, newType.getValueHandler());
        assertEquals(type.getValueHandler(), null);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new JavaTypeStub();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null, refType);
        Object handler = new Object();
        ReferenceType newType = type.withContentValueHandler(handler);

        assertEquals(handler, newType.getContentValueHandler());
        assertEquals(type.getContentValueHandler(), null);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new JavaTypeStub();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null, refType);
        ReferenceType newType = type.withStaticTyping();

        assertTrue(newType.isStatic());
        assertFalse(type.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType refType = new JavaTypeStub();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null, refType);
        ReferenceType newType = (ReferenceType) type.refine(Integer.class, null, null, null);

        assertEquals(Integer.class, newType.getRawClass());
        assertEquals(type.getBindings(), newType.getBindings());
        assertEquals(type.getSuperClass(), newType.getSuperClass());
        assertEquals(type.getSuperInterfaces(), newType.getSuperInterfaces());
        assertEquals(refType, newType.getContentType());
        assertEquals(type.getAnchorType(), newType.getAnchorType());
        assertEquals(type.getValueHandler(), newType.getValueHandler());
        assertEquals(type.getTypeHandler(), newType.getTypeHandler());
    }

    @Test
    public void testToString() {
        JavaType refType = new JavaTypeStub();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null, refType);

        assertNotNull(type.toString());
        assertNotEquals("", type.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType = new JavaTypeStub();

        ReferenceType type1 = ReferenceType.construct(String.class, null, null, null, refType);
        ReferenceType type2 = ReferenceType.construct(String.class, null, null, null, refType);
        ReferenceType type3 = ReferenceType.construct(Integer.class, null, null, null, refType);

        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
    }
}