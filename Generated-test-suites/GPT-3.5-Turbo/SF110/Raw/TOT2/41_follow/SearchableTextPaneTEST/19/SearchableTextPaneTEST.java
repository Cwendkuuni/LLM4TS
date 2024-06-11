import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;

public class SearchableTextPaneTest {

    @Test
    public void testConstructor() {
        // Test case for constructor
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        assertEquals(font, textPane.getFont());
        assertEquals(4, textPane.getTabSize());
    }
    
    @Test
    public void testHighlight() {
        // Test case for highlight method
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertNotNull(textPane.highlight("test", 0));
    }
    
    @Test
    public void testRemoveHighlights() {
        // Test case for removeHighlights method
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        textPane.highlight("test", 0);
        textPane.removeHighlights();
        assertEquals(0, textPane.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testSearchWithoutStartPos() {
        // Test case for search method without startPos
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertNotNull(textPane.search("test"));
    }
    
    @Test
    public void testSearchWithStartPos() {
        // Test case for search method with startPos
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(-1, textPane.search("test", 5));
    }
}