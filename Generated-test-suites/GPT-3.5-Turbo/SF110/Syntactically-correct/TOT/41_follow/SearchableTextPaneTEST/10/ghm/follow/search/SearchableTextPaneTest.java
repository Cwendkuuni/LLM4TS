import org.junit.Before;
import org.junit.Test;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public void testGetScrollableTracksViewportWidth() {
        // Normal case - text width fits the viewport width
        textPane.setSize(new Dimension(100, 100));
        assertTrue(textPane.getScrollableTracksViewportWidth());

        // Normal case - text width exceeds the viewport width
        textPane.setSize(new Dimension(300, 100));
        assertFalse(textPane.getScrollableTracksViewportWidth());

        // Edge case - viewport width is zero
        textPane.setSize(new Dimension(0, 100));
        assertFalse(textPane.getScrollableTracksViewportWidth());

        // Edge case - width of the parent component is zero
        textPane.setSize(new Dimension(100, 100));
        textPane.setParent(null);
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() throws BadLocationException {
        // Normal case - highlight a single word
        String term = "example";
        int flags = 0;
        List<LineResult> lineResults = textPane.highlight(term, flags);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLineNumber());
        assertEquals(0, lineResult.getLineStartOffset());
        assertEquals(0, lineResult.getLineEndOffset());
        assertEquals(1, lineResult.getWordResults().size());
        WordResult wordResult = lineResult.getWordResults().get(0);
        assertEquals(0, wordResult.getStart());
        assertEquals(7, wordResult.getEnd());

        // Normal case - highlight multiple words on multiple lines
        term = "example";
        flags = 0;
        textPane.setText("This is an example.\nAnother example here.");
        lineResults = textPane.highlight(term, flags);
        assertEquals(2, lineResults.size());
        lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLineNumber());
        assertEquals(0, lineResult.getLineStartOffset());
        assertEquals(18, lineResult.getLineEndOffset());
        assertEquals(1, lineResult.getWordResults().size());
        wordResult = lineResult.getWordResults().get(0);
        assertEquals(8, wordResult.getStart());
        assertEquals(15, wordResult.getEnd());
        lineResult = lineResults.get(1);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(19, lineResult.getLineStartOffset());
        assertEquals(40, lineResult.getLineEndOffset());
        assertEquals(1, lineResult.getWordResults().size());
        wordResult = lineResult.getWordResults().get(0);
        assertEquals(28, wordResult.getStart());
        assertEquals(35, wordResult.getEnd());

        // Error scenario - term is null
        term = null;
        flags = 0;
        lineResults = textPane.highlight(term, flags);
        assertEquals(0, lineResults.size());

        // Error scenario - term is empty
        term = "";
        flags = 0;
        lineResults = textPane.highlight(term, flags);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        // Normal case - add highlight to a specific range
        textPane.setText("This is an example.");
        int start = 5;
        int length = 2;
        textPane.addHighlight(start, length);
        assertEquals(1, textPane.getHighlighter().getHighlights().length);

        // Error scenario - invalid start and length values
        start = -1;
        length = -1;
        try {
            textPane.addHighlight(start, length);
            fail("Expected BadLocationException to be thrown");
        } catch (BadLocationException e) {
            assertEquals("BadLocationException in SearchableTextPane", e.getMessage());
        }
        assertEquals(0, textPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testRemoveHighlights() throws BadLocationException {
        // Normal case - remove existing highlights
        textPane.setText("This is an example.");
        textPane.addHighlight(0, 4);
        textPane.addHighlight(8, 3);
        textPane.removeHighlights();
        assertEquals(0, textPane.getHighlighter().getHighlights().length);

        // Normal case - remove highlights when no highlights exist
        textPane.removeHighlights();
        assertEquals(0, textPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch() throws BadLocationException {
        // Normal case - search term is found from the given start position
        textPane.setText("This is an example.");
        String term = "example";
        int startPos = 0;
        int expectedPos = 8;
        int result = textPane.search(term, startPos);
        assertEquals(expectedPos, result);

        // Normal case - previous search term is the same as current search term
        term = "example";
        startPos = 8;
        expectedPos = 16;   // Searching for the next occurrence of "example" after the previous one
        result = textPane.search(term);
        assertEquals(expectedPos, result);

        // Normal case - search term is not found
        term = "example";
        startPos = 17;
        expectedPos = -1;
        result = textPane.search(term, startPos);
        assertEquals(expectedPos, result);

        // Normal case - search term is not found, previous search term exists
        term = "example";
        startPos = 0;
        expectedPos = -1;   // No more occurrences of "example" after the previous one
        result = textPane.search(term);
        assertEquals(expectedPos, result);

        // Error scenario - searching from invalid start position
        term = "example";
        startPos = -1;
        expectedPos = -1;
        result = textPane.search(term, startPos);
        assertEquals(expectedPos, result);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        // Normal case - convert list of word results to line results
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 3));
        words.add(new WordResult(4, 7));
        words.add(new WordResult(10, 13));
        words.add(new WordResult(15, 18));
        List<LineResult> lineResults = textPane.convertWords2Lines(words);
        assertEquals(2, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLineNumber());
        assertEquals(0, lineResult.getLineStartOffset());
        assertEquals(9, lineResult.getLineEndOffset());
        assertEquals(2, lineResult.getWordResults().size());
        WordResult wordResult = lineResult.getWordResults().get(0);
        assertEquals(0, wordResult.getStart());
        assertEquals(3, wordResult.getEnd());
        wordResult = lineResult.getWordResults().get(1);
        assertEquals(4, wordResult.getStart());
        assertEquals(7, wordResult.getEnd());
        lineResult = lineResults.get(1);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(10, lineResult.getLineStartOffset());
        assertEquals(27, lineResult.getLineEndOffset());
        assertEquals(2, lineResult.getWordResults().size());
        wordResult = lineResult.getWordResults().get(0);
        assertEquals(10, wordResult.getStart());
        assertEquals(13, wordResult.getEnd());
        wordResult = lineResult.getWordResults().get(1);
        assertEquals(15, wordResult.getStart());
        assertEquals(18, wordResult.getEnd());

        // Normal case - convert empty list of word results
        words = new ArrayList<>();
        lineResults = textPane.convertWords2Lines(words);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        // Normal case - update word result with line number and line offset
        WordResult wordResult = new WordResult(4, 7);
        LineResult lineResult = new LineResult(2, 0, 9);
        textPane.updateWordResult(wordResult, lineResult);
        assertEquals(2, wordResult.getParent().getLineNumber());
        assertEquals(0, wordResult.getLineOffset());

        // Error scenario - word result is null
        wordResult = null;
        try {
            textPane.updateWordResult(wordResult, lineResult);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("wordResult is null", e.getMessage());
        }

        // Error scenario - line result is null
        wordResult = new WordResult(4, 7);
        lineResult = null;
        try {
            textPane.updateWordResult(wordResult, lineResult);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("lineResult is null", e.getMessage());
        }
    }

}