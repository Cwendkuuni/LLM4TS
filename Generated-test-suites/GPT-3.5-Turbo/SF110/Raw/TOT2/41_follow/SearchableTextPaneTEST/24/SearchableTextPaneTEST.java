package ghm.follow.search;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;
import org.junit.Before;
import java.util.List;

public class SearchableTextPaneTest {

    private SearchableTextPane textPane;

    @Before
    public void setUp() {
        textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }

    @Test
    public void testHighlight() {
        List<LineResult> lineResults = textPane.highlight("test", 0);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        try {
            textPane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("Exception thrown when it shouldn't");
        }
    }

    @Test
    public void testRemoveHighlights() {
        textPane.removeHighlights();
        // No assertion needed, just check for exceptions
    }

    @Test
    public void testSearch() {
        int result = textPane.search("searchTerm");
        assertEquals(-1, result); // Initial search should return -1
    }

    @Test
    public void testSearchWithStartPosition() {
        int result = textPane.search("searchTerm", 5);
        assertEquals(-1, result); // Search with specified start position should return -1
    }

    @Test
    public void testConvertWords2Lines() {
        List<LineResult> lineResults = textPane.convertWords2Lines(new ArrayList<>());
        assertNotNull(lineResults);
    }

    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 5, null);
        LineResult lineResult = new LineResult(1, 0, 10);
        try {
            textPane.updateWordResult(wordResult, lineResult);
            assertNotNull(wordResult.getLineOffset());
        } catch (BadLocationException e) {
            fail("Exception thrown when it shouldn't");
        }
    }
}