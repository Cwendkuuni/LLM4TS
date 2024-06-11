import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class SearchableTextPaneTest {

    @Test
    public void testHighlight() {
        // Test highlighting functionality
        SearchableTextPane textPane = new SearchableTextPane(null, 4);
        List<LineResult> lineResults = textPane.highlight("test", 0);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        // Test adding highlights
        SearchableTextPane textPane = new SearchableTextPane(null, 4);
        try {
            textPane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testRemoveHighlights() {
        // Test removing highlights
        SearchableTextPane textPane = new SearchableTextPane(null, 4);
        textPane.addHighlight(0, 5);
        textPane.removeHighlights();
        // Need to assert that highlights are removed
    }

    @Test
    public void testSearch() {
        // Test search functionality
        SearchableTextPane textPane = new SearchableTextPane(null, 4);
        int pos = textPane.search("test");
        assertEquals(-1, pos); // Assuming term not found initially
    }

    @Test
    public void testSearchWithStartPos() {
        // Test search functionality with specified start position
        SearchableTextPane textPane = new SearchableTextPane(null, 4);
        int pos = textPane.search("test", 5);
        assertEquals(-1, pos); // Assuming term not found initially from specified position
    }

    @Test
    public void testConvertWords2Lines() {
        // Test converting words to lines functionality
        SearchableTextPane textPane = new SearchableTextPane(null, 4);
        // Need to create WordResult list and test conversion
    }

    @Test
    public void testUpdateWordResult() {
        // Test updating word result functionality
        SearchableTextPane textPane = new SearchableTextPane(null, 4);
        // Need to mock LineResult and WordResult to test method
    }
}