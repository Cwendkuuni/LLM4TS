import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.List;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertFalse(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = textPane.highlight("test", 0);
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertThrows(BadLocationException.class, () -> textPane.addHighlight(0, 5));
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        textPane.highlight("test", 0);
        textPane.removeHighlights();
        assertEquals(0, textPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(-1, textPane.search("test"));
    }

    @Test
    public void testSearchWithStartPosition() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(-1, textPane.search("test", 5));
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(0, textPane.convertWords2Lines(new ArrayList<>()).size());
    }

    @Test
    public void testUpdateWordResult() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(1, 0, 10);
        assertThrows(BadLocationException.class, () -> textPane.updateWordResult(wordResult, lineResult));
    }
}