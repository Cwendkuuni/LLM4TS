package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {
    @Test
    public void testConstructor() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType superClass = SimpleType.construct(Object.class, bindings, null, null, null, null, null, null);
        SimpleType[] superInts = new SimpleType[0];
        ReferenceType refType = new ReferenceType(ReferenceTypeTest.class, bindings, superClass, superInts, superClass, superClass, null, null, false);

        assertEquals(ReferenceTypeTest.class, refType.getRawClass());
        assertEquals(bindings, refType.getBindings());
        assertEquals(superClass, refType.getSuperClass());
        assertEquals(superInts, refType.getInterfaces());
        assertEquals(superClass, refType.getContentType());
        assertEquals(superClass, refType.getReferencedType());
        assertFalse(refType.hasContentType());
        assertTrue(refType.isReferenceType());
        assertTrue(refType.getErasedSignature(new StringBuilder()).length() > 0);
        assertTrue(refType.getGenericSignature(new StringBuilder()).length() > 0);
        assertEquals(superClass, refType.getAnchorType());
        assertTrue(refType.isAnchorType());
    }

    @Test
    public void testConstructorWithNulls() {
        ReferenceType refType = new ReferenceType(ReferenceTypeTest.class, null, null, null, null, null, null, null, false);

        assertEquals(ReferenceTypeTest.class, refType.getRawClass());
        assertTrue(refType.getBindings().isEmpty());
        assertNull(refType.getSuperClass());
        assertNull(refType.getInterfaces());
        assertNull(refType.getContentType());
        assertNull(refType.getReferencedType());
        assertFalse(refType.hasContentType());
        assertTrue(refType.isReferenceType());
        assertTrue(refType.getErasedSignature(new StringBuilder()).length() > 0);
        assertTrue(refType.getGenericSignature(new StringBuilder()).length() > 0);
        assertEquals(refType, refType.getAnchorType());
        assertTrue(refType.isAnchorType());
    }
}