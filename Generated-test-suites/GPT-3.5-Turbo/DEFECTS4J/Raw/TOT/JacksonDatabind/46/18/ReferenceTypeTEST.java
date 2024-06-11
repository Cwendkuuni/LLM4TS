
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getParameterSource());
        assertSame(refType, referenceType.getReferencedType());
        assertNotNull(referenceType.getErasedSignature(new StringBuilder()));
        assertNotNull(referenceType.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType updatedReferenceType = referenceType.withStaticTyping();

        assertNotNull(updatedReferenceType);
        assertNotNull(updatedReferenceType.getErasedSignature(new StringBuilder()));
        assertNotNull(updatedReferenceType.getGenericSignature(new StringBuilder()));
        assertTrue(updatedReferenceType.isReferenceType());
        assertEquals(1, updatedReferenceType.containedTypeCount());
        assertEquals(refType, updatedReferenceType.containedType(0));
        assertEquals(cls, updatedReferenceType.getParameterSource());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object contentValueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType updatedReferenceType = referenceType.withContentValueHandler(contentValueHandler);

        assertNotNull(updatedReferenceType);
        assertNotNull(updatedReferenceType.getErasedSignature(new StringBuilder()));
        assertNotNull(updatedReferenceType.getGenericSignature(new StringBuilder()));
        assertSame(contentValueHandler, updatedReferenceType.getReferencedType().getValueHandler());
        assertEquals(cls, updatedReferenceType.getParameterSource());
    }
}

