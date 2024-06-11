package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType("T"));
        assertTrue(empty.isEmpty());
        assertTrue(empty.equals(TypeBindings.emptyBindings()));
    }

    @Test
    public void testCreateWithOneTypeArg() {
        Class<List<Integer>> listIntType = new ArrayList<>() {}.getClass();
        TypeBindings bindings = TypeBindings.create(listIntType, null);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertTrue(bindings.hasUnbound("T"));
        assertNotNull(bindings.findBoundType("T"));
        assertEquals(Integer.TYPE.getType(), bindings.findBoundType("T").getRawType());
        assertTrue(bindings.isEmpty());
        assertTrue(bindings.equals(TypeBindings.createIfNeeded(listIntType, null)));

        TypeBindings bindings2 = TypeBindings.createIfNeeded(listIntType, JavaType.constructParametricType(List.class, Integer.TYPE));
        assertEquals(1, bindings2.size());
        assertEquals("T", bindings2.getBoundName(0));
        assertTrue(bindings2.hasUnbound("T"));
        assertNotNull(bindings2.findBoundType("T"));
        assertEquals(Integer.TYPE.getType(), bindings2.findBoundType("T").getRawType());
        assertFalse(bindings2.isEmpty());
        assertNotEquals(bindings, bindings2);
    }

    @Test
    public void testCreateWithTwoTypeArgs() {
        Class<Map<Integer, String>> mapIntStringType = new HashMap<>() {}.getClass();
        TypeBindings bindings = TypeBindings.create(mapIntStringType, null);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertTrue(bindings.hasUnbound("K"));
        assertTrue(bindings.hasUnbound("V"));
        assertNull(bindings.findBoundType("K"));
        assertNull(bindings.findBoundType("V"));
        assertTrue(bindings.isEmpty());
        assertTrue(bindings.equals(TypeBindings.createIfNeeded(mapIntStringType, null)));

        TypeBindings bindings2 = TypeBindings.createIfNeeded(mapIntStringType, JavaType.constructParametricType(Map.class, JavaType.of(Integer.TYPE), JavaType.of(String.class)));
        assertEquals(2, bindings2.size());
        assertEquals("K", bindings2.getBoundName(0));
        assertEquals("V", bindings2.getBoundName(1));
        assertTrue(bindings2.hasUnbound("K"));
        assertTrue(bindings2.hasUnbound("V"));
        assertNotNull(bindings2.findBoundType("K"));
        assertNotNull(bindings2.findBoundType("V"));
        assertFalse(bindings2.isEmpty());
        assertNotEquals(bindings, bindings2);
    }

    @Test
    public void testCreateWithResolvedTypes() {
        Class<Map<Integer, List<String>>> mapIntListStringType = new HashMap<>() {}.getClass();
        JavaType keyType = JavaType.constructParametricType(Map.class, JavaType.of(Integer.TYPE), JavaType.constructParametricType(List.class, String.class));
        TypeBindings bindings = TypeBindings.create(mapIntListStringType, keyType);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertTrue(bindings.hasUnbound("K"));
        assertTrue(bindings.hasUnbound("V"));
        assertNotNull(bindings.findBoundType("K"));
        assertNotNull(bindings.findBoundType("V"));
        assertFalse(bindings.isEmpty());
        assertTrue(bindings.equals(TypeBindings.createIfNeeded(mapIntListStringType, keyType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMismatchingCounts() {
        Class<List<Integer>> listIntType = new ArrayList<>() {}.getClass();
        TypeBindings.create(listIntType, new JavaType[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMismatchingCounts2() {
        Class<List<Integer>> listIntType = new ArrayList<>() {}.getClass();
        TypeBindings.create(listIntType, new String[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMismatchingCounts3() {
        Class<List<Integer>> listIntType = new ArrayList<>() {}.getClass();
        TypeBindings.create(listIntType, new JavaType[1], new JavaType[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMismatchingNames() {
        Class<List<Integer>> listIntType = new ArrayList<>() {}.getClass();
        TypeVariable<?>[] typeParams = listIntType.getTypeParameters();
        String[] names = new String[2];
        names[0] = typeParams[0].getName();
        names[1] = "T2";
        TypeBindings.create(listIntType, names);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMismatchingNames2() {
        Class<List<Integer>> listIntType = new ArrayList<>() {}.getClass();
        TypeVariable<?>[] typeParams = listIntType.getTypeParameters();
        String[] names = new String[2];
        names[0] = "T";
        names[1] = typeParams[0].getName();
        TypeBindings.create(listIntType, names);
    }

    @Test
    public void testWithUnboundVariable() {
        Class<List<Integer>> listIntType = new ArrayList<>() {}.getClass();
        TypeBindings bindings = TypeBindings.create(listIntType, null);
        TypeBindings bindings2 = bindings.withUnboundVariable("T2");
        assertEquals(2, bindings2.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals("T2", bindings2.getBoundName(0));
        assertFalse(bindings.hasUnbound("T2"));
        assertTrue(bindings2.hasUnbound("T2"));
        assertNull(bindings.findBoundType("T2"));
        assertNull(bindings2.findBoundType("T2"));
    }
}