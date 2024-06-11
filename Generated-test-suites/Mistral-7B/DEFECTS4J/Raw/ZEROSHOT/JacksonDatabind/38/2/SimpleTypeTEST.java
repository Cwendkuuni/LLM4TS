package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType simpleTypeConstructUnsafe = SimpleType.constructUnsafe(stringClass);
        SimpleType simpleTypeConstruct = SimpleType.construct(stringClass);

        assertEquals(stringClass, simpleType.getRawClass());
        assertEquals(simpleType, simpleTypeConstructUnsafe);
        assertNotEquals(simpleType, simpleTypeConstruct);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType copiedSimpleType = new SimpleType(simpleType);

        assertEquals(simpleType, copiedSimpleType);
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType staticSimpleType = simpleType.withStaticTyping();

        assertFalse(simpleType._asStatic);
        assertTrue(staticSimpleType._asStatic);
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object valueHandler = new Object();
        SimpleType valueHandlerSimpleType = simpleType.withValueHandler(valueHandler);

        assertNotEquals(simpleType, valueHandlerSimpleType);
        assertEquals(valueHandler, valueHandlerSimpleType._valueHandler);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object typeHandler = new Object();
        SimpleType typeHandlerSimpleType = simpleType.withTypeHandler(typeHandler);

        assertNotEquals(simpleType, typeHandlerSimpleType);
        assertEquals(typeHandler, typeHandlerSimpleType._typeHandler);
    }

    @Test
    public void testSimpleTypeWithSuperTypes() {
        Class<String> stringClass = String.class;
        Class<?> listClass = List.class;
        JavaType superClass = new SimpleType(listClass);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), superClass, superInterfaces);

        assertNotNull(simpleType.getSuperClass());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertEquals(0, simpleType.getSuperInterfaces().length);
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        TypeBindings bindings = new TypeBindings();
        bindings.put(Integer.TYPE.getTypeName(), new SimpleType(Integer.class));
        SimpleType boundSimpleType = simpleType.withBindings(bindings);

        assertNotEquals(simpleType, boundSimpleType);
        assertEquals(bindings, boundSimpleType._bindings);
    }

    @Test
    public void testSimpleTypeIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeToString() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        String toString = simpleType.toString();
        String expected = "[simple type, class " + stringClass.getName() + "]";

        assertEquals(expected, toString);
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringClass);

        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testSimpleTypeNotEqualsDifferentClasses() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(intClass);

        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testSimpleTypeNotEqualsDifferentBindings() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        TypeBindings bindings1 = new TypeBindings();
        bindings1.put(Integer.TYPE.getTypeName(), new SimpleType(Integer.class));
        SimpleType simpleType2 = new SimpleType(stringClass);
        TypeBindings bindings2 = new TypeBindings();
        bindings2.put(Character.TYPE.getTypeName(), new SimpleType(Character.class));

        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testSimpleTypeCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        String canonicalName = simpleType.buildCanonicalName();
        String expected = "[Ljava.lang.String;";

        assertEquals(expected, canonicalName);
    }

    @Test
    public void testSimpleTypeGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        StringBuilder erasedSignature = new StringBuilder();
        simpleType.getErasedSignature(erasedSignature);
        String expected = "Ljava/lang/String;";

        assertEquals(expected, erasedSignature.toString());
    }

    @Test
    public void testSimpleTypeGetGenericSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        StringBuilder genericSignature = new StringBuilder();
        simpleType.getGenericSignature(genericSignature);
        String expected = "<T:Ljava/lang/String;>";

        assertEquals(expected, genericSignature.toString());
    }
}