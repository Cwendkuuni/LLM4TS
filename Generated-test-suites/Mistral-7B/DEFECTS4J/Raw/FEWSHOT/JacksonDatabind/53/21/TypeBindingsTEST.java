package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    private TypeBindings bindings;

    @Before
    public void setUp() {
        bindings = TypeBindings.create(List.class, JavaType.constructParametricType(ArrayList.class, Integer.class));
    }

    @Test
    public void testCreate() {
        assertEquals(2, bindings.size());
        assertArrayEquals(new String[] { "E", "K" }, bindings.getNames());
        assertArrayEquals(new JavaType[] { JavaType.constructParametricType(ArrayList.class, Integer.class),
                JavaType.constructParametricType(Collection.class, Object.class) }, bindings.getTypeParameters().toArray());
    }

    @Test
    public void testCreateEmpty() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals(0, empty.size());
        assertNull(empty.getNames());
        assertArrayEquals(new JavaType[0], empty.getTypeParameters().toArray());
    }

    @Test
    public void testFindBoundType() {
        JavaType intType = JavaType.constructRawType(Integer.class);
        JavaType stringType = JavaType.constructRawType(String.class);
        JavaType listType = TypeBindings.create(List.class, intType);
        JavaType mapType = TypeBindings.create(Map.class, listType, stringType);

        assertEquals(intType, mapType.findBoundType("E"));
        assertEquals(listType, mapType.findBoundType("K"));
        assertEquals(stringType, mapType.findBoundType("V"));
        assertNull(mapType.findBoundType("nonexistent"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidNumberOfTypeParameters() {
        Class<?> erasedType = Map.class;
        JavaType[] types = new JavaType[3];
        TypeBindings.create(erasedType, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidNumberOfTypeParameterNames() {
        Class<?> erasedType = Map.class;
        String[] names = new String[3];
        TypeBindings.create(erasedType, names, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMismatchingNumberOfTypeParameterNamesAndTypes() {
        Class<?> erasedType = Map.class;
        String[] names = new String[2];
        names[0] = "E";
        names[1] = "K";
        JavaType[] types = new JavaType[3];
        types[0] = JavaType.constructParametricType(ArrayList.class, Integer.class);
        types[1] = JavaType.constructParametricType(List.class, Object.class);
        TypeBindings.create(erasedType, names, types);
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType[] types = new JavaType[1];
        types[0] = JavaType.constructRawType(String.class);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("T");
        JavaType unboundType = bindingsWithUnboundVariable.findBoundType("T");
        assertNull(unboundType);

        String[] names = bindingsWithUnboundVariable.getNames();
        assertEquals(3, names.length);
        assertSame(bindings.getNames()[0], names[0]);
        assertSame(bindings.getNames()[1], names[1]);
        assertEquals("T", names[2]);

        JavaType[] newTypes = bindingsWithUnboundVariable.getTypeParameters().toArray(JavaType[].class);
        assertArrayEquals(newTypes, bindings.getTypeParameters().toArray());
    }

}