import static org.junit.Assert.*;
import org.junit.Test;

public class SearchableTextPaneTest {
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Test normal case
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertTrue(textPane.getScrollableTracksViewportWidth());
        
        // Test when parent is not null
        JFrame frame = new JFrame();
        frame.add(textPane);
        frame.setSize(200, 200);
        assertEquals(false, textPane.getScrollableTracksViewportWidth());
        frame.dispose();
    }
    
    @Test
    public void testHighlight() {
        // Test when term is not null
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(0, textPane.highlight("test", 0).size());
        
        // Test for BadLocationException
        SearchableTextPane textPane2 = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(0, textPane2.highlight(null, 0).size());
    }
    
    @Test
    public void testAddHighlight() {
        // Test for BadLocationException
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertThrows(BadLocationException.class, () -> textPane.addHighlight(10, 5));
    }
    
    @Test
    public void testSearch() {
        // Test when term is not null
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(-1, textPane.search("test"));
        
        // Test for BadLocationException
        SearchableTextPane textPane2 = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(-1, textPane2.search(null));
    }
    
    @Test
    public void testConvertWords2Lines() {
        // Test normal case
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(0, textPane.convertWords2Lines(new ArrayList<WordResult>()).size());
        
        // Test for BadLocationException
        SearchableTextPane textPane2 = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertThrows(BadLocationException.class, () -> textPane2.convertWords2Lines(new ArrayList<WordResult>()));
    }
    
    @Test
    public void testUpdateWordResult() {
        // Test for BadLocationException
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        WordResult wordResult = new WordResult(5, 10, new LineResult(1, 0, 5));
        LineResult lineResult = new LineResult(1, 0, 5);
        assertThrows(BadLocationException.class, () -> textPane.updateWordResult(wordResult, lineResult));
    }
    
}