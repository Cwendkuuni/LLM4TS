import org.junit.Test;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Typical use case
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        assertTrue(textPane.getScrollableTracksViewportWidth());
        
        // Edge case
        textPane.setSize(100, 100);
        assertFalse(textPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() {
        // Typical use case
        SearchableTextPane textPane = new SearchableTextPane(null, 0);
        int initialSize = textPane.highlight("test", 0).size();
        assertEquals(0, initialSize);
        
        // Edge case
        List<LineResult> lineResults = textPane.highlight("", 0);
        assertEquals(0, lineResults.size());
    }
    
    @Test
    public void testAddHighlight() {
        // Todo
    }
    
    @Test
    public void testRemoveHighlights() {
        // Todo
    }
    
    @Test
    public void testSearch() {
        // Todo
    }
    
    @Test
    public void testConvertWords2Lines() {
        // Todo
    }
    
    @Test
    public void testUpdateWordResult() {
        // Todo
    }
}