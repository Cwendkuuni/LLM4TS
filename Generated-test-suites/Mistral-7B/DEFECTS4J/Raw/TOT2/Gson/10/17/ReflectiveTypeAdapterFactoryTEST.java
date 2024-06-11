import com.google.gson.*;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ReflectiveTypeAdapterFactoryTest {

 private static class TestClass {
 private int intField;
 private String stringField;
 private TestInner testInner;

 @JsonAdapter(TestInnerAdapter.class)
 private TestInner innerField;

 public TestClass() {
 }

 public TestClass(int intField, String stringField, TestInner testInner, TestInner innerField) {
 this.intField = intField;
 this.stringField = stringField;
 this.testInner = testInner;
 this.innerField = innerField;
 }

 public static class TestInner {
 private int intField;
 private String stringField;

 public TestInner() {
 }

 public TestInner(int intField, String stringField) {
 this.intField = intField;
 this.stringField = stringField;
 }
 }
 }

 @Test
 public void testCreate() {
 Gson gson = new GsonBuilder()
 .excludeFieldsWithoutConcreteAdapters()
 .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
 .registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new IdentityFieldNamingPolicy(), new Excluder() {
 @Override
 public boolean excludeField(Field f, boolean serialize) {
 return false;
 }
 }))
 .create();

 TestClass testClass = gson.fromJson("{\"intField\":1,\"stringField\":\"test\",\"innerField\":{\"intField\":2,\"stringField\":\"innerTest\"}}", TestClass.class);
 assertEquals(1, testClass.intField);
 assertEquals("test", testClass.stringField);
 assertEquals(2, testClass.testInner.intField);
 assertEquals("innerTest", testClass.testInner.stringField);
 assertEquals(2, testClass.innerField.intField);
 assertEquals("innerTest", testClass.innerField.getClass().getSimpleName());
 }

 @Test(expected = JsonSyntaxException.class)
 public void testCreate_NullValue() {
 Gson gson = new GsonBuilder()
 .excludeFieldsWithoutConcreteAdapters()
 .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
 .registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new IdentityFieldNamingPolicy(), new Excluder() {
 @Override
 public boolean excludeField(Field f, boolean serialize) {
 return false;
 }
 }))
 .create();

 TestClass testClass = gson.fromJson("{\"intField\":null,\"stringField\":\"test\",\"innerField\":{\"intField\":2,\"stringField\":\"innerTest\"}}", TestClass.class);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testCreate_MissingField() {
 Gson gson = new GsonBuilder()
 .excludeFieldsWithoutConcreteAdapters()
 .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
 .registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new IdentityFieldNamingPolicy(), new Excluder() {
 @Override
 public boolean excludeField(Field f, boolean serialize) {
 return false;
 }
 }))
 .create();

 TestClass testClass = gson.fromJson("{\"intField\":1,\"stringField\":\"test\"}", TestClass.class);
 }

 @Test
 public void testWriteRead() throws IOException {
 Gson gson = new GsonBuilder()
 .excludeFieldsWithoutConcreteAdapters()
 .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
 .registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new IdentityFieldNamingPolicy(), new Excluder() {
 @Override
 public boolean excludeField(Field f, boolean serialize) {
 return false;
 }
 }))
 .create();

 TestClass testClass = new TestClass(1, "test", new TestClass.TestInner(2, "innerTest"), new TestClass.TestInner(3, "innerTest2"));
 JsonWriter writer = new JsonWriter(System.out);
 gson.toJson(testClass, writer);
 writer.flush();
 TestClass deserializedTestClass = gson.fromJson(System.in, TestClass.class);
 assertEquals(1, deserializedTestClass.intField);
 assertEquals("test", deserializedTestClass.stringField);
 assertEquals(2, deserializedTestClass.testInner.intField);
 assertEquals("innerTest", deserializedTestClass.testInner.stringField);
 assertEquals(3, deserializedTestClass.innerField.intField);
 assertEquals("innerTest2", deserializedTestClass.innerField.getClass().getSimpleName());
 }
}