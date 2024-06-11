
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws IOException {
        // Create a JsonParserSequence with two parsers
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Close the JsonParserSequence
        jsonParserSequence.close();

        // Check that both parsers are closed
        Assert.assertTrue(parser1.isClosed());
        Assert.assertTrue(parser2.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        // Create a JsonParserSequence with two parsers
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Set the next token for parser1
        jsonParserSequence.delegate = parser1;
        parser1.setNextToken(JsonToken.START_OBJECT);

        // Get the next token from the JsonParserSequence
        JsonToken token = jsonParserSequence.nextToken();

        // Check that the token is START_OBJECT
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        // Check that the delegate is now parser2
        Assert.assertEquals(parser2, jsonParserSequence.delegate);
    }

    @Test
    public void testContainedParsersCount() {
        // Create a JsonParserSequence with two parsers
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Check that the contained parsers count is 2
        Assert.assertEquals(2, jsonParserSequence.containedParsersCount());
    }
}
