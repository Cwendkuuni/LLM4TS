import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"hello\", \"world\", \"jackson\"]";
        String[] expected = {"hello", "world", "jackson"};
        String[] result = mapper.readValue(json, StringArrayDeserializer.class);
        Assertions.assertArrayEquals(expected, result);
    }
}