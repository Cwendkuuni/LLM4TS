import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertThrows(BadLocationException.class, () -> {
            searchableTextPane.addHighlight(0, 5);
        });
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.highlight("test", 0);
        searchableTextPane.removeHighlights();
        assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertEquals(-1, searchableTextPane.search("test"));
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 5));
        assertThrows(BadLocationException.class, () -> {
            searchableTextPane.convertWords2Lines(words);
        });
    }

    @Test
    public void testUpdateWordResult() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(1, 0, 10);
        assertThrows(BadLocationException.class, () -> {
            searchableTextPane.updateWordResult(wordResult, lineResult);
        });
    }
}