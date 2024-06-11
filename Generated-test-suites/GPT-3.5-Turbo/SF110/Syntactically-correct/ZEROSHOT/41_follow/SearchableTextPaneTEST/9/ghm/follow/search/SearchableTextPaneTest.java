import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.List;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        boolean tracksViewportWidth = pane.getScrollableTracksViewportWidth();
        assertTrue(tracksViewportWidth);
    }

    @Test
    public void testHighlight() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = pane.highlight("test", 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testAddHighlight() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        try {
            pane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        pane.removeHighlights();
    }

    @Test
    public void testSearch1() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        int searchPos = pane.search("test");
        assertEquals(-1, searchPos);
    }

    @Test
    public void testSearch2() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        int searchPos = pane.search("test", 0);
        assertEquals(-1, searchPos);
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        try {
            List<LineResult> lineResults = pane.convertWords2Lines(null);
            assertNotNull(lineResults);
            assertTrue(lineResults.isEmpty());
        } catch (BadLocationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateWordResult() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(0, 0, 10);
        try {
            pane.updateWordResult(wordResult, lineResult);
            assertEquals(1, lineResult.getWordResults().size());
        } catch (BadLocationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}