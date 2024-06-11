import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
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
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private static final String JSON_STRING = "{\"name\":\"John\",\"age\":42,\"city\":\"New York\"}";
 private static final Class<Person> PERSON_CLASS = Person.class;
 private Excluder excluder;
 private FieldNamingPolicy fieldNamingPolicy;
 private ConstructorConstructor constructorConstructor;
 private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

 @Before
 public void setUp() {
 fieldNamingPolicy = new FieldNamingPolicy.Standard();
 excluder = new Excluder();
 constructorConstructor = new ConstructorConstructor();
 reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
 }

 @Test
 public void testCreateTypeAdapter() {
 TypeAdapter<Person> personTypeAdapter = reflectiveTypeAdapterFactory.create(new Gson(), new TypeToken<Person>() {});
 assertNotNull(personTypeAdapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateTypeAdapterForPrimitive() {
 TypeAdapter<Integer> integerTypeAdapter = reflectiveTypeAdapterFactory.create(new Gson(), new TypeToken<Integer>() {});
 fail("ReflectiveTypeAdapterFactory should not create type adapter for primitive types.");
 }

 @Test
 public void testBindingFields() throws Exception {
 Person person = new Person("John", 42, "New York");
 TypeToken<Person> personToken = new TypeToken<Person>() {};
 ObjectConstructor<Person> constructor = constructorConstructor.get(personToken);
 Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(new Gson(), personToken, PERSON_CLASS);

 assertEquals(3, boundFields.size());
 for (ReflectiveTypeAdapterFactory.BoundField boundField : boundFields.values()) {
 String name = boundField.name;
 if ("name".equals(name)) {
 assertTrue(boundField.serialized);
 assertFalse(boundField.deserialized);
 } else if ("age".equals(name)) {
 assertFalse(boundField.serialized);
 assertTrue(boundField.deserialized);
 } else if ("city".equals(name)) {
 assertTrue(boundField.serialized);
 assertTrue(boundField.deserialized);
 } else {
 fail("Unexpected field name: " + name);
 }
 }
 }

 @Test
 public void testSerializationWithJsonAdapter() throws Exception {
 Person person = new Person("John", 42, "New York");
 TypeToken<Person> personToken = new TypeToken<Person>() {};
 ObjectConstructor<Person> constructor = constructorConstructor.get(personToken);
 ReflectiveTypeAdapterFactory.BoundField nameBoundField = getNameBoundField(boundFields(personToken));
 ReflectiveTypeAdapterFactory.BoundField cityBoundField = getCityBoundField(boundFields(personToken));
 ReflectiveTypeAdapterFactory.Adapter<Person> adapter = (ReflectiveTypeAdapterFactory.Adapter<Person>) reflectiveTypeAdapterFactory.create(new Gson(), personToken);

 String json = adapter.write(new JsonWriter(System.out), person).toString();
 assertEquals(JSON_STRING, json);
 }

 @Test
 public void testDeserializationWithJsonAdapter() throws Exception {
 JsonReader reader = new JsonReader(new StringReader(JSON_STRING));
 ReflectiveTypeAdapterFactory.Adapter<Person> adapter = (ReflectiveTypeAdapterFactory.Adapter<Person>) reflectiveTypeAdapterFactory.create(new Gson(), new TypeToken<Person>() {});
 Person person = adapter.read(reader, null);
 assertEquals("John", person.getName());
 assertEquals(42, person.getAge());
 assertEquals("New York", person.getCity());
 }

 private static Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields(TypeToken<?> typeToken) {
 return reflectiveTypeAdapterFactory.getBoundFields(new Gson(), typeToken, typeToken.getRawType());
 }

 private static ReflectiveTypeAdapterFactory.BoundField getNameBoundField(Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields) {
 for (ReflectiveTypeAdapterFactory.BoundField boundField : boundFields.values()) {
 if ("name".equals(boundField.name)) {
 return boundField;
 }
 }
 throw new IllegalStateException();
 }

 private static ReflectiveTypeAdapterFactory.BoundField getCityBoundField(Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields) {
 for (ReflectiveTypeAdapterFactory.BoundField boundField : boundFields.values()) {
 if ("city".equals(boundField.name)) {
 return boundField;
 }
 }
 throw new IllegalStateException();
 }
}

class Person {
 @JsonAdapter(PersonNameAdapter.class)
 private String name;
 private int age;
 private String city;

 public Person(String name, int age, String city) {
 this.name = name;
 this.age = age;
 this.city = city;
 }

 public String getName() {
 return name;
 }

 public int getAge() {
 return age;
 }

 public String getCity() {
 return city;
 }

 public static class PersonNameAdapter extends TypeAdapter<String> {
 @Override public String read(JsonReader jsonReader) throws IOException {
 return jsonReader.nextString();
 }

 @Override public void write(JsonWriter jsonWriter, String value) throws IOException {
 jsonWriter.value(value);
 }
 }
}