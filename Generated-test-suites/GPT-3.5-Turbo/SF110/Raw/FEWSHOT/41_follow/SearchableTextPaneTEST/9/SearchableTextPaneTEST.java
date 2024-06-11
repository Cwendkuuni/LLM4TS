
package ghm.follow.search;

import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.logging.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testHighlight() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = textPane.highlight("term", 0);
        assertNotNull(lineResults);
    }
    
    @Test
    public void testAddHighlight() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        textPane.setText("This is a test.");
        textPane.addHighlight(0, 4);
        Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
        assertNotNull(highlights);
        assertEquals(1, highlights.length);
    }
    
    @Test
    public void testRemoveHighlights() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        textPane.setText("This is a test.");
        textPane.addHighlight(0, 4);
        textPane.removeHighlights();
        Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
        assertNotNull(highlights);
        assertEquals(0, highlights.length);
    }
    
    @Test
    public void testSearch() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        int result = textPane.search("term");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchStartPos() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        textPane.setText("This is a test.");
        int result = textPane.search("is", 5);
        assertEquals(2, result);
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 3));
        words.add(new WordResult(5, 7));
        words.add(new WordResult(9, 12));
        List<LineResult> lineResults = textPane.convertWords2Lines(words);
        assertNotNull(lineResults);
        assertEquals(1, lineResults.size());
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        LineResult lineResult = new LineResult(1, 0, 15);
        WordResult wordResult = new WordResult(5, 7);
        textPane.updateWordResult(wordResult, lineResult);
        assertEquals(1, wordResult.parent.lineNumber);
        assertEquals(0, wordResult.lineOffset);
    }

}
