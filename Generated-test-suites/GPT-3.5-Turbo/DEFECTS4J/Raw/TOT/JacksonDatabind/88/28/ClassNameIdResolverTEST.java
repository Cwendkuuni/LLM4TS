
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver resolver;

    @Before
    public void setup() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        resolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testRegisterSubtype_ShouldNotDoAnything() {
        // Given
        Class<?> type = String.class;
        String name = "String";

        // When
        resolver.registerSubtype(type, name);

        // Then
        // No verification needed, registerSubtype() does nothing in this implementation
    }

    @Test
    public void testGetMechanism_ShouldReturnClass() {
        // Given

        // When
        JsonTypeInfo.Id mechanism = resolver.getMechanism();

        // Then
        assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
    }

    @Test
    public void testIdFromValue_ShouldReturnClassName() {
        // Given
        Object value = new Object();

        // When
        String id = resolver.idFromValue(value);

        // Then
        assertEquals(value.getClass().getName(), id);
    }

    @Test
    public void testIdFromValueAndType_ShouldReturnClassName() {
        // Given
        Object value = new Object();
        Class<?> type = value.getClass();

        // When
        String id = resolver.idFromValueAndType(value, type);

        // Then
        assertEquals(value.getClass().getName(), id);
    }

    @Test
    public void testTypeFromId_WhenValidId_ShouldReturnJavaType() throws Exception {
        // Given
        DatabindContext context = null; // Mocked context
        String id = Object.class.getName();

        // When
        JavaType javaType = resolver.typeFromId(context, id);

        // Then
        assertEquals(TypeFactory.defaultInstance().constructType(Object.class), javaType);
    }

    @Test
    public void testTypeFromId_WhenInvalidId_ShouldReturnNull() throws Exception {
        // Given
        DatabindContext context = null; // Mocked context
        String id = "com.example.InvalidClass";

        // When
        JavaType javaType = resolver.typeFromId(context, id);

        // Then
        assertNull(javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromId_WhenExceptionThrown_ShouldThrowIllegalArgumentException() throws Exception {
        // Given
        DatabindContext context = null; // Mocked context
        String id = "com.example.ExceptionClass";

        // When
        resolver.typeFromId(context, id);

        // Then
        // Exception should be thrown
    }

    @Test
    public void test_idFrom_WhenEnumValue_ShouldReturnSuperclass() throws Exception {
        // Given
        Object value = TestEnum.VALUE;

        // When
        String id = resolver._idFrom(value, value.getClass(), resolver._typeFactory);

        // Then
        assertEquals(TestEnum.class.getSuperclass().getName(), id);
    }

    @Test
    public void test_idFrom_WhenEnumValueNotSubclass_ShouldReturnClassName() throws Exception {
        // Given
        Object value = TestEnum.NOT_SUBCLASS;

        // When
        String id = resolver._idFrom(value, value.getClass(), resolver._typeFactory);

        // Then
        assertEquals(value.getClass().getName(), id);
    }

    @Test
    public void test_idFrom_WhenValueIsEnumSet_ShouldReturnEnumSetType() throws Exception {
        // Given
        Object value = TestEnumSet.VALUE;

        // When
        String id = resolver._idFrom(value, value.getClass(), resolver._typeFactory);

        // Then
        assertEquals("java.util.EnumSet<" + TestEnum.class.getName() + ">", id);
    }

    @Test
    public void test_idFrom_WhenValueIsEnumMap_ShouldReturnEnumMapType() throws Exception {
        // Given
        Object value = TestEnumMap.VALUE;

        // When
        String id = resolver._idFrom(value, value.getClass(), resolver._typeFactory);

        // Then
        assertEquals("java.util.EnumMap<" + TestEnum.class.getName() + ",java.lang.Object>", id);
    }

    @Test
    public void test_idFrom_WhenValueIsArrayAsList_ShouldReturnArrayListType() throws Exception {
        // Given
        Object value = Arrays.asList();

        // When
        String id = resolver._idFrom(value, value.getClass(), resolver._typeFactory);

        // Then
        assertEquals("java.util.ArrayList", id);
    }

    private enum TestEnum {
        VALUE,
        NOT_SUBCLASS
    }

    private static class TestEnumSet {
        private static final EnumSet<TestEnum> VALUE = EnumSet.allOf(TestEnum.class);
    }

    private static class TestEnumMap {
        private static final EnumMap<TestEnum, Object> VALUE = new EnumMap<>(TestEnum.class);
    }

    @Override
    public String getDescForKnownTypeIds() {
        return "class name used as type id";
    }
}

