
import org.junit.Test;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testHighlight() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        List<LineResult> result = textPane.highlight("test", 0);
        assertNotNull(result);
    }
    
    @Test
    public void testAddHighlight() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        try {
            textPane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        textPane.removeHighlights();
    }
    
    @Test
    public void testSearchWithoutPreviousSearch() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        int result = textPane.search("test");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithPreviousSearch() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        textPane.search("test");
        int result = textPane.search("test");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithTermNull() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        int result = textPane.search(null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithTermEmpty() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        int result = textPane.search("");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithStartPosBeforeTerm() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        int startPos = -1;
        int result = textPane.search("test", startPos);
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithStartPosAfterTerm() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        int startPos = 10;
        int result = textPane.search("test", startPos);
        assertEquals(-1, result);
    }
    
    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        List<WordResult> words = new ArrayList<WordResult>();
        words.add(new WordResult(0, 4));
        try {
            List<LineResult> result = textPane.convertWords2Lines(words);
            assertEquals(1, result.size());
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
    
    @Test
    public void testUpdateWordResult() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        WordResult wordResult = new WordResult(0, 4);
        LineResult lineResult = new LineResult(0, 0, 10);
        try {
            textPane.updateWordResult(wordResult, lineResult);
            assertEquals(5, wordResult.getLineOffset());
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
}
