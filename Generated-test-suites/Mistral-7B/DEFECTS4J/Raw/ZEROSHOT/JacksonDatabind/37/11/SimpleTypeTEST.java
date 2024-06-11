package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType constructedType = simpleType.construct();
        assertEquals(simpleType, constructedType);
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<?> stringClass = String.class;
        Class<?> stringBuilderClass = StringBuilder.class;
        SimpleType simpleTypeString = new SimpleType(stringClass);
        SimpleType simpleTypeStringBuilder = new SimpleType(stringBuilderClass);

        JavaType narrowedType = simpleTypeString.narrow(stringBuilderClass);
        assertEquals(simpleTypeStringBuilder, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<?> intClass = int[].class;
        new SimpleType(intClass);
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<?> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringClass);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType1, simpleType3);
    }

    @Test
    public void testSimpleTypeToString() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals("[simple type, class [" + stringClass.getName() + "]]", simpleType.toString());
    }

    @Test
    public void testSimpleTypeGetCanonicalName() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(stringClass.getName(), simpleType.buildCanonicalName());
    }

    @Test
    public void testSimpleTypeGetErasedSignature() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder expectedSignature = new StringBuilder("[Ljava/lang/String;");
        assertEquals(expectedSignature.toString(), simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testSimpleTypeGetGenericSignature() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder expectedSignature = new StringBuilder("[Ljava/lang/String;");
        assertEquals(expectedSignature.toString(), simpleType.getGenericSignature(new StringBuilder()).toString());

        Class<?> listStringClass = List.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType listStringType = typeFactory.constructCollectionType(List.class, String.class);
        SimpleType listStringSimpleType = (SimpleType) listStringType;
        StringBuilder actualSignature = new StringBuilder();
        listStringSimpleType.getGenericSignature(actualSignature);
        StringBuilder expectedListSignature = new StringBuilder("[Ljava/util/List;Ljava/lang/Class;[Ljava/lang/Class;]");
        assertEquals(expectedListSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object handler = new Object() {};

        JavaType typeWithHandler = simpleType.withTypeHandler(handler);
        JavaType expectedTypeWithHandler = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, handler, false);
        assertEquals(expectedTypeWithHandler, typeWithHandler);
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object handler = new Object() {};

        JavaType typeWithContentTypeHandler = simpleType.withContentTypeHandler(handler);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> simpleType.withContentTypeHandler(handler));
        assertEquals("Simple types have no content types; can not call withContenTypeHandler()", exception.getMessage());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object handler = new Object() {};

        JavaType typeWithValueHandler = simpleType.withValueHandler(handler);
        JavaType expectedTypeWithValueHandler = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, handler, null, false);
        assertEquals(expectedTypeWithValueHandler, typeWithValueHandler);
    }

    @Test
    public void testSimpleTypeWithContentValueHandler() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object handler = new Object() {};

        JavaType typeWithContentValueHandler = simpleType.withContentValueHandler(handler);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> simpleType.withContentValueHandler(handler));
        assertEquals("Simple types have no content types; can not call withContenValueHandler()", exception.getMessage());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType staticType = simpleType.withStaticTyping();
        assertEquals(simpleType, staticType);
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Class<?> stringBuilderClass = StringBuilder.class;
        JavaType refinedType = simpleType.refine(stringBuilderClass, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
}