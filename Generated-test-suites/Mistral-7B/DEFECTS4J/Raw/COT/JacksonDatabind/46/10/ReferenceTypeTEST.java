package com.example.junit.test.referencetype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object() {
        };
        Object typeHandler = new Object() {
        };

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(int.class, referenceType.getContentType().getType());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testNarrowNull() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        assertNull(referenceType.narrow(null));
    }

    @Test
    public void testNarrowSubclass() {
        Class<String> cls = String.class;
        Class<Integer> subclass = Integer.class;
        JavaType refType = JavaType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        JavaType result = referenceType.narrow(subclass);

        assertEquals(JavaType.construct(Integer.class), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        Class<String> cls = String.class;
        Class<Double> subclass = Double.class;
        JavaType refType = JavaType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        referenceType.narrow(subclass);
    }

    @Test
    public void testEqualsRefType() {
        Class<String> cls = String.class;
        JavaType refType1 = JavaType.construct(int.class);
        JavaType refType2 = JavaType.construct(int.class);

        ReferenceType referenceType1 = new ReferenceType(cls, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(cls, refType2, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(cls, refType1, null, null, true);
        assertFalse(referenceType1.equals(referenceType3));
    }

    @Test
    public void testEqualsNull() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        assertFalse(referenceType.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);

        ReferenceType referenceType1 = new ReferenceType(cls, refType, null, null, false);
        Object obj = new Object();

        assertFalse(referenceType1.equals(obj));
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        String result = referenceType.toString();

        String expected = "[reference type, class [Ljava.lang.String;<T int>]";
        assertEquals(expected, result);
    }
}