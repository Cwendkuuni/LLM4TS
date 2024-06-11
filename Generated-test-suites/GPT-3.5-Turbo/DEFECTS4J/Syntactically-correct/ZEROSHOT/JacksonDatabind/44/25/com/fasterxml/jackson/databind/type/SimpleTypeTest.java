package com.fasterxml.jackson.databind.type;


import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertTrue(simpleType instanceof SimpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(null, simpleType.getBindings());
        assertEquals(TypeFactory.unknownType(), simpleType.getSuperClass());
        assertEquals(null, simpleType.getInterfaces());
        assertEquals(null, simpleType.getValueHandler());
        assertEquals(null, simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(int[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(Object.class);
        assertTrue(narrowedType instanceof SimpleType);
        assertEquals(Object.class, narrowedType.getRawClass());
        assertEquals(simpleType.getBindings(), narrowedType.getBindings());
        assertEquals(simpleType.getSuperClass(), narrowedType.getSuperClass());
        assertArrayEquals(simpleType.getInterfaces(), narrowedType.getInterfaces());
        assertEquals(simpleType.getValueHandler(), narrowedType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), narrowedType.getTypeHandler());
        assertEquals(simpleType.isStatic(), narrowedType.isStatic());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentType(simpleType);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType typeWithHandler = simpleType.withTypeHandler("handler");
        assertTrue(typeWithHandler instanceof SimpleType);
        assertEquals(String.class, typeWithHandler.getRawClass());
        assertEquals(simpleType.getBindings(), typeWithHandler.getBindings());
        assertEquals(simpleType.getSuperClass(), typeWithHandler.getSuperClass());
        assertArrayEquals(simpleType.getInterfaces(), typeWithHandler.getInterfaces());
        assertEquals(simpleType.getValueHandler(), typeWithHandler.getValueHandler());
        assertEquals("handler", typeWithHandler.getTypeHandler());
        assertEquals(simpleType.isStatic(), typeWithHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentTypeHandler("handler");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType typeWithValueHandler = simpleType.withValueHandler("handler");
        assertTrue(typeWithValueHandler instanceof SimpleType);
        assertEquals(String.class, typeWithValueHandler.getRawClass());
        assertEquals(simpleType.getBindings(), typeWithValueHandler.getBindings());
        assertEquals(simpleType.getSuperClass(), typeWithValueHandler.getSuperClass());
        assertArrayEquals(simpleType.getInterfaces(), typeWithValueHandler.getInterfaces());
        assertEquals("handler", typeWithValueHandler.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), typeWithValueHandler.getTypeHandler());
        assertEquals(simpleType.isStatic(), typeWithValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentValueHandler("handler");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType instanceof SimpleType);
        assertEquals(String.class, staticType.getRawClass());
        assertEquals(simpleType.getBindings(), staticType.getBindings());
        assertEquals(simpleType.getSuperClass(), staticType.getSuperClass());
        assertArrayEquals(simpleType.getInterfaces(), staticType.getInterfaces());
        assertEquals(simpleType.getValueHandler(), staticType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), staticType.getTypeHandler());
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    private static String _classSignature(Class<?> cls, StringBuilder sb, boolean erase) {
        if (cls.isPrimitive()) {
            if (cls == Integer.TYPE) {
                sb.append('I');
            } else if (cls == Long.TYPE) {
                sb.append('J');
            } else if (cls == Boolean.TYPE) {
                sb.append('Z');
            } else if (cls == Double.TYPE) {
                sb.append('D');
            } else if (cls == Float.TYPE) {
                sb.append('F');
            } else if (cls == Byte.TYPE) {
                sb.append('B');
            } else if (cls == Short.TYPE) {
                sb.append('S');
            } else if (cls == Character.TYPE) {
                sb.append('C');
            } else if (cls == Void.TYPE) {
                sb.append('V');
            } else {
                throw new IllegalStateException("Unrecognized primitive type: " + cls.getName());
            }
        } else {
            sb.append('L');
            String name = cls.getName();
            for (int i = 0, len = name.length(); i < len; ++i) {
                char c = name.charAt(i);
                if (c == '.') {
                    c = '/';
                }
                sb.append(c);
            }
            if (!erase) {
                sb.append(';');
            }
        }
        return sb.toString();
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType sameType = SimpleType.constructUnsafe(String.class);
        SimpleType differentType = SimpleType.constructUnsafe(Long.class);

        assertTrue(simpleType.equals(simpleType));
        assertTrue(simpleType.equals(sameType));
        assertFalse(simpleType.equals(differentType));
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals(new Object()));
    }
}