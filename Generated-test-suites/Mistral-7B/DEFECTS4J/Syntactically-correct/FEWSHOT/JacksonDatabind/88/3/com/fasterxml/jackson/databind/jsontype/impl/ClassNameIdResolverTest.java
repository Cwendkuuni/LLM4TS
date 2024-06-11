package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private static final TypeFactory typeFactory = new TypeFactoryBuilder().build();

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        Object value = new Object();
        String id = resolver.idFromValue(value);
        String expectedId = value.getClass().getName();
        assertEquals(expectedId, id);

        value = new ArrayList<>();
        id = resolver.idFromValue(value);
        assertEquals("java.util.ArrayList", id);

        value = new HashMap<>();
        id = resolver.idFromValue(value);
        assertEquals("java.util.HashMap", id);

        value = new EnumSet.AllEnumSet(Enum.class);
        id = resolver.idFromValue(value);
        assertEquals("java.util.EnumSet", id);

        value = new EnumMap<>(Enum.class) {
            {
                put(Enum.VAL1, "val1");
            }
        };
        id = resolver.idFromValue(value);
        assertEquals("java.util.EnumMap<java.lang.Enum, java.lang.String>", id);

        value = Arrays.asList(Enum.VAL1, Enum.VAL2);
        id = resolver.idFromValue(value);
        assertEquals("java.util.ArrayList<java.lang.Enum>", id);

        value = Collections.singletonMap(Enum.VAL1, "val1");
        id = resolver.idFromValue(value);
        assertEquals("java.util.HashMap<java.lang.Enum, java.lang.String>", id);

        value = new MyInnerClass();
        id = resolver.idFromValue(value);
        fail("Expected exception: java.lang.IllegalArgumentException");
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        Object value = new Object();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        assertEquals(type.getName(), id);

        value = new ArrayList<>();
        type = ArrayList.class;
        id = resolver.idFromValueAndType(value, type);
        assertEquals(type.getName(), id);

        value = new HashMap<>();
        type = HashMap.class;
        id = resolver.idFromValueAndType(value, type);
        assertEquals(type.getName(), id);

        value = new EnumSet.AllEnumSet(Enum.class);
        type = EnumSet.class;
        id = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.EnumSet<java.lang.Enum>", id);

        value = new EnumMap<>(Enum.class) {
            {
                put(Enum.VAL1, "val1");
            }
        };
        type = EnumMap.class;
        id = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.EnumMap<java.lang.Enum, java.lang.String>", id);

        value = Arrays.asList(Enum.VAL1, Enum.VAL2);
        type = ArrayList.class;
        id = resolver.idFromValueAndType(value, type.getComponentType());
        assertEquals("java.lang.Object", id);

        value = Collections.singletonMap(Enum.VAL1, "val1");
        type = HashMap.class;
        id = resolver.idFromValueAndType(value, type.getComponentType());
        assertEquals("java.lang.Enum, java.lang.String", id);

        value = new MyInnerClass();
        type = MyOuterClass.InnerClass.class;
        id = resolver.idFromValueAndType(value, type);
        assertEquals(type.getName(), id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        String id = "java.util.ArrayList";
        JavaType javaType = resolver.typeFromId(null, id);
        assertEquals(ArrayList.class, javaType.getRawClass());

        id = "java.util.EnumMap<java.lang.Enum, java.lang.String>";
        javaType = resolver.typeFromId(null, id);
        assertEquals(EnumMap.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        assertEquals(Enum.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        assertEquals(String.class, javaType.getRawClass());

        id = "java.lang.Object";
        javaType = resolver.typeFromId(null, id);
        assertEquals(Object.class, javaType.getRawClass());

        id = "java.util.HashMap<java.lang.String, java.util.List<java.lang.Number>>";
        javaType = resolver.typeFromId(null, id);
        assertEquals(HashMap.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        assertEquals(String.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        javaType = javaType.getComponentType();
        assertEquals(List.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        assertEquals(Number.class, javaType.getRawClass());

        id = "java.lang.Class<java.util.ArrayList>";
        javaType = resolver.typeFromId(null, id);
        assertEquals(ArrayList.class.getTypeName().getCanonicalName(), id);
        assertEquals(ArrayList.class, javaType.getRawClass());

        id = "java.lang.Class<java.util.EnumMap<java.lang.Enum, java.lang.String>>";
        javaType = resolver.typeFromId(null, id);
        assertEquals(EnumMap.class.getTypeName().getCanonicalName(), id);
        assertEquals(EnumMap.class, javaType.getRawClass());

        id = "java.lang.Class<java.lang.Enum>";
        javaType = resolver.typeFromId(null, id);
        assertEquals(Enum.class.getTypeName().getCanonicalName(), id);
        assertEquals(Enum.class, javaType.getRawClass());

        id = "java.lang.Class<java.lang.Number>";
        javaType = resolver.typeFromId(null, id);
        assertEquals(Number.class.getTypeName().getCanonicalName(), id);
        assertEquals(Number.class, javaType.getRawClass());

        id = "java.lang.Class<java.lang.String>";
        javaType = resolver.typeFromId(null, id);
        assertEquals(String.class.getTypeName().getCanonicalName(), id);
        assertEquals(String.class, javaType.getRawClass());

        id = "java.lang.Class<java.lang.Object>";
        javaType = resolver.typeFromId(null, id);
        assertEquals(Object.class.getTypeName().getCanonicalName(), id);
        assertEquals(Object.class, javaType.getRawClass());

        id = "java.lang.Class<java.lang.Class<java.lang.Object>>";
        javaType = resolver.typeFromId(null, id);
        assertEquals(Class.class.getTypeName().getCanonicalName(), id);
        assertEquals(Class.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        assertEquals(Object.class, javaType.getRawClass());

        id = "java.lang.Class<java.lang.Class<java.lang.Class<java.lang.Object>>>";
        javaType = resolver.typeFromId(null, id);
        assertEquals(Class.class.getTypeName().getCanonicalName(), id);
        assertEquals(Class.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        assertEquals(Class.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        assertEquals(Class.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        assertEquals(Object.class, javaType.getRawClass());

        id = "java.lang.Class<java.util.Arrays$ArrayList>";
        javaType = resolver.typeFromId(null, id);
        assertEquals("java.util.ArrayList", id);
        assertEquals(ArrayList.class, javaType.getRawClass());

        id = "java.lang.Class<java.util.Collections$SingletonMap>";
        javaType = resolver.typeFromId(null, id);
        assertEquals("java.util.Map", id);
        assertEquals(HashMap.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        assertEquals(String.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        assertEquals(Object.class, javaType.getRawClass());

        id = "java.lang.Class<java.util.Collections$SingletonList>";
        javaType = resolver.typeFromId(null, id);
        assertEquals("java.util.List", id);
        assertEquals(ArrayList.class, javaType.getRawClass());
        javaType = javaType.getComponentType();
        assertEquals(String.class, javaType.getRawClass());

        id = "java.lang.Class<my.package.MyClass>";
        javaType = resolver.typeFromId(null, id);
        fail("Expected exception: java.lang.ClassNotFoundException");
    }

    private static class MyOuterClass {
        static class InnerClass {
        }
    }

    private static class MyInnerClass {
    }
}