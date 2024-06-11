package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testExcludeField() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, true);
        assertEquals(false, result);
    }

    @Test
    public void testGetFieldNames() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, fieldNames.size());
        assertEquals("constructorConstructor", fieldNames.get(0));
    }

    @Test
    public void testCreate() {
        Gson gson = new Gson();
        TypeToken<String> type = TypeToken.get(String.class);
        TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(),
                FieldNamingStrategy.IDENTITY, new Excluder());
        TypeAdapter<String> adapter = factory.create(gson, type);
        assertNull(adapter);
    }

    @Test
    public void testCreateBoundField() throws NoSuchFieldException {
        Gson gson = new Gson();
        Field field = ReflectiveTypeAdapterFactory.Adapter.class.getDeclaredField("constructor");
        TypeToken<?> fieldType = TypeToken.get(String.class);
        ReflectiveTypeAdapterFactory.BoundField boundField = ReflectiveTypeAdapterFactory.createBoundField(gson,
                field, "constructor", fieldType, true, true);
        assertEquals("constructor", boundField.name);
        assertEquals(true, boundField.serialized);
        assertEquals(true, boundField.deserialized);
    }

    @Test
    public void testGetBoundFields() {
        Gson gson = new Gson();
        TypeToken<?> type = TypeToken.get(String.class);
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(),
                FieldNamingStrategy.IDENTITY, new Excluder());
        Class<?> raw = type.getRawType();
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, type, raw);
        assertEquals(0, boundFields.size());
    }

    @Test
    public void testAdapterRead() throws IOException {
        Gson gson = new Gson();
        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(() -> "instance",
                new LinkedHashMap<>());
        JsonReader jsonReader = new JsonReader(null);
        String result = adapter.read(jsonReader);
        assertSame("instance", result);
    }

    @Test
    public void testAdapterWrite() throws IOException {
        Gson gson = new Gson();
        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(() -> "instance",
                new LinkedHashMap<>());
        JsonWriter jsonWriter = new JsonWriter(null);
        adapter.write(jsonWriter, "instance");
    }
}