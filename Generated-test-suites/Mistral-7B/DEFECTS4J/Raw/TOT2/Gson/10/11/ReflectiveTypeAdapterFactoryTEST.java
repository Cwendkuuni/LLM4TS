import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingPolicy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private Gson gson;
 private TypeAdapterFactory reflectiveTypeAdapterFactory;

 @Before
 public void setUp() {
 gson = new GsonBuilder()
 .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
 .setExcluder(new Excluder())
 .setConstructorConstructor(new ConstructorConstructor())
 .create();
 reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(gson.getConstructorConstructor(),
 new FieldNamingPolicy.Standard(), new Excluder());
 }

 @Test
 public void testCreateTypeAdapterForSimpleClass() {
 Class<SimpleClass> simpleClass = SimpleClass.class;
 TypeAdapter<SimpleClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<SimpleClass>() {});

 SimpleClass instance = typeAdapter.fromJson("{\"name\": \"John\", \"age\": 30}", SimpleClass.class);
 assertEquals("John", instance.name);
 assertEquals(30, instance.age);
 }

 @Test
 public void testCreateTypeAdapterForInterface() {
 Class<MyInterface> myInterface = MyInterface.class;
 TypeAdapter<MyInterface> typeAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyInterface>() {});

 MyInterface instance = typeAdapter.fromJson("{\"name\": \"Jane\"}", MyInterface.class);
 assertEquals("Jane", instance.getName());
 }

 @Test
 public void testExcludeField() {
 Class<ExcludedClass> excludedClass = ExcludedClass.class;
 TypeAdapter<ExcludedClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<ExcludedClass>() {});

 ExcludedClass instance = typeAdapter.fromJson("{\"name\": \"John\"}", ExcludedClass.class);
 assertNull(instance.excludedField);
 }

 @Test
 public void testWriteReadStringField() throws IOException {
 Class<StringFieldClass> stringFieldClass = StringFieldClass.class;
 TypeAdapter<StringFieldClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<StringFieldClass>() {});

 StringFieldClass instance = new StringFieldClass();
 instance.stringField = "Test";

 JsonElement json = typeAdapter.toJson(instance);
 JsonObject jsonObject = json.getAsJsonObject();
 assertEquals("Test", jsonObject.get("stringField").getAsString());

 instance = typeAdapter.fromJson(json.toString(), StringFieldClass.class);
 assertEquals("Test", instance.stringField);
 }

 @Test
 public void testWriteReadIntField() throws IOException {
 Class<IntFieldClass> intFieldClass = IntFieldClass.class;
 TypeAdapter<IntFieldClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<IntFieldClass>() {});

 IntFieldClass instance = new IntFieldClass();
 instance.intField = 5;

 JsonElement json = typeAdapter.toJson(instance);
 JsonObject jsonObject = json.getAsJsonObject();
 assertEquals(5, jsonObject.get("intField").getAsInt());

 instance = typeAdapter.fromJson(json.toString(), IntFieldClass.class);
 assertEquals(5, instance.intField);
 }

 @Test
 public void testWriteReadJsonArrayField() throws IOException {
 Class<JsonArrayFieldClass> jsonArrayFieldClass = JsonArrayFieldClass.class;
 TypeAdapter<JsonArrayFieldClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<JsonArrayFieldClass>() {});

 JsonArrayFieldClass instance = new JsonArrayFieldClass();
 instance.jsonArrayField = new JsonArray();
 instance.jsonArrayField.add("Test1");
 instance.jsonArrayField.add("Test2");

 JsonElement json = typeAdapter.toJson(instance);
 JsonObject jsonObject = json.getAsJsonObject();
 JsonArray jsonArray = jsonObject.get("jsonArrayField").getAsJsonArray();
 assertEquals(2, jsonArray.size());
 assertEquals("Test1", jsonArray.get(0).getAsString());
 assertEquals("Test2", jsonArray.get(1).getAsString());

 instance = typeAdapter.fromJson(json.toString(), JsonArrayFieldClass.class);
 assertEquals(2, instance.jsonArrayField.size());
 assertEquals("Test1", instance.jsonArrayField.get(0).getAsString());
 assertEquals("Test2", instance.jsonArrayField.get(1).getAsString());
 }

 @Test
 public void testWriteReadJsonObjectField() throws IOException {
 Class<JsonObjectFieldClass> jsonObjectFieldClass = JsonObjectFieldClass.class;
 TypeAdapter<JsonObjectFieldClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<JsonObjectFieldClass>() {});

 JsonObjectFieldClass instance = new JsonObjectFieldClass();
 instance.jsonObjectField = new JsonObject();
 instance.jsonObjectField.addProperty("name", "Test");
 instance.jsonObjectField.addProperty("age", 30);

 JsonElement json = typeAdapter.toJson(instance);
 JsonObject jsonObject = json.getAsJsonObject();
 JsonObject jsonObject2 = jsonObject.get("jsonObjectField").getAsJsonObject();
 assertEquals("Test", jsonObject2.get("name").getAsString());
 assertEquals(30, jsonObject2.get("age").getAsInt());

 instance = typeAdapter.fromJson(json.toString(), JsonObjectFieldClass.class);
 assertEquals("Test", instance.jsonObjectField.get("name").getAsString());
 assertEquals(30, instance.jsonObjectField.get("age").getAsInt());
 }

 @Test
 public void testWriteReadFieldAnnotatedWithJsonAdapter() throws IOException {
 Class<AnnotatedClass> annotatedClass = AnnotatedClass.class;
 TypeAdapter<AnnotatedClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<AnnotatedClass>() {});

 AnnotatedClass instance = new AnnotatedClass();
 instance.annotatedField = new Date();

 JsonElement json = typeAdapter.toJson(instance);
 JsonObject jsonObject = json.getAsJsonObject();
 JsonElement jsonElement = jsonObject.get("annotatedField");
 assertTrue(jsonElement.isJsonPrimitive());
 assertTrue(jsonElement.isJsonNull());

 instance = typeAdapter.fromJson(json.toString(), AnnotatedClass.class);
 assertNull(instance.annotatedField);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testCreateTypeAdapterForNullType() {
 reflectiveTypeAdapterFactory.create(gson, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateTypeAdapterForInvalidType() {
 try {
 reflectiveTypeAdapterFactory.create(gson, new TypeToken<Object>() {});
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}

class SimpleClass {
 String name;
 int age;
}

interface MyInterface {
 String getName();
}

class ExcludedClass {
 @Exclude private String excludedField;
}

class StringFieldClass {
 String stringField;
}

class IntFieldClass {
 int intField;
}

class JsonArrayFieldClass {
 List<String> jsonArrayField;
}

class JsonObjectFieldClass {
 JsonObject jsonObjectField;
}

class AnnotatedClass {
 @JsonAdapter(DateAdapter.class) private Date annotatedField;
}