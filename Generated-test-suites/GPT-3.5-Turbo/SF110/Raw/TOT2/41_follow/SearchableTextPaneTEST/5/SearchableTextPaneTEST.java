import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        // Typical use case
        assertEquals(1, searchableTextPane.highlight("search", 0).size());

        // Edge case
        assertEquals(0, searchableTextPane.highlight("", 0).size());

        // Error scenario
        assertNull(searchableTextPane.highlight(null, 0));
    }

    @Test
    public void testAddHighlight() {
        // Edge case
        try {
            searchableTextPane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("Failed to add highlight with valid parameters");
        }
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.highlight("search", 0);
        searchableTextPane.removeHighlights();
        assertTrue(true); // No exception should be thrown
    }

    @Test
    public void testSearch() {
        // Typical use case
        assertEquals(-1, searchableTextPane.search("term"));

        // Edge case
        assertEquals(-1, searchableTextPane.search("test", 10));

        // Error scenario
        assertEquals(-1, searchableTextPane.search(null));
    }

    @Test
    public void testConvertWords2Lines() {
        // Edge case
        try {
            searchableTextPane.convertWords2Lines(Collections.emptyList());
        } catch (BadLocationException e) {
            fail("Failed to convert words to lines with empty list");
        }
    }

    @Test
    public void testUpdateWordResult() {
        // Edge case
        try {
            searchableTextPane.updateWordResult(new WordResult(0, 5), new LineResult(1, 0, 10));
        } catch (BadLocationException e) {
            fail("Failed to update word result with valid parameters");
        }
    }
}