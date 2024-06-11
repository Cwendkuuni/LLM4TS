import static org.junit.Assert.*;
import org.junit.*;

public class JsonParserSequenceTest {

    @Test
    public void testClose() {
        // Test case 1: Test close method when there is only one parser
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {new JsonParser1()});
        try {
            sequence.close();
            assertTrue(true);
        } catch (IOException e) {
            fail("Exception thrown when closing single parser sequence");
        }

        // Test case 2: Test close method when there are multiple parsers
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {new JsonParser1(), new JsonParser2()});
        try {
            sequence.close();
            assertTrue(true);
        } catch (IOException e) {
            fail("Exception thrown when closing parser sequence");
        }
    }

    @Test
    public void testNextToken() {
        // Test case 1: Test nextToken method when there is only one parser
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {new JsonParser1()});
        try {
            JsonToken token = sequence.nextToken();
            assertNull(token);
        } catch (IOException e) {
            fail("Exception thrown when calling nextToken on single parser sequence");
        } catch (JsonParseException e) {
            fail("Exception thrown when calling nextToken on single parser sequence");
        }

        // Test case 2: Test nextToken method when there are multiple parsers
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {new JsonParser1(), new JsonParser2()});
        try {
            JsonToken token = sequence.nextToken();
            assertNotNull(token);
            assertEquals(JsonToken.START_ARRAY, token);
        } catch (IOException e) {
            fail("Exception thrown when calling nextToken on parser sequence");
        } catch (JsonParseException e) {
            fail("Exception thrown when calling nextToken on parser sequence");
        }
    }

    @Test
    public void testContainedParsersCount() {
        // Test case 1: Test containedParsersCount method when there is only one parser
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {new JsonParser1()});
        int count = sequence.containedParsersCount();
        assertEquals(1, count);

        // Test case 2: Test containedParsersCount method when there are multiple parsers
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {new JsonParser1(), new JsonParser2()});
        int count = sequence.containedParsersCount();
        assertEquals(2, count);
    }

    @Test
    public void testCreateFlattened() {
        // Test case 1: Test createFlattened method with non-sequence parsers
        JsonParser first = new JsonParser1();
        JsonParser second = new JsonParser2();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        assertNotNull(sequence);
        int count = sequence.containedParsersCount();
        assertEquals(2, count);

        // Test case 2: Test createFlattened method with sequence parsers
        JsonParser first = new JsonParserSequence(new JsonParser[] {new JsonParser1()});
        JsonParser second = new JsonParserSequence(new JsonParser[] {new JsonParser2()});
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        assertNotNull(sequence);
        int count = sequence.containedParsersCount();
        assertEquals(2, count);
    }

    @Test
    public void testSwitchToNext() {
        // Test case 1: Test switchToNext method when there is only one parser
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {new JsonParser1()});
        boolean result = sequence.switchToNext();
        
        assertFalse(result);
        int count = sequence.containedParsersCount();
        assertEquals(1, count);

        // Test case 2: Test switchToNext method when there are multiple parsers
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {new JsonParser1(), new JsonParser2()});
        boolean result = sequence.switchToNext();
        
        assertTrue(result);
        int count = sequence.containedParsersCount();
        assertEquals(2, count);
    }
}