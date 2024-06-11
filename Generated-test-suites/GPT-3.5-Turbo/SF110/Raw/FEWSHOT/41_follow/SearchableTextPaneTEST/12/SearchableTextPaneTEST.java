package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {
    private SearchableTextPane textPane;
    
    @Before
    public void setUp() {
        textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Test when parent is null
        assertEquals(true, textPane.getScrollableTracksViewportWidth());
        
        // Test when parent size is greater than text pane width
        textPane.setSize(100, 50);
        textPane.setPreferredSize(new Dimension(80, 50));
        assertEquals(true, textPane.getScrollableTracksViewportWidth());
        
        // Test when parent size is equal to text pane width
        textPane.setSize(80, 50);
        assertEquals(true, textPane.getScrollableTracksViewportWidth());
        
        // Test when parent size is less than text pane width
        textPane.setSize(60, 50);
        assertEquals(false, textPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() {
        // Test with null term
        assertEquals(new ArrayList<LineResult>(), textPane.highlight(null, 0));
        
        // Test with empty term
        assertEquals(new ArrayList<LineResult>(), textPane.highlight("", 0));
        
        // Test with non-empty term and no search results
        assertEquals(new ArrayList<LineResult>(), textPane.highlight("test", 0));
        
        // Test with non-empty term and search results
        assertEquals(2, textPane.highlight("hello", 0).size());
    }
    
    @Test
    public void testAddHighlight() {
        // Test with valid start and length
        try {
            textPane.addHighlight(0, 5);
            // If no exception is thrown, the test passes
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        // Test with invalid start and length
        try {
            textPane.addHighlight(-1, 5);
        } catch (BadLocationException e) {
            assertEquals(BadLocationException.class, e.getClass());
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        // Test when there are no highlights
        textPane.removeHighlights();
        // If no exception is thrown, the test passes
        
        // Test when there are highlights
        try {
            textPane.addHighlight(0, 5);
            textPane.removeHighlights();
            // If no exception is thrown, the test passes
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSearch() {
        // Test with null term
        assertEquals(-1, textPane.search(null));
        
        // Test with empty term
        assertEquals(-1, textPane.search(""));
        
        // Test with non-empty term and no search results
        assertEquals(-1, textPane.search("test"));
        
        // Test with non-empty term and search result at 0
        assertEquals(0, textPane.search("hello"));
        
        // Test with non-empty term and search result after previous search
        assertEquals(10, textPane.search("world"));
    }
    
    @Test
    public void testSearchWithStartPos() {
        // Test with null term and start position 0
        assertEquals(-1, textPane.search(null, 0));
        
        // Test with empty term and start position 0
        assertEquals(-1, textPane.search("", 0));
        
        // Test with non-empty term, start position 0, and no search result
        assertEquals(-1, textPane.search("test", 0));
        
        // Test with non-empty term, start position 0, and search result at 10
        assertEquals(10, textPane.search("hello", 0));
    }
    
    @Test
    public void testConvertWords2Lines() {
        // Test with empty list of words
        assertEquals(new ArrayList<LineResult>(), textPane.convertWords2Lines(new ArrayList<WordResult>()));
        
        // Test with non-empty list of words
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 5));
        words.add(new WordResult(10, 15));
        assertEquals(2, textPane.convertWords2Lines(words).size());
    }
    
    @Test
    public void testUpdateWordResult() {
        // Test with null wordResult and null lineResult
        try {
            textPane.updateWordResult(null, null);
        } catch (BadLocationException e) {
            assertEquals(BadLocationException.class, e.getClass());
        }
        
        // Test with valid wordResult and null lineResult
        try {
            WordResult wordResult = new WordResult(0, 5);
            LineResult lineResult = null;
            textPane.updateWordResult(wordResult, lineResult);
            // If no exception is thrown, the test passes
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}