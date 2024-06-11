package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.node.AbstractNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);

        assertEquals(int.class, refIntType.getRawClass());
        assertEquals(intType, refIntType.getContentType());
        assertEquals(intType.getTypeId(), refIntType.getTypeId());
        assertEquals(false, refIntType.isReferenceType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);

        assertEquals(intType, refIntType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);

        assertTrue(refIntType.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);
        Object valueHandler = new Object();

        JavaType refIntTypeWithValueHandler = refIntType.withValueHandler(valueHandler);

        assertNotSame(refIntType, refIntTypeWithValueHandler);
        assertEquals(valueHandler, refIntTypeWithValueHandler.getValueHandler());
        assertEquals(refIntType.getTypeId(), refIntTypeWithValueHandler.getTypeId());
        assertEquals(refIntType.getRawClass(), refIntTypeWithValueHandler.getRawClass());
        assertEquals(refIntType.getContentType(), refIntTypeWithValueHandler.getContentType());
        assertEquals(refIntType.isReferenceType(), refIntTypeWithValueHandler.isReferenceType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);
        JavaType intTypeWithHandler = SimpleType.construct(int.class, new ObjectNode(new Object()).getValueFactory());

        JavaType refIntTypeWithContentTypeHandler = refIntType.withContentTypeHandler(intTypeWithHandler);

        assertNotSame(refIntType, refIntTypeWithContentTypeHandler);
        assertEquals(intTypeWithHandler, refIntTypeWithContentTypeHandler.getContentType());
        assertEquals(refIntType.getValueHandler(), refIntTypeWithContentTypeHandler.getValueHandler());
        assertEquals(refIntType.getTypeHandler(), refIntTypeWithContentTypeHandler.getTypeHandler());
        assertEquals(refIntType.getTypeId(), refIntTypeWithContentTypeHandler.getTypeId());
        assertEquals(refIntType.getRawClass(), refIntTypeWithContentTypeHandler.getRawClass());
        assertEquals(refIntType.isReferenceType(), refIntTypeWithContentTypeHandler.isReferenceType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);
        Object valueHandler = new Object();

        JavaType refIntTypeWithValueHandler = refIntType.withValueHandler(valueHandler);

        assertNotSame(refIntType, refIntTypeWithValueHandler);
        assertEquals(valueHandler, refIntTypeWithValueHandler.getValueHandler());
        assertEquals(refIntType.getTypeHandler(), refIntTypeWithValueHandler.getTypeHandler());
        assertEquals(refIntType.getContentTypeHandler(), refIntTypeWithValueHandler.getContentTypeHandler());
        assertEquals(refIntType.getTypeId(), refIntTypeWithValueHandler.getTypeId());
        assertEquals(refIntType.getRawClass(), refIntTypeWithValueHandler.getRawClass());
        assertEquals(refIntType.isReferenceType(), refIntTypeWithValueHandler.isReferenceType());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);
        Object valueHandler = new Object();

        JavaType refIntTypeWithContentValueHandler = refIntType.withContentValueHandler(valueHandler);

        assertNotSame(refIntType, refIntTypeWithContentValueHandler);
        assertEquals(valueHandler, refIntTypeWithContentValueHandler.getContentValueHandler());
        assertEquals(refIntType.getValueHandler(), refIntTypeWithContentValueHandler.getValueHandler());
        assertEquals(refIntType.getTypeHandler(), refIntTypeWithContentValueHandler.getTypeHandler());
        assertEquals(refIntType.getContentTypeHandler(), refIntTypeWithContentValueHandler.getContentTypeHandler());
        assertEquals(refIntType.getTypeId(), refIntTypeWithContentValueHandler.getTypeId());
        assertEquals(refIntType.getRawClass(), refIntTypeWithContentValueHandler.getRawClass());
        assertEquals(refIntType.isReferenceType(), refIntTypeWithContentValueHandler.isReferenceType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);

        JavaType refIntTypeWithStaticTyping = refIntType.withStaticTyping();

        assertNotSame(refIntType, refIntTypeWithStaticTyping);
        assertTrue(refIntTypeWithStaticTyping.isStatic());
        assertFalse(refIntTypeWithStaticTyping.isDynamic());
        assertFalse(refIntType.isStatic());
        assertTrue(refIntType.isDynamic());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);

        assertEquals("[reference type, class java.lang.Integer", refIntType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType strType = SimpleType.construct(String.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);
        JavaType refStrType = ReferenceType.construct(String.class, strType);
        JavaType refIntToStrType = refIntType.narrow(String.class);

        assertNotSame(refIntType, refIntToStrType);
        assertEquals(refStrType, refIntToStrType);
    }

    @Test
    public void testGetErasedSignature() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);

        StringBuilder result = new StringBuilder();
        refIntType.getErasedSignature(result);

        assertEquals("[I", result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);

        StringBuilder result = new StringBuilder();
        refIntType.getGenericSignature(result);

        assertEquals("[Ljava.lang.Class;<T:Ljava.lang.Class<T;>;>Lcom.fasterxml.jackson.databind.type.ReferenceType<T:Ljava.lang.Class<T;>;>;", result.toString());
    }

    @Test
    public void testContainsType() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);

        assertNull(refIntType.containedType(0));
        assertNull(refIntType.containedType(1));
        assertEquals(intType, refIntType.containedType(0));
    }

    @Test
    public void testContainedTypeCount() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);

        assertEquals(1, refIntType.containedTypeCount());
    }

    @Test
    public void testToString() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType = ReferenceType.construct(int.class, intType);

        assertEquals("[reference type, class java.lang.Integer]", refIntType.toString());
    }

    @Test
    public void testEquals() {
        JavaType intType = SimpleType.construct(int.class);
        JavaType refIntType1 = ReferenceType.construct(int.class, intType);
        JavaType refIntType2 = ReferenceType.construct(int.class, intType);
        JavaType refIntType3 = ReferenceType.construct(String.class, intType);

        assertTrue(refIntType1.equals(refIntType1));
        assertTrue(refIntType1.equals(refIntType2));
        assertFalse(refIntType1.equals(refIntType3));
        assertFalse(refIntType1.equals(null));
        assertFalse(refIntType1.equals(""));
        assertFalse(refIntType1.equals(new Object()));
    }
}