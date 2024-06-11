package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

/**
 * Test class for SimpleType
 */
public class SimpleTypeTest {

    /**
     * Test constructor with single argument
     */
    @Test
    public void testConstructorWithSingleArgument() {
        Class<?> cls = Integer.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType javaType = TypeFactory.defaultInstance().constructType(cls);
        SimpleType expected = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(expected, simpleType);
        assertEquals(javaType, simpleType.getRawType());
    }

    /**
     * Test constructor with multiple arguments
     */
    @Test
    public void testConstructorWithMultipleArguments() {
        Class<?> cls = Integer.class;
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), superClass, superInterfaces);
        SimpleType expected = new SimpleType(cls, TypeBindings.emptyBindings(), superClass, superInterfaces, null, null, false);
        assertEquals(expected, simpleType);
        assertEquals(javaType, simpleType.getRawType());
    }

    /**
     * Test copy constructor
     */
    @Test
    public void testCopyConstructor() {
        Class<?> cls = Integer.class;
        SimpleType source = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType target = new SimpleType(source);
        assertEquals(source, target);
        assertEquals(source.getRawType(), target.getRawType());
    }

    /**
     * Test method constructUnsafe
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithInvalidClass() {
        Class<?> cls = Map.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    /**
     * Test method construct
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidClass() {
        Class<?> cls = Map.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    /**
     * Test method _narrow
     */
    @Test
    public void testNarrow() {
        Class<?> cls = Integer.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType subclassType = new SimpleType(Integer[].class);
        SimpleType expected = new SimpleType(Integer[].class, TypeBindings.emptyBindings(), simpleType.getRawType(), new JavaType[0]);
        SimpleType result = simpleType._narrow(Integer[].class);
        assertEquals(expected, result);
    }

    /**
     * Test method withContentType
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        simpleType.withContentType(TypeFactory.defaultInstance().constructType(String.class));
    }

    /**
     * Test method withTypeHandler
     */
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        Object handler = new Object();
        SimpleType expected = new SimpleType(Integer.class, TypeBindings.emptyBindings(), simpleType.getRawType(), new JavaType[0], null, handler, false);
        SimpleType result = simpleType.withTypeHandler(handler);
        assertEquals(expected, result);
    }

    /**
     * Test method withContentTypeHandler
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        simpleType.withContentTypeHandler(TypeFactory.defaultInstance().constructType(String.class));
    }

    /**
     * Test method withValueHandler
     */
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        Object handler = new Object();
        SimpleType expected = new SimpleType(Integer.class, TypeBindings.emptyBindings(), simpleType.getRawType(), new JavaType[0], handler, null, false);
        SimpleType result = simpleType.withValueHandler(handler);
        assertEquals(expected, result);
    }

    /**
     * Test method withStaticTyping
     */
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Integer.class);
        SimpleType expected = new SimpleType(Integer.class, TypeBindings.emptyBindings(), simpleType.getRawType(), new JavaType[0], null, null, true);
        SimpleType result = simpleType.withStaticTyping();
        assertEquals(expected, result);
    }

    /**
     * Test method refine
     */
    @Test
    public void testRefine() {
        Class<?> rawType = Integer[].class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(rawType);
        SimpleType result = simpleType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    /**
     * Test method buildCanonicalName
     */
    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = Integer.class;
        SimpleType simpleType = new SimpleType(cls);
        String expected = "[simple type, class " + cls.getName() + "]";
        String result = simpleType.toString();
        assertEquals(expected, result);
    }

    /**
     * Test method equals
     */
    @Test
    public void testEquals() {
        Class<?> cls = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = new SimpleType(cls);
        SimpleType simpleType3 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}