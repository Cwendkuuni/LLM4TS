package com.yourpackage.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

/**
 * Test file for SimpleType class
 */
public class SimpleTypeTest {

    /**
     * Test constructor with Class parameter
     */
    @Test
    public void testSimpleTypeConstructionWithClass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals("SimpleType for String", simpleType.getTypeDescription().getRawClass(), stringClass);
    }

    /**
     * Test constructor with Class, TypeBindings, JavaType superClass and JavaType[] superInterfaces
     */
    @Test
    public void testSimpleTypeConstructionWithClassBindingsSuperClassSuperInterfaces() {
        Class<String> stringClass = String.class;
        JavaType superClass = JavaType.construct(ArrayList.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.construct(Comparable.class) };
        SimpleType simpleType = new SimpleType(stringClass, null, superClass, superInterfaces);
        assertEquals("SimpleType for String with superClass and superInterfaces", simpleType.getTypeDescription().getRawClass(), stringClass);
        assertEquals("SuperClass", simpleType.getSuperClass(), superClass);
        assertEquals("SuperInterfaces", simpleType.getSuperInterfaces(), Arrays.asList(superInterfaces));
    }

    /**
     * Test constructor with TypeBase
     */
    @Test
    public void testSimpleTypeConstructionWithTypeBase() {
        Class<String> stringClass = String.class;
        SimpleType base = new SimpleType(stringClass);
        SimpleType simpleType = new SimpleType(base);
        assertEquals("SimpleType for String from TypeBase", simpleType.getTypeDescription().getRawClass(), stringClass);
    }

    /**
     * Test constructUnsafe method
     */
    @Test
    public void testSimpleTypeConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertEquals("SimpleType for String constructed unsafely", simpleType.getTypeDescription().getRawClass(), stringClass);
    }

    /**
     * Test construct deprecated method
     */
    @Test(expected = DeprecationException.class)
    public void testSimpleTypeConstructDeprecated() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
    }

    /**
     * Test narrow method
     */
    @Test
    public void testSimpleTypeNarrow() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType stringBuilderSimpleType = new SimpleType(stringBuilderClass);
        SimpleType narrowedType = stringSimpleType.narrow(stringBuilderClass);
        assertNull("Narrowed type should be null for different class", narrowedType);

        SimpleType subclassSimpleType = new SimpleType(stringClass);
        SimpleType narrowedSubclassType = subclassSimpleType.narrow(stringClass);
        assertEquals("Narrowed type for same class should be the same instance", narrowedSubclassType, subclassSimpleType);
    }

    /**
     * Test withContentType method
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithContentType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        simpleType.withContentType(JavaType.construct(Integer.class));
    }

    /**
     * Test withTypeHandler method
     */
    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        Object handler = new Object() {};
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType withHandlerType = simpleType.withTypeHandler(handler);
        assertEquals("SimpleType with TypeHandler", withHandlerType.getTypeDescription().getRawClass(), stringClass);
        assertEquals("TypeHandler", withHandlerType.getTypeHandler(), handler);
    }

    /**
     * Test withContentTypeHandler method
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        simpleType.withContentTypeHandler(new Object() {});
    }

    /**
     * Test withValueHandler method
     */
    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        Object handler = new Object() {};
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType withHandlerType = simpleType.withValueHandler(handler);
        assertEquals("SimpleType with ValueHandler", withHandlerType.getTypeDescription().getRawClass(), stringClass);
        assertEquals("ValueHandler", withHandlerType.getValueHandler(), handler);
    }

    /**
     * Test withStaticTyping method
     */
    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue("Static typing is true", staticType.isStatic());
    }

    /**
     * Test refine method
     */
    @Test
    public void testSimpleTypeRefine() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType refinedType = simpleType.refine(stringBuilderClass, null, null, null);
        assertNull("Refined type should be null for different class", refinedType);
    }

    /**
     * Test buildCanonicalName method
     */
    @Test
    public void testSimpleTypeBuildCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("Canonical name for String", "[" + stringClass.getName() + "]", canonicalName);

        Class<List<String>> listStringClass = List.class.getTypeParameters()[0];
        TypeBindings bindings = new TypeBindings();
        bindings.bind(0, JavaType.construct(String.class));
        SimpleType listSimpleType = new SimpleType(listStringClass, bindings, null, null);
        String listCanonicalName = listSimpleType.buildCanonicalName();
        assertEquals("Canonical name for List<String>", "[java.util.List, [Ljava.lang.String;]", listCanonicalName);
    }

    /**
     * Test isContainerType method
     */
    @Test
    public void testSimpleTypeIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse("SimpleType for String is not a container type", simpleType.isContainerType());
    }

    /**
     * Test getErasedSignature method
     */
    @Test
    public void testSimpleTypeGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder erasedSignature = new StringBuilder();
        simpleType.getErasedSignature(erasedSignature);
        assertEquals("Erased signature for String", stringClass.getName(), erasedSignature.toString());
    }

    /**
     * Test getGenericSignature method
     */
    @Test
    public void testSimpleTypeGetGenericSignature() {
        Class<List<String>> listStringClass = List.class.getTypeParameters()[0];
        TypeBindings bindings = new TypeBindings();
        bindings.bind(0, JavaType.construct(String.class));
        SimpleType listSimpleType = new SimpleType(listStringClass, bindings, null, null);
        StringBuilder genericSignature = new StringBuilder();
        listSimpleType.getGenericSignature(genericSignature);
        assertEquals("Generic signature for List<String>", "[Ljava.lang.String;]", genericSignature.toString());
    }
}