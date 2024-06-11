package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testCreate() {
        Class<List<String>> listOfStringType = List.class.getTypeParameters()[0].getRawType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listOfStringType);

        TypeBindings bindings = TypeBindings.create(listOfStringType, javaType);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));

        JavaType javaType2 = TypeFactory.defaultInstance().constructType(String.class);
        bindings = TypeBindings.create(listOfStringType, javaType2);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(JavaType.getTypeInstance(String.class), bindings.getBoundType(0));

        TypeVariable<String> typeVariable = (TypeVariable<String>) listOfStringType.getTypeParameters()[0];
        bindings = TypeBindings.create(listOfStringType, typeVariable.getGenericDeclaration());
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(typeVariable, bindings.findBoundType("T"));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<List<String>> listOfStringType = List.class.getTypeParameters()[0].getRawType();

        TypeBindings bindings = TypeBindings.create(listOfStringType, null);
        assertEquals(1, bindings.size());
        assertNull(bindings.findBoundType("T"));
        assertTrue(bindings.hasUnbound("T"));

        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("T");
        assertEquals(1, bindingsWithUnboundVariable.size());
        assertEquals("T", bindingsWithUnboundVariable.getBoundName(0));
        assertEquals(null, bindingsWithUnboundVariable.getBoundType(0));
        assertTrue(bindingsWithUnboundVariable.hasUnbound("T"));
        assertFalse(bindingsWithUnboundVariable.hasUnbound("U"));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());

        Class<List<String>> listOfStringType = List.class.getTypeParameters()[0].getRawType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listOfStringType);
        TypeBindings bindingsWithType = TypeBindings.create(listOfStringType, javaType);
        assertFalse(bindingsWithType.isEmpty());
    }

    @Test
    public void testFindBoundType() {
        Class<List<String>> listOfStringType = List.class.getTypeParameters()[0].getRawType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listOfStringType);

        TypeBindings bindings = TypeBindings.create(listOfStringType, javaType);
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
        assertEquals(javaType, bindings.findBoundType("T"));
        assertNull(bindings.findBoundType("U"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidNumberOfTypeArguments() {
        Class<List<String, Integer>> listOfStringIntegerType = List.class.getTypeParameters()[0].getRawType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listOfStringIntegerType);

        TypeBindings bindings = TypeBindings.create(List.class, javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidTypeArguments() {
        Class<List<String, Integer>> listOfStringIntegerType = List.class.getTypeParameters()[0].getRawType();
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);

        TypeBindings bindings = TypeBindings.create(listOfStringIntegerType, javaType1, javaType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyTypeArgumentsList() {
        Class<List<String>> listOfStringType = List.class.getTypeParameters()[0].getRawType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listOfStringType);

        TypeBindings bindings = TypeBindings.create(listOfStringType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyTypeVariablesArray() {
        Class<List<String>> listOfStringType = List.class.getTypeParameters()[0].getRawType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listOfStringType);

        TypeBindings bindings = TypeBindings.create(listOfStringType, null, new String[0]);
    }
}