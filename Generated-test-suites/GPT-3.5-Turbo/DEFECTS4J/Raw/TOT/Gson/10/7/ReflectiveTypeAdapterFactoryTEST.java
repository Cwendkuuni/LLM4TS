
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectiveTypeAdapterFactoryTest {
    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Test
    public void testCreate_ReturnsTypeAdapter_WhenRawTypeAssignableToObject() {
        // Arrange
        Gson gson = new GsonBuilder().create();
        TypeToken<Object> type = TypeToken.get(Object.class);

        // Act
        TypeAdapter<Object> result = reflectiveTypeAdapterFactory.create(gson, type);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testCreate_ReturnsNull_WhenRawTypeNotAssignableToObject() {
        // Arrange
        Gson gson = new GsonBuilder().create();
        TypeToken<Integer> type = TypeToken.get(Integer.class);

        // Act
        TypeAdapter<Integer> result = reflectiveTypeAdapterFactory.create(gson, type);

        // Assert
        assertNull(result);
    }

    @Test
    public void testRead_ReturnsNull_WhenJsonIsNull() throws IOException {
        // Arrange
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);

        // Act
        Object result = adapter.read(null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testRead_ReturnsConstructedInstance_WhenJsonIsNotNull() throws IOException {
        // Arrange
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(() -> new Object(), null);

        // Act
        Object result = adapter.read(null);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testRead_ThrowsJsonSyntaxException_WhenJsonIsInvalid() throws IOException {
        // Arrange
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(() -> new Object(), null);
        JsonReaderStub jsonReader = new JsonReaderStub("Invalid Json");

        // Act & Assert
        assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
    }

    @Test
    public void testWrite_WritesNullValue_WhenValueIsNull() throws IOException {
        // Arrange
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        JsonWriterStub jsonWriter = new JsonWriterStub();

        // Act
        adapter.write(jsonWriter, null);
        jsonWriter.close();

        // Assert
        assertTrue(jsonWriter.isOutputNullValue());
    }

    @Test
    public void testWrite_CallsBoundFieldWrite_WhenValueIsNotNull() throws IOException {
        // Arrange
        ReflectiveTypeAdapterFactory.BoundField boundField1 = new ReflectiveTypeAdapterFactory.BoundField(null, true, true) {
            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
                return true;
            }

            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                writer.value("test");
            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {

            }
        };
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, Map.of("field1", boundField1));
        JsonWriterStub jsonWriter = new JsonWriterStub();

        // Act
        adapter.write(jsonWriter, new Object());
        jsonWriter.close();

        // Assert
        assertEquals("{\"field1\":\"test\"}", jsonWriter.getOutput());
    }

    private static class JsonReaderStub extends JsonReader {
        private String input;

        public JsonReaderStub(String input) {
            super(null);
            this.input = input;
        }

        @Override
        public JsonToken peek() {
            return null;
        }

        @Override
        public void close() {

        }
    }

    private static class JsonWriterStub extends JsonWriter {
        private StringBuilder output;

        public JsonWriterStub() {
            super(null);
            this.output = new StringBuilder();
        }

        @Override
        public void nullValue() {
            output.append("null");
        }

        @Override
        public void value(String value) {
            output.append("\"").append(value).append("\"");
        }

        @Override
        public void beginObject() {
            output.append("{");
        }

        @Override
        public void endObject() {
            output.append("}");
        }

        @Override
        public void close() {

        }

        public boolean isOutputNullValue() {
            return output.toString().equals("null");
        }

        public String getOutput() {
            return output.toString();
        }
    }
}

