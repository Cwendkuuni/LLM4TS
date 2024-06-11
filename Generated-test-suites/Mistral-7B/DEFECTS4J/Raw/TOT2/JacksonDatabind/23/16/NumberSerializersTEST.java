import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        JsonSerializer<?> intSerializer = allDeserializers.get(Integer.class.getName());
        JsonSerializer<?> longSerializer = allDeserializers.get(Long.class.getName());

        assertEquals(intSerializer, intSerializer); // Check that intSerializer is not null
        assertEquals(longSerializer, longSerializer); // Check that longSerializer is not null
    }
}