package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<?> cls = TestClass.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType();
        JavaType[] superInts = new JavaType[1];
        JavaType refType = new SimpleType();
        JavaType anchorType = new SimpleType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        // verify parameters are set correctly
        assertEquals(cls, referenceType._class);
        assertEquals(bindings, referenceType._bindings);
        assertEquals(superClass, referenceType._superClass);
        assertEquals(superInts, referenceType._superInterfaces);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(anchorType, referenceType._anchorType);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertTrue(referenceType._asStatic);

        // verify that anchorType is set to this when anchorType is null
        anchorType = null;

        referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertEquals(referenceType, referenceType._anchorType);
    }

    @Test
    public void testEquals() {
        Class<?> cls = TestClass.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType();
        JavaType[] superInts = new JavaType[1];
        JavaType refType = new SimpleType();
        JavaType anchorType = new SimpleType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        ReferenceType referenceType1 = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);
        ReferenceType referenceType2 = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);
        ReferenceType referenceType3 = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, false);

        // test reflexive property
        assertTrue(referenceType1.equals(referenceType1));

        // test symmetric property
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType1));

        // test transitive property
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType3));
        assertTrue(referenceType1.equals(referenceType3));

        // test equality to null
        assertFalse(referenceType1.equals(null));

        // test equality to different type
        assertFalse(referenceType1.equals(new Object()));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType();

        ReferenceType referenceType1 = new ReferenceType();
        ReferenceType referenceType2 = referenceType1.withContentType(contentType);

        assertEquals(contentType, referenceType2.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();

        ReferenceType referenceType1 = new ReferenceType();
        ReferenceType referenceType2 = referenceType1.withTypeHandler(handler);

        assertEquals(handler, referenceType2._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();

        ReferenceType referenceType1 = new ReferenceType();
        ReferenceType referenceType2 = referenceType1.withContentTypeHandler(handler);

        assertEquals(handler, referenceType2.getContentType().getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();

        ReferenceType referenceType1 = new ReferenceType();
        ReferenceType referenceType2 = referenceType1.withContentValueHandler(handler);

        assertEquals(handler, referenceType2.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType1 = new ReferenceType();
        ReferenceType referenceType2 = referenceType1.withStaticTyping();

        assertTrue(referenceType2._asStatic);
    }

    @Test
    public void testGetAnchorType() {
        JavaType anchorType = new SimpleType();

        ReferenceType referenceType1 = new ReferenceType();
        referenceType1._anchorType = anchorType;
        JavaType actualAnchorType = referenceType1.getAnchorType();

        assertEquals(anchorType, actualAnchorType);
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType1 = new ReferenceType();
        ReferenceType referenceType2 = new ReferenceType();
        referenceType2._anchorType = referenceType1;

        // test when _anchorType is referenceType itself
        assertTrue(referenceType1.isAnchorType());

        // test when _anchorType is not referenceType itself
        assertFalse(referenceType2.isAnchorType());
    }

    private class TestClass {}

}