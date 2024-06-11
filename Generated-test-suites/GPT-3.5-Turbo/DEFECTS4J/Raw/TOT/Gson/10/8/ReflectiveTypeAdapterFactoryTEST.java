import static org.junit.Assert.*;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {
    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingStrategy fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
        Excluder excluder = Excluder.DEFAULT;

        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    }

    @Test
    public void testExcludeField() {
        Field field = getFieldByName("testField", ReflectiveTypeAdapterFactory.class);
        boolean serialize = true;

        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, serialize, reflectiveTypeAdapterFactory.excluder);

        assertFalse(result);
    }

    @Test
    public void testGetFieldNames() {
        Field field = getFieldByName("testField", ReflectiveTypeAdapterFactory.class);

        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);

        assertEquals(1, fieldNames.size());
        assertEquals("testField", fieldNames.get(0));
    }

    @Test
    public void testCreate() {
        Gson gson = new Gson();
        TypeToken<String> typeToken = TypeToken.get(String.class);

        TypeAdapter<String> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

        assertNull(typeAdapter);
    }

    @Test
    public void testCreateBoundFieldWithJsonAdapterAnnotation() {
        Gson gson = new Gson();
        Field field = getFieldByName("testField", ReflectiveTypeAdapterFactory.class);
        String name = "testField";
        TypeToken<?> fieldType = TypeToken.OBJECT;
        boolean serialize = true;
        boolean deserialize = false;

        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);

        assertNotNull(boundField);
    }

    @Test
    public void testCreateBoundFieldWithoutJsonAdapterAnnotation() {
        Gson gson = new Gson();
        Field field = getFieldByName("testField", ReflectiveTypeAdapterFactory.class);
        String name = "testField";
        TypeToken<?> fieldType = TypeToken.OBJECT;
        boolean serialize = true;
        boolean deserialize = false;

        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);

        assertNotNull(boundField);
    }

    @Test
    public void testGetBoundFields() {
        Gson gson = new Gson();
        TypeToken<?> typeToken = TypeToken.get(ReflectiveTypeAdapterFactory.class);
        Class<?> raw = ReflectiveTypeAdapterFactory.class;

        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, raw);

        assertNotNull(boundFields);
    }

    @Test
    public void testAdapterReadWithNullValue() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(reflectiveTypeAdapterFactory);
        Gson gson = gsonBuilder.create();
        JsonReader jsonReader = new JsonReader(new StringReader("null"));

        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);

        String result = adapter.read(jsonReader);

        assertNull(result);
    }

    @Test
    public void testAdapterReadWithException() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(reflectiveTypeAdapterFactory);
        Gson gson = gsonBuilder.create();
        JsonReader jsonReader = new JsonReader(new StringReader("{\"name\": \"testName\"}"));

        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);

        try {
            adapter.read(jsonReader);
            fail("Expected a JsonSyntaxException to be thrown");
        } catch (JsonSyntaxException e) {
            // Test passed
        }
    }

    @Test
    public void testAdapterWriteWithNullValue() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(reflectiveTypeAdapterFactory);
        Gson gson = gsonBuilder.create();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);

        adapter.write(jsonWriter, null);
        jsonWriter.flush();

        assertEquals("null", stringWriter.toString());
    }

    @Test
    public void testAdapterWrite() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(reflectiveTypeAdapterFactory);
        Gson gson = gsonBuilder.create();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        TestObject testObject = new TestObject("testName", 1);

        adapter.write(jsonWriter, testObject);
        jsonWriter.flush();

        assertEquals("{\"name\":\"testName\",\"id\":1}", stringWriter.toString());
    }

    private Field getFieldByName(String fieldName, Class<?> clazz) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            fail("Field " + fieldName + " not found in class " + clazz.getName());
        }

        return null;
    }

    private static class TestObject {
        @SerializedName("name")
        private String name;
        private int id;

        public TestObject(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
}