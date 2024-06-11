import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {
    
    private SearchableTextPane searchableTextPane;
    
    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_noParent_returnsTrue() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_parentWidthLessThanPreferredWidth_returnsTrue() {
        JScrollPane parent = new JScrollPane();
        parent.setPreferredSize(new Dimension(200, 300));
        searchableTextPane.setParent(parent);
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        
        assertTrue(result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_parentWidthGreaterThanPreferredWidth_returnsFalse() {
        JScrollPane parent = new JScrollPane();
        parent.setPreferredSize(new Dimension(500, 300));
        searchableTextPane.setParent(parent);
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        
        assertFalse(result);
    }
    
    @Test
    public void testHighlight_noTerm_returnsEmptyLineResults() {
        List<LineResult> result = searchableTextPane.highlight(null, 0);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testHighlight_emptyTerm_returnsEmptyLineResults() {
        List<LineResult> result = searchableTextPane.highlight("", 0);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testHighlight_validTermAndFlags_returnsLineResults() {
        String term = "search";
        int flags = 0;
        
        List<LineResult> result = searchableTextPane.highlight(term, flags);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testAddHighlight_validStartAndLength_addsHighlight() {
        int start = 0;
        int length = 5;
        
        try {
            searchableTextPane.addHighlight(start, length);
            Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
            assertEquals(1, highlights.length);
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
    
    @Test
    public void testRemoveHighlights_removeAllHighlights() {
        try {
            searchableTextPane.addHighlight(0, 5);
            searchableTextPane.addHighlight(10, 8);
            searchableTextPane.addHighlight(20, 4);
            
            searchableTextPane.removeHighlights();
            Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
            assertEquals(0, highlights.length);
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
    
    @Test
    public void testSearch_noTerm_returnsNegativeOne() {
        int result = searchableTextPane.search(null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_emptyTerm_returnsNegativeOne() {
        int result = searchableTextPane.search("");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_sameTermAsLastSearch_returnsCorrectPosition() {
        searchableTextPane.lastSearchTerm = "search";
        searchableTextPane.lastSearchPos = 5;
        
        int result = searchableTextPane.search("search");
        
        assertEquals(10, result);
    }
    
    @Test
    public void testSearch_differentTermAsLastSearch_returnsCorrectPosition() {
        searchableTextPane.lastSearchTerm = "search";
        searchableTextPane.lastSearchPos = 5;
        
        int result = searchableTextPane.search("test");
        
        assertEquals(0, result);
    }
    
    @Test
    public void testSearch_validTermAndStartPos_returnsCorrectPosition() {
        searchableTextPane.setText("This is a test");
        
        int result = searchableTextPane.search("test", 0);
        
        assertEquals(10, result);
    }
    
    @Test
    public void testSearch_invalidStartPos_returnsNegativeOne() {
        searchableTextPane.setText("This is a test");
        
        int result = searchableTextPane.search("test", 20);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testConvertWords2Lines_validWords_returnsLineResults() {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 3));
        words.add(new WordResult(5, 8));
        words.add(new WordResult(12, 15));
        
        try {
            List<LineResult> result = searchableTextPane.convertWords2Lines(words);
            
            assertNotNull(result);
            assertFalse(result.isEmpty());
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
    
    @Test
    public void testUpdateWordResult_validWordAndLineResult_updatesWordResult() {
        WordResult wordResult = new WordResult(0, 3);
        LineResult lineResult = new LineResult(0, 0, 10);
        
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
            
            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(1, wordResult.parent.lineNumber);
            assertEquals(0, wordResult.lineOffset);
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
}