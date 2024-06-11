import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.*;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 @SuppressWarnings("unchecked")
 @Test
 public void testReflectiveTypeAdapterFactory() throws Exception {
 // Create a mock Gson instance for testing
 Gson gson = new GsonBuilder().create();

 // Test case for a simple JavaBean with no JsonAdapter or SerializedName annotations
 class TestBean {
 private int id;
 private String name;

 public int getId() {
 return id;
 }

 public void setId(int id) {
 this.id = id;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
 }

 TypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
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
 return false;
 }
 }
 );

 // Test serialization
 TestBean testBean = new TestBean();
 testBean.setId(1);
 testBean.setName("Test");
 JsonWriter jsonWriter = new JsonWriter(new StringWriter());
 gson.getAdapter(ReflectiveTypeAdapterFactory.class).writeTree(jsonWriter, testBean);
 String jsonString = jsonWriter.toString();
 assertEquals("{\"id\":1,\"name\":\"Test\"}", jsonString);

 // Test deserialization
 JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
 TestBean deserializedTestBean = gson.fromJson(jsonReader, TestBean.class);
 assertEquals(1, deserializedTestBean.getId());
 assertEquals("Test", deserializedTestBean.getName());

 // Test case for a JavaBean with a JsonAdapter annotation
 class TestBeanWithJsonAdapter {
 @JsonAdapter(AdapterForTestBeanWithJsonAdapter.class)
 private int id;
 private String name;

 public int getId() {
 return id;
 }

 public void setId(int id) {
 this.id = id;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
 }

 TypeToken<TestBeanWithJsonAdapter> testBeanWithJsonAdapterTypeToken = new TypeToken<TestBeanWithJsonAdapter>() {};
 Adapter<TestBeanWithJsonAdapter> adapter = (Adapter<TestBeanWithJsonAdapter>) reflectiveTypeAdapterFactory.create(gson, testBeanWithJsonAdapterTypeToken);

 // Test serialization with JsonAdapter
 TestBeanWithJsonAdapter testBeanWithJsonAdapter = new TestBeanWithJsonAdapter();
 testBeanWithJsonAdapter.setId(2);
 testBeanWithJsonAdapter.setName("TestWithJsonAdapter");
 JsonWriter jsonWriterWithJsonAdapter = new JsonWriter(new StringWriter());
 adapter.write(jsonWriterWithJsonAdapter, testBeanWithJsonAdapter);
 String jsonStringWithJsonAdapter = jsonWriterWithJsonAdapter.toString();
 assertEquals("{\"id\":2}", jsonStringWithJsonAdapter);

 // Test deserialization with JsonAdapter
 JsonReader jsonReaderWithJsonAdapter = new JsonReader(new StringReader(jsonStringWithJsonAdapter));
 TestBeanWithJsonAdapter deserializedTestBeanWithJsonAdapter = adapter.read(jsonReaderWithJsonAdapter);
 assertEquals(2, deserializedTestBeanWithJsonAdapter.getId());
 assertNull(deserializedTestBeanWithJsonAdapter.getName());

 // Test case for a JavaBean with a SerializedName annotation
 class TestBeanWithSerializedName {
 @SerializedName("testId")
 private int id;
 private String name;

 public int getId() {
 return id;
 }

 public void setId(int id) {
 this.id = id;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
 }

 TypeToken<TestBeanWithSerializedName> testBeanWithSerializedNameTypeToken = new TypeToken<TestBeanWithSerializedName>() {};
 Adapter<TestBeanWithSerializedName> adapterWithSerializedName = (Adapter<TestBeanWithSerializedName>) reflectiveTypeAdapterFactory.create(gson, testBeanWithSerializedNameTypeToken);

 // Test serialization with SerializedName
 TestBeanWithSerializedName testBeanWithSerializedName = new TestBeanWithSerializedName();
 testBeanWithSerializedName.setId(3);
 testBeanWithSerializedName.setName("TestWithSerializedName");
 JsonWriter jsonWriterWithSerializedName = new JsonWriter(new StringWriter());
 adapterWithSerializedName.write(jsonWriterWithSerializedName, testBeanWithSerializedName);
 String jsonStringWithSerializedName = jsonWriterWithSerializedName.toString();
 assertEquals("{\"testId\":3,\"name\":\"TestWithSerializedName\"}", jsonStringWithSerializedName);

 // Test deserialization with SerializedName
 JsonReader jsonReaderWithSerializedName = new JsonReader(new StringReader(jsonStringWithSerializedName));
 TestBeanWithSerializedName deserializedTestBeanWithSerializedName = adapterWithSerializedName.read(jsonReaderWithSerializedName);
 assertEquals(3, deserializedTestBeanWithSerializedName.getId());
 assertEquals("TestWithSerializedName", deserializedTestBeanWithSerializedName.getName());
 }

 static class AdapterForTestBeanWithJsonAdapter extends TypeAdapter<TestBeanWithJsonAdapter> {
 @Override public TestBeanWithJsonAdapter read(JsonReader in) throws IOException {
 TestBeanWithJsonAdapter testBeanWithJsonAdapter = new TestBeanWithJsonAdapter();
 in.nextInt(); // read int id
 return testBeanWithJsonAdapter;
 }

 @Override public void write(JsonWriter out, TestBeanWithJsonAdapter value) throws IOException {
 out.value(value.getId());
 }
 }
}