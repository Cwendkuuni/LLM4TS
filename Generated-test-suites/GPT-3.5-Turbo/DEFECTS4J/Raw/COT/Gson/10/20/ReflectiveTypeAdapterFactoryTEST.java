
import com.google.gson.*;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setUp() {
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, new Excluder());
    }
    
    // Test for excludeField(Field f, boolean serialize)
    @Test
    public void excludeField_excludeClassAndField_shouldReturnTrue() {
        Field field = getFieldFromClass(ReflectiveTypeAdapterFactory.class, "constructorConstructor");
        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, true);
        assertTrue(result);
    }
    
    @Test
    public void excludeField_includeClassAndField_shouldReturnFalse() {
        Field field = getFieldFromClass(ReflectiveTypeAdapterFactory.class, "excluder");
        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, true);
        assertFalse(result);
    }
    
    // Test for getFieldNames(Field f)
    @Test
    public void getFieldNames_noSerializedNameAnnotation_shouldReturnSingleDefaultName() {
        Field field = getFieldFromClass(ReflectiveTypeAdapterFactory.class, "constructorConstructor");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, fieldNames.size());
        assertEquals("constructorConstructor", fieldNames.get(0));
    }
    
    @Test
    public void getFieldNames_withSerializedNameAnnotationWithSingleValue_shouldReturnSingleSerializedName() {
        Field field = getFieldFromClass(ReflectiveTypeAdapterFactory.class, "fieldNamingPolicy");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(1, fieldNames.size());
        assertEquals("fieldNamingPolicy", fieldNames.get(0));
    }
    
    @Test
    public void getFieldNames_withSerializedNameAnnotationWithMultipleValues_shouldReturnSerializedNameAndAlternates() {
        Field field = getFieldFromClass(ReflectiveTypeAdapterFactory.class, "excluder");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(3, fieldNames.size());
        assertEquals("excluder", fieldNames.get(0));
        assertEquals("excluder1", fieldNames.get(1));
        assertEquals("excluder2", fieldNames.get(2));
    }
    
    // Test for create(Gson gson, final TypeToken<T> type)
    @Test
    public void create_primitiveType_shouldReturnNull() {
        TypeToken<Integer> typeToken = TypeToken.get(Integer.TYPE);
        TypeAdapter<Integer> typeAdapter = reflectiveTypeAdapterFactory.create(new Gson(), typeToken);
        assertNull(typeAdapter);
    }
    
    @Test
    public void create_interfaceType_shouldReturnEmptyMap() {
        TypeToken<Map> typeToken = TypeToken.get(Map.class);
        TypeAdapter<Map> typeAdapter = reflectiveTypeAdapterFactory.create(new Gson(), typeToken);
        assertNotNull(typeAdapter);
        assertEquals(0, reflectiveTypeAdapterFactory.getBoundFields(new Gson(), typeToken, Map.class).size());
    }
    
    // Helper method to get Field object from a class
    private Field getFieldFromClass(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

}
