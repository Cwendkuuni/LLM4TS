import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        
        // Test case for empty string input
        String input1 = "";
        JsonParser jp1 = jsonFactory.createParser(input1);
        jp1.nextToken();
        String[] result1 = StringArrayDeserializer.instance.deserialize(jp1, ctxt);
        Assert.assertNull("Test case for deserializing empty string", result1);

        // Test case for null values in the array
        String input2 = "[\"str1\",null,\"str2\",null,\"str3\"]";
        JsonParser jp2 = jsonFactory.createParser(input2);
        jp2.nextToken();
        String[] result2 = StringArrayDeserializer.instance.deserialize(jp2, ctxt);
        String[] expected2 = {"str1", null, "str2", null, "str3"};
        Assert.assertArrayEquals("Test case for deserializing null values", expected2, result2);

        // Test case for normal input
        String input3 = "[\"str1\",\"str2\",\"str3\"]";
        JsonParser jp3 = jsonFactory.createParser(input3);
        jp3.nextToken();
        String[] result3 = StringArrayDeserializer.instance.deserialize(jp3, ctxt);
        String[] expected3 = {"str1", "str2", "str3"};
        Assert.assertArrayEquals("Test case for normal input", expected3, result3);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        // Test case for deserializing with type
        String input = "[\"str1\",\"str2\",\"str3\"]";
        JsonParser jp = jsonFactory.createParser(input);
        jp.nextToken();
        Object result = StringArrayDeserializer.instance.deserializeWithType(jp, ctxt, null);
        String[] expected = {"str1", "str2", "str3"};
        Assert.assertArrayEquals("Test case for deserializing with type", expected, (String[]) result);
    }
}