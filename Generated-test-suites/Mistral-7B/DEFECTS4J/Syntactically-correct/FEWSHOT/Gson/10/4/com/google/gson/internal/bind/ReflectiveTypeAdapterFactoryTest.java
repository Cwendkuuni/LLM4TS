package com.google.gson.internal.bind;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.*;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {
    private static class TestClass {
        private String name;
        @SerializedName("id")
        private int id;
        private boolean flag;
        private List<String> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }

    @Test
    public void testCreateTypeAdapter() {
        FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
            @Override
            public String translateName(Field field) {
                return field.getName().replace("_", "");
            }
        };

        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeField(Field field, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return false;
            }
        };

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(TestClass.class);

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        TypeAdapter<TestClass> adapter = factory.create(new Gson(), new TypeToken<TestClass>() {});

        assertNotNull(adapter);
    }

    @Test
    public void testWriteRead() throws IOException {
        FieldNamingPolicy fieldNamingPolicy = new FieldNamingPolicy() {
            @Override
            public String translateName(Field field) {
                return field.getName().replace("_", "");
            }
        };

        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeField(Field field, boolean serialize) {
                return false;
            }

            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return false;
            }
        };

        ConstructorConstructor constructorConstructor = new ConstructorConstructor(TestClass.class);

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        TypeAdapter<TestClass> adapter = factory.create(new Gson(), new TypeToken<TestClass>() {});

        String json = "{\"name\":\"test\",\"id\":1,\"flag\":true,\"list\":[\"item1\",\"item2\"]}";

        TestClass testClass = new TestClass();
        testClass.setName("test");
        testClass.setId(1);
        testClass.setFlag(true);
        testClass.setList(new ArrayList<>(Collections.singletonList("item1")));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter jsonWriter = new JsonWriter(outputStream);
        jsonWriter.setLenient(true);

        adapter.write(jsonWriter, testClass);
        jsonWriter.flush();

        JsonObject jsonObject = new JsonParser().parse(new ByteArrayInputStream(outputStream.toByteArray())).getAsJsonObject();
        TestClass deserializedTestClass = adapter.read(new JsonReader(new ByteArrayInputStream(outputStream.toByteArray()))).getValue();

        assertEquals(testClass.getName(), deserializedTestClass.getName());
        assertEquals(testClass.getId(), deserializedTestClass.getId());
        assertEquals(testClass.isFlag(), deserializedTestClass.isFlag());
        assertEquals(testClass.getList().size(), deserializedTestClass.getList().size());
        assertEquals(testClass.getList().get(0), deserializedTestClass.getList().get(0));
    }
}