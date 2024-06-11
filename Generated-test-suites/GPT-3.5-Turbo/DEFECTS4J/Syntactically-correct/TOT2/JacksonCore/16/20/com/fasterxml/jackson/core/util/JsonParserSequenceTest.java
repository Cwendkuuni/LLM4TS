import static org.junit.Assert.*;
import org.junit.Test;

public class JsonParserSequenceTest {

    @Test
    public void testContainedParsersCount() {
        JsonParser p1 = new MyParser();
        JsonParser p2 = new MyParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(p1, p2);
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testSwitchToNext() {
        JsonParser p1 = new MyParser();
        JsonParser p2 = new MyParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(p1, p2);
        assertTrue(sequence.switchToNext());
        assertFalse(sequence.switchToNext());
    }

    private static class MyParser extends JsonParser {
        // Implement abstract methods of JsonParser for test purposes
    }

}