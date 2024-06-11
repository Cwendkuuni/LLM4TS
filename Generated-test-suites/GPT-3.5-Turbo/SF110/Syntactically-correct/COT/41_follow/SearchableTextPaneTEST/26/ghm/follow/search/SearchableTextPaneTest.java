import static org.junit.Assert.*;
import org.junit.Test;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 0);
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }

    @Test
    public void testHighlight() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 0);
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 0);
        try {
            searchableTextPane.addHighlight(0, 10);
        } catch (BadLocationException e) {
            fail("BadLocationException");
        }
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 0);
        searchableTextPane.removeHighlights();
    }

    @Test
    public void testSearch1() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 0);
        int result = searchableTextPane.search("test");
        assertEquals(-1, result);
    }

    @Test
    public void testSearch2() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 0);
        int result = searchableTextPane.search("test", 0);
        assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 0);
        List<WordResult> words = new ArrayList<>();
        List<LineResult> result = null;
        try {
            result = searchableTextPane.convertWords2Lines(words);
        } catch (BadLocationException e) {
            fail("BadLocationException");
        }
        assertNotNull(result);
    }

    @Test
    public void testUpdateWordResult() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 0);
        WordResult wordResult = new WordResult(0, 10);
        LineResult lineResult = new LineResult(0, 0, 10);
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
        } catch (BadLocationException e) {
            fail("BadLocationException");
        }
        assertEquals(1, wordResult.parent.lineNumber);
        assertEquals(0, wordResult.lineOffset);
    }
}