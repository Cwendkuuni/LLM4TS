import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {
    // ...

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"typeId\":\"com.example.TestClass\", \"value\":{\"name\":\"Test\", \"age\":42}}";
        JsonParser jp = mapper.getFactory().createParser(jsonString);
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, "_type", true, TestClass.class);
        Object result = deserializer.deserializeTypedFromObject(jp, new TestContext());
        TestClass expected = new TestClass("Test", 42);
        assertEquals(expected, result);
    }

    // ...

    static class TestContext extends DeserializationContext {
        // ...
    }

    // ...
}