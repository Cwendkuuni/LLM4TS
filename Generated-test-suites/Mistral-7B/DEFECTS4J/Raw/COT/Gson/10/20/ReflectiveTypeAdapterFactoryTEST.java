import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter;
import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.create;
import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.excludeField;
import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getBoundFields;
import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getFieldNames;
import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.ReflectiveTypeAdapterFactory;

public class ReflectiveTypeAdapterFactoryTest {

    @org.junit.Test
    public void testCreate() {
        Gson gson = new GsonBuilder().create();
        TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(),
            new FieldNamingStrategy() {
                @Override
                public String translateName(Field field) {
                    return field.getName();
                }
            },
            new Excluder() {
                @Override
                public boolean excludeClass(Class clazz, boolean serialize) {
                    return false;
                }

                @Override
                public boolean excludeField(Field field, boolean serialize) {
                    return false;
                }
            }
        );
        TypeAdapter<Map<String, Integer>> adapter = create(gson, TypeToken.get(Map.class, TypeToken.get(Integer.class)));
        assertNotNull(adapter);
    }

    @org.junit.Test
    public void testExcludeField() {
        Class testClass = new TestClass();
        Field excludedField = testClass.getClass().getDeclaredField("excludedField");
        excludedField.setAccessible(true);

        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(),
            new FieldNamingStrategy() {
                @Override
                public String translateName(Field field) {
                    return field.getName();
                }
            },
            new Excluder() {
                @Override
                public boolean excludeClass(Class clazz, boolean serialize) {
                    return false;
                }

                @Override
                public boolean excludeField(Field field, boolean serialize) {
                    return excludeField(field, serialize, this) && field.getName().equals("excludedField");
                }
            }
        );

        TypeAdapter<TestClass> adapter = create(new GsonBuilder().create(), TypeToken.get(TestClass.class));
        TestClass testInstance = adapter.read(new JsonReader(new JsonElement("{}").getAsJsonObject().getAsReader()));
        assertFalse(excludedField.isAccessible(testInstance));
        testInstance.setExcludedField(1);
        JsonElement jsonElement = new Gson().toJsonTree(testInstance, new TypeToken<TestClass>() {});
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        assertFalse(jsonObject.has("excludedField"));
    }

    @org.junit.Test
    public void testGetFieldNames() {
        Class testClass = new TestClass();
        Field field = TestClass.class.getDeclaredField("field");
        field.setAccessible(true);

        List<String> fieldNames = getFieldNames(field);
        assertEquals("field", fieldNames.get(0));
    }

    @org.junit.Test
    public void testCreateBoundField() {
        Class testClass = new TestClass();
        Field field = TestClass.class.getDeclaredField("boundField");
        field.setAccessible(true);

        Gson gson = new Gson();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(),
            new FieldNamingStrategy() {
                @Override
                public String translateName(Field field) {
                    return field.getName();
                }
            },
            new Excluder() {
                @Override
                public boolean excludeClass(Class clazz, boolean serialize) {
                    return false;
                }

                @Override
                public boolean excludeField(Field field, boolean serialize) {
                    return false;
                }
            }
        );
        TypeAdapter<TestClass> adapter = create(gson, TypeToken.get(TestClass.class));
        BoundField boundField = adapter.getBoundFields().get("boundField");

        assertEquals("boundField", boundField.name);
        assertTrue(boundField.serialized);
        assertTrue(boundField.deserialized);
    }

    @org.junit.Test
    public void testCreateAdapter() throws IOException {
        Class testClass = new TestClass();
        Field field = TestClass.class.getDeclaredField("boundField");
        field.setAccessible(true);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
            new ConstructorConstructor(),
            new FieldNamingStrategy() {
                @Override
                public String translateName(Field field) {
                    return field.getName();
                }
            },
            new Excluder() {
                @Override
                public boolean excludeClass(Class clazz, boolean serialize) {
                    return false;
                }

                @Override
                public boolean excludeField(Field field, boolean serialize) {
                    return false;
                }
            }
        );
        TypeAdapter<TestClass> adapter = create(gson, TypeToken.get(TestClass.class));

        TestClass testInstance = new TestClass();
        testInstance.setBoundField("test");
        String jsonString = new Gson().toJson(testInstance);
        JsonElement jsonElement = JsonElement.parse(jsonString);
        TestClass deserializedInstance = adapter.read(jsonElement.getAsJsonReader());
        assertEquals(testInstance.getBoundField(), deserializedInstance.getBoundField());

        JsonWriter jsonWriter = new JsonWriter(new StringBuilder());
        adapter.write(jsonWriter, testInstance);
        assertEquals(jsonString, jsonWriter.toString());

        // Test deserialization with a null value
        jsonElement = new JsonElement("{\"boundField\": null}").getAsJsonObject().getAsJsonReader();
        deserializedInstance = adapter.read(jsonElement);
        assertNull(deserializedInstance.getBoundField());
    }

    static class TestClass {
        private String boundField;
        private int excludedField;

        public String getBoundField() {
            return boundField;
        }

        public void setBoundField(String boundField) {
            this.boundField = boundField;
        }

        public int getExcludedField() {
            return excludedField;
        }

        public void setExcludedField(int excludedField) {
            this.excludedField = excludedField;
        }
    }
}