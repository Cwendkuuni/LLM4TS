import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

    private ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter;

    @Before
    public void setUp() {
        Gson gson = new Gson();
        adapter = new ReflectiveTypeAdapterFactory.Adapter<>(new DefaultObjectConstructor<>(), new HashMap<>());
    }

    @Test
    public void read_shouldDeserializeObject_correctly() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30}";
        JsonReader reader = new JsonReader(new StringReader(json));
        TestObject result = adapter.read(reader);
        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    public void write_shouldSerializeObject_correctly() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        TestObject object = new TestObject("John", 30);
        adapter.write(writer, object);
        writer.flush();
        String json = stringWriter.toString();

        assertEquals("{\"name\":\"John\",\"age\":30}", json);
    }

    @Test
    public void read_shouldThrowException_ifJsonIsInvalid() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,}";
        JsonReader reader = new JsonReader(new StringReader(json));
        assertThrows(JsonSyntaxException.class, () -> {
            adapter.read(reader);
        });
    }

    @Test
    public void read_shouldInitializeObjectWithNull_ifJsonIsNull() throws IOException {
        String json = "null";
        JsonReader reader = new JsonReader(new StringReader(json));
        TestObject result = adapter.read(reader);
        assertNull(result);
    }

    @Test
    public void write_shouldWriteNull_ifObjectIsNull() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        adapter.write(writer, null);
        writer.flush();
        String json = stringWriter.toString();

        assertEquals("null", json);
    }

    private static class DefaultObjectConstructor<T> implements ObjectConstructor<T> {
        @Override
        public T construct() {
            return null;
        }
    }

    private static class TestObject {
        private String name;
        private int age;

        public TestObject(String name, int age) {
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
}