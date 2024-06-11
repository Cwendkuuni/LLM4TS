package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.EnumSet;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver idResolver;

    @Before
    public void setUp() {
        idResolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, idResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // This method should not do anything for class name based resolvers
    }

    @Test
    public void testIdFromValue() {
        Object value = "testValue";
        String id = idResolver.idFromValue(value);
        assertNotNull(id);
        assertEquals(String.class.getName(), id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = "testValue";
        Class<?> type = String.class;
        String id = idResolver.idFromValueAndType(value, type);
        assertNotNull(id);
        assertEquals(String.class.getName(), id);
    }

    @Test
    public void testTypeFromId() {
        DatabindContext context = null;
        String id = String.class.getName();
        try {
            JavaType javaType = idResolver.typeFromId(context, id);
            assertNotNull(javaType);
            assertEquals(String.class, javaType.getRawClass());
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void test_idFrom_EnumSet() {
        EnumSet<SampleEnum> enumSet = EnumSet.of(SampleEnum.VALUE1, SampleEnum.VALUE2);
        String id = idResolver._idFrom(enumSet, EnumSet.class, TypeFactory.defaultInstance());
        assertNotNull(id);
        assertEquals("java.util.EnumSet<" + SampleEnum.class.getName() + ">", id);
    }

    @Test
    public void test_idFrom_EnumMap() {
        EnumMap<SampleEnum, String> enumMap = new EnumMap<>(SampleEnum.class);
        enumMap.put(SampleEnum.VALUE1, "value1");
        String id = idResolver._idFrom(enumMap, EnumMap.class, TypeFactory.defaultInstance());
        assertNotNull(id);
        assertEquals("java.util.EnumMap<" + SampleEnum.class.getName() + ", java.lang.Object>", id);
    }

    @Test
    public void test_IdFrom_ArraysCollectionsList() {
        List<String> list = Arrays.asList("value1", "value2");
        String id = idResolver._idFrom(list, List.class, TypeFactory.defaultInstance());
        assertNotNull(id);
        assertEquals("java.util.ArrayList", id);
    }

    public enum SampleEnum {
        VALUE1, VALUE2
    }
}