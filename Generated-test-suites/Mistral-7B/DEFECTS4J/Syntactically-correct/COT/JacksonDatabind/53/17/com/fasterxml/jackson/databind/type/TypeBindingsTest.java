package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameter;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateEmptyMapTypeBindings() {
        Class<?> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.create(mapClass, Collections.emptyList());
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMapTypeBindingsWithInvalidNumberOfTypeArgs() {
        Class<?> mapClass = Map.class;
        List<JavaType> typeList = Arrays.asList(JavaType.constructUnboundTypeArgumentList(2));
        TypeBindings bindings = TypeBindings.create(mapClass, typeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMapTypeBindingsWithInvalidTypeArgs() {
        Class<?> mapClass = Map.class;
        List<JavaType> typeList = Arrays.asList(JavaType.constructTypeArgument(Object.class), JavaType.constructTypeArgument(int[].class));
        TypeBindings bindings = TypeBindings.create(mapClass, typeList);
    }

    @Test
    public void testCreateMapTypeBindings() {
        Class<?> mapClass = Map.class;
        List<JavaType> typeList = Arrays.asList(JavaType.constructTypeArgument(String.class), JavaType.constructTypeArgument(Integer.class));
        TypeBindings bindings = TypeBindings.create(mapClass, typeList);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());

        assertEquals("keyType", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));

        assertEquals("valueType", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1));
    }

    @Test
    public void testCreateListTypeBindings() {
        Class<?> listClass = List.class;
        List<JavaType> typeList = Arrays.asList(JavaType.constructTypeArgument(String.class));
        TypeBindings bindings = TypeBindings.create(listClass, typeList);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());

        assertEquals("typeVariableName", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
    }

    @Test
    public void testCreateArrayListTypeBindings() {
        Class<?> arrayListClass = ArrayList.class;
        List<JavaType> typeList = Arrays.asList(JavaType.constructTypeArgument(Integer.class));
        TypeBindings bindings = TypeBindings.create(arrayListClass, typeList);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());

        assertEquals("typeVariableName", bindings.getBoundName(0));
        assertEquals(Integer.class, bindings.getBoundType(0));
    }

    @Test
    public void testCreateAbstractListTypeBindings() {
        Class<?> abstractListClass = AbstractList.class;
        List<JavaType> typeList = Collections.emptyList();
        TypeBindings bindings = TypeBindings.create(abstractListClass, typeList);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());

        bindings = TypeBindings.create(abstractListClass, Arrays.asList(JavaType.constructTypeArgument(String.class)));
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());

        assertEquals("typeVariableName", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
    }

    @Test
    public void testCreateCollectionTypeBindings() {
        Class<?> collectionClass = Collection.class;
        List<JavaType> typeList = Collections.emptyList();
        TypeBindings bindings = TypeBindings.create(collectionClass, typeList);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());

        bindings = TypeBindings.create(collectionClass, Arrays.asList(JavaType.constructTypeArgument(String.class)));
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());

        assertEquals("typeVariableName", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIterableTypeBindings() {
        Class<?> iterableClass = Iterable.class;
        List<JavaType> typeList = Collections.emptyList();
        TypeBindings bindings = TypeBindings.create(iterableClass, typeList);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());

        bindings = TypeBindings.create(iterableClass, Arrays.asList(JavaType.constructTypeArgument(String.class)));
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());

        assertEquals("typeVariableName", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
    }

    @Test
    public void testFindBoundType() {
        Class<?> mapClass = Map.class;
        List<JavaType> typeList = Arrays.asList(JavaType.constructTypeArgument(String.class), JavaType.constructTypeArgument(Integer.class));
        TypeBindings bindings = TypeBindings.create(mapClass, typeList);

        assertEquals(bindings.getBoundType(0), bindings.findBoundType("keyType"));
        assertEquals(bindings.getBoundType(1), bindings.findBoundType("valueType"));

        assertNull(bindings.findBoundType("unknownType"));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<?> mapClass = Map.class;
        List<JavaType> typeList = Arrays.asList(JavaType.constructTypeArgument(String.class), JavaType.constructTypeArgument(Integer.class));
        TypeBindings bindings = TypeBindings.create(mapClass, typeList);

        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("unknownType");
        String[] unboundVariables = bindingsWithUnboundVariable.getUnboundVariableNames();
        assertEquals(1, unboundVariables.length);
        assertEquals("unknownType", unboundVariables[0]);
    }

    @Test
    public void testGetUnboundVariableNames() {
        Class<?> mapClass = Map.class;
        TypeVariable<?>[] vars = mapClass.getTypeParameters();
        List<JavaType> typeList = Arrays.asList(JavaType.constructTypeArgument(vars[0]), JavaType.constructTypeArgument(vars[1]));
        TypeBindings bindings = TypeBindings.create(mapClass, typeList);

        String[] unboundVariables = bindings.getUnboundVariableNames();
        assertEquals(2, unboundVariables.length);
        assertEquals(vars[0].getName(), unboundVariables[0]);
        assertEquals(vars[1].getName(), unboundVariables[1]);
    }

    @Test
    public void testTypeParameterArray() {
        Class<?> mapClass = Map.class;
        List<JavaType> typeList = Arrays.asList(JavaType.constructTypeArgument(String.class), JavaType.constructTypeArgument(Integer.class));
        TypeBindings bindings = TypeBindings.create(mapClass, typeList);

        JavaType[] typeParameterArray = bindings.typeParameterArray();
        assertEquals(2, typeParameterArray.length);
        assertEquals(bindings.getBoundType(0), typeParameterArray[0]);
        assertEquals(bindings.getBoundType(1), typeParameterArray[1]);
    }

    @Test
    public void testToString() {
        Class<?> mapClass = Map.class;
        List<JavaType> typeList = Arrays.asList(JavaType.constructTypeArgument(String.class), JavaType.constructTypeArgument(Integer.class));
        TypeBindings bindings = TypeBindings.create(mapClass, typeList);

        String stringRepresentation = bindings.toString();
        String expectedStringRepresentation = "<Map<String, Integer>>";
        assertEquals(expectedStringRepresentation, stringRepresentation);
    }

    @Test
    public void testEquals() {
        Class<?> mapClass = Map.class;
        List<JavaType> typeList1 = Arrays.asList(JavaType.constructTypeArgument(String.class), JavaType.constructTypeArgument(Integer.class));
        List<JavaType> typeList2 = Arrays.asList(JavaType.constructTypeArgument(String.class), JavaType.constructTypeArgument(Integer.class));
        List<JavaType> typeList3 = Arrays.asList(JavaType.constructTypeArgument(String.class), JavaType.constructTypeArgument(Double.class));
        TypeBindings bindings1 = TypeBindings.create(mapClass, typeList1);
        TypeBindings bindings2 = TypeBindings.create(mapClass, typeList2);
        TypeBindings bindings3 = TypeBindings.create(ArrayList.class, typeList1);

        assertTrue(bindings1.equals(bindings1));
        assertTrue(bindings1.equals(bindings2));
        assertFalse(bindings1.equals(bindings3));
    }

    @Test
    public void testHashCode() {
        Class<?> mapClass = Map.class;
        List<JavaType> typeList1 = Arrays.asList(JavaType.constructTypeArgument(String.class), JavaType.constructTypeArgument(Integer.class));
        List<JavaType> typeList2 = Arrays.asList(JavaType.constructTypeArgument(String.class), JavaType.constructTypeArgument(Integer.class));
        List<JavaType> typeList3 = Arrays.asList(JavaType.constructTypeArgument(String.class), JavaType.constructTypeArgument(Double.class));
        TypeBindings bindings1 = TypeBindings.create(mapClass, typeList1);
        TypeBindings bindings2 = TypeBindings.create(mapClass, typeList2);
        TypeBindings bindings3 = TypeBindings.create(ArrayList.class, typeList1);

        int hashCode1 = bindings1.hashCode();
        int hashCode2 = bindings2.hashCode();
        int hashCode3 = bindings3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }

    
}