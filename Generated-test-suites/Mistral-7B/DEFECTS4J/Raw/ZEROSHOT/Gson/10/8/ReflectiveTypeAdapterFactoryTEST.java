import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testReflectiveTypeAdapterFactory() {
        // Test constructor
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingStrategy fieldNamingPolicy = new FieldNamingPolicy();
        Excluder excluder = new Excluder();
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        // Test excludeField method
        Class testClass = TestClass.class;
        Field testField = testClass.getDeclaredField("testField");
        Assert.assertTrue(reflectiveTypeAdapterFactory.excludeField(testField, true));
        Assert.assertFalse(reflectiveTypeAdapterFactory.excludeField(testField, false));

        // Test getFieldNames method
        Field testSerializedNameField = testClass.getDeclaredField("testSerializedNameField");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(testSerializedNameField);
        Assert.assertEquals(2, fieldNames.size());
        Assert.assertEquals("testSerializedName", fieldNames.get(0));
        Assert.assertEquals("testAlternateName", fieldNames.get(1));

        // Test create method
        TypeToken testTypeToken = new TypeToken<TestClass>() {};
        TypeAdapter<TestClass> typeAdapter = reflectiveTypeAdapterFactory.create(new Gson(), (TypeToken<TestClass>) testTypeToken);
        Assert.assertNotNull(typeAdapter);

        // Test write and read methods
        TestClass testObject = new TestClass();
        testObject.testField = 123;
        testObject.testSerializedNameField = "testSerializedNameValue";
        testObject.testAlternateNameField = "testAlternateNameValue";

        JsonWriter jsonWriter = new JsonWriter(System.out);
        jsonWriter.setLenient(true);
        typeAdapter.write(jsonWriter, testObject);
        jsonWriter.flush();

        JsonReader jsonReader = new JsonReader(new StringReader("{\"testSerializedName\":\"testSerializedNameValue\", \"testAlternateName\":\"testAlternateNameValue\"}"));
        TestClass deserializedObject = typeAdapter.read(jsonReader);

        Assert.assertEquals(testObject.testField, deserializedObject.testField);
        Assert.assertEquals(testObject.testSerializedNameField.get(0), deserializedObject.testSerializedNameField.get(0));
        Assert.assertEquals(testObject.testAlternateNameField.get(0), deserializedObject.testAlternateNameField.get(0));

        // Test writeField method
        try {
            Assert.assertFalse(reflectiveTypeAdapterFactory.boundFieldWriteField(testObject, "nonExistentField"));
        } catch (IllegalAccessException e) {
            Assert.fail("Exception should not be thrown for non-existent field");
        }

        try {
            Assert.assertTrue(reflectiveTypeAdapterFactory.boundFieldWriteField(testObject, "testField"));
        } catch (IllegalAccessException e) {
            Assert.fail("Exception should not be thrown for existing field");
        }

        // Test getBoundFields method
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(new Gson(), new TypeToken<TestClass>() {}, TestClass.class);
        Assert.assertEquals(4, boundFields.size());

        ReflectiveTypeAdapterFactory.BoundField testFieldBoundField = boundFields.get("testField");
        Assert.assertEquals("testField", testFieldBoundField.name);
        Assert.assertTrue(testFieldBoundField.serialized);
        Assert.assertTrue(testFieldBoundField.deserialized);

        ReflectiveTypeAdapterFactory.BoundField testSerializedNameFieldBoundField = boundFields.get("testSerializedNameField");
        Assert.assertEquals("testSerializedName", testSerializedNameFieldBoundField.name);
        Assert.assertTrue(testSerializedNameFieldBoundField.serialized);
        Assert.assertTrue(testSerializedNameFieldBoundField.deserialized);

        ReflectiveTypeAdapterFactory.BoundField testAlternateNameFieldBoundField = boundFields.get("testAlternateNameField");
        Assert.assertEquals("testAlternateName", testAlternateNameFieldBoundField.name);
        Assert.assertTrue(testAlternateNameFieldBoundField.serialized);
        Assert.assertTrue(testAlternateNameFieldBoundField.deserialized);

        ReflectiveTypeAdapterFactory.BoundField nonExistentFieldBoundField = boundFields.get("nonExistentField");
        Assert.assertNull(nonExistentFieldBoundField);
    }

    static class TestClass {
        int testField = 123;

        @SerializedName("testSerializedName")
        String testSerializedNameField = "testSerializedNameValue";

        @SerializedName({"testAlternateName", "alternateName"})
        String testAlternateNameField = "testAlternateNameValue";

        public TestClass() {}
    }

    static class Excluder {
        public boolean excludeClass(Class clazz, boolean serialize) {
            return false;
        }

        public boolean excludeField(Field field, boolean serialize) {
            return false;
        }
    }

    static class FieldNamingPolicy {
        public String translateName(Field field) {
            return field.getName();
        }
    }
}