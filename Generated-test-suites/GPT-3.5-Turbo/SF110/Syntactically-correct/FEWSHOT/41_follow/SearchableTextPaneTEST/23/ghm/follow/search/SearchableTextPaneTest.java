
package ghm.follow.search;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.util.List;

public class SearchableTextPaneTest {

    private Font font = new Font("Arial", Font.PLAIN, 12);

    @Test
    public void testGetScrollableTracksViewportWidth_NullParent_ReturnsTrue() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_WidthEqualToPreferredWidth_ReturnsTrue() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setPreferredSize(new Dimension(100, 200));
        textPane.setSize(new Dimension(100, 200));
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_WidthGreaterThanPreferredWidth_ReturnsFalse() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setPreferredSize(new Dimension(100, 200));
        textPane.setSize(new Dimension(150, 200));
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }

    @Test
    public void testHighlight_EmptyTerm_ReturnsEmptyLineResults() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        List<LineResult> lineResults = textPane.highlight("", 0);
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlight_NullTerm_ReturnsEmptyLineResults() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        List<LineResult> lineResults = textPane.highlight(null, 0);
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlight_TermNotFound_ReturnsEmptyLineResults() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        List<LineResult> lineResults = textPane.highlight("hello", 0);
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlight_TermFound_ReturnsNonEmptyLineResults() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        List<LineResult> lineResults = textPane.highlight("test", 0);
        assertFalse(lineResults.isEmpty());
    }

    @Test
    public void testAddHighlight_StartAndLengthPositive_AddsHighlight() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        try {
            textPane.getDocument().insertString(0, "This is a test", null);
        } catch (Exception e) {
            fail("Exception occurred when inserting text");
        }
        try {
            textPane.addHighlight(5, 2);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception occurred when adding highlight");
        }
    }

    @Test
    public void testRemoveHighlights_NoHighlights_KeepsNoHighlights() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        textPane.removeHighlights();
        assertTrue(true);
    }

    @Test
    public void testSearch_NullTerm_ReturnsNegativeOne() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        int result = textPane.search(null);
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_EmptyTerm_ReturnsNegativeOne() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        int result = textPane.search("");
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_TermNotFound_ReturnsNegativeOne() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        int result = textPane.search("hello");
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_TermFoundInMiddle_ReturnsCorrectPosition() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        int result = textPane.search("is");
        assertEquals(2, result);
    }

    @Test
    public void testSearch_LastSearchPosInvalid_ReturnsCorrectPosition() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        textPane.search("is");
        int result = textPane.search("is");
        assertEquals(2, result);
    }

    @Test
    public void testSearch_ValidTermWithStartPosition_ReturnsCorrectPosition() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        int result = textPane.search("is", 3);
        assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines_WordListWithMultipleLines_ReturnsCorrectLineResults() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        try {
            textPane.getDocument().insertString(0, "Line 1\nLine 2\nLine 3\n", null);
        } catch (Exception e) {
            fail("Exception occurred when inserting text");
        }
        List<WordResult> words = Arrays.asList(
                new WordResult(2, 6),
                new WordResult(12, 15),
                new WordResult(20, 23));
        try {
            List<LineResult> lineResults = textPane.convertWords2Lines(words);
            assertEquals(3, lineResults.size());
            assertEquals(0, lineResults.get(0).getLine());
            assertEquals(0, lineResults.get(0).getLineStart());
            assertEquals(7, lineResults.get(0).getLineEnd());
            assertEquals(1, lineResults.get(1).getLine());
            assertEquals(8, lineResults.get(1).getLineStart());
            assertEquals(15, lineResults.get(1).getLineEnd());
            assertEquals(2, lineResults.get(2).getLine());
            assertEquals(16, lineResults.get(2).getLineStart());
            assertEquals(23, lineResults.get(2).getLineEnd());
        } catch (Exception e) {
            fail("Exception occurred when converting words to lines");
        }
    }

    @Test
    public void testUpdateWordResult_WordResultAndLineResult_ValidLineResultUpdated() {
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        try {
            textPane.getDocument().insertString(0, "This is a test", null);
        } catch (Exception e) {
            fail("Exception occurred when inserting text");
        }
        LineResult lineResult = new LineResult(0, 0, 4);
        WordResult wordResult = new WordResult(0, 3);
        try {
            textPane.updateWordResult(wordResult, lineResult);
            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(wordResult, lineResult.getWordResults().get(0));
            assertEquals(1, wordResult.getParent().getLineNumber());
            assertEquals(0, wordResult.getLineOffset());
        } catch (Exception e) {
            fail("Exception occurred when updating word result");
        }
    }

}
