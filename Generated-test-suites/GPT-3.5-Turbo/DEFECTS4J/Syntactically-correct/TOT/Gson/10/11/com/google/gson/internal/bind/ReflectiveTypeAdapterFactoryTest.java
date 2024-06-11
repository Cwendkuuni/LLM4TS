import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    public void setUp() {
        Excluder excluder = Excluder.DEFAULT;
        FieldNamingStrategy fieldNamingStrategy = field -> {
            SerializedName annotation = field.getAnnotation(SerializedName.class);
            if (annotation != null) {
                return annotation.value();
            } else {
                return field.getName();
            }
        };
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(Collections.emptyMap()),
                fieldNamingStrategy,
                excluder
        );
    }

    @Test
    public void testCreateWithPrimitiveType_shouldReturnNullTypeAdapter() {
        Gson gson = new GsonBuilder().create();
        TypeToken<Integer> primitiveTypeToken = TypeToken.get(int.class);
        assertNull(reflectiveTypeAdapterFactory.create(gson, primitiveTypeToken));
    }

    @Test
    public void testCreateWithObjectType_shouldReturnAdapterWithConstructorAndBoundFields() {
        Gson gson = new GsonBuilder().create();
        TypeToken<TestClass> objectTypeToken = TypeToken.get(TestClass.class);
        Adapter<TestClass> adapter = (Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, objectTypeToken);
        assertNotNull(adapter);
        assertNotNull(adapter.constructor);
        assertNotNull(adapter.boundFields);
    }

    @Test
    public void testCreateWithNullGson_shouldReturnNullTypeAdapter() {
        Gson gson = null;
        TypeToken<TestClass> objectTypeToken = TypeToken.get(TestClass.class);
        assertNull(reflectiveTypeAdapterFactory.create(gson, objectTypeToken));
    }

    @Test
    public void testCreateWithNullTypeToken_shouldReturnNullTypeAdapter() {
        Gson gson = new GsonBuilder().create();
        TypeToken<TestClass> nullTypeToken = null;
        assertNull(reflectiveTypeAdapterFactory.create(gson, nullTypeToken));
    }

    @Test
    public void testCreateWithInterfaceType_shouldReturnAdapterWithConstructorAndNoBoundFields() {
        Gson gson = new GsonBuilder().create();
        TypeToken<InterfaceClass> interfaceTypeToken = TypeToken.get(InterfaceClass.class);
        Adapter<InterfaceClass> adapter = (Adapter<InterfaceClass>) reflectiveTypeAdapterFactory.create(gson, interfaceTypeToken);
        assertNotNull(adapter);
        assertNotNull(adapter.constructor);
        assertTrue(adapter.boundFields.isEmpty());
    }

    @Test
    public void testCreateWithSuperclassType_shouldReturnAdapterWithConstructorAndBoundFields() {
        Gson gson = new GsonBuilder().create();
        TypeToken<SubclassClass> superclassTypeToken = TypeToken.get(SubclassClass.class);
        Adapter<SubclassClass> adapter = (Adapter<SubclassClass>) reflectiveTypeAdapterFactory.create(gson, superclassTypeToken);
        assertNotNull(adapter);
        assertNotNull(adapter.constructor);
        assertFalse(adapter.boundFields.isEmpty());
    }

    @Test
    public void testBoundFieldWriteField_shouldReturnFalseForNonSerializedField() throws NoSuchFieldException {
        Gson gson = new GsonBuilder().create();
        TypeToken<TestClass> objectTypeToken = TypeToken.get(TestClass.class);
        Adapter<TestClass> adapter = (Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, objectTypeToken);
        TestClass testObject = new TestClass();
        Field nonSerializedField = testObject.getClass().getDeclaredField("nonSerializedField");
        BoundField boundField = adapter.boundFields.get(nonSerializedField.getName());
        assertFalse(boundField.writeField(testObject));
    }

    @Test
    public void testBoundFieldWriteField_shouldReturnTrueForSerializedField() throws NoSuchFieldException {
        Gson gson = new GsonBuilder().create();
        TypeToken<TestClass> objectTypeToken = TypeToken.get(TestClass.class);
        Adapter<TestClass> adapter = (Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, objectTypeToken);
        TestClass testObject = new TestClass();
        Field serializedField = testObject.getClass().getDeclaredField("serializedField");
        BoundField boundField = adapter.boundFields.get(serializedField.getName());
        assertTrue(boundField.writeField(testObject));
    }

    @Test
    public void testAdapterReadWithNullReader_shouldReturnNull() throws IOException {
        Gson gson = new GsonBuilder().create();
        TypeToken<TestClass> objectTypeToken = TypeToken.get(TestClass.class);
        Adapter<TestClass> adapter = (Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, objectTypeToken);
        assertNull(adapter.read(null));
    }

    @Test
    public void testAdapterWriteWithNullWriter_shouldWriteNullValue() throws IOException {
        Gson gson = new GsonBuilder().create();
        TypeToken<TestClass> objectTypeToken = TypeToken.get(TestClass.class);
        Adapter<TestClass> adapter = (Adapter<TestClass>) reflectiveTypeAdapterFactory.create(gson, objectTypeToken);
        NullWriter writer = new NullWriter();
        adapter.write(writer, null);
        assertEquals("null", writer.toString());
    }

    private static class TestClass {
        @SerializedName("field1")
        private String serializedField;
        private String nonSerializedField;
    }

    private interface InterfaceClass {}

    private static class SuperclassClass {
        private String superclassField;
    }

    private static class SubclassClass extends SuperclassClass {
        private String subclassField;
    }

    private static class NullWriter extends Writer {
        private StringBuilder builder = new StringBuilder();

        @Override
        public void write(char[] cbuf, int off, int len) {
            builder.append(cbuf, off, len);
        }

        @Override
        public void flush() {}

        @Override
        public void close() {}

        @Override
        public String toString() {
            return builder.toString();
        }
    }
}