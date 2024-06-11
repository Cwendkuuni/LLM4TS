import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

 private static final Class<ReflectiveTypeAdapterFactory> REFLECTIVE_TYPE_ADAPTER_FACTORY = ReflectiveTypeAdapterFactory.class;
 private static final Constructor<ReflectiveTypeAdapterFactory> CONSTRUCTOR = ReflectiveTypeAdapterFactory.class.getDeclaredConstructor(
     ConstructorConstructor.class, FieldNamingStrategy.class, Excluder.class);
 private static final Gson GSON = new GsonBuilder().setExclusionStrategies(new Excluder()).create();

 @Test
 public void testCreateTypeAdapter() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeAdapter<?> adapter = factory.create(GSON, new TypeToken<HashMap<String, Integer>>(){}.getType());
 assertNotNull(adapter);
 }

 @Test
 public void testSerializedName() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 String json = personAdapter.toJson(person).getAsString();
 assertEquals("{\"name\":\"John Doe\"}", json);
 }

 @Test
 public void testAlternateName() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 String json = personAdapter.toJson(person, new String[]{"username"}).getAsString();
 assertEquals("{\"username\":\"John Doe\"}", json);
 }

 @Test
 public void testExclusion() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 person.setExcludedField("excluded");
 String json = personAdapter.toJson(person).getAsString();
 assertEquals("{}", json);
 }

 @Test
 public void testNullField() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person(null);
 String json = personAdapter.toJson(person).getAsString();
 assertEquals("{}", json);
 }

 @Test
 public void testSetField() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 String json = "{\"name\":\"Jane Doe\"}";
 JsonObject jsonObject = JsonElement.parse(json).getAsJsonObject();

 personAdapter.fromJson(jsonObject, person);
 assertEquals("Jane Doe", person.getName());
 }

 @Test
 public void testSetFieldWithExclusion() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 person.setExcludedField("excluded");
 String json = "{\"name\":\"Jane Doe\",\"excluded\":\"excluded\"}";
 JsonObject jsonObject = JsonElement.parse(json).getAsJsonObject();

 try {
 personAdapter.fromJson(jsonObject, person);
 fail();
 } catch (JsonSyntaxException e) {
 //expected
 }
 }

 @Test
 public void testSetFieldWithIncompatibleType() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 String json = "{\"name\":1,\"excluded\":\"excluded\"}";
 JsonObject jsonObject = JsonElement.parse(json).getAsJsonObject();

 try {
 personAdapter.fromJson(jsonObject, person);
 fail();
 } catch (JsonSyntaxException e) {
 //expected
 }
 }

 @Test
 public void testSetFieldWithPrimitive() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 person.setAge(30);
 String json = "{\"name\":\"Jane Doe\",\"age\":31}";
 JsonObject jsonObject = JsonElement.parse(json).getAsJsonObject();

 try {
 personAdapter.fromJson(jsonObject, person);
 fail();
 } catch (JsonSyntaxException e) {
 //expected
 }
 }

 @Test
 public void testSetFieldWithList() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 List<String> list = Arrays.asList("Jane Doe", "Jim Doe");
 person.setList(list);
 String json = "{\"name\":\"John Doe\",\"list\":[\"Jane Doe\",\"Jim Doe\"]}";
 JsonObject jsonObject = JsonElement.parse(json).getAsJsonObject();

 personAdapter.fromJson(jsonObject, person);
 assertEquals(list, person.getList());
 }

 @Test
 public void testSetFieldWithMap() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 Map<String, String> map = new HashMap<>();
 map.put("name", "Jane Doe");
 map.put("age", "31");
 person.setMap(map);
 String json = "{\"name\":\"John Doe\",\"map\":{\"name\":\"Jane Doe\",\"age\":\"31\"}}";
 JsonObject jsonObject = JsonElement.parse(json).getAsJsonObject();

 personAdapter.fromJson(jsonObject, person);
 assertEquals(map, person.getMap());
 }

 @Test
 public void testSetFieldWithNestedObject() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 Address address = new Address();
 address.setCity("New York");
 address.setCountry("USA");
 person.setAddress(address);
 String json = "{\"name\":\"John Doe\",\"address\":{\"city\":\"New York\",\"country\":\"USA\"}}";
 JsonObject jsonObject = JsonElement.parse(json).getAsJsonObject();

 personAdapter.fromJson(jsonObject, person);
 assertEquals(address, person.getAddress());
 assertEquals("New York", address.getCity());
 assertEquals("USA", address.getCountry());
 }

 @Test
 public void testCreateTypeAdapterWithConstructor() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 TypeAdapter<Person> adapter = personAdapter.getClass().getDeclaredMethod("newInstance", Gson.class)
     .invoke(personAdapter, GSON);
 TypeToken<Person> expectedTypeToken = new TypeToken<Person>(){}.getType();
 assertEquals(expectedTypeToken, adapter.getType());
 }

 @Test
 public void testGetField() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 String json = personAdapter.toJson(person).getAsString();
 JsonObject jsonObject = JsonElement.parse(json).getAsJsonObject();
 Person readPerson = personAdapter.fromJson(jsonObject, Person.class);

 Field field = Person.class.getDeclaredField("name");
 field.setAccessible(true);
 String readName = (String) field.get(readPerson);
 assertEquals("John Doe", readName);
 }

 @Test
 public void testGetNestedField() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 Address address = new Address();
 address.setCity("New York");
 address.setCountry("USA");
 person.setAddress(address);
 String json = personAdapter.toJson(person).getAsString();
 JsonObject jsonObject = JsonElement.parse(json).getAsJsonObject();
 Person readPerson = personAdapter.fromJson(jsonObject, Person.class);

 Field field = Address.class.getDeclaredField("city");
 field.setAccessible(true);
 String readCity = (String) field.get(readPerson.getAddress());
 assertEquals("New York", readCity);
 }

 @Test
 public void testGetFieldWithExclusion() throws Exception {
 ReflectiveTypeAdapterFactory factory = CONSTRUCTOR.newInstance(
     new ConstructorConstructor(), new FieldNamingStrategyOf(), new ExcluderOf());
 TypeToken<Person> personTypeToken = new TypeToken<Person>(){}.getType();
 TypeAdapter<Person> personAdapter = factory.create(GSON, personTypeToken);

 Person person = new Person("John Doe");
 person.setExcludedField("excluded");
 String json = personAdapter.toJson(person).getAsString();
 JsonObject jsonObject = JsonElement.parse(json).getAsJsonObject();
 Person readPerson = personAdapter.fromJson(jsonObject, Person.class);

 Field field = Person.class.getDeclaredField("excluded");
 field.setAccessible(true);
 assertNull(field.get(readPerson));
 }

 private static class Person {
 private String name;
 private int age = -1;
 private List<String> list = Collections.emptyList();
 private Map<String, String> map = Collections.emptyMap();
 private Address address;
 private String excluded;

 public Person(String name) {
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }

 public List<String> getList() {
 return list;
 }

 public void setList(List<String> list) {
 this.list = list;
 }

 public Map<String, String> getMap() {
 return map;
 }

 public void setMap(Map<String, String> map) {
 this.map = map;
 }

 public Address getAddress() {
 return address;
 }

 public void setAddress(Address address) {
 this.address = address;
 }

 public String getExcludedField() {
 return excluded;
 }

 public void setExcludedField(String excluded) {
 this.excluded = excluded;
 }

 public static class Address {
 private String city;
 private String country;

 public String getCity() {
 return city;
 }

 public void setCity(String city) {
 this.city = city;
 }

 public String getCountry() {
 return country;
 }

 public void setCountry(String country) {
 this.country = country;
 }
 }
 }

 private static class FieldNamingStrategyOf extends FieldNamingPolicy {
 @Override public String translateName(Field f) {
 return f.getName();
 }
 }

 private static class ExcluderOf extends Excluder {
 @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
 return false;
 }

 @Override public boolean excludeField(Field f, boolean serialize) {
 return f.isAnnotationPresent(SerializedName.class) && f.getName().equals("excluded");
 }
 }
}