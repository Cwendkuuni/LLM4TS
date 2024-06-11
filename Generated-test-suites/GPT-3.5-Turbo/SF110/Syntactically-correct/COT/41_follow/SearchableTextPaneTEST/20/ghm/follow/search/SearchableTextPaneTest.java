

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.util.List;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setup() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        searchableTextPane = new SearchableTextPane(font, 4);
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
        try {
            searchableTextPane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("Unexpected exception occurred: " + e);
        }
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
    }

    @Test
    public void testSearch() {
        int result = searchableTextPane.search("test");
        assertEquals(-1, result);
    }

    @Test
    public void testSearchWithStartPosition() {
        int result = searchableTextPane.search("test", 10);
        assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines() {
        try {
            List<LineResult> lineResults = searchableTextPane.convertWords2Lines(null);
            assertNotNull(lineResults);
        } catch (BadLocationException e) {
            fail("Unexpected exception occurred: " + e);
        }
    }

    @Test
    public void testUpdateWordResult() {
        try {
            WordResult wordResult = new WordResult(0, 5);
            LineResult lineResult = new LineResult(0, 0, 10);
            
            searchableTextPane.updateWordResult(wordResult, lineResult);
            
            assertEquals(1, wordResult.parent.lineNumber);
            assertEquals(0, wordResult.lineOffset);
            assertEquals(wordResult, lineResult.getWordResults().get(0));
        } catch (BadLocationException e) {
            fail("Unexpected exception occurred: " + e);
        }
    }
}