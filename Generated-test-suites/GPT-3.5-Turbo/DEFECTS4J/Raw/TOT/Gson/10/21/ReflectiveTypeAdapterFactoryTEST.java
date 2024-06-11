import com.google.gson.Gson;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingPolicy fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
        Excluder excluder = new Excluder();
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
    }

    @Test
    public void excludeField_withValidField_shouldReturnFalse() {
        Field field = getField("name");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertFalse(result);
    }

    @Test
    public void excludeField_withExcludedField_shouldReturnTrue() {
        Field field = getField("id");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertTrue(result);
    }

    @Test
    public void create_withNonObjectClass_shouldReturnNull() {
        Gson gson = new Gson();
        TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
        TypeAdapter<Integer> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNull(result);
    }

    @Test
    public void create_withObjectClass_shouldReturnAdapter() {
        Gson gson = new Gson();
        TypeToken<TestObject> typeToken = TypeToken.get(TestObject.class);
        TypeAdapter<TestObject> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNotNull(result);
    }

    @Test
    public void getFieldNames_withFieldWithoutSerializedNameAnnotation_shouldReturnSingleNameList() {
        Field field = getField("name");
        List<String> result = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, result.size());
        assertEquals("name", result.get(0));
    }

    @Test
    public void getFieldNames_withFieldWithSerializedNameAnnotation_shouldReturnMultipleNameList() {
        Field field = getField("age");
        List<String> result = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(3, result.size());
        assertEquals("age", result.get(0));
        assertEquals("years", result.get(1));
        assertEquals("old", result.get(2));
    }

    @Test
    public void createBoundField_withJsonAdapterAnnotation_shouldReturnBoundFieldWithAdapter() {
        Gson gson = new Gson();
        Field field = getField("customAttribute");
        String name = "custom_attribute";
        TypeToken<String> fieldType = TypeToken.get(String.class);
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);
        assertNotNull(result);
        assertEquals(name, result.name);
        assertTrue(result.serialized);
        assertTrue(result.deserialized);
    }

    @Test
    public void createBoundField_withoutJsonAdapterAnnotation_shouldReturnBoundFieldWithDefaultAdapter() {
        Gson gson = new Gson();
        Field field = getField("age");
        String name = "age";
        TypeToken<Integer> fieldType = TypeToken.get(Integer.class);
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);
        assertNotNull(result);
        assertEquals(name, result.name);
        assertTrue(result.serialized);
        assertTrue(result.deserialized);
    }

    @Test
    public void getBoundFields_withEmptyRawClass_shouldReturnEmptyMap() {
        Gson gson = new Gson();
        TypeToken<Map> typeToken = TypeToken.get(Map.class);
        Class<Map> raw = typeToken.getRawType();
        Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, raw);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getBoundFields_withNonInterfaceRawClass_shouldReturnBoundFieldMap() {
        Gson gson = new Gson();
        TypeToken<TestObject> typeToken = TypeToken.get(TestObject.class);
        Class<TestObject> raw = typeToken.getRawType();
        Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, raw);
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("id"));
        assertTrue(result.containsKey("age"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBoundFields_withDuplicateFieldNames_shouldThrowException() {
        Gson gson = new Gson();
        TypeToken<TestObjectDuplicateFields> typeToken = TypeToken.get(TestObjectDuplicateFields.class);
        Class<TestObjectDuplicateFields> raw = typeToken.getRawType();
        reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, raw);
    }

    @Test
    public void Adapter_read_withJsonNull_shouldReturnNull() throws IOException {
        Gson gson = new Gson();
        String json = "{\"name\":null,\"id\":null,\"age\":null}";
        TypeToken<TestObject> typeToken = TypeToken.get(TestObject.class);
        TypeAdapter<TestObject> adapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        TestObject result = adapter.read(new JsonReader(new StringReader(json)));
        assertNull(result);
    }

    @Test
    public void Adapter_read_withInvalidJson_shouldThrowException() throws IOException {
        Gson gson = new Gson();
        String json = "{\"name\":\"Test Object\",\"id\":\"Invalid\",\"age\":\"20\"}";
        TypeToken<TestObject> typeToken = TypeToken.get(TestObject.class);
        TypeAdapter<TestObject> adapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        try {
            adapter.read(new JsonReader(new StringReader(json)));
            fail("Expected JsonSyntaxException to be thrown");
        } catch (JsonSyntaxException e) {
            // Exception successfully thrown
        }
    }

    @Test
    public void Adapter_read_withValidJson_shouldReturnTestObject() throws IOException {
        Gson gson = new Gson();
        String json = "{\"name\":\"Test Object\",\"id\":123,\"age\":30}";
        TypeToken<TestObject> typeToken = TypeToken.get(TestObject.class);
        TypeAdapter<TestObject> adapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        TestObject result = adapter.read(new JsonReader(new StringReader(json)));
        assertNotNull(result);
        assertEquals("Test Object", result.getName());
        assertEquals(123, result.getId());
        assertEquals(30, result.getAge());
    }

    @Test
    public void Adapter_write_withNullObject_shouldWriteJsonNull() throws IOException {
        Gson gson = new Gson();
        StringWriter writer = new StringWriter();
        TypeToken<TestObject> typeToken = TypeToken.get(TestObject.class);
        TypeAdapter<TestObject> adapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        adapter.write(new JsonWriter(writer), null);
        assertEquals("null", writer.toString());
    }

    @Test
    public void Adapter_write_withTestObject_shouldWriteValidJson() throws IOException {
        Gson gson = new Gson();
        StringWriter writer = new StringWriter();
        TestObject testObject = new TestObject("Test Object", 123, 30);
        TypeToken<TestObject> typeToken = TypeToken.get(TestObject.class);
        TypeAdapter<TestObject> adapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        adapter.write(new JsonWriter(writer), testObject);
        assertEquals("{\"name\":\"Test Object\",\"id\":123,\"age\":30}", writer.toString());
    }

    private Field getField(String fieldName) {
        try {
            return TestObject.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            fail("Field not found");
        }
        return null;
    }
}

class TestObject {
    private String name;
    private int id;
    @SerializedName(value = "age", alternate = {"years", "old"})
    private int age;

    public TestObject(String name, int id, int age) {
        this.name = name;
        this.id = id;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }
}

class TestObjectDuplicateFields {
    @SerializedName("name")
    private String name;
    @SerializedName("name")
    private String duplicateName;
}