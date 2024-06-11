package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {
    private static final TypeFactory typeFactory = new TypeFactoryBuilder().build();

    @Test
    public void testIdFromValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with primitive types
        assertEquals("java.lang.Integer", resolver.idFromValue(42));
        assertEquals("java.lang.Boolean", resolver.idFromValue(true));
        assertEquals("java.lang.String", resolver.idFromValue("hello"));

        // Test with simple classes
        assertEquals("java.lang.String", resolver.idFromValue("Hello World"));
        assertEquals("java.util.ArrayList", resolver.idFromValue(new ArrayList<>()));
        assertEquals("java.util.HashMap", resolver.idFromValue(new HashMap<>()));
        assertEquals("java.util.Arrays$ArrayList", resolver.idFromValue(Arrays.asList(new String[]{"a", "b"})));

        // Test with custom classes
        AnnotatedClass ac = mapper.introspect(MyClass.class);
        assertEquals(ac.getRawClass().getName(), resolver.idFromValue(new MyClass()));

        // Test with Enum classes
        assertEquals("java.lang.Enum", resolver.idFromValue(MyEnum.VAL1));
        assertEquals("com.example.MyEnum$MyEnumSubType", resolver.idFromValue(MyEnumSubType.VAL1));

        // Test with collections of Enums
        assertEquals("java.util.EnumSet", resolver.idFromValue(EnumSet.allOf(MyEnum.class)));
        assertEquals("java.util.EnumMap", resolver.idFromValue(new EnumMap<>(MyEnum.class) {
            {
                put(MyEnum.VAL1, "value1");
                put(MyEnum.VAL2, "value2");
            }
        }));
    }

    @Test
    public void testIdFromValueAndType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with primitive types
        assertEquals("java.lang.Integer", resolver.idFromValueAndType(42, Integer.class));
        assertEquals("java.lang.Boolean", resolver.idFromValueAndType(true, Boolean.class));
        assertEquals("java.lang.String", resolver.idFromValueAndType("hello", String.class));

        // Test with simple classes
        assertEquals("java.util.ArrayList", resolver.idFromValueAndType(new ArrayList<>(), ArrayList.class));
        assertEquals("java.util.HashMap", resolver.idFromValueAndType(new HashMap<>(), HashMap.class));
        assertEquals("java.util.Arrays$ArrayList", resolver.idFromValueAndType(Arrays.asList(new String[]{"a", "b"}), ArrayList.class));

        // Test with custom classes
        AnnotatedClass ac = mapper.introspect(MyClass.class);
        assertEquals(ac.getRawClass().getName(), resolver.idFromValueAndType(new MyClass(), MyClass.class));

        // Test with Enum classes
        assertEquals("java.lang.Enum", resolver.idFromValueAndType(MyEnum.VAL1, Enum.class));
        assertEquals("com.example.MyEnum$MyEnumSubType", resolver.idFromValueAndType(MyEnumSubType.VAL1, MyEnumSubType.class));

        // Test with collections of Enums
        assertEquals("java.util.EnumSet", resolver.idFromValueAndType(EnumSet.allOf(MyEnum.class), EnumSet.class));
        assertEquals("java.util.EnumMap", resolver.idFromValueAndType(new EnumMap<>(MyEnum.class) {
            {
                put(MyEnum.VAL1, "value1");
                put(MyEnum.VAL2, "value2");
            }
        }, EnumMap.class));
    }

    @Test
    public void testTypeFromId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        // Test with basic classes
        JavaType javaLangInteger = resolver.typeFromId(mapper.getDatabindContext(), "java.lang.Integer");
        assertEquals(Integer.class, javaLangInteger.getRawClass());
        JavaType javaLangString = resolver.typeFromId(mapper.getDatabindContext(), "java.lang.String");
        assertEquals(String.class, javaLangString.getRawClass());

        // Test with primitive wrappers
        JavaType javaLangIntegerWrapper = resolver.typeFromId(mapper.getDatabindContext(), "java.lang.Integer");
        assertEquals(Integer.class, javaLangIntegerWrapper.getRawClass());
        JavaType javaLangBooleanWrapper = resolver.typeFromId(mapper.getDatabindContext(), "java.lang.Boolean");
        assertEquals(Boolean.class, javaLangBooleanWrapper.getRawClass());

        // Test with custom classes
        JavaType myClass = resolver.typeFromId(mapper.getDatabindContext(), MyClass.class.getName());
        assertEquals(MyClass.class, myClass.getRawClass());

        // Test with collections of custom classes
        JavaType javaUtilArrayList = resolver.typeFromId(mapper.getDatabindContext(), "java.util.ArrayList");
        JavaType javaUtilArrayListOfMyClass = resolver.typeFromId(mapper.getDatabindContext(), "java.util.ArrayList<com.example.MyClass>");
        assertEquals(new TypeReference<ArrayList<MyClass>>(){}.getType(), javaUtilArrayListOfMyClass);

        // Test with Enum classes
        JavaType javaLangEnum = resolver.typeFromId(mapper.getDatabindContext(), "java.lang.Enum");
        assertEquals(Enum.class, javaLangEnum.getRawClass());
        JavaType comExampleMyEnum = resolver.typeFromId(mapper.getDatabindContext(), "com.example.MyEnum");
        JavaType comExampleMyEnumMyEnumSubType = resolver.typeFromId(mapper.getDatabindContext(), "com.example.MyEnum$MyEnumSubType");
        assertEquals(MyEnum.class, comExampleMyEnum.getRawClass());
        assertEquals(MyEnumSubType.class, comExampleMyEnumMyEnumSubType.getRawClass());

        // Test with collections of Enum classes
        JavaType javaUtilEnumSet = resolver.typeFromId(mapper.getDatabindContext(), "java.util.EnumSet<com.example.MyEnum>");
        JavaType javaUtilEnumMap = resolver.typeFromId(mapper.getDatabindContext(), "java.util.EnumMap<com.example.MyEnum, java.lang.String>");
        assertEquals(new TypeReference<EnumSet<MyEnum>>(){}.getType(), javaUtilEnumSet);
        assertEquals(new TypeReference<EnumMap<MyEnum, String>>(){}.getType(), javaUtilEnumMap);
    }

    private static class MyClass {
        // Empty class for testing purposes
    }

    private static enum MyEnum {
        VAL1, VAL2;

        public static MyEnumSubType VAL3 = new MyEnumSubType("VAL3");

        private String name;

        MyEnum(String name) {
            this.name = name;
        }
    }

    private static class MyEnumSubType extends MyEnum {
        private final String name;

        MyEnumSubType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}