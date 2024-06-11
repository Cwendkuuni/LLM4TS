import static org.junit.Assert.*;
import org.junit.*;
import com.fasterxml.jackson.core.util.*;
import com.fasterxml.jackson.core.*;
import java.io.IOException;
import java.util.*;

public class JsonParserSequenceTest {

    @Test
    public void testConstructor() throws IOException {
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new JsonFactory().createParser("{\"name\":\"John\", \"age\":30}");
        parsers[1] = new JsonFactory().createParser("{\"name\":\"Jane\", \"age\":25}");
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        assertEquals(2, sequence.containedParsersCount());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertEquals("name", sequence.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        assertEquals("John", sequence.getText());
    }

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser first = new JsonFactory().createParser("{\"name\":\"John\", \"age\":30}");
        JsonParser second = new JsonFactory().createParser("{\"name\":\"Jane\", \"age\":25}");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        assertEquals(2, sequence.containedParsersCount());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertEquals("name", sequence.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        assertEquals("John", sequence.getText());
    }
}