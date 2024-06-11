package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(int[].class);
        JavaType refdType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
        assertEquals(String.class, referenceType.getContentType());
        assertEquals(int[].class, referenceType.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefdType() {
        JavaType baseType = new SimpleType(int[].class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new ReferenceType(String.class, null, null, null, null, null);
        JavaType refdType = new SimpleType(int.class);
        ReferenceType.upgradeFrom(baseType, refdType);
    }

    @Test
    public void testConstruct() {
        Class<?> clazz = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object[].class;
        JavaType[] superInterfaces = new JavaType[] { List.class };
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(clazz, bindings,
                superClass, superInterfaces, refType);
        assertEquals(String[].class, referenceType.getType());
        assertEquals(Integer.class, referenceType.getContentType());
    }

    @Test
    public void testWithContentType() {
        Class<?> clazz = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object[].class;
        JavaType[] superInterfaces = new JavaType[] { List.class };
        JavaType refType = new SimpleType(Integer.class);
        JavaType contentType = new SimpleType(Double.class);
        ReferenceType referenceType = new ReferenceType(clazz, bindings,
                superClass, superInterfaces, refType, null, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentType(contentType);
        assertEquals(String[].class, newReferenceType.getType());
        assertEquals(Double.class, newReferenceType.getContentType());
    }

    @Test
    public void testGetContentType() {
        Class<?> clazz = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object[].class;
        JavaType[] superInterfaces = new JavaType[] { List.class };
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(clazz, bindings,
                superClass, superInterfaces, refType, null, null, null, false);
        assertEquals(Integer.class, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> clazz = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object[].class;
        JavaType[] superInterfaces = new JavaType[] { List.class };
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(clazz, bindings,
                superClass, superInterfaces, refType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        Class<?> clazz = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object[].class;
        JavaType[] superInterfaces = new JavaType[] { List.class };
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(clazz, bindings,
                superClass, superInterfaces, refType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> clazz = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object[].class;
        JavaType[] superInterfaces = new JavaType[] { List.class };
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(clazz, bindings,
                superClass, superInterfaces, refType, null, null, null, false);
        StringBuilder expected = new StringBuilder("Ljava/lang/Object;[Ljava/lang/Object;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> clazz = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object[].class;
        JavaType[] superInterfaces = new JavaType[] { List.class };
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(clazz, bindings,
                superClass, superInterfaces, refType, null, null, null, false);
        StringBuilder expected = new StringBuilder("Ljava/util/List;<Ljava/lang/String;Ljava/lang/Integer;>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        Class<?> clazz = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object[].class;
        JavaType[] superInterfaces = new JavaType[] { List.class };
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(clazz, bindings,
                superClass, superInterfaces, refType, null, null, null, false);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        Class<?> clazz = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object[].class;
        JavaType[] superInterfaces = new JavaType[] { List.class };
        JavaType refType = new SimpleType(Integer.class);
        JavaType anchorType = new SimpleType(String[].class);
        ReferenceType referenceType = new ReferenceType(clazz, bindings,
                superClass, superInterfaces, refType, anchorType, null, null, false);
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        Class<?> clazz = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object[].class;
        JavaType[] superInterfaces = new JavaType[] { List.class };
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(clazz, bindings,
                superClass, superInterfaces, refType, null, null, null, false);
        String expected = "[reference type, class [Ljava.lang.String;<Ljava.lang.Integer;>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> clazz1 = String[].class;
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        JavaType superClass1 = Object[].class;
        JavaType[] superInterfaces1 = new JavaType[] { List.class };
        JavaType refType1 = new SimpleType(Integer.class);
        ReferenceType referenceType1 = new ReferenceType(clazz1, bindings1,
                superClass1, superInterfaces1, refType1, null, null, null, false);

        Class<?> clazz2 = String[].class;
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        JavaType superClass2 = Object[].class;
        JavaType[] superInterfaces2 = new JavaType[] { List.class };
        JavaType refType2 = new SimpleType(Integer.class);
        ReferenceType referenceType2 = new ReferenceType(clazz2, bindings2,
                superClass2, superInterfaces2, refType2, null, null, null, false);

        ReferenceType referenceType3 = referenceType1;
        assertTrue(referenceType1.equals(referenceType3));
        assertTrue(referenceType3.equals(referenceType1));

        ReferenceType referenceType4 = new ReferenceType(clazz1, bindings1,
                superClass1, superInterfaces1, refType1, null, null, null, false);
        assertTrue(referenceType1.equals(referenceType4));
        assertTrue(referenceType4.equals(referenceType1));

        ReferenceType referenceType5 = new ReferenceType(clazz2, bindings2,
                superClass2, superInterfaces2, refType2, null, null, null, false);
        assertFalse(referenceType1.equals(referenceType5));
        assertFalse(referenceType5.equals(referenceType1));
    }
}