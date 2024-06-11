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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {
    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingStrategy fieldNamingPolicy = new FieldNamingStrategy();
        Excluder excluder = new Excluder();
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    }

    @Test
    public void testExcludeField() {
        Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertEquals(false, result);
    }

    @Test
    public void testGetFieldNames() throws NoSuchFieldException {
        Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        List<String> result = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expected = new ArrayList<>();
        expected.add("EMPTY_STRING_ARRAY");
        assertEquals(expected, result);
    }

    @Test
    public void testCreate() {
        Gson gson = new Gson();
        TypeToken<String> type = TypeToken.get(String.class);
        TypeAdapter<String> result = reflectiveTypeAdapterFactory.create(gson, type);
        assertNull(result);
    }

    @Test
    public void testCreateBoundField() throws NoSuchFieldException {
        Gson gson = new Gson();
        Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        String name = "EMPTY_STRING_ARRAY";
        TypeToken<?> fieldType = TypeToken.get(String[].class);
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);
        assertEquals(name, result.name);
        assertEquals(serialize, result.serialized);
        assertEquals(deserialize, result.deserialized);
    }

    @Test
    public void testGetBoundFields() {
        Gson gson = new Gson();
        TypeToken<?> type = TypeToken.get(ReflectiveTypeAdapterFactory.class);
        Class<?> raw = ReflectiveTypeAdapterFactory.class;
        Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(gson, type, raw);
        Map<String, ReflectiveTypeAdapterFactory.BoundField> expected = new LinkedHashMap<>();
        assertEquals(expected, result);
    }

    @Test
    public void testBoundFieldWriteField() throws NoSuchFieldException, IllegalAccessException, IOException {
        Field field = ReflectiveTypeAdapterFactory.BoundField.class.getDeclaredField("name");
        field.setAccessible(true);
        ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField("test", true, true) {
            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
                return true;
            }

            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
            }
        };

        boolean result = boundField.writeField(null);
        assertEquals(true, result);
    }

    @Test
    public void testAdapterRead() throws IOException {
        JsonReader in = new JsonReader(null);
        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<String>(null, null);
        String result = adapter.read(in);
        assertNull(result);
    }

    @Test
    public void testAdapterWrite() throws IOException {
        JsonWriter out = new JsonWriter(null);
        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<String>(null, null);
        adapter.write(out, null);
    }
}