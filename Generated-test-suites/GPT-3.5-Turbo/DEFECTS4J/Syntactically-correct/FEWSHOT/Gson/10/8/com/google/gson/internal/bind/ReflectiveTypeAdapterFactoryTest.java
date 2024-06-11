package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonReader;
import com.google.gson.JsonToken;
import com.google.gson.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ExcluderTest;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
    private Gson gson;
    private TypeToken typeToken;

    @Before
    public void setup() {
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        FieldNamingStrategy fieldNamingPolicy = mock(FieldNamingStrategy.class);
        Excluder excluder = mock(Excluder.class);
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
        gson = mock(Gson.class);
        typeToken = mock(TypeToken.class);
    }

    @Test
    public void testExcludeField() throws NoSuchFieldException {
        Field field = UtilTest.class.getDeclaredField("testStripLeadingAndTrailingQuotes");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertFalse(result);
    }

    @Test
    public void testExcludeField_nullField() {
        boolean result = reflectiveTypeAdapterFactory.excludeField(null, true);
        assertFalse(result);
    }

    @Test
    public void testExcludeField_excludeClass() throws NoSuchFieldException {
        Field field = UtilTest.class.getDeclaredField("testStripLeadingAndTrailingQuotes");
        Excluder excluder = mock(Excluder.class);
        when(excluder.excludeClass(field.getType(), true)).thenReturn(true);
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, excluder);

        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertTrue(result);
    }

    @Test
    public void testExcludeField_excludeField() throws NoSuchFieldException {
        Field field = UtilTest.class.getDeclaredField("testStripLeadingAndTrailingQuotes");
        Excluder excluder = mock(Excluder.class);
        when(excluder.excludeField(field, true)).thenReturn(true);
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, excluder);

        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertTrue(result);
    }

    @Test
    public void testExcludeField_excludeClassAndField() throws NoSuchFieldException {
        Field field = UtilTest.class.getDeclaredField("testStripLeadingAndTrailingQuotes");
        Excluder excluder = mock(Excluder.class);
        when(excluder.excludeClass(field.getType(), true)).thenReturn(true);
        when(excluder.excludeField(field, true)).thenReturn(true);
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, excluder);

        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertTrue(result);
    }

    @Test
    public void testGetFieldNames_noAnnotation() throws NoSuchFieldException {
        Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        List<String> expected = Collections.singletonList("EMPTY_STRING_ARRAY");

        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(expected, fieldNames);
    }

    @Test
    public void testGetFieldNames_annotationWithValue() throws NoSuchFieldException {
        Field field = Util.class.getDeclaredField("stripLeadingAndTrailingQuotes");
        List<String> expected = Collections.singletonList("stripLeadingAndTrailingQuotes");

        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(expected, fieldNames);
    }

    @Test
    public void testGetFieldNames_annotationWithAlternate() throws NoSuchFieldException {
        Field field = Util.class.getDeclaredField("stripLeadingHyphens");
        List<String> expected = new ArrayList<>();
        expected.add("stripLeadingHyphens");
        expected.add("stripLeadingHyphensAlternate");

        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(expected, fieldNames);
    }

    @Test
    public void testGetBoundFields_interfaceRawType() {
        TypeToken type = TypeToken.get(ArrayList.class);
        Map<String, BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(gson, type, List.class);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetBoundFields_noFields() {
        TypeToken type = TypeToken.get(String.class);
        Map<String, BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(gson, type, String.class);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetBoundFields_oneField() throws NoSuchFieldException {
        TypeToken type = TypeToken.get(Util.class);
        Class<?> raw = Util.class;
        Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        field.setAccessible(true);
        Type fieldType = $Gson$Types.resolve(type.getType(), raw, field.getGenericType());
        TypeToken fieldTypeToken = TypeToken.get(fieldType);

        Map<String, BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(gson, type, raw);
        assertNotNull(result);
        assertEquals(1, result.size());

        BoundField boundField = result.get("EMPTY_STRING_ARRAY");
        assertNotNull(boundField);
        assertEquals("EMPTY_STRING_ARRAY", boundField.name);
        assertFalse(boundField.serialized);
        assertFalse(boundField.deserialized);
    }

    @Test
    public void testGetBoundFields_multipleFields() throws NoSuchFieldException {
        TypeToken type = TypeToken.get(Util.class);
        Class<?> raw = Util.class;
        Field field1 = Util.class.getDeclaredField("stripLeadingAndTrailingQuotes");
        Field field2 = Util.class.getDeclaredField("stripLeadingHyphens");
        field1.setAccessible(true);
        field2.setAccessible(true);
        Type fieldType1 = $Gson$Types.resolve(type.getType(), raw, field1.getGenericType());
        Type fieldType2 = $Gson$Types.resolve(type.getType(), raw, field2.getGenericType());
        TypeToken fieldTypeToken1 = TypeToken.get(fieldType1);
        TypeToken fieldTypeToken2 = TypeToken.get(fieldType2);

        Map<String, BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(gson, type, raw);
        assertNotNull(result);
        assertEquals(2, result.size());

        BoundField boundField1 = result.get("stripLeadingAndTrailingQuotes");
        assertNotNull(boundField1);
        assertEquals("stripLeadingAndTrailingQuotes", boundField1.name);
        assertTrue(boundField1.serialized);
        assertTrue(boundField1.deserialized);

        BoundField boundField2 = result.get("stripLeadingHyphens");
        assertNotNull(boundField2);
        assertEquals("stripLeadingHyphens", boundField2.name);
        assertTrue(boundField2.serialized);
        assertTrue(boundField2.deserialized);
    }

    @Test
    public void testCreate_primitiveRawType() {
        TypeToken type = TypeToken.get(int.class);
        TypeAdapter<?> result = reflectiveTypeAdapterFactory.create(gson, type);
        assertNull(result);
    }

    @Test
    public void testCreate_notAssignableFromToObject() {
        TypeToken type = TypeToken.get(ExcluderTest.class);
        TypeAdapter<?> result = reflectiveTypeAdapterFactory.create(gson, type);
        assertNull(result);
    }

    //Please note that testing the private Adapter class is not possible here as it is not accessible.

}