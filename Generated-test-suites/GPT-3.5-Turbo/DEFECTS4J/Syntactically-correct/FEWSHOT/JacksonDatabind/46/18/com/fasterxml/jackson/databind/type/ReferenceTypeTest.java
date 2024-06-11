package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Object.class);
        Object valueHandler = "valueHandler";
        Object typeHandler = "typeHandler";

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithTyeHandler() {
        Object typeHandler = "typeHandler";

        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType withTypeHandler = referenceType.withTypeHandler(typeHandler);

        assertEquals(referenceType._class, withTypeHandler._class);
        assertEquals(referenceType._referencedType, withTypeHandler._referencedType);
        assertEquals(referenceType._valueHandler, withTypeHandler._valueHandler);
        assertNotEquals(referenceType._typeHandler, withTypeHandler._typeHandler);
        assertEquals(typeHandler, withTypeHandler._typeHandler);
        assertEquals(referenceType._asStatic, withTypeHandler._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = "typeHandler";

        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType withContentTypeHandler = referenceType.withContentTypeHandler(typeHandler);

        assertEquals(referenceType._class, withContentTypeHandler._class);
        assertNotEquals(referenceType._referencedType.<Object>getTypeHandler(), withContentTypeHandler._referencedType.<Object>getTypeHandler());
        assertEquals(typeHandler, withContentTypeHandler._referencedType.<Object>getTypeHandler());
        assertEquals(referenceType._valueHandler, withContentTypeHandler._valueHandler);
        assertEquals(referenceType._typeHandler, withContentTypeHandler._typeHandler);
        assertEquals(referenceType._asStatic, withContentTypeHandler._asStatic);
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = "valueHandler";

        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType withValueHandler = referenceType.withValueHandler(valueHandler);

        assertEquals(referenceType._class, withValueHandler._class);
        assertEquals(referenceType._referencedType, withValueHandler._referencedType);
        assertNotEquals(referenceType._valueHandler, withValueHandler._valueHandler);
        assertEquals(valueHandler, withValueHandler._valueHandler);
        assertEquals(referenceType._typeHandler, withValueHandler._typeHandler);
        assertEquals(referenceType._asStatic, withValueHandler._asStatic);
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = "valueHandler";

        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType withContentValueHandler = referenceType.withContentValueHandler(valueHandler);

        assertEquals(referenceType._class, withContentValueHandler._class);
        assertEquals(referenceType._referencedType, withContentValueHandler._referencedType);
        assertNotEquals(referenceType._referencedType.<Object>getValueHandler(), withContentValueHandler._referencedType.<Object>getValueHandler());
        assertEquals(valueHandler, withContentValueHandler._referencedType.<Object>getValueHandler());
        assertEquals(referenceType._valueHandler, withContentValueHandler._valueHandler);
        assertEquals(referenceType._typeHandler, withContentValueHandler._typeHandler);
        assertEquals(referenceType._asStatic, withContentValueHandler._asStatic);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType withStaticTyping = referenceType.withStaticTyping();

        assertEquals(referenceType._class, withStaticTyping._class);
        assertEquals(referenceType._referencedType, withStaticTyping._referencedType);
        assertEquals(referenceType._valueHandler, withStaticTyping._valueHandler);
        assertEquals(referenceType._typeHandler, withStaticTyping._typeHandler);
        assertTrue(withStaticTyping._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        StringBuilder sb = new StringBuilder();
        String canonicalName = referenceType.buildCanonicalName();

        sb.append(referenceType._class.getName());
        sb.append('<');
        sb.append(referenceType._referencedType.toCanonical());
        assertEquals(sb.toString(), canonicalName);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;

        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        ReferenceType narrowed = (ReferenceType) referenceType._narrow(subclass);

        assertEquals(subclass, narrowed._class);
        assertEquals(referenceType._referencedType, narrowed._referencedType);
        assertEquals(referenceType._valueHandler, narrowed._valueHandler);
        assertEquals(referenceType._typeHandler, narrowed._typeHandler);
        assertEquals(referenceType._asStatic, narrowed._asStatic);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType(Object.class);

        ReferenceType referenceType = new ReferenceType(Object.class, refType, null, null, false);
        JavaType referencedType = referenceType.getReferencedType();

        assertEquals(refType, referencedType);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new SimpleType(Object.class);

        ReferenceType referenceType = new ReferenceType(Object.class, refType, null, null, false);
        JavaType containedType = referenceType.containedType(0);

        assertEquals(refType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        assertNull(referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        assertEquals(Object.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);

        assertEquals(sb, erasedSignature);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        StringBuilder genericSignature = referenceType.getGenericSignature(sb);

        assertEquals(sb, genericSignature);
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(Object.class, new SimpleType(Object.class), null, null, false);
        StringBuilder sb = new StringBuilder();
        String stringValue = sb.append("[reference type, class ")
                .append(referenceType.buildCanonicalName())
                .append('<')
                .append(referenceType._referencedType)
                .append('>')
                .append(']')
                .toString();
        String toString = referenceType.toString();

        assertEquals(stringValue, toString);
    }

    @Test
    public void testEquals() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(Object.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType other = new ReferenceType(cls, refType, null, null, false);

        assertEquals(referenceType, other);
    }
}