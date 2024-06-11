import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;
import java.util.List;

public class SearchableTextPaneTest {
    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        searchableTextPane = new SearchableTextPane(font, 4);
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
        // Verify removal of highlights
    }

    @Test
    public void testSearch() {
        int pos = searchableTextPane.search("search");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchWithStartPos() {
        int pos = searchableTextPane.search("search", 5);
        assertTrue(pos >= 5);
    }

    @Test
    public void testConvertWords2Lines() {
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(new ArrayList<WordResult>());
        assertNotNull(lineResults);
    }

    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 4);
        LineResult lineResult = new LineResult(1, 0, 10);

        assertDoesNotThrow(() -> searchableTextPane.updateWordResult(wordResult, lineResult));
    }
}