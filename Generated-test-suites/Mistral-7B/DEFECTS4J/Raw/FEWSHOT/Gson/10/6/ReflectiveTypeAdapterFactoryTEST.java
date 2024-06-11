package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testCreate() {
        FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
            @Override
            public String translateName(Field field) {
                return field.getName();
            }
        };

        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeField(Field field, boolean serialize) {
                return false;
            }
        };

        ConstructorConstructor constructorConstructor = new ConstructorConstructor();

        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                constructorConstructor, fieldNamingPolicy, excluder);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        TypeAdapterFactory typeAdapterFactory = reflectiveTypeAdapterFactory;

        TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};
        TypeAdapter<MyClass> adapter = gson.getAdapter(typeAdapterFactory, typeToken);
        assertNotNull(adapter);
    }

    @Test
    public void testExcludeField() {
        FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
            @Override
            public String translateName(Field field) {
                return field.getName();
            }
        };

        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeField(Field field, boolean serialize) {
                if (field.getName().equals("fieldToExclude")) {
                    return serialize;
                }
                return false;
            }
        };

        ConstructorConstructor constructorConstructor = new ConstructorConstructor();

        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                constructorConstructor, fieldNamingPolicy, excluder);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        TypeAdapterFactory typeAdapterFactory = reflectiveTypeAdapterFactory;

        TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};
        TypeAdapter<MyClass> adapter = gson.getAdapter(typeAdapterFactory, typeToken);

        MyClass myClass = new MyClass();
        myClass.fieldToExclude = "excluded value";

        JsonElement jsonElement = adapter.toJson(myClass);
        assertNull(jsonElement.getAsJsonObject().get("fieldToExclude"));
    }

    @Test(expected = JsonSyntaxException.class)
    public void testJsonAdapter() {
        FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
            @Override
            public String translateName(Field field) {
                return field.getName();
            }
        };

        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeField(Field field, boolean serialize) {
                return false;
            }
        };

        ConstructorConstructor constructorConstructor = new ConstructorConstructor();

        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                constructorConstructor, fieldNamingPolicy, excluder);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        TypeAdapterFactory typeAdapterFactory = reflectiveTypeAdapterFactory;

        TypeToken<MyJsonAdapterClass> typeToken = new TypeToken<MyJsonAdapterClass>() {};

        JsonAdapter<MyJsonAdapterClass> jsonAdapter = getTypeAdapter(
                constructorConstructor, gson, typeToken.getType(),
                MyJsonAdapterClass.class);

        MyJsonAdapterClass myJsonAdapterClass = new MyJsonAdapterClass();

        JsonObject jsonObject = jsonAdapter.toJsonTree(myJsonAdapterClass).getAsJsonObject();
        jsonObject.addProperty("test", "value");

        MyJsonAdapterClass deserializedMyJsonAdapterClass = jsonAdapter.fromJsonTree(jsonObject);
        assertEquals("value", deserializedMyJsonAdapterClass.test);

        TypeToken<MyClassWithJsonAdapterField> typeTokenMyClassWithJsonAdapterField = new TypeToken<MyClassWithJsonAdapterField>() {};
        TypeAdapter<MyClassWithJsonAdapterField> typeAdapterMyClassWithJsonAdapterField = gson.getAdapter(typeAdapterFactory, typeTokenMyClassWithJsonAdapterField);

        MyClassWithJsonAdapterField myClassWithJsonAdapterField = new MyClassWithJsonAdapterField();
        myClassWithJsonAdapterField.jsonAdapterField = new MyJsonAdapterClass();

        JsonElement jsonElement = typeAdapterMyClassWithJsonAdapterField.toJson(myClassWithJsonAdapterField);
        assertNotNull(jsonElement);

        MyClassWithJsonAdapterField deserializedMyClassWithJsonAdapterField = typeAdapterMyClassWithJsonAdapterField.fromJson(jsonElement);
        assertEquals(jsonAdapterClass, deserializedMyClassWithJsonAdapterField.jsonAdapterField);
    }

    private static JsonAdapter<MyJsonAdapterClass> getTypeAdapter(
            ConstructorConstructor constructorConstructor, Gson gson, Type type,
            Class<? extends JsonAdapter<?>> jsonAdapterClass) {
        return gson.getDelegateAdapterFactory().getAdapter(jsonAdapterClass, type);
    }

    @SuppressWarnings("unused")
    static class MyClass {
        private int field;

        public int getField() {
            return field;
        }

        public void setField(int field) {
            this.field = field;
        }
    }

    @SuppressWarnings("unused")
    static class MyJsonAdapterClass {
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

    @SuppressWarnings("unused")
    static class MyClassWithJsonAdapterField {
        private MyJsonAdapterClass jsonAdapterField;

        public MyJsonAdapterClass getJsonAdapterField() {
            return jsonAdapterField;
        }

        public void setJsonAdapterField(MyJsonAdapterClass jsonAdapterField) {
            this.jsonAdapterField = jsonAdapterField;
        }
    }
}