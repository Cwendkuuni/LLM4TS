import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Font;
import java.util.List;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertTrue(stp.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = stp.highlight("Java", 0);
        // Add assertions for the expected line results
    }

    @Test
    public void testSearch() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(-1, stp.search("Python"));
    }

    @Test
    public void testAddHighlight() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertThrows(BadLocationException.class, () -> stp.addHighlight(10, 5));
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        // Add test case to check if highlights are removed successfully
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        // Add test case to check the conversion of WordResults to LineResults
    }

    @Test
    public void testUpdateWordResult() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        // Add test case to check the updating of a WordResult within a LineResult
    }

    // Add more test cases for other methods and edge cases

}