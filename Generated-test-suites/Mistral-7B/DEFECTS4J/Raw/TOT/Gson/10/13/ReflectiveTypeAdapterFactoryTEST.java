import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testReflectiveTypeAdapterFactory() throws IOException {
        // Given
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);

        Field field = Map.class.getDeclaredField("entrySet");
        field.setAccessible(true);

        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingStrategy() {
                    @Override
                    public String translateName(Field f) {
                        return f.getName();
                    }
                },
                new Excluder() {
                    @Override
                    public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override
                    public boolean excludeField(Field f, boolean serialize) {
                        return false;
                    }
                });

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(reflectiveTypeAdapterFactory)
                .create();

        TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};

        TypeAdapter<Map<String, Integer>> typeAdapter = gson.getDelegateAdapter(this, typeToken);

        // When
        String json = gson.toJson(map);
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        Map<String, Integer> deserializedMap = typeAdapter.read(jsonObject.getAsJsonObject().entrySet().getAsJsonArray());

        // Then
        assertEquals(map, deserializedMap);
    }

    @Test
    public void testSerializationWithJsonAdapter() throws IOException {
        // Given
        MyClass myClass = new MyClass("test");

        FieldNamingStrategy fieldNamingPolicy = new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return f.getName().toLowerCase();
            }
        };

        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                fieldNamingPolicy,
                new Excluder() {
                    @Override
                    public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override
                    public boolean excludeField(Field f, boolean serialize) {
                        return false;
                    }
                });

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(reflectiveTypeAdapterFactory)
                .create();

        JsonAdapter jsonAdapter = new JsonAdapter() {
            @Override
            public JsonElement marshal(JsonWriter out, MyClass src) throws IOException {
                out.value("MyJsonAdapterValue");
                return null;
            }

            @Override
            public MyClass unmarshal(JsonReader in) throws IOException {
                return new MyClass("MyJsonAdapterValue");
            }
        };

        MyClass annotatedMyClass = new MyClass("test") {
            @JsonAdapter(JsonAdapter.class)
            private String myField;
        };

        TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};

        // When
        String json = gson.toJson(annotatedMyClass);

        // Then
        assertEquals("{\"my_field\":\"MyJsonAdapterValue\"}", json);
    }

    @Test
    public void testSerializationWithCustomBoundField() throws IOException, IllegalAccessException {
        // Given
        MyClass myClass = new MyClass("test");

        FieldNamingStrategy fieldNamingPolicy = new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return f.getName().toLowerCase();
            }
        };

        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                fieldNamingPolicy,
                new Excluder() {
                    @Override
                    public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override
                    public boolean excludeField(Field f, boolean serialize) {
                        return false;
                    }
                });

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(reflectiveTypeAdapterFactory)
                .create();

        TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};
        Field field = MyClass.class.getDeclaredField("myField");
        field.setAccessible(true);

        TypeAdapter<MyClass> typeAdapter = new TypeAdapter<MyClass>(gson.getDelegateAdapter(this, typeToken),
                new ReflectiveTypeAdapterFactory.BoundField("my_field", true, true) {
                    @Override
                    void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                        super.write(writer, value);
                        String myFieldValue = (String) field.get(value);
                        writer.name("custom_bound_field");
                        writer.value(myFieldValue);
                    }
                });

        // When
        String json = gson.toJson(myClass);

        // Then
        assertEquals("{\"custom_bound_field\":\"test\"}", json);
    }

    @Test
    public void testDeserializationWithCustomBoundField() throws IOException, IllegalAccessException {
        // Given
        String json = "{\"custom_bound_field\":\"test\"}";

        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingStrategy() {
                    @Override
                    public String translateName(Field f) {
                        return f.getName().toLowerCase();
                    }
                },
                new Excluder() {
                    @Override
                    public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override
                    public boolean excludeField(Field f, boolean serialize) {
                        return false;
                    }
                });

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(reflectiveTypeAdapterFactory)
                .create();

        TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};
        Field field = MyClass.class.getDeclaredField("myField");
        field.setAccessible(true);

        TypeAdapter<MyClass> typeAdapter = new TypeAdapter<MyClass>(gson.getDelegateAdapter(this, typeToken),
                new ReflectiveTypeAdapterFactory.BoundField("custom_bound_field", false, true) {
                    @Override
                    MyClass read(JsonReader reader) throws IOException, IllegalAccessException {
                        MyClass myClass = super.read(reader);
                        field.set(myClass, reader.nextString());
                        return myClass;
                    }
                });

        // When
        MyClass deserializedMyClass = typeAdapter.read(new JsonReader(new StringReader(json)));

        // Then
        assertEquals("test", deserializedMyClass.myField);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDeserializationWithInvalidJson() throws IOException {
        // Given
        String json = "{\"test\":1}";

        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingStrategy() {
                    @Override
                    public String translateName(Field f) {
                        return f.getName().toLowerCase();
                    }
                },
                new Excluder() {
                    @Override
                    public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override
                    public boolean excludeField(Field f, boolean serialize) {
                        return false;
                    }
                });

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(reflectiveTypeAdapterFactory)
                .create();

        TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};

        // When & Then (expected JsonSyntaxException)
        typeAdapter.read(new JsonReader(new StringReader(json)));
    }

    private static class MyClass {
        private String myField;

        public MyClass(String myField) {
            this.myField = myField;
        }

        public String getMyField() {
            return myField;
        }

        public void setMyField(String myField) {
            this.myField = myField;
        }
    }
}