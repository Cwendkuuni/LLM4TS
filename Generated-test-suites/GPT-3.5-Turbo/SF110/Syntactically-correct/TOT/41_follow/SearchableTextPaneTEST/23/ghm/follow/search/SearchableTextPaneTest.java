import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    private SearchableTextPane textPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        textPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withNullParent_shouldReturnTrue() {
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentWidthGreaterThanPreferredWidth_shouldReturnTrue() {
        JPanel parent = new JPanel(new BorderLayout());
        parent.setSize(500, 500);
        parent.add(textPane);

        boolean result = textPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentWidthLessThanPreferredWidth_shouldReturnFalse() {
        JPanel parent = new JPanel(new BorderLayout());
        parent.setSize(200, 500);
        parent.add(textPane);

        boolean result = textPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }

    @Test
    public void testHighlight_withEmptyTerm_shouldNotHighlightAnyWord() {
        String term = "";
        int flags = 0;

        List<LineResult> lineResults = textPane.highlight(term, flags);

        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlight_withNullTerm_shouldNotHighlightAnyWord() {
        String term = null;
        int flags = 0;

        List<LineResult> lineResults = textPane.highlight(term, flags);

        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlight_withValidTermAndValidFlags_shouldHighlightMatchingWords() {
        String term = "example";
        int flags = 0;

        List<LineResult> lineResults = textPane.highlight(term, flags);

        assertEquals(2, lineResults.size());
        assertEquals(1, lineResults.get(0).getWordResults().size());
        assertEquals(1, lineResults.get(1).getWordResults().size());
    }

    @Test
    public void testAddHighlight_withValidStartAndLength_shouldAddHighlight() {
        int start = 10;
        int length = 5;

        try {
            textPane.addHighlight(start, length);
        } catch (BadLocationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveHighlights_shouldRemoveAllHighlights() {
        String term = "example";
        int flags = 0;

        List<LineResult> lineResults = textPane.highlight(term, flags);

        assertEquals(2, lineResults.size());

        textPane.removeHighlights();

        lineResults = textPane.highlight(term, flags);

        assertEquals(2, lineResults.size());
        for (LineResult lineResult : lineResults) {
            assertEquals(0, lineResult.getWordResults().size());
        }
    }

    @Test
    public void testSearch_withEmptyTerm_shouldReturnMinusOne() {
        String term = "";

        int result = textPane.search(term);

        assertEquals(-1, result);
        assertNull(textPane.getLastSearchTerm());
    }

    @Test
    public void testSearch_withNullTerm_shouldReturnMinusOne() {
        String term = null;

        int result = textPane.search(term);

        assertEquals(-1, result);
        assertNull(textPane.getLastSearchTerm());
    }

    @Test
    public void testSearch_withNonMatchingTerm_shouldReturnMinusOne() {
        String term = "xyz";

        int result = textPane.search(term);

        assertEquals(-1, result);
        assertEquals(term, textPane.getLastSearchTerm());
    }

    @Test
    public void testSearch_withMatchingTerm_shouldReturnPosition() {
        String term = "example";

        int result = textPane.search(term);

        assertEquals(0, result);
        assertEquals(term, textPane.getLastSearchTerm());
    }

    @Test
    public void testSearch_withMatchingTermAndStartPos_shouldReturnNextPosition() {
        String term = "example";
        int startPos = 0;

        int result = textPane.search(term, startPos);

        assertEquals(7, result);
        assertEquals(term, textPane.getLastSearchTerm());
    }

    @Test
    public void testConvertWords2Lines_withEmptyWordsList_shouldReturnEmptyList() {
        List<WordResult> words = new ArrayList<WordResult>();

        try {
            List<LineResult> lineResults = textPane.convertWords2Lines(words);

            assertEquals(0, lineResults.size());
        } catch (BadLocationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConvertWords2Lines_withNonEmptyWordsList_shouldReturnLineResultsList() {
        List<WordResult> words = new ArrayList<WordResult>();
        WordResult word1 = new WordResult(0, 6);
        WordResult word2 = new WordResult(10, 15);
        words.add(word1);
        words.add(word2);

        try {
            List<LineResult> lineResults = textPane.convertWords2Lines(words);

            assertEquals(2, lineResults.size());
            for (LineResult lineResult : lineResults) {
                assertEquals(1, lineResult.getWordResults().size());
            }
        } catch (BadLocationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateWordResult_shouldUpdateWordResultAndLineResult() {
        WordResult wordResult = new WordResult(0, 6);
        LineResult lineResult = new LineResult(0, 0, 10);

        try {
            textPane.updateWordResult(wordResult, lineResult);

            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(1, wordResult.getParent().getLineNumber());
            assertEquals(0, wordResult.getLineOffset());
        } catch (BadLocationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}