package com.google.gson;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TypeInfoFactoryTest {
    @Test
    public void testGetTypeInfoForArray() {
        Type type = String[].class;
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals(type, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() {
        Type typeDefiningF = TestObject.class;
        Field f = getField(TestObject.class, "list");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
        ParameterizedType expectedType = (ParameterizedType) TestObject.class.getDeclaredField("list").getGenericType();
        assertEquals(expectedType.getActualTypeArguments()[0], typeInfo.getType());
    }

    private Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class TestObject {
        private List<String> list;
        private Map<String, Integer> map;
    }
}