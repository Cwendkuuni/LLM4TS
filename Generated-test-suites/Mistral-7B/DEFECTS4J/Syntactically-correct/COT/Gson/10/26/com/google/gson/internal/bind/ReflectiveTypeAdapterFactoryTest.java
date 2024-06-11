import com.google.gson.*;
import com.google.gson.annotations.*;
import com.google.gson.internal.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private static final class TestClass {
 @SerializedName("name")
 private String name;
 @JsonAdapter(StringAdapter.class)
 private String id;
 private int value;
 private transient int transientValue;
 private List<Integer> list;
 private Map<String, String> map;

 public TestClass() {}

 public TestClass(String name, int value, String id, List<Integer> list, Map<String, String> map) {
 this.name = name;
 this.value = value;
 this.id = id;
 this.list = list;
 this.map = map;
 }
 }

 @JsonAdapter(ReflectiveTypeAdapterFactory.class)
 private static class StringAdapter extends TypeAdapter<String> {
 @Override public String read(JsonReader in) throws IOException {
 return in.nextString();
 }

 @Override public void write(JsonWriter out, String value) throws IOException {
 out.value(value);
 }
 }

 @Test
 public void testCreateTypeAdapter() throws Exception {
 Gson gson = new GsonBuilder().create();
 TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), null, null);
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});

 TestClass testClass = new TestClass("testName", 1, "testId", null, null);
 JsonWriter writer = new JsonWriter(System.out);
 adapter.write(writer, testClass);
 writer.flush();

 JsonReader reader = new JsonReader(new StringReader(writer.toString()));
 TestClass deserializedTestClass = adapter.read(reader);
 assertEquals(testClass.name, deserializedTestClass.name);
 assertEquals(testClass.value, deserializedTestClass.value);
 assertEquals(testClass.id, deserializedTestClass.id);
 assertNull(deserializedTestClass.list);
 assertNull(deserializedTestClass.map);
 assertEquals(testClass.transientValue, deserializedTestClass.transientValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateTypeAdapterForInterface() throws Exception {
 Gson gson = new GsonBuilder().create();
 TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), null, null);
 factory.create(gson, new TypeToken<Object>() {});
 fail("Should not create type adapter for interface");
 }

 @Test
 public void testWriteDefaultName() throws Exception {
 TestClass testClass = new TestClass("testName", 1, "testId", null, null);
 Gson gson = new GsonBuilder().create();
 TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), null, null);
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});
 JsonWriter writer = new JsonWriter(System.out);
 adapter.write(writer, testClass);
 writer.flush();

 JsonReader reader = new JsonReader(new StringReader(writer.toString()));
 reader.beginObject();
 String name = reader.nextName();
 assertEquals("name", name);
 reader.endObject();
 }

 @Test
 public void testWriteAlternativeName() throws Exception {
 TestClass testClass = new TestClass("testName", 1, "testId", null, null);
 TestClass testClassWithAlternativeName = new TestClass("testName2", 1, "testId", null, null);
 Gson gson = new GsonBuilder().create();
 TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), null, null);
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});
 JsonWriter writer = new JsonWriter(System.out);
 adapter.write(writer, testClass);
 writer.flush();

 JsonReader reader = new JsonReader(new StringReader(writer.toString()));
 reader.beginObject();
 reader.nextName(); // read the default name
 reader.nextNull(); // read the null value
 reader.beginObject(); // read the alternative name object
 String name = reader.nextName();
 assertEquals("name2", name); // alternative name
 reader.endObject();
 reader.endObject();

 TestClass deserializedTestClass = adapter.read(reader);
 assertEquals(testClassWithAlternativeName.name, deserializedTestClass.name);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testReadNullName() throws Exception {
 TestClass testClass = new TestClass("testName", 1, "testId", null, null);
 Gson gson = new GsonBuilder().create();
 TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), null, null);
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});
 JsonReader reader = new JsonReader(new StringReader("{\"name\":null}"));
 try {
 adapter.read(reader);
 fail("Should throw JsonSyntaxException for null name");
 } catch (JsonSyntaxException e) {
 // expected
 }
 }

 @Test
 public void testWriteField() throws Exception {
 TestClass testClass = new TestClass("testName", 1, "testId", null, null);
 Gson gson = new GsonBuilder().create();
 TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), null, null);
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});
 JsonWriter writer = new JsonWriter(System.out);
 adapter.write(writer, testClass);
 writer.flush();

 JsonReader reader = new JsonReader(new StringReader(writer.toString()));
 reader.beginObject();
 String name = reader.nextName();
 assertEquals("name", name);
 reader.nextNull(); // read the null value
 reader.beginObject();
 String name2 = reader.nextName();
 assertEquals("name", name2); // should read the original name
 reader.endObject();
 reader.endObject();

 TestClass deserializedTestClass = adapter.read(reader);
 assertEquals(testClass.name, deserializedTestClass.name);
 }

 @Test
 public void testReadPrimitiveField() throws Exception {
 TestClass testClass = new TestClass("testName", 1, "testId", null, null);
 Gson gson = new GsonBuilder().create();
 TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), null, null);
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});
 JsonWriter writer = new JsonWriter(System.out);
 adapter.write(writer, testClass);
 writer.flush();

 JsonReader reader = new JsonReader(new StringReader(writer.toString()));
 reader.beginObject();
 String name = reader.nextName();
 assertEquals("name", name);
 reader.nextNull(); // read the null value
 reader.beginObject();
 String name2 = reader.nextName();
 assertEquals("value", name2); // should read the original name
 int value = reader.nextInt();
 assertEquals(testClass.value, value);
 reader.endObject();
 reader.endObject();

 TestClass deserializedTestClass = adapter.read(reader);
 assertEquals(testClass.value, deserializedTestClass.value);
 }

 @Test
 public void testWriteAndReadList() throws Exception {
 TestClass testClass = new TestClass("testName", 1, "testId", null, null);
 List<Integer> list = new ArrayList<>();
 list.add(1);
 list.add(2);
 testClass.list = list;
 Gson gson = new GsonBuilder().create();
 TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), null, null);
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});
 JsonWriter writer = new JsonWriter(System.out);
 adapter.write(writer, testClass);
 writer.flush();

 JsonReader reader = new JsonReader(new StringReader(writer.toString()));
 reader.beginObject();
 String name = reader.nextName();
 assertEquals("name", name);
 reader.nextNull(); // read the null value
 reader.beginObject();
 String name2 = reader.nextName();
 assertEquals("list", name2); // should read the original name
 reader.beginArray();
 int firstValue = reader.nextInt();
 assertEquals(1, firstValue);
 int secondValue = reader.nextInt();
 assertEquals(2, secondValue);
 reader.endArray();
 reader.endObject();
 reader.endObject();

 TestClass deserializedTestClass = adapter.read(reader);
 List<Integer> deserializedList = deserializedTestClass.list;
 assertEquals(2, deserializedList.size());
 assertEquals(1, deserializedList.get(0));
 assertEquals(2, deserializedList.get(1));
 }

 @Test
 public void testWriteAndReadMap() throws Exception {
 TestClass testClass = new TestClass("testName", 1, "testId", null, null);
 Map<String, String> map = new LinkedHashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 testClass.map = map;
 Gson gson = new GsonBuilder().create();
 TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), null, null);
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});
 JsonWriter writer = new JsonWriter(System.out);
 adapter.write(writer, testClass);
 writer.flush();

 JsonReader reader = new JsonReader(new StringReader(writer.toString()));
 reader.beginObject();
 String name = reader.nextName();
 assertEquals("name", name);
 reader.nextNull(); // read the null value
 reader.beginObject();
 String name2 = reader.nextName();
 assertEquals("map", name2); // should read the original name
 reader.beginObject();
 reader.nextName(); // read the key name
 String key = reader.nextString();
 assertEquals("key1", key);
 reader.nextString(); // read the value string
 String value = reader.nextString();
 assertEquals("value1", value);
 reader.endObject();
 reader.beginObject();
 reader.nextName(); // read the key name
 key = reader.nextString();
 assertEquals("key2", key);
 reader.nextString(); // read the value string
 value = reader.nextString();
 assertEquals("value2", value);
 reader.endObject();
 reader.endObject();
 reader.endObject();

 TestClass deserializedTestClass = adapter.read(reader);
 Map<String, String> deserializedMap = deserializedTestClass.map;
 assertEquals(2, deserializedMap.size());
 assertEquals("value1", deserializedMap.get("key1"));
 assertEquals("value2", deserializedMap.get("key2"));
 }
}