import org.junit.*;
import static org.junit.Assert.*;
import java.util.Collection;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer;
    private JsonParser parser;
    private DeserializationContext context;

    @Before
    public void setUp() throws Exception {
        deserializer = new StringCollectionDeserializer(JavaType.collectionType(String.class),
                new JsonStringDeserializer(), new DefaultValueInstantiator());
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        parser = new JsonParser(json);
        context = new DeserializationContext();
        
        Collection<String> result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
        assertTrue(result.contains("value3"));
    }

    @Test
    public void testDeserializeWithNullValues() throws Exception {
        String json = "[null, \"value1\", null, \"value2\", null]";
        parser = new JsonParser(json);
        context = new DeserializationContext();
        
        Collection<String> result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertFalse(result.contains(null));
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
    }

    @Test
    public void testDeserializeWithEmptyArray() throws Exception {
        String json = "[]";
        parser = new JsonParser(json);
        context = new DeserializationContext();
        
        Collection<String> result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithNonArrayValue() throws Exception {
        String json = "\"value1\"";
        parser = new JsonParser(json);
        context = new DeserializationContext();
        
        Collection<String> result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("value1"));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidValue() throws Exception {
        String json = "[\"value1\", true, \"value2\"]";
        parser = new JsonParser(json);
        context = new DeserializationContext();
        
        deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        String json = "\"value1\"";
        parser = new JsonParser(json);
        context = new DeserializationContext();
        StringCollectionTypeDeserializer typeDeserializer = new StringCollectionTypeDeserializer();
        
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);

        assertNotNull(result);
        assertTrue(result instanceof Collection);
        Collection<String> collectionResult = (Collection<String>) result;
        assertEquals(1, collectionResult.size());
        assertTrue(collectionResult.contains("value1"));
    }

    @Test
    public void testGetContentDeserializer() throws Exception {
        JsonDeserializer<String> contentDeserializer = deserializer.getContentDeserializer();

        assertNotNull(contentDeserializer);
        assertTrue(contentDeserializer instanceof JsonStringDeserializer);
    }

    @Test
    public void testGetValueInstantiator() throws Exception {
        ValueInstantiator valueInstantiator = deserializer.getValueInstantiator();

        assertNotNull(valueInstantiator);
        assertTrue(valueInstantiator instanceof DefaultValueInstantiator);
    }
}