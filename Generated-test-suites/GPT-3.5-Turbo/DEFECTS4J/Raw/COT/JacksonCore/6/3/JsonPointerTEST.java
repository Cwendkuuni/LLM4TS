import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        // Test for matches()
        // Test case 1: JsonPointer is empty
        JsonPointer jsonPointer1 = JsonPointer.EMPTY;
        assertTrue(jsonPointer1.matches());

        // Test case 2: JsonPointer is not empty
        JsonPointer jsonPointer2 = new JsonPointer("/test");
        assertFalse(jsonPointer2.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        // Test for getMatchingProperty()
        // Test case 1: JsonPointer matches property
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        assertEquals("test", jsonPointer1.getMatchingProperty());

        // Test case 2: JsonPointer doesn't match property
        JsonPointer jsonPointer2 = new JsonPointer("/test123");
        assertNull(jsonPointer2.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        // Test for getMatchingIndex()
        // Test case 1: JsonPointer matches index
        JsonPointer jsonPointer1 = new JsonPointer("/123");
        assertEquals(123, jsonPointer1.getMatchingIndex());

        // Test case 2: JsonPointer doesn't match index
        JsonPointer jsonPointer2 = new JsonPointer("/test");
        assertEquals(-1, jsonPointer2.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        // Test for mayMatchProperty()
        // Test case 1: JsonPointer matches property
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        assertTrue(jsonPointer1.mayMatchProperty());

        // Test case 2: JsonPointer doesn't match property
        JsonPointer jsonPointer2 = new JsonPointer("/test123");
        assertFalse(jsonPointer2.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        // Test for mayMatchElement()
        // Test case 1: JsonPointer matches index
        JsonPointer jsonPointer1 = new JsonPointer("/123");
        assertTrue(jsonPointer1.mayMatchElement());

        // Test case 2: JsonPointer doesn't match index
        JsonPointer jsonPointer2 = new JsonPointer("/test");
        assertFalse(jsonPointer2.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        // Test for matchProperty()
        // Test case 1: JsonPointer matches property
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        JsonPointer matchedPointer1 = jsonPointer1.matchProperty("test");
        assertNotNull(matchedPointer1);
        assertEquals(jsonPointer1.tail(), matchedPointer1);

        // Test case 2: JsonPointer doesn't match property
        JsonPointer jsonPointer2 = new JsonPointer("/test123");
        JsonPointer matchedPointer2 = jsonPointer2.matchProperty("test");
        assertNull(matchedPointer2);
    }

    @Test
    public void testMatchElement() {
        // Test for matchElement()
        // Test case 1: JsonPointer matches index
        JsonPointer jsonPointer1 = new JsonPointer("/123");
        JsonPointer matchedPointer1 = jsonPointer1.matchElement(123);
        assertNotNull(matchedPointer1);
        assertEquals(jsonPointer1.tail(), matchedPointer1);

        // Test case 2: JsonPointer doesn't match index
        JsonPointer jsonPointer2 = new JsonPointer("/test");
        JsonPointer matchedPointer2 = jsonPointer2.matchElement(123);
        assertNull(matchedPointer2);
    }

    @Test
    public void testTail() {
        // Test for tail()
        // Test case 1: JsonPointer has a tail
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        assertEquals(JsonPointer.EMPTY, jsonPointer1.tail());

        // Test case 2: JsonPointer has no tail
        JsonPointer jsonPointer2 = JsonPointer.EMPTY;
        assertNull(jsonPointer2.tail());
    }
}