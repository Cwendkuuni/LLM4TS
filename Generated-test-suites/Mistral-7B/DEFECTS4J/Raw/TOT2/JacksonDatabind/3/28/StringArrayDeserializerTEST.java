import org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "[\"Hello\", \"World\", \"Java\"]";
        JsonParser jsonParser = jsonFactory.createParser(jsonString);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        String[] expected = {"Hello", "World", "Java"};
        Assert.assertArrayEquals(expected, result);
    }
}