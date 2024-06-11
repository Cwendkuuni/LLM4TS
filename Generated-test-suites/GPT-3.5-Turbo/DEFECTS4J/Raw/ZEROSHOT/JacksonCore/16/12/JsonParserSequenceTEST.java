import com.fasterxml.jackson.core.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws IOException {
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{});
        sequence.close();
        // Assertion
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{});
        JsonToken token = sequence.nextToken();
        // Assertion
    }

    @Test
    public void testCreateFlattened() {
        JsonParser first = new JsonParser();
        JsonParser second = new JsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        // Assertion
    }

    @Test
    public void testAddFlattenedActiveParsers() {
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{});
        List<JsonParser> result = new ArrayList<>();
        sequence.addFlattenedActiveParsers(result);
        // Assertion
    }

    @Test
    public void testContainedParsersCount() {
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{});
        int count = sequence.containedParsersCount();
        // Assertion
    }

    @Test
    public void testSwitchToNext() {
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{});
        boolean switched = sequence.switchToNext();
        // Assertion
    }
}