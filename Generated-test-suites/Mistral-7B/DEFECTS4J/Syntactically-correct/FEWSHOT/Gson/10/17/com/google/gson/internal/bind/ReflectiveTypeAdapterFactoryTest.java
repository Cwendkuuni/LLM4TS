package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.google.gson.*;
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCreate_SimpleClass() {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson);
        FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
        Excluder excluder = new Excluder();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        TypeAdapter<DummyClass> adapter = factory.create(gson, new TypeToken<DummyClass>() {});
        assertNotNull(adapter);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreate_ClassWithJsonAdapter() {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson);
        FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
        Excluder excluder = new Excluder();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        TypeAdapter<JsonAdapterTestClass> adapter = factory.create(gson, new TypeToken<JsonAdapterTestClass>() {});
        assertNotNull(adapter);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreate_ClassWithSerializedName() {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson);
        FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
        Excluder excluder = new Excluder();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        TypeAdapter<SerializedNameTestClass> adapter = factory.create(gson, new TypeToken<SerializedNameTestClass>() {});
        assertNotNull(adapter);
    }

    @Test
    public void testGetBoundFields_Interface() {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson);
        FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
        Excluder excluder = new Excluder();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, new TypeToken<InterfaceTest>() {}.where(new TypeToken<Class<?>>() {}.typeOf(InterfaceTest.class)), InterfaceTest.class);
        assertEquals(Collections.emptyMap(), boundFields);
    }

    @Test
    public void testGetBoundFields_ClassWithFields() {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson);
        FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
        Excluder excluder = new Excluder();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, new TypeToken<FieldTestClass>() {}.where(new TypeToken<Class<?>>() {}.typeOf(FieldTestClass.class)), FieldTestClass.class);

        assertNotNull(boundFields.get("name1"));
        assertNotNull(boundFields.get("name2"));
        assertEquals("name1", boundFields.get("name1").name);
        assertEquals(true, boundFields.get("name1").serialized);
        assertEquals(true, boundFields.get("name1").deserialized);
        assertEquals("name2", boundFields.get("name2").name);
        assertEquals(false, boundFields.get("name2").serialized);
        assertEquals(true, boundFields.get("name2").deserialized);
    }

    @Test
    public void testRead_ObjectWithFields() throws IOException {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson);
        FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
        Excluder excluder = new Excluder();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        String json = "{" +
                "\"name1\":\"value1\"," +
                "\"name2\":\"value2\"" +
                "}";

        TypeAdapter<FieldTestClass> adapter = factory.create(gson, new TypeToken<FieldTestClass>() {});
        FieldTestClass instance = adapter.read(new JsonReader(new StringReader(json)));

        assertEquals("value1", instance.name1);
        assertEquals("value2", instance.name2);
    }

    @Test
    public void testWrite_ObjectWithFields() throws IOException {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(gson);
        FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
        Excluder excluder = new Excluder();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        TypeAdapter<FieldTestClass> adapter = factory.create(gson, new TypeToken<FieldTestClass>() {});
        FieldTestClass instance = new FieldTestClass();
        instance.name1 = "value1";
        instance.name2 = "value2";

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(stringWriter));
        adapter.write(jsonWriter, instance);

        assertEquals("{\"name1\":\"value1\",\"name2\":\"value2\"}", stringWriter.toString());
    }
}

class DummyClass {}

class JsonAdapterTestClass {
    @JsonAdapter(JsonAdapter.class)
    int value;
}

class SerializedNameTestClass {
    @SerializedName("testName")
    int value;
}

interface InterfaceTest {}

class FieldTestClass {
    @JsonAdapter(JsonAdapter.class)
    String name1;
    String name2;
}

class JsonAdapter {
    @JsonAdapter(JsonAdapter.class)
    static class Inner {}
}

class FieldNamingPolicy implements FieldNamingStrategy {
    @Override
    public String translateName(Field field) {
        return field.getName();
    }
}