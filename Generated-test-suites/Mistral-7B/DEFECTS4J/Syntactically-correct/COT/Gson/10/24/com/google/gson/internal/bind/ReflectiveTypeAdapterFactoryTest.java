import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testReflectiveTypeAdapterFactory() {
        // Create a Gson instance with the ReflectiveTypeAdapterFactory
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setExcluder(new Excluder())
                .registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(
                        new ConstructorConstructor(),
                        new FieldNamingPolicy() {
                            @Override
                            public String translateName(Field field) {
                                return field.getName();
                            }
                        },
                        new Excluder() {
                            @Override
                            public boolean excludeClass(Class<?> clazz, boolean serialize) {
                                return false;
                            }

                            @Override
                            public boolean excludeField(Field field, boolean serialize) {
                                return false;
                            }
                        }))
                .create();

        // Test serialization and deserialization of a simple JavaBean
        User user = new User("John Doe", 30);
        String json = gson.toJson(user);
        JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
        assertNotNull(jsonElement);

        Type userType = new TypeToken<User>() {}.getType();
        User deserializedUser = gson.fromJson(jsonElement, userType);
        assertNotNull(deserializedUser);
        assert(deserializedUser.getName().equals("John Doe"));
        assert(deserializedUser.getAge() == 30);
    }
}

class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}