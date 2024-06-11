package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.*;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testCreate() {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = gson.getFactory().constructorConstructor;
        FieldNamingStrategy fieldNamingPolicy = gson.getFieldNamingStrategy();
        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeField(Field f, boolean serialize) {
                return false;
            }
        };

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
        TypeAdapter<Foo> fooAdapter = factory.create(gson, new TypeToken<Foo>() {});
        assertNotNull(fooAdapter);
    }

    @Test
    public void testWriteReadStringField() throws IOException {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = gson.getFactory().constructorConstructor;
        FieldNamingStrategy fieldNamingPolicy = gson.getFieldNamingStrategy();
        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeField(Field f, boolean serialize) {
                return false;
            }
        };

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        Class<?> clazz = Foo.class;
        TypeToken<Foo> type = new TypeToken<Foo>() {};
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, type, clazz);

        assertEquals(2, boundFields.size());

        String name1 = "name1";
        String name2 = "name2";

        Field field1 = clazz.getDeclaredField("name1");
        Field field2 = clazz.getDeclaredField("name2");

        ReflectiveTypeAdapterFactory.BoundField boundField1 = boundFields.get(name1);
        ReflectiveTypeAdapterFactory.BoundField boundField2 = boundFields.get(name2);

        assertEquals("name1", boundField1.name);
        assertEquals(true, boundField1.serialized);
        assertEquals(true, boundField1.deserialized);

        assertEquals("name2", boundField2.name);
        assertEquals(true, boundField2.serialized);
        assertEquals(true, boundField2.deserialized);

        Foo foo = new Foo("name1 value", "name2 value");

        JsonWriter jsonWriter = new JsonWriter(System.out);
        factory.getAdapter(gson, new TypeToken<Foo>() {}).write(jsonWriter, foo);
        jsonWriter.close();

        JsonReader jsonReader = new JsonReader(new JsonParser().parse("{" +
                "\"name1\":\"name1 value\"," +
                "\"name2\":\"name2 value\"" +
                "}"));

        Foo readFoo = factory.getAdapter(gson, new TypeToken<Foo>() {}).read(jsonReader);

        assertEquals(foo, readFoo);
    }

    @Test
    public void testWriteReadIntegerField() throws IOException {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = gson.getFactory().constructorConstructor;
        FieldNamingStrategy fieldNamingPolicy = gson.getFieldNamingStrategy();
        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeField(Field f, boolean serialize) {
                return false;
            }
        };

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        Class<?> clazz = Foo.class;
        TypeToken<Foo> type = new TypeToken<Foo>() {};
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, type, clazz);

        assertEquals(2, boundFields.size());

        String name1 = "name1";
        String name2 = "name2";

        Field field1 = clazz.getDeclaredField("name1");
        Field field2 = clazz.getDeclaredField("name2");

        ReflectiveTypeAdapterFactory.BoundField boundField1 = boundFields.get(name1);
        ReflectiveTypeAdapterFactory.BoundField boundField2 = boundFields.get(name2);

        assertEquals("name1", boundField1.name);
        assertEquals(true, boundField1.serialized);
        assertEquals(true, boundField1.deserialized);

        assertEquals("name2", boundField2.name);
        assertEquals(true, boundField2.serialized);
        assertEquals(true, boundField2.deserialized);

        Foo foo = new Foo("name1 value", 42);

        JsonWriter jsonWriter = new JsonWriter(System.out);
        factory.getAdapter(gson, new TypeToken<Foo>() {}).write(jsonWriter, foo);
        jsonWriter.close();

        JsonReader jsonReader = new JsonReader(new JsonParser().parse("{" +
                "\"name1\":\"name1 value\"," +
                "\"name2\":42" +
                "}"));

        Foo readFoo = factory.getAdapter(gson, new TypeToken<Foo>() {}).read(jsonReader);

        assertEquals(foo, readFoo);
    }

    @Test
    public void testWriteReadJsonAdapterField() throws IOException {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = gson.getFactory().constructorConstructor;
        FieldNamingStrategy fieldNamingPolicy = gson.getFieldNamingStrategy();
        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeField(Field f, boolean serialize) {
                return false;
            }
        };

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        Class<?> clazz = FooJsonAdapter.class;
        TypeToken<FooJsonAdapter> type = new TypeToken<FooJsonAdapter>() {};
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, type, clazz);

        assertEquals(1, boundFields.size());

        String name = "foo";

        Field field = clazz.getDeclaredField("foo");

        ReflectiveTypeAdapterFactory.BoundField boundField = boundFields.get(name);

        assertEquals("foo", boundField.name);
        assertEquals(true, boundField.serialized);
        assertEquals(true, boundField.deserialized);

        FooJsonAdapter fooJsonAdapter = new FooJsonAdapter("name");

        JsonWriter jsonWriter = new JsonWriter(System.out);
        factory.getAdapter(gson, new TypeToken<FooJsonAdapter>() {}).write(jsonWriter, fooJsonAdapter);
        jsonWriter.close();

        JsonReader jsonReader = new JsonReader(new JsonParser().parse("{" +
                "\"foo\":\"name\"" +
                "}"));

        FooJsonAdapter readFooJsonAdapter = factory.getAdapter(gson, new TypeToken<FooJsonAdapter>() {}).read(jsonReader);

        assertEquals(fooJsonAdapter, readFooJsonAdapter);
    }

    @Test
    public void testExcludeField() {
        Gson gson = new GsonBuilder().create();
        ConstructorConstructor constructorConstructor = gson.getFactory().constructorConstructor;
        FieldNamingStrategy fieldNamingPolicy = gson.getFieldNamingStrategy();
        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeField(Field f, boolean serialize) {
                return "name1".equals(f.getName());
            }
        };

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        Class<?> clazz = Foo.class;
        TypeToken<Foo> type = new TypeToken<Foo>() {};
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, type, clazz);

        assertEquals(1, boundFields.size());

        String name2 = "name2";

        Field field2 = clazz.getDeclaredField("name2");

        ReflectiveTypeAdapterFactory.BoundField boundField2 = boundFields.get(name2);

        assertEquals("name2", boundField2.name);
        assertEquals(true, boundField2.serialized);
        assertEquals(true, boundField2.deserialized);

        Foo foo = new Foo("name1 value", "name2 value");

        JsonWriter jsonWriter = new JsonWriter(System.out);
        factory.getAdapter(gson, new TypeToken<Foo>() {}).write(jsonWriter, foo);
        jsonWriter.close();

        JsonElement jsonElement = new JsonParser().parse("{" +
                "\"name1\":\"name1 value\"" +
                "}").getAsJsonObject();

        JsonReader jsonReader = new JsonReader(new JsonParser().parse(jsonElement.toString()));
        jsonReader.setLenient(true);

        try {
            Foo readFoo = factory.getAdapter(gson, new TypeToken<Foo>() {}).read(jsonReader);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    private static class Foo {
        private String name1;
        private int name2;

        public Foo(String name1, int name2) {
            this.name1 = name1;
            this.name2 = name2;
        }

        public String getName1() {
            return name1;
        }

        public void setName1(String name1) {
            this.name1 = name1;
        }

        public int getName2() {
            return name2;
        }

        public void setName2(int name2) {
            this.name2 = name2;
        }
    }

    @JsonAdapter(FooJsonAdapter.class)
    private static class FooJsonAdapter {
        private final String name;

        public FooJsonAdapter(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}