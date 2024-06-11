package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        ReferenceType refTypeInstance = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
        assertEquals(cls, refTypeInstance.getRawClass());
        assertEquals(bindings, refTypeInstance.getBindings());
        assertEquals(superClass, refTypeInstance.getSuperClass());
        assertEquals(superInts, refTypeInstance.getSuperInterfaces());
        assertEquals(refType, refTypeInstance.getContentType());
        assertEquals(refType, refTypeInstance.getReferencedType());
    }

    @Test
    public void testUpgradeFrom() {
        Class<?> baseClass = Integer.class;
        JavaType baseType = JavaType.construct(baseClass);
        JavaType refType = JavaType.construct(String.class);
        ReferenceType refTypeInstance = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(baseClass, refTypeInstance.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), refTypeInstance.getBindings());
        assertEquals(null, refTypeInstance.getSuperClass());
        assertEquals(new JavaType[0], refTypeInstance.getSuperInterfaces());
        assertEquals(refType, refTypeInstance.getContentType());
        assertEquals(refType, refTypeInstance.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = Integer.class;
        JavaType superClass = JavaType.construct(Object.class);
        JavaType refType = JavaType.construct(String.class);
        ReferenceType refTypeInstance = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        assertTrue(refTypeInstance.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        Class<?> cls = Integer.class;
        JavaType superClass = JavaType.construct(Object.class);
        JavaType refType = JavaType.construct(String.class);
        ReferenceType refTypeInstance = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        assertTrue(refTypeInstance.hasContentType());
    }

    @Test
    public void testGetContentType() {
        Class<?> cls = Integer.class;
        JavaType superClass = JavaType.construct(Object.class);
        JavaType refType = JavaType.construct(String.class);
        ReferenceType refTypeInstance = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        assertEquals(refType, refTypeInstance.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = Integer.class;
        JavaType superClass = JavaType.construct(Object.class);
        JavaType refType = JavaType.construct(String.class);
        ReferenceType refTypeInstance = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        assertEquals(refType, refTypeInstance.getReferencedType());
    }

    @Test
    public void testGetAnchorType() {
        Class<?> cls = Integer.class;
        JavaType superClass = JavaType.construct(Object.class);
        JavaType refType = JavaType.construct(String.class);
        ReferenceType refTypeInstance = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        assertNull(refTypeInstance.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        Class<?> cls = Integer.class;
        JavaType superClass = JavaType.construct(Object.class);
        JavaType refType = JavaType.construct(String.class);
        ReferenceType refTypeInstance = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        assertFalse(refTypeInstance.isAnchorType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = Integer.class;
        JavaType superClass = JavaType.construct(Object.class);
        JavaType refType = JavaType.construct(String.class);
        StringBuilder expectedSignature = new StringBuilder("[I");
        ReferenceType refTypeInstance = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        StringBuilder actualSignature = refTypeInstance.getErasedSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = Integer.class;
        JavaType superClass = JavaType.construct(Object.class);
        JavaType refType = JavaType.construct(String.class);
        StringBuilder expectedSignature = new StringBuilder("[I<Ljava.lang.String;>");
        ReferenceType refTypeInstance = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        StringBuilder actualSignature = refTypeInstance.getGenericSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }
}