import com.google.gson.*;
import com.google.gson.excpetions.JsonCreationException;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private static class TestClass {
 @SerializedName("name") String name;
 @SerializedName("age") int age;
 @SerializedName("list") List<Integer> list;

 public TestClass(String name, int age, List<Integer> list) {
 this.name = name;
 this.age = age;
 this.list = list;
 }
 }

 @Test
 public void testCreateAdapterForObject() {
 Gson gson = new GsonBuilder()
 .excludeFieldsWithoutConventionalNameStartingWith("o_")
 .create();
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(gson), new FieldNamingPolicy(), new Excluder());
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

 TestClass original = new TestClass("John Doe", 30, Arrays.asList(1, 2, 3));
 String json = gson.toJson(original);
 TestClass deserialized = adapter.read(new JsonReader(new StringReader(json)));
 assertEquals(original, deserialized);
 }

 @Test(expected = JsonCreationException.class)
 public void testCreateAdapterForNullType() {
 Gson gson = new GsonBuilder()
 .excludeFieldsWithoutConventionalNameStartingWith("o_")
 .create();
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(gson), new FieldNamingPolicy(), new Excluder());
 TypeAdapter<?> adapter = factory.create(gson, null);
 fail("Should throw JsonCreationException");
 }

 @Test(expected = JsonSyntaxException.class)
 public void testCreateAdapterForInvalidJson() {
 Gson gson = new GsonBuilder()
 .excludeFieldsWithoutConventionalNameStartingWith("o_")
 .create();
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(gson), new FieldNamingPolicy(), new Excluder());
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});
 String json = "{ \"name\": \"John Doe\", \"age\": 30, \"list\": [1, 2, 3] }"; // missing end brace
 JsonReader reader = new JsonReader(new StringReader(json));
 try {
 adapter.read(reader);
 fail("Should throw JsonSyntaxException");
 } catch (JsonSyntaxException e) {
 // expected
 }
 }

 @Test
 public void testCreateAdapterForInterface() {
 Gson gson = new GsonBuilder()
 .excludeFieldsWithoutConventionalNameStartingWith("o_")
 .create();
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(gson), new FieldNamingPolicy(), new Excluder());
 TypeAdapter<MyInterface> adapter = (TypeAdapter<MyInterface>) factory.create(gson, new TypeToken<MyInterface>() {});
 assertNotNull(adapter);
 }

 @Test
 public void testExcludeFields() {
 Class<?> clazz = TestClassExcludingField.class;
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(new GsonBuilder().create()), new FieldNamingPolicy(), new Excluder());
 TypeAdapter<TestClassExcludingField> adapter = factory.create(new GsonBuilder().create(), new TypeToken<TestClassExcludingField>() {});
 TestClassExcludingField original = new TestClassExcludingField("John Doe", 30);
 String json = new GsonBuilder().create().toJson(original);
 TestClassExcludingField deserialized = adapter.read(new JsonReader(new StringReader(json)));
 assertEquals(original, deserialized);
 assertNull(deserialized.list);
 }

 @Test
 public void testJsonAdapter() {
 Class<?> clazz = TestClassWithJsonAdapter.class;
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(new GsonBuilder().create()), new FieldNamingPolicy(), new Excluder());
 TypeAdapter<TestClassWithJsonAdapter> adapter = factory.create(new GsonBuilder().create(), new TypeToken<TestClassWithJsonAdapter>() {});
 TestClassWithJsonAdapter original = new TestClassWithJsonAdapter("John Doe", 30, new TestClassWithJsonAdapterList());
 String json = new GsonBuilder().create().toJson(original);
 TestClassWithJsonAdapter deserialized = adapter.read(new JsonReader(new StringReader(json)));
 assertEquals(original, deserialized);
 }

 @Test
 public void testListSerialization() {
 Class<?> clazz = TestClassWithList.class;
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(new GsonBuilder().create()), new FieldNamingPolicy(), new Excluder());
 TypeAdapter<TestClassWithList> adapter = factory.create(new GsonBuilder().create(), new TypeToken<TestClassWithList>() {});
 TestClassWithList original = new TestClassWithList("John Doe", Arrays.asList(1, 2, 3));
 String json = new GsonBuilder().create().toJson(original);
 TestClassWithList deserialized = adapter.read(new JsonReader(new StringReader(json)));
 assertEquals(original, deserialized);
 }

 @Test
 public void testListDeserializationWithNull() {
 Class<?> clazz = TestClassWithList.class;
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(new GsonBuilder().create()), new FieldNamingPolicy(), new Excluder());
 TypeAdapter<TestClassWithList> adapter = factory.create(new GsonBuilder().create(), new TypeToken<TestClassWithList>() {});
 String json = "{\"name\":\"John Doe\"}";
 TestClassWithList original = new TestClassWithList("John Doe", null);
 JsonReader reader = new JsonReader(new StringReader(json));
 try {
 adapter.read(reader);
 fail("Should throw JsonCreationException");
 } catch (JsonCreationException e) {
 // expected
 }
 }

 @Test
 public void testListDeserializationWithEmptyArray() {
 Class<?> clazz = TestClassWithList.class;
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(new GsonBuilder().create()), new FieldNamingPolicy(), new Excluder());
 TypeAdapter<TestClassWithList> adapter = factory.create(new GsonBuilder().create(), new TypeToken<TestClassWithList>() {});
 String json = "{\"name\":\"John Doe\", \"list\":[]}";
 TestClassWithList original = new TestClassWithList("John Doe", null);
 JsonReader reader = new JsonReader(new StringReader(json));
 TestClassWithList deserialized = adapter.read(reader);
 assertEquals(original, deserialized);
 assertNull(deserialized.list);
 }

 @Test
 public void testListDeserializationWithInvalidJson() {
 Class<?> clazz = TestClassWithList.class;
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(new GsonBuilder().create()), new FieldNamingPolicy(), new Excluder());
 TypeAdapter<TestClassWithList> adapter = factory.create(new GsonBuilder().create(), new TypeToken<TestClassWithList>() {});
 String json = "{\"name\":\"John Doe\", \"list\":[1, 2, 3, 4}";
 JsonReader reader = new JsonReader(new StringReader(json));
 try {
 adapter.read(reader);
 fail("Should throw JsonSyntaxException");
 } catch (JsonSyntaxException e) {
 // expected
 }
 }

 private static class TestClassExcludingField {
 private String name;
 private List<Integer> list;

 TestClassExcludingField(String name) {
 this.name = name;
 }
 }

 private static class TestClassWithJsonAdapter {
 @JsonAdapter(TestClassWithJsonAdapterListAdapter.class)
 private TestClassWithJsonAdapterList list;
 private String name;
 private int age;

 TestClassWithJsonAdapter(String name, int age, TestClassWithJsonAdapterList list) {
 this.name = name;
 this.age = age;
 this.list = list;
 }
 }

 private static class TestClassWithJsonAdapterList {
 @Expose
 private List<Integer> list;

 TestClassWithJsonAdapterList() {
 this.list = new ArrayList<>();
 }
 }

 private static class TestClassWithList {
 private String name;
 private List<Integer> list;

 TestClassWithList(String name, List<Integer> list) {
 this.name = name;
 this.list = list;
 }
 }
}