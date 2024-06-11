package ghm.follow.search;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;
import java.util.List;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth_withNullParent() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);

        boolean result = textPane.getScrollableTracksViewportWidth();

        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentWidthGreaterThanTextPaneWidth() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setBounds(0, 0, 300, 200);

        boolean result = textPane.getScrollableTracksViewportWidth();

        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentWidthLessThanTextPaneWidth() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setBounds(0, 0, 200, 200);

        boolean result = textPane.getScrollableTracksViewportWidth();

        assertFalse(result);
    }

    @Test
    public void testHighlight_withNullTerm() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet");

        List<LineResult> result = textPane.highlight(null, 0);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testHighlight_withEmptyTerm() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet");

        List<LineResult> result = textPane.highlight("", 0);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testHighlight_withTermNotFound() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet");

        List<LineResult> result = textPane.highlight("consectetur", 0);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testHighlight_withTermFound() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet");

        List<LineResult> result = textPane.highlight("ipsum", 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getWordResults().size());
        assertEquals(6, result.get(0).getWordResults().get(0).start);
        assertEquals(11, result.get(0).getWordResults().get(0).end);
    }

    @Test
    public void testRemoveHighlights_withNoHighlights() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet");

        textPane.removeHighlights();
    }

    @Test
    public void testRemoveHighlights_withHighlights() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet");
        textPane.highlight("ipsum", 0);

        textPane.removeHighlights();
    }

    @Test
    public void testSearch_withNullTerm() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet");

        int result = textPane.search(null);

        assertEquals(-1, result);
    }

    @Test
    public void testSearch_withEmptyTerm() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet");

        int result = textPane.search("");

        assertEquals(-1, result);
    }

    @Test
    public void testSearch_withTermNotFound() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet");

        int result = textPane.search("consectetur");

        assertEquals(-1, result);
    }

    @Test
    public void testSearch_withTermFound() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet");

        int result = textPane.search("ipsum");

        assertEquals(6, result);
    }

    @Test
    public void testSearch_withTermFoundAfterPreviousSearchTerm() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor ipsum sit amet");
        textPane.search("ipsum");

        int result = textPane.search("ipsum");

        assertEquals(18, result);
    }

    @Test
    public void testSearch_withTermFoundAfterPreviousSearchTermWithStartPosition() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor ipsum sit amet");
        textPane.search("ipsum", 12);

        int result = textPane.search("ipsum");

        assertEquals(18, result);
    }

    @Test
    public void testConvertWords2Lines_withNoWords() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);

        List<LineResult> result = null;
        try {
            result = textPane.convertWords2Lines(new ArrayList<WordResult>());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testConvertWords2Lines_withWordsOnMultipleLines() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.");

        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 5));
        words.add(new WordResult(6, 11));
        words.add(new WordResult(29, 38));

        List<LineResult> result = null;
        try {
            result = textPane.convertWords2Lines(words);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(0, result.get(0).getWordResults().size());
        assertEquals(0, result.get(1).getWordResults().size());
        assertEquals(1, result.get(2).getWordResults().size());
    }

    @Test
    public void testConvertWords2Lines_withWordsOnSameLine() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet");

        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 5));
        words.add(new WordResult(6, 11));
        words.add(new WordResult(12, 17));

        List<LineResult> result = null;
        try {
            result = textPane.convertWords2Lines(words);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getWordResults().size());
    }

    @Test
    public void testUpdateWordResult() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.");

        WordResult wordResult = new WordResult(29, 38);
        LineResult lineResult = new LineResult(1, 28, 45);

        try {
            textPane.updateWordResult(wordResult, lineResult);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }

        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(2, wordResult.parent.lineNumber);
        assertEquals(28, wordResult.getLineOffset());
    }

}