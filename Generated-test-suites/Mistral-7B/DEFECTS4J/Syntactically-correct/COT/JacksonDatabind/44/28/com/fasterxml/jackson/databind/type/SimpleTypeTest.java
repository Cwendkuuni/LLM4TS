package com.example.simpletype;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    
    
    @Test
    public void testProtectedConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProtectedConstructorWithClass_Map() {
        new SimpleType(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProtectedConstructorWithClass_Collection() {
        new SimpleType(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProtectedConstructorWithClass_Array() {
        new SimpleType(int[].class);
    }

    @Test
    public void testProtectedConstructorWithClass_TypeBindings() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings());
        assertNotNull(simpleType);
    }

    @Test
    public void testCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType original = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType copied = new SimpleType(original);
        assertEquals(original, copied);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstruct_Map() {
        SimpleType simpleType = SimpleType.construct(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstruct_Collection() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstruct_Array() {
        SimpleType simpleType = SimpleType.construct(int[].class);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType narrowed = simpleType.narrow(subclass);
        assertEquals(subclass, narrowed._class);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(simpleType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType withTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(typeHandler, withTypeHandler._typeHandler);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping._asStatic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefine() {
        Class<?> rawType = List.class;
        SimpleType simpleType = new SimpleType(rawType, TypeBindings.emptyBindings(), null, null);
        JavaType refined = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refined);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        assertEquals("java.util.List<java.lang.Object>", simpleType.buildCanonicalName());

        JavaType elementType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType genericSimpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        SimpleType genericSimpleTypeWithElementType = new SimpleType(List.class, TypeBindings.bindValue(0, elementType), null, null);
        assertEquals("java.util.List<java.lang.String>", genericSimpleTypeWithElementType.buildCanonicalName());
    }

    
}