package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testCreate_NullInput_ReturnsNull() {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
        assertNull(factory.create(gson, null));
    }

    @Test
    public void testCreate_PrimitiveType_ReturnsNull() {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
        assertNull(factory.create(gson, new TypeToken<Integer>() {}.getType()));
    }

    @Test
    public void testCreate_ClassWithNoFields_ReturnsNull() {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
        assertNull(factory.create(gson, new TypeToken<Object>() {}.getType()));
    }

    @Test
    public void testCreate_ClassWithField_ReturnsAdapterInstance() {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

        class TestObject {
            @SerializedName("name")
            String name;

            @JsonAdapter(MyJsonAdapter.class)
            int id;
        }

        TypeAdapter<TestObject> adapter = factory.create(gson, new TypeToken<TestObject>() {}.getType());
        assertNotNull(adapter);
    }

    @Test
    public void testGetBoundFields_SimpleClass_ReturnsMapWithSingleEntry() {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

        class TestObject {
            String name;
            int id;
        }

        Map<String, ReflectiveTypeAdapterFactory.BoundField> fields = factory.getBoundFields(gson, new TypeToken<TestObject>() {}.getType(), TestObject.class);
        assertEquals(2, fields.size());

        for (Map.Entry<String, ReflectiveTypeAdapterFactory.BoundField> entry : fields.entrySet()) {
            String name = entry.getKey();
            ReflectiveTypeAdapterFactory.BoundField boundField = entry.getValue();
            assertEquals(name, boundField.name);
            assertEquals(true, boundField.serialized);
            assertEquals(true, boundField.deserialized);
        }
    }

    @Test
    public void testGetBoundFields_ClassWithJsonAdapter_ReturnsMapWithSingleEntry() {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

        class TestObject {
            @SerializedName("name")
            String name;

            @JsonAdapter(MyJsonAdapter.class)
            int id;
        }

        Map<String, ReflectiveTypeAdapterFactory.BoundField> fields = factory.getBoundFields(gson, new TypeToken<TestObject>() {}.getType(), TestObject.class);
        assertEquals(2, fields.size());

        for (Map.Entry<String, ReflectiveTypeAdapterFactory.BoundField> entry : fields.entrySet()) {
            String name = entry.getKey();
            ReflectiveTypeAdapterFactory.BoundField boundField = entry.getValue();
            if (name.equals("id")) {
                assertEquals(MyJsonAdapter.class, boundField.typeAdapter.getClass());
            } else {
                assertEquals(String.class, boundField.typeAdapter.getClass());
            }
            assertEquals(true, boundField.serialized);
            assertEquals(true, boundField.deserialized);
        }
    }

    @Test
    public void testGetBoundFields_ClassWithExcludedField_ReturnsMapWithoutExcludedEntry() {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

        class TestObject {
            @Exclude
            String excludedField;
            String name;
            int id;
        }

        Excluder excluder = new Excluder() {
            @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return clazz.isAssignableFrom(TestObject.class);
            }

            @Override public boolean excludeField(Field field, boolean serialize) {
                return field.getName().equals("excludedField");
            }
        };

        Map<String, ReflectiveTypeAdapterFactory.BoundField> fields = factory.getBoundFields(gson, new TypeToken<TestObject>() {}.getType(), TestObject.class, excluder);
        assertEquals(2, fields.size());

        for (Map.Entry<String, ReflectiveTypeAdapterFactory.BoundField> entry : fields.entrySet()) {
            String name = entry.getKey();
            ReflectiveTypeAdapterFactory.BoundField boundField = entry.getValue();
            if (!name.equals("excludedField")) {
                assertEquals(true, boundField.serialized);
                assertEquals(true, boundField.deserialized);
            }
        }
    }

    @Test
    public void testGetBoundFields_ClassWithNestedExcludedField_ReturnsMapWithoutExcludedEntry() {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

        class TestObject {
            class InnerClass {
                @Exclude
                String excludedField;
                String name;
            }
            InnerClass innerClass;
            String name;
            int id;
        }

        Excluder excluder = new Excluder() {
            @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return clazz.isAssignableFrom(TestObject.class);
            }

            @Override public boolean excludeField(Field field, boolean serialize) {
                return field.getName().equals("excludedField") || field.getType().isAssignableFrom(InnerClass.class);
            }
        };

        Map<String, ReflectiveTypeAdapterFactory.BoundField> fields = factory.getBoundFields(gson, new TypeToken<TestObject>() {}.getType(), TestObject.class, excluder);
        assertEquals(3, fields.size());

        for (Map.Entry<String, ReflectiveTypeAdapterFactory.BoundField> entry : fields.entrySet()) {
            String name = entry.getKey();
            ReflectiveTypeAdapterFactory.BoundField boundField = entry.getValue();
            if (!name.equals("excludedField")) {
                assertEquals(true, boundField.serialized);
                assertEquals(true, boundField.deserialized);
            }
        }
    }

    @Test
    public void testWriteRead_SimpleClass() throws IOException {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

        class TestObject {
            String name;
            int id;
        }

        TestObject testObject = new TestObject();
        testObject.name = "John Doe";
        testObject.id = 42;

        String json = gson.toJson(testObject);
        System.out.println(json);

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        TestObject deserializedObject = gson.fromJson(jsonReader, TestObject.class);

        assertEquals("John Doe", deserializedObject.name);
        assertEquals(42, deserializedObject.id);
    }

    @Test
    public void testWriteRead_ClassWithJsonAdapter() throws IOException {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

        class TestObject {
            @SerializedName("name")
            String name;

            @JsonAdapter(MyJsonAdapter.class)
            int id;
        }

        TestObject testObject = new TestObject();
        testObject.name = "John Doe";
        testObject.id = 42;

        String json = gson.toJson(testObject);
        System.out.println(json);

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        TestObject deserializedObject = gson.fromJson(jsonReader, TestObject.class);

        assertEquals("John Doe", deserializedObject.name);
        assertEquals(42, deserializedObject.id);
    }

    @Test
    public void testWriteRead_ClassWithExcludedField() throws IOException {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

        class TestObject {
            @Exclude
            String excludedField;
            String name;
            int id;
        }

        TestObject testObject = new TestObject();
        testObject.name = "John Doe";
        testObject.id = 42;

        String json = gson.toJson(testObject, new TypeToken<TestObject>() {}.getType());
        System.out.println(json);

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        TestObject deserializedObject = gson.fromJson(jsonReader, TestObject.class);

        assertEquals("John Doe", deserializedObject.name);
        assertEquals(42, deserializedObject.id);
        assertNull(deserializedObject.excludedField);
    }

    @Test
    public void testWriteRead_ClassWithNestedExcludedField() throws IOException {
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

        class TestObject {
            class InnerClass {
                @Exclude
                String excludedField;
                String name;
            }

            InnerClass innerClass;
            String name;
            int id;
        }

        TestObject testObject = new TestObject();
        testObject.name = "John Doe";
        testObject.id = 42;
        testObject.innerClass = new TestObject.InnerClass();
        testObject.innerClass.name = "Inner John Doe";

        String json = gson.toJson(testObject, new TypeToken<TestObject>() {}.getType());
        System.out.println(json);

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        TestObject deserializedObject = gson.fromJson(jsonReader, TestObject.class);

        assertEquals("John Doe", deserializedObject.name);
        assertEquals(42, deserializedObject.id);
        assertNull(deserializedObject.innerClass.excludedField);
        assertEquals("Inner John Doe", deserializedObject.innerClass.name);
    }

    static class MyJsonAdapter implements JsonAdapter<Integer> {
        @Override public Integer read(JsonReader in) throws IOException {
            return in.nextInt();
        }

        @Override public void write(JsonWriter out, Integer value) throws IOException {
            out.value(value);
        }
    }
}

class FieldNamingPolicy {
    String translateName(Field field) {
        return field.getName();
    }
}

class Excluder {
    boolean excludeClass(Class<?> clazz, boolean serialize) {
        return false;
    }

    boolean excludeField(Field field, boolean serialize) {
        return false;
    }
}