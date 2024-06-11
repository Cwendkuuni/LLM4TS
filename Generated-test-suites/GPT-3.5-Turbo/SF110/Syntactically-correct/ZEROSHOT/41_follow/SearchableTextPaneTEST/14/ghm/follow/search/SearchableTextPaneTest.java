package ghm.follow.search;

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
    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testHighlight() {
        String term = "test";
        int flags = 0;
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);

        // Test if highlights are added
        Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertNotNull(highlights);
        assertEquals(lineResults.size(), highlights.length);

        // Test if wordResults in lineResults are correct
        for (int i = 0; i < lineResults.size(); i++) {
            LineResult lineResult = lineResults.get(i);
            List<WordResult> wordResults = lineResult.getWordResults();
            assertEquals(wordResults.size(), 1);
            WordResult wordResult = wordResults.get(0);
            assertEquals(highlights[i].getStartOffset(), wordResult.start);
            assertEquals(highlights[i].getEndOffset(), wordResult.end);
        }

        // Test if lineOffsets and lineNumbers in wordResults are correct
        for (int i = 0; i < lineResults.size(); i++) {
            LineResult lineResult = lineResults.get(i);
            List<WordResult> wordResults = lineResult.getWordResults();
            for (WordResult wordResult : wordResults) {
                int lineOffset = searchableTextPane.getLineStartOffset(lineResult.getLine());
                assertEquals(wordResult.getLineOffset(), lineOffset);
                assertEquals(wordResult.parent.lineNumber, lineResult.getLine() + 1);
            }
        }
    }

    @Test
    public void testRemoveHighlights() {
        String term = "test";
        int flags = 0;
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);

        // Test if highlights are added before removing them
        Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertNotNull(highlights);
        assertEquals(lineResults.size(), highlights.length);

        // Remove highlights
        searchableTextPane.removeHighlights();

        // Test if highlights are removed
        highlights = searchableTextPane.getHighlighter().getHighlights();
        assertNull(highlights);
    }

    @Test
    public void testSearch() {
        String term = "test";
        int flags = 0;

        // Test if search returns correct position for the first time
        int pos = searchableTextPane.search(term);
        assertEquals(0, pos);

        // Test if search returns correct position for subsequent searches
        pos = searchableTextPane.search(term);
        assertNotEquals(0, pos);

        // Test if search returns -1 for non-existent term
        term = "non-existent";
        pos = searchableTextPane.search(term);
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchWithStartPosition() {
        String term = "test";
        int flags = 0;
        int startPos = 5;

        // Test if search returns correct position for the first time
        int pos = searchableTextPane.search(term, startPos);
        assertEquals(0, pos);

        // Test if search returns correct position for subsequent searches
        pos = searchableTextPane.search(term, startPos);
        assertNotEquals(0, pos);

        // Test if search returns -1 for non-existent term
        term = "non-existent";
        pos = searchableTextPane.search(term, startPos);
        assertEquals(-1, pos);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult("test", 0, 4));
        words.add(new WordResult("test", 5, 9));
        words.add(new WordResult("test", 10, 14));

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
        assertNotNull(lineResults);
        assertEquals(1, lineResults.size());

        LineResult lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLine());
        assertEquals(0, lineResult.getLineStartOffset());
        assertEquals(15, lineResult.getLineEndOffset());

        List<WordResult> wordResults = lineResult.getWordResults();
        assertNotNull(wordResults);
        assertEquals(3, wordResults.size());

        for (int i = 0; i < wordResults.size(); i++) {
            WordResult wordResult = wordResults.get(i);
            assertEquals(words.get(i).text, wordResult.text);
            assertEquals(words.get(i).start, wordResult.start);
            assertEquals(words.get(i).end, wordResult.end);
        }
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult("test", 0, 4));
        words.add(new WordResult("test", 5, 9));
        words.add(new WordResult("test", 10, 14));

        LineResult lineResult = new LineResult(0, 0, 15);
        for (WordResult wordResult : words) {
            searchableTextPane.updateWordResult(wordResult, lineResult);

            int line = searchableTextPane.getLineOfOffset(wordResult.start);
            assertEquals(line, wordResult.getLineOffset());
            assertEquals(line + 1, wordResult.parent.lineNumber);
        }
    }
}