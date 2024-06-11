import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
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

    private Gson gson;
    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingStrategy fieldNamingStrategy = new FieldNamingStrategy() {
            @Override
            public String translateName(Field field) {
                return field.getName();
            }
        };
        Excluder excluder = new Excluder();
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
        gson = new Gson();
    }

    @Test
    public void testExcludeField_withExcludedType_ReturnFalse() throws Exception {
        Field field = getClass().getDeclaredField("field");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertFalse(result);
    }

    @Test
    public void testExcludeField_withIncludedType_ReturnTrue() throws Exception {
        Field field = getClass().getDeclaredField("includedField");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertTrue(result);
    }

    @Test
    public void testCreate_withPrimitiveType_ReturnNull() {
        TypeAdapter<String> result = reflectiveTypeAdapterFactory.create(gson, new TypeToken<String>() {});
        assertNull(result);
    }

    @Test
    public void testCreate_withObjectType_ReturnTypeAdapter() {
        TypeAdapter<Object> result = reflectiveTypeAdapterFactory.create(gson, new TypeToken<Object>() {});
        assertNotNull(result);
    }

    @Test
    public void testCreate_withInterfaceType_ReturnNull() {
        TypeAdapter<List<String>> result = reflectiveTypeAdapterFactory.create(gson, new TypeToken<List<String>>() {});
        assertNull(result);
    }

    @Test
    public void testCreateBoundField_withJsonAdapterAnnotation_ReturnBoundFieldWithMappedTypeAdapter() throws Exception {
        Gson context = gson;
        Field field = getClass().getDeclaredField("field");
        String name = "field";
        TypeToken<?> fieldType = TypeToken.get(String.class);
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField expectedBoundField = new ReflectiveTypeAdapterFactory.BoundField(name, serialize, deserialize) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                Object fieldValue = field.get(value);
                TypeAdapter t =
                        new TypeAdapterRuntimeTypeWrapper(context, gson, fieldType.getType());
                t.write(writer, fieldValue);
            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
                Object fieldValue = gson.fromJson(reader, fieldType.getType());
                if (fieldValue != null || !Primitives.isPrimitive(fieldType.getRawType())) {
                    field.set(value, fieldValue);
                }
            }

            @Override
            public boolean writeField(Object value) throws IOException, IllegalAccessException {
                if (!serialized) return false;
                Object fieldValue = field.get(value);
                return fieldValue != value;
            }
        };

        ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(context, field, name, fieldType, serialize, deserialize);
        assertEquals(expectedBoundField.name, result.name);
        assertEquals(expectedBoundField.serialized, result.serialized);
        assertEquals(expectedBoundField.deserialized, result.deserialized);
        assertEquals(expectedBoundField.getClass(), result.getClass());
    }

    @Test
    public void testCreateBoundField_withoutJsonAdapterAnnotation_ReturnBoundFieldWithTypeAdapter() throws Exception {
        Gson context = gson;
        Field field = getClass().getDeclaredField("includedField");
        String name = "includedField";
        TypeToken<?> fieldType = TypeToken.get(String.class);
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField expectedBoundField = new ReflectiveTypeAdapterFactory.BoundField(name, serialize, deserialize) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                Object fieldValue = field.get(value);
                TypeAdapter t =
                        new TypeAdapterRuntimeTypeWrapper(context, gson, fieldType.getType());
                t.write(writer, fieldValue);
            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
                Object fieldValue = gson.fromJson(reader, fieldType.getType());
                if (fieldValue != null || !Primitives.isPrimitive(fieldType.getRawType())) {
                    field.set(value, fieldValue);
                }
            }

            @Override
            public boolean writeField(Object value) throws IOException, IllegalAccessException {
                if (!serialized) return false;
                Object fieldValue = field.get(value);
                return fieldValue != value;
            }
        };

        ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(context, field, name, fieldType, serialize, deserialize);
        assertEquals(expectedBoundField.name, result.name);
        assertEquals(expectedBoundField.serialized, result.serialized);
        assertEquals(expectedBoundField.deserialized, result.deserialized);
        assertEquals(expectedBoundField.getClass(), result.getClass());
    }

    private String field;
    @JsonAdapter(StringTypeAdapter.class)
    private String includedField;

    static class StringTypeAdapter extends TypeAdapter<String> {

        @Override
        public void write(JsonWriter out, String value) throws IOException {
            out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            return in.nextString();
        }
    }

}