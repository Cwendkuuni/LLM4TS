package com.example.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<String> cls = String.class;
        SimpleType type = SimpleType.constructUnsafe(cls);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidType() {
        Class<List<Integer>> cls = new ArrayList<Integer>().getClass();
        SimpleType.construct(cls); // should not compile
    }

    @Test
    public void testNarrow() {
        Class<String> stringType = String.class;
        Class<Integer> intType = Integer.class;
        SimpleType stringSimpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), null, null);
        SimpleType intSimpleType = new SimpleType(intType, TypeBindings.emptyBindings(), null, null);

        JavaType narrowedType = stringSimpleType._narrow(intType);
        assertNull(narrowedType);

        JavaType narrowedType2 = intSimpleType._narrow(stringType);
        assertSame(intSimpleType, narrowedType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidType() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), null, null);
        JavaType invalidType = new Object() {}.getClass();
        JavaType narrowedType = stringSimpleType._narrow(invalidType); // should not compile
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertThrows(IllegalArgumentException.class, () -> type.withContentTypeHandler(new Object()));
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        JavaType typeHandler = TypeFactory.defaultInstance().constructType(Object.class);
        SimpleType result = type.withTypeHandler(typeHandler);
        assertSame(type, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object valueHandler = new Object();
        SimpleType result = type.withValueHandler(valueHandler);
        assertSame(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        SimpleType type = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType result = type.withStaticTyping();
        assertSame(type, result);
    }

    @Test
    public void testRefine() {
        Class<String> rawType = String.class;
        JavaType superClass = TypeFactory.defaultInstance().constructType(List.class);
        JavaType[] superInterfaces = new JavaType[] { TypeFactory.defaultInstance().constructType(TypeDeserializer.class) };
        SimpleType type = new SimpleType(rawType, TypeBindings.emptyBindings(), null, superClass, null, null, false);
        assertNull(type.refine(rawType, TypeBindings.emptyBindings(), null, superInterfaces));
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<String>> listOfStringType = new ArrayList<String>().getClass();
        SimpleType simpleType = new SimpleType(listOfStringType, TypeBindings.emptyBindings(), null, null);
        String canonicalName = simpleType.buildCanonicalName();
        assertTrue(canonicalName.contains("java.util.List"));
        assertTrue(canonicalName.contains("java.lang.String"));
    }

    @Test
    public void testIsContainerType() {
        Class<List<String>> listOfStringType = new ArrayList<String>().getClass();
        SimpleType simpleType = new SimpleType(listOfStringType, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<List<String>> listOfStringType = new ArrayList<String>().getClass();
        SimpleType simpleType = new SimpleType(listOfStringType, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("[java.util.List]", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<List<String>>> listOfListOfStringType = new ArrayList<List<String>>().getClass();
        SimpleType simpleType = new SimpleType(listOfListOfStringType, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[java.util.List, java.util.List<java.lang.String>]", sb.toString());
    }
}