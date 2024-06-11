package com.example.junit4; // replace with your package name

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType = new SimpleType(integerClass);
        SimpleType expectedSimpleType = new SimpleType(integerClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(expectedSimpleType.toString(), simpleType.toString());
    }

    @Test
    public void testConstructUnsafe() {
        try {
            Class<List<Integer>> listClass = ArrayList.class;
            SimpleType simpleType = SimpleType.constructUnsafe(listClass);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstruct() {
        Class<List<Integer>> listClass = ArrayList.class;
        SimpleType simpleType = SimpleType.construct(listClass);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType expectedSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, typeHandler, false);
        assertEquals(expectedSimpleType.toString(), simpleType.withTypeHandler(typeHandler).toString());
    }

    @Test
    public void testNarrow() {
        Class<Number> numberClass = Number.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType = new SimpleType(numberClass);
        JavaType narrowType = simpleType.narrow(integerClass);
        SimpleType expectedSimpleType = new SimpleType(integerClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals(expectedSimpleType.toString(), narrowType.toString());
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<List<Integer>>> listListClass = new ArrayList<List<Integer>>().getClass();
        SimpleType simpleType = new SimpleType(listListClass);
        String expectedCanonicalName = "[java.util.List, [java.util.List, java.lang.Integer]]";
        assertEquals(expectedCanonicalName, simpleType.buildCanonicalName());
    }

    @Test
    public void testToString() {
        Class<List<List<Integer>>> listListClass = new ArrayList<List<Integer>>().getClass();
        SimpleType simpleType = new SimpleType(listListClass);
        String expectedString = "[simple type, class [" + List.class.getName() + ", [" + List.class.getName() + ", java.lang.Integer]]]";
        assertEquals(expectedString, simpleType.toString());
    }

    @Test
    public void testEquals() {
        Class<List<List<Integer>>> listListClass1 = new ArrayList<List<Integer>>().getClass();
        Class<List<List<Integer>>> listListClass2 = new ArrayList<List<Integer>>().getClass();
        SimpleType simpleType1 = new SimpleType(listListClass1);
        SimpleType simpleType2 = new SimpleType(listListClass2);
        SimpleType simpleType3 = new SimpleType(listListClass1);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType3));
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testRefine() {
        Class<List<List<Integer>>> listListClass = new ArrayList<List<Integer>>().getClass();
        SimpleType simpleType = new SimpleType(listListClass);
        JavaType javaType = simpleType.refine(List.class, TypeBindings.emptyBindings(), null, null);
        assertNull(javaType);
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<List<Integer>>> listListClass = new ArrayList<List<Integer>>().getClass();
        SimpleType simpleType = new SimpleType(listListClass);
        SimpleType expectedSimpleType = new SimpleType(listListClass, TypeBindings.emptyBindings(), null, null, null, null, true);
        assertEquals(expectedSimpleType.toString(), simpleType.withStaticTyping().toString());
    }
}