import com.google.gson.FieldNamingStrategy;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testExcludeField_Serialize_Excluded() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, new Excluder());
        Field field = getField("excludeField");
        boolean actual = factory.excludeField(field, true);
        assertTrue(actual);
    }

    @Test
    public void testExcludeField_Serialize_NotExcluded() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, new Excluder());
        Field field = getField("notExcludedField");
        boolean actual = factory.excludeField(field, true);
        assertFalse(actual);
    }

    @Test
    public void testExcludeField_Deserialize_Excluded() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, new Excluder());
        Field field = getField("excludeField");
        boolean actual = factory.excludeField(field, false);
        assertTrue(actual);
    }

    @Test
    public void testExcludeField_Deserialize_NotExcluded() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, new Excluder());
        Field field = getField("notExcludedField");
        boolean actual = factory.excludeField(field, false);
        assertFalse(actual);
    }

    private Field getField(String fieldName) {
        try {
            return ReflectiveTypeAdapterFactory.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testGetFieldNames_NoSerializedNameAnnotation() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = getField("noAnnotationField");
        List<String> actual = factory.getFieldNames(field);
        List<String> expected = Collections.singletonList("noAnnotationField");
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFieldNames_SerializedNameAnnotation() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = getField("serializedNameField");
        List<String> actual = factory.getFieldNames(field);
        List<String> expected = Collections.singletonList("customName");
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFieldNames_SerializedNameWithAlternatesAnnotation() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(null, null, null);
        Field field = getField("serializedNameWithAlternatesField");
        List<String> actual = factory.getFieldNames(field);
        List<String> expected = new ArrayList<>();
        expected.add("customName");
        expected.add("alternateName1");
        expected.add("alternateName2");
        assertEquals(expected, actual);
    }

    @Test
    public void testCreate_PrimitiveRawType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(null, null, null);
        TypeAdapter<String> actual = factory.create(null, TypeToken.get(String.class));
        assertNull(actual);
    }

    @Test
    public void testCreate_NonPrimitiveRawType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<String> actual = factory.create(null, TypeToken.get(String.class));
        assertNotNull(actual);
    }

    @Test
    public void testCreate_NullType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<String> actual = factory.create(null, TypeToken.get(null));
        assertNull(actual);
    }

    @Test
    public void testCreate_ObjectType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<Object> actual = factory.create(null, TypeToken.get(Object.class));
        assertNotNull(actual);
    }

    @Test
    public void testCreate_CustomObjectType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<CustomObject> actual = factory.create(null, TypeToken.get(CustomObject.class));
        assertNotNull(actual);
    }

    @Test
    public void testCreate_CustomObjectTypeWithJsonAdapter() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<CustomObjectWithJsonAdapter> actual = factory.create(null, TypeToken.get(CustomObjectWithJsonAdapter.class));
        assertNotNull(actual);
    }

    @Test
    public void testCreate_CustomObjectWithGenericType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<CustomObjectWithGenericType<Integer>> actual = factory.create(null, TypeToken.getParameterized(CustomObjectWithGenericType.class, Integer.class));
        assertNotNull(actual);
    }

    @Test
    public void testCreate_GenericArrayType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<CustomObject[]> actual = factory.create(null, TypeToken.get(CustomObject[].class));
        assertNull(actual);
    }

    @Test
    public void testCreate_BoundedType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<BoundedClass> actual = factory.create(null, TypeToken.get(BoundedClass.class));
        assertNull(actual);
    }

    @Test
    public void testCreate_SuperType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<SuperClass> actual = factory.create(null, TypeToken.get(SuperClass.class));
        assertNull(actual);
    }

    @Test
    public void testCreate_SubType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<SubClass> actual = factory.create(null, TypeToken.get(SubClass.class));
        assertNull(actual);
    }

    @Test
    public void testCreate_InterfaceType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<InterfaceClass> actual = factory.create(null, TypeToken.get(InterfaceClass.class));
        assertNull(actual);
    }

    @Test
    public void testCreate_GenericType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<GenericType<Integer>> actual = factory.create(null, TypeToken.getParameterized(GenericType.class, Integer.class));
        assertNull(actual);
    }

    @Test
    public void testCreate_BoundedGenericType() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, null);
        TypeAdapter<BoundedGenericType<Integer>> actual = factory.create(null, TypeToken.getParameterized(BoundedGenericType.class, Integer.class));
        assertNull(actual);
    }

    private static class CustomObject {}
    private static class CustomObjectWithJsonAdapter {}
    private static class CustomObjectWithGenericType<T> {}
    private static class CustomObjectArray<T> {}
    private static class BoundedClass<T extends Number> {}
    private static class SuperClass {}
    private static class SubClass extends SuperClass {}
    private interface InterfaceClass {}
    private static class GenericType<T> {}
    private static class BoundedGenericType<T extends Number> {}

    /* Test field */
    public int excludeField;
    public int notExcludedField;

    /* Test field names */
    public String noAnnotationField;
    @SerializedName("customName")
    public String serializedNameField;
    @SerializedName(value = "customName", alternate = {"alternateName1", "alternateName2"})
    public String serializedNameWithAlternatesField;

    /* Test adapter */
    public static class TestAdapter extends TypeAdapter<Object> {
        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            // Do nothing
        }

        @Override
        public Object read(JsonReader in) throws IOException {
            return null;
        }
    }

}