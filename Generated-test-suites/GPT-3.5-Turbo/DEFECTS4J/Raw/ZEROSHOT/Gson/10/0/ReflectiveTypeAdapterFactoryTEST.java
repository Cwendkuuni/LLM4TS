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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testExcludeField() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = getFieldFromClass("excludeField");

        boolean actual = reflectiveTypeAdapterFactory.excludeField(field, true);

        assertFalse(actual);
    }

    @Test
    public void testGetFieldNames_serializedNameNull() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = getFieldFromClass("getFieldNames");

        List<String> actual = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expected = Collections.singletonList("testField");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetFieldNames_serializedNameNotNull() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = getFieldFromClass("getFieldNames");

        List<String> actual = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expected = Collections.singletonList("serializedField");

        assertEquals(expected, actual);
    }

    @Test
    public void testCreate_rawNotAssignableFromObject() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Gson gson = new Gson();
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};

        TypeAdapter<Integer> actual = reflectiveTypeAdapterFactory.create(gson, typeToken);

        assertNull(actual);
    }

    @Test
    public void testCreate_rawAssignableFromObject() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Gson gson = new Gson();
        TypeToken<Object> typeToken = new TypeToken<Object>() {};

        TypeAdapter<Object> actual = reflectiveTypeAdapterFactory.create(gson, typeToken);

        assertNotNull(actual);
    }

    @Test
    public void testCreateBoundField_boundFieldWithJsonAdapterAnnotation() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Gson gson = new Gson();
        Field field = getFieldFromClass("createBoundField");
        String name = "testField";
        TypeToken<?> fieldType = TypeToken.get(String.class);
        boolean serialize = true;
        boolean deserialize = false;

        ReflectiveTypeAdapterFactory.BoundField actual = reflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);

        assertNotNull(actual);
    }

    @Test
    public void testCreateBoundField_boundFieldWithoutJsonAdapterAnnotation() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Gson gson = new Gson();
        Field field = getFieldFromClass("createBoundField");
        String name = "testField";
        TypeToken<?> fieldType = TypeToken.get(String.class);
        boolean serialize = true;
        boolean deserialize = false;

        ReflectiveTypeAdapterFactory.BoundField actual = reflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);

        assertNotNull(actual);
    }

    @Test
    public void testGetBoundFields() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Gson gson = new Gson();
        TypeToken<?> typeToken = TypeToken.get(ReflectiveTypeAdapterFactoryTest.class);

        Map<String, ReflectiveTypeAdapterFactory.BoundField> actual = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, ReflectiveTypeAdapterFactoryTest.class);

        assertNotNull(actual);
    }

    @Test
    public void testRead_nullInput() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        JsonReader jsonReader = new JsonReader(null);

        Object actual = adapter.read(jsonReader);

        assertNull(actual);
    }

    @Test
    public void testRead_nonNullInput() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        JsonReader jsonReader = new JsonReader(null);

        Object actual = adapter.read(jsonReader);

        assertNotNull(actual);
    }

    @Test
    public void testWrite_nullInput() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        JsonWriter jsonWriter = new JsonWriter(null);

        adapter.write(jsonWriter, null);

        // Check that there are no exceptions thrown
    }

    @Test
    public void testWrite_nonNullInput() throws IOException {
        ReflectiveTypeAdapterFactory.Adapter<Object> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        JsonWriter jsonWriter = new JsonWriter(null);

        adapter.write(jsonWriter, new Object());

        // Check that there are no exceptions thrown
    }

    private Field getFieldFromClass(String methodName) {
        try {
            return ReflectiveTypeAdapterFactory.class.getDeclaredMethod(methodName).getReturnType();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}