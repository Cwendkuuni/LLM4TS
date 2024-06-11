package com.example.junit; // replace with your testing package

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
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls.getName(), simpleType.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidClass() {
        SimpleType.construct(Map.class);
    }

    @Test
    public void testCopyConstructor() {
        Class<String> cls = String.class;
        SimpleType parentType = new SimpleType(cls);
        SimpleType childType = new SimpleType(parentType);
        assertEquals(parentType.getName(), childType.getName());
    }

    @Test
    public void testNarrowMethod() {
        Class<String> parentClass = String.class;
        Class<StringBuilder> childClass = StringBuilder.class;
        SimpleType parentType = new SimpleType(parentClass);
        SimpleType childType = new SimpleType(childClass);
        SimpleType narrowedType = parentType.narrow(childClass);
        assertEquals(childType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        Class<String> parentClass = String.class;
        Class<Integer> childClass = Integer.class;
        SimpleType parentType = new SimpleType(parentClass);
        parentType.narrow(childClass);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        Object handler = new Object();
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertEquals(simpleType.getName(), withHandler.getName());
        assertEquals(handler, withHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidClass() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        simpleType.withTypeHandler(new Object()[0]);
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        Object handler = new Object();
        SimpleType withHandler = simpleType.withValueHandler(handler);
        assertEquals(simpleType.getName(), withHandler.getName());
        assertEquals(handler, withHandler.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidHandler() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        simpleType.withValueHandler(new Object()[0]);
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType staticType = simpleType.withStaticTyping();
        assertEquals(simpleType.getName(), staticType.getName());
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testConstructUnsafeMethod() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls.getName(), simpleType.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructMethodDeprecated() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test
    public void testRefineMethod() {
        Class<String> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unconstructedListType(Integer.class);
        JavaType[] superInterfaces = new JavaType[]{TypeFactory.unconstructedMapType(String.class, Integer.class)};
        SimpleType parentType = new SimpleType(rawType, bindings, superClass, superInterfaces);
        JavaType refinedType = parentType.refine(rawType, bindings, null, null);
        assertTrue(refinedType == null);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<List<String>>> cls = Arrays.asList(Arrays.asList(String.class)).getClass();
        SimpleType simpleType = new SimpleType(cls);
        String canonicalName = simpleType.buildCanonicalName();
        assertTrue(canonicalName.contains(List.class.getName()));
        assertTrue(canonicalName.contains(String.class.getName()));
    }

    
}