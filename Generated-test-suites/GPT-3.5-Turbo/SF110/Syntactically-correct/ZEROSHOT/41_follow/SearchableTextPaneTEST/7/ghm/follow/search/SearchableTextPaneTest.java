import static org.junit.Assert.*;
import org.junit.Test;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }
    
    @Test
    public void testHighlight() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        List<LineResult> lineResults = textPane.highlight("test", 0);
        assertNotNull(lineResults);
    }
    
    @Test
    public void testAddHighlight() {
        try {
            SearchableTextPane textPane = new SearchableTextPane(null, 0);
            textPane.addHighlight(0, 5);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        try {
            textPane.removeHighlights();
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testSearch() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        int result = textPane.search("test");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithStartPos() {
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        int result = textPane.search("test", 0);
        assertEquals(-1, result);
    }
    
    @Test
    public void testConvertWords2Lines() {
        try {
            SearchableTextPane textPane = new SearchableTextPane(null, 0);
            List<WordResult> words = new ArrayList<WordResult>();
            WordResult word = new WordResult(0, 5);
            words.add(word);
            List<LineResult> lines = textPane.convertWords2Lines(words);
            assertNotNull(lines);
            assertEquals(1, lines.size());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testUpdateWordResult() {
        try {
            SearchableTextPane textPane = new SearchableTextPane(null, 0);
            WordResult wordResult = new WordResult(0, 5);
            LineResult lineResult = new LineResult(1, 0, 10);
            textPane.updateWordResult(wordResult, lineResult);
            assertEquals(1, wordResult.parent.lineNumber);
            assertEquals(0, wordResult.lineOffset);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}