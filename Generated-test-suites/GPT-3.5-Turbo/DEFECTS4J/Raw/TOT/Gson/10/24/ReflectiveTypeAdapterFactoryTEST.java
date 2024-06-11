
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {
    private ReflectiveTypeAdapterFactory.TypedAdapter adapter;
    private TestObject testObject;

    @Before
    public void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        Excluder excluder = Excluder.DEFAULT;
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, null, excluder);
        Gson gson = new Gson();
        adapter = new ReflectiveTypeAdapterFactory.TypedAdapter(reflectiveTypeAdapterFactory, gson);
        testObject = new TestObject();
    }

    @Test
    public void testWrite() throws IllegalAccessException, IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        adapter.write(jsonWriter, testObject);
        jsonWriter.close();
        String jsonString = stringWriter.toString();

        Assert.assertEquals("{\"field1\":\"value1\",\"field2\":2}", jsonString);
    }

    @Test
    public void testRead() throws IllegalAccessException, IOException {
        String jsonString = "{\"field1\":\"value1\",\"field2\":2}";
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        TestObject result = (TestObject) adapter.read(jsonReader);
        jsonReader.close();

        Assert.assertEquals("value1", result.field1);
        Assert.assertEquals(2, result.field2);
    }

    @Test
    public void testReadNull() throws IllegalAccessException, IOException {
        String jsonString = "null";
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        TestObject result = (TestObject) adapter.read(jsonReader);
        jsonReader.close();

        Assert.assertNull(result);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testReadInvalidFormat() throws IllegalAccessException, IOException {
        String jsonString = "{\"field1\":\"value1\",\"field2\":}";
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        adapter.read(jsonReader);
        jsonReader.close();
    }

    @Test
    public void testWriteNull() throws IllegalAccessException, IOException {
        testObject = null;
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        adapter.write(jsonWriter, testObject);
        jsonWriter.close();
        String jsonString = stringWriter.toString();

        Assert.assertEquals("null", jsonString);
    }

    private static class TestObject {
        @SerializedName("field1")
        private String field1 = "value1";
        @SerializedName("field2")
        private int field2 = 2;
    }
}
