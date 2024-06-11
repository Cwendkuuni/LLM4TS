import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testAddAll() {
        assertNotNull(allDeserializers);
        assertEquals(6, allDeserializers.size());
    }

    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        assertNotNull(shortSerializer);
        assertEquals(Short.class, shortSerializer.handledType());
        assertEquals(JsonParser.NumberType.INT, shortSerializer._numberType);
        assertEquals("number", shortSerializer._schemaType);
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        assertNotNull(integerSerializer);
        assertEquals(Integer.class, integerSerializer.handledType());
        assertEquals(JsonParser.NumberType.INT, integerSerializer._numberType);
        assertEquals("integer", integerSerializer._schemaType);
    }

    // Additional test cases for other serializers can be added here

}