import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private Gson gson;

 @Before
 public void setup() {
 gson = new GsonBuilder()
 .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
 .setExcluder(new Excluder())
 .create();
 }

 @Test
 public void testReflectiveTypeAdapterFactory() {
 Class<?> testClass = TestClass.class;
 TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy() {
 @Override public String translateName(Field field) {
 return field.getName();
 }
 },
 new Excluder() {
 @Override public boolean excludeField(Field f, boolean serialize) {
 return false;
 }
 }
 );
 TypeAdapter<TestClass> adapter = factory.create(gson, typeToken);

 TestClass testObject = new TestClass();
 testObject.field1 = "hello";
 testObject.field2 = new TestNestedClass();
 testObject.field2.field3 = "world";

 String json = gson.toJson(testObject);
 System.out.println(json);

 TestClass deserializedObject = adapter.read(new JsonReader(new StringReader(json)));
 assertEquals(testObject.field1, deserializedObject.field1);
 assertEquals(testObject.field2, deserializedObject.field2);
 assertEquals(testObject.field2.field3, deserializedObject.field2.field3);
 }

 @Test
 public void testExcludeField() {
 Class<?> testClass = TestExcludeClass.class;
 TypeToken<TestExcludeClass> typeToken = new TypeToken<TestExcludeClass>() {};
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy() {
 @Override public String translateName(Field field) {
 return field.getName();
 }
 },
 new Excluder() {
 @Override public boolean excludeField(Field f, boolean serialize) {
 return f.getName().equals("excludedField");
 }
 }
 );
 TypeAdapter<TestExcludeClass> adapter = factory.create(gson, typeToken);

 TestExcludeClass testObject = new TestExcludeClass();
 testObject.includedField = "included";
 testObject.excludedField = "excluded";

 String json = gson.toJson(testObject);
 System.out.println(json);

 TestExcludeClass deserializedObject = adapter.read(new JsonReader(new StringReader(json)));
 assertEquals(testObject.includedField, deserializedObject.includedField);
 assertNull(deserializedObject.excludedField);
 }
}

class TestClass {
 @SerializedName("field1")
 String field1;
 NestedClass field2;

 static class NestedClass {
 @SerializedName("field3")
 String field3;
 }
}

class TestExcludeClass {
 @SerializedName("includedField")
 String includedField;
 @Exclude
 String excludedField;
}