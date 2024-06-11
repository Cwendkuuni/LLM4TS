package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.Class;
import java.lang.reflect.Type;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruction() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
        assertEquals(String[].class, referenceType.getRawType());
        assertEquals(Integer.TYPE, referenceType.getContentType().getRawType());
    }

    @Test
    public void testWithContentType() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), TypeBindings.emptyBindings(), null, null, refdType);
        JavaType newContentType = new SimpleType(Long.TYPE);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(String[].class, newReferenceType.getRawType());
        assertEquals(Long.TYPE, newReferenceType.getContentType().getRawType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
        JavaType newRefdType = new SimpleType(Long.TYPE);
        ReferenceType newReferenceType = ReferenceType.upgradeFrom(referenceType, newRefdType);
        assertEquals(String[].class, newReferenceType.getRawType());
        assertEquals(Long.TYPE, newReferenceType.getContentType().getRawType());
    }

    @Test
    public void testConstruct() {
        Class<String[]> cls = String[].class;
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = ReferenceType.construct(cls, TypeBindings.emptyBindings(), superClass, superInts, refdType);
        assertEquals(String[].class, referenceType.getRawType());
        assertEquals(Integer.TYPE, referenceType.getContentType().getRawType());
    }

    @Test
    public void testDeprecatedConstruct() {
        Class<String> cls = String.class;
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = ReferenceType.construct(cls, refdType);
        assertEquals(String.class, referenceType.getRawType());
        assertEquals(Integer.TYPE, referenceType.getContentType().getRawType());
    }

    @Test
    public void testGetContentType() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), TypeBindings.emptyBindings(), null, null, refdType);
        JavaType contentType = referenceType.getContentType();
        assertEquals(Integer.TYPE, contentType.getRawType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), TypeBindings.emptyBindings(), null, null, refdType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), TypeBindings.emptyBindings(), null, null, refdType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String[]> cls = String[].class;
        JavaType superClass = new SimpleType(Object.class);
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refdType);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String[]> cls = String[].class;
        JavaType superClass = new SimpleType(Object.class);
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refdType);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;<T>Ljava/lang/Object;T;>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), TypeBindings.emptyBindings(), null, null, refdType);
        JavaType anchorType = referenceType.getAnchorType();
        assertEquals(referenceType, anchorType);
    }

    @Test
    public void testIsAnchorType() {
        JavaType baseType = new SimpleType(String[].class);
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), TypeBindings.emptyBindings(), null, null, refdType);
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        Class<String[]> cls = String[].class;
        JavaType superClass = new SimpleType(Object.class);
        JavaType refdType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refdType);
        String expected = "[reference type, class [" + cls.getName() + "], class java.lang.Object, interfaces [], class java.lang.Integer]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType baseType1 = new SimpleType(String[].class);
        JavaType refdType1 = new SimpleType(Integer.TYPE);
        JavaType baseType2 = new SimpleType(String[].class);
        JavaType refdType2 = new SimpleType(Integer.TYPE);
        ReferenceType referenceType1 = new ReferenceType(baseType1.getRawClass(), TypeBindings.emptyBindings(), null, null, refdType1);
        ReferenceType referenceType2 = new ReferenceType(baseType2.getRawClass(), TypeBindings.emptyBindings(), null, null, refdType2);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        JavaType newContentType = new SimpleType(Long.TYPE);
        ReferenceType newReferenceType = new ReferenceType(referenceType1, newContentType);
        assertFalse(referenceType1.equals(newReferenceType));
    }
}