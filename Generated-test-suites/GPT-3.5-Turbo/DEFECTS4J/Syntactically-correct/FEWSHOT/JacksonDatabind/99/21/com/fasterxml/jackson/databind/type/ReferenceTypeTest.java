package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ReferenceTypeTest {
    private ReferenceType referenceType;
    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType(String.class);
        referenceType = new ReferenceType(simpleType, simpleType);
    }

    @Test
    public void testConstructor() {
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(simpleType.getTypeBindings(), referenceType.getTypeBindings());
        assertEquals(simpleType.superInterfaceCount(), referenceType.superInterfaceCount());
        assertEquals(simpleType.getContentType(), referenceType.getContentType());
        assertEquals(simpleType.getValueHandler(), referenceType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), referenceType.getTypeHandler());
        assertFalse(referenceType.isJavaLangObject());
        assertFalse(referenceType.isTyped());
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        ReferenceType upgradedType = ReferenceType.upgradeFrom(simpleType, simpleType);
        assertEquals(simpleType.getRawClass(), upgradedType.getRawClass());
        assertEquals(simpleType.getTypeBindings(), upgradedType.getTypeBindings());
        assertEquals(simpleType.superInterfaceCount(), upgradedType.superInterfaceCount());
        assertEquals(simpleType.getContentType(), upgradedType.getContentType());
        assertEquals(simpleType.getValueHandler(), upgradedType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), upgradedType.getTypeHandler());
        assertFalse(upgradedType.isJavaLangObject());
        assertFalse(upgradedType.isTyped());
        assertNull(upgradedType.getAnchorType());
    }

    @Test
    public void testConstruct() {
        ReferenceType newType = ReferenceType.construct(String.class, null);
        assertEquals(String.class, newType.getRawClass());
        assertNull(newType.getTypeBindings());
        assertNull(newType.superClass());
        assertEquals(0, newType.superInterfaceCount());
        assertNull(newType.getContentType());
        assertNull(newType.getValueHandler());
        assertNull(newType.getTypeHandler());
        assertFalse(newType.isJavaLangObject());
        assertFalse(newType.isTyped());
        assertNull(newType.getAnchorType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(Integer.class);
        ReferenceType newType = referenceType.withContentType(contentType);
        assertEquals(contentType, newType.getContentType());
        assertEquals(referenceType.getValueHandler(), newType.getValueHandler());
        assertEquals(referenceType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(referenceType.getRawClass(), newType.getRawClass());
        assertEquals(referenceType.getTypeBindings(), newType.getTypeBindings());
        assertEquals(referenceType.superClass(), newType.superClass());
        assertEquals(referenceType.superInterfaceCount(), newType.superInterfaceCount());
        assertEquals(referenceType.isJavaLangObject(), newType.isJavaLangObject());
        assertEquals(referenceType.isTyped(), newType.isTyped());
        assertEquals(referenceType.getAnchorType(), newType.getAnchorType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Integer(1);
        ReferenceType newType = referenceType.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
        assertEquals(referenceType.getContentType(), newType.getContentType());
        assertEquals(referenceType.getValueHandler(), newType.getValueHandler());
        assertEquals(referenceType.getRawClass(), newType.getRawClass());
        assertEquals(referenceType.getTypeBindings(), newType.getTypeBindings());
        assertEquals(referenceType.superClass(), newType.superClass());
        assertEquals(referenceType.superInterfaceCount(), newType.superInterfaceCount());
        assertEquals(referenceType.isJavaLangObject(), newType.isJavaLangObject());
        assertEquals(referenceType.isTyped(), newType.isTyped());
        assertEquals(referenceType.getAnchorType(), newType.getAnchorType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Integer(1);
        ReferenceType newType = referenceType.withContentTypeHandler(handler);
        assertEquals(handler, newType.getContentType().getTypeHandler());
        assertEquals(referenceType.getContentType(), newType.getContentType());
        assertEquals(referenceType.getRawClass(), newType.getRawClass());
        assertEquals(referenceType.getTypeBindings(), newType.getTypeBindings());
        assertEquals(referenceType.superClass(), newType.superClass());
        assertEquals(referenceType.superInterfaceCount(), newType.superInterfaceCount());
        assertEquals(referenceType.isJavaLangObject(), newType.isJavaLangObject());
        assertEquals(referenceType.isTyped(), newType.isTyped());
        assertEquals(referenceType.getAnchorType(), newType.getAnchorType());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType newType = referenceType.withStaticTyping();
        assertTrue(newType.isTyped());
        assertEquals(referenceType.getRawClass(), newType.getRawClass());
        assertEquals(referenceType.getTypeBindings(), newType.getTypeBindings());
        assertEquals(referenceType.superClass(), newType.superClass());
        assertEquals(referenceType.superInterfaceCount(), newType.superInterfaceCount());
        assertEquals(referenceType.getContentType(), newType.getContentType());
        assertEquals(referenceType.getValueHandler(), newType.getValueHandler());
        assertEquals(referenceType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(referenceType.getContentType(), newType.getContentType());
        assertEquals(referenceType.isJavaLangObject(), newType.isJavaLangObject());
        assertEquals(referenceType.getAnchorType(), newType.getAnchorType());
    }

    @Test
    public void testRefine() {
        JavaType superClass = new SimpleType(Number.class);
        JavaType[] interfaces = { new SimpleType(Comparable.class) };
        ReferenceType newType = referenceType.refine(Object.class, null, superClass, interfaces);
        assertEquals(referenceType.getRawClass(), newType.getRawClass());
        assertEquals(referenceType.getTypeBindings(), newType.getTypeBindings());
        assertEquals(superClass, newType.superClass());
        assertEquals(interfaces.length, newType.superInterfaceCount());
        assertEquals(interfaces[0], newType.superInterface(0));
        assertEquals(referenceType.getContentType(), newType.getContentType());
        assertEquals(referenceType.getValueHandler(), newType.getValueHandler());
        assertEquals(referenceType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(referenceType.isJavaLangObject(), newType.isJavaLangObject());
        assertEquals(referenceType.isTyped(), newType.isTyped());
        assertEquals(referenceType.getAnchorType(), newType.getAnchorType());
    }

    @Test
    public void testToString() {
        assertEquals("[reference type, class java.lang.String<String>]", referenceType.toString());
    }
}