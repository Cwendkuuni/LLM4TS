package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeVariableStash;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeUtils;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertFalse(bindings.hasUnbound("T"));
        assertNull(bindings.findBoundType("T"));
    }

    @Test
    public void testCreate() {
        Class<List<Integer>> listIntClass = new ArrayList<Integer>().getClass();

        TypeBindings bindings = TypeBindings.create(listIntClass, null);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0).getRawType());
        assertFalse(bindings.hasUnbound("T"));

        bindings = TypeBindings.create(listIntClass, Collections.singletonList(JavaType.constructParametricType(List.class, Integer.TYPE)));
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0).getRawType());
        assertFalse(bindings.hasUnbound("T"));
    }

    @Test
    public void testCreateWithUnboundVariables() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();

        TypeBindings bindings = TypeBindings.create(listStringClass, null);
        assertEquals(1, bindings.size());
        assertFalse(bindings.hasUnbound("T"));

        bindings = TypeBindings.create(listStringClass, Collections.singletonList(TypeUtils.unboundMarkerTypeVariable()));
        assertEquals(1, bindings.size());
        assertTrue(bindings.hasUnbound("T"));
        assertNull(bindings.findBoundType("T"));

        String[] unboundVariables = bindings.toArray(new String[0]);
        assertEquals(1, unboundVariables.length);
        assertEquals("T", unboundVariables[0]);

        bindings = bindings.withUnboundVariable("U");
        assertEquals(2, bindings.size());
        assertTrue(bindings.hasUnbound("T"));
        assertTrue(bindings.hasUnbound("U"));
        assertNull(bindings.findBoundType("T"));
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testCreateIfNeeded() {
        Class<List<Integer>> listIntClass = new ArrayList<Integer>().getClass();
        JavaType javaType = JavaType.constructParametricType(List.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.createIfNeeded(listIntClass, javaType);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0).getRawType());
        assertFalse(bindings.hasUnbound("T"));

        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        bindings = TypeBindings.createIfNeeded(listIntClass, null);
        assertEquals(emptyBindings, bindings);
    }

    @Test
    public void testFindBoundType() {
        Class<List<Integer>> listIntClass = new ArrayList<Integer>().getClass();
        JavaType javaType = JavaType.constructParametricType(List.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.create(listIntClass, javaType);
        assertEquals(Integer.TYPE, bindings.findBoundType("T").getRawType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidNumberOfTypeArguments() {
        Class<List<Integer>> listIntClass = new ArrayList<Integer>().getClass();

        // One type argument, but class expects two
        TypeBindings bindings = TypeBindings.create(listIntClass, JavaType.constructParametricType(List.class, Integer.TYPE));
        assertFalse(bindings.hasUnbound("U"));

        // Two type arguments, but class expects one
        TypeBindings bindings2 = TypeBindings.create(listIntClass, JavaType.constructParametricType(List.class, Integer.TYPE, String.class));
        assertEquals(2, bindings2.size());
        assertTrue(bindings2.hasUnbound("U"));
        assertNull(bindings2.findBoundType("T"));
        assertEquals(String.class, bindings2.getBoundType(1).getRawType());
    }

    @Test
    public void testToString() {
        Class<List<Integer>> listIntClass = new ArrayList<Integer>().getClass();
        JavaType javaType = JavaType.constructParametricType(List.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.create(listIntClass, javaType);
        String representation = bindings.toString();
        assertTrue(representation.startsWith("<"));
        assertTrue(representation.endsWith(">"));
        assertTrue(representation.contains("Integer"));
    }

    @Test
    public void testHashCodeAndEquals() {
        Class<List<Integer>> listIntClass = new ArrayList<Integer>().getClass();
        JavaType javaType = JavaType.constructParametricType(List.class, Integer.TYPE);

        TypeBindings bindings1 = TypeBindings.create(listIntClass, javaType);
        TypeBindings bindings2 = TypeBindings.create(listIntClass, javaType);

        assertEquals(bindings1, bindings2);
        assertEquals(bindings1.hashCode(), bindings2.hashCode());

        JavaType javaType2 = JavaType.constructParametricType(List.class, String.class);
        TypeBindings bindings3 = TypeBindings.create(listIntClass, javaType2);

        assertFalse(bindings1.equals(bindings3));
        assertFalse(bindings1.hashCode() == bindings3.hashCode());
    }

    @Test
    public void testTypeParameterArray() {
        Class<List<List<String>>> listOfListStringClass = new ArrayList<List<String>>().getClass();
        JavaType javaType = JavaType.constructParametricType(List.class, List.class, String.class);

        TypeBindings bindings = TypeBindings.create(listOfListStringClass, javaType);

        JavaType[] types = bindings.typeParameterArray();
        assertEquals(3, types.length);
        assertEquals(List.class, types[0].getRawType());
        assertEquals(List.class, types[1].getRawType());
        assertEquals(String.class, types[2].getRawType());
    }

    @Test
    public void testHasUnbound() {
        Class<List<List<String>>> listOfListStringClass = new ArrayList<List<String>>().getClass();
        JavaType javaType = JavaType.constructParametricType(List.class, List.class, String.class);

        TypeBindings bindings = TypeBindings.create(listOfListStringClass, javaType);

        Set<String> unboundVariables = new HashSet<>();
        unboundVariables.add("A");
        unboundVariables.add("B");
        unboundVariables.add("C");

        for (String variableName : bindings.getBoundNames()) {
            assertFalse(unboundVariables.contains(variableName));
        }

        for (String variableName : unboundVariables) {
            assertTrue(bindings.hasUnbound(variableName));
        }
    }

    @Test
    public void testGetBoundNames() {
        Class<List<List<String>>> listOfListStringClass = new ArrayList<List<String>>().getClass();
        JavaType javaType = JavaType.constructParametricType(List.class, List.class, String.class);

        TypeBindings bindings = TypeBindings.create(listOfListStringClass, javaType);

        String[] boundNames = bindings.getBoundNames();
        assertEquals(3, boundNames.length);
        assertEquals("java.util.List", boundNames[0]);
        assertEquals("java.util.List", boundNames[1]);
        assertEquals("java.lang.String", boundNames[2]);
    }

    @Test
    public void testGetBoundType() {
        Class<List<List<String>>> listOfListStringClass = new ArrayList<List<String>>().getClass();
        JavaType javaType = JavaType.constructParametricType(List.class, List.class, String.class);

        TypeBindings bindings = TypeBindings.create(listOfListStringClass, javaType);

        TypeVariable<?>[] typeParameters = listOfListStringClass.getTypeParameters();
        TypeVariable<?> typeVariableA = typeParameters[0];
        TypeVariable<?> typeVariableB = typeParameters[1];
        TypeVariable<?> typeVariableC = typeParameters[2];

        assertEquals(typeVariableA, bindings.getBoundType(0).getType());
        assertEquals(typeVariableB, bindings.getBoundType(1).getType());
        assertEquals(typeVariableC, bindings.getBoundType(2).getType());
    }

    @Test
    public void testGetTypeParameters() {
        Class<List<List<String>>> listOfListStringClass = new ArrayList<List<String>>().getClass();
        JavaType javaType = JavaType.constructParametricType(List.class, List.class, String.class);

        TypeBindings bindings = TypeBindings.create(listOfListStringClass, javaType);

        List<JavaType> typeParameters = bindings.getTypeParameters();
        List<JavaType> expectedTypeParameters = Arrays.asList(JavaType.constructParametricType(List.class, Object.class),
                                                              JavaType.constructParametricType(List.class, Object.class),
                                                              JavaType.constructParametricType(String.class));

        assertEquals(expectedTypeParameters, typeParameters);
    }
}