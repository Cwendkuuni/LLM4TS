package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private static final TypeFactory typeFactory = new TypeFactory();

    @Test
    public void testIdFromValue() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        // Test with basic classes
        assertEquals("java.lang.String", resolver.idFromValue("Hello World"));
        assertEquals("java.util.ArrayList", resolver.idFromValue(new ArrayList<>()));
        assertEquals("java.lang.Integer", resolver.idFromValue(123));

        // Test with enums
        assertEquals("java.lang.Enum", resolver.idFromValue(Enum.class));
        assertEquals("java.lang.Enum$EnumConstantType", resolver.idFromValue(Enum.class.getEnumConstants()[0].getClass()));

        // Test with collections
        Object enumSet = new EnumSet<>(Enum.values());
        assertEquals("java.util.EnumSet", resolver.idFromValue(enumSet));
        Object enumMap = new EnumMap<>(Enum.class) {{ put(Enum.VALUES[0], "value"); }};
        assertEquals("java.util.EnumMap", resolver.idFromValue(enumMap));

        // Test with inner classes
        Class innerClass = Class.forName("java.lang.String$CaseInsensitiveComparator");
        assertEquals("java.lang.String$CaseInsensitiveComparator", resolver.idFromValue(innerClass.newInstance()));

        // Test with custom classes
        Class customClass = Class.forName("com.example.CustomClass");
        assertEquals("com.example.CustomClass", resolver.idFromValue(new CustomClass()));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        // Test with basic classes
        assertEquals("java.lang.String", resolver.idFromValueAndType("Hello World", String.class));
        assertEquals("java.util.ArrayList", resolver.idFromValueAndType(new ArrayList<>(), ArrayList.class));
        assertEquals("java.lang.Integer", resolver.idFromValueAndType(123, Integer.class));

        // Test with enums
        assertEquals("java.lang.Enum", resolver.idFromValueAndType(Enum.class, Enum.class));
        assertEquals("java.lang.Enum$EnumConstantType", resolver.idFromValueAndType(Enum.class.getEnumConstants()[0], Enum.class.getEnumConstants()[0].getClass()));

        // Test with collections
        Object enumSet = new EnumSet<>(Enum.values());
        assertEquals("java.util.EnumSet", resolver.idFromValueAndType(enumSet, EnumSet.class));
        Object enumMap = new EnumMap<>(Enum.class) {{ put(Enum.VALUES[0], "value"); }};
        assertEquals("java.util.EnumMap", resolver.idFromValueAndType(enumMap, EnumMap.class));

        // Test with inner classes
        Class innerClass = Class.forName("java.lang.String$CaseInsensitiveComparator");
        assertEquals("java.lang.String$CaseInsensitiveComparator", resolver.idFromValueAndType(innerClass.newInstance(), innerClass));

        // Test with custom classes
        Class customClass = Class.forName("com.example.CustomClass");
        assertEquals("com.example.CustomClass", resolver.idFromValueAndType(new CustomClass(), customClass));
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        // Test with basic classes
        JavaType stringType = resolver.typeFromId(null, "java.lang.String");
        assertEquals(String.class, stringType.getRawClass());
        JavaType listType = resolver.typeFromId(null, "java.util.ArrayList");
        assertEquals(ArrayList.class, listType.getRawClass());
        JavaType intType = resolver.typeFromId(null, "java.lang.Integer");
        assertEquals(Integer.class, intType.getRawClass());

        // Test with enums
        JavaType enumType = resolver.typeFromId(null, "java.lang.Enum");
        assertEquals(Enum.class, enumType.getRawClass());
        JavaType enumConstantType = resolver.typeFromId(null, "java.lang.Enum$EnumConstantType");
        assertEquals(Enum.class.getEnumConstants()[0].getClass(), enumConstantType.getRawClass());

        // Test with collections
        JavaType enumSetType = resolver.typeFromId(null, "java.util.EnumSet");
        assertEquals(EnumSet.class, enumSetType.getRawClass());
        JavaType enumMapType = resolver.typeFromId(null, "java.util.EnumMap");
        assertEquals(EnumMap.class, enumMapType.getRawClass());

        // Test with inner classes
        JavaType innerClassType = resolver.typeFromId(null, "java.lang.String$CaseInsensitiveComparator");
        assertEquals(InnerClass.class, innerClassType.getRawClass());
        InnerClass innerClassInstance = new InnerClass();
        JavaType innerClassInstanceType = resolver.typeFromId(null, innerClassInstance.getClass().getName());
        assertEquals(InnerClass.class, innerClassInstanceType.getRawClass());

        // Test with custom classes
        JavaType customClassType = resolver.typeFromId(null, "com.example.CustomClass");
        assertEquals(CustomClass.class, customClassType.getRawClass());
    }

    @Test
    public void testIdFromValueWithInvalidClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        try {
            resolver.idFromValue("InvalidClass", Object.class);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id 'InvalidClass' (for id type 'Id.class'): Invalid class [Ljava.lang.String;", e.getMessage());
        }
    }

    @Test
    public void testIdFromValueWithUnknownClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        try {
            resolver.idFromValueAndType("UnknownClass", null);
            fail();
        } catch (IOException e) {
            assertEquals("no such class found", e.getMessage());
        }
    }

    private static class InnerClass {
        // Empty inner class for testing purposes
    }

    private static class CustomClass {
        // Empty custom class for testing purposes
    }
}