package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

public class ClassNameIdResolverTest {

    @Test
    public void testIdFromValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with basic classes
        String id = resolver.idFromValue("hello world".getClass());
        Assert.assertEquals("java.lang.String", id);

        id = resolver.idFromValue(new Object() {});
        Assert.assertNull(id);

        // Test with arrays
        id = resolver.idFromValue(new int[2]);
        Assert.assertEquals("java.lang.int[2]", id);

        id = resolver.idFromValue(new Object[] { 1, 2 });
        Assert.assertEquals("java.lang.Object[]", id);

        // Test with collections
        id = resolver.idFromValue(Arrays.asList(1, 2));
        Assert.assertEquals("java.util.ArrayList", id);

        // Test with enum
        Enum e = Enum.values()[0];
        id = resolver.idFromValue(e.getClass());
        Assert.assertEquals("java.lang.Enum", id);

        id = resolver.idFromValue(Enum.class);
        Assert.assertEquals("java.lang.Class<java.lang.Enum>", id);

        id = resolver.idFromValue(Collections.singletonMap("key", e));
        Assert.assertEquals("java.util.Map<java.lang.String,java.lang.Enum>", id);

        // Test with inner class
        Class<?> outerClass = Enum.class;
        Class<?> innerClass = new AnonymousInnerClass();

        id = resolver.idFromValue(innerClass);
        Assert.assertEquals("java.lang.Class<" + innerClass.getName() + ">", id);

        // Test with generic class
        id = resolver.idFromValue(new ArrayList<>());
        Assert.assertEquals("java.util.ArrayList", id);

        id = resolver.idFromValue(new HashMap<>().getClass());
        Assert.assertEquals("java.util.HashMap", id);

        id = resolver.idFromValue(new EnumMap<>(Enum.class).getClass());
        Assert.assertEquals("java.util.EnumMap<java.lang.Enum,java.lang.Object>", id);

        id = resolver.idFromValue(new EnumSet<>(EnumSet.allOf(Enum.class)).getClass());
        Assert.assertEquals("java.util.EnumSet<java.lang.Enum>", id);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with basic classes
        Object value = "hello world";
        String id = resolver.idFromValueAndType(value, value.getClass());
        Assert.assertEquals("java.lang.String", id);

        // Test with arrays
        value = new int[2];
        id = resolver.idFromValueAndType(value, value.getClass());
        Assert.assertEquals("java.lang.int[2]", id);

        value = new Object[] { 1, 2 };
        id = resolver.idFromValueAndType(value, value.getClass());
        Assert.assertEquals("java.lang.Object[]", id);

        // Test with collections
        value = Arrays.asList(1, 2);
        id = resolver.idFromValueAndType(value, value.getClass());
        Assert.assertEquals("java.util.ArrayList", id);

        // Test with enum
        Enum e = Enum.values()[0];
        id = resolver.idFromValueAndType(e, e.getClass());
        Assert.assertEquals("java.lang.Enum", id);

        id = resolver.idFromValueAndType(Enum.class, Enum.class);
        Assert.assertEquals("java.lang.Class<java.lang.Enum>", id);

        id = resolver.idFromValueAndType(Collections.singletonMap("key", e), Map.class);
        Assert.assertEquals("java.util.Map", id);

        // Test with inner class
        Class<?> outerClass = Enum.class;
        Class<?> innerClass = new AnonymousInnerClass();

        id = resolver.idFromValueAndType(innerClass, innerClass.getClass());
        Assert.assertEquals("java.lang.Class<" + innerClass.getName() + ">", id);

        // Test with generic class
        id = resolver.idFromValueAndType(new ArrayList<>(), ArrayList.class);
        Assert.assertEquals("java.util.ArrayList", id);

        id = resolver.idFromValueAndType(new HashMap<>(), HashMap.class);
        Assert.assertEquals("java.util.HashMap", id);

        id = resolver.idFromValueAndType(new EnumMap<>(Enum.class), EnumMap.class);
        Assert.assertEquals("java.util.EnumMap<java.lang.Enum,java.lang.Object>", id);

        id = resolver.idFromValueAndType(new EnumSet<>(EnumSet.allOf(Enum.class)), EnumSet.class);
        Assert.assertEquals("java.util.EnumSet<java.lang.Enum>", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with basic classes
        JavaType javaLangString = typeFactory.constructType(String.class);
        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(javaLangString, javaType);

        // Test with arrays
        JavaType intArray = typeFactory.constructType(int[].class);
        id = "java.lang.int[2]";
        javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(intArray, javaType);

        JavaType objectArray = typeFactory.constructType(Object[].class);
        id = "java.lang.Object[]";
        javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(objectArray, javaType);

        // Test with collections
        JavaType javaUtilArrayList = typeFactory.constructType(ArrayList.class);
        id = "java.util.ArrayList";
        javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(javaUtilArrayList, javaType);

        // Test with enum
        JavaType javaLangEnum = typeFactory.constructType(Enum.class);
        id = "java.lang.Enum";
        javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(javaLangEnum, javaType);

        JavaType javaLangClassEnum = typeFactory.constructType(Class.class, Enum.class);
        id = "java.lang.Class<java.lang.Enum>";
        javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(javaLangClassEnum, javaType);

        JavaType javaUtilMap = typeFactory.constructType(Map.class);
        id = "java.util.Map";
        javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(javaUtilMap, javaType);

        // Test with inner class
        JavaType anonymousInnerClass = typeFactory.constructType(AnonymousInnerClass.class);
        id = "java.lang.Class<" + AnonymousInnerClass.class.getName() + ">";
        javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(anonymousInnerClass, javaType);

        // Test with generic class
        JavaType javaUtilArrayListGeneric = typeFactory.constructType(ArrayList.class, String.class);
        id = "java.util.ArrayList";
        javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(javaUtilArrayListGeneric, javaType);

        JavaType javaUtilHashMap = typeFactory.constructType(Map.class, String.class, String.class);
        id = "java.util.Map";
        javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(javaUtilHashMap, javaType);

        JavaType javaUtilEnumMap = typeFactory.constructType(EnumMap.class, Enum.class, String.class);
        id = "java.util.EnumMap<java.lang.Enum,java.lang.String>";
        javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(javaUtilEnumMap, javaType);

        JavaType javaUtilEnumSet = typeFactory.constructType(EnumSet.class, Enum.class);
        id = "java.util.EnumSet<java.lang.Enum>";
        javaType = resolver.typeFromId(null, id);
        Assert.assertEquals(javaUtilEnumSet, javaType);
    }

    static class AnonymousInnerClass {
        // empty inner class
    }

}