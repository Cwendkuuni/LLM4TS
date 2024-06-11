
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;

public class NumberSerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAddAll() throws Exception {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertNotNull(allDeserializers.get(Integer.class.getName()));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllWithNullMap() throws Exception {
        NumberSerializers.addAll(null);
    }

    @Test
    public void testShortSerializer_ValidInput() throws Exception {
        ShortSerializer shortSerializer = new ShortSerializer();
        Short shortValue = Short.parseShort("123");
        String json = objectMapper.writeValueAsString(shortValue);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Short deserializedShort = jsonParser.readValueAs(Short.class);

        assertEquals(shortValue, deserializedShort);
    }

    @Test(expected = JsonMappingException.class)
    public void testShortSerializer_InvalidInput() throws Exception {
        ShortSerializer shortSerializer = new ShortSerializer();
        String invalidInput = "abc";
        String json = objectMapper.writeValueAsString(shortSerializer);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        assertFalse(jsonParser.readValueAs(Short.class) instanceof Short);
    }

    // Add test cases for other edge cases and exception handling scenarios in the remaining methods

    @After
    public void teardown() {
        objectMapper = null;
    }
}