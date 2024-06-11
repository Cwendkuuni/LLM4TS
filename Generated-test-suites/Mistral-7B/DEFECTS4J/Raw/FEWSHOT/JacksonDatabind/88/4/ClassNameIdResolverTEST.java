package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIdFromValue() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());
        assertEquals("java.lang.String", resolver.idFromValue("hello"));
        assertEquals("java.util.ArrayList", resolver.idFromValue(new ArrayList<>()));
        assertEquals("java.util.HashMap", resolver.idFromValue(new HashMap<>()));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());
        assertEquals("java.lang.String", resolver.idFromValueAndType("hello", String.class));
        assertEquals("java.util.ArrayList", resolver.idFromValueAndType(new ArrayList<>(), ArrayList.class));
        assertEquals("java.util.HashMap", resolver.idFromValueAndType(new HashMap<>(), HashMap.class));
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());
        assertEquals(String.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.String"));
        assertEquals(ArrayList.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList"));
        assertEquals(HashMap.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.HashMap"));
        assertNull(resolver.typeFromId(mapper.getDeserializationContext(), "com.nonexistent.Class"));
    }

    @Test
    public void testIdFromValueEnum() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());
        assertEquals("java.lang.String", resolver.idFromValue("hello"));
        assertEquals("java.lang.Boolean", resolver.idFromValue(Boolean.TRUE));
        assertEquals("java.lang.Enum", resolver.idFromValue(Enum.class));
        assertEquals("com.example.MyEnum", resolver.idFromValue(MyEnum.FOO));
    }

    @Test
    public void testIdFromValueEnumMap() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());
        assertEquals("java.util.Map", resolver.idFromValue(new EnumMap<>(MyEnum.class)));
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>",
                resolver.idFromValue(new EnumMap<>(MyEnum.class) {
                    {
                        put(MyEnum.FOO, 1);
                        put(MyEnum.BAR, 2);
                    }
                }));
    }

    @Test
    public void testIdFromValueEnumSet() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());
        assertEquals("java.util.Set", resolver.idFromValue(new EnumSet<>(MyEnum.class)));
        assertEquals("java.util.EnumSet<java.lang.Enum>",
                resolver.idFromValue(EnumSet.allOf(MyEnum.class)));
    }

    @Test
    public void testTypeFromIdCollection() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());
        assertEquals(List.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.List"));
        assertEquals(ArrayList.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList"));
        assertEquals(LinkedList.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.LinkedList"));
    }

    @Test
    public void testTypeFromIdMap() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());
        assertEquals(Map.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.Map"));
        assertEquals(HashMap.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.HashMap"));
        assertEquals(TreeMap.class, resolver.typeFromId(mapper.getDeserializationContext(), "java.util.TreeMap"));
    }

    @Test
    public void testTypeFromIdCustomClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());
        Class customClass = new CustomClass();
        assertEquals(customClass.getClass(), resolver.typeFromId(mapper.getDeserializationContext(), customClass.getClass().getName()));
    }

    static class CustomClass {
        // empty class
    }

    enum MyEnum {
        FOO, BAR
    }
}