import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
    private Gson gson;

    @Mock
    private JsonWriter jsonWriter;
    @Mock
    private JsonReader jsonReader;
    @Mock
    private Gson mockGson;
    @Mock
    private Excluder excluder;

    @Before
    public void setup() {
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(mock(ConstructorConstructor.class), mock(FieldNamingStrategy.class), excluder);
        gson = new Gson();
    }

    @Test
    public void testExcludeField() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");

        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);

        assert(result);
    }

    @Test
    public void testGetFieldNames() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");

        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
        ReflectiveTypeAdapterFactory.BoundField boundField = mock(ReflectiveTypeAdapterFactory.BoundField.class);
        boundFields.put("name", boundField);

        reflectiveTypeAdapterFactory.getBoundFields(mockGson, TypeToken.get(String.class), ReflectiveTypeAdapterFactory.class).put("name", boundField);

        when(boundField.writeField(any())).thenReturn(false);

        ReflectiveTypeAdapterFactory.BoundField result = boundFields.get("name");

        assert(result == boundField);
        verify(boundField, times(1)).writeField(any());
    }

    @Test
    public void testCreate() {
        TypeToken<String> stringToken = TypeToken.get(String.class);

        TypeAdapter<String> result = reflectiveTypeAdapterFactory.create(gson, stringToken);

        assert(result == null);
    }

    @Test
    public void testWrite() throws IOException, IllegalAccessException {
        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(() -> "Test", new LinkedHashMap<>());
        String value = "Test";

        adapter.write(jsonWriter, value);

        verify(jsonWriter, times(1)).beginObject();
        verify(jsonWriter, times(1)).endObject();
        verify(jsonWriter, never()).name(anyString());
        verify(jsonWriter, never()).nullValue();
        verify(jsonWriter, never()).value(anyString());
        verify(jsonWriter, never()).value(anyNumber());
        verify(jsonWriter, never()).value(anyBoolean());
    }

    @Test
    public void testWriteNullValue() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(() -> null, new LinkedHashMap<>());
        String value = null;

        adapter.write(jsonWriter, value);

        verify(jsonWriter, times(1)).nullValue();
        verify(jsonWriter, never()).beginObject();
        verify(jsonWriter, never()).endObject();
        verify(jsonWriter, never()).name(anyString());
        verify(jsonWriter, never()).value(anyString());
        verify(jsonWriter, never()).value(anyNumber());
        verify(jsonWriter, never()).value(anyBoolean());
    }

    @Test
    public void testReadNullValue() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(() -> null, new LinkedHashMap<>());

        String result = adapter.read(jsonReader);

        assert(result == null);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testReadInvalidJson() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(() -> "Test", new LinkedHashMap<>());

        when(jsonReader.peek()).thenThrow(new IllegalStateException());

        adapter.read(jsonReader);
    }

    @Test
    public void testRead() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(() -> "Test", new LinkedHashMap<>());
        String result = null;

        when(jsonReader.peek()).thenReturn(com.google.gson.stream.JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("Test");

        result = adapter.read(jsonReader);

        assert(result.equals("Test"));
    }

}