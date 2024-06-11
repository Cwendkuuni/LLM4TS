import com.google.gson.*;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

    private Gson gson;

    @Before
    public void setup() {
        gson = new Gson();
    }

    // Test for the excludeField method
    @Test
    public void excludeField_excludesFieldWhenSerializeIsTrue_ReturnsTrue() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = getFieldByName(ReflectiveTypeAdapterFactory.class, "excludeField");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertTrue(result);
    }

    // Test for the excludeField method
    @Test
    public void excludeField_doesNotExcludeFieldWhenSerializeIsFalse_ReturnsFalse() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = getFieldByName(ReflectiveTypeAdapterFactory.class, "excludeField");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, false);
        assertFalse(result);
    }

    // Helper method to get field by name
    private Field getFieldByName(Class<?> clazz, String fieldName) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    // Test for the getFieldNames method
    @Test
    public void getFieldNames_returnsDefaultName_whenSerializedNameAnnotationIsNull() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = getFieldByName(ReflectiveTypeAdapterFactory.class, "getFieldNames");
        List<String> names = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, names.size());
        assertEquals("getFieldNames", names.get(0));
    }

    // Test for the getFieldNames method
    @Test
    public void getFieldNames_returnsSerializedName_whenAnnotationIsNotNull() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = getFieldByName(ReflectiveTypeAdapterFactory.class, "fieldName");
        List<String> names = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, names.size());
        assertEquals("fieldName", names.get(0));
    }

    // Test for the create method
    @Test
    public void create_returnsNull_whenRawTypeIsNotAssignableFromObject() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
        TypeAdapter<Integer> adapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNull(adapter);
    }

    // Test for the Adapter class read method
    @Test
    public void adapter_read_returnsNull_whenJsonTokenIsNull() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        JsonReader reader = new JsonReader(new TestJsonReader());
        assertNull(adapter.read(reader));
    }

    // Test for the Adapter class read method
    @Test
    public void adapter_read_throwsJsonSyntaxException_whenIllegalStateExceptionIsThrown() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        JsonReader reader = new JsonReader(new TestJsonReader());
        reader.setNextToken(JsonToken.BEGIN_ARRAY);
        try {
            adapter.read(reader);
            fail("Expected JsonSyntaxException to be thrown");
        } catch (JsonSyntaxException e) {
            // Test passes
        }
    }

    // Test for the Adapter class write method
    @Test
    public void adapter_write_writesNullValue_whenValueIsNull() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        JsonWriter writer = new JsonWriter(new TestJsonWriter());
        adapter.write(writer, null);
        writer.close();
        assertEquals("null", writer.getOutput());
    }

    // Test for the Adapter class write method
    @Test
    public void adapter_write_writesField_whenWriteFieldIsTrue() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        JsonWriter writer = new JsonWriter(new TestJsonWriter());
        ReflectiveTypeAdapterFactory.BoundField field = new ReflectiveTypeAdapterFactory.BoundField("fieldName", true, true) {
            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
                return true;
            }

            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                writer.value("fieldValue");
            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
            }
        };
        adapter.boundFields.put("fieldName", field);
        adapter.write(writer, null);
        writer.close();
        assertEquals("{\"fieldName\":\"fieldValue\"}", writer.getOutput());
    }

}