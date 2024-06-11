import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;;

public class StringArrayDeserializerTest {
    @Test
    public void testDefaultDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"hello\", \"world\"]";
        StringArrayDeserializer serializer = StringArrayDeserializer.instance;
        String[] result = mapper.readValue(json, serializer.getType());
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }
}