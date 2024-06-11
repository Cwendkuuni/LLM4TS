
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testStringCollectionDeserializerConstructor() {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        assertNotNull(stringCollectionDeserializer);
    }

    @Test
    public void testIsCachable() {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        assertTrue(stringCollectionDeserializer.isCachable());
        stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null).withResolved(null, null, null, null);
        assertFalse(stringCollectionDeserializer.isCachable());
    }

    @Test
    public void testCreateContextual() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonDeserializer<?> deserializer = stringCollectionDeserializer.createContextual(mockContext(), mockProperty());
        assertNotNull(deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullInput() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonParser mockParser = mockParser(null);
        DeserializationContext mockContext = mockContext();
        Collection<String> result = stringCollectionDeserializer.deserialize(mockParser, mockContext);
        fail();
    }

    @Test
    public void testDeserializeEmptyInput() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonParser mockParser = mockParser(new StringReader("[]"));
        DeserializationContext mockContext = mockContext();
        Collection<String> result = stringCollectionDeserializer.deserialize(mockParser, mockContext);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeSingleStringInput() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonParser mockParser = mockParser(new StringReader("[\"" + "test" + "\"]"));
        DeserializationContext mockContext = mockContext();
        Collection<String> result = stringCollectionDeserializer.deserialize(mockParser, mockContext);
        assertEquals(1, result.size());
        assertEquals("test", result.get(0));
    }

    @Test
    public void testDeserializeMultipleStringsInput() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonParser mockParser = mockParser(new StringReader("[\"" + "test1" + "\",\"" + "test2" + "\"]"));
        DeserializationContext mockContext = mockContext();
        Collection<String> result = stringCollectionDeserializer.deserialize(mockParser, mockContext);
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0));
        assertEquals("test2", result.get(1));
    }

    @Test
    public void testDeserializeCustomValueDeserializer() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonDeserializer<String> customValueDeserializer = mock(JsonDeserializer.class);
        when(customValueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("test");
        StringCollectionDeserializer deserializerWithCustomValueDeserializer = stringCollectionDeserializer.withResolved(null, null, null, customValueDeserializer);
        JsonParser mockParser = mockParser(new StringReader("[]"));
        DeserializationContext mockContext = mockContext();
        Collection<String> result = deserializerWithCustomValueDeserializer.deserialize(mockParser, mockContext);
        assertEquals(1, result.size());
        assertEquals("test", result.get(0));
    }

    @Test
    public void testDeserializeNullValueInput() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonParser mockParser = mockParser(new StringReader("[]"));
        DeserializationContext mockContext = mockContext();
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode nullNode = jsonFactory.createNullValue();
        JsonParser nullParser = jsonFactory.createParser(nullNode);
        when(mockParser.nextValue()).thenReturn(nullNode);
        Collection<String> result = stringCollectionDeserializer.deserialize(mockParser, mockContext);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeEmptyArrayInput() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonParser mockParser = mockParser(new StringReader("[]"));
        DeserializationContext mockContext = mockContext();
        Collection<String> result = stringCollectionDeserializer.deserialize(mockParser, mockContext);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeEmptyStringArrayInput() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonParser mockParser = mockParser(new StringReader("[]"));
        DeserializationContext mockContext = mockContext();
        Collection<String> result = stringCollectionDeserializer.deserialize(mockParser, mockContext);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeEmptyStringListInput() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonParser mockParser = mockParser(new StringReader("[]"));
        DeserializationContext mockContext = mockContext();
        Collection<String> result = stringCollectionDeserializer.deserialize(mockParser, mockContext);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeEmptyStringArrayWithCustomValueDeserializerInput() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonDeserializer<String> customValueDeserializer = mock(JsonDeserializer.class);
        when(customValueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("test");
        StringCollectionDeserializer deserializerWithCustomValueDeserializer = stringCollectionDeserializer.withResolved(null, null, null, customValueDeserializer);
        JsonParser mockParser = mockParser(new StringReader("[]"));
        DeserializationContext mockContext = mockContext();
        Collection<String> result = deserializerWithCustomValueDeserializer.deserialize(mockParser, mockContext);
        assertEquals(0, result.size());
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeInvalidInput() throws JsonProcessingException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        JsonParser mockParser = mockParser(new StringReader("123"));
        DeserializationContext mockContext = mockContext();
        stringCollectionDeserializer.deserialize(mockParser, mockContext);
        fail();
    }
}