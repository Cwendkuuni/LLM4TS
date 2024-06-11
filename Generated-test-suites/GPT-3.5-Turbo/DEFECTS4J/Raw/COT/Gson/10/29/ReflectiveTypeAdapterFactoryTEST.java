
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.TypeAdapter;

public class ReflectiveTypeAdapterFactoryTest {

    @Mock
    ConstructorConstructor constructorConstructorMock;

    @Mock
    FieldNamingStrategy fieldNamingPolicyMock;

    @Mock
    Excluder excluderMock;

    TypeAdapter typeAdapter = Mockito.mock(TypeAdapter.class);

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    public ReflectiveTypeAdapterFactoryTest() {
        MockitoAnnotations.initMocks(this);
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructorMock,
                fieldNamingPolicyMock, excluderMock);
    }

    @Test
    public void testExcludeField_Serialize_Excluded_True() {
        // Arrange
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredFields()[0];
        boolean serialize = true;

        // Act
        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, serialize, excluderMock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testExcludeField_Deserialize_Excluded_False() {
        // Arrange
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredFields()[0];
        boolean serialize = false;

        // Act
        boolean result = ReflectiveTypeAdapterFactory.excludeField(field, serialize, excluderMock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetFieldNames_AnnotationNull_ReturnListWithTranslatedName() {
        // Arrange
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredFields()[0];

        // Act
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);

        // Assert
        assertEquals(1, fieldNames.size());
        assertEquals("constructorConstructor", fieldNames.get(0));
    }

    @Test
    public void testGetFieldNames_AlternatesEmpty_ReturnListWithSerializedName() {
        // Arrange
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredFields()[1];

        // Act
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);

        // Assert
        assertEquals(1, fieldNames.size());
        assertEquals("constructorConstructor", fieldNames.get(0));
    }

    @Test
    public void testGetFieldNames_AlternatesNotEmpty_ReturnListWithSerializedNameAndAlternates() {
        // Arrange
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredFields()[2];

        // Act
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);

        // Assert
        assertEquals(3, fieldNames.size());
        assertEquals("serializedName", fieldNames.get(0));
        assertEquals("alternate1", fieldNames.get(1));
        assertEquals("alternate2", fieldNames.get(2));
    }

    @Test
    public void testCreateBoundField_JsonAdapterNotNull_ReturnBoundFieldWithJsonAdapter() {
        // Arrange
        Gson context = new Gson();
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredFields()[0];
        TypeToken fieldType = TypeToken.get(field.getType());
        boolean serialize = true;
        boolean deserialize = true;

        // Act
        BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(context, field, "field",
                fieldType, serialize, deserialize);

        // Assert
        assertNotNull(boundField);
        assertTrue(boundField instanceof ReflectiveTypeAdapterFactory.BoundField);
    }

    @Test
    public void testCreateBoundField_JsonAdapterNull_ReturnBoundFieldWithTypeAdapterFromContext() {
        // Arrange
        Gson context = new Gson();
        Field field = ReflectiveTypeAdapterFactory.class.getDeclaredFields()[1];
        TypeToken fieldType = TypeToken.get(field.getType());
        boolean serialize = true;
        boolean deserialize = true;

        // Act
        BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(context, field, "field",
                fieldType, serialize, deserialize);

        // Assert
        assertNotNull(boundField);
        assertTrue(boundField instanceof ReflectiveTypeAdapterFactory.BoundField);
    }

    @Test
    public void testGetBoundFields() {
        // Arrange
        Gson context = new Gson();
        TypeToken type = TypeToken.get(ReflectiveTypeAdapterFactory.class);
        Class raw = type.getRawType();

        // Act
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
                reflectiveTypeAdapterFactory.getBoundFields(context, type, raw);

        // Assert
        assertNotNull(boundFields);
        assertEquals(4, boundFields.size());
    }

    @Test
    public void testAdapterConstructor() {
        // Arrange
        ObjectConstructor constructor = Mockito.mock(ObjectConstructor.class);
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
                new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>();

        // Act
        ReflectiveTypeAdapterFactory.Adapter adapter =
                new ReflectiveTypeAdapterFactory.Adapter(constructor, boundFields);

        // Assert
        assertNotNull(adapter);
        assertTrue(adapter instanceof ReflectiveTypeAdapterFactory.Adapter);
    }

    @Test
    public void testRead_NullValue_ReturnNull() throws IOException {
        // Arrange
        JsonReader in = new JsonReader(new StringReader("null"));

        // Act
        Object result = reflectiveTypeAdapterFactory.read(in);

        // Assert
        assertNull(result);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testRead_IllegalStateException_ThrowJsonSyntaxException() throws IOException {
        // Arrange
        JsonReader in = new JsonReader(new StringReader("illegal"));

        // Act
        reflectiveTypeAdapterFactory.read(in);
    }

    @Test
    public void testWrite_NullValue_WriteNull() throws IOException {
        // Arrange
        JsonWriter out = new JsonWriter(new StringWriter());
        Object value = null;

        // Act
        reflectiveTypeAdapterFactory.write(out, value);
        out.close();

        // Assert
        assertEquals("null", out.toString());
    }

    @Test
    public void testWrite_NotNullValue_WriteJsonObject() throws IOException {
        // Arrange
        JsonWriter out = new JsonWriter(new StringWriter());
        Object value = new ReflectiveTypeAdapterFactory();

        // Act
        reflectiveTypeAdapterFactory.write(out, value);
        out.close();

        // Assert
        assertTrue(out.toString().startsWith("{"));
        assertTrue(out.toString().endsWith("}"));
    }
}

