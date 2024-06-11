import org.junit.Assert; import org.junit.Before; import org.junit.Test; import org.mockito.Mockito;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

 private Gson gson;
 private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

 @Before
 public void setUp() {
 gson = new GsonBuilder().create();
 reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 Mockito.mock(ConstructorConstructor.class),
 Mockito.mock(FieldNamingStrategy.class),
 Mockito.mock(Excluder.class));
 }

 @Test
 public void testCreateTypeAdapter() throws IOException {
 Class<MyClass> type = MyClass.class;
 TypeAdapter<MyClass> adapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

 Assert.assertNotNull(adapter);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCreateTypeAdapterForPrimitive() {
 TypeAdapterFactory factory = reflectiveTypeAdapterFactory;
 TypeAdapter<Integer> adapter = factory.create(gson, new TypeToken<Integer>() {});
 }

 @Test
 public void testGetBoundFields() {
 MyClass object = new MyClass();
 TypeToken<MyClass> type = new TypeToken<MyClass>() {};
 Type adapterType = TypeToken.get(type.getType(), MyClass.class);

 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 Mockito.mock(ConstructorConstructor.class),
 Mockito.mock(FieldNamingStrategy.class),
 Mockito.mock(Excluder.class));
 Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, type, MyClass.class);

 Assert.assertNotNull(boundFields);
 Assert.assertEquals(2, boundFields.size());

 List<String> fieldNames = getFieldNames(MyClass.class, "name1");
 Assert.assertEquals(fieldNames, boundFields.get("name1").getFieldNames());
 fieldNames = getFieldNames(MyClass.class, "name2");
 Assert.assertEquals(fieldNames, boundFields.get("name2").getFieldNames());
 }

 @Test
 public void testWriteRead() throws IOException {
 MyClass object = new MyClass("value1", "value2");
 TypeToken<MyClass> type = new TypeToken<MyClass>() {};
 Type adapterType = TypeToken.get(type.getType(), MyClass.class);

 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 Mockito.mock(ConstructorConstructor.class),
 Mockito.mock(FieldNamingStrategy.class),
 Mockito.mock(Excluder.class));
 TypeAdapter<MyClass> adapter = factory.create(gson, type);

 String json = "{\"name1\":\"value1\",\"name2\":\"value2\"}";
 MyClass readObject = adapter.read(new JsonReader(new StringReader(json)));

 Assert.assertEquals(object, readObject);

 StringWriter writer = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(writer);
 adapter.write(jsonWriter, object);
 String jsonString = writer.toString();

 Assert.assertEquals(json, jsonString);
 }

 private List<String> getFieldNames(Class<?> clazz, String fieldName) {
 Field field = null;
 try {
 field = clazz.getDeclaredField(fieldName);
 } catch (NoSuchFieldException ignored) {
 }

 return getFieldNames(field);
}

 private List<String> getFieldNames(Field field) {
 return ((ReflectiveTypeAdapterFactory.BoundField) field.getAnnotation(ReflectiveTypeAdapterFactory.BoundField.class)).getFieldNames();
}

 static class MyClass {
 @SerializedName("name1")
 private String name1;
 @SerializedName("name2")
 private String name2;

 public MyClass(String name1, String name2) {
 this.name1 = name1;
 this.name2 = name2;
 }
 }
}