import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

public class JsonParserSequenceTest {

    @Test
    public void testNextToken() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});

        when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser2.nextToken()).thenReturn(JsonToken.START_ARRAY);

        JsonToken token = jsonParserSequence.nextToken();

        assertEquals(JsonToken.START_ARRAY, token);
    }
}