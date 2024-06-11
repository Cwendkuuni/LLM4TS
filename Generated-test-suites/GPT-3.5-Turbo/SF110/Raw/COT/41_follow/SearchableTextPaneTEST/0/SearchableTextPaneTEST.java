import org.junit.*;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    SearchableTextPane textPane;

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
        String term = "test";
        int flags = 0;
        List<LineResult> lineResults = textPane.highlight(term, flags);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        int start = 0;
        int length = 5;
        try {
            textPane.addHighlight(start, length);
        } catch (BadLocationException e) {
            fail("BadLocationException thrown");
        }
    }

    @Test
    public void testRemoveHighlights() {
        textPane.removeHighlights();
        // No assertion as it just removes highlights
    }

    @Test
    public void testSearch() {
        String term = "search";
        int pos = textPane.search(term);
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchWithStartPos() {
        String term = "search";
        int startPos = 10;
        int pos = textPane.search(term, startPos);
        assertEquals(-1, pos);
    }

    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = new ArrayList<>();
        WordResult wordResult = new WordResult(0, 5);
        words.add(wordResult);
        try {
            List<LineResult> lineResults = textPane.convertWords2Lines(words);
            assertNotNull(lineResults);
            assertEquals(1, lineResults.size());
        } catch (BadLocationException e) {
            fail("BadLocationException thrown");
        }
    }

    @Test
    public void testUpdateWordResult() {
        LineResult lineResult = new LineResult(0, 0, 10);
        WordResult wordResult = new WordResult(0, 5);
        try {
            textPane.updateWordResult(wordResult, lineResult);
            assertNotNull(lineResult.getWordResults());
            assertEquals(1, lineResult.getWordResults().size());
        } catch (BadLocationException e) {
            fail("BadLocationException thrown");
        }
    }
}