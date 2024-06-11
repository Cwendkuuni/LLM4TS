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
    private SearchableTextPane textPane;
    
    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        textPane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Test if the scrollableTracksViewportWidth returns true when parent is null
        assertEquals(true, textPane.getScrollableTracksViewportWidth());
        
        // Test if the scrollableTracksViewportWidth returns true when ui.getPreferredSize(this).width is less than or equal to parent.getSize().width
        JPanel parent = new JPanel();
        parent.setSize(100, 100);
        textPane.setParent(parent);
        assertEquals(true, textPane.getScrollableTracksViewportWidth());
        
        // Test if the scrollableTracksViewportWidth returns false when ui.getPreferredSize(this).width is greater than parent.getSize().width
        parent.setSize(50, 50);
        assertEquals(false, textPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() {
        // Test if highlight method adds highlights to the textPane
        String searchTerm = "test";
        List<LineResult> lineResults = textPane.highlight(searchTerm, 0);
        
        assertEquals(0, lineResults.size());
        
        // TODO: Add more test cases
    }
    
    @Test
    public void testAddHighlight() {
        // Test if addHighlight method adds a highlight to the textPane
        try {
            textPane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("BadLocationException occurred: " + e.getMessage());
        }
        
        // TODO: Add more test cases
    }
    
    @Test
    public void testRemoveHighlights() {
        // Test if removeHighlights method removes all highlights from the textPane
        try {
            textPane.addHighlight(0, 5);
            textPane.addHighlight(10, 15);
            textPane.removeHighlights();
            
            // TODO: Check if the highlights are removed
        } catch (BadLocationException e) {
            fail("BadLocationException occurred: " + e.getMessage());
        }
        
        // TODO: Add more test cases
    }
    
    @Test
    public void testSearch() {
        // Test if search method returns the correct position of the searched term
        String searchTerm = "test";
        int searchPos = textPane.search(searchTerm);
        
        assertEquals(-1, searchPos);
        
        // TODO: Add more test cases
    }
    
    @Test
    public void testSearchWithStartPos() {
        // Test if search method returns the correct position of the searched term starting from the given startPos
        String searchTerm = "test";
        int startPos = 0;
        int searchPos = textPane.search(searchTerm, startPos);
        
        assertEquals(-1, searchPos);
        
        // TODO: Add more test cases
    }
    
    @Test
    public void testConvertWords2Lines() {
        // Test if convertWords2Lines method converts a list of WordResult to a list of LineResult correctly
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 4));
        words.add(new WordResult(10, 15));
        
        try {
            List<LineResult> lines = textPane.convertWords2Lines(words);
            
            assertEquals(2, lines.size());
            
            // TODO: Check if the conversion is correct
        } catch (BadLocationException e) {
            fail("BadLocationException occurred: " + e.getMessage());
        }
        
        // TODO: Add more test cases
    }
    
    @Test
    public void testUpdateWordResult() {
        // Test if updateWordResult method updates the LineResult correctly with the given WordResult
        WordResult wordResult = new WordResult(0, 4);
        LineResult lineResult = new LineResult(0, 0, 10);
        
        try {
            textPane.updateWordResult(wordResult, lineResult);
            
            // TODO: Check if the update is correct
        } catch (BadLocationException e) {
            fail("BadLocationException occurred: " + e.getMessage());
        }
        
        // TODO: Add more test cases
    }
}