package ghm.follow.search;

import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.util.logging.*;
import java.util.*;
import javax.swing.text.*;
import java.io.*;

public class SearchableTextPaneTest {
    
    private SearchableTextPane searchableTextPane;
    
    @Before
    public void setUp() throws Exception {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertNotNull(result);
    }
    
    @Test
    public void testHighlight() {
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertNotNull(lineResults);
    }
    
    @Test
    public void testAddHighlight() throws BadLocationException {
        searchableTextPane.addHighlight(0, 5);
        Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertNotNull(highlights);
        assertEquals(1, highlights.length);
    }
    
    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
        Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertNotNull(highlights);
        assertEquals(0, highlights.length);
    }
    
    @Test
    public void testSearch() {
        int result = searchableTextPane.search("test");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithStartPos() {
        int result = searchableTextPane.search("test", 0);
        assertEquals(-1, result);
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        ArrayList<WordResult> words = new ArrayList<WordResult>();
        words.add(new WordResult(0, 3));
        words.add(new WordResult(5, 8));
        words.add(new WordResult(10, 13));
        ArrayList<LineResult> lines = searchableTextPane.convertWords2Lines(words);
        assertNotNull(lines);
        assertEquals(1, lines.size());
        assertEquals(0, lines.get(0).getLineNumber());
        assertEquals(0, lines.get(0).getLineStartOffset());
        assertEquals(15, lines.get(0).getLineEndOffset());
        assertEquals(words, lines.get(0).getWordResults());
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        WordResult wordResult = new WordResult(0, 3);
        LineResult lineResult = new LineResult(0, 0, 15);
        searchableTextPane.updateWordResult(wordResult, lineResult);
        assertEquals(0, wordResult.getParent().getLineNumber());
        assertEquals(0, wordResult.getLineOffset());
    }
}