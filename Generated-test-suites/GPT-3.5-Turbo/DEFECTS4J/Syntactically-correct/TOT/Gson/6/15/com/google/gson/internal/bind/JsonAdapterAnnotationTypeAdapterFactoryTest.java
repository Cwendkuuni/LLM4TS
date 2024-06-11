
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private Gson gson;

    @Before
    public void setUp() {
        factory = new JsonAdapterAnnotationTypeAdapterFactory(mock(com.google.gson.internal.ConstructorConstructor.class));
        gson = new Gson();
    }

    @Test
    public void create_WhenAnnotationIsNull_ReturnsNull() {
        TypeAdapter<?> typeAdapter = factory.create(gson, com.google.gson.reflect.TypeToken.get(Object.class));
        assertEquals(null, typeAdapter);
    }

    @Test
    public void create_WhenAnnotationHasTypeAdapterValue_ReturnsTypeAdapterInstance() {
        TypeAdapter<?> typeAdapter = factory.create(gson, com.google.gson.reflect.TypeToken.get(JsonAdapterType.class));
        assertEquals(JsonAdapterTypeAdapter.class, typeAdapter.getClass());
    }

    @Test
    public void create_WhenAnnotationHasTypeAdapterFactoryValue_ReturnsTypeAdapterInstance() {
        TypeAdapter<?> typeAdapter = factory.create(gson, com.google.gson.reflect.TypeToken.get(JsonAdapterFactoryType.class));
        assertEquals(JsonAdapterFactoryTypeAdapter.class, typeAdapter.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_WhenAnnotationHasInvalidValue_ThrowsIllegalArgumentException() {
        factory.create(gson, com.google.gson.reflect.TypeToken.get(InvalidType.class));
    }

    @Test
    public void getTypeAdapter_WhenValueIsTypeAdapter_ReturnsTypeAdapterInstance() {
        com.google.gson.internal.ConstructorConstructor constructorConstructor = mock(com.google.gson.internal.ConstructorConstructor.class);
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson,
                com.google.gson.reflect.TypeToken.get(JsonAdapter.class), mock(JsonAdapter.class));
        assertEquals(JsonAdapterTypeAdapter.class, typeAdapter.getClass());
    }

    @Test
    public void getTypeAdapter_WhenValueIsTypeAdapterFactory_ReturnsTypeAdapterInstance() {
        com.google.gson.internal.ConstructorConstructor constructorConstructor = mock(com.google.gson.internal.ConstructorConstructor.class);
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson,
                com.google.gson.reflect.TypeToken.get(JsonAdapter.class), mock(JsonAdapterFactory.class));
        assertEquals(JsonAdapterFactoryTypeAdapter.class, typeAdapter.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_WhenValueIsInvalid_ThrowsIllegalArgumentException() {
        com.google.gson.internal.ConstructorConstructor constructorConstructor = mock(com.google.gson.internal.ConstructorConstructor.class);
        factory.getTypeAdapter(constructorConstructor, gson,
                com.google.gson.reflect.TypeToken.get(JsonAdapter.class), mock(InvalidType.class));
    }

    @Test
    public void getTypeAdapter_WhenValueIsTypeAdapter_ReturnsTypeAdapterInstanceWithNullSafe() {
        com.google.gson.internal.ConstructorConstructor constructorConstructor = mock(com.google.gson.internal.ConstructorConstructor.class);
        TypeAdapter<?> typeAdapter = factory.getTypeAdapter(constructorConstructor, gson,
                com.google.gson.reflect.TypeToken.get(JsonAdapter.class), mock(JsonAdapter.class));
        JsonReader reader = new JsonReader(new StringReader("test"));
        reader.setLenient(true);
        JsonWriter writer = new JsonWriter(new StringWriter());
        try {
            typeAdapter.write(writer, "test");
            typeAdapter.read(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        verify(constructorConstructor).get(com.google.gson.reflect.TypeToken.get(JsonAdapterType.class));
    }

    private static class JsonAdapterType {
    }

    private static class JsonAdapterTypeAdapter extends TypeAdapter<JsonAdapterType> {

        @Override
        public void write(JsonWriter out, JsonAdapterType value) throws IOException {
            out.value("test");
        }

        @Override
        public JsonAdapterType read(JsonReader in) throws IOException {
            in.nextString();
            return new JsonAdapterType();
        }
    }

    private static class JsonAdapterFactoryType {
    }

    private static class JsonAdapterFactoryTypeAdapter extends TypeAdapter<JsonAdapterFactoryType> {

        private Gson gson;

        public JsonAdapterFactoryTypeAdapter(Gson gson) {
            this.gson = gson;
        }

        @Override
        public void write(JsonWriter out, JsonAdapterFactoryType value) throws IOException {
            JsonParser parser = new JsonParser();
            out.jsonValue(parser.parse("test").toString());
        }

        @Override
        public JsonAdapterFactoryType read(JsonReader in) throws IOException {
            JsonParser parser = new JsonParser();
            return gson.fromJson(parser.parse("[\"test\"]"), JsonAdapterFactoryType.class);
        }
    }

    private static class InvalidType {
    }

}
