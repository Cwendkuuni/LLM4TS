package ghm.follow.search;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;
import java.util.List;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }

    @Test
    public void testHighlight() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        List<LineResult> lineResults = textPane.highlight("test", 0);
        assertNotNull(lineResults);
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        textPane.removeHighlights();
        // Need to verify visually
    }

    @Test
    public void testSearch() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        int result = textPane.search("test");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithStartPos() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        int result = textPane.search("test", 0);
        assertEquals(-1, result);
    }
}
