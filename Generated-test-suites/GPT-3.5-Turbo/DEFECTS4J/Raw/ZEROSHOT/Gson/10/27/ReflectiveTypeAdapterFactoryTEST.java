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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;

public class ReflectiveTypeAdapterFactoryTest {
    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingStrategy fieldNamingStrategy = new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return null;
            }
        };
        Excluder excluder = new Excluder();
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    }

    @Test
    public void testExcludeField() {
        Field f = null;
        boolean serialize = true;

        boolean result = reflectiveTypeAdapterFactory.excludeField(f, serialize);
        assertTrue(result);
    }

    @Test
    public void testExcludeField_withExcludedClass() {
        Field f = null;
        boolean serialize = true;
        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                return true;
            }
        };
        ReflectiveTypeAdapterFactory.reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, excluder);

        boolean result = reflectiveTypeAdapterFactory.excludeField(f, serialize);
        assertTrue(result);
    }

    @Test
    public void testExcludeField_withExcludedField() {
        Field f = new Field();
        f.setType(String.class);
        boolean serialize = true;
        Excluder excluder = new Excluder() {
            @Override
            public boolean excludeField(Field f, boolean serialize) {
                return true;
            }
        };
        ReflectiveTypeAdapterFactory.reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, excluder);

        boolean result = reflectiveTypeAdapterFactory.excludeField(f, serialize);
        assertTrue(result);
    }

    @Test
    public void testGetFieldNames() {
        Field f = new Field();
        f.setAnnotation(SerializedName.class);
        ReflectiveTypeAdapterFactory.reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(f);
        assertNotNull(fieldNames);
    }

    @Test
    public void testCreate() {
        Gson gson = new Gson();
        TypeToken<String> type = new TypeToken<String>(){};

        TypeAdapter<String> typeAdapter = reflectiveTypeAdapterFactory.create(gson, type);
        assertNull(typeAdapter);
    }

    @Test
    public void testCreate_withPrimitiveType() {
        Gson gson = new Gson();
        TypeToken<Boolean> type = new TypeToken<Boolean>(){};

        TypeAdapter<Boolean> typeAdapter = reflectiveTypeAdapterFactory.create(gson, type);
        assertNull(typeAdapter);
    }

    @Test
    public void testCreateBoundField() {
        Gson gson = new Gson();
        ReflectiveTypeAdapterFactory fieldType = new ReflectiveTypeAdapterFactory(null, null, null);
        boolean serialize = true;
        boolean deserialize = true;

        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, fieldType, "name", TypeToken.get(String.class), serialize, deserialize);
        assertNotNull(boundField);
    }

    @Test
    public void testCreateBoundField_withJsonAdapterPresent() {
        Gson gson = new Gson();
        ReflectiveTypeAdapterFactory fieldType = new ReflectiveTypeAdapterFactory(null, null, null);
        boolean serialize = true;
        boolean deserialize = true;
        JsonAdapter jsonAdapter = new JsonAdapter() {
            @Override
            public Class<?> value() {
                return null;
            }

            @Override
            public Class<?>[] classes() {
                return new Class<?>[0];
            }

            @Override
            public Class<? extends TypeAdapter> adapter() {
                return null;
            }
        };
        Field f = null;

        field.setAnnotation(jsonAdapter);
        ReflectiveTypeAdapterFactory.reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        ReflectiveTypeAdapterFactory.getTypeAdapter();

        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, fieldType, "name", TypeToken.get(String.class), serialize, deserialize);
        assertNotNull(boundField);
    }

    @Test
    public void testGetBoundFields() {
        Gson gson = new Gson();
        TypeToken<?> type = new TypeToken<Object>(){};
        Class<?> raw = Object.class;

        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, type, raw);
        assertNotNull(boundFields);
    }

    @Test
    public void testGetBoundFields_withInterface() {
        Gson gson = new Gson();
        TypeToken<?> type = new TypeToken<Object>(){};
        Class<?> raw = Object.class;

        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, type, raw);
        assertNotNull(boundFields);
    }

    @Test
    public void testWriteField() {
        ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField("name", true, true) {
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

        try {
            boolean result = boundField.writeField(null);
            assertFalse(result);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteField_withSerializedFalse() {
        ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField("name", false, true) {
            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
                return true;
            }

            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {

            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {

            }
        };

        try {
            boolean result = boundField.writeField(null);
            assertFalse(result);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteField_withFieldValueNullAndSerializedTrue() {
        ReflectiveTypeAdapterFactory.BoundField boundField = new ReflectiveTypeAdapterFactory.BoundField("name", true, true) {
            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
                return true;
            }

            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {

            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {

            }
        };

        try {
            boolean result = boundField.writeField(null);
            assertTrue(result);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAdapterRead() {
        JsonReader in = new JsonReader();
        ReflectiveTypeAdapterFactory.Adapter<T> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);

        try {
            Object result = adapter.read(in);
            assertNull(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAdapterWrite() {
        JsonWriter out = new JsonWriter();
        ReflectiveTypeAdapterFactory.Adapter<T> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);

        try {
            adapter.write(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}