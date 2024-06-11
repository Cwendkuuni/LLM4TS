import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.*;

public class JsonParserSequenceTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        JsonParser[] parsers = {new DummyJsonParser()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        assertEquals(sequence._parsers.length, 1);
        assertEquals(sequence._nextParser, 1);
    }

    // Test case for createFlattened() method
    // When both parsers are not instances of JsonParserSequence
    @Test
    public void testCreateFlattened_BothNonSequenceParsers() {
        JsonParser first = new DummyJsonParser();
        JsonParser second = new DummyJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        assertEquals(sequence._parsers.length, 2);
        assertEquals(sequence._nextParser, 1);
    }

    // Test case for createFlattened() method
    // When only the first parser is an instance of JsonParserSequence
    @Test
    public void testCreateFlattened_FirstSequenceParser() {
        JsonParserSequence first = new JsonParserSequence(new JsonParser[]{new DummyJsonParser()});
        JsonParser second = new DummyJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        assertEquals(sequence._parsers.length, 2);
        assertEquals(sequence._nextParser, 1);
    }

    // Test case for createFlattened() method
    // When only the second parser is an instance of JsonParserSequence
    @Test
    public void testCreateFlattened_SecondSequenceParser() {
        JsonParser first = new DummyJsonParser();
        JsonParserSequence second = new JsonParserSequence(new JsonParser[]{new DummyJsonParser()});
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        assertEquals(sequence._parsers.length, 2);
        assertEquals(sequence._nextParser, 1);
    }

    // Test case for createFlattened() method
    // When both parsers are instances of JsonParserSequence
    @Test
    public void testCreateFlattened_BothSequenceParsers() {
        JsonParserSequence first = new JsonParserSequence(new JsonParser[]{new DummyJsonParser()});
        JsonParserSequence second = new JsonParserSequence(new JsonParser[]{new DummyJsonParser()});
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        assertEquals(sequence._parsers.length, 2);
        assertEquals(sequence._nextParser, 1);
    }

    // Test case for close() method
    @Test
    public void testClose() throws IOException {
        JsonParser[] parsers = {new DummyJsonParser()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        sequence.close();
        // Check if close() method is called on the delegate parser
        assertEquals(((DummyJsonParser)parsers[0]).isCloseCalled(), true);
    }

    // Test case for nextToken() method
    // When there are tokens available in the current delegate parser
    @Test
    public void testNextToken_TokensAvailable() throws IOException, JsonParseException {
        JsonParser[] parsers = {new DummyJsonParser()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        JsonToken token = sequence.nextToken();
        // Check if nextToken() returns the token from the delegate parser
        assertEquals(token, JsonToken.START_OBJECT);
    }

    // Test case for nextToken() method
    // When there are no more tokens available in the current delegate parser
    // and there are no more parsers in the sequence
    @Test
    public void testNextToken_NoTokensAvailableNoMoreParsers() throws IOException, JsonParseException {
        JsonParser[] parsers = {new DummyJsonParser()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        // Consume available tokens
        sequence.nextToken();
        sequence.nextToken();
        JsonToken token = sequence.nextToken();
        // Check if nextToken() returns null when there are no more tokens and no more parsers
        assertEquals(token, null);
    }

    // Test case for nextToken() method
    // When there are no more tokens available in the current delegate parser
    // but there are more parsers in the sequence
    @Test
    public void testNextToken_NoTokensAvailableMoreParsers() throws IOException, JsonParseException {
        JsonParser[] parsers = {new DummyJsonParser(), new DummyJsonParser()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        // Consume available tokens in the first parser
        sequence.nextToken();
        sequence.nextToken();
        // Consume available tokens in the second parser
        sequence.nextToken();
        sequence.nextToken();
        JsonToken token = sequence.nextToken();
        // Check if nextToken() returns the token from the next available parser
        assertEquals(token, JsonToken.END_OBJECT);
    }

    // Test case for containedParsersCount() method
    @Test
    public void testContainedParsersCount() {
        JsonParser[] parsers = {new DummyJsonParser(), new DummyJsonParser()};
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        int count = sequence.containedParsersCount();
        // Check if containedParsersCount() returns the number of parsers in the sequence
        assertEquals(count, 2);
    }

    // Custom dummy implementation of JsonParser
    // Used for testing purposes only
    private class DummyJsonParser extends JsonParser {
        private boolean closeCalled = false;

        @Override
        public void close() throws IOException {
            closeCalled = true;
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return JsonToken.START_OBJECT;
        }

        public boolean isCloseCalled() {
            return closeCalled;
        }
    }

}