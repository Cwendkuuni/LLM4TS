import ghm.follow.search.SearchableTextPane;
import org.junit.Test;
import javax.swing.text.BadLocationException;
import java.awt.Font;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = textPane.highlight("test", 0);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        try {
            textPane.addHighlight(0, 4);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        textPane.removeHighlights();
    }

    @Test
    public void testSearch() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        int result = textPane.search("test");
        assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<WordResult> words = new ArrayList<>();
        LineResult lineResult = textPane.convertWords2Lines(words);
        assertNotNull(lineResult);
    }

    @Test
    public void testUpdateWordResult() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        WordResult wordResult = new WordResult(0, 4);
        LineResult lineResult = new LineResult(1, 2, 3);
        try {
            textPane.updateWordResult(wordResult, lineResult);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}