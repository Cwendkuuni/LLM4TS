package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType rt = ReferenceType.construct(cls, refType, null, null);
        assertNotNull(rt);
        assertEquals(rt.getReferencedType(), refType);
        assertEquals(rt.getParameterSource(), cls);
        assertEquals(rt.containedTypeCount(), 1);
        assertEquals(rt.containedTypeName(0), "T");
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType rt = ReferenceType.construct(cls, refType, null, null);
        Object contentHandler = new Object();
        ReferenceType newRt = rt.withContentTypeHandler(contentHandler);
        assertEquals(newRt.getReferencedType().getTypeHandler(), contentHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType rt = ReferenceType.construct(cls, refType, null, null);
        Object contentValueHandler = new Object();
        ReferenceType newRt = rt.withContentValueHandler(contentValueHandler);
        assertEquals(newRt.getReferencedType().getValueHandler(), contentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType rt = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newRt = rt.withStaticTyping();
        assertTrue(newRt.isReferenceType());
        assertTrue(newRt.getReferencedType().isReferenceType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType rt = ReferenceType.construct(cls, refType, null, null);
        String canonicalName = cls.getName() + "<" + refType.toCanonical() + ">";
        assertEquals(rt.buildCanonicalName(), canonicalName);
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType rt = ReferenceType.construct(cls, refType, null, null);
        Class<?> subclass = Object.class;
        ReferenceType newRt = (ReferenceType) rt._narrow(subclass);
        assertEquals(newRt.getReferencedType(), refType);
        assertEquals(newRt.getParameterSource(), subclass);
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType rt = ReferenceType.construct(cls, refType, null, null);
        String str = "[reference type, class " + rt.buildCanonicalName() + "<" + refType.toString() + ">]";
        assertEquals(rt.toString(), str);
    }

    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType rt1 = ReferenceType.construct(cls, refType, null, null);
        ReferenceType rt2 = ReferenceType.construct(cls, refType, null, null);
        ReferenceType rt3 = ReferenceType.construct(Object.class, refType, null, null);
        assertFalse(rt1.equals(null));
        assertTrue(rt1.equals(rt1));
        assertTrue(rt1.equals(rt2));
        assertFalse(rt1.equals(rt3));
    }
}