
import com.google.gson.Gson;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

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
    public void excludeField_shouldExcludeFieldBasedOnTypeAndSerializeParameter() throws Exception {
        // Arrange
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        boolean serialize = true;

        // Act
        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, serialize, reflectiveTypeAdapterFactory.excluder);

        // Assert
        assertFalse(result);
    }

    @Test
    public void getFieldNames_shouldReturnSingleDefaultName() throws Exception {
        // Arrange
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");

        // Act
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);

        // Assert
        assertEquals(1, fieldNames.size());
    }

    @Test
    public void getFieldNames_shouldReturnSerializedNameAndAlternates() throws Exception {
        // Arrange
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        field.setAccessible(true);
        SerializedName serializedName = field.getAnnotation(SerializedName.class);
        serializedName.value();
        String[] alternates = serializedName.alternate();

        // Act
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);

        // Assert
        assertEquals(alternates.length + 1, fieldNames.size());
    }

    @Test
    public void create_shouldReturnNullForNonAssignableType() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<Integer> typeToken = TypeToken.get(Integer.class);

        // Act
        TypeAdapter<Integer> result = reflectiveTypeAdapterFactory.create(gson, typeToken);

        // Assert
        assertNull(result);
    }

    @Test
    public void create_shouldReturnAdapterForAssignableType() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<Object> typeToken = TypeToken.get(Object.class);

        // Act
        TypeAdapter<?> result = reflectiveTypeAdapterFactory.create(gson, typeToken);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void create_shouldCreateAdapterWithConstructorAndBoundFields() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<Object> typeToken = TypeToken.get(Object.class);

        // Act
        TypeAdapter<?> result = reflectiveTypeAdapterFactory.create(gson, typeToken);

        // Assert
        assertNotNull(result);
        assertSame(constructorConstructor.get(typeToken), result.constructor);
        assertEquals(getBoundFields(gson, typeToken, typeToken.getRawType()), result.boundFields);
    }

    private Map<String, ReflectiveTypeAdapterFactory.BoundField> getBoundFields(Gson context, TypeToken<?> type, Class<?> raw) {
        Map<String, ReflectiveTypeAdapterFactory.BoundField> result = new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>();
        if (raw.isInterface()) {
            return result;
        }

        Type declaredType = type.getType();
        while (raw != Object.class) {
            Field[] fields = raw.getDeclaredFields();
            for (Field field : fields) {
                boolean serialize = ReflectiveTypeAdapterFactory.excludeField(field, true);
                boolean deserialize = ReflectiveTypeAdapterFactory.excludeField(field, false);
                if (!serialize && !deserialize) {
                    continue;
                }
                field.setAccessible(true);
                Type fieldType = $Gson$Types.resolve(type.getType(), raw, field.getGenericType());
                List<String> fieldNames = getFieldNames(field);
                ReflectiveTypeAdapterFactory.BoundField previous = null;
                for (int i = 0; i < fieldNames.size(); ++i) {
                    String name = fieldNames.get(i);
                    if (i != 0) serialize = false; // only serialize the default name
                    ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(context, field, name,
                            TypeToken.get(fieldType), serialize, deserialize);
                    ReflectiveTypeAdapterFactory.BoundField replaced = result.put(name, boundField);
                    if (previous == null) previous = replaced;
                }
                if (previous != null) {
                    throw new IllegalArgumentException(declaredType
                            + " declares multiple JSON fields named " + previous.name);
                }
            }
            type = TypeToken.get($Gson$Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
            raw = type.getRawType();
        }
        return result;
    }
}
