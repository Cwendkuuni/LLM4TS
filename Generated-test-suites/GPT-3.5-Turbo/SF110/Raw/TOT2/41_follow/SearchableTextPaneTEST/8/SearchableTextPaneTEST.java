import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {
    
    private SearchableTextPane searchableTextPane;
    
    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.setText("This is a test text.");
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() {
        String searchTerm = "test";
        int flags = 0;
        
        assertNotNull(searchableTextPane.highlight(searchTerm, flags));
    }
    
    @Test
    public void testAddHighlight() {
        try {
            searchableTextPane.addHighlight(5, 4);
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
    }
    
    @Test
    public void testSearch() {
        String searchTerm = "test";
        
        assertEquals(10, searchableTextPane.search(searchTerm));
    }
    
    @Test
    public void testConvertWords2Lines() {
        assertNotNull(searchableTextPane.highlight("test", 0));
    }
    
    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 4);
        LineResult lineResult = new LineResult(1, 0, 10);
        
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
    }
    
    @Test
    public void testSearchExceptions() {
        try {
            searchableTextPane.search(null);
            fail("Should have thrown an exception for null search term");
        } catch (NullPointerException e) {
            // Expected exception
        }
        
        try {
            searchableTextPane.addHighlight(-1, 5);
            fail("Should have thrown an exception for negative start index");
        } catch (BadLocationException e) {
            // Expected exception
        }
    }
}