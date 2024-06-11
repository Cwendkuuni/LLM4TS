import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReflectiveTypeAdapterFactoryTest {

    private Gson gson;

    @Before
    public void setup() {
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory())
                .excludeFieldsWithoutExposeAnnotation()
                .excludeFieldsWithModifiers()
                .create();
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException {
        // Test data
        TestClass testObject = new TestClass();
        testObject.setId(1);
        testObject.setName("Test Object");

        // Serialize the test object
        String json = gson.toJson(testObject);

        // Deserialize the JSON
        TestClass deserializedObject = gson.fromJson(json, TestClass.class);

        // Verify the deserialized object
        assertEquals(testObject.getId(), deserializedObject.getId());
        assertEquals(testObject.getName(), deserializedObject.getName());
    }

    @Test
    public void testNullObject() throws IOException {
        // Serialize a null object
        String json = gson.toJson(null);

        // Deserialize the JSON
        TestClass deserializedObject = gson.fromJson(json, TestClass.class);

        // Verify the deserialized object is null
        assertNull(deserializedObject);
    }

    static class TestClass {

        @Expose
        private int id;

        @Expose
        @SerializedName("name")
        private String name;

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}