import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.Font;
import java.util.List;
import javax.swing.text.BadLocationException;

public class SearchableTextPaneTest {
    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        assertThrows(BadLocationException.class, () -> searchableTextPane.addHighlight(-1, 5));
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
        // Assert that the highlights are removed
    }

    @Test
    public void testSearch() {
        assertEquals(-1, searchableTextPane.search("test"));
    }

    @Test
    public void testSearchWithStartPosition() {
        assertEquals(-1, searchableTextPane.search("test", 0));
    }

    @Test
    public void testConvertWords2Lines() {
        // Test with empty list of words
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(new ArrayList<WordResult>());
        assertNotNull(lineResults);
    }

    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 4);
        LineResult lineResult = new LineResult(1, 0, 10);
        assertThrows(BadLocationException.class, () -> searchableTextPane.updateWordResult(wordResult, lineResult));
    }
}