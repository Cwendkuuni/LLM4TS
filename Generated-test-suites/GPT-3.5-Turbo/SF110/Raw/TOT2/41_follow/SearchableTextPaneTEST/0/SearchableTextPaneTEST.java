import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertTrue(searchableTextPane.highlight("test", 0).isEmpty());
    }

    @Test
    public void testAddHighlight() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        try {
            searchableTextPane.addHighlight(0, 5);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.removeHighlights();
    }

    @Test
    public void testSearch() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(-1, searchableTextPane.search("test"));
    }

    @Test
    public void testSearchWithStartPos() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(-1, searchableTextPane.search("test", 5));
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        try {
            searchableTextPane.convertWords2Lines(new ArrayList<>());
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testUpdateWordResult() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        try {
            searchableTextPane.updateWordResult(new WordResult(0, 5), new LineResult(0, 0, 10));
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

}