package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse_SimpleClass() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType parse = parser.parse("String");
        assertEquals(String.class, parse.getRawClass());
    }

    @Test
    public void testParse_ClassWithGenerics() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType parse = parser.parse("List<String>");
        assertEquals(Collections.list(String.class), parse.getRawClass());
    }

    @Test
    public void testParse_ClassWithInnerClass() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType parse = parser.parse("java.util.Map$Entry");
        assertEquals(Map.Entry.class, parse.getRawClass());
    }

    @Test
    public void testParse_ArrayType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType parse = parser.parse("int[]");
        JavaType componentType = parse.getContentType();
        assertEquals(int[].class, parse.getRawClass());
        assertEquals(Integer.TYPE, componentType.getRawClass());
    }

    @Test
    public void testParse_NestedTypes() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType parse = parser.parse("java.util.Map<String, java.util.List<Integer>>");
        assertEquals(Map.class, parse.getRawClass());
        Map<?, ?> map = parse.getValueInstance(Collections.emptyMap());
        assertEquals(String.class, map.keySet().getClass());
        assertEquals(List.class, map.getValueType().getParameterTypes()[0]);
        assertEquals(Integer.class, map.getValueType().getParameterTypes()[0].getRawClass());
    }

    @Test
    public void testParse_MalformedInput() {
        TypeParser parser = new TypeParser(new TypeFactory());
        assertThrows(IllegalArgumentException.class, () -> parser.parse(""));
        assertThrows(IllegalArgumentException.class, () -> parser.parse(" String"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<String> "));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<String> <>"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<String> <String> >"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<String> <String, String> >"));
    }

    @Test
    public void testParse_CustomClassLoader() {
        ClassLoader customClassLoader = new sun.misc.Launcher$AppClassLoader("test");
        TypeFactory factory = new TypeFactory(customClassLoader);
        TypeParser parser = new TypeParser(factory);
        JavaType parse = parser.parse("MyCustomClass");
        assertEquals(MyCustomClass.class, parse.getRawClass());
    }

    @Test
    public void testParse_RecursiveType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType listOfListOfInt = parser.parse("List<List<Integer>>");
        assertEquals(List.class, listOfListOfInt.getRawClass());
        List<? extends List<? extends Number>> list = listOfListOfInt.getValueInstance(Collections.emptyList());
        assertTrue(list.isEmpty());
        List<List<Integer>> listInstance = listOfListOfInt.getValueInstance(Collections.singletonList(Collections.emptyList()));
        assertFalse(listInstance.isEmpty());
        List<List<Integer>> list2 = listInstance.get(0);
        assertTrue(list2.isEmpty());
        List<Integer> list3 = list2.get(0);
        assertEquals(Collections.emptyList(), list3);
        assertEquals(Integer.class, list3.getClass().getComponentType());
    }

    @Test
    public void testParse_TypeWithWildcard() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType parse = parser.parse("List<? extends Number>");
        assertEquals(List.class, parse.getRawClass());
        List<? extends Number> list = parse.getValueInstance(Collections.emptyList());
        assertTrue(list.isEmpty());
        list.add(1);
        assertThrows(UnsupportedOperationException.class, () -> list.set(0, 2));
        assertThrows(ClassCastException.class, () -> list.get(0).intValue());
        assertTrue(list.get(0) instanceof Number);
    }

    @Test
    public void testParse_TypeWithArrayWildcard() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType parse = parser.parse("List<Integer[]>");
        assertEquals(List.class, parse.getRawClass());
        List<Integer[]> list = parse.getValueInstance(Collections.emptyList());
        assertTrue(list.isEmpty());
        Integer[] intArray = new Integer[1];
        intArray[0] = 1;
        list.add(intArray);
        Integer[] listElement = list.get(0);
        assertEquals(1, listElement.length);
        assertEquals(1, listElement[0].intValue());
    }

    @Test
    public void testParse_TypeWithMultipleWildcards() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType parse = parser.parse("List<? extends Number> <? extends Number>");
        assertEquals(List.class, parse.getRawClass());
        List<? extends Number> list = parse.getValueInstance(Collections.emptyList());
        assertTrue(list.isEmpty());
        list.add(1);
        assertThrows(UnsupportedOperationException.class, () -> list.set(0, 2));
        assertThrows(ClassCastException.class, () -> list.get(0).intValue());
        assertTrue(list.get(0) instanceof Number);
    }

    @Test
    public void testParse_TypeWithMissingClosingBracket() {
        TypeParser parser = new TypeParser(new TypeFactory());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<String> "));
    }

    @Test
    public void testParse_TypeWithExtraClosingBracket() {
        TypeParser parser = new TypeParser(new TypeFactory());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<String> >"));
    }

    @Test
    public void testParse_TypeWithMissingComma() {
        TypeParser parser = new TypeParser(new TypeFactory());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<String, Integer>"));
    }

    @Test
    public void testParse_TypeWithMissingTypeArgument() {
        TypeParser parser = new TypeParser(new TypeFactory());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<>"));
    }

    @Test
    public void testParse_TypeWithNonExistentClass() {
        TypeParser parser = new TypeParser(new TypeFactory());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("NonExistentClass"));
    }

    @Test
    public void testParse_TypeWithIllegalCharacter() {
        TypeParser parser = new TypeParser(new TypeFactory());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<String%>"));
    }

    @Test
    public void testParse_TypeWithCustomTypeFactory() {
        List<Integer> intList = Arrays.asList(1, 2, 3);
        TypeFactory customFactory = new TypeFactory() {
            @Override
            public JavaType _fromClass(BeanPropertyType property, JavaType base, TypeBindings bindings) {
                if (List.class.isAssignableFrom(base.getRawClass())) {
                    return _fromCollection(property, base, bindings, intList);
                }
                return super._fromClass(property, base, bindings);
            }

            private JavaType _fromCollection(BeanPropertyType property, JavaType base, TypeBindings bindings,
                    List<?> list) {
                JavaType elementType = bindings.getElementType();
                return _factory._fromCollection(property, base, bindings, elementType, list.getClass());
            }
        };
        TypeParser parser = new TypeParser(customFactory);
        JavaType parse = parser.parse("List<Integer>");
        assertEquals(List.class, parse.getRawClass());
        List<?> list = parse.getValueInstance(Collections.emptyList());
        assertEquals(Collections.emptyList(), list);
        List<Integer> listInstance = parse.getValueInstance(Collections.singletonList(Collections.emptyList()));
        assertEquals(Collections.singletonList(Collections.emptyList()), listInstance);
        List<Integer> list2 = listInstance.get(0);
        assertEquals(Collections.emptyList(), list2);
        List<Integer> list3 = list2.subList(0, 1);
        assertEquals(1, list3.size());
        assertEquals(1, list3.get(0).intValue());
    }
}