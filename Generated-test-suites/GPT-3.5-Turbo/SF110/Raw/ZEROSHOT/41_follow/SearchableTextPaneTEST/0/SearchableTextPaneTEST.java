import org.junit.Test;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane textPane = new SearchableTextPane();
        
        boolean result = textPane.getScrollableTracksViewportWidth();
        
        assertTrue(result);
    }
    
    @Test
    public void testHighlight() {
        SearchableTextPane textPane = new SearchableTextPane();
        String term = "search";
        int flags = 0;
        
        List<LineResult> result = textPane.highlight(term, flags);
        
        assertNotNull(result);
    }
    
    @Test
    public void testAddHighlight() {
        SearchableTextPane textPane = new SearchableTextPane();
        int start = 0;
        int length = 5;
        
        assertThrows(BadLocationException.class, () -> {
            textPane.addHighlight(start, length);
        });
    }
    
    @Test
    public void testRemoveHighlights() {
        SearchableTextPane textPane = new SearchableTextPane();
        
        textPane.removeHighlights();
        
        // No assertion, method should run without any exception
    }
    
    @Test
    public void testSearch() {
        SearchableTextPane textPane = new SearchableTextPane();
        String term = "search";
        
        int result = textPane.search(term);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithStartPosition() {
        SearchableTextPane textPane = new SearchableTextPane();
        String term = "search";
        int startPos = 5;
        
        int result = textPane.search(term, startPos);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane textPane = new SearchableTextPane();
        List<WordResult> words = new ArrayList<>();
        
        assertThrows(BadLocationException.class, () -> {
            textPane.convertWords2Lines(words);
        });
    }
    
    @Test
    public void testUpdateWordResult() {
        SearchableTextPane textPane = new SearchableTextPane();
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(1, 0, 10);
        
        assertThrows(BadLocationException.class, () -> {
            textPane.updateWordResult(wordResult, lineResult);
        });
    }
    
}