package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getTypeBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());

        SimpleType simpleTypeWithBindings = new SimpleType(String.class, TypeBindings.emptyBindings());
        assertEquals(String.class, simpleTypeWithBindings.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithBindings.getTypeBindings());
        assertNull(simpleTypeWithBindings.getSuperClass());
        assertNull(simpleTypeWithBindings.getSuperInterfaces());
        assertFalse(simpleTypeWithBindings.isStatic());

        SimpleType simpleTypeWithSuper = new SimpleType(String.class, TypeBindings.emptyBindings(),
                JavaType.construct(Collection.class), new JavaType[0]);
        assertEquals(String.class, simpleTypeWithSuper.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithSuper.getTypeBindings());
        assertEquals(Collection.class, simpleTypeWithSuper.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleTypeWithSuper.getSuperInterfaces());
        assertFalse(simpleTypeWithSuper.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        SimpleType original = new SimpleType(String.class);
        SimpleType copy = new SimpleType(original);

        assertEquals(original.getRawClass(), copy.getRawClass());
        assertEquals(original.getTypeBindings(), copy.getTypeBindings());
        assertEquals(original.getSuperClass(), copy.getSuperClass());
        assertArrayEquals(original.getSuperInterfaces(), copy.getSuperInterfaces());
        assertEquals(original.isStatic(), copy.isStatic());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        SimpleType baseType = new SimpleType(String.class);
        SimpleType narrowedType = baseType.narrow(String[].class);

        assertNotNull(narrowedType);
        assertEquals(String[].class, narrowedType.getRawClass());
        assertEquals(baseType.getTypeBindings(), narrowedType.getTypeBindings());
        assertNull(narrowedType.getSuperClass());
        assertArrayEquals(new JavaType[0], narrowedType.getSuperInterfaces());
        assertFalse(narrowedType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        new SimpleType(Map.class);
    }

    @Test
    public void testSimpleTypeGetCanonicalName() {
        SimpleType stringType = new SimpleType(String.class);
        SimpleType listStringType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                JavaType.construct(List.class), new JavaType[] { SimpleType.construct(String.class) });

        assertEquals("java.lang.String", stringType.getCanonicalName());
        assertEquals("java.util.List<java.lang.String>", listStringType.getCanonicalName());
    }

    @Test
    public void testSimpleTypeToString() {
        SimpleType stringType = new SimpleType(String.class);
        SimpleType listStringType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                JavaType.construct(List.class), new JavaType[] { SimpleType.construct(String.class) });

        assertEquals("[simple type, class java.lang.String]", stringType.toString());
        assertEquals("[simple type, class java.util.List<java.lang.String>]", listStringType.toString());
    }

    @Test
    public void testSimpleTypeEquals() {
        SimpleType stringType1 = new SimpleType(String.class);
        SimpleType stringType2 = new SimpleType(String.class);
        SimpleType listStringType1 = new SimpleType(String.class, TypeBindings.emptyBindings(),
                JavaType.construct(List.class), new JavaType[] { SimpleType.construct(String.class) });
        SimpleType listStringType2 = new SimpleType(String.class, TypeBindings.emptyBindings(),
                JavaType.construct(List.class), new JavaType[] { SimpleType.construct(String.class) });

        assertTrue(stringType1.equals(stringType2));
        assertTrue(listStringType1.equals(listStringType2));

        SimpleType differentClassType = new SimpleType(Integer.class);
        assertFalse(stringType1.equals(differentClassType));

        SimpleType differentBindingsType = new SimpleType(String.class, TypeBindings.emptyBindings());
        assertFalse(stringType1.equals(differentBindingsType));
    }

    @Test
    public void testSimpleTypeWithContentType() {
        SimpleType stringType = new SimpleType(String.class);

        try {
            stringType.withContentType(JavaType.construct(Integer.class));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        SimpleType stringType = new SimpleType(String.class);

        SimpleType stringTypeWithHandler = stringType.withTypeHandler(new TypeDeserializer() {});
        assertNotSame(stringType, stringTypeWithHandler);
        assertEquals(stringType.getRawClass(), stringTypeWithHandler.getRawClass());
        assertNotNull(stringTypeWithHandler.getTypeHandler());

        SimpleType stringTypeWithHandler2 = stringTypeWithHandler.withTypeHandler(new TypeDeserializer() {});
        assertNotSame(stringTypeWithHandler, stringTypeWithHandler2);
        assertEquals(stringTypeWithHandler.getRawClass(), stringTypeWithHandler2.getRawClass());
        assertNotNull(stringTypeWithHandler2.getTypeHandler());

        SimpleType stringTypeWithHandler3 = stringTypeWithHandler.withTypeHandler(null);
        assertNotSame(stringTypeWithHandler, stringTypeWithHandler3);
        assertEquals(stringTypeWithHandler.getRawClass(), stringTypeWithHandler3.getRawClass());
        assertNull(stringTypeWithHandler3.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        SimpleType stringType = new SimpleType(String.class);

        try {
            stringType.withContentTypeHandler(new TypeSerializer() {});
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        SimpleType stringType = new SimpleType(String.class);

        SimpleType stringTypeWithHandler = stringType.withValueHandler(new Object() {});
        assertNotSame(stringType, stringTypeWithHandler);
        assertEquals(stringType.getRawClass(), stringTypeWithHandler.getRawClass());
        assertNotNull(stringTypeWithHandler.getValueHandler());

        SimpleType stringTypeWithHandler2 = stringTypeWithHandler.withValueHandler(new Object() {});
        assertNotSame(stringTypeWithHandler, stringTypeWithHandler2);
        assertEquals(stringTypeWithHandler.getRawClass(), stringTypeWithHandler2.getRawClass());
        assertNotNull(stringTypeWithHandler2.getValueHandler());

        SimpleType stringTypeWithHandler3 = stringTypeWithHandler.withValueHandler(null);
        assertNotSame(stringTypeWithHandler, stringTypeWithHandler3);
        assertEquals(stringTypeWithHandler.getRawClass(), stringTypeWithHandler3.getRawClass());
        assertNull(stringTypeWithHandler3.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithContentValueHandler() {
        SimpleType stringType = new SimpleType(String.class);

        try {
            stringType.withContentValueHandler(new Object() {});
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        SimpleType stringType = new SimpleType(String.class);

        SimpleType stringTypeStatic = stringType.withStaticTyping();
        assertNotSame(stringType, stringTypeStatic);
        assertEquals(stringType.getRawClass(), stringTypeStatic.getRawClass());
        assertTrue(stringTypeStatic.isStatic());

        SimpleType stringTypeStatic2 = stringTypeStatic.withStaticTyping();
        assertSame(stringTypeStatic, stringTypeStatic2);
    }

    @Test
    public void testSimpleTypeRefine() {
        SimpleType stringType = new SimpleType(String.class);

        SimpleType refinedType = stringType.refine(String[].class, TypeBindings.emptyBindings(),
                JavaType.construct(Collection.class), new JavaType[0]);

        assertNull(refinedType);
    }

    @Test
    public void testSimpleTypeGetErasedSignature() {
        SimpleType stringType = new SimpleType(String.class);

        StringBuilder sb = new StringBuilder(50);
        stringType.getErasedSignature(sb);

        assertEquals("String", sb.toString());
    }

    @Test
    public void testSimpleTypeGetGenericSignature() {
        SimpleType stringType = new SimpleType(String.class);

        StringBuilder sb = new StringBuilder(50);
        stringType.getGenericSignature(sb);

        assertEquals("java.lang.String", sb.toString());
    }
}