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
        searchableTextPane = new SearchableTextPane(font, 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() {
        try {
            searchableTextPane.addHighlight(0, 5);
            Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
            assertEquals(1, highlights.length);
        } catch (BadLocationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testRemoveHighlights() {
        try {
            searchableTextPane.addHighlight(0, 5);
            Highlighter.Highlight[] beforeHighlights = searchableTextPane.getHighlighter().getHighlights();
            assertEquals(1, beforeHighlights.length);
            searchableTextPane.removeHighlights();
            Highlighter.Highlight[] afterHighlights = searchableTextPane.getHighlighter().getHighlights();
            assertEquals(0, afterHighlights.length);
        } catch (BadLocationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSearch_sameTerm() {
        searchableTextPane.search("test");
        int pos = searchableTextPane.search("test");
        assertEquals(0, pos);
    }

    @Test
    public void testSearch_differentTerm() {
        searchableTextPane.search("test1");
        int pos = searchableTextPane.search("test2");
        assertEquals(0, pos);
    }

    @Test
    public void testSearch_nullTerm() {
        int pos = searchableTextPane.search(null);
        assertEquals(-1, pos);
    }

    @Test
    public void testSearch_emptyTerm() {
        int pos = searchableTextPane.search("");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearch_startPos() {
        int pos = searchableTextPane.search("test", 5);
        assertEquals(-1, pos);
    }

    @Test
    public void testConvertWords2Lines() {
        try {
            List<WordResult> words = new ArrayList<>();
            WordResult word1 = new WordResult(0, 5, null);
            WordResult word2 = new WordResult(10, 15, null);
            WordResult word3 = new WordResult(20, 25, null);
            words.add(word1);
            words.add(word2);
            words.add(word3);
            List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
            assertNotNull(lineResults);
            assertEquals(1, lineResults.size());
        } catch (BadLocationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateWordResult() {
        try {
            WordResult wordResult = new WordResult(0, 5, null);
            LineResult lineResult = new LineResult(0, 0, 10);
            searchableTextPane.updateWordResult(wordResult, lineResult);
            assertEquals(1, lineResult.getWordResults().size());
        } catch (BadLocationException e) {
            fail(e.getMessage());
        }
    }
}