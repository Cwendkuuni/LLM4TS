import static org.junit.Assert.*;
import org.junit.*;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;

    private final JsonParser parser1 = new JsonParser() {
        @Override
        public JsonToken nextToken() throws IOException {
            return JsonToken.START_OBJECT;
        }
        // Implement other methods as needed
    };

    private final JsonParser parser2 = new JsonParser() {
        @Override
        public JsonToken nextToken() throws IOException {
            return JsonToken.START_ARRAY;
        }
        // Implement other methods as needed
    };

    private final JsonParser parser3 = new JsonParser() {
        @Override
        public JsonToken nextToken() throws IOException {
            return null; // Simulating no more tokens
        }
        // Implement other methods as needed
    };

    @Before
    public void setup() {
        JsonParser[] parsers = {parser1, parser2, parser3};
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testNextToken_SuccessfulParsing_WhenParsersHaveTokensInSequence() throws IOException {
        assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        assertEquals(JsonToken.START_ARRAY, jsonParserSequence.nextToken());
    }

    @Test
    public void testNextToken_NullToken_WhenAllParsersHaveNoMoreTokens() throws IOException {
        assertNull(jsonParserSequence.nextToken());
    }

    @Test
    public void testClose_ClosesAllParsers_WhenCalled() throws IOException {
        jsonParserSequence.close();
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
        assertTrue(parser3.isClosed());
    }

    @Test
    public void testContainedParsersCount_ReturnsCorrectCount_WhenCalled() {
        assertEquals(3, jsonParserSequence.containedParsersCount());
    }
}