import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserialize_validInputWithValues() throws Exception {
        String json = "[\"value1\",\"value2\",\"value3\"]";
        Collection<String> expected = Arrays.asList("value1", "value2", "value3");

        Collection<String> result = mapper.readValue(json, Collection.class);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_validInputWithNullValue() throws Exception {
        String json = "[null]";
        Collection<String> expected = Collections.singletonList(null);

        Collection<String> result = mapper.readValue(json, Collection.class);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_validInputWithEmptyArray() throws Exception {
        String json = "[]";
        Collection<String> expected = Collections.emptyList();

        Collection<String> result = mapper.readValue(json, Collection.class);

        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_invalidInputWithNonArray() throws Exception {
        String json = "\"value\"";
        Collection<String> result = mapper.readValue(json, Collection.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_invalidInputWithMalformedJson() throws Exception {
        String json = "[\"value1\",\"value2\"";
        Collection<String> result = mapper.readValue(json, Collection.class);
    }

    @Test
    public void testDeserialize_validInputWithAdditionalFields() throws Exception {
        String json = "[\"value1\",\"value2\"]{\"field\":\"value\"}";
        Collection<String> expected = Arrays.asList("value1", "value2");

        Collection<String> result = mapper.readValue(json, Collection.class);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_validInputWithSingleValue() throws Exception {
        String json = "\"value\"";
        Collection<String> expected = Collections.singletonList("value");

        Collection<String> result = mapper.readValue(json, Collection.class);

        assertEquals(expected, result);
    }
}