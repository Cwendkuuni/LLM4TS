
import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Font;

public class SearchableTextPaneTest {

    @Test
    public void testConstructor() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane pane = new SearchableTextPane(font, tabSize);
        assertEquals(font, pane.getFont());
        assertEquals(tabSize, pane.getTabSize());
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane pane = new SearchableTextPane(font, tabSize);
        assertFalse(pane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane pane = new SearchableTextPane(font, tabSize);

        String term = "Lorem";
        int flags = 0;
        assertEquals(0, pane.highlight(term, flags).size());
    }

    @Test
    public void testAddHighlight() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane pane = new SearchableTextPane(font, tabSize);
        try {
            pane.addHighlight(0, 5);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveHighlights() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane pane = new SearchableTextPane(font, tabSize);
        try {
            pane.removeHighlights();
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSearch() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane pane = new SearchableTextPane(font, tabSize);

        String term = "Lorem";
        assertEquals(-1, pane.search(term));
    }

    @Test
    public void testSearchWithStartPos() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane pane = new SearchableTextPane(font, tabSize);

        String term = "Lorem";
        int startPos = 0;
        assertEquals(-1, pane.search(term, startPos));
    }

    @Test
    public void testConvertWords2Lines() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane pane = new SearchableTextPane(font, tabSize);

        try {
            assertEquals(0, pane.convertWords2Lines(new ArrayList<WordResult>()).size());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateWordResult() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane pane = new SearchableTextPane(font, tabSize);
        try {
            WordResult wordResult = new WordResult(0, 5);
            LineResult lineResult = new LineResult(1, 0, 10);

            pane.updateWordResult(wordResult, lineResult);

            assertEquals(1, wordResult.parent.lineNumber);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
