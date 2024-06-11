import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;

    @Mock
    private JsonParser firstParser;

    @Mock
    private JsonParser secondParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void testClose() {
        try {
            jsonParserSequence.close();
        } catch (Exception e) {
            fail("Exception thrown when closing JsonParserSequence");
        }
    }

    @Test
    public void testNextToken() {
        try {
            jsonParserSequence.nextToken();
        } catch (Exception e) {
            fail("Exception thrown when getting next token from JsonParserSequence");
        }
    }
}