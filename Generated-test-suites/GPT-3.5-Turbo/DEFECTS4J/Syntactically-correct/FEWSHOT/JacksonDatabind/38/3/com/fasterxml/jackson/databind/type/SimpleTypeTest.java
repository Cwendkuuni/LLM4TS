package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType type = new SimpleType(String.class);

        assertEquals(String.class, type.getRawClass());
        assertEquals(0, type.getBindings().size());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructorWithBindingsAndSuperClassAndSuperInterfaces() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType[] superInterfaces = new SimpleType[0];
        SimpleType type = new SimpleType(String.class, bindings, superClass, superInterfaces);

        assertEquals(String.class, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(superInterfaces, type.getSuperInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructorWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType type = new SimpleType(String.class).withTypeHandler(typeHandler);

        assertEquals(typeHandler, type.getTypeHandler());
    }

    @Test
    public void testConstructorWithStaticTyping() {
        SimpleType type = new SimpleType(String.class).withStaticTyping();

        assertTrue(type.isContainerType());
    }

    @Test
    public void testGetContentType() {
        SimpleType type = new SimpleType(String.class);

        assertNull(type.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContentTypeWithException() {
        SimpleType type = new SimpleType(String.class);
        type.getContentType();
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        SimpleType type = new SimpleType(String.class);

        try {
            type.withContentTypeHandler(handler);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        SimpleType type = new SimpleType(String.class);

        try {
            type.withContentValueHandler(handler);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testRefine() {
        SimpleType type = new SimpleType(String.class);
        JavaType result = type.refine(String.class, TypeBindings.emptyBindings(), null, null);

        assertNull(result);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType type = new SimpleType(String.class);
        type.getBindings().addType(String.class);

        assertEquals("java.lang.String<java.lang.String>", type.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(String.class);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);

        assertEquals(_classSignature(String.class, new StringBuilder(), true).toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(String.class);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);

        assertEquals(_classSignature(String.class, new StringBuilder(), false).toString() + ";", result.toString());
    }

    @Test
    public void testEqualsWithSameObject() {
        SimpleType type = new SimpleType(String.class);

        assertTrue(type.equals(type));
    }

    @Test
    public void testEqualsWithNull() {
        SimpleType type = new SimpleType(String.class);

        assertFalse(type.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        SimpleType type = new SimpleType(String.class);
        SimpleType other = new SimpleType(Integer.class);

        assertFalse(type.equals(other));
    }

    @Test
    public void testEqualsWithDifferentBindings() {
        SimpleType type = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType other = new SimpleType(String.class, new TypeBindings(), null, null);

        assertFalse(type.equals(other));
    }

    @Test
    public void testEqualsWithSameClassAndBindings() {
        SimpleType type = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType other = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);

        assertTrue(type.equals(other));
    }

    @Test
    public void testToString() {
        SimpleType type = new SimpleType(String.class);

        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testEqualsWithDifferentSuperClass() {
        SimpleType superClass1 = new SimpleType(Object.class);
        SimpleType superClass2 = new SimpleType(Number.class);

        SimpleType type1 = new SimpleType(String.class, TypeBindings.emptyBindings(), superClass1, null);
        SimpleType type2 = new SimpleType(String.class, TypeBindings.emptyBindings(), superClass2, null);

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEqualsWithNullSuperClass() {
        SimpleType superClass = new SimpleType(Object.class);

        SimpleType type1 = new SimpleType(String.class, TypeBindings.emptyBindings(), superClass, null);
        SimpleType type2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testConstructorWithBase() {
        TypeBase base = new TypeBase(String.class);

        new SimpleType(base);
    }

    @Test
    public void testConstructorWithTypeHandlerAndValueHandlerAndAsStatic() {
        Object typeHandler = new Object();
        Object valueHandler = new Object();
        SimpleType type = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, valueHandler, typeHandler, true);

        assertEquals(typeHandler, type.getTypeHandler());
        assertEquals(valueHandler, type.getValueHandler());
    }

    @Test
    public void testConstructorWithExtraHash() {
        int extraHash = 10;
        SimpleType type = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, extraHash, null, null, false);

        assertEquals(extraHash, type.getExtraHash());
    }

    @Test
    public void testConstructorWithSuperInts() {
        SimpleType superClass = new SimpleType(Object.class);
        SimpleType[] superInts = new SimpleType[2];
        SimpleType type = new SimpleType(String.class, TypeBindings.emptyBindings(), superClass, superInts);

        assertEquals(superClass, type.getSuperClass());
        assertEquals(superInts, type.getSuperInterfaces());
    }

    private StringBuilder _classSignature(Class<?> cls, StringBuilder sb, boolean isErased) {
        sb.append(cls.getName());
        if (!isErased) {
            final int count = cls.getTypeParameters().length;
            if (count > 0) {
                sb.append('<');
                for (int i = 0; i < count; ++i) {
                    if (i > 0) {
                        sb.append(',');
                    }
                    sb.append('?');
                }
                sb.append('>');
            }
        }
        return sb;
    }

}