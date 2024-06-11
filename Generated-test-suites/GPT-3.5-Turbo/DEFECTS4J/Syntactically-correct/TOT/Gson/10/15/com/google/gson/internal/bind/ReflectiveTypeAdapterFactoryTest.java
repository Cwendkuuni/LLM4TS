import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReflectiveTypeAdapterFactoryTest {

    private Gson gson;

    @Before
    public void setUp() {
        gson = new Gson();
    }

    @Test
    public void testCreate_withValidTypeToken_returnsTypeAdapter() {
        TypeToken<?> typeToken = TypeToken.get(ReflectiveTypeAdapterFactory.class);
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);

        assertNotNull(reflectiveTypeAdapterFactory.create(gson, typeToken));
    }

    @Test
    public void testCreate_withNonAssignableType_returnsNull() {
        TypeToken<?> typeToken = TypeToken.get(Integer.class);
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);

        assertNull(reflectiveTypeAdapterFactory.create(gson, typeToken));
    }

    @Test
    public void testRead_withValidJson_returnsDeserializedObject() {
        String json = "{\"name\":\"John Doe\",\"age\":30}";
        ReflectiveTypeAdapterFactory.Adapter<Person> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(
                () -> new Person(), null);

        Person person = adapter.read(gson.newJsonReader(json));

        assertNotNull(person);
        assertEquals("John Doe", person.getName());
        assertEquals(30, person.getAge());
    }

    @Test
    public void testRead_withNullJson_returnsNull() {
        ReflectiveTypeAdapterFactory.Adapter<Person> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(
                () -> new Person(), null);

        Person person = adapter.read(gson.newJsonReader(null));

        assertNull(person);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testRead_withInvalidJson_throwsJsonSyntaxException() {
        String json = "{\"name\":\"John Doe\",\"age\":}";
        ReflectiveTypeAdapterFactory.Adapter<Person> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(
                () -> new Person(), null);

        adapter.read(gson.newJsonReader(json));
    }

    @Test
    public void testWrite_withValidObject_writesJson() {
        Person person = new Person("John Doe", 30);
        ReflectiveTypeAdapterFactory.Adapter<Person> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(
                () -> new Person(), null);

        JsonObject jsonObject = gson.toJsonTree(person).getAsJsonObject();

        assertNotNull(jsonObject);
        assertEquals("John Doe", jsonObject.get("name").getAsString());
        assertEquals(30, jsonObject.get("age").getAsInt());
    }

    @Test
    public void testWrite_withNullObject_writesNullJson() {
        ReflectiveTypeAdapterFactory.Adapter<Person> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(
                () -> new Person(), null);

        JsonObject jsonObject = gson.toJsonTree(null, Person.class).getAsJsonObject();

        assertNull(jsonObject);
    }

}