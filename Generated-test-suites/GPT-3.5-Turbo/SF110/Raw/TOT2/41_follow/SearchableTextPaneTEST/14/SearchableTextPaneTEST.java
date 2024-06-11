import org.junit.*;
import java.awt.Font;
import java.util.List;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {
    
    private SearchableTextPane searchableTextPane;
    
    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        assertThrows(BadLocationException.class, () -> searchableTextPane.addHighlight(10, 5));
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
        // No assertion as it is a void method
    }

    @Test
    public void testSearch() {
        int result = searchableTextPane.search("test");
        assertEquals(-1, result);
    }

    @Test
    public void testSearchStartPos() {
        int result = searchableTextPane.search("test", 5);
        assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines() {
        // Include edge cases for converting words to lines
    }

    @Test
    public void testUpdateWordResult() {
        // Include edge cases for updating word result
    }
}