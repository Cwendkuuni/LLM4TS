import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

    private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final int DEFAULT_TAB_SIZE = 4;

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane pane = new SearchableTextPane(DEFAULT_FONT, DEFAULT_TAB_SIZE);
        assertFalse(pane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        SearchableTextPane pane = new SearchableTextPane(DEFAULT_FONT, DEFAULT_TAB_SIZE);
        List<LineResult> results = pane.highlight("test", 0);
        assertNotNull(results);
    }

    @Test
    public void testAddHighlight() {
        SearchableTextPane pane = new SearchableTextPane(DEFAULT_FONT, DEFAULT_TAB_SIZE);
        try {
            pane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("BadLocationException should not have been thrown");
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        SearchableTextPane pane = new SearchableTextPane(DEFAULT_FONT, DEFAULT_TAB_SIZE);
        pane.removeHighlights();
    }

    @Test
    public void testSearch() {
        SearchableTextPane pane = new SearchableTextPane(DEFAULT_FONT, DEFAULT_TAB_SIZE);
        int pos = pane.search("test");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchWithStartPos() {
        SearchableTextPane pane = new SearchableTextPane(DEFAULT_FONT, DEFAULT_TAB_SIZE);
        int pos = pane.search("test", 10);
        assertEquals(-1, pos);
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane pane = new SearchableTextPane(DEFAULT_FONT, DEFAULT_TAB_SIZE);
        try {
            List<WordResult> words = new ArrayList<>();
            words.add(new WordResult(0, 4));
            words.add(new WordResult(10, 14));
            List<LineResult> results = pane.convertWords2Lines(words);
            assertNotNull(results);
        } catch (BadLocationException e) {
            fail("BadLocationException should not have been thrown");
        }
    }

    @Test
    public void testUpdateWordResult() {
        SearchableTextPane pane = new SearchableTextPane(DEFAULT_FONT, DEFAULT_TAB_SIZE);
        try {
            WordResult wordResult = new WordResult(0, 4);
            LineResult lineResult = new LineResult(1, 0, 10);
            pane.updateWordResult(wordResult, lineResult);
            assertNotNull(lineResult.getWordResults());
            assertEquals(1, lineResult.getWordResults().size());
        } catch (BadLocationException e) {
            fail("BadLocationException should not have been thrown");
        }
    }
}