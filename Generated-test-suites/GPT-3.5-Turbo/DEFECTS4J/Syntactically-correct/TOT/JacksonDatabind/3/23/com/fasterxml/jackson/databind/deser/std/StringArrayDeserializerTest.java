import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_withValidArray() throws IOException {
        StringArrayDeserializer deser = StringArrayDeserializer.instance;

        // Creating a JSON parser
        JsonParser jp = new ObjectMapper().getFactory().createParser("[\"value1\", \"value2\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Deserializing the JSON array
        String[] result = deser.deserialize(jp, ctxt);

        // Asserting the result
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, result);
    }

    @Test
    public void testDeserialize_withEmptyArray() throws IOException {
        StringArrayDeserializer deser = StringArrayDeserializer.instance;

        // Creating a JSON parser
        JsonParser jp = new ObjectMapper().getFactory().createParser("[]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Deserializing the JSON array
        String[] result = deser.deserialize(jp, ctxt);

        // Asserting the result
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_withInvalidInputShouldThrowException() throws IOException {
        StringArrayDeserializer deser = StringArrayDeserializer.instance;

        // Creating a JSON parser
        JsonParser jp = new ObjectMapper().getFactory().createParser("{ \"key\": \"value\" }");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Deserializing the JSON array, which is not a valid input
        deser.deserialize(jp, ctxt);
    }

    @Test
    public void testDeserialize_withNullElement_shouldDeserializeAsNull() throws IOException {
        StringArrayDeserializer deser = StringArrayDeserializer.instance;

        // Creating a JSON parser
        JsonParser jp = new ObjectMapper().getFactory().createParser("[\"value1\", null, \"value3\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Deserializing the JSON array with a null element
        String[] result = deser.deserialize(jp, ctxt);

        // Asserting the result
        String[] expected = new String[]{"value1", null, "value3"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_withEmptyStringValue_shouldDeserializeAsNullObject() throws IOException {
        StringArrayDeserializer deser = StringArrayDeserializer.instance;

        // Creating a JSON parser
        JsonParser jp = new ObjectMapper().getFactory().createParser("[\"value1\", \"\", \"value3\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Deserializing the JSON array with an empty string value
        String[] result = deser.deserialize(jp, ctxt);

        // Asserting the result
        String[] expected = new String[]{"value1", null, "value3"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_whenSingleValueIsAccepted_shouldDeserializeSingleValueArray() throws IOException {
        StringArrayDeserializer deser = StringArrayDeserializer.instance;

        // Creating a JSON parser
        JsonParser jp = new ObjectMapper().getFactory().createParser("\"singleValue\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        // Deserializing the JSON array with a single value
        String[] result = deser.deserialize(jp, ctxt);

        // Asserting the result
        Assert.assertArrayEquals(new String[]{"singleValue"}, result);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_whenSingleValueIsNotAccepted_shouldThrowException() throws IOException {
        StringArrayDeserializer deser = StringArrayDeserializer.instance;

        // Creating a JSON parser
        JsonParser jp = new ObjectMapper().getFactory().createParser("\"singleValue\"");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        ctxt.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        // Deserializing the JSON array with a single value, which is not allowed
        deser.deserialize(jp, ctxt);
    }

}