import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.logging.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {
    private SearchableTextPane searchableTextPane;
    
    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        searchableTextPane = new SearchableTextPane(font, 4);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Test when parent is null
        boolean result1 = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result1);
        
        // Test when parent is not null and preferred width is less than or equal to parent width
        JPanel parent = new JPanel();
        parent.setSize(new Dimension(100, 100));
        parent.add(searchableTextPane);
        boolean result2 = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result2);
        
        // Test when parent is not null and preferred width is greater than parent width
        parent.setSize(new Dimension(50, 100));
        boolean result3 = searchableTextPane.getScrollableTracksViewportWidth();
        assertFalse(result3);
    }
    
    @Test
    public void testHighlight() {
        // Test highlight when term is null
        List<LineResult> lineResults1 = searchableTextPane.highlight(null, 0);
        assertNotNull(lineResults1);
        assertTrue(lineResults1.isEmpty());
        
        // Test highlight when term is empty
        List<LineResult> lineResults2 = searchableTextPane.highlight("", 0);
        assertNotNull(lineResults2);
        assertTrue(lineResults2.isEmpty());
        
        // Test highlight when term is not null and not empty
        List<LineResult> lineResults3 = searchableTextPane.highlight("test", 0);
        assertNotNull(lineResults3);
    }
    
    @Test
    public void testAddHighlight() {
        // Test addHighlight with valid start and length
        try {
            searchableTextPane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
        
        // Test addHighlight with invalid start and length
        try {
            searchableTextPane.addHighlight(-1, 5);
            fail("BadLocationException should be thrown");
        } catch (BadLocationException e) {
            // expected exception
        }
        
        try {
            searchableTextPane.addHighlight(0, -5);
            fail("BadLocationException should be thrown");
        } catch (BadLocationException e) {
            // expected exception
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        // Test removeHighlights
        searchableTextPane.removeHighlights();
    }
    
    @Test
    public void testSearch() {
        // Test search when term is null
        int result1 = searchableTextPane.search(null);
        assertEquals(-1, result1);
        
        // Test search when term is empty
        int result2 = searchableTextPane.search("");
        assertEquals(-1, result2);
        
        // Test search when term is not null and not empty
        int result3 = searchableTextPane.search("test");
        assertEquals(-1, result3);
    }
    
    @Test
    public void testSearchWithStartPos() {
        // Test search with startPos when term is null
        int result1 = searchableTextPane.search(null, 0);
        assertEquals(-1, result1);
        
        // Test search with startPos when term is empty
        int result2 = searchableTextPane.search("", 0);
        assertEquals(-1, result2);
        
        // Test search with startPos when term is not null and not empty
        int result3 = searchableTextPane.search("test", 0);
        assertEquals(-1, result3);
    }
    
    @Test
    public void testConvertWords2Lines() {
        // Test convertWords2Lines when words are empty
        List<WordResult> words1 = new ArrayList<>();
        try {
            List<LineResult> lineResults1 = searchableTextPane.convertWords2Lines(words1);
            assertNotNull(lineResults1);
            assertTrue(lineResults1.isEmpty());
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
        
        // Test convertWords2Lines when words are not empty
        WordResult word1 = new WordResult(0, 4);
        WordResult word2 = new WordResult(5, 9);
        List<WordResult> words2 = new ArrayList<>();
        words2.add(word1);
        words2.add(word2);
        try {
            List<LineResult> lineResults2 = searchableTextPane.convertWords2Lines(words2);
            assertNotNull(lineResults2);
            assertFalse(lineResults2.isEmpty());
            assertEquals(1, lineResults2.size());
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
    
    @Test
    public void testUpdateWordResult() {
        // Test updateWordResult with valid wordResult and lineResult
        WordResult wordResult = new WordResult(0, 4);
        LineResult lineResult = new LineResult(0, 0, 10);
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
            assertEquals(1, lineResult.getWordResults().size());
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
        
        // Test updateWordResult with invalid line number
        WordResult wordResult2 = new WordResult(0, 4);
        LineResult lineResult2 = new LineResult(0, 0, 10);
        try {
            searchableTextPane.updateWordResult(wordResult2, lineResult2);
            fail("BadLocationException should be thrown");
        } catch (BadLocationException e) {
            // expected exception
        }
    }
}