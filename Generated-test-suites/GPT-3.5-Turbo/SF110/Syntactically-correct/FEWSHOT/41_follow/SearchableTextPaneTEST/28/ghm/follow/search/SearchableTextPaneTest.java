package ghm.follow.search;

import org.junit.Before;
import org.junit.Test;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;
    
    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidthWithoutParent() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testGetScrollableTracksViewportWidthWithParent() {
        searchableTextPane.setParent(new JPanel());
        
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlightWithEmptyTerm() {
        List<LineResult> lineResults = searchableTextPane.highlight("", 0);
        
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
    }
    
    @Test
    public void testHighlightWithNullTerm() {
        List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
        
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
    }
    
    @Test
    public void testHighlightWithTerm() {
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
    }
    
    @Test
    public void testAddHighlight() throws BadLocationException {
        searchableTextPane.addHighlight(0, 5);
        
        DefaultHighlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        
        assertNotNull(highlights);
        assertEquals(1, highlights.length);
        assertEquals(0, highlights[0].getStartOffset());
        assertEquals(5, highlights[0].getEndOffset());
    }
    
    @Test
    public void testRemoveHighlights() throws BadLocationException {
        searchableTextPane.addHighlight(0, 5);
        
        searchableTextPane.removeHighlights();
        
        DefaultHighlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        
        assertNotNull(highlights);
        assertEquals(0, highlights.length);
    }
    
    @Test
    public void testSearchWithEmptyTerm() {
        int position = searchableTextPane.search("");
        
        assertEquals(-1, position);
        assertNull(searchableTextPane.getLastSearchTerm());
    }
    
    @Test
    public void testSearchWithNullTerm() {
        int position = searchableTextPane.search(null);
        
        assertEquals(-1, position);
        assertNull(searchableTextPane.getLastSearchTerm());
    }
    
    @Test
    public void testSearchWithTerm() {
        int position = searchableTextPane.search("test");
        
        assertEquals(-1, position);
        assertEquals("test", searchableTextPane.getLastSearchTerm());
    }
    
    @Test
    public void testSearchWithTermAndStartPosition() {
        searchableTextPane.search("test");
        
        int position = searchableTextPane.search("test", 5);
        
        assertEquals(-1, position);
        assertEquals("test", searchableTextPane.getLastSearchTerm());
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        LineResult lineResult1 = new LineResult(0, 0, 5);
        LineResult lineResult2 = new LineResult(1, 6, 10);
        LineResult lineResult3 = new LineResult(2, 11, 15);

        List<WordResult> wordResults = List.of(
                new WordResult(0, 1),
                new WordResult(6, 7),
                new WordResult(11, 12),
                new WordResult(11, 15)
        );
        
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
        
        assertNotNull(lineResults);
        assertEquals(3, lineResults.size());
        assertEquals(lineResult1, lineResults.get(0));
        assertEquals(lineResult2, lineResults.get(1));
        assertEquals(lineResult3, lineResults.get(2));
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        LineResult lineResult = new LineResult(0, 0, 5);
        WordResult wordResult = new WordResult(0, 1);
        wordResult.setParent(lineResult);
        
        searchableTextPane.updateWordResult(wordResult, lineResult);
        
        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(1, wordResult.getParent().lineNumber);
        assertEquals(0, wordResult.getLineOffset());
    }
}