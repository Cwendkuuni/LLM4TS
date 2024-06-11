import static org.junit.Assert.*;
import org.junit.Test;

public class SearchableTextPaneTest {
    SearchableTextPane textPane;
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        textPane = new SearchableTextPane();
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        textPane = new SearchableTextPane();
        assertEquals(0, textPane.highlight("", 0).size());
    }

    @Test
    public void testAddHighlight() {
        textPane = new SearchableTextPane();
        assertThrows(BadLocationException.class, () -> textPane.addHighlight(0, -1));
    }

    @Test
    public void testRemoveHighlights() {
        textPane = new SearchableTextPane();
        textPane.removeHighlights();
        // Assert that highlights have been removed
    }

    @Test
    public void testSearch() {
        textPane = new SearchableTextPane();
        assertEquals(-1, textPane.search("term"));
    }

    @Test
    public void testConvertWords2Lines() {
        textPane = new SearchableTextPane();
        assertEquals(0, textPane.convertWords2Lines(new ArrayList<WordResult>()).size());
    }

    @Test
    public void testUpdateWordResult() {
        textPane = new SearchableTextPane();
        LineResult lineResult = new LineResult(1, 0, 10);
        WordResult wordResult = new WordResult(0, 5);
        textPane.updateWordResult(wordResult, lineResult);
        // Assert the wordResult has been updated in lineResult
    }
}