package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testReferenceTypeUpgradeFrom() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, referencedType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testReferenceTypeWithContentType() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        JavaType newContentType = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(Long.class, newReferenceType.getContentType().getRawClass());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object() {};
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, handler, false);
        JavaType newReferenceType = referenceType.withTypeHandler(null);
        assertNotSame(handler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object contentHandler = new Object() {};
        Object typeHandler = new Object() {};
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType, null, contentHandler, typeHandler, false);
        JavaType newReferenceType = referenceType.withContentTypeHandler(null);
        assertNotSame(contentHandler, newReferenceType.getContentTypeHandler());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object() {};
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType, null, handler, null, false);
        JavaType newReferenceType = referenceType.withValueHandler(null);
        assertNotSame(handler, newReferenceType.getValueHandler());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object contentHandler = new Object() {};
        Object valueHandler = new Object() {};
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, valueHandler, false);
        JavaType newReferenceType = referenceType.withContentValueHandler(null);
        assertNotSame(contentHandler, newReferenceType.getContentValueHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        JavaType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testReferenceTypeEquals() {
        JavaType referencedType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referencedType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType1 = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType1, null, null, null, false);
        JavaType referenceType2 = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType2, null, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        JavaType differentReferenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, referencedType1, null, null, null, false);
        assertFalse(referenceType1.equals(differentReferenceType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeUpgradeFromInvalidBaseType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType.upgradeFrom(simpleType, referencedType);
    }
}