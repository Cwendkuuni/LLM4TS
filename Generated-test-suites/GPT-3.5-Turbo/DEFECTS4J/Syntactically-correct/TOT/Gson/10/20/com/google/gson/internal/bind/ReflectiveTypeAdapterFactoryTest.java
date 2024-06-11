
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Mock
    private ConstructorConstructor constructorConstructor;
    @Mock
    private FieldNamingStrategy fieldNamingStrategy;
    @Mock
    private Excluder excluder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    }

    @Test
    public void testCreate() {
        // Prepare test data
        Gson gson = mock(Gson.class);
        TypeToken<String> typeToken = new TypeToken<String>() {};

        // Perform the method call
        TypeAdapter<String> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

        // Verify the result
        assertNotNull(typeAdapter);
    }

    @Test
    public void testExcludeField_whenExcluderExcludesClassAndField() throws NoSuchFieldException {
        // Prepare test data
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        boolean serialize = true;

        when(excluder.excludeClass(ReflectiveTypeAdapterFactory.class, serialize)).thenReturn(true);
        when(excluder.excludeField(field, serialize)).thenReturn(true);

        // Perform the method call
        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, serialize, excluder);

        // Verify the result
        assertEquals(true, result);
    }

    @Test
    public void testExcludeField_whenExcluderDoesNotExcludeClassAndField() throws NoSuchFieldException {
        // Prepare test data
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        boolean serialize = true;

        when(excluder.excludeClass(ReflectiveTypeAdapterFactory.class, serialize)).thenReturn(false);
        when(excluder.excludeField(field, serialize)).thenReturn(false);

        // Perform the method call
        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, serialize, excluder);

        // Verify the result
        assertEquals(false, result);
    }

    @Test
    public void testGetFieldNames_whenNoSerializedNameAnnotationPresent() throws NoSuchFieldException {
        // Prepare test data
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");

        when(fieldNamingStrategy.translateName(field)).thenReturn("constructorConstructor");

        // Perform the method call
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);

        // Verify the result
        List<String> expectedFieldNames = Collections.singletonList("constructorConstructor");
        assertEquals(expectedFieldNames, fieldNames);
    }

    @Test
    public void testGetFieldNames_whenSerializedNameAnnotationPresent() throws NoSuchFieldException {
        // Prepare test data
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        SerializedName serializedName = mock(SerializedName.class);

        when(field.getAnnotation(SerializedName.class)).thenReturn(serializedName);
        when(serializedName.value()).thenReturn("cc");
        when(serializedName.alternate()).thenReturn(new String[]{"alt1", "alt2"});

        // Perform the method call
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);

        // Verify the result
        List<String> expectedFieldNames = new ArrayList<>();
        expectedFieldNames.add("cc");
        expectedFieldNames.add("alt1");
        expectedFieldNames.add("alt2");
        assertEquals(expectedFieldNames, fieldNames);
    }

    @Test
    public void testCreateBoundField_whenJsonAdapterPresent() throws NoSuchFieldException {
        // Prepare test data
        Gson context = mock(Gson.class);
        TypeToken<?> fieldType = mock(TypeToken.class);
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        JsonAdapter jsonAdapter = mock(JsonAdapter.class);
        TypeAdapter<?> mappedTypeAdapter = mock(TypeAdapter.class);
        ObjectConstructor<?> constructor = mock(ObjectConstructor.class);

        when(field.getAnnotation(JsonAdapter.class)).thenReturn(jsonAdapter);
        when(constructorConstructor.get(fieldType)).thenReturn(constructor);
        when(constructor.construct()).thenReturn(new ReflectiveTypeAdapterFactory.Adapter<>(constructor, new HashMap<>()));

        when(jsonAdapter.value()).thenReturn(mappedTypeAdapter);

        // Perform the method call
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(context, field, "constructorConstructor", fieldType, true, true);

        // Verify the result
        assertNotNull(boundField);
    }

    @Test
    public void testCreateBoundField_whenJsonAdapterNotPresent() throws NoSuchFieldException {
        // Prepare test data
        Gson context = mock(Gson.class);
        TypeToken<?> fieldType = mock(TypeToken.class);
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        TypeAdapter<?> typeAdapter = mock(TypeAdapter.class);
        ObjectConstructor<?> constructor = mock(ObjectConstructor.class);

        when(field.getAnnotation(JsonAdapter.class)).thenReturn(null);
        when(context.getAdapter(fieldType)).thenReturn(typeAdapter);
        when(constructorConstructor.get(fieldType)).thenReturn(constructor);
        when(constructor.construct()).thenReturn(new ReflectiveTypeAdapterFactory.Adapter<>(constructor, new HashMap<>()));

        // Perform the method call
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(context, field, "constructorConstructor", fieldType, true, true);

        // Verify the result
        assertNotNull(boundField);
    }

    @Test
    public void testGetBoundFields_whenRawClassIsInterface() {
        // Prepare test data
        Gson context = mock(Gson.class);
        TypeToken<?> type = mock(TypeToken.class);
        Class<?> rawClass = List.class;

        when(rawClass.isInterface()).thenReturn(true);

        // Perform the method call
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(context, type, rawClass);

        // Verify the result
        assertNotNull(boundFields);
        assertEquals(0, boundFields.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBoundFields_whenMultipleFieldsWithSameName() {
        // Prepare test data
        Gson context = mock(Gson.class);
        TypeToken<?> type = mock(TypeToken.class);
        Class<?> rawClass = ReflectiveTypeAdapterFactory.class;

        Field field1 = ReflectiveTypeAdapterFactory.class.getDeclaredFields()[0];
        Field field2 = ReflectiveTypeAdapterFactory.class.getDeclaredFields()[1];

        when(excluder.excludeClass(ReflectiveTypeAdapterFactory.class, true)).thenReturn(false);
        when(excluder.excludeField(field1, true)).thenReturn(true);
        when(excluder.excludeField(field2, true)).thenReturn(true);

        // Perform the method call
        reflectiveTypeAdapterFactory.getBoundFields(context, type, rawClass);
    }

    @Test
    public void testAdapterRead() throws IOException {
        // Prepare test data
        JsonReader in = mock(JsonReader.class);
        TypeAdapter<Object> typeAdapter = mock(TypeAdapter.class);
        TypeToken<Object> typeToken = mock(TypeToken.class);

        ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField("fieldName", true, true) {
            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
                return false;
            }
            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
            }
            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
            }
        };
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new HashMap<>();
        boundFields.put("fieldName", boundField);

        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, boundFields);
        ObjectConstructor<Object> constructor = mock(ObjectConstructor.class);
        when(constructor.construct()).thenReturn(new Object());

        when(typeToken.getRawType()).thenReturn(Object.class);
        when(constructorConstructor.get(typeToken)).thenReturn(constructor);

        // Perform the method call
        Object result = adapter.read(in);

        // Verify the result
        assertNotNull(result);
        verify(in, times(1)).peek();
        verify(in, times(1)).nextNull();
        verify(in, times(1)).beginObject();
        verify(in, times(1)).hasNext();
        verify(in, times(1)).nextName();
        verify(in, times(1)).skipValue();
        verify(in, times(1)).endObject();
    }

    @Test
    public void testAdapterWrite() throws IOException {
        // Prepare test data
        JsonWriter out = mock(JsonWriter.class);
        ReflectiveTypeAdapterFactory.BoundField boundField = mock(ReflectiveTypeAdapterFactory.BoundField.class);
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, Collections.singletonMap("fieldName", boundField));
        Object value = mock(Object.class);

        // Perform the method call
        adapter.write(out, value);

        // Verify the result
        verify(out, times(1)).beginObject();
        verify(out, times(1)).name("fieldName");
        verify(boundField, times(1)).write(out, value);
        verify(out, times(1)).endObject();
    }

}
