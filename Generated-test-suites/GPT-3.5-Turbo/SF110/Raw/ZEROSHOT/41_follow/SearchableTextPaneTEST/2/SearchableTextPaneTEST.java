import org.junit.Test;
import java.awt.*;
import java.util.List;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        Font font = new Font("Arial", Font.BOLD, 12);
        int tabSize = 4;
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);

        assertFalse(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        Font font = new Font("Arial", Font.BOLD, 12);
        int tabSize = 4;
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        String term = "test";
        int flags = 0;

        List<LineResult> lineResults = textPane.highlight(term, flags);

        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() {
        Font font = new Font("Arial", Font.BOLD, 12);
        int tabSize = 4;
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        int start = 0;
        int length = 5;

        try {
            textPane.addHighlight(start, length);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals(1, textPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testRemoveHighlights() {
        Font font = new Font("Arial", Font.BOLD, 12);
        int tabSize = 4;
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        int start = 0;
        int length = 5;

        try {
            textPane.addHighlight(start, length);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        textPane.removeHighlights();

        assertEquals(0, textPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch() {
        Font font = new Font("Arial", Font.BOLD, 12);
        int tabSize = 4;
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        String term = "test";

        int result = textPane.search(term);

        assertEquals(-1, result);
        assertNull(textPane.lastSearchTerm);
    }

    @Test
    public void testConvertWords2Lines() {
        Font font = new Font("Arial", Font.BOLD, 12);
        int tabSize = 4;
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        List<WordResult> words = List.of(new WordResult(0, 4), new WordResult(5, 9), new WordResult(10, 14));

        try {
            List<LineResult> lineResults = textPane.convertWords2Lines(words);

            assertNotNull(lineResults);
            assertEquals(1, lineResults.size());
            assertEquals(0, lineResults.get(0).getLineNumber());
            assertEquals(0, lineResults.get(0).getLineStartOffset());
            assertEquals(15, lineResults.get(0).getLineEndOffset());
            assertEquals(words, lineResults.get(0).getWordResults());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testUpdateWordResult() {
        Font font = new Font("Arial", Font.BOLD, 12);
        int tabSize = 4;
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        WordResult wordResult = new WordResult(0, 4);
        LineResult lineResult = new LineResult(0, 0, 15);

        try {
            textPane.updateWordResult(wordResult, lineResult);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(1, wordResult.getParent().getLineNumber());
        assertEquals(0, wordResult.getLineOffset());
    }
}