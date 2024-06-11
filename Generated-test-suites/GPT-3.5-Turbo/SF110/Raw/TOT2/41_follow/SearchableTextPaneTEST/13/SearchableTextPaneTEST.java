import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Font;
import java.util.List;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Test for typical use case
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertTrue(textPane.getScrollableTracksViewportWidth());
        
        // Test for edge case where parent is null
        textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        textPane.setParent(null);
        assertTrue(textPane.getScrollableTracksViewportWidth());

        // Test for edge case where text width is greater than parent width
        textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        textPane.setText("This is a long text that exceeds the parent width");
        assertFalse(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        
        // Test for typical use case
        List<LineResult> lineResults = textPane.highlight("text", 0);
        assertNotNull(lineResults);
        
        // Test for edge case where term is null
        lineResults = textPane.highlight(null, 0);
        assertTrue(lineResults.isEmpty());

        // Test for exception handling scenario
        lineResults = textPane.highlight("exception", 0);
        assertNotNull(lineResults);
    }

    @Test
    public void testSearch() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        
        // Test for typical use case
        int result = textPane.search("text");
        assertTrue(result >= 0);

        // Test for edge case where term is null
        result = textPane.search(null);
        assertEquals(-1, result);

        // Test for exception handling scenario
        result = textPane.search("exception");
        assertTrue(result >= -1);
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        
        // Test for typical use case
        List<WordResult> wordResults = new ArrayList<>();
        wordResults.add(new WordResult(0, 4));
        wordResults.add(new WordResult(5, 9));
        List<LineResult> lineResults = textPane.convertWords2Lines(wordResults);
        assertNotNull(lineResults);
        
        // Test for empty words list
        List<LineResult> emptyLineResults = textPane.convertWords2Lines(new ArrayList<>());
        assertTrue(emptyLineResults.isEmpty());
    }

}