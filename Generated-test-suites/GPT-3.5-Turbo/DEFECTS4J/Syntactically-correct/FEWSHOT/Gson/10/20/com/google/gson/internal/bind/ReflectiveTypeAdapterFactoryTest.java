package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.<Type, InstanceCreator<?>>emptyMap());
        FieldNamingStrategy fieldNamingStrategy = Gson::fieldNamingPolicy;
        Excluder excluder = new Excluder();
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    }

    @Test
    public void testExcludeField_withExcludedField_returnsTrue() throws NoSuchFieldException {
        Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertTrue(result);
    }

    @Test
    public void testGetFieldNames_withNoSerializedNameAnnotation_returnsFieldName() throws NoSuchFieldException {
        Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, fieldNames.size());
        assertEquals("EMPTY_STRING_ARRAY", fieldNames.get(0));
    }

    @Test
    public void testGetFieldNames_withSerializedNameAnnotation_returnsSerializedName() throws NoSuchFieldException {
        Field field = Util.class.getDeclaredField("stripLeadingAndTrailingQuotes");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, fieldNames.size());
        assertEquals("stripLeadingAndTrailingQuotes", fieldNames.get(0));
    }

    @Test
    public void testGetBoundFields_withEmptyRawType_returnsEmptyMap() {
        Gson gson = new Gson();
        TypeToken<?> typeToken = TypeToken.get(Object.class);
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, Object.class);
        assertNotNull(boundFields);
        assertTrue(boundFields.isEmpty());
    }

    @Test
    public void testCreate_withPrimitiveType_returnsNull() {
        Gson gson = new Gson();
        TypeToken<?> typeToken = TypeToken.get(int.class);
        TypeAdapter<?> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNull(typeAdapter);
    }

    @Test
    public void testCreate_withCustomType_returnsTypeAdapterInstance() {
        Gson gson = new Gson();
        TypeToken<String> typeToken = new TypeToken<String>() {};
        TypeAdapter<String> typeAdapter = (TypeAdapter<String>) reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNotNull(typeAdapter);
    }
}