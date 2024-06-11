import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.logging.*;
import java.util.*;
import javax.swing.text.*;

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
    public void testGetScrollableTracksViewportWidth_ParentIsNull() {
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_ViewWidthLessThanParentWidth() {
        Component parent = new JPanel();
        parent.setSize(new Dimension(500, 300));
        textPane.setParent(parent);

        boolean result = textPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_ViewWidthGreaterThanParentWidth() {
        Component parent = new JPanel();
        parent.setSize(new Dimension(300, 500));
        textPane.setParent(parent);

        boolean result = textPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }

    @Test
    public void testHighlight_EmptyTerm() {
        String term = "";
        int flags = 0;

        List<LineResult> lineResults = textPane.highlight(term, flags);

        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlight_NullTerm() {
        String term = null;
        int flags = 0;

        List<LineResult> lineResults = textPane.highlight(term, flags);

        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlight_ValidTermAndFlags() {
        textPane.setText("This is a test text. This is another test text.");
        String term = "test";
        int flags = 0;

        List<LineResult> lineResults = textPane.highlight(term, flags);

        assertFalse(lineResults.isEmpty());
        assertEquals(2, lineResults.size());

        LineResult firstLineResult = lineResults.get(0);
        assertEquals(0, firstLineResult.getLineNumber());
        assertEquals(10, firstLineResult.getLineStartOffset());
        assertEquals(20, firstLineResult.getLineEndOffset());
        List<WordResult> firstLineWords = firstLineResult.getWordResults();
        assertEquals(2, firstLineWords.size());

        LineResult secondLineResult = lineResults.get(1);
        assertEquals(1, secondLineResult.getLineNumber());
        assertEquals(23, secondLineResult.getLineStartOffset());
        assertEquals(43, secondLineResult.getLineEndOffset());
        List<WordResult> secondLineWords = secondLineResult.getWordResults();
        assertEquals(2, secondLineWords.size());
    }

    @Test
    public void testAddHighlight_ValidStartAndLength() {
        try {
            textPane.setText("This is a test");

            int start = 8;
            int length = 4;

            textPane.addHighlight(start, length);

            Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
            assertEquals(1, highlights.length);

            Highlighter.Highlight highlight = highlights[0];
            assertEquals(start, highlight.getStartOffset());
            assertEquals(start + length, highlight.getEndOffset());

        } catch (BadLocationException e) {
            fail("BadLocationException");
        }
    }

    @Test
    public void testRemoveHighlights_HighlightsExist() {
        try {
            textPane.setText("This is a test");
            textPane.addHighlight(8, 4);

            textPane.removeHighlights();

            Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
            assertEquals(0, highlights.length);

        } catch (BadLocationException e) {
            fail("BadLocationException");
        }
    }

    @Test
    public void testRemoveHighlights_NoHighlights() {
        Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
        assertEquals(0, highlights.length);

        textPane.removeHighlights();

        highlights = textPane.getHighlighter().getHighlights();
        assertEquals(0, highlights.length);
    }

    @Test
    public void testSearch_TermNotFound() {
        textPane.setText("This is a test");

        String term = "notfound";

        int result = textPane.search(term);

        assertEquals(-1, result);
        assertNull(textPane.getLastSearchTerm());
        assertEquals(-1, textPane.getLastSearchPos());
    }

    @Test
    public void testSearch_TermFound() {
        textPane.setText("This is a test text. This is another test text.");

        String term = "test";

        int result = textPane.search(term);

        assertEquals(10, result);
        assertEquals(term, textPane.getLastSearchTerm());
        assertEquals(10, textPane.getLastSearchPos());
    }

    @Test
    public void testSearch_TermNotFoundAfterPreviousSearch() {
        textPane.setText("This is a test text. This is another test text.");
        textPane.setLastSearchTerm("test");
        textPane.setLastSearchPos(10);

        String term = "notfound";

        int result = textPane.search(term);

        assertEquals(-1, result);
        assertNull(textPane.getLastSearchTerm());
        assertEquals(-1, textPane.getLastSearchPos());
    }

    @Test
    public void testSearch_WithStartPos() {
        textPane.setText("This is a test text. This is another test text.");

        String term = "test";
        int startPos = 20;

        int result = textPane.search(term, startPos);

        assertEquals(40, result);
        assertEquals(term, textPane.getLastSearchTerm());
        assertEquals(40, textPane.getLastSearchPos());
    }

    @Test
    public void testSearch_WithStartPos_TermNotFound() {
        textPane.setText("This is a test text. This is another test text.");

        String term = "test";
        int startPos = 30;

        int result = textPane.search(term, startPos);

        assertEquals(-1, result);
        assertNull(textPane.getLastSearchTerm());
        assertEquals(-1, textPane.getLastSearchPos());
    }

    @Test
    public void testConvertWords2Lines_WithLineResults() {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(10, 14));
        words.add(new WordResult(20, 26));
        words.add(new WordResult(30, 35));
        words.add(new WordResult(40, 45));

        try {
            List<LineResult> lineResults = textPane.convertWords2Lines(words);

            assertFalse(lineResults.isEmpty());
            assertEquals(4, lineResults.size());

            LineResult firstLineResult = lineResults.get(0);
            assertEquals(0, firstLineResult.getLineNumber());
            assertEquals(10, firstLineResult.getLineStartOffset());
            assertEquals(26, firstLineResult.getLineEndOffset());
            List<WordResult> firstLineWords = firstLineResult.getWordResults();
            assertEquals(2, firstLineWords.size());

            LineResult secondLineResult = lineResults.get(1);
            assertEquals(1, secondLineResult.getLineNumber());
            assertEquals(26, secondLineResult.getLineStartOffset());
            assertEquals(35, secondLineResult.getLineEndOffset());
            List<WordResult> secondLineWords = secondLineResult.getWordResults();
            assertEquals(1, secondLineWords.size());

            LineResult thirdLineResult = lineResults.get(2);
            assertEquals(2, thirdLineResult.getLineNumber());
            assertEquals(35, thirdLineResult.getLineStartOffset());
            assertEquals(56, thirdLineResult.getLineEndOffset());
            List<WordResult> thirdLineWords = thirdLineResult.getWordResults();
            assertEquals(2, thirdLineWords.size());

            LineResult fourthLineResult = lineResults.get(3);
            assertEquals(3, fourthLineResult.getLineNumber());
            assertEquals(56, fourthLineResult.getLineStartOffset());
            assertEquals(71, fourthLineResult.getLineEndOffset());
            List<WordResult> fourthLineWords = fourthLineResult.getWordResults();
            assertEquals(1, fourthLineWords.size());

        } catch (BadLocationException e) {
            fail("BadLocationException");
        }
    }


    @Test
    public void testUpdateWordResult_ValidWordAndLineResult() {
        try {
            WordResult wordResult = new WordResult(10, 14);
            LineResult lineResult = new LineResult(2, 5, 15);

            textPane.updateWordResult(wordResult, lineResult);

            assertNotNull(lineResult.getWordResults());
            assertFalse(lineResult.getWordResults().isEmpty());
            assertEquals(2, lineResult.getWordResults().size());
            assertEquals(wordResult, lineResult.getWordResults().get(1));

            assertEquals(3, lineResult.getLineNumber());

            assertEquals(5, wordResult.getLineOffset());

        } catch (BadLocationException e) {
            fail("BadLocationException");
        }
    }

    @Test
    public void testUpdateWordResult_ValidWordAndLineResult_FirstWordInLine() {
        try {
            WordResult wordResult = new WordResult(5, 8);
            LineResult lineResult = new LineResult(2, 5, 15);
            lineResult.setLineOffset(5);

            textPane.updateWordResult(wordResult, lineResult);

            assertNotNull(lineResult.getWordResults());
            assertFalse(lineResult.getWordResults().isEmpty());
            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(wordResult, lineResult.getWordResults().get(0));

            assertEquals(3, lineResult.getLineNumber());

            assertEquals(5, wordResult.getLineOffset());

        } catch (BadLocationException e) {
            fail("BadLocationException");
        }
    }
}