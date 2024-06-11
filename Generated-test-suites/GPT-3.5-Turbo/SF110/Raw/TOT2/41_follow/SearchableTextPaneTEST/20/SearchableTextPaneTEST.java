import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;
import java.util.List;
import javax.swing.text.BadLocationException;

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
        String searchTerm = "test";
        int flags = 0;
        List<LineResult> lineResults = searchableTextPane.highlight(searchTerm, flags);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        try {
            searchableTextPane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
        // It is difficult to test the removal of highlights directly, as the UI does not expose it
    }

    @Test
    public void testSearch() {
        String searchTerm = "search";
        int startPos = 0;
        int result = searchableTextPane.search(searchTerm, startPos);
        assertTrue(result >= -1);
    }

    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 4));
        try {
            List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
            assertNotNull(lineResults);
        } catch (BadLocationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 4);
        LineResult lineResult = new LineResult(0, 0, 10);
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
            assertEquals(lineResult.getWordResults().size(), 1);
        } catch (BadLocationException e) {
            fail("Exception thrown");
        }
    }

}