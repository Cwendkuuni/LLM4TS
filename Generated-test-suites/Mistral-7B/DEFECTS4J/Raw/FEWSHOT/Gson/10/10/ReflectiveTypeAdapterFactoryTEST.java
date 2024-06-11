package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingStrategy;
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

public final class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testCreateTypeAdapter() {
        Gson gson = new Gson();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingStrategy() {
                    @Override public String translateName(Field field) {
                        return field.getName().toLowerCase().replace("_", "");
                    }
                },
                new Excluder() {
                    @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override public boolean excludeField(Field field, boolean serialize) {
                        return false;
                    }
                });

        TypeAdapter<?> adapter = factory.create(gson, TypeToken.get(String.class));
        assertNotNull(adapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTypeAdapterWithInterface() {
        Gson gson = new Gson();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingStrategy() {
                    @Override public String translateName(Field field) {
                        return field.getName().toLowerCase().replace("_", "");
                    }
                },
                new Excluder() {
                    @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override public boolean excludeField(Field field, boolean serialize) {
                        return false;
                    }
                });

        TypeAdapter<?> adapter = factory.create(gson, TypeToken.get(Object.class));
    }

    @Test
    public void testGetBoundFields() {
        Gson gson = new Gson();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingStrategy() {
                    @Override public String translateName(Field field) {
                        return field.getName().toLowerCase().replace("_", "");
                    }
                },
                new Excluder() {
                    @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override public boolean excludeField(Field field, boolean serialize) {
                        return false;
                    }
                });

        Class<?> testClass = TestClass.class;
        TypeToken<TestClass> type = TypeToken.get(testClass);
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, type, testClass);
        assertNotNull(boundFields);
        assertEquals(4, boundFields.size());

        for (ReflectiveTypeAdapterFactory.BoundField boundField : boundFields.values()) {
            assertNotNull(boundField.name);
            assertNotNull(boundField.serialize);
            assertNotNull(boundField.deserialize);
        }
    }

    @Test
    public void testReadAndWrite() throws IOException {
        Gson gson = new Gson();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingStrategy() {
                    @Override public String translateName(Field field) {
                        return field.getName().toLowerCase().replace("_", "");
                    }
                },
                new Excluder() {
                    @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override public boolean excludeField(Field field, boolean serialize) {
                        return false;
                    }
                });

        TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));

        TestClass testObject = new TestClass();
        testObject.setName("testName");
        testObject.setNumber(123);
        testObject.setBoolean(true);
        testObject.setNull(null);

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("name", JsonElement.newString("testName"));
        jsonObject.add("number", JsonElement.numberValue(123));
        jsonObject.add("boolean", JsonElement.booleanValue(true));
        jsonObject.addProperty("null", null);

        TestClass testObject2 = adapter.read(new JsonReader(new StringReader(jsonObject.toString())));

        assertEquals("testName", testObject2.getName());
        assertEquals(123, testObject2.getNumber());
        assertEquals(true, testObject2.isBoolean());
        assertNull(testObject2.getNull());
    }

    @Test(expected = JsonSyntaxException.class)
    public void testReadWithNullName() throws IOException {
        Gson gson = new Gson();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingStrategy() {
                    @Override public String translateName(Field field) {
                        return field.getName().toLowerCase().replace("_", "");
                    }
                },
                new Excluder() {
                    @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override public boolean excludeField(Field field, boolean serialize) {
                        return false;
                    }
                });

        TypeAdapter<TestClass> adapter = factory.create(gson, TypeToken.get(TestClass.class));

        TestClass testObject = new TestClass();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", null);

        adapter.read(new JsonReader(new StringReader(jsonObject.toString())));
    }
}

class TestClass {
    private String name;
    private int number;
    private boolean boolean_;
    private Object null_;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isBoolean() {
        return boolean_;
    }

    public void setBoolean(boolean boolean_) {
        this.boolean_ = boolean_;
    }

    public Object getNull() {
        return null_;
    }

    public void setNull(Object null_) {
        this.null_ = null_;
    }
}