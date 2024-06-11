import org.junit.Test;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Typical use case
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        assertFalse(searchableTextPane.highlight(null, 0).isEmpty());
    }
    
    @Test
    public void testAddHighlight() {
        // Edge case: BadLocationException
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        assertThrows(BadLocationException.class, () -> { searchableTextPane.addHighlight(-1, 5); });
    }
    
    @Test
    public void testRemoveHighlights() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        searchableTextPane.removeHighlights();
        // No exception is thrown
    }
    
    @Test
    public void testSearch() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        assertEquals(-1, searchableTextPane.search(null));
    }
    
    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        // Edge case: BadLocationException
        assertThrows(BadLocationException.class, () -> { searchableTextPane.convertWords2Lines(null); });
    }
    
    @Test
    public void testUpdateWordResult() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        // Edge case: BadLocationException
        WordResult wordResult = new WordResult(0, 0);
        LineResult lineResult = new LineResult(0, 0, 0);
        assertThrows(BadLocationException.class, () -> { searchableTextPane.updateWordResult(wordResult, lineResult); });
    }
}