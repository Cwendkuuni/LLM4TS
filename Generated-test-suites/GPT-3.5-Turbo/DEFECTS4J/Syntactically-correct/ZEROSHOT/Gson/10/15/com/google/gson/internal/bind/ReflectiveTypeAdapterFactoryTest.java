import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.*;
import com.google.gson.internal.*;
import com.google.gson.reflect.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {
    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingStrategy fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
        Excluder excluder = Excluder.DEFAULT;
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    }

    @Test
    public void testExcludeField() throws NoSuchFieldException {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(),
                FieldNamingPolicy.IDENTITY, Excluder.DEFAULT);
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        boolean serialize = false;
        boolean excludeFieldResult = ReflectiveTypeAdapterFactory.excludeField(field, serialize, reflectiveTypeAdapterFactory.excluder);
        assertFalse(excludeFieldResult);
    }

    @Test
    public void testGetFieldNames() throws NoSuchFieldException {
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(Arrays.asList("constructorConstructor"), fieldNames);
    }

    @Test
    public void testCreate() {
        Gson gson = new Gson();
        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
        TypeAdapter<List<String>> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNull(typeAdapter);
    }

    @Test
    public void testCreateBoundField() throws IOException, IllegalAccessException {
        Gson gson = new Gson();
        Field field = FieldNamingPolicy.class.getDeclaredField("IDENTITY");
        String name = "name";
        TypeToken<?> fieldType = TypeToken.get(String.class);
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);
        assertNotNull(boundField);

        JsonWriter writer = new JsonWriter(null);
        Object value = new Object();
        boundField.write(writer, value);

        JsonReader reader = new JsonReader(null);
        boundField.read(reader, value);

        boolean writeFieldResult = boundField.writeField(value);
        assertTrue(writeFieldResult);
    }

    @Test
    public void testGetBoundFields() {
        Gson gson = new Gson();
        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
        Class<?> raw = String.class;
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, raw);
        assertNotNull(boundFields);
    }

    @Test
    public void testAdapter() {
        ObjectConstructor<String> constructor = new ObjectConstructor<String>() {
            @Override
            public String construct() {
                return null;
            }
        };
        TypeToken<String> typeToken = new TypeToken<String>() {};
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new HashMap<>();
        ReflectiveTypeAdapterFactory.Adapter<String> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);
        assertNotNull(adapter);

        JsonReader reader = new JsonReader(null);
        try {
            adapter.read(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonWriter writer = new JsonWriter(null);
        try {
            adapter.write(writer, "test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}