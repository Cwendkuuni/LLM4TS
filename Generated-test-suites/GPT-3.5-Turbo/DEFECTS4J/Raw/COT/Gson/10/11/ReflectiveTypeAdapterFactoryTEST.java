import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
    private ConstructorConstructor constructorConstructor;
    private FieldNamingStrategy fieldNamingStrategy;
    private Excluder excluder;

    @Before
    public void setUp() {
        constructorConstructor = mock(ConstructorConstructor.class);
        fieldNamingStrategy = mock(FieldNamingStrategy.class);
        excluder = mock(Excluder.class);
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor,
                fieldNamingStrategy, excluder);
    }

    @Test
    public void testExcludeField() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, true, excluder);
        assertFalse(result);
    }

    @Test
    public void testGetFieldNamesWithoutSerializedName() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        List<String> names = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, names.size());
        assertEquals("constructorConstructor", names.get(0));
    }

    @Test
    public void testGetFieldNamesWithSerializedName() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("excluder");
        List<String> names = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, names.size());
        assertEquals("excluder", names.get(0));
    }

    @Test
    public void testCreateWithTypeNotAssignableToObject() {
        Gson gson = mock(Gson.class);
        TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
        TypeAdapter<Integer> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNull(result);
    }

    @Test
    public void testCreateWithPrimitiveType() {
        Gson gson = mock(Gson.class);
        TypeToken<int[]> typeToken = TypeToken.get(int[].class);
        TypeAdapter<int[]> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNull(result);
    }

    @Test
    public void testCreateWithObject() {
        Gson gson = mock(Gson.class);
        TypeToken<TestObject> typeToken = TypeToken.get(TestObject.class);
        TypeAdapter<TestObject> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNotNull(result);
    }

    @Test
    public void testCreateBoundFieldWithJsonAdapterAnnotation() throws NoSuchFieldException {
        Gson gson = mock(Gson.class);
        Field field = TestObject.class.getDeclaredField("field2");
        String name = "field2";
        TypeToken<?> fieldType = TypeToken.get(String.class);
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(
                gson, field, name, fieldType, serialize, deserialize);
        assertNotNull(boundField);
        assertTrue(boundField instanceof ReflectiveTypeAdapterFactory.BoundField);
    }

    @Test
    public void testCreateBoundFieldWithoutJsonAdapterAnnotation() throws NoSuchFieldException {
        Gson gson = mock(Gson.class);
        Field field = TestObject.class.getDeclaredField("field1");
        String name = "field1";
        TypeToken<?> fieldType = TypeToken.get(String.class);
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(
                gson, field, name, fieldType, serialize, deserialize);
        assertNotNull(boundField);
        assertTrue(boundField instanceof ReflectiveTypeAdapterFactory.BoundField);
    }

    @Test
    public void testCreateBoundFieldWithJsonAdapterAnnotationAndMappedIsNull() throws NoSuchFieldException {
        Gson gson = mock(Gson.class);
        Field field = TestObject.class.getDeclaredField("field2");
        String name = "field2";
        TypeToken<?> fieldType = TypeToken.get(String.class);
        boolean serialize = true;
        boolean deserialize = true;
        TypeAdapter<?> mapped = null;
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(
                gson, field, name, fieldType, serialize, deserialize);
        assertNotNull(boundField);
        assertTrue(boundField instanceof ReflectiveTypeAdapterFactory.BoundField);
    }

    @Test
    public void testGetBoundFields() {
        Gson gson = mock(Gson.class);
        TypeToken<?> typeToken = TypeToken.get(TestObject.class);
        Class<?> raw = typeToken.getRawType();
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, raw);
        assertNotNull(boundFields);
        assertEquals(2, boundFields.size());
    }

    @Test
    public void testGetYBoundFieldsWithInterfaceClass() {
        Gson gson = mock(Gson.class);
        TypeToken<?> typeToken = TypeToken.get(Runnable.class);
        Class<?> raw = typeToken.getRawType();
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, raw);
        assertNotNull(boundFields);
        assertEquals(0, boundFields.size());
    }

    @Test
    public void testAdapterRead() throws IOException {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
        when(reader.hasNext()).thenReturn(true, false);
        when(reader.nextName()).thenReturn("field1", "field2");
        when(reader.beginObject()).thenReturn(reader);
        when(reader.next()).thenReturn(JsonToken.STRING, JsonToken.STRING);
        when(reader.endObject()).thenReturn(reader);
        when(constructorConstructor.construct()).thenReturn(new TestObject());
        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(
                constructorConstructor, new LinkedHashMap<>());
        TestObject result = adapter.read(reader);
        assertNotNull(result);
    }

    @Test
    public void testAdapterWrite() throws IOException {
        JsonWriter writer = mock(JsonWriter.class);
        when(writer.beginObject()).thenReturn(writer);
        when(writer.endObject()).thenReturn(writer);
        TestObject testObject = new TestObject();
        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(
                constructorConstructor, new LinkedHashMap<>());
        adapter.write(writer, testObject);
        verify(writer, times(2)).name(any(String.class));
        verify(writer, times(2)).value(any(String.class));
    }

    public static class TestObject {
        @SerializedName("customField")
        private String field1;
        @JsonAdapter(TestAdapter.class)
        private String field2;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }
    }

    public static class TestAdapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter out, String value) throws IOException {
            out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            return in.nextString();
        }
    }
}