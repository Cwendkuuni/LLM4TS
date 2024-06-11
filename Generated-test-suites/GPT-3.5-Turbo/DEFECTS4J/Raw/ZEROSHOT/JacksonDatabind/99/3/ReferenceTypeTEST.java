package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstructor1() {
        JavaType refType = new SimpleType();
        JavaType anchorType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(
                getClass(), new TypeBindings(),
                new SimpleType(), new JavaType[0], refType, anchorType,
                new Object(), new Object(), true);

        assertEquals(getClass(), referenceType._class);
        assertEquals(new TypeBindings(), referenceType._bindings);
        assertEquals(new SimpleType(), referenceType._superClass);
        assertArrayEquals(new JavaType[0], referenceType._superInterfaces);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(anchorType, referenceType._anchorType);
    }

    @Test
    public void testConstructor2() {
        SimpleType base = new SimpleType();
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(base, refType);

        assertEquals(base, referenceType);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(referenceType, referenceType._anchorType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType();
        JavaType refdType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(
                getClass(), new TypeBindings(),
                new SimpleType(), new JavaType[0], refdType, null,
                null, null, false);

        assertEquals(refdType.hashCode(), referenceType._hashCode);

        assertTrue(refdType.getTypeHandler() == null);
    }

    @Test
    public void testConstruct() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = ReferenceType.construct(
                getClass(), new TypeBindings(),
                new SimpleType(), new JavaType[0], refType);

        assertEquals(getClass(), referenceType._class);
        assertEquals(new TypeBindings(), referenceType._bindings);
        assertEquals(new SimpleType(), referenceType._superClass);
        assertArrayEquals(new JavaType[0], referenceType._superInterfaces);
        assertEquals(refType, referenceType._referencedType);
        assertNull(referenceType._anchorType);
    }

    @Test
    public void testConstruct2() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = ReferenceType.construct(
                getClass(), refType);

        assertEquals(getClass(), referenceType._class);
        assertEquals(TypeBindings.emptyBindings(), referenceType._bindings);
        assertNull(referenceType._superClass);
        assertNull(referenceType._superInterfaces);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }
}