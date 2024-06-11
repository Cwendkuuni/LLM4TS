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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.assertEquals;

public class ReflectiveTypeAdapterFactoryTest {
    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
    private ConstructorConstructor constructorConstructor;
    private Gson gson;

    @Before
    public void setUp() {
        constructorConstructor = Mockito.mock(ConstructorConstructor.class);
        FieldNamingStrategy fieldNamingStrategy = Mockito.mock(FieldNamingStrategy.class);
        Excluder excluder = Mockito.mock(Excluder.class);
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
        gson = new Gson();
    }

    @Test
    public void testExcludeField_Serialize_ReturnFalse() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertEquals(false, result);
    }

    @Test
    public void testExcludeField_Deserialize_ReturnFalse() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, false);
        assertEquals(false, result);
    }

    @Test
    public void testGetFieldNames_NonSerializedNameField_ReturnSingleName() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expectedNames = Collections.singletonList("constructorConstructor");
        assertEquals(expectedNames, fieldNames);
    }

    @Test
    public void testGetFieldNames_SerializedNameField_ReturnSerializedName() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        SerializedName annotation = Mockito.mock(SerializedName.class);
        Mockito.when(annotation.value()).thenReturn("constructor");
        Mockito.when(field.getAnnotation(SerializedName.class)).thenReturn(annotation);
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expectedNames = Collections.singletonList("constructor");
        assertEquals(expectedNames, fieldNames);
    }

    @Test
    public void testGetFieldNames_SerializedNameWithAlternateField_ReturnSerializedNameAndAlternates() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        SerializedName annotation = Mockito.mock(SerializedName.class);
        Mockito.when(annotation.value()).thenReturn("constructor");
        Mockito.when(annotation.alternate()).thenReturn(new String[]{"alternate1", "alternate2"});
        Mockito.when(field.getAnnotation(SerializedName.class)).thenReturn(annotation);
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("constructor");
        expectedNames.add("alternate1");
        expectedNames.add("alternate2");
        assertEquals(expectedNames, fieldNames);
    }

    @Test
    public void testCreate_NonObjectRawType_ReturnNull() {
        TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
        TypeAdapter<Integer> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertEquals(null, typeAdapter);
    }

    @Test
    public void testCreate_ObjectRawType_ReturnAdapter() {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        ObjectConstructor<Object> constructor = Mockito.mock(ObjectConstructor.class);
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);
        Mockito.when(constructorConstructor.get(typeToken)).thenReturn(constructor);
        Mockito.when(constructor.construct()).thenReturn(new Object());
        Mockito.when(constructorConstructor.get(typeToken)).thenReturn(constructor);
        TypeAdapter<Object> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertEquals(adapter.getClass(), typeAdapter.getClass());
    }

    @Test
    public void testCreate_ObjectRawTypeWithNullTypeAdapter_ReturnAdapter() {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        ObjectConstructor<Object> constructor = Mockito.mock(ObjectConstructor.class);
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);
        Mockito.when(constructorConstructor.get(typeToken)).thenReturn(constructor);
        Mockito.when(constructor.construct()).thenReturn(new Object());
        Mockito.when(constructorConstructor.get(typeToken)).thenReturn(constructor);
        Mockito.when(gson.getAdapter(typeToken)).thenReturn(null);
        TypeAdapter<Object> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertEquals(adapter.getClass(), typeAdapter.getClass());
    }

    @Test
    public void testCreate_ObjectRawTypeWithJsonAdapterTypeAdapter_ReturnAdapter() {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        ObjectConstructor<Object> constructor = Mockito.mock(ObjectConstructor.class);
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);
        JsonAdapter jsonAdapterAnnotation = Mockito.mock(JsonAdapter.class);
        TypeAdapter<?> jsonAdapterTypeAdapter = Mockito.mock(TypeAdapter.class);
        Mockito.when(jsonAdapterAnnotation.value()).thenReturn(jsonAdapterTypeAdapter);
        TypeAdapter<Object> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertEquals(adapter.getClass(), typeAdapter.getClass());
    }

    @Test
    public void testGetBoundFields_WithInterfaceRaw_ReturnEmptyMap() {
        TypeToken<Runnable> typeToken = TypeToken.get(Runnable.class);
        Class<Runnable> raw = (Class<Runnable>) typeToken.getRawType();
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, raw);
        assertEquals(0, boundFields.size());
    }

    @Test
    public void testGetBoundFields_WithObjectRaw_ReturnBoundFieldsMap() {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        Class<Object> raw = (Class<Object>) typeToken.getRawType();
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, raw);
        assertEquals(raw.getDeclaredFields().length, boundFields.size());
    }

    @Test
    public void testCreateBoundField_WithJsonAdapterPresent_ReturnBoundField() throws NoSuchFieldException {
        Gson context = Mockito.mock(Gson.class);
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        TypeToken<?> fieldType = TypeToken.get(ReflectiveTypeAdapterFactory.class);
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(context, field, "constructorConstructor", fieldType, serialize, deserialize);
        assertEquals(ReflectiveTypeAdapterFactory.BoundField.class, boundField.getClass());
    }

    @Test
    public void testCreateBoundField_WithNoJsonAdapterPresent_ReturnBoundField() throws NoSuchFieldException {
        Gson context = Mockito.mock(Gson.class);
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        TypeToken<?> fieldType = TypeToken.get(ReflectiveTypeAdapterFactory.class);
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(context, field, "constructorConstructor", fieldType, serialize, deserialize);
        assertEquals(ReflectiveTypeAdapterFactory.BoundField.class, boundField.getClass());
    }

    @Test
    public void testAdapterRead() throws IOException {
        JsonReader readerMock = Mockito.mock(JsonReader.class);
        Mockito.when(readerMock.peek()).thenReturn(JsonToken.STRING);
        Mockito.when(readerMock.nextString()).thenReturn("test");
        Reader reader = new Reader();
        Type type = ReflectiveTypeAdapterFactory.Adapter.class.getDeclaredClasses()[0];
        TypeAdapter<Object> typeAdapter = reflectiveTypeAdapterFactory.create(gson, TypeToken.get(type));
        Object result = typeAdapter.read(readerMock);
        assertEquals(reader, result);
    }

    @Test
    public void testAdapterWrite() throws IOException {
        JsonWriter writerMock = Mockito.mock(JsonWriter.class);
        Writer writer = new Writer();
        Type type = ReflectiveTypeAdapterFactory.Adapter.class.getDeclaredClasses()[0];
        TypeAdapter<Object> typeAdapter = reflectiveTypeAdapterFactory.create(gson, TypeToken.get(type));
        typeAdapter.write(writerMock, writer);
        Mockito.verify(writerMock, Mockito.times(1)).beginObject();
        Mockito.verify(writerMock, Mockito.times(1)).endObject();
    }

    private static class Reader {
        @SerializedName("test")
        private String test;
    }

    private static class Writer {
        @SerializedName("test")
        private String test;
    }
}