import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.plaf.ComponentUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;

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
        Component parent = null;
        ComponentUI ui = null;
        
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
        
        parent = new JPanel();
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
        
        ui = new ComponentUI() {
            // Mock the ComponentUI class for testing
        };
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() throws BadLocationException {
        String term = "test";
        String text = "This is a test string";
        
        searchableTextPane.setText(text);
        
        List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
        
        assertNotNull(lineResults);
        assertEquals(1, lineResults.size());
        
        LineResult lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLine());
        assertEquals(10, lineResult.getLineStartOffset());
        assertEquals(21, lineResult.getLineEndOffset());
        
        List<WordResult> wordResults = lineResult.getWordResults();
        assertEquals(1, wordResults.size());
        
        WordResult wordResult = wordResults.get(0);
        assertEquals(10, wordResult.getStart());
        assertEquals(14, wordResult.getEnd());
        assertEquals(text.substring(10, 14), wordResult.getText());
    }
    
    @Test
    public void testAddHighlight() {
        int start = 5;
        int length = 10;
        
        try {
            searchableTextPane.addHighlight(start, length);
            DefaultHighlighter.DefaultHighlightPainter wordPainter =
                new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
            DefaultHighlighter highlighter = (DefaultHighlighter) searchableTextPane.getHighlighter();
            DefaultHighlighter.DefaultHighlightPainter painter =
                (DefaultHighlighter.DefaultHighlightPainter) highlighter.getHighlights()[0].getPainter();
            assertEquals(wordPainter, painter);
            
            assertEquals(start, highlighter.getHighlights()[0].getStartOffset());
            assertEquals(start + length, highlighter.getHighlights()[0].getEndOffset());
        } catch (BadLocationException e) {
            // Handle exception
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        try {
            searchableTextPane.addHighlight(5, 10);
            searchableTextPane.addHighlight(15, 20);
            
            searchableTextPane.removeHighlights();
            
            DefaultHighlighter highlighter = (DefaultHighlighter) searchableTextPane.getHighlighter();
            assertEquals(0, highlighter.getHighlights().length);
        } catch (BadLocationException e) {
            // Handle exception
        }
    }
    
    @Test
    public void testSearch() {
        String term = "test";
        String text = "This is a test string";
        
        searchableTextPane.setText(text);
        
        int pos = searchableTextPane.search(term);
        assertEquals(10, pos);
        
        pos = searchableTextPane.search(term, 15);
        assertEquals(-1, pos);
        
        term = "string";
        pos = searchableTextPane.search(term);
        assertEquals(15, pos);
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(5, 10, "word"));
        words.add(new WordResult(15, 20, "result"));
        
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
        
        assertNotNull(lineResults);
        assertEquals(2, lineResults.size());
        
        LineResult lineResult1 = lineResults.get(0);
        assertEquals(0, lineResult1.getLine());
        assertEquals(5, lineResult1.getLineStartOffset());
        assertEquals(15, lineResult1.getLineEndOffset());
        
        List<WordResult> wordResults1 = lineResult1.getWordResults();
        assertEquals(1, wordResults1.size());
        
        WordResult wordResult1 = wordResults1.get(0);
        assertEquals(5, wordResult1.getStart());
        assertEquals(10, wordResult1.getEnd());
        assertEquals("word", wordResult1.getText());
        
        LineResult lineResult2 = lineResults.get(1);
        assertEquals(1, lineResult2.getLine());
        assertEquals(15, lineResult2.getLineStartOffset());
        assertEquals(37, lineResult2.getLineEndOffset());
        
        List<WordResult> wordResults2 = lineResult2.getWordResults();
        assertEquals(1, wordResults2.size());
        
        WordResult wordResult2 = wordResults2.get(0);
        assertEquals(15, wordResult2.getStart());
        assertEquals(20, wordResult2.getEnd());
        assertEquals("result", wordResult2.getText());
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        WordResult wordResult = new WordResult(5, 10, "word");
        LineResult lineResult = new LineResult(0, 5, 15);
        
        searchableTextPane.updateWordResult(wordResult, lineResult);
        
        assertEquals(1, lineResult.getWordResults().size());
        
        WordResult updatedWordResult = lineResult.getWordResults().get(0);
        assertEquals(5, updatedWordResult.getStart());
        assertEquals(10, updatedWordResult.getEnd());
        assertEquals("word", updatedWordResult.getText());
        
        assertEquals(1, updatedWordResult.getParent().getLineNumber());
        assertEquals(5, updatedWordResult.getLineOffset());
    }
}