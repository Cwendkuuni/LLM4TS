import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new StringCollectionDeserializer();
    }

    @Test
    public void testDeserialize() {
        String json = "[\"string1\", \"string2\"]";
        Collection<String> result = deserializer.deserialize(json);
        assertEquals(2, result.size());
        assertTrue(result.contains("string1"));
        assertTrue(result.contains("string2"));
    }

    @Test
    public void testDeserializeWithEmptyString() {
        String json = "[]";
        Collection<String> result = deserializer.deserialize(json);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeWithNullValue() {
        String json = "[null]";
        Collection<String> result = deserializer.deserialize(json);
        assertEquals(1, result.size());
        assertNull(result.get(0));
    }

    @Test
    public void testDeserializeWithEmptyStringValue() {
        String json = "[\"\"]";
        Collection<String> result = deserializer.deserialize(json);
        assertEquals(1, result.size());
        assertTrue(result.contains(""));
    }

    @Test
    public void testDeserializeWithWhitespaceStringValue() {
        String json = "[\"  \"]";
        Collection<String> result = deserializer.deserialize(json);
        assertEquals(1, result.size());
        assertTrue(result.contains("  "));
    }

    @Test
    public void testDeserializeWithSpecialCharacters() {
        String json = "[\"!@#$%^&*()\", \"-=_+[]{};':\\\"<>,.?/\", \"\\u1234\"]";
        Collection<String> result = deserializer.deserialize(json);
        assertEquals(3, result.size());
        assertTrue(result.contains("!@#$%^&*()"));
        assertTrue(result.contains("-=_+[]{};':\"<>,.?/"));
        assertTrue(result.contains("ሴ"));
    }

}