import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.*;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws Exception {
        // Test Case: Close called on JsonParserSequence

        // Create the parsers
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new DummyJsonParser();
        parsers[1] = new DummyJsonParser();

        // Create the JsonParserSequence
        JsonParserSequence sequence = new JsonParserSequence(parsers);

        // Close the sequence
        sequence.close();

        // Verify that close was called on each parser
        assertEquals(true, ((DummyJsonParser) parsers[0]).isCloseCalled());
        assertEquals(true, ((DummyJsonParser) parsers[1]).isCloseCalled());
    }

    @Test
    public void testNextToken() throws Exception {
        // Test Case: Next token called on JsonParserSequence

        // Create the delegates
        DummyJsonParser delegate1 = new DummyJsonParser();
        DummyJsonParser delegate2 = new DummyJsonParser();

        // Set the tokens to be returned by each delegate
        delegate1.addToken(JsonToken.START_ARRAY);
        delegate2.addToken(JsonToken.VALUE_NUMBER_INT);

        // Create the JsonParserSequence
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { delegate1, delegate2 });

        // Call nextToken
        JsonToken token = sequence.nextToken();

        // Verify that the correct token is returned
        assertEquals(JsonToken.START_ARRAY, token);

        // Call nextToken again
        token = sequence.nextToken();

        // Verify that the correct token is returned
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
    }

    @Test
    public void testContainedParsersCount() throws Exception {
        // Test Case: ContainedParsersCount called on JsonParserSequence

        // Create the parsers
        JsonParser[] parsers = new JsonParser[3];
        parsers[0] = new DummyJsonParser();
        parsers[1] = new DummyJsonParser();
        parsers[2] = new DummyJsonParser();

        // Create the JsonParserSequence
        JsonParserSequence sequence = new JsonParserSequence(parsers);

        // Call containedParsersCount
        int count = sequence.containedParsersCount();

        // Verify that the correct count is returned
        assertEquals(3, count);
    }
}