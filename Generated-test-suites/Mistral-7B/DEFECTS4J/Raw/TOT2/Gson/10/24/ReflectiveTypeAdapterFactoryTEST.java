import com.google.gson.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testCreateTypeAdapter() {
    Gson gson = new GsonBuilder().create();
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName().toLowerCase();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }
    );
    TypeAdapter<Person> adapter = factory.create(gson, new TypeToken<Person>() {});
    assertNotNull(adapter);
  }

  @Test
  public void testSerialization() throws Exception {
    Person person = new Person("John Doe", 30);
    Gson gson = new GsonBuilder().create();
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName().toLowerCase();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }
    );
    TypeAdapter<Person> adapter = factory.create(gson, new TypeToken<Person>() {});

    String json = adapter.toJson(person);
    Person deserializedPerson = adapter.fromJson(json, Person.class);
    assertEquals(person, deserializedPerson);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testSerializationWithNullName() throws Exception {
    Person person = new Person(null, 30);
    Gson gson = new GsonBuilder().create();
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName().toLowerCase();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }
    );
    TypeAdapter<Person> adapter = factory.create(gson, new TypeToken<Person>() {});

    String json = adapter.toJson(person);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializationWithNullName() throws Exception {
    String json = "{\"age\":30}";
    Gson gson = new GsonBuilder().create();
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName().toLowerCase();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }
    );
    TypeAdapter<Person> adapter = factory.create(gson, new TypeToken<Person>() {});

    Person person = adapter.fromJson(json, Person.class);
    assertNull(person);
  }

  @Test(expected = IllegalAccessException.class)
  public void testDeserializationWithInaccessibleField() throws Exception {
    class InaccessiblePerson {
      private String name;
      private int age;

      InaccessiblePerson(String name, int age) {
        this.name = name;
        this.age = age;
      }

      private String getName() {
        return name;
      }
    }

    String json = "{\"name\":\"John Doe\",\"age\":30}";
    Gson gson = new GsonBuilder().create();
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName().toLowerCase();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }
    );
    TypeAdapter<InaccessiblePerson> adapter = factory.create(gson, new TypeToken<InaccessiblePerson>() {});

    InaccessiblePerson person = adapter.fromJson(json, InaccessiblePerson.class);
  }

  @Test
  public void testDeserializationWithMissingField() throws Exception {
    String json = "{\"age\":30}";
    Gson gson = new GsonBuilder().create();
    ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingStrategy() {
          @Override public String translateName(Field field) {
            return field.getName().toLowerCase();
          }
        },
        new Excluder() {
          @Override public boolean excludeClass(Class<?> clazz, boolean serialize) {
            return false;
          }

          @Override public boolean excludeField(Field field, boolean serialize) {
            return false;
          }
        }
    );
    TypeAdapter<Person> adapter = factory.create(gson, new TypeToken<Person>() {});

    Person person = adapter.fromJson(json, Person.class);
    assertNull(person.name);
    assertEquals(30, person.age);
  }

  static class Person {
    String name;
    int age;

    Person(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }
}