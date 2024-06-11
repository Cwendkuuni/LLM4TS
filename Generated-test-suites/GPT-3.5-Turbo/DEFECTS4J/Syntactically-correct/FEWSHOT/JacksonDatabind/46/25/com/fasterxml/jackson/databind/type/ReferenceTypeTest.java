package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertFalse(referenceType.hasValueHandler());
        assertFalse(referenceType.hasTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        ReferenceType withTypeHandler = referenceType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertEquals(referenceType, withTypeHandler.withTypeHandler(typeHandler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        ReferenceType withContentTypeHandler = referenceType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, withContentTypeHandler.getContentType().getTypeHandler());
        assertEquals(referenceType, withContentTypeHandler.withContentTypeHandler(contentTypeHandler));
    }
    
    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        Object valueHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        ReferenceType withValueHandler = referenceType.withValueHandler(valueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertEquals(referenceType, withValueHandler.withValueHandler(valueHandler));
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        Object contentValueHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        ReferenceType withContentValueHandler = referenceType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, withContentValueHandler.getContentType().getValueHandler());
        assertEquals(referenceType, withContentValueHandler.withContentValueHandler(contentValueHandler));
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        assertTrue(referenceType.isStaticTyping());
        assertEquals(referenceType, referenceType.withStaticTyping());
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        assertEquals(refType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        assertEquals(cls, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName());
        sb.append('<');
        sb.append(refType.getErasedSignature());
        sb.append('>');
        assertEquals(sb.toString(), referenceType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        StringBuilder sb = new StringBuilder();
        referenceType.getReferencedType().getGenericSignature(sb);
        String refTypeSignature = sb.toString();

        sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        sb.append('<');
        sb.append(refTypeSignature);
        sb.append(';');
        
        assertEquals(sb.toString(), referenceType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        StringBuilder sb = new StringBuilder();
        sb.append("[reference type, class ");
        sb.append(referenceType.getErasedSignature());
        sb.append('<');
        sb.append(refType);
        sb.append('>');
        sb.append(']');
        assertEquals(sb.toString(), referenceType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(cls);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        
        ReferenceType sameType = ReferenceType.construct(cls, refType, null, null);
        assertEquals(referenceType, sameType);

        Class<?> newCls = Integer.class;
        JavaType newRefType = JavaType.construct(newCls);
        ReferenceType differentType = ReferenceType.construct(newCls, newRefType, null, null);
        assertFalse(referenceType.equals(differentType));
    }
}