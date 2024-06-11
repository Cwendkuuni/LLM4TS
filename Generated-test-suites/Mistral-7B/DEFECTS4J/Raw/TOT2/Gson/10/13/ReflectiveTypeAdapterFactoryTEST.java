import com.google.gson.*;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private static final class TestClass {
 private int field1;
 private String field2;
 private List<String> field3;

 @JsonAdapter(CustomJsonAdapter.class)
 private CustomClass field4;

 public TestClass() {
 field3 = new ArrayList<>();
 }

 public TestClass(int field1, String field2, List<String> field3, CustomClass field4) {
 this();
 this.field1 = field1;
 this.field2 = field2;
 this.field3 = field3;
 this.field4 = field4;
 }

 public int getField1() {
 return field1;
 }

 public void setField1(int field1) {
 this.field1 = field1;
 }

 public String getField2() {
 return field2;
 }

 public void setField2(String field2) {
 this.field2 = field2;
 }

 public List<String> getField3() {
 return field3;
 }

 public void setField3(List<String> field3) {
 this.field3 = field3;
 }

 public CustomClass getField4() {
 return field4;
 }

 public void setField4(CustomClass field4) {
 this.field4 = field4;
 }

 public static class CustomClass {
 private String customField;

 public CustomClass() {
 }

 public CustomClass(String customField) {
 this.customField = customField;
 }

 public String getCustomField() {
 return customField;
 }

 public void setCustomField(String customField) {
 this.customField = customField;
 }
 }
}

 @Test
 public void testCreateAdapter() {
 Gson gson = new GsonBuilder().setFieldNamingStrategy(FieldNamingPolicy.IDENTITY).create();
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingStrategy() {
 @Override public String translateName(Field f) {
 return f.getName();
 }
 },
 new Excluder() {
 @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
 return false;
 }

 @Override public boolean excludeField(Field f, boolean serialize) {
 return f.isSynthetic();
 }
 });
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});
 assertNotNull(adapter);
 }

 @Test
 public void testSerialization() throws Exception {
 TestClass obj = new TestClass(1, "field2", Collections.singletonList("field3"), new CustomClass("customField"));
 String json = new GsonBuilder().create().toJson(obj);
 TestClass deserialized = new GsonBuilder().create().fromJson(json, TestClass.class);
 assertEquals(obj, deserialized);
 }

 @Test
 public void testSerializationWithCustomName() throws Exception {
 TestClass obj = new TestClass(1, "field2", Collections.singletonList("field3"), new CustomClass("customField"));
 String json = new GsonBuilder().setFieldNamingStrategy(new FieldNamingPolicy.Custom() {
 @Override public String translateName(Field f) {
 return "custom_" + f.getName();
 }
 }).create().toJson(obj);
 TestClass deserialized = new GsonBuilder()
 .setFieldNamingStrategy(new FieldNamingPolicy.Custom() {
 @Override public String translateName(Field f) {
 return "custom_" + f.getName();
 }
 }).create().fromJson(json, TestClass.class);
 assertEquals(obj, deserialized);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testSerializationNullField() throws Exception {
 TestClass obj = new TestClass(1, null, Collections.singletonList("field3"), new CustomClass("customField"));
 String json = new GsonBuilder().create().toJson(obj);
 new GsonBuilder().create().fromJson(json, TestClass.class);
 fail("Expected JsonSyntaxException");
 }

 @Test(expected = JsonSyntaxException.class)
 public void testDeserializationNullField() throws Exception {
 String json = "{\"custom_field1\": 1, \"custom_field3\": [\"field3\"]}";
 TestClass obj = new GsonBuilder().create().fromJson(json, TestClass.class);
 fail("Expected JsonSyntaxException");
 }

 @Test(expected = JsonSyntaxException.class)
 public void testDeserializationMissingField() throws Exception {
 String json = "{\"custom_field1\": 1, \"custom_field3\": [\"field3\"]}";
 TestClass obj = new GsonBuilder().create().fromJson(json, TestClass.class);
 fail("Expected JsonSyntaxException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializationInvalidType() throws Exception {
 String json = "{\"custom_field1\": 1, \"custom_field3\": [\"field3\"]}";
 TestClass obj = new GsonBuilder().create().fromJson(json, Integer.class);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testDeserializationEmptyList() throws Exception {
 String json = "{\"custom_field1\": 1}";
 TestClass obj = new GsonBuilder().create().fromJson(json, TestClass.class);
 assertTrue(obj.getField3().isEmpty());
 }
}