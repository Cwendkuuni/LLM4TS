import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setup() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingStrategy fieldNamingStrategy = new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return f.getName();
            }
        };
        Excluder excluder = new Excluder();
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    }

    @Test
    public void testExcludeField() {
        Field field = getField();
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertEquals(false, result);
    }

    @Test
    public void testGetFieldNames() {
        Field field = getField();
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertNotNull(fieldNames);
        assertEquals(1, fieldNames.size());
        assertEquals("testField", fieldNames.get(0));
    }

    private Field getField() {
        try {
            return TestObject.class.getDeclaredField("testField");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class TestObject {
        @SerializedName("testField")
        private String testField;
    }
}