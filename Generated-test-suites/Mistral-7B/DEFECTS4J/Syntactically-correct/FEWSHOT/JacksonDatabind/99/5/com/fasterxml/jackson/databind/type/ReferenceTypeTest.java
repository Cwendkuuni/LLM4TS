package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntegerType.NUMBER_TYPE;
        JavaType anchorType = null;
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperclass());
        assertEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getContentType());
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testConstructWithAnchorType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntegerType.NUMBER_TYPE;
        JavaType anchorType = BooleanType.BOOLEAN_TYPE;
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType, anchorType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperclass());
        assertEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType baseType = SimpleType.construct(IntegerType.NUMBER_TYPE, bindings, cls, superClass, superInts);
        JavaType refType = IntegerType.NUMBER_TYPE;
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperclass());
        assertEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getContentType());
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testUpgradeFromWithAnchorType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType baseType = SimpleType.construct(BooleanType.BOOLEAN_TYPE, bindings, cls, superClass, superInts);
        JavaType refType = IntegerType.NUMBER_TYPE;
        JavaType anchorType = BooleanType.BOOLEAN_TYPE;
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType, anchorType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperclass());
        assertEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullBaseType() {
        JavaType refType = IntegerType.NUMBER_TYPE;
        ReferenceType.upgradeFrom(null, refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType baseType = SimpleType.construct(IntegerType.NUMBER_TYPE, TypeBindings.emptyBindings(), String.class, Object.class, new JavaType[0]);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testGetContentType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntegerType.NUMBER_TYPE;
        JavaType anchorType = null;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetAnchorType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntegerType.NUMBER_TYPE;
        JavaType anchorType = BooleanType.BOOLEAN_TYPE;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType);
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntegerType.NUMBER_TYPE;
        JavaType anchorType = BooleanType.BOOLEAN_TYPE;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType);
        assertTrue(referenceType.isAnchorType());

        JavaType refType2 = StringType.STRING_TYPE;
        ReferenceType referenceType2 = new ReferenceType(cls, bindings, superClass, superInts, refType2, anchorType);
        assertFalse(referenceType2.isAnchorType());
    }
}