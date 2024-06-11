import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectionTypeAdapterFactory() {
    Class testClass = ReflectiveTypeAdapterFactory.class;
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(Excluder.DEFAULT)
        .create();
    TypeAdapterFactory adapterFactory = gson.getAdapterFactory();

    TypeAdapter<Person> personAdapter = adapterFactory.create(gson, new TypeToken<Person>() {});
    Person person = new Person("Alice", 30);
    String json = gson.toJson(person);

    JsonElement jsonElement = new JsonParser().parse(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("Alice", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());

    Person deserializedPerson = personAdapter.read(new JsonReader(new StringReader(json)));
    assertEquals(person, deserializedPerson);
  }

  @Test
  public void testReflectionTypeAdapterFactoryWithAnnotations() {
    Class testClass = ReflectiveTypeAdapterFactory.class;
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(Excluder.DEFAULT)
        .create();
    TypeAdapterFactory adapterFactory = gson.getAdapterFactory();

    class PersonWithAnnotation {
      @SerializedName("age_in_years")
      int age;

      @JsonAdapter(IntegerStringAdapter.class)
      String name;
    }

    TypeAdapter<PersonWithAnnotation> personAdapter = adapterFactory.create(gson, new TypeToken<PersonWithAnnotation>() {});
    PersonWithAnnotation person = new PersonWithAnnotation();
    person.age = 30;
    person.name = "Alice";
    String json = gson.toJson(person);

    JsonElement jsonElement = new JsonParser().parse(json);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("Alice", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age_in_years").getAsInt());

    PersonWithAnnotation deserializedPerson = personAdapter.read(new JsonReader(new StringReader(json)));
    assertEquals(person, deserializedPerson);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReflectionTypeAdapterFactoryWithExcludedFields() {
    Class testClass = ReflectiveTypeAdapterFactory.class;
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(Excluder.DEFAULT)
        .create();
    TypeAdapterFactory adapterFactory = gson.getAdapterFactory();

    class PersonWithExcludedFields {
      private int excludedAge;
      String name;

      @Exclude
      boolean excludedBoolean;

      @Exclude(serialize = true)
      List<String> excludedList;

      ReflectiveTypeAdapterFactory.BoundField excludedBoundField = new ReflectiveTypeAdapterFactory.BoundField("excludedField", false, false) {
        @Override
        public boolean writeField(Object value) throws IOException, IllegalAccessException {
          throw new RuntimeException("Should not be called");
        }
      };
    }

    TypeAdapter<PersonWithExcludedFields> personAdapter = adapterFactory.create(gson, new TypeToken<PersonWithExcludedFields>() {});
    PersonWithExcludedFields person = new PersonWithExcludedFields();
    person.name = "Alice";
    person.excludedBoolean = true;
    person.excludedList = new ArrayList<>();
    person.excludedList.add("item1");

    try {
      String json = gson.toJson(person);
      fail();
    } catch (JsonSyntaxException e) {
      // expected
    }
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReflectionTypeAdapterFactoryWithJsonAdapterException() {
    Class testClass = ReflectiveTypeAdapterFactory.class;
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(Excluder.DEFAULT)
        .create();
    TypeAdapterFactory adapterFactory = gson.getAdapterFactory();

    class CustomJsonAdapterException extends JsonAdapter<String, Integer> {
      @Override public Integer read(JsonReader in) throws IOException {
        throw new JsonSyntaxException();
      }

      @Override public void write(JsonWriter out, Integer value) throws IOException {
        out.value("InvalidIntValue");
      }
    }

    class PersonWithJsonAdapterException {
      @JsonAdapter(CustomJsonAdapterException.class)
      int intField;
    }

    TypeAdapter<PersonWithJsonAdapterException> personAdapter = adapterFactory.create(gson, new TypeToken<PersonWithJsonAdapterException>() {});
    PersonWithJsonAdapterException person = new PersonWithJsonAdapterException();
    person.intField = 30;

    String json = gson.toJson(person);

    try {
      PersonWithJsonAdapterException deserializedPerson = personAdapter.read(new JsonReader(new StringReader(json)));
      fail();
    } catch (JsonSyntaxException e) {
      // expected
    }
  }
}

class JsonParser {
  public JsonElement parse(String jsonString) {
    return new JsonParser().parse(new StringReader(jsonString));
  }
}