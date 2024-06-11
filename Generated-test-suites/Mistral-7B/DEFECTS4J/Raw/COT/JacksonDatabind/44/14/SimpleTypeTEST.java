package com.example; // replace with your package name

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    // Test constructor with a Class
    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(intClass);
        assertNotNull(simpleType);
        assertEquals(intClass, simpleType.getRawClass());
        assertEquals(Integer.TYPE, simpleType.getRawClass().getTypeName());
        assertEquals(intClass.getName(), simpleType.getClassName());
    }

    // Test constructor with Class, TypeBindings, superClass, and superInterfaces
    @Test
    public void testSimpleTypeConstructorWithClassAndBindings() {
        Class<Integer> intClass = Integer.class;
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        SimpleType simpleType = new SimpleType(intClass, TypeBindings.emptyBindings(), superClass, null);
        assertNotNull(simpleType);
        assertEquals(intClass, simpleType.getRawClass());
        assertEquals(Integer.TYPE, simpleType.getRawClass().getTypeName());
        assertEquals(intClass.getName(), simpleType.getClassName());
    }

    // Test copy-constructor
    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<Integer> intClass = Integer.class;
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        SimpleType simpleType = new SimpleType(intClass, TypeBindings.emptyBindings(), superClass, null);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertNotNull(copiedSimpleType);
        assertEquals(simpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(simpleType.getClass(), copiedSimpleType.getClass());
    }

    // Test constructUnsafe method
    @Test
    public void testSimpleTypeConstructUnsafe() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = SimpleType.constructUnsafe(intClass);
        assertNotNull(simpleType);
        assertEquals(intClass, simpleType.getRawClass());
        assertEquals(Integer.TYPE, simpleType.getRawClass().getTypeName());
        assertEquals(intClass.getName(), simpleType.getClassName());
    }

    // Test construct method (deprecated)
    @Test
    public void testSimpleTypeConstructDeprecated() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = SimpleType.construct(intClass);
        assertNotNull(simpleType);
        assertEquals(intClass, simpleType.getRawClass());
        assertEquals(Integer.TYPE, simpleType.getRawClass().getTypeName());
        assertEquals(intClass.getName(), simpleType.getClassName());
    }

    // Test _narrow method
    @Test
    public void testSimpleTypeNarrow() {
        Class<Integer> intClass = Integer.class;
        Class<String> stringClass = String.class;
        SimpleType intSimpleType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
        SimpleType stringSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType narrowedIntSimpleType = intSimpleType._narrow(intClass);
        SimpleType narrowedStringSimpleType = intSimpleType._narrow(stringClass);
        assertSame(intSimpleType, narrowedIntSimpleType);
        assertNotSame(intSimpleType, narrowedStringSimpleType);
    }

    // Test withContentType method
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null);
        simpleType.withContentType(TypeFactory.defaultInstance().constructType(String.class));
    }

    // Test withContentTypeHandler method
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null);
        simpleType.withContentTypeHandler(Object.class);
    }

    // Test withTypeHandler method
    @Test
    public void testSimpleTypeWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null);
        Object typeHandler = new Object() {};
        SimpleType withTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    // Test withValueHandler method
    @Test
    public void testSimpleTypeWithValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null);
        Object valueHandler = new Object() {};
        SimpleType withValueHandler = simpleType.withValueHandler(valueHandler);
        assertNotSame(simpleType, withValueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    // Test withStaticTyping method
    @Test
    public void testSimpleTypeWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertNotSame(simpleType, withStaticTyping);
        assertTrue(withStaticTyping.isStatic());
    }

    // Test refine method
    @Test
    public void testSimpleTypeRefine() {
        Class<Integer> intClass = Integer.class;
        JavaType javaInt = TypeFactory.defaultInstance().constructType(intClass);
        SimpleType simpleType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
        assertNull(simpleType.refine(intClass, TypeBindings.emptyBindings(), null, null));
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNotNull(refinedType);
        assertEquals(String.class, refinedType.getRawClass());
    }

    // Test buildCanonicalName method
    @Test
    public void testSimpleTypeBuildCanonicalName() {
        Class<Integer> intClass = Integer.class;
        Class<String> stringClass = String.class;
        SimpleType intSimpleType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
        SimpleType stringSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType intWithGenericSimpleType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, Arrays.asList(TypeFactory.defaultInstance().constructType(String.class)));
        assertEquals("java.lang.Integer", intSimpleType.buildCanonicalName());
        assertEquals("java.lang.Integer[]", intSimpleType.buildCanonicalName(new StringBuilder()).toString());
        assertEquals("java.lang.Integer<java.lang.String>", intWithGenericSimpleType.buildCanonicalName());
        assertEquals("java.lang.String", stringSimpleType.buildCanonicalName());
        assertEquals("java.lang.String[]", stringSimpleType.buildCanonicalName(new StringBuilder()).toString());
    }

    
}