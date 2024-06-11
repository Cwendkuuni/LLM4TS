package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);

        // Test with EnumSet
        EnumSet<MyEnum> enumSet = EnumSet.allOf(MyEnum.class);
        String expectedEnumSetId = TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, MyEnum.class).toCanonical();
        assertEquals(expectedEnumSetId, resolver.idFromValue(enumSet));

        // Test with EnumMap
        EnumMap<MyEnum, String> enumMap = new EnumMap<>(MyEnum.class);
        enumMap.put(MyEnum.KEY1, "value1");
        String expectedEnumMapId = TypeFactory.defaultInstance().constructMapType(EnumMap.class, MyEnum.class, String.class).toCanonical();
        assertEquals(expectedEnumMapId, resolver.idFromValue(enumMap));

        // Test with other classes
        MyClass myClass = new MyClass();
        assertEquals(myClass.getClass().getName(), resolver.idFromValue(myClass));
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);

        // Test with EnumSet and Enum class
        EnumSet<MyEnum> enumSet = EnumSet.allOf(MyEnum.class);
        String expectedEnumSetId = TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, MyEnum.class).toCanonical();
        assertEquals(expectedEnumSetId, resolver.idFromValueAndType(enumSet, EnumSet.class));

        // Test with EnumMap and Enum class
        EnumMap<MyEnum, String> enumMap = new EnumMap<>(MyEnum.class);
        enumMap.put(MyEnum.KEY1, "value1");
        String expectedEnumMapId = TypeFactory.defaultInstance().constructMapType(EnumMap.class, MyEnum.class, String.class).toCanonical();
        assertEquals(expectedEnumMapId, resolver.idFromValueAndType(enumMap, EnumMap.class));

        // Test with other classes
        MyClass myClass = new MyClass();
        assertEquals(myClass.getClass().getName(), resolver.idFromValueAndType(myClass, null));
    }

    @Test
    public void testTypeFromId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);

        // Test with EnumSet
        String enumSetId = TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, MyEnum.class).toCanonical();
        JavaType enumSetType = resolver.typeFromId(null, enumSetId);
        assertEquals(enumSetId, enumSetType.toCanonical()); // TypeFactory always uses canonical representation

        // Test with EnumMap
        String enumMapId = TypeFactory.defaultInstance().constructMapType(EnumMap.class, MyEnum.class, String.class).toCanonical();
        JavaType enumMapType = resolver.typeFromId(null, enumMapId);
        assertEquals(enumMapId, enumMapType.toCanonical()); // TypeFactory always uses canonical representation

        // Test with other classes
        String myClassId = MyClass.class.getName();
        JavaType myClassType = resolver.typeFromId(null, myClassId);
        assertEquals(myClassId, myClassType.toCanonical()); // TypeFactory always uses canonical representation

        // Test with unknown id
        String unknownId = "com.example.UnknownClass";
        assertNull(resolver.typeFromId(null, unknownId));
    }

    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);

        // Test with EnumSet
        EnumSet<MyEnum> enumSet = EnumSet.allOf(MyEnum.class);
        String expectedEnumSetId = TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, MyEnum.class).toCanonical();
        assertEquals(expectedEnumSetId, resolver._idFrom(enumSet, EnumSet.class, TypeFactory.defaultInstance()));

        // Test with EnumMap
        EnumMap<MyEnum, String> enumMap = new EnumMap<>(MyEnum.class);
        enumMap.put(MyEnum.KEY1, "value1");
        String expectedEnumMapId = TypeFactory.defaultInstance().constructMapType(EnumMap.class, MyEnum.class, String.class).toCanonical();
        assertEquals(expectedEnumMapId, resolver._idFrom(enumMap, EnumMap.class, TypeFactory.defaultInstance()));

        // Test with other classes
        MyClass myClass = new MyClass();
        assertEquals(myClass.getClass().getName(), resolver._idFrom(myClass, null, TypeFactory.defaultInstance()));
    }
}