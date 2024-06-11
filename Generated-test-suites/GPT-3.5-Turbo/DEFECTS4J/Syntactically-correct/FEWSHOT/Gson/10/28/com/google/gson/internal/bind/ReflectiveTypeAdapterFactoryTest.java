import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {
    @Mock
    private Gson gson;
    @Mock
    private TypeToken typeToken;
    @Mock
    private ObjectConstructor constructor;
    @Mock
    private JsonWriter jsonWriter;
    @Mock
    private JsonReader jsonReader;

    private ReflectiveTypeAdapterFactory.BoundField boundField;

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        when(constructorConstructor.get(typeToken)).thenReturn(constructor);

        FieldNamingStrategy fieldNamingStrategy = mock(FieldNamingStrategy.class);
        Excluder excluder = mock(Excluder.class);

        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);

        boundField = new ReflectiveTypeAdapterFactory.BoundField("field", true, true) {
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
    }

    @Test
    public void testCreate() {
        when(typeToken.getRawType()).thenReturn((Class) Object.class);
        TypeAdapter<Object> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNull(result);
    }

    @Test
    public void testCreate_PRIMITIVE() {
        when(typeToken.getRawType()).thenReturn((Class) int.class);
        TypeAdapter<Object> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNull(result);
    }

    @Test
    public void testCreate_Interface() {
        when(typeToken.getRawType()).thenReturn((Class) List.class);
        TypeAdapter<Object> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNotNull(result);
    }

    @Test
    public void testExcludeField() {
        Field field = mock(Field.class);
        when(field.getType()).thenReturn((Class) String.class);
        when(field.getName()).thenReturn("field");

        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, true);
        assertFalse(result);

        result = ReflectiveTypeAdapterFactory.excludeField(field, false);
        assertFalse(result);
    }

    @Test
    public void testCreateBoundField_Serialize_Primitive() {
        Gson context = mock(Gson.class);
        Field field = mock(Field.class);
        TypeToken fieldType = mock(TypeToken.class);
        boolean serialize = true;
        boolean deserialize = false;

        when(fieldType.getRawType()).thenReturn((Class) int.class);
        when(field.getAnnotation(JsonAdapter.class)).thenReturn(null);

        ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(context, field, "name", fieldType, serialize, deserialize);
        assertNotNull(result);
    }

    @Test
    public void testCreateBoundField_Deserialize() {
        Gson context = mock(Gson.class);
        Field field = mock(Field.class);
        TypeToken fieldType = mock(TypeToken.class);
        boolean serialize = false;
        boolean deserialize = true;

        when(fieldType.getRawType()).thenReturn((Class) String.class);
        when(field.getAnnotation(JsonAdapter.class)).thenReturn(null);
        when(context.getAdapter(fieldType)).thenReturn(null);

        ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(context, field, "name", fieldType, serialize, deserialize);
        assertNotNull(result);
    }

    @Test
    public void testCreateBoundField_Deserialize_WithAnnotation() {
        Gson context = mock(Gson.class);
        Field field = mock(Field.class);
        TypeToken fieldType = mock(TypeToken.class);
        boolean serialize = false;
        boolean deserialize = true;

        JsonAdapter adapterAnnotation = mock(JsonAdapter.class);
        TypeAdapter typeAdapter = mock(TypeAdapter.class);

        when(fieldType.getRawType()).thenReturn((Class) String.class);
        when(field.getAnnotation(JsonAdapter.class)).thenReturn(adapterAnnotation);
        when(constructorConstructor.get(any(TypeToken.class))).thenReturn(constructor);
        when(getTypeAdapter(any(ConstructorConstructor.class), any(Gson.class), any(TypeToken.class), any(JsonAdapter.class)))
                .thenReturn(typeAdapter);

        ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(context, field, "name", fieldType, serialize, deserialize);
        assertNotNull(result);
    }

    @Test
    public void testGetBoundFields_Interface() {
        Gson context = mock(Gson.class);
        TypeToken<?> type = mock(TypeToken.class);
        Class<?> raw = List.class;

        Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(context, type, raw);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetBoundFields() {
        Gson context = mock(Gson.class);
        TypeToken<?> type = mock(TypeToken.class);
        Class<?> raw = ReflectiveTypeAdapterFactory.class;

        Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(context, type, raw);
        assertNotNull(result);
    }

    @Test
    public void testAdapter_Read_NullValue() throws IOException {
        JsonReader in = mock(JsonReader.class);

        when(in.peek()).thenReturn(JsonToken.NULL);

        Object instance = reflectiveTypeAdapterFactory.new Adapter(constructor, Collections.emptyMap()).read(in);
        assertNull(instance);
    }

    @Test(expected = AssertionError.class)
    public void testAdapter_Write_Exception() throws IOException {
        Object value = new Object();
        reflectiveTypeAdapterFactory.new Adapter(constructor, Collections.singletonMap("field", boundField)).write(jsonWriter, value);
    }
}