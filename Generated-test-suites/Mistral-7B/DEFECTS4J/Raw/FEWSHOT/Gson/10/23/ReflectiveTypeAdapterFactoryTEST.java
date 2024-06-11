package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingPolicy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testCreateClassWithAnnotatedFields() throws Exception {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CAMEL_CASE)
                .setExcluder(new Excluder())
                .create();

        ConstructorConstructor constructorConstructor = gson.getFactory()
                .constructorConstructor(Foo.class);

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor,
                new FieldNamingPolicy(), new Excluder());

        TypeAdapter<Foo> adapter = factory.create(gson, TypeToken.get(Foo.class));

        Foo foo = new Foo();
        foo.bar = "test";

        String json = gson.toJson(foo);
        System.out.println(json);

        JsonReader jsonReader = new JsonReader(new StringReader(json));

        Foo fooFromJson = adapter.read(jsonReader);

        assertEquals("test", fooFromJson.bar);
        assertNotNull(fooFromJson.qux);

        JsonWriter jsonWriter = new JsonWriter(new StringWriter());

        adapter.write(jsonWriter, foo);

        String json2 = jsonWriter.toString();
        System.out.println(json2);

        JsonReader jsonReader2 = new JsonReader(new StringReader(json2));

        Foo fooFromJson2 = adapter.read(jsonReader2);

        assertEquals("test", fooFromJson2.bar);
        assertNotNull(fooFromJson2.qux);
    }

    @Test
    public void testCreateClassWithAnnotatedFieldsAndJsonAdapter() throws Exception {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CAMEL_CASE)
                .setExcluder(new Excluder())
                .registerTypeAdapter(Foo.class, new FooTypeAdapter())
                .create();

        ConstructorConstructor constructorConstructor = gson.getFactory()
                .constructorConstructor(Foo.class);

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor,
                new FieldNamingPolicy(), new Excluder());

        TypeAdapter<Foo> adapter = factory.create(gson, TypeToken.get(Foo.class));

        Foo foo = new Foo();
        foo.quux = new Baz();

        String json = gson.toJson(foo);
        System.out.println(json);

        JsonReader jsonReader = new JsonReader(new StringReader(json));

        Foo fooFromJson = adapter.read(jsonReader);

        assertEquals("baz", fooFromJson.quux.toString());

        JsonWriter jsonWriter = new JsonWriter(new StringWriter());

        adapter.write(jsonWriter, foo);

        String json2 = jsonWriter.toString();
        System.out.println(json2);

        JsonReader jsonReader2 = new JsonReader(new StringReader(json2));

        Foo fooFromJson2 = adapter.read(jsonReader2);

        assertEquals("baz", fooFromJson2.quux.toString());
    }

    @Test
    public void testCreateInterface() throws Exception {
        Gson gson = new GsonBuilder()
                .create();

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());

        TypeAdapter<Bar> adapter = factory.create(gson, TypeToken.get(Bar.class));

        Bar bar = new BarImpl();

        String json = gson.toJson(bar);
        System.out.println(json);

        JsonReader jsonReader = new JsonReader(new StringReader(json));

        Bar barFromJson = adapter.read(jsonReader);

        assertEquals("BarImpl", barFromJson.getClass().getName());
    }

    static class Foo {
        @SerializedName("bar")
        String bar;
        transient Object qux;

        @JsonAdapter(FooAdapter.class)
        Baz quux;
    }

    static class FooAdapter extends TypeAdapter<Baz> {
        @Override public Baz read(JsonReader in) throws IOException {
            return new Baz();
        }

        @Override public void write(JsonWriter out, Baz value) throws IOException {
            out.value("baz");
        }
    }

    static class Bar {
        String name;
    }

    static class BarImpl implements Bar {
        @Override public String toString() {
            return "BarImpl";
        }
    }

    static class Baz {
        @Override public String toString() {
            return "baz";
        }
    }
}