import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.*;

public class JsonParserSequenceTest {

    @Test
    public void testContainedParsersCount() {
        JsonParser parser1 = new JsonFactory().createParser("{}");
        JsonParser parser2 = new JsonFactory().createParser("[]");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testCreateFlattened() {
        JsonParser parser1 = new JsonFactory().createParser("{}");
        JsonParser parser2 = new JsonFactory().createParser("[]");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertNotNull(sequence);
    }

    @Test
    public void testClose() throws Exception {
        JsonParser parser1 = new JsonFactory().createParser("{}");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1 });

        sequence.close();

        assertTrue(parser1.isClosed());
    }

    @Test
    public void testNextToken() throws Exception {
        JsonParser parser1 = new JsonFactory().createParser("{}");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1 });

        assertNull(sequence.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenWithException() throws Exception {
        JsonParser parser1 = new JsonFactory().createParser("invalid JSON");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1 });

        sequence.nextToken();
    }
}