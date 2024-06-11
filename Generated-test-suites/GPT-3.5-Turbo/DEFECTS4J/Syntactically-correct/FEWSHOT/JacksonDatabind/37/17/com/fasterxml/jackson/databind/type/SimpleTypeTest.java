package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

import java.util.Collection;
import java.util.Map;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
    }

    @Test
    public void testConstruct_withCollection() {
        try {
            SimpleType.construct(Collection.class);
            // Should throw exception
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not construct SimpleType for a Collection"));
        }
    }

    @Test
    public void testConstruct_withMap() {
        try {
            SimpleType.construct(Map.class);
            // Should throw exception
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not construct SimpleType for a Map"));
        }
    }

    @Test
    public void testConstruct_withArray() {
        try {
            SimpleType.construct(Object[].class);
            // Should throw exception
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not construct SimpleType for an array"));
        }
    }

    @Test
    public void test_narrow() {
        SimpleType baseType = SimpleType.constructUnsafe(Object.class);
        SimpleType type = (SimpleType) baseType._narrow(String.class);
        assertEquals(String.class, type.getRawClass());
    }
    
    @Test
    public void test_withTypeHandler() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType withHandler = baseType.withTypeHandler(handler);
        assertEquals(handler, withHandler.getTypeHandler());
    }

    @Test
    public void test_withContentValueHandler() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        try {
            baseType.withContentValueHandler(handler);
            // Should throw exception
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Simple types have no content types"));
        }
    }

    @Test
    public void test_withStaticTyping() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        SimpleType staticType = baseType.withStaticTyping();
        assertTrue(staticType.isStatic());
    }

    @Test
    public void test_refine() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = baseType.refine(Object.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void test_isContainerType() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        assertFalse(baseType.isContainerType());
    }

    @Test
    public void test_getErasedSignature() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        assertEquals("Ljava/lang/String;", baseType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void test_getGenericSignature() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        assertEquals("Ljava/lang/String;", baseType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void test_toString() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", baseType.toString());
    }

    @Test
    public void test_equals_sameInstance() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        assertTrue(baseType.equals(baseType));
    }

    @Test
    public void test_equals_null() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        assertFalse(baseType.equals(null));
    }

    @Test
    public void test_equals_differentClass() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        assertFalse(baseType.equals(new Object()));
    }

    @Test
    public void test_equals_differentRawClass() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        SimpleType otherType = SimpleType.constructUnsafe(Integer.class);
        assertFalse(baseType.equals(otherType));
    }

    @Test
    public void test_equals_sameRawClass_differentBindings() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        SimpleType otherType = SimpleType.constructUnsafe(String.class);
        assertFalse(baseType.equals(otherType));
    }

    @Test
    public void test_equals_sameRawClass_sameBindings() {
        SimpleType baseType = SimpleType.constructUnsafe(String.class);
        SimpleType otherType = SimpleType.constructUnsafe(String.class);
        assertTrue(baseType.equals(otherType));
    }
}