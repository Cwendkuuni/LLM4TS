import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

    private Gson gson;
    private TypeAdapterFactory typeAdapterFactory;

    @Before
    public void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.<Type, InstanceCreator<?>>emptyMap());
        FieldNamingStrategy fieldNamingStrategy = new FieldNamingStrategy() {
            @Override
            public String translateName(Field field) {
                return field.getName();
            }
        };
        Excluder excluder = new Excluder();
        typeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
        gson = new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create();
    }

    @Test
    public void create_TypeIsPrimitive_ReturnsNull() {
        TypeAdapter<Integer> result = typeAdapterFactory.create(gson, TypeToken.get(int.class));
        assertNull(result);
    }

    @Test
    public void create_TypeIsNull_ReturnsNull() {
        TypeAdapter<Object> result = typeAdapterFactory.create(gson, null);
        assertNull(result);
    }

    @Test
    public void create_RawTypeIsInterface_ReturnsEmptyMap() {
        TypeToken<List<Integer>> type = new TypeToken<List<Integer>>() {};

        TypeAdapter<List<Integer>> result = typeAdapterFactory.create(gson, type);

        assertNotNull(result);
        assertTrue(result instanceof ReflectiveTypeAdapterFactory.Adapter);

        ReflectiveTypeAdapterFactory.Adapter adapter = (ReflectiveTypeAdapterFactory.Adapter) result;
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = adapter.boundFields;
        assertNotNull(boundFields);
        assertEquals(0, boundFields.size());
    }

    @Test
    public void create_RawTypeIsObject_ReturnsAdapterWithBoundFields() {
        TypeToken<TestObject> type = new TypeToken<TestObject>() {};

        TypeAdapter<TestObject> result = typeAdapterFactory.create(gson, type);

        assertNotNull(result);
        assertTrue(result instanceof ReflectiveTypeAdapterFactory.Adapter);

        ReflectiveTypeAdapterFactory.Adapter adapter = (ReflectiveTypeAdapterFactory.Adapter) result;
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = adapter.boundFields;
        assertNotNull(boundFields);
        assertEquals(2, boundFields.size());

        assertTrue(boundFields.containsKey("id"));
        assertTrue(boundFields.containsKey("name"));

        ReflectiveTypeAdapterFactory.BoundField idField = boundFields.get("id");
        ReflectiveTypeAdapterFactory.BoundField nameField = boundFields.get("name");

        assertNotNull(idField);
        assertNotNull(nameField);

        assertFalse(idField.deserialized);
        assertTrue(nameField.deserialized);
    }

    @Test
    public void create_TypeAdapterWithAnnotation_ReturnsTypeAdapterWithMappedField() {
        TypeToken<TestObjectWithAnnotation> type = new TypeToken<TestObjectWithAnnotation>() {};

        TypeAdapter<TestObjectWithAnnotation> result = typeAdapterFactory.create(gson, type);

        assertNotNull(result);
        assertTrue(result instanceof ReflectiveTypeAdapterFactory.Adapter);

        ReflectiveTypeAdapterFactory.Adapter adapter = (ReflectiveTypeAdapterFactory.Adapter) result;
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = adapter.boundFields;
        assertNotNull(boundFields);
        assertEquals(1, boundFields.size());

        assertTrue(boundFields.containsKey("status"));

        ReflectiveTypeAdapterFactory.BoundField statusField = boundFields.get("status");

        assertNotNull(statusField);

        assertTrue(statusField.deserialized);
    }

    @Test
    public void Adapter_read_NullToken_ReturnsNull() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = new ReflectiveTypeAdapterFactory.Adapter<TestObject>(null, null);

        JsonReader reader = new JsonReader(new StringReader("null"));
        reader.setLenient(true);

        TestObject result = adapter.read(reader);

        assertNull(result);
    }

    @Test(expected = AssertionError.class)
    public void Adapter_read_IllegalStateException_ThrowsJsonSyntaxException() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = new ReflectiveTypeAdapterFactory.Adapter<TestObject>(null, null);

        JsonReader reader = new JsonReader(new StringReader("{"));
        reader.setLenient(true);

        TestObject result = adapter.read(reader);
    }

    @Test
    public void Adapter_read_WithFields_ReadsFieldsCorrectly() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = new ReflectiveTypeAdapterFactory.Adapter<TestObject>(null, getBoundFields());

        JsonReader reader = new JsonReader(new StringReader("{\"id\": 1, \"name\": \"John\"}"));
        reader.setLenient(true);

        TestObject result = adapter.read(reader);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getName());
    }

    @Test
    public void Adapter_write_NullValue_WritesNull() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = new ReflectiveTypeAdapterFactory.Adapter<TestObject>(null, getBoundFields());

        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);

        adapter.write(jsonWriter, null);

        jsonWriter.close();

        assertEquals("null", writer.toString());
    }

    @Test
    public void Adapter_write_WithFields_WritesFieldsCorrectly() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = new ReflectiveTypeAdapterFactory.Adapter<TestObject>(null, getBoundFields());

        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);

        TestObject testObject = new TestObject(1, "John");
        adapter.write(jsonWriter, testObject);

        jsonWriter.close();

        assertEquals("{\"id\":1,\"name\":\"John\"}", writer.toString());
    }

    private Map<String, ReflectiveTypeAdapterFactory.BoundField> getBoundFields() {
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();

        boundFields.put("id", new ReflectiveTypeAdapterFactory.BoundField("id", true, true) {
            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
                return true;
            }

            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                TestObject testObject = (TestObject) value;
                writer.value(testObject.getId());
            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {

            }
        });

        boundFields.put("name", new ReflectiveTypeAdapterFactory.BoundField("name", true, true) {
            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
                return true;
            }

            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                TestObject testObject = (TestObject) value;
                writer.value(testObject.getName());
            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {

            }
        });

        return boundFields;
    }
}