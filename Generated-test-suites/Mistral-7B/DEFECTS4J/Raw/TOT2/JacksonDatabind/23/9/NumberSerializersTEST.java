import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    @Test
    public void testAddAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        Map<String, Object> input = mapper.readValue(
                "{ \"Integer\": 1, \"Long\": 2, \"Byte\": 1, \"Short\": -1, \"Float\": 3.14, \"Double\": 2.71828}", typeRef);

        assertEquals(input, allDeserializers);
    }
}