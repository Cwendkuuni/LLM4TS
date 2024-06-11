import static org.junit.Assert.*;
import static com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

@RunWith(Parameterized.class)
public class ReflectiveTypeAdapterFactoryTest {

 private static final Class<?> TEST_CLASS = Object.class;
 private static final Excluder EXCLUDER = new Excluder();;
 private static final ConstructorConstructor CONSTRUCTOR_CONSTRUCTOR = new ConstructorConstructor();
 private static final FieldNamingStrategy FIELD_NAMING_POLICY = new FIeldNameAnnotationsNamingPolicy();
 private static final ReflectiveTypeAdapterFactory FACTORY = new ReflectiveTypeAdapterFactory(CONSTRUCTOR_CONSTRUCTOR, FIELD_NAMING_POLICY, EXCLUDER);

 @Parameters(name = "{0}")
 public static List<Object> data() {
 return new ArrayList<Object>() {{
 add(new TestClass1());
 add(new TestClass2());
 }};
 }

 @Test
 public void testCreate() throws Exception {
 TypeAdapter<?> adapter = FACTORY.create(new Gson(), new TypeToken<Object>() {{}});
 assertNotNull(adapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateNullTypeToken() {
 FACTORY.create(new Gson(), null);
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateInterfaceType() {
 TypeAdapter<?> adapter = FACTORY.create(new Gson(), new TypeToken<Object>() {{}});
 assertNotNull(adapter);

 Class<?> interfaceType = IInterface.class;
 assertNull(FACTORY.create(new Gson(), new TypeToken<Object>() {{setType(interfaceType);}}));
 }

 @Test
 public void testCreateClassWithFields() throws Exception {
 Object instance = new TestClass1();
 TypeAdapter<?> adapter = FACTORY.create(new Gson(), new TypeToken<TestClass1>() {{}});
 assertNotNull(adapter);

 JsonObject json = new JsonObject();
 json.addProperty("intField", 1);
 json.addProperty("stringField", "test");
 json.add("arrayField", new JsonArray());
 json.add("listField", new JsonArray());
 json.add("mapField", new JsonObject());
 json.addProperty("booleanField", true);
 json.addProperty("doubleField", 3.14);
 json.add("customField", new JsonPrimitive(new CustomObject()));
 json.add("customAdapterField", new JsonPrimitive(new CustomAdapterObject()));
 json.addProperty("excludedField", null);
 json.addProperty("excludedBooleanField", false);
 json.addProperty("excludedDoubleField", 0.0);

 adapter.write(new JsonWriter(System.out), instance);
 adapter.read(new JsonReader(new StringReader(json.toString())), instance);

 assertSame(instance, adapter.read(new JsonReader(new StringReader(json.toString()))));
 }

 @Test(expected = JsonSyntaxException.class)
 public void testCreateClassWithInvalidJson() throws Exception {
 Object instance = new TestClass1();
 TypeAdapter<?> adapter = FACTORY.create(new Gson(), new TypeToken<TestClass1>() {{}});
 assertNotNull(adapter);

 JsonObject json = new JsonObject();
 json.addProperty("name", "invalidJson");

 adapter.write(new JsonWriter(System.out), instance);
 adapter.read(new JsonReader(new StringReader(json.toString())), instance);
 fail("Expected JsonSyntaxException, but got: " + instance);
 }

 @Test
 public void testExcludeField() {
 assertFalse(excludeField(new FieldTestClass1().field1, true));
 assertFalse(excludeField(new FieldTestClass1().field2, false));
 assertTrue(excludeField(new FieldTestClass1().field3, true));
 assertTrue(excludeField(new FieldTestClass1().field4, false));
 }

 @Test
 public void testGetFieldNames() throws Exception {
 TestClass1 testInstance = new TestClass1();
 Field[] fields = TestClass1.class.getDeclaredFields();
 for (Field field : fields) {
 String expected = getFieldNames(field).get(0);
 String actual = getFieldNames(FACTORY, TestClass1.class, field)[0];
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testGetBoundFields() throws Exception {
 TestClass1 testInstance = new TestClass1();
 Map<String, BoundField> expected = getBoundFields(new Gson(), new TypeToken<TestClass1>() {{}});
 Map<String, BoundField> actual = FACTORY.getBoundFields(new Gson(), new TypeToken<TestClass1>() {{}}, TestClass1.class);
 assertEquals(expected.size(), actual.size());
 for (Map.Entry<String, BoundField> entry : expected.entrySet()) {
 String name = entry.getKey();
 BoundField expectedField = entry.getValue();
 BoundField actualField = actual.get(name);
 assertEquals(expectedField, actualField);
 }
 }

 @Test
 public void testGetJsonAdapter() throws Exception {
 JsonAdapter jsonAdapterAnnotation = new TestJsonAdapterAnnotatedClass().field1.annotation(JsonAdapter.class);
 TypeAdapter<?> expected = getTypeAdapter(CONSTRUCTOR_CONSTRUCTOR, new Gson(), new TypeToken<Class<?>>() {{setType(jsonAdapterAnnotation.value().clazz());}}, jsonAdapterAnnotation);
 TypeAdapter<?> actual = FACTORY.getTypeAdapter(CONSTRUCTOR_CONSTRUCTOR, new Gson(), new TypeToken<FieldTestClass1>(){}.getDeclaredField("field1").getType(), jsonAdapterAnnotation);
 assertEquals(expected, actual);
 }

 private static String getFieldNames(Field field) {
 return String.join(", ", ReflectiveTypeAdapterFactory.getFieldNames(field).toArray(new String[0]));
 }

 private static String[] getFieldNames(ReflectiveTypeAdapterFactory factory, Class<?> clazz, Field field) throws IOException, IllegalAccessException {
 return factory.getFieldNames(field)[0].split(", ");
 }

 private static Map<String, BoundField> getBoundFields(Gson gson, TypeToken<?> type, Class<?> raw) {
 Map<String, BoundField> result = new LinkedHashMap<>();
 for (Field field : raw.getDeclaredFields()) {
 result.putAll(getBoundFields(gson, type, field, raw, field.getName()));
 }
 return result;
 }

 private static Map<String, BoundField> getBoundFields(Gson gson, TypeToken<?> type, Field field, Class<?> raw, String name) throws IOException, IllegalAccessException {
 Map<String, BoundField> result = new LinkedHashMap<>();
 BoundField boundField = new BoundField(name, true, false) {};
 result.put(name, boundField);
 return result;
 }

 private static class CustomObject {
 // Custom object for testing
 }

 private static class CustomAdapterObject {
 @JsonAdapter(CustomObjectAdapter.class)
 private int customField;

 static class CustomObjectAdapter extends TypeAdapter<CustomObject> {
 @Override public CustomObject read(JsonReader in) throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override public void write(JsonWriter out, CustomObject value) throws IOException {
 out.value("customObject");
 }
 }
 }

 private static class FieldTestClass1 {
 @SerializedName("intField")
 public int field1;
 @SerializedName("stringField")
 public String field2;
 @JsonAdapter(CustomAdapterObject.CustomObjectAdapter.class)
 @SerializedName("customField")
 public CustomObject field3;
 public transient int field4;
 }

 private static class IInterface {
 // Interface for testing
 }

 private static class TestClass1 {
 @SerializedName("intField")
 private int intField;
 @SerializedName("stringField")
 private String stringField;
 private int[] arrayField;
 private List<Integer> listField;
 private Map<String, Integer> mapField;
 private boolean booleanField;
 private double doubleField;
 @JsonAdapter(CustomAdapterObject.CustomObjectAdapter.class)
 private CustomObject customField;
 @JsonAdapter(CustomAdapterObject.CustomAdapterObject.class)
 private CustomAdapterObject customAdapterField;
 @JsonAdapter(TestJsonAdapterAnnotatedClass.JsonAdapter1.class)
 private TestJsonAdapterAnnotatedClass.JsonAdapter1 excludedField;
 @JsonAdapter(TestJsonAdapterAnnotatedClass.JsonAdapter2.class)
 private TestJsonAdapterAnnotatedClass.JsonAdapter2 excludedBooleanField;
 @JsonAdapter(TestJsonAdapterAnnotatedClass.JsonAdapter3.class)
 private TestJsonAdapterAnnotatedClass.JsonAdapter3 excludedDoubleField;
 }

 private static class TestJsonAdapterAnnotatedClass {
 @JsonAdapter(JsonAdapter1.class)
 public static class JsonAdapter1 {}

 @JsonAdapter(JsonAdapter2.class)
 public static class JsonAdapter2 {}

 @JsonAdapter(JsonAdapter3.class)
 public static class JsonAdapter3 {}

 @JsonAdapter(JsonAdapter1.class)
 public static class JsonAdapterAnnotatedField1 {
 @SerializedName("field1")
 public int field1;
 }

 @JsonAdapter(JsonAdapter2.class)
 public static class JsonAdapterAnnotatedField2 {
 @SerializedName("field2")
 public boolean field2;
 }

 @JsonAdapter(JsonAdapter3.class)
 public static class JsonAdapterAnnotatedField3 {
 @SerializedName("field3")
 public double field3;
 }
 }
}