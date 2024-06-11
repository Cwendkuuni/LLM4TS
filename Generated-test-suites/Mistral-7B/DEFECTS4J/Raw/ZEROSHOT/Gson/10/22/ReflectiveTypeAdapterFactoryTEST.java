import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.*;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 @Test
 public void testReflectiveTypeAdapterFactory() {
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
 Gson gson = new GsonBuilder()
 .setLenient()
 .registerTypeAdapterFactory(factory)
 .create();

 // Test serialization and deserialization of simple classes
 class TestClass {
 @SerializedName("testField")
 private int testField;

 public TestClass() {}

 public int getTestField() {
 return testField;
 }

 public void setTestField(int testField) {
 this.testField = testField;
 }
 }

 TestClass original = new TestClass();
 original.setTestField(1);
 String json = gson.toJson(original);
 TestClass deserialized = gson.fromJson(json, TestClass.class);
 assertEquals(1, deserialized.getTestField());

 // Test serialization and deserialization of classes with multiple fields
 class TestClassWithMultipleFields {
 @SerializedName("testField1")
 private int testField1;
 @SerializedName("testField2")
 private String testField2;

 public TestClassWithMultipleFields() {}

 public int getTestField1() {
 return testField1;
 }

 public void setTestField1(int testField1) {
 this.testField1 = testField1;
 }

 public String getTestField2() {
 return testField2;
 }

 public void setTestField2(String testField2) {
 this.testField2 = testField2;
 }
 }

 TestClassWithMultipleFields originalWithMultipleFields = new TestClassWithMultipleFields();
 originalWithMultipleFields.setTestField1(1);
 originalWithMultipleFields.setTestField2("test");
 json = gson.toJson(originalWithMultipleFields);
 TestClassWithMultipleFields deserializedWithMultipleFields = gson.fromJson(json, TestClassWithMultipleFields.class);
 assertEquals(1, deserializedWithMultipleFields.getTestField1());
 assertEquals("test", deserializedWithMultipleFields.getTestField2());

 // Test excluding fields from serialization and deserialization
 class TestClassToExclude {
 @SerializedName("testField1")
 private int testField1;
 @Exclude private int testField2;

 public TestClassToExclude() {}

 public int getTestField1() {
 return testField1;
 }

 public void setTestField1(int testField1) {
 this.testField1 = testField1;
 }
 }

 TestClassToExclude originalToExclude = new TestClassToExclude();
 originalToExclude.setTestField1(1);
 json = gson.toJson(originalToExclude);
 TestClassToExclude deserializedToExclude = gson.fromJson(json, TestClassToExclude.class);
 assertEquals(1, deserializedToExclude.getTestField1());
 assertEquals(0, deserializedToExclude.testField2);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testIllegalStateException() {
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
 Gson gson = new GsonBuilder()
 .setLenient()
 .registerTypeAdapterFactory(factory)
 .create();

 class TestClassWithIllegalStateException {
 @SerializedName("testField")
 private int testField;
 @JsonAdapter(IllegalStateExceptionAdapter.class)
 private int testField2;

 public TestClassWithIllegalStateException() {}

 public int getTestField() {
 return testField;
 }

 public void setTestField(int testField) {
 this.testField = testField;
 }
 }

 TestClassWithIllegalStateException originalWithIllegalStateException = new TestClassWithIllegalStateException();
 originalWithIllegalStateException.setTestField(1);
 String jsonWithIllegalStateException = gson.toJson(originalWithIllegalStateException);
 }

 static class IllegalStateExceptionAdapter extends TypeAdapter<IllegalStateException> {
 @Override public IllegalStateException read(JsonReader in) throws IOException {
 throw new IllegalStateException();
 }

 @Override public void write(JsonWriter out, IllegalStateException value) throws IOException {
 throw new IOException();
 }
 }
}