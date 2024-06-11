import com.google.gson.*;
import com.google.gson.internal.ReflectiveTypeAdapterFactory;
import org.junit.Assert;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectionTypeAdapterFactory() {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder())
        .create();

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingPolicy() {
          @Override
          public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder()
    );

    // When
    TypeToken<Person> typeToken = new TypeToken<Person>() {};
    TypeAdapter<Person> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

    Person person = new Person("John Doe", 30);

    // Then
    String json = gson.toJson(person);
    Assert.assertEquals("{\"name\":\"John Doe\",\"age\":30}", json);

    Person deserializedPerson = gson.fromJson(json, Person.class);
    Assert.assertEquals(person, deserializedPerson);
  }

  @Test
  public void testReflectionTypeAdapterFactoryWithJsonAdapter() {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder())
        .create();

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingPolicy() {
          @Override
          public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder()
    );

    class WithJsonAdapter {
      @JsonAdapter(DateAdapter.class)
      private Date date;

      public WithJsonAdapter(Date date) {
        this.date = date;
      }

      public Date getDate() {
        return date;
      }
    }

    TypeToken<WithJsonAdapter> typeToken = new TypeToken<WithJsonAdapter>() {};
    TypeAdapter<WithJsonAdapter> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

    WithJsonAdapter withJsonAdapter = new WithJsonAdapter(new Date(System.currentTimeMillis()));

    // When
    String json = gson.toJson(withJsonAdapter);
    Assert.assertEquals("{\"date\":\"2022-06-29T15:23:22.215Z\"}", json);

    WithJsonAdapter deserializedWithJsonAdapter = gson.fromJson(json, WithJsonAdapter.class);
    Assert.assertEquals(withJsonAdapter, deserializedWithJsonAdapter);
  }

  @Test
  public void testReflectionTypeAdapterFactoryWithExcludedFields() {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder() {
          @Override
          public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return clazz.equals(String.class);
          }
        })
        .create();

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingPolicy() {
          @Override
          public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder() {
          @Override
          public boolean excludeField(Field field, boolean serialize) {
            return field.getName().equals("name");
          }
        }
    );

    class PersonWithExcludedFields {
      private String name;
      @Exclude private int age;

      public String getName() {
        return name;
      }

      public int getAge() {
        return age;
      }

      public PersonWithExcludedFields(String name, int age) {
        this.name = name;
        this.age = age;
      }
    }

    TypeToken<PersonWithExcludedFields> typeToken = new TypeToken<PersonWithExcludedFields>() {};
    TypeAdapter<PersonWithExcludedFields> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

    PersonWithExcludedFields personWithExcludedFields = new PersonWithExcludedFields("John Doe", 30);

    // When
    String json = gson.toJson(personWithExcludedFields);
    Assert.assertEquals("{}", json);

    PersonWithExcludedFields deserializedPersonWithExcludedFields = gson.fromJson(json, PersonWithExcludedFields.class);
    Assert.assertNull(deserializedPersonWithExcludedFields);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReflectionTypeAdapterFactoryWithInvalidJson() {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder())
        .create();

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingPolicy() {
          @Override
          public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder()
    );

    TypeToken<String> typeToken = new TypeToken<String>() {};
    TypeAdapter<String> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

    String json = "{";

    // When
    String result = typeAdapter.read(new JsonReader(new StringReader(json)));

    // Then - exception should be thrown
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReflectionTypeAdapterFactoryWithIllegalAccess() {
    // Given
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setExcluder(new Excluder())
        .create();

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingPolicy() {
          @Override
          public String translateName(Field field) {
            return field.getName();
          }
        },
        new Excluder()
    );

    class PersonWithPrivateSetter {
      private String name;

      public String getName() {
        return name;
      }

      private void setName(String name) {
        this.name = name;
      }

      public PersonWithPrivateSetter(String name) {
        this.name = name;
      }
    }

    TypeToken<PersonWithPrivateSetter> typeToken = new TypeToken<PersonWithPrivateSetter>() {};
    TypeAdapter<PersonWithPrivateSetter> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

    PersonWithPrivateSetter personWithPrivateSetter = new PersonWithPrivateSetter("John Doe");

    // When
    String json = gson.toJson(personWithPrivateSetter);

    // Then - exception should be thrown
    typeAdapter.write(new JsonWriter(new StringWriter()), null);
  }
}