package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.getValueType();
        assertEquals(String.class, javaType.getRawClass());
        assertEquals(SimpleType.class, javaType.getClass());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalSimpleType = new SimpleType(stringClass);
        SimpleType copySimpleType = new SimpleType(originalSimpleType);
        JavaType originalJavaType = originalSimpleType.getValueType();
        JavaType copiedJavaType = copySimpleType.getValueType();
        assertEquals(originalJavaType, copiedJavaType);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType = new SimpleType(integerClass);
        Object intHandler = new Object() {};
        SimpleType withTypeHandler = simpleType.withTypeHandler(intHandler);
        JavaType javaType = withTypeHandler.getValueType();
        assertEquals(Integer.class, javaType.getRawClass());
        assertEquals(SimpleType.class, javaType.getClass());
        assertEquals(intHandler, withTypeHandler._typeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithWrongType() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType wrongType = new SimpleType(Integer.class).getValueType();
        simpleType.withContentType(wrongType);
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object stringHandler = new Object() {};
        SimpleType withContentTypeHandler = simpleType.withContentTypeHandler(stringHandler);
        JavaType javaType = withContentTypeHandler.getValueType();
        assertEquals(String.class, javaType.getRawClass());
        assertEquals(SimpleType.class, javaType.getClass());
        assertEquals(stringHandler, withContentTypeHandler._contentTypeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithWrongContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object intHandler = new Object() {};
        simpleType.withContentValueHandler(intHandler);
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertEquals(simpleType._asStatic, !withStaticTyping._asStatic);
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = new Class<Integer>(){}.getClass();
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType refinedType = simpleType.refine(javaType.getType(), bindings, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testSimpleTypeBuildCanonicalName() {
        Class<List<String>> listOfStringsClass = List.class.getTypeParameters()[0].getRawType();
        SimpleType simpleType = new SimpleType(listOfStringsClass);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("[Ljava.lang.String;", canonicalName);
    }

    @Test
    public void testSimpleTypeIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeToString() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String toString = simpleType.toString();
        StringBuilder expectedStringBuilder = new StringBuilder("[simple type, class ");
        expectedStringBuilder.append(stringClass.getName()).append(']');
        assertEquals(expectedStringBuilder.toString(), toString);
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringClass);
        assertTrue(simpleType1.equals(simpleType2));
        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testSimpleTypeGetErasedSignature() {
        Class<List<String>> listOfStringsClass = List.class.getTypeParameters()[0].getRawType();
        SimpleType simpleType = new SimpleType(listOfStringsClass);
        StringBuilder expectedStringBuilder = new StringBuilder("[L");
        expectedStringBuilder.append(String.class.getName()).append(';');
        StringBuilder actualStringBuilder = new StringBuilder();
        simpleType.getErasedSignature(actualStringBuilder);
        assertEquals(expectedStringBuilder.toString(), actualStringBuilder.toString());
    }

    @Test
    public void testSimpleTypeGetGenericSignature() {
        Class<List<List<String>>> listOfListOfStringsClass = List.class.getTypeParameters()[0].getGenericComponentType();
        SimpleType simpleType = new SimpleType(listOfListOfStringsClass);
        StringBuilder expectedStringBuilder = new StringBuilder("[");
        expectedStringBuilder.append(List.class.getName()).append('<');
        expectedStringBuilder.append(String.class.getName()).append('>');
        expectedStringBuilder.append("]");
        StringBuilder actualStringBuilder = new StringBuilder();
        simpleType.getGenericSignature(actualStringBuilder);
        assertEquals(expectedStringBuilder.toString(), actualStringBuilder.toString());
    }
}