package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testCreateTypeAdapter() {
        FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
            @Override
            public String translateName(Field field) {
                return field.getName().toUpperCase();
            }
        };

        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeClass(Class clazz, boolean serialize) {
                return clazz.isPrimitive();
            }

            @Override
            public boolean excludeField(Field field, boolean serialize) {
                return false;
            }
        };

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(fieldNamingPolicy)
                .setExcluder(excluder)
                .create();

        TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), fieldNamingPolicy, excluder);

        Type type = TypeToken.get(String[].class).getType();
        TypeAdapter<String[]> typeAdapter = factory.create(gson, new TypeToken<String[]>() {});
        assertNotNull(typeAdapter);
    }

    @Test
    public void testCreateTypeAdapterWithJsonAdapter() {
        FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
            @Override
            public String translateName(Field field) {
                return field.getName().toUpperCase();
            }
        };

        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeClass(Class clazz, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeField(Field field, boolean serialize) {
                return false;
            }
        };

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(fieldNamingPolicy)
                .setExcluder(excluder)
                .create();

        JsonAdapter<String> jsonAdapter = new JsonAdapter<String>() {
            @Override
            public String read(JsonElement json) throws IOException {
                return json.getAsString();
            }

            @Override
            public JsonElement write(JsonWriter out, String value) throws IOException {
                out.value(value);
                return null;
            }
        };

        TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), fieldNamingPolicy, excluder);

        Type type = TypeToken.get(String.class).getType();
        TypeAdapter<String> typeAdapter = factory.create(gson, new TypeToken<String>() {});
        assertNotNull(typeAdapter);

        JsonAdapter<String> jsonAdapterAnnotation = typeAdapter.getClass().getAnnotation(JsonAdapter.class);
        assertNull(jsonAdapterAnnotation);

        type = TypeToken.get(String.class).getType();
        TypeAdapter<String> jsonAdapterTypeAdapter = factory.create(gson, new TypeToken<String>() {
            @JsonAdapter(MyJsonAdapter.class)
            static class MyJsonAdapter extends JsonAdapter<String> {
                @Override
                public String read(JsonElement json) throws IOException {
                    return json.getAsString();
                }

                @Override
                public JsonElement write(JsonWriter out, String value) throws IOException {
                    out.value(value);
                    return null;
                }
            }
        });
        typeAdapter = getTypeAdapter(gson, type, String.class, jsonAdapterTypeAdapter);
        assertNotNull(jsonAdapterTypeAdapter);
        jsonAdapterAnnotation = typeAdapter.getClass().getAnnotation(JsonAdapter.class);
        assertNotNull(jsonAdapterAnnotation);
        assertNotNull(jsonAdapterAnnotation.value());
    }
}

class MyJsonAdapter extends JsonAdapter<String> {
    // same as the one used in the test
}