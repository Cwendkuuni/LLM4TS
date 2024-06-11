package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        assertNotNull(refType);
        assertEquals(String.class, refType.getType());
        assertEquals(Integer.class, refType.getContentType());
        assertFalse(refType.isContainerType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        assertEquals(Integer.class, refType.getReferencedType().getType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        assertTrue(refType.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        JavaType handlerType = new SimpleType(StringHandler.class);
        JavaType handlerTypeWithContentHandler = new SimpleReferenceType(handlerType, null, null, null);
        JavaType refTypeWithTypeHandler = refType.withTypeHandler(handlerTypeWithContentHandler);
        assertEquals(refType, refTypeWithTypeHandler.withTypeHandler(null));
        assertEquals(handlerTypeWithContentHandler, refTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        JavaType handlerType = new SimpleType(StringHandler.class);
        JavaType handlerTypeWithContentHandler = new SimpleReferenceType(null, handlerType, null, null);
        JavaType refTypeWithContentTypeHandler = refType.withContentTypeHandler(handlerTypeWithContentHandler);
        assertEquals(refType, refTypeWithContentTypeHandler.withContentTypeHandler(null));
        assertEquals(handlerTypeWithContentHandler, refTypeWithContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        JavaType handlerType = new SimpleType(StringHandler.class);
        JavaType refTypeWithValueHandler = refType.withValueHandler(handlerType);
        assertEquals(handlerType, refTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        JavaType handlerType = new SimpleType(StringHandler.class);
        JavaType refTypeWithContentValueHandler = refType.withContentValueHandler(handlerType);
        assertEquals(handlerType, refTypeWithContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        JavaType refTypeWithStaticTyping = refType.withStaticTyping();
        assertTrue(refTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        assertEquals("class com.fasterxml.jackson.databind.type.ReferenceType<java.lang.String, class java.lang.Integer>", refType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        JavaType narrowedType = refType._narrow(Long.class);
        assertEquals(new SimpleType(Long.class), narrowedType);
    }

    @Test
    public void testGetContainedTypes() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        Type[] containedTypes = refType.getContainedTypes();
        assertNull(containedTypes);
    }

    @Test
    public void testGetContainedTypesCount() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        int containedTypesCount = refType.containedTypeCount();
        assertEquals(1, containedTypesCount);
    }

    @Test
    public void testGetContainedType() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        JavaType containedType = refType.containedType(0);
        assertEquals(refType.getReferencedType(), containedType);
    }

    @Test
    public void testContainedTypeName() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        String containedTypeName = refType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        assertEquals(Integer.class, refType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        StringBuilder erasedSignature = refType.getErasedSignature(new StringBuilder());
        assertEquals("class com.fasterxml.jackson.databind.type.SimpleType<java.lang.Integer>", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        StringBuilder genericSignature = new StringBuilder();
        refType.getGenericSignature(genericSignature);
        assertEquals("<java.lang.String, class java.lang.Integer>", genericSignature.toString());
    }

    @Test
    public void testEquals() {
        JavaType baseType1 = new SimpleType(Integer.class);
        JavaType refType1 = ReferenceType.construct(String.class, baseType1, null, null);
        JavaType baseType2 = new SimpleType(Integer.class);
        JavaType refType2 = ReferenceType.construct(String.class, baseType2, null, null);
        JavaType refType3 = ReferenceType.construct(String.class, baseType1, null, null);
        JavaType refType4 = new SimpleType(String.class);

        assertEquals(refType1, refType1);
        assertFalse(refType1.equals(baseType1));
        assertFalse(refType1.equals(refType2));
        assertFalse(refType1.equals(refType3));
        assertFalse(refType1.equals(refType4));
    }

    @Test
    public void testToString() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType refType = ReferenceType.construct(String.class, baseType, null, null);
        assertEquals("[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<java.lang.String, class java.lang.Integer>]", refType.toString());
    }
}