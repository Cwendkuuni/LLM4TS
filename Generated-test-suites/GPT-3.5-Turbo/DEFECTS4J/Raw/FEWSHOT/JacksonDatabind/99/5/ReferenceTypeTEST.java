package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class ReferenceTypeTest {
    @Test
    public void testwithContentType() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType type = new ReferenceType(Object.class, null, null, null, refType, null, null, null, false);
        assertEquals(refType, type.getContentType());

        JavaType newContentType = new SimpleType(String.class);
        JavaType newType = type.withContentType(newContentType);
        assertEquals(newContentType, newType.getContentType());
    }

    @Test
    public void testwithTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType type = new ReferenceType(Object.class, null, null, null, null, null, null, typeHandler, false);
        assertEquals(typeHandler, type.getTypeHandler());

        Object newTypeHandler = new Object();
        JavaType newType = type.withTypeHandler(newTypeHandler);
        assertEquals(newTypeHandler, newType.getTypeHandler());
    }

    @Test
    public void testwithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType type = new ReferenceType(Object.class, null, null, null, refType, null, null, null, false);
        assertEquals(contentTypeHandler, type.getContentType().getTypeHandler());

        Object newContentTypeHandler = new Object();
        JavaType newType = type.withContentTypeHandler(newContentTypeHandler);
        assertEquals(newContentTypeHandler, newType.getContentType().getTypeHandler());
    }

    @Test
    public void testwithValueHandler() {
        Object valueHandler = new Object();
        ReferenceType type = new ReferenceType(Object.class, null, null, null, null, null, valueHandler, null, false);
        assertEquals(valueHandler, type.getValueHandler());

        Object newValueHandler = new Object();
        JavaType newType = type.withValueHandler(newValueHandler);
        assertEquals(newValueHandler, newType.getValueHandler());
    }

    @Test
    public void testwithContentValueHandler() {
        Object contentValueHandler = new Object();
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType type = new ReferenceType(Object.class, null, null, null, refType, null, null, null, false);
        assertEquals(contentValueHandler, type.getContentType().getValueHandler());

        Object newContentValueHandler = new Object();
        JavaType newType = type.withContentValueHandler(newContentValueHandler);
        assertEquals(newContentValueHandler, newType.getContentType().getValueHandler());
    }

    @Test
    public void testwithStaticTyping() {
        ReferenceType type = new ReferenceType(Object.class, null, null, null, null, null, null, null, true);
        assertTrue(type.isStaticTyping());

        JavaType newType = type.withStaticTyping();
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testrefine() {
        JavaType superClass = new SimpleType(Number.class);
        JavaType[] superInterfaces = new JavaType[1];
        superInterfaces[0] = new SimpleType(CharSequence.class);
        ReferenceType type = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);

        Class<?> rawType = Integer.class;
        TypeBindings bindings = null;
        JavaType refType = new SimpleType(Integer.class);
        JavaType newType = type.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, newType.getRawClass());
        assertEquals(bindings, newType.getBindings());
        assertEquals(superClass, newType.getSuperClass());
        assertEquals(superInterfaces[0], newType.getInterfaces()[0]);
        assertEquals(refType, newType.getContentType());
        assertFalse(newType.isStaticTyping());
    }

    @Test
    public void testgetErasedSignature() {
        ReferenceType type = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        expected.append("Ljava/lang/Object<>");
        assertEquals(expected.toString(), type.getErasedSignature(sb).toString());
    }

    @Test
    public void testgetGenericSignature() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType type = new ReferenceType(Object.class, null, null, null, refType, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        expected.append("Lcom/fasterxml/jackson/databind/type/SimpleType;");
        expected.append("<Ljava/lang/Integer;>;");
        assertEquals(expected.toString(), type.getGenericSignature(sb).toString());
    }

    @Test
    public void testgetAnchorType() {
        JavaType anchorType = new SimpleType(String.class);
        ReferenceType type = new ReferenceType(Object.class, null, null, null, null, anchorType, null, null, false);
        assertEquals(anchorType, type.getAnchorType());
    }

    @Test
    public void testisAnchorType() {
        ReferenceType type = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        assertTrue(type.isAnchorType());
    }

    @Test
    public void testtoString() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType type = new ReferenceType(Object.class, null, null, null, refType, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        expected.append("[reference type, class java.lang.Object<");
        expected.append(refType.toString());
        expected.append(">");
        expected.append("]");
        assertEquals(expected.toString(), type.toString());
    }

    @Test
    public void testequals() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType type1 = new ReferenceType(Object.class, null, null, null, refType, null, null, null, false);
        ReferenceType type2 = new ReferenceType(Object.class, null, null, null, refType, null, null, null, false);
        assertTrue(type1.equals(type2));
    }
}