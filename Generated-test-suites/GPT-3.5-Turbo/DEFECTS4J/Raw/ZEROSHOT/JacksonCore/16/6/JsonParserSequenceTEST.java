import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;
    private JsonParser[] parsers;
    private JsonParser first;
    private JsonParser second;

    @Before
    public void setUp() {
        first = mock(JsonParser.class);
        second = mock(JsonParser.class);
        when(first.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(second.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        parsers = new JsonParser[]{first, second};
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @After
    public void tearDown() {
        jsonParserSequence.close();
    }

    @Test
    public void testCreateFlattened() {
        JsonParserSequence result = JsonParserSequence.createFlattened(first, second);
        assertEquals(parsers.length, result.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        jsonParserSequence.close();
        // TODO: add assertion
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonToken result = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, result);
    }

    @Test
    public void testContainedParsersCount() {
        int result = jsonParserSequence.containedParsersCount();
        assertEquals(parsers.length, result);
    }
}