import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory adapterFactory;
    private ConstructorConstructor constructorConstructor;
    private FieldNamingStrategy fieldNamingStrategy;
    private Excluder excluder;

    @Before
    public void setUp() {
        constructorConstructor = mock(ConstructorConstructor.class);
        fieldNamingStrategy = mock(FieldNamingStrategy.class);
        excluder = mock(Excluder.class);
        adapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    }

    @Test
    public void testCreate_shouldReturnNullForPrimitiveType() {
        TypeToken<Integer> typeToken = TypeToken.get(int.class);
        TypeAdapter<Integer> adapter = adapterFactory.create(mock(Gson.class), typeToken);
        assertNull(adapter);
    }

    @Test
    public void testCreate_shouldReturnNullForNonObjectClass() {
        TypeToken<String> typeToken = TypeToken.get(String.class);
        TypeAdapter<String> adapter = adapterFactory.create(mock(Gson.class), typeToken);
        assertNull(adapter);
    }

    @Test
    public void testCreate_shouldReturnAdapterForObjectType() {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        TypeAdapter<Object> adapter = adapterFactory.create(mock(Gson.class), typeToken);
        assertNotNull(adapter);
    }

    @Test
    public void testCreate_shouldCreateAdapterWithConstructorAndBoundFields() {
        Gson gson = mock(Gson.class);
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        TypeAdapter<Object> expectedAdapter = mock(TypeAdapter.class);
        when(constructorConstructor.get(typeToken)).thenReturn(mock(ObjectConstructor.class));
        when(gson.getAdapter(typeToken)).thenReturn(expectedAdapter);

        TypeAdapter<Object> adapter = adapterFactory.create(gson, typeToken);

        assertNotNull(adapter);
        verify(constructorConstructor).get(typeToken);
        verify(gson).getAdapter(typeToken);
    }

    @Test
    public void testCreateBoundFields_shouldReturnEmptyMapForInterface() {
        Gson gson = mock(Gson.class);
        TypeToken<ReflectiveTypeAdapterFactoryTest> typeToken = TypeToken.get(ReflectiveTypeAdapterFactoryTest.class);
        Class<ReflectiveTypeAdapterFactoryTest> raw = ReflectiveTypeAdapterFactoryTest.class;

        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = adapterFactory.getBoundFields(gson, typeToken, raw);

        assertNotNull(boundFields);
        assertTrue(boundFields.isEmpty());
    }

    @Test
    public void testCreateBoundFields_shouldReturnMapWithBoundFields() {
        Gson gson = mock(Gson.class);
        TypeToken<TestObject> typeToken = TypeToken.get(TestObject.class);
        Class<TestObject> raw = TestObject.class;

        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = adapterFactory.getBoundFields(gson, typeToken, raw);

        assertNotNull(boundFields);
        assertFalse(boundFields.isEmpty());
    }

    @Test
    public void testCreateBoundFields_shouldThrowExceptionForDuplicateFieldNames() {
        Gson gson = mock(Gson.class);
        TypeToken<DuplicateFieldTestObject> typeToken = TypeToken.get(DuplicateFieldTestObject.class);
        Class<DuplicateFieldTestObject> raw = DuplicateFieldTestObject.class;

        try {
            adapterFactory.getBoundFields(gson, typeToken, raw);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("class com.google.gson.internal.bind.ReflectiveTypeAdapterFactoryTest$DuplicateFieldTestObject declares multiple JSON fields named field", e.getMessage());
        }
    }

    @Test
    public void testAdapterRead_shouldReturnNullForNullToken() throws IOException {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.NULL);

        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = createAdapter();

        TestObject object = adapter.read(reader);

        assertNull(object);
        verify(reader).nextNull();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAdapterRead_shouldReadFieldsFromJsonReader() throws IOException {
        JsonReader reader = mock(JsonReader.class);
        when(reader.hasNext()).thenReturn(true, true, true, false);
        when(reader.nextName()).thenReturn("id", "name", "active");
        when(reader.peek()).thenReturn(JsonToken.NUMBER, JsonToken.STRING, JsonToken.BOOLEAN);
        when(reader.nextInt()).thenReturn(1);
        when(reader.nextString()).thenReturn("Test");
        when(reader.nextBoolean()).thenReturn(true);

        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = createAdapter();

        TestObject object = adapter.read(reader);

        assertNotNull(object);
        assertEquals(1, object.id);
        assertEquals("Test", object.name);
        assertTrue(object.active);

        verify(reader, times(3)).hasNext();
        verify(reader, times(3)).nextName();
        verify(reader).nextInt();
        verify(reader).nextString();
        verify(reader).nextBoolean();
    }

    @Test
    public void testAdapterWrite_shouldWriteNullForNullValue() throws IOException {
        JsonWriter writer = mock(JsonWriter.class);
        TestObject object = null;

        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = createAdapter();
        adapter.write(writer, object);

        verify(writer).nullValue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAdapterWrite_shouldWriteFieldsToJsonWriter() throws IOException {
        JsonWriter writer = mock(JsonWriter.class);
        TestObject object = new TestObject(1, "Test", true);

        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = createAdapter();
        adapter.write(writer, object);

        verify(writer).beginObject();
        verify(writer).name("id");
        verify(writer).value(1);
        verify(writer).name("name");
        verify(writer).value("Test");
        verify(writer).name("active");
        verify(writer).value(true);
        verify(writer).endObject();
    }

    private ReflectiveTypeAdapterFactory.Adapter<TestObject> createAdapter() {
        ObjectConstructor<TestObject> constructor = mock(ObjectConstructor.class);
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new HashMap<>();
        ReflectiveTypeAdapterFactory.BoundField idBoundField = createBoundField("id", true, true);
        ReflectiveTypeAdapterFactory.BoundField nameBoundField = createBoundField("name", true, true);
        ReflectiveTypeAdapterFactory.BoundField activeBoundField = createBoundField("active", true, true);
        boundFields.put("id", idBoundField);
        boundFields.put("name", nameBoundField);
        boundFields.put("active", activeBoundField);
        return new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);
    }

    private ReflectiveTypeAdapterFactory.BoundField createBoundField(String name, boolean serialized, boolean deserialized) {
        ReflectiveTypeAdapterFactory.BoundField boundField = mock(ReflectiveTypeAdapterFactory.BoundField.class);
        when(boundField.name).thenReturn(name);
        when(boundField.serialized).thenReturn(serialized);
        when(boundField.deserialized).thenReturn(deserialized);
        return boundField;
    }

    private class TestObject {
        @SerializedName("id")
        int id;
        @SerializedName("name")
        String name;
        @SerializedName("active")
        boolean active;

        public TestObject(int id, String name, boolean active) {
            this.id = id;
            this.name = name;
            this.active = active;
        }
    }

    private class DuplicateFieldTestObject {
        @SerializedName("field1")
        String field;
        @SerializedName("field2")
        String field;

        public DuplicateFieldTestObject(String field) {
            this.field = field;
        }
    }
}