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
    private Font font;
    private int tabSize;
    
    @Before
    public void setUp() {
        font = new Font("Arial", Font.PLAIN, 12);
        tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Test when parent is null
        searchableTextPane.setParent(null);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
        
        // Test when parent is not null and ui's preferred size is less than parent's size
        Component parent = new JPanel();
        parent.setSize(new Dimension(500, 300));
        searchableTextPane.setParent(parent);
        ComponentUI ui = new BasicTextUI();
        ui.getPreferredSize(searchableTextPane);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
        
        // Test when parent is not null and ui's preferred size is greater than parent's size
        parent.setSize(new Dimension(200, 300));
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() {
        // Test with term as null
        List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
        
        // Test with term as empty string
        lineResults = searchableTextPane.highlight("", 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
        
        // Test with valid term and flags
        lineResults = searchableTextPane.highlight("test", 0);
        assertNotNull(lineResults);
    }
    
    @Test
    public void testAddHighlight() {
        // Test with valid start and length
        try {
            searchableTextPane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
        // No exception should be thrown
    }
    
    @Test
    public void testSearch() {
        // Test with term as null
        int result = searchableTextPane.search(null);
        assertEquals(-1, result);
        
        // Test with term as empty string
        result = searchableTextPane.search("");
        assertEquals(-1, result);
        
        // Test with valid term
        result = searchableTextPane.search("test");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithStartPos() {
        // Test with term as null and startPos as 0
        int result = searchableTextPane.search(null, 0);
        assertEquals(-1, result);
        
        // Test with term as empty string and startPos as 0
        result = searchableTextPane.search("", 0);
        assertEquals(-1, result);
        
        // Test with valid term and startPos as 0
        result = searchableTextPane.search("test", 0);
        assertEquals(-1, result);
    }
    
    @Test
    public void testConvertWords2Lines() {
        try {
            List<WordResult> words = new ArrayList<>();
            words.add(new WordResult(0, 4));
            List<LineResult> lines = searchableTextPane.convertWords2Lines(words);
            assertNotNull(lines);
            assertEquals(1, lines.size());
            
            lines = searchableTextPane.convertWords2Lines(new ArrayList<>());
            assertNotNull(lines);
            assertTrue(lines.isEmpty());
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
    
    @Test
    public void testUpdateWordResult() {
        try {
            LineResult lineResult = new LineResult(0, 0, 10);
            WordResult wordResult = new WordResult(0, 4);
            
            searchableTextPane.updateWordResult(wordResult, lineResult);
            
            assertNotNull(lineResult.getWordResults());
            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(1, lineResult.getWordResults().get(0).parent.lineNumber);
            assertEquals(0, lineResult.getWordResults().get(0).lineOffset);
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
}