package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeCreation() {
        // Test default constructor
        SimpleType simpleType1 = new SimpleType(String.class);
        assertNotNull(simpleType1);

        // Test constructor with bindings, superClass, and superInterfaces
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType[] superInterfaces = new JavaType[] { TypeFactory.defaultInstance().constructType(Comparable.class) };
        SimpleType simpleType2 = new SimpleType(String.class, bindings, superClass, superInterfaces);
        assertNotNull(simpleType2);

        // Test copy constructor
        SimpleType simpleType3 = new SimpleType(simpleType1);
        assertNotNull(simpleType3);
        assertEquals(simpleType1, simpleType3);

        // Test pass-through constructor
        SimpleType simpleType4 = new SimpleType(simpleType2);
        assertNotNull(simpleType4);
        assertEquals(simpleType2, simpleType4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgExceptionWhenCreatingSimpleTypeForArray() {
        new SimpleType(int[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgExceptionWhenCreatingSimpleTypeForCollection() {
        new SimpleType(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgExceptionWhenCreatingSimpleTypeForMap() {
        new SimpleType(HashMap.class);
    }

    @Test
    public void testNarrowMethod() {
        // Test narrowing to the same class
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        JavaType narrowedType = simpleType1._narrow(String.class);
        assertEquals(simpleType1, narrowedType);
        assertEquals(simpleType2, narrowedType);

        // Test narrowing to a subclass
        Class<Number> numberClass = Number.class;
        SimpleType simpleTypeNumber = new SimpleType(numberClass);
        JavaType narrowedTypeNumber = simpleType1._narrow(numberClass);
        assertNotNull(narrowedTypeNumber);
        assertTrue(numberClass.isAssignableFrom(narrowedTypeNumber._class));

        // Test narrowing to a superclass
        JavaType superclass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType narrowedTypeSuperclass = simpleTypeNumber._narrow(superclass);
        assertNull(narrowedTypeSuperclass);

        // Test narrowing to an unrelated class
        Class<Boolean> booleanClass = Boolean.class;
        JavaType narrowedTypeUnrelated = simpleTypeNumber._narrow(booleanClass);
        assertNull(narrowedTypeUnrelated);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(null);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertNull(simpleTypeWithHandler.getTypeHandler());

        Object typeHandler = new Object();
        SimpleType simpleTypeWithTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertNotSame(simpleType, simpleTypeWithTypeHandler);
        assertEquals(typeHandler, simpleTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithContentTypeHandler = simpleType.withContentTypeHandler(null);
        assertNotSame(simpleType, simpleTypeWithContentTypeHandler);
        assertNull(simpleTypeWithContentTypeHandler.getContentTypeHandler());

        Object contentTypeHandler = new Object();
        SimpleType simpleTypeWithContentTypeHandler2 = simpleType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(simpleType, simpleTypeWithContentTypeHandler2);
        assertEquals(contentTypeHandler, simpleTypeWithContentTypeHandler2.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithHandler = simpleType.withValueHandler(null);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertNull(simpleTypeWithHandler.getValueHandler());

        Object valueHandler = new Object();
        SimpleType simpleTypeWithValueHandler = simpleType.withValueHandler(valueHandler);
        assertNotSame(simpleType, simpleTypeWithValueHandler);
        assertEquals(valueHandler, simpleTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithStaticTyping = simpleType.withStaticTyping();
        assertNotSame(simpleType, simpleTypeWithStaticTyping);
        assertTrue(simpleTypeWithStaticTyping.isStatic());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testEqualsMethod() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);
        SimpleType simpleType4 = new SimpleType(String.class);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertTrue(simpleType1.equals(simpleType4));
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(""));
        assertFalse(simpleType1.equals(Integer.TYPE));
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("class java.lang.String", canonicalName);

        TypeBindings bindings = TypeBindings.builder().bindValue("id", TypeFactory.defaultInstance().constructType(Integer.class)).build();
        SimpleType simpleTypeWithBindings = new SimpleType(String.class, bindings);
        String canonicalNameWithBindings = simpleTypeWithBindings.buildCanonicalName();
        String expectedCanonicalNameWithBindings = "[simple type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expectedCanonicalNameWithBindings, canonicalNameWithBindings);
    }

    @Test
    public void testIsContainerTypeMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());

        SimpleType arrayType = new SimpleType(int[].class);
        assertTrue(arrayType.isContainerType());

        SimpleType listType = new SimpleType(ArrayList.class);
        assertTrue(listType.isContainerType());

        SimpleType mapType = new SimpleType(HashMap.class);
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expectedSignature = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actualSignature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expectedSignature, actualSignature);
    }

    @Test
    public void testGetGenericSignatureMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expectedSignature = new StringBuilder("Ljava/lang/String;");
        StringBuilder actualSignature = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expectedSignature, actualSignature);

        TypeBindings bindings = TypeBindings.builder().bindValue("id", TypeFactory.defaultInstance().constructType(Integer.class)).build();
        SimpleType simpleTypeWithBindings = new SimpleType(String.class, bindings);
        StringBuilder expectedSignatureWithBindings = new StringBuilder("[Ljava/lang/String<Ljava/lang/Integer;>;]");
        StringBuilder actualSignatureWithBindings = simpleTypeWithBindings.getGenericSignature(new StringBuilder());
        assertEquals(expectedSignatureWithBindings, actualSignatureWithBindings);
    }
}