import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Field;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private Gson gson;

 @Before
 public void setup() {
 gson = new GsonBuilder().create();
 }

 @Test
 public void testCreate_NullTypeToken() {
 assertNull(ReflectiveTypeAdapterFactory.create(gson, null));
 }

 @Test
 public void testCreate_NonObjectType() {
 Integer type = Integer.TYPE;
 assertNull(ReflectiveTypeAdapterFactory.create(gson, new TypeToken<Integer>() {}.getType()));
 }

 @Test
 public void testExcludeField() {
 Class<?> clazz = TestClass.class;
 Field field = clazz.getDeclaredField("excludedField");
 assertFalse(ReflectiveTypeAdapterFactory.excludeField(field, true));
 assertFalse(ReflectiveTypeAdapterFactory.excludeField(field, false));

 field.setAccessible(true);
 field.set(new TestClass(), "excludedValue");
 TestClass instance = new TestClass();
 assertFalse(ReflectiveTypeAdapterFactory.excludeField(field, true, new Excluder() {
 @Override
 public boolean excludeClass(Class<?> clazz, boolean serialize) {
 return false;
 }

 @Override
 public boolean excludeField(Field field, boolean serialize) {
 return false;
 }
 }));
 }

 @Test
 public void testGetFieldNames() {
 Class<?> clazz = TestClass.class;
 Field field = clazz.getDeclaredField("field1");
 List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
 assertEquals(1, fieldNames.size());
 assertEquals("field1", fieldNames.get(0));

 field = clazz.getDeclaredField("field2");
 field.setAnnotation(new SerializedName("name2"));
 fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
 assertEquals(1, fieldNames.size());
 assertEquals("name2", fieldNames.get(0));

 field = clazz.getDeclaredField("field3");
 field.setAnnotation(new SerializedName("name3"));
 field.setAnnotation(new JsonAdapter(AdapterClass.class));
 fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(field);
 assertEquals(1, fieldNames.size());
 assertEquals("name3", fieldNames.get(0));
 }

 @Test
 public void testCreate_NullGson() {
 assertNull(ReflectiveTypeAdapterFactory.create(null, new TypeToken<TestClass>() {}.getType()));
 }

 @Test
 public void testCreate_NullType() {
 assertNull(ReflectiveTypeAdapterFactory.create(gson, null));
 }

 @Test
 public void testCreate() throws IOException {
 TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {}.getType();
 TypeAdapter<TestClass> typeAdapter = ReflectiveTypeAdapterFactory.create(gson, typeToken);
 TestClass instance = new TestClass();
 assertNotNull(typeAdapter);

 JsonWriter writer = new JsonWriter(System.out);
 typeAdapter.write(writer, instance);
 writer.flush();

 JsonReader reader = new JsonReader(new StringReader("[\"test1\",\"test2\",\"test3\"]"));
 reader.setLenient(true);
 TestClass deserializedInstance = typeAdapter.read(reader);
 assertEquals(instance, deserializedInstance);
 }

 @Test
 public void testCreate_ExcludedField() throws IOException {
 Class<?> clazz = TestClassWithExcludedField.class;
 TypeToken<TestClassWithExcludedField> typeToken = new TypeToken<TestClassWithExcludedField>() {}.getType();
 TypeAdapter<TestClassWithExcludedField> typeAdapter = ReflectiveTypeAdapterFactory.create(gson, typeToken);
 TestClassWithExcludedField instance = new TestClassWithExcludedField();
 assertNotNull(typeAdapter);

 JsonWriter writer = new JsonWriter(System.out);
 typeAdapter.write(writer, instance);
 writer.flush();

 JsonReader reader = new JsonReader(new StringReader("[\"test1\",\"test2\"]"));
 reader.setLenient(true);
 TestClassWithExcludedField deserializedInstance = typeAdapter.read(reader);
 assertEquals(instance, deserializedInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreate_DuplicateFieldNames() throws IOException {
 Class<?> clazz = TestClassWithDuplicateFieldNames.class;
 TypeToken<TestClassWithDuplicateFieldNames> typeToken = new TypeToken<TestClassWithDuplicateFieldNames>() {}.getType();
 TypeAdapter<TestClassWithDuplicateFieldNames> typeAdapter = ReflectiveTypeAdapterFactory.create(gson, typeToken);
 TestClassWithDuplicateFieldNames instance = new TestClassWithDuplicateFieldNames();
 assertNotNull(typeAdapter);

 JsonWriter writer = new JsonWriter(System.out);
 typeAdapter.write(writer, instance);
 writer.flush();
 }
}

 class TestClass {
 private String field1;
 private String field2;

 public TestClass() {
 }

 public String getField1() {
 return field1;
 }

 public void setField1(String field1) {
 this.field1 = field1;
 }

 public String getField2() {
 return field2;
 }

 public void setField2(String field2) {
 this.field2 = field2;
 }
 }

 class AdapterClass extends TypeAdapter<String> {
 @Override
 public String read(JsonReader reader) throws IOException {
 return reader.nextString();
 }

 @Override
 public void write(JsonWriter writer, String value) throws IOException {
 writer.value(value);
 }
}

 class TestClassWithExcludedField {
 @Exclude
 private String excludedField;

 private String field1;
 private String field2;

 public TestClassWithExcludedField() {
 }

 public String getField1() {
 return field1;
 }

 public void setField1(String field1) {
 this.field1 = field1;
 }

 public String getField2() {
 return field2;
 }

 public void setField2(String field2) {
 this.field2 = field2;
 }
}

 class TestClassWithDuplicateFieldNames {
 @SerializedName("test")
 private String test1;

 @SerializedName("test")
 private String test2;

 public TestClassWithDuplicateFieldNames() {
 }

 public String getTest1() {
 return test1;
 }

 public void setTest1(String test1) {
 this.test1 = test1;
 }

 public String getTest2() {
 return test2;
 }

 public void setTest2(String test2) {
 this.test2 = test2;
 }
}