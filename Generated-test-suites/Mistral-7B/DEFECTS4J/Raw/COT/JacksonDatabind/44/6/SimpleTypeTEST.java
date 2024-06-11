package com.fasterxml.jackson.databind.type; // make sure the test package is the same as the class package

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    // Test constructor with Class
    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Test constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInterfaces
    @Test
    public void testSimpleTypeConstructorWithArguments() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInterfaces);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
    }

    // Test copy constructor
    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType original = new SimpleType(cls, bindings, superClass, superInterfaces);
        SimpleType copy = new SimpleType(original);
        assertEquals(original.getRawClass(), copy.getRawClass());
        assertEquals(original.getSuperClass(), copy.getSuperClass());
        assertArrayEquals(original.getSuperInterfaces(), copy.getSuperInterfaces());
    }

    // Test constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInterfaces, int extraHash, Object valueHandler, Object typeHandler, boolean asStatic
    @Test
    public void testSimpleTypeConstructorWithAllArguments() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInterfaces, 0, valueHandler, typeHandler, asStatic);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(asStatic, simpleType.isStatic());
    }

    // Test static method constructUnsafe
    @Test
    public void testSimpleTypeConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleType.getSuperInterfaces());
    }

    // Test static method construct
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructDeprecated() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    // Test narrow method
    @Test
    public void testSimpleTypeNarrow() {
        Class<?> superClass = Object.class;
        Class<?> subclass = String.class;
        SimpleType simpleType = new SimpleType(superClass);
        SimpleType narrowed = simpleType.narrow(subclass);
        assertEquals(subclass, narrowed.getRawClass());
        assertEquals(simpleType.getRawClass(), narrowed.getSuperClass());
        assertArrayEquals(new JavaType[0], narrowed.getSuperInterfaces());
    }

    // Test withContentType method
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(TypeFactory.unknownType());
    }

    // Test withTypeHandler method
    @Test
    public void testSimpleTypeWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertEquals(handler, withHandler.getTypeHandler());
        assertNotSame(simpleType, withHandler);
    }

    // Test withContentTypeHandler method
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentTypeHandler(new Object());
    }

    // Test withValueHandler method
    @Test
    public void testSimpleTypeWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withHandler = simpleType.withValueHandler(handler);
        assertEquals(handler, withHandler.getValueHandler());
        assertNotSame(simpleType, withHandler);
    }

    // Test withContentValueHandler method
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler(new Object());
    }

    // Test withStaticTyping method
    @Test
    public void testSimpleTypeWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticTyping = simpleType.withStaticTyping();
        assertTrue(staticTyping.isStatic());
        assertNotSame(simpleType, staticTyping);
    }

    // Test refine method
    @Test
    public void testSimpleTypeRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(rawType, bindings, superClass, superInterfaces);
        assertNull(simpleType.refine(rawType, bindings, superClass, superInterfaces));
    }

    // Test buildCanonicalName method
    @Test
    public void testSimpleTypeBuildCanonicalName() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals(canonicalName, cls.getName());
    }

    
}