package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("foo"));
    }

    @Test
    public void testCreateBindingsForOneTypeParameter() {
        Class<List<Integer>> listIntType = new ArrayList<>() {}.getClass();
        TypeBindings bindings = TypeBindings.create(listIntType, null);
        assertEquals(1, bindings.size());
        assertNotNull(bindings.findBoundType("E"));
    }

    @Test
    public void testCreateBindingsForTwoTypeParameters() {
        Class<Map<Integer, List<String>>> mapIntListStringType = new HashMap<>() {}.getClass();
        TypeBindings bindings = TypeBindings.create(mapIntListStringType, null);
        assertEquals(2, bindings.size());
        assertNotNull(bindings.findBoundType("K"));
        assertNotNull(bindings.findBoundType("V"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForWrongNumberOfTypeParameters() {
        Class<List<Integer>> listIntType = new ArrayList<>() {}.getClass();
        TypeBindings.create(listIntType, new JavaType[] { JavaType.constructRawType(String.class) });
    }

    @Test
    public void testCreateBindingsForClassWithNoTypeParameters() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.create(stringClass, null);
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("foo"));
    }

    @Test
    public void testCreateBindingsForClassWithOneTypeParameter() {
        Class<List<Integer>> listIntType = new ArrayList<>() {}.getClass();
        TypeBindings bindings = TypeBindings.create(listIntType, null);
        assertEquals(1, bindings.size());
        assertNotNull(bindings.findBoundType("E"));
    }

    @Test
    public void testCreateBindingsForClassWithTwoTypeParameters() {
        Class<Map<Integer, List<String>>> mapIntListStringType = new HashMap<>() {}.getClass();
        TypeBindings bindings = TypeBindings.create(mapIntListStringType, null);
        assertEquals(2, bindings.size());
        assertNotNull(bindings.findBoundType("K"));
        assertNotNull(bindings.findBoundType("V"));
    }

    @Test
    public void testCreateBindingsForClassWithTypeParametersAndTypeArgument() {
        Class<Map<Integer, List<String>>> mapIntListStringType = new HashMap<>() {}.getClass();
        TypeVariable<?> intVar = mapIntListStringType.getTypeParameters()[0];
        TypeVariable<?> listVar = mapIntListStringType.getTypeParameters()[1];
        JavaType intTypeArg = JavaType.constructType(Integer.class);
        JavaType listStringTypeArg = JavaType.constructType(List.class, JavaType.constructType(String.class));
        TypeBindings bindings = TypeBindings.create(mapIntListStringType, intTypeArg, listStringTypeArg);
        assertEquals(2, bindings.size());
        assertNotNull(bindings.findBoundType("K"));
        assertNotNull(bindings.findBoundType("V"));
        assertEquals(intTypeArg, bindings.findBoundType("K"));
        assertEquals(listStringTypeArg, bindings.findBoundType("V"));
    }

    @Test
    public void testCreateBindingsForClassWithTypeParametersAndTypeArgumentsList() {
        Class<Map<Integer, List<String>>> mapIntListStringType = new HashMap<>() {}.getClass();
        TypeVariable<?> intVar = mapIntListStringType.getTypeParameters()[0];
        TypeVariable<?> listVar = mapIntListStringType.getTypeParameters()[1];
        JavaType intTypeArg = JavaType.constructType(Integer.class);
        JavaType listStringTypeArg = JavaType.constructType(List.class, JavaType.constructType(String.class));
        TypeBindings bindings = TypeBindings.create(mapIntListStringType, new JavaType[] { intTypeArg, listStringTypeArg });
        assertEquals(2, bindings.size());
        assertNotNull(bindings.findBoundType("K"));
        assertNotNull(bindings.findBoundType("V"));
        assertEquals(intTypeArg, bindings.findBoundType("K"));
        assertEquals(listStringTypeArg, bindings.findBoundType("V"));
    }

    @Test
    public void testCreateBindingsForClassWithTypeParametersAndUnboundVariables() {
        Class<Map<Integer, List<String>>> mapIntListStringType = new HashMap<>() {}.getClass();
        TypeVariable<?> intVar = mapIntListStringType.getTypeParameters()[0];
        TypeVariable<?> listVar = mapIntListStringType.getTypeParameters()[1];
        TypeBindings bindings = TypeBindings.create(mapIntListStringType, null);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("X");
        assertNotNull(bindingsWithUnboundVariable.findBoundType("K"));
        assertNotNull(bindingsWithUnboundVariable.findBoundType("V"));
        assertEquals(3, bindingsWithUnboundVariable.size());
        assertNotNull(bindingsWithUnboundVariable.findBoundType("X"));
        assertNull(bindingsWithUnboundVariable.findBoundType("X2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForClassWithTypeParametersAndUnboundVariableWithWrongName() {
        Class<Map<Integer, List<String>>> mapIntListStringType = new HashMap<>() {}.getClass();
        TypeBindings bindings = TypeBindings.create(mapIntListStringType, null);
        bindings = bindings.withUnboundVariable("X");
        bindings = bindings.withUnboundVariable("Y"); // should be fine
        bindings = bindings.withUnboundVariable("X2"); // should fail
    }
}