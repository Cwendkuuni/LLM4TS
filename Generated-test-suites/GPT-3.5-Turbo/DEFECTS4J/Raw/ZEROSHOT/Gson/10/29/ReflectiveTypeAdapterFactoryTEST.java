import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
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
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
    private ConstructorConstructor constructorConstructor;
    private FieldNamingStrategy fieldNamingStrategy;
    private Excluder excluder;
    private Gson gson;

    @Before
    public void setup() {
        constructorConstructor = mock(ConstructorConstructor.class);
        fieldNamingStrategy = mock(FieldNamingStrategy.class);
        excluder = mock(Excluder.class);
        gson = mock(Gson.class);
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor,
                fieldNamingStrategy, excluder);
    }

    @Test
    public void excludeField_notExcluded_returnsFalse() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertEquals(false, result);
    }

    @Test
    public void excludeField_excludedClass_returnsTrue() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        when(excluder.excludeClass(any(Class.class), any(Boolean.class))).thenReturn(true);
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertEquals(true, result);
    }

    @Test
    public void excludeField_excludedField_returnsTrue() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        when(excluder.excludeField(any(Field.class), any(Boolean.class))).thenReturn(true);
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertEquals(true, result);
    }

    @Test
    public void getFieldNames_annotationNull_returnsListWithTranslatedName() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        when(fieldNamingStrategy.translateName(any(Field.class))).thenReturn("translatedName");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, fieldNames.size());
        assertEquals("translatedName", fieldNames.get(0));
    }

    @Test
    public void getFieldNames_annotationNotNullAndNoAlternates_returnsListWithSerializedNameValue() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        SerializedName serializedName = mock(SerializedName.class);
        when(field.getAnnotation(SerializedName.class)).thenReturn(serializedName);
        when(serializedName.value()).thenReturn("serializedName");

        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, fieldNames.size());
        assertEquals("serializedName", fieldNames.get(0));
    }

    @Test
    public void getFieldNames_annotationNotNullAndAlternates_returnsListWithSerializedNameValueAndAlternateValues() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        SerializedName serializedName = mock(SerializedName.class);
        when(field.getAnnotation(SerializedName.class)).thenReturn(serializedName);
        when(serializedName.value()).thenReturn("serializedName");
        when(serializedName.alternate()).thenReturn(new String[]{"alternate1", "alternate2"});

        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(3, fieldNames.size());
        assertEquals("serializedName", fieldNames.get(0));
        assertEquals("alternate1", fieldNames.get(1));
        assertEquals("alternate2", fieldNames.get(2));
    }

    @Test
    public void create_nonAssignableRawType_returnsNull() {
        TypeToken<Object> typeToken = new TypeToken<Object>() {};
        TypeAdapter<?> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertEquals(null, result);
    }

    @Test
    public void create_assignableRawType_returnsTypeAdapter() {
        TypeToken<ReflectiveTypeAdapterFactoryTest> typeToken = new TypeToken<ReflectiveTypeAdapterFactoryTest>() {};
        TypeAdapter<?> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertEquals(ReflectiveTypeAdapterFactory.Adapter.class, result.getClass());
    }

    @Test
    public void create_nullValueInJsonReader_returnsNull() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);
        Object result = reflectiveTypeAdapterFactory.create(gson, new TypeToken<Object>() {}).read(jsonReader);
        assertEquals(null, result);
    }

    @Test
    public void create_readJsonThrowsIllegalAccessException_throwsAssertionError() throws IOException, IllegalAccessException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
        Exception thrownException = null;
        try {
            reflectiveTypeAdapterFactory.create(gson, new TypeToken<Object>() {}).read(jsonReader);
        } catch (Exception e) {
            thrownException = e;
        }
        assertEquals(AssertionError.class, thrownException.getClass());
    }

    @Test
    public void create_notNullValueInJsonReader_returnsConstructedObject() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(true, false);
        when(jsonReader.nextName()).thenReturn("field");
        when(jsonReader.nextString()).thenReturn("value");
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        ObjectConstructor<Object> objectConstructor = mock(ObjectConstructor.class);
        when(objectConstructor.construct()).thenReturn(new ReflectiveTypeAdapterFactoryTest());
        when(constructorConstructor.get(any(TypeToken.class))).thenReturn(objectConstructor);

        Object result = reflectiveTypeAdapterFactory.create(gson, new TypeToken<Object>() {}).read(jsonReader);
        assertEquals(ReflectiveTypeAdapterFactoryTest.class, result.getClass());
    }

    @Test
     public void write_nullValue_writesNullValueToWriter() throws IOException {
        JsonWriter jsonWriter = mock(JsonWriter.class);
        reflectiveTypeAdapterFactory.create(gson, new TypeToken<Object>() {}).write(jsonWriter, null);
        assertEquals(JsonToken.NULL, jsonWriter.peek());
    }
}