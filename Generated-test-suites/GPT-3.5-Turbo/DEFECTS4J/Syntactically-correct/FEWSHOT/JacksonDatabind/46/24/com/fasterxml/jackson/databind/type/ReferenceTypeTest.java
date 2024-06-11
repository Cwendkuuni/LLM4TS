package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.nullType();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.nullType();
        Object valueHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newType = referenceType.withContentValueHandler(valueHandler);

        assertNotNull(newType);
        assertEquals(referenceType.getRawClass(), newType.getRawClass());
        assertEquals(referenceType.getReferencedType(), newType.getReferencedType());
        assertEquals(valueHandler, newType.getReferencedType().getValueHandler());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.nullType();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);

        String expected = "[reference type, class " + referenceType.buildCanonicalName() + "<" + referenceType.getReferencedType() + ">]";
        assertEquals(expected, referenceType.toString());
    }

    // Add more test cases for other methods in the ReferenceType class

}