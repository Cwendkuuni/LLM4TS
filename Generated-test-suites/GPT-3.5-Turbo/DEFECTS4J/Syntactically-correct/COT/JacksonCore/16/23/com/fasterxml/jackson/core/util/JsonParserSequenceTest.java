
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser parser1 = null; // create parser1
        JsonParser parser2 = null; // create parser2
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertNotNull(sequence);
    }

    @Test
    public void testContainedParsersCount() {
        JsonParser parser1 = null; // create parser1
        JsonParser parser2 = null; // create parser2
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testClose() {
        JsonParser parser1 = null; // create parser1
        JsonParser parser2 = null; // create parser2
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        try {
            sequence.close();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextToken() {
        JsonParser parser1 = null; // create parser1
        JsonParser parser2 = null; // create parser2
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        try {
            assertNotNull(sequence.nextToken());
        } catch (IOException | JsonParseException e) {
            fail("Exception should not be thrown");
        }
    }
}
