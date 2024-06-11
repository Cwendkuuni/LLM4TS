import static org.junit.Assert.*;
import org.junit.*;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane textPane = new SearchableTextPane();
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        SearchableTextPane textPane = new SearchableTextPane();
        List<LineResult> lineResults = textPane.highlight(null, 0);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        SearchableTextPane textPane = new SearchableTextPane();
        try {
            textPane.addHighlight(-1, 10);
        } catch (BadLocationException e) {
            // Exception is expected for invalid parameters
            return;
        }
        fail("Exception not thrown");
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane textPane = new SearchableTextPane();
        // Add highlights and then remove
        textPane.removeHighlights();
        // Make assertion for removed highlights
    }

    @Test
    public void testSearch() {
        SearchableTextPane textPane = new SearchableTextPane();
        int result = textPane.search("search");
        assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane textPane = new SearchableTextPane();
        List<WordResult> words = new ArrayList<WordResult>();
        // Add WordResult objects to words list
        try {
            List<LineResult> lineResults = textPane.convertWords2Lines(words);
            assertNotNull(lineResults);
        } catch (BadLocationException e){
            fail("Exception not expected");
        }
    }

    @Test
    public void testUpdateWordResult() {
        SearchableTextPane textPane = new SearchableTextPane();
        WordResult wordResult = new WordResult();
        LineResult lineResult = new LineResult();
        try {
            textPane.updateWordResult(null, lineResult);
        } catch (BadLocationException e) {
            // Exception is expected for null parameters
            return;
        }
        fail("Exception not thrown");
    }
}