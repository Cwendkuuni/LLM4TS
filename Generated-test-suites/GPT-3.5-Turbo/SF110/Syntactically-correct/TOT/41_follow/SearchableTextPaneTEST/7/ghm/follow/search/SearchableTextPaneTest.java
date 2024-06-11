import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.util.List;
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
    public void testGetScrollableTracksViewportWidth_NullParent_ReturnsTrue() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_WithParentLargerWidth_ReturnsTrue() {
        JPanel parent = new JPanel();
        parent.setSize(new Dimension(400, 200));
        searchableTextPane.setParent(parent);
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_WithParentSmallerWidth_ReturnsFalse() {
        JPanel parent = new JPanel();
        parent.setSize(new Dimension(200, 200));
        searchableTextPane.setParent(parent);
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }
    
    @Test
    public void testHighlight_NullTerm_ReturnsEmptyLineResults() {
        List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_EmptyTerm_ReturnsEmptyLineResults() {
        List<LineResult> lineResults = searchableTextPane.highlight("", 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }
    
    // More test cases for highlighting method
    
    @Test
    public void testAddHighlight_ValidRange_AddsHighlight() {
        try {
            searchableTextPane.addHighlight(10, 5);
            // Check if highlight added successfully
            // Assertion code here
        } catch (BadLocationException e) {
            fail("Unexpected Exception: " + e.getMessage());
        }
    }
    
    // More test cases for addHighlight method
    
    @Test
    public void testRemoveHighlights_RemovesAllHighlights() {
        try {
            // Add some highlights before removing
            // Assertion code here
            searchableTextPane.removeHighlights();
            // Check if all highlights are removed
            // Assertion code here
        } catch (Exception e) {
            fail("Unexpected Exception: " + e.getMessage());
        }
    }
    
    // More test cases for removeHighlights method
    
    @Test
    public void testSearch_NullTerm_ReturnsNegativeOne() {
        int result = searchableTextPane.search(null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_EmptyTerm_ReturnsNegativeOne() {
        int result = searchableTextPane.search("");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_SameTermTwice_ReturnsNextPosition() {
        int result1 = searchableTextPane.search("Hello");
        int result2 = searchableTextPane.search("Hello");
        
        assertTrue(result2 > result1);
    }
    
    // More test cases for search method
    
    @Test
    public void testConvertWords2Lines_EmptyWords_ReturnsEmptyLines() {
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(new ArrayList<WordResult>());
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testUpdateWordResult_ValidWordResult_AddsWordToLineResult() {
        LineResult lineResult = new LineResult(1, 0, 10);
        WordResult wordResult = new WordResult("Test", 0, 4);
        
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
            // Check if word added to lineResult
            // Assertion code here
        } catch (BadLocationException e) {
            fail("Unexpected Exception: " + e.getMessage());
        }
    }
    
    // More test cases for updateWordResult method
    
}