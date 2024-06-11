import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import java.util.*;
import javax.swing.plaf.*;
import javax.swing.*;
import javax.swing.text.*;
import java.util.logging.*;
import java.lang.reflect.*;

public class SearchableTextPaneTest {
    
    @Test
    public void testConstructor() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane pane = new SearchableTextPane(font, tabSize);
        
        assertEquals(font, pane.getFont());
        assertEquals(tabSize, pane.getTabSize());
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        
        assertTrue(pane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        String term = "test";
        int flags = 0;
        
        List<LineResult> lineResults = pane.highlight(term, flags);
        
        assertNotNull(lineResults);
    }
    
    @Test
    public void testAddHighlight() throws BadLocationException {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        int start = 0;
        int length = 5;
        
        pane.addHighlight(start, length);
        
        Highlighter.Highlight[] highlights = pane.getHighlighter().getHighlights();
        assertEquals(1, highlights.length);
        assertEquals(start, highlights[0].getStartOffset());
        assertEquals(start + length, highlights[0].getEndOffset());
    }
    
    @Test
    public void testRemoveHighlights() throws BadLocationException {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        
        pane.addHighlight(0, 5);
        pane.removeHighlights();
        
        Highlighter.Highlight[] highlights = pane.getHighlighter().getHighlights();
        assertEquals(0, highlights.length);
    }
    
    @Test
    public void testSearch() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        String term = "test";
        
        int pos = pane.search(term);
        
        assertEquals(-1, pos);
    }
    
    @Test
    public void testSearchWithStartPosition() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        String term = "test";
        int startPos = 5;
        
        int pos = pane.search(term, startPos);
        
        assertEquals(-1, pos);
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<WordResult> words = new ArrayList<WordResult>();
        words.add(new WordResult(0, 4));
        words.add(new WordResult(6, 10));
        
        List<LineResult> lines = pane.convertWords2Lines(words);
        
        assertNotNull(lines);
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        WordResult wordResult = new WordResult(0, 4);
        LineResult lineResult = new LineResult(0, 0, 10);
        
        pane.updateWordResult(wordResult, lineResult);
        
        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(1, lineResult.getWordResults().get(0).parent.lineNumber);
        assertEquals(0, lineResult.getWordResults().get(0).getLineOffset());
    }
}