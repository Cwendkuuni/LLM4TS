import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class JsonParserSequenceTest {

    private JsonParserSequence parserSequence;

    @Before
    public void setUp() {
        JsonParser parser1 = new MockJsonParser();
        JsonParser parser2 = new MockJsonParser();
        parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
    }

    @Test
    public void testClose() throws IOException {
        parserSequence.close();
        Assert.assertTrue(parserSequence.switchToNext()); // The first parser should switch to the second parser
        Assert.assertTrue(parserSequence.switchToNext()); // The second parser should switch to null
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        Assert.assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken()); // First token should be START_OBJECT
        Assert.assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken()); // Second token should be FIELD_NAME
        Assert.assertEquals(JsonToken.VALUE_STRING, parserSequence.nextToken()); // Third token should be VALUE_STRING
        Assert.assertEquals(JsonToken.END_OBJECT, parserSequence.nextToken()); // Fourth token should be END_OBJECT
        Assert.assertNull(parserSequence.nextToken()); // After the sequence ends, it should return null
    }

    @Test
    public void testContainedParsersCount() {
        Assert.assertEquals(2, parserSequence.containedParsersCount()); // The sequence should contain 2 parsers
    }

    private class MockJsonParser extends JsonParser {
        // Implement mocked methods for testing
    }
}