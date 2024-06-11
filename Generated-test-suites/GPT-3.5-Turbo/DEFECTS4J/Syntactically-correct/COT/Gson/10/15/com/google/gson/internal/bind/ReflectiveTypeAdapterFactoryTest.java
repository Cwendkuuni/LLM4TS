import org.junit.Assert;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testExcludeField() {
        ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);

        // Test serialization exclusion
        boolean serializeExcluded = adapterFactory.excludeField(TestClass.class.getField("serializeExcluded"), true);
        Assert.assertTrue(serializeExcluded);

        // Test deserialization exclusion
        boolean deserializeExcluded = adapterFactory.excludeField(TestClass.class.getField("deserializeExcluded"), false);
        Assert.assertTrue(deserializeExcluded);
    }

    @Test
    public void testGetFieldNames() throws NoSuchFieldException {
        ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        List<String> fieldNames = adapterFactory.getFieldNames(TestClass.class.getField("fieldWithAnnotation"));

        Assert.assertEquals(3, fieldNames.size());
        Assert.assertEquals("firstFieldName", fieldNames.get(0));
        Assert.assertEquals("secondFieldName", fieldNames.get(1));
        Assert.assertEquals("thirdFieldName", fieldNames.get(2));
    }

    @Test
    public void testCreate() {
        ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Gson gson = new Gson();
        TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

        TypeAdapter<TestClass> typeAdapter = adapterFactory.create(gson, typeToken);

        Assert.assertNotNull(typeAdapter);
    }

    @Test
    public void testCreateBoundField() throws NoSuchFieldException {
        ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Gson gson = new Gson();
        Field field = TestClass.class.getField("fieldWithAnnotation");
        TypeToken<?> fieldType = TypeToken.get(String.class);

        ReflectiveTypeAdapterFactory.BoundField boundField = adapterFactory.createBoundField(gson, field, "fieldName", fieldType, true, true);

        Assert.assertNotNull(boundField);
    }

    @Test
    public void testGetBoundFields() {
        ReflectiveTypeAdapterFactory adapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        Gson gson = new Gson();
        TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
        Class<?> raw = TestClass.class;

        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = adapterFactory.getBoundFields(gson, typeToken, raw);

        Assert.assertNotNull(boundFields);
        Assert.assertEquals(1, boundFields.size());
        Assert.assertTrue(boundFields.containsKey("fieldWithAnnotation"));
    }

    @Test
    public void testAdapterRead() throws IOException {
        ObjectConstructor<TestClass> constructor = new ObjectConstructor<TestClass>() {
            @Override
            public TestClass construct() {
                return new TestClass();
            }
        };
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new HashMap<>();
        ReflectiveTypeAdapterFactory.Adapter<TestClass> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);
        JsonReader jsonReader = new JsonReader(new StringReader("{}"));

        TestClass result = adapter.read(jsonReader);

        Assert.assertNotNull(result);
    }

    @Test
    public void testAdapterWrite() throws IOException {
        ObjectConstructor<TestClass> constructor = new ObjectConstructor<TestClass>() {
            @Override
            public TestClass construct() {
                return new TestClass();
            }
        };
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new HashMap<>();
        ReflectiveTypeAdapterFactory.Adapter<TestClass> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);
        JsonWriter jsonWriter = new JsonWriter(new StringWriter());
        TestClass value = new TestClass();

        adapter.write(jsonWriter, value);

        jsonWriter.flush(); //flush the writer
        Assert.assertNotEquals("null", jsonWriter.toString());
    }

    public static class TestClass {
        @SerializedName("fieldWithAnnotation")
        private String fieldWithAnnotation;

        @SerializedName("serializeExcluded")
        private String serializeExcluded;

        @SerializedName("deserializeExcluded")
        private String deserializeExcluded;
    }
}