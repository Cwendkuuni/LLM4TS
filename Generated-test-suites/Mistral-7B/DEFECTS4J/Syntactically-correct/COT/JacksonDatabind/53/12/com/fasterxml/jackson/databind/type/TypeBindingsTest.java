package com.example.test; // replace with your test package name

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreate() {
        Class<List<String>> stringListClass = List.class;
        TypeVariable<String> stringTypeVariable = stringListClass.getTypeParameters()[0];
        JavaType stringType = JavaType.constructTypeOrNull(String.class);
        TypeBindings bindings = TypeBindings.create(stringListClass, Collections.singletonList(stringType));
        assertFalse(bindings.isEmpty());
        assertEquals(stringTypeVariable.getName(), bindings.getBoundName(0));
        assertEquals(stringType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeeded() {
        Class<List<String>> stringListClass = List.class;
        JavaType stringType = JavaType.constructTypeOrNull(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(stringListClass, stringType);
        assertFalse(bindings.isEmpty());
        assertEquals(stringTypeVariable.getName(), bindings.getBoundName(0));
        assertEquals(stringType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<LinkedHashMap<String, String>> mapClass = LinkedHashMap.class;
        JavaType stringType = JavaType.constructTypeOrNull(String.class);
        TypeBindings bindings = TypeBindings.create(mapClass, stringType);
        assertFalse(bindings.isEmpty());
        assertEquals(mapClass.getTypeParameters()[0].getName(), bindings.getBoundName(0));
        assertEquals(stringType, bindings.getBoundType(0));

        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("key");
        assertTrue(bindingsWithUnboundVariable.hasUnbound("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfArguments() {
        Class<List<String>> stringListClass = List.class;
        JavaType stringType = JavaType.constructTypeOrNull(String.class);
        TypeBindings.create(stringListClass, stringType, stringType); // should have only 2 arguments
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedArguments() {
        Class<ArrayList<List<String>>> nestedListClass = ArrayList.class;
        TypeVariable<List<String>> listTypeVariable = nestedListClass.getTypeParameters()[0];
        JavaType listType = JavaType.constructTypeOrNull(List.class, JavaType.constructTypeOrNull(String.class));
        JavaType stringType = JavaType.constructTypeOrNull(String.class);
        TypeBindings.create(nestedListClass, stringType, listType); // should have List<String> as first argument
    }

    @Test
    public void testFindBoundType() {
        Class<HashMap<String, ArrayList<String>>> mapClass = HashMap.class;
        JavaType stringType = JavaType.constructTypeOrNull(String.class);
        JavaType arrayListType = JavaType.constructTypeOrNull(ArrayList.class, JavaType.constructTypeOrNull(String.class));
        TypeBindings bindings = TypeBindings.create(mapClass, stringType, arrayListType);
        assertEquals(stringType, bindings.findBoundType("key"));
        assertEquals(arrayListType, bindings.findBoundType("value"));
    }

    @Test
    public void testGetTypeParameters() {
        Class<HashMap<String, ArrayList<String>>> mapClass = HashMap.class;
        JavaType stringType = JavaType.constructTypeOrNull(String.class);
        JavaType arrayListType = JavaType.constructTypeOrNull(ArrayList.class, JavaType.constructTypeOrNull(String.class));
        TypeBindings bindings = TypeBindings.create(mapClass, stringType, arrayListType);
        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertEquals(2, typeParameters.size());
        assertEquals(stringType, typeParameters.get(0));
        assertEquals(arrayListType, typeParameters.get(1));
    }

    @Test
    public void testToString() {
        Class<HashMap<String, ArrayList<String>>> mapClass = HashMap.class;
        JavaType stringType = JavaType.constructTypeOrNull(String.class);
        JavaType arrayListType = JavaType.constructTypeOrNull(ArrayList.class, JavaType.constructTypeOrNull(String.class));
        TypeBindings bindings = TypeBindings.create(mapClass, stringType, arrayListType);
        String toString = bindings.toString();
        assertTrue(toString.startsWith("<"));
        assertTrue(toString.endsWith(">"));
        assertTrue(toString.contains(","));
        assertTrue(toString.contains(stringType.getRawClass().getSimpleName()));
        assertTrue(toString.contains(arrayListType.getRawClass().getSimpleName()));
    }

    
}