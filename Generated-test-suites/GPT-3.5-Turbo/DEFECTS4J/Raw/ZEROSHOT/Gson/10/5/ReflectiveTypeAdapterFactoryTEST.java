import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReflectiveTypeAdapterFactoryTest {

    @Test
    void excludeField() {
        // Create an instance of ReflectiveTypeAdapterFactory
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());

        // Test excludeField method with a field that needs to be serialized
        Field serializeField = new Field("fieldName", null, null, null);
        assertFalse(reflectiveTypeAdapterFactory.excludeField(serializeField, true));

        // Test excludeField method with a field that needs to be deserialized
        Field deserializeField = new Field("fieldName", null, null, null);
        assertFalse(reflectiveTypeAdapterFactory.excludeField(deserializeField, false));
    }

    @Test
    void getFieldNames() {
        // Create an instance of ReflectiveTypeAdapterFactory
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());

        // Test getFieldNames method with a field that does not have SerializedName annotation
        Field normalField = new Field("normalFieldName", null, null, null);
        List<String> normalFieldNames = reflectiveTypeAdapterFactory.getFieldNames(normalField);
        assertEquals(Collections.singletonList("normalFieldName"), normalFieldNames);

        // Test getFieldNames method with a field that has SerializedName annotation
        Field serializedNameField = new Field("serializedNameField", null, null, null);
        SerializedName serializedNameAnnotation = new SerializedName() {
            @Override
            public String value() {
                return "serializedFieldName";
            }

            @Override
            public String[] alternate() {
                return new String[0];
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };
        serializedNameField.setAnnotation(serializedNameAnnotation);
        List<String> serializedNameFieldNames = reflectiveTypeAdapterFactory.getFieldNames(serializedNameField);
        assertEquals(Collections.singletonList("serializedFieldName"), serializedNameFieldNames);
    }

    @Test
    void create() {
        // Create an instance of Gson
        Gson gson = new Gson();

        // Create an instance of ReflectiveTypeAdapterFactory
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());

        // Create a TypeToken
        TypeToken<String> typeToken = TypeToken.get(String.class);

        // Test create method with a non-primitive type
        TypeAdapter<String> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNotNull(typeAdapter);
    }

    @Test
    void create_primitiveType() {
        // Create an instance of Gson
        Gson gson = new Gson();

        // Create an instance of ReflectiveTypeAdapterFactory
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());

        // Create a TypeToken for an int
        TypeToken<Integer> typeToken = TypeToken.get(int.class);

        // Test create method with a primitive type
        TypeAdapter<Integer> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNull(typeAdapter);
    }

    @Test
    void create_boundFieldsNotEmpty() {
        // Create an instance of Gson
        Gson gson = new Gson();

        // Create an instance of ReflectiveTypeAdapterFactory
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());

        // Create a TypeToken
        TypeToken<TestObject> typeToken = new TypeToken<TestObject>() {};

        // Test create method with bound fields not empty
        TypeAdapter<TestObject> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNotNull(typeAdapter);
    }

    static class TestObject {
        private String field1;
        private int field2;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public int getField2() {
            return field2;
        }

        public void setField2(int field2) {
            this.field2 = field2;
        }
    }

    @Test
    void createBoundField() {
        // Create an instance of Gson
        Gson gson = new Gson();

        // Create an instance of ReflectiveTypeAdapterFactory
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());

        // Create a Field
        Field field = new Field("fieldName", null, null, null);

        // Create a TypeToken
        TypeToken<String> typeToken = TypeToken.get(String.class);

        // Test createBoundField method
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, "fieldName", typeToken, true, false);
        assertNotNull(boundField);
    }

    @Test
    void getBoundFields() {
        // Create an instance of Gson
        Gson gson = new Gson();

        // Create an instance of ReflectiveTypeAdapterFactory
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy(), new Excluder());

        // Create a TypeToken
        TypeToken<TestObject> typeToken = new TypeToken<TestObject>() {};

        // Test getBoundFields method
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, TestObject.class);
        assertFalse(boundFields.isEmpty());
    }

    @Test
    void Adapter() {
        // Create an instance of ObjectConstructor
        ObjectConstructor<TestObject> constructor = new ObjectConstructor<TestObject>() {
            @Override
            public TestObject construct() {
                return new TestObject();
            }
        };

        // Create a Map of BoundFields
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
        boundFields.put("field1", new ReflectiveTypeAdapterFactory.BoundField("field1", true, false) {
            @Override
            boolean writeField(Object value) throws IOException, IllegalAccessException {
                return false;
            }

            @Override
            void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {

            }

            @Override
            void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {

            }
        });

        // Create an instance of Adapter
        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(constructor, boundFields);
        assertNotNull(adapter);

        // Create an instance of JsonReader
        JsonReader jsonReader = new JsonReader(null);

        // Test read method
        assertThrows(JsonSyntaxException.class, () -> {
            adapter.read(jsonReader);
        });

        // Create an instance of JsonWriter
        JsonWriter jsonWriter = new JsonWriter(null);

        // Test write method
        assertDoesNotThrow(() -> {
            adapter.write(jsonWriter, new TestObject());
        });
    }

}