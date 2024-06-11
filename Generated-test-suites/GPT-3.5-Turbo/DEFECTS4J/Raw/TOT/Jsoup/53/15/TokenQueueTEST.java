import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {

    @Test
    public void testIsEmpty_WhenQueueIsEmpty_ReturnsTrue() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }

    @Test
    public void testIsEmpty_WhenQueueIsNotEmpty_ReturnsFalse() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.isEmpty());
    }

    @Test
    public void testPeek_WhenQueueIsEmpty_ReturnsZero() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertEquals(0, tokenQueue.peek());
    }

    @Test
    public void testPeek_WhenQueueIsNotEmpty_ReturnsFirstCharacter() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertEquals('t', tokenQueue.peek());
    }

    @Test
    public void testAddFirst_AddsCharacterToStartOfQueue() {
        TokenQueue tokenQueue = new TokenQueue("est");
        tokenQueue.addFirst('t');
        assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testAddFirst_AddsStringToStartOfQueue() {
        TokenQueue tokenQueue = new TokenQueue("est");
        tokenQueue.addFirst("t");
        assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testMatches_WhenNextCharactersMatchSequence_ReturnsTrue() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matches("tes"));
    }

    @Test
    public void testMatches_WhenNextCharactersDoNotMatchSequence_ReturnsFalse() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.matches("abc"));
    }

    @Test
    public void testMatchesCS_WhenNextCharactersMatchSequenceCaseSensitive_ReturnsTrue() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matchesCS("Tes"));
    }

    @Test
    public void testMatchesCS_WhenNextCharactersDoNotMatchSequenceCaseSensitive_ReturnsFalse() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.matchesCS("abc"));
    }

    @Test
    public void testMatchesAny_WhenNextCharactersMatchAnyOfSequences_ReturnsTrue() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matchesAny("abc", "test"));
    }

    @Test
    public void testMatchesAny_WhenNextCharactersDoNotMatchAnyOfSequences_ReturnsFalse() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.matchesAny("abc", "def"));
    }

    @Test
    public void testMatchesStartTag_WhenNextCharactersMatchStartTagSequence_ReturnsTrue() {
        TokenQueue tokenQueue = new TokenQueue("<html");
        assertTrue(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchesStartTag_WhenNextCharactersDoNotMatchStartTagSequence_ReturnsFalse() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.matchesStartTag());
    }

    @Test
    public void testMatchChomp_WhenQueueMatchesSequence_RemovesMatchedStringFromQueueAndReturnsTrue() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertTrue(tokenQueue.matchChomp("test"));
        assertEquals("", tokenQueue.toString());
    }

    @Test
    public void testMatchChomp_WhenQueueDoesNotMatchSequence_ReturnsFalse() {
        TokenQueue tokenQueue = new TokenQueue("test");
        assertFalse(tokenQueue.matchChomp("abc"));
        assertEquals("test", tokenQueue.toString());
    }

    @Test
    public void testMatchesWhitespace_WhenNextCharacterIsWhitespace_ReturnsTrue() {
        TokenQueue tokenQueue = new TokenQueue(" ");
        assertTrue(tokenQueue.matchesWhitespace());
    }

    @Test
    public void testMatchesWhitespace_WhenNextCharacterIsNotWhitespace_ReturnsFalse() {
        TokenQueue tokenQueue = new TokenQueue("abc");
        assertFalse(tokenQueue.matchesWhitespace());
    }
    
    // Tests for other methods will be added here
    
}