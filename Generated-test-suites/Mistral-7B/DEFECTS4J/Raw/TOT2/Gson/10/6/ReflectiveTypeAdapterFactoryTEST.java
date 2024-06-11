import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ReflectiveTypeAdapterFactoryTest {

 private Gson gson;

 @Before
 public void init() {
 gson = new GsonBuilder()
 .setPrettyPrinting()
 .create();
 }

 @Test
 public void testCreateAdapterForSimpleClass() {
 Class<Person> personClass = Person.class;
 TypeAdapter<Person> adapter = gson.getDelegateAdapter(new TypeToken<Person>() {});
 assertEquals(Person.class, adapter.getClass());
 }

 @Test
 public void testCreateAdapterForComplexClass() {
 Class<Employee> employeeClass = Employee.class;
 TypeAdapter<Employee> adapter = gson.getDelegateAdapter(new TypeToken<Employee>() {});
 assertEquals(Employee.class, adapter.getClass());
 }

 @Test
 public void testSerializeAndDeserializeSimpleClass() throws IOException {
 Person person = new Person("John Doe", 30);
 String json = gson.toJson(person);
 Person deserializedPerson = gson.fromJson(json, Person.class);
 assertEquals(person, deserializedPerson);
 }

 @Test
 public void testSerializeAndDeserializeComplexClass() throws IOException {
 Employee employee = new Employee("John Doe", 30, new Address("123 Main St", "Anytown"));
 String json = gson.toJson(employee);
 Employee deserializedEmployee = gson.fromJson(json, Employee.class);
 assertEquals(employee, deserializedEmployee);
 }

 @Test
 public void testExcludeField() {
 class ExcludedPerson {
 @Exclude private int age;
 String name;

 ExcludedPerson(String name, int age) {
 this.name = name;
 this.age = age;
 }
 }
 TypeAdapter<ExcludedPerson> adapter = gson.getDelegateAdapter(new TypeToken<ExcludedPerson>() {});
 ExcludedPerson person = new ExcludedPerson("John Doe", 30);
 String json = gson.toJson(person);
 JsonElement parsedJson = JsonParser.parseString(json);
 JsonObject jsonObject = parsedJson.getAsJsonObject();
 assertNull(jsonObject.get("age"));
 }

 @Test
 public void testExcludeClass() {
 class ExcludedClass {
 List<Person> people;
 }

 @SuppressWarnings("unchecked")
 TypeAdapter<ExcludedClass> adapter = gson.getDelegateAdapter(new TypeToken<ExcludedClass>() {});
 ExcludedClass excludedClass = new ExcludedClass();
 excludedClass.people = new ArrayList<>();
 excludedClass.people.add(new Person("John Doe", 30));
 String json = gson.toJson(excludedClass);
 JsonElement parsedJson = JsonParser.parseString(json);
 JsonObject jsonObject = parsedJson.getAsJsonObject();
 assertNull(jsonObject.get("people"));
 }

 @Test(expected = JsonSyntaxException.class)
 public void testInvalidJson() {
 String json = "{ \"name\": \"John Doe\", \"age\": }";
 gson.fromJson(json, Person.class);
 }

 @Test
 public void testNullValue() {
 Person person = null;
 String json = gson.toJson(person);
 assertNull(gson.fromJson(json, Person.class));
 }

 @Test(expected = IllegalAccessException.class)
 public void testIllegalAccess() throws IOException {
 Person person = new Person("John Doe", 30);
 String json = gson.toJson(person);
 TypeAdapter<Person> adapter = gson.getDelegateAdapter(new TypeToken<Person>() {});
 JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
 Map<String, BoundField> boundFields = adapter.getClass().getDeclaredField("boundFields").get(adapter);
 Field nameField = Person.class.getDeclaredField("name");
 nameField.setAccessible(true);
 nameField.set(jsonObject, "Invalid Name");
 adapter.read(JsonReader.NULL, null);
 fieldNamingPolicy.translateName(nameField); // This should throw an IllegalAccessException
 }
}

class Person {
 String name;
 int age;

 Person(String name, int age) {
 this.name = name;
 this.age = age;
 }
}

class Address {
 String street;
 String city;

 Address(String street, String city) {
 this.street = street;
 this.city = city;
 }
}

class Employee {
 String name;
 int age;
 Address address;

 Employee(String name, int age, Address address) {
 this.name = name;
 this.age = age;
 this.address = address;
 }
}