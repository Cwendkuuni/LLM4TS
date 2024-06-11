import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReflectiveTypeAdapterFactoryTest {

    @Mock
    private ConstructorConstructor constructorConstructor;
    @Mock
    private FieldNamingStrategy fieldNamingPolicy;
    @Mock
    private Excluder excluder;
    @InjectMocks
    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void excludeField_notExcluded_returnsFalse() {
        Field field = getFieldFromClass(Foo.class, "bar");
        boolean excluded = ReflectiveTypeAdapterFactory.excludeField(field, true, excluder);
        assertEquals(false, excluded);
    }

    @Test
    public void excludeField_excludedClass_returnsTrue() {
        when(excluder.excludeClass(eq(String.class), eq(true))).thenReturn(true);
        Field field = getFieldFromClass(Bar.class, "foo");
        boolean excluded = ReflectiveTypeAdapterFactory.excludeField(field, true, excluder);
        assertEquals(true, excluded);
    }

    @Test
    public void excludeField_excludedField_returnsTrue() {
        when(excluder.excludeClass(eq(String.class), eq(true))).thenReturn(false);
        when(excluder.excludeField(any(Field.class), eq(true))).thenReturn(true);
        Field field = getFieldFromClass(Bar.class, "foo");
        boolean excluded = ReflectiveTypeAdapterFactory.excludeField(field, true, excluder);
        assertEquals(true, excluded);
    }

    @Test
    public void getFieldNames_noSerializedNameAnnotation_returnsDefaultName() {
        Field field = getFieldFromClass(Baz.class, "qux");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(Collections.singletonList("qux"), fieldNames);
    }

    @Test
    public void getFieldNames_serializedNameAnnotation_returnsSerializedName() {
        Field field = getFieldFromClass(Baz.class, "quux");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        assertEquals(Collections.singletonList("Fred"), fieldNames);
    }

    @Test
    public void getFieldNames_serializedNameAnnotationWithAlternates_returnsAllNames() {
        Field field = getFieldFromClass(Baz.class, "corge");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Garply");
        expectedNames.add("Waldo");
        expectedNames.add("Plugh");
        assertEquals(expectedNames, fieldNames);
    }

    @Test
    public void create_notAssignableRawType_returnsNull() {
        Gson gson = mock(Gson.class);
        TypeToken<String> typeToken = new TypeToken<String>() {};
        TypeAdapterFactory factory = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertEquals(null, factory);
    }

    @Test
    public void create_primitiveType_returnsNull() {
        Gson gson = mock(Gson.class);
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
        TypeAdapterFactory factory = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertEquals(null, factory);
    }

    // TODO: Add test cases for create with valid raw types

    // TODO: Add test cases for create with invalid raw types

    @Test
    public void createBoundField_noJsonAdapterAnnotation_returnsBoundFieldWithNoAdapter() {
        Gson gson = mock(Gson.class);
        Field field = getFieldFromClass(BoundFieldClass.class, "fieldWithoutAnnotation");
        TypeToken<?> fieldType = TypeToken.get(String.class);
        boolean serialize = true;
        boolean deserialize = false;
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, "fieldWithoutAnnotation", fieldType, serialize, deserialize);
        assertEquals(false, boundField.jsonAdapterPresent);
        assertEquals(null, boundField.typeAdapter);
    }

    // TODO: Add test cases for createBoundField with JsonAdapter annotation

    // TODO: Add test cases for createBoundField with mapped TypeAdapter

    private Field getFieldFromClass(Class<?> clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return field;
    }

    private static class Foo {
        private String bar;
    }

    private static class Bar {
        @SerializedName("foo")
        private String foo;
    }

    private static class Baz {
        private String qux;
        @SerializedName("Fred")
        private String quux;
        @SerializedName(value = "Garply", alternate = {"Waldo", "Plugh"})
        private String corge;
    }

    private static class BoundFieldClass {
        private String fieldWithoutAnnotation;
    }
}