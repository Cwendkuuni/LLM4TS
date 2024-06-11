import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    @Test
    public void testAddAll() throws Exception {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        JsonSerializer<?> intSerializer = allDeserializers.get(Integer.class.getName());
        assertEquals(intSerializer, allDeserializers.get(Integer.TYPE.getName()));
        assertEquals(intSerializer, allDeserializers.get(Long.class.getName())); // Long is tested in LongSerializer test case
        // ... test other serializers in the map
    }
}