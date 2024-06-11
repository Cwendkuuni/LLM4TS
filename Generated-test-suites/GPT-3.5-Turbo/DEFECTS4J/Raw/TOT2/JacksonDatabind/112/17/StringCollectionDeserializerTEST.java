import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize_basic() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonFactory factory = new JsonFactory();
        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser parser = factory.createParser(json);

        List<String> result = deserializer.deserialize(parser, DeserializationContext.emptyInstance());
        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");
        expected.add("value3");

        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_nonArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonFactory factory = new JsonFactory();
        String json = "\"non-array\"";
        JsonParser parser = factory.createParser(json);

        deserializer.deserialize(parser, DeserializationContext.emptyInstance());
    }

    // Add more test cases for other methods and edge cases

}