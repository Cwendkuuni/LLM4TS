import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.js.Serializers;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(11, allDeserializers.size()); // Check the size of the map after adding all serializers
    }
}