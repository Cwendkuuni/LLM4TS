import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.*;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws Exception {
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[0]);
        sequence.close();
    }

    @Test
    public void testNextToken() throws Exception {
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[0]);
        JsonToken token = sequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testContainedParsersCount() throws Exception {
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[0]);
        int count = sequence.containedParsersCount();
        assertEquals(0, count);
    }
}