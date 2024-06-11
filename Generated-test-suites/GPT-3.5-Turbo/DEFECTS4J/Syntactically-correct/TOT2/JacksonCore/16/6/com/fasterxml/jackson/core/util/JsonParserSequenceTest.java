import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);

        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testAddFlattenedActiveParsers() {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1 });

        assertEquals(sequence, sequence.addFlattenedActiveParsers(new ArrayList<JsonParser>()));
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser });

        sequence.close();

        Mockito.verify(parser).close();
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);
        Mockito.when(parser1.nextToken()).thenReturn(null);
        Mockito.when(parser2.nextToken()).thenReturn(JsonToken.FIELD_NAME);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        JsonToken token = sequence.nextToken();

        assertEquals(JsonToken.FIELD_NAME, token);
    }

    @Test
    public void testSwitchToNext() {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        boolean result = sequence.switchToNext();

        assertTrue(result);
        assertEquals(parser2, sequence.delegate);
    }
}