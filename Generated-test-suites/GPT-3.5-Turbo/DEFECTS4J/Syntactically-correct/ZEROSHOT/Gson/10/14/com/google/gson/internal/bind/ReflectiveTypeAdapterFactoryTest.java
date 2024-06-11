import com.google.gson.Gson;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Assert;
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
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;


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
    public void excludeField_Serialize_ExcludeField() {
        Field field = getField("excludeField");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        Assert.assertTrue(result);
    }

    @Test
    public void excludeField_Deserialize_DoNotExcludeField() {
        Field field = getField("excludeField");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, false);
        Assert.assertFalse(result);
    }

    @Test
    public void getFieldNames_NoAnnotation_ReturnSingleFieldName() {
        Field field = getField("noAnnotationField");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        Assert.assertEquals(1, fieldNames.size());
        Assert.assertEquals("noAnnotationField", fieldNames.get(0));
    }

    @Test
    public void getFieldNames_SerializedNameAnnotation_ReturnSingleFieldName() {
        Field field = getField("serializedNameField");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        Assert.assertEquals(1, fieldNames.size());
        Assert.assertEquals("serializedFieldName", fieldNames.get(0));
    }

    @Test
    public void getFieldNames_SerializedNameAnnotationWithAlternates_ReturnFieldNamesWithAlternates() {
        Field field = getField("serializedNameWithAlternatesField");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        Assert.assertEquals(4, fieldNames.size());
        Assert.assertEquals("serializedFieldName", fieldNames.get(0));
        Assert.assertEquals("alternate1", fieldNames.get(1));
        Assert.assertEquals("alternate2", fieldNames.get(2));
        Assert.assertEquals("alternate3", fieldNames.get(3));
    }

    private Field getField(String fieldName) {
        Field field = null;
        try {
            field = ReflectiveTypeAdapterFactory.class.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return field;
    }
}