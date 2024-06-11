package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.text.BadLocationException;

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
        assertTrue(textPane.getScrollableTracksViewportWidth());
        
        textPane.setSize(100, 100);
        assertFalse(textPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() throws BadLocationException {
        String term = "search";
        int flags = 0;
        
        List<LineResult> lineResults = textPane.highlight(term, flags);
        
        assertEquals(0, lineResults.size());
        assertEquals(0, textPane.getHighlighter().getHighlights().length);
        
        term = "text";
        flags = 1;
        
        lineResults = textPane.highlight(term, flags);
        
        assertEquals(0, lineResults.size());
        assertEquals(0, textPane.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testRemoveHighlights() throws BadLocationException {
        String term = "search";
        int flags = 0;
        
        textPane.highlight(term, flags);
        
        assertEquals(0, textPane.getHighlighter().getHighlights().length);
        
        textPane.removeHighlights();
        
        assertEquals(0, textPane.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testSearch() {
        String term = "search";
        
        int pos = textPane.search(term);
        
        assertEquals(-1, pos);
        assertEquals(null, textPane.lastSearchTerm);
        
        term = "text";
        
        pos = textPane.search(term);
        
        assertEquals(-1, pos);
        assertEquals(term, textPane.lastSearchTerm);
    }
    
    @Test
    public void testSearchWithStartPos() {
        String term = "search";
        int startPos = 0;
        
        int pos = textPane.search(term, startPos);
        
        assertEquals(-1, pos);
        
        term = "text";
        startPos = 5;
        
        pos = textPane.search(term, startPos);
        
        assertEquals(-1, pos);
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        List<WordResult> words = new ArrayList<WordResult>();
        LineResult lineResult = new LineResult(1, 0, 10);
        
        List<LineResult> lines = textPane.convertWords2Lines(words);
        
        assertTrue(lines.isEmpty());
        
        WordResult wordResult = new WordResult(0, 5);
        words.add(wordResult);
        
        lines = textPane.convertWords2Lines(words);
        
        assertEquals(1, lines.size());
        assertEquals(lineResult, lines.get(0));
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(1, 0, 10);
        
        textPane.updateWordResult(wordResult, lineResult);
        
        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(1, wordResult.parent.lineNumber);
        
        assertEquals(0, wordResult.lineOffset);
    }

}