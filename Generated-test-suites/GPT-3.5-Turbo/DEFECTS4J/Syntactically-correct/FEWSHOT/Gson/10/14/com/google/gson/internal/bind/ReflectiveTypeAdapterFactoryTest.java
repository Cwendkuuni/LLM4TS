package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;

public class ReflectiveTypeAdapterFactoryTest {
    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
    private ConstructorConstructor constructorConstructor;
    private FieldNamingStrategy fieldNamingStrategy;
    private Excluder excluder;

    @Before
    public void setUp() {
        constructorConstructor = new ConstructorConstructor();
        fieldNamingStrategy = new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return f.getName();
            }
        };
        excluder = new Excluder();
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    }

    @Test
    public void testCreatePrimitiveTypeAdapter() {
        Gson gson = new Gson();
        TypeToken<Integer> type = TypeToken.get(Integer.class);
        TypeAdapter<Integer> adapter = reflectiveTypeAdapterFactory.create(gson, type);
        assertNull(adapter);
    }

    @Test
    public void testCreateObjectTypeAdapter() {
        Gson gson = new Gson();
        TypeToken<Object> type = TypeToken.get(Object.class);
        TypeAdapter<Object> adapter = reflectiveTypeAdapterFactory.create(gson, type);
        assertNull(adapter);
    }

    @Test
    public void testCreateTypeAdapterWithFields() {
        Gson gson = new Gson();
        TypeToken<DummyClass> type = TypeToken.get(DummyClass.class);
        TypeAdapter<DummyClass> adapter = reflectiveTypeAdapterFactory.create(gson, type);
        assertNotNull(adapter);
    }

    @Test
    public void testCreateTypeAdapterWithInterface() {
        Gson gson = new Gson();
        TypeToken<InterfaceClass> type = TypeToken.get(InterfaceClass.class);
        TypeAdapter<InterfaceClass> adapter = reflectiveTypeAdapterFactory.create(gson, type);
        assertNotNull(adapter);
    }

    @Test
    public void testExcludeField() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("excludedField");
        boolean serialize = true;
        boolean deserialize = true;
        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, serialize, excluder);
        assertFalse(result);
    }

    @Test
    public void testExcludeFieldWithExcludedType() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("excludedField");
        boolean serialize = true;
        boolean deserialize = false;
        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, serialize, excluder);
        assertTrue(result);
    }

    @Test
    public void testGetFieldNamesWithSerializedName() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("fieldWithSerializedName");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expected = Collections.singletonList("custom_name");
        assertEquals(expected, fieldNames);
    }

    @Test
    public void testGetFieldNamesWithoutSerializedName() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("fieldWithoutSerializedName");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expected = Collections.singletonList("fieldWithoutSerializedName");
        assertEquals(expected, fieldNames);
    }

    private static class DummyClass {
        @SerializedName("custom_name")
        private String fieldWithSerializedName;
        private String fieldWithoutSerializedName;
        @JsonAdapter(DummyClassAdapter.class)
        private String fieldWithJsonAdapter;
        private String excludedField;

        private static class DummyClassAdapter extends TypeAdapter<String> {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                out.value(value);
            }

            @Override
            public String read(JsonReader in) throws IOException {
                return in.nextString();
            }
        }
    }

    private interface InterfaceClass {
        void method();
    }
}