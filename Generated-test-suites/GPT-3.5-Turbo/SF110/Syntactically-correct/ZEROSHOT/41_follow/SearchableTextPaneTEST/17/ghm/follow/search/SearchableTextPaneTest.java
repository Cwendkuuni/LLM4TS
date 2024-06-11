package ghm.follow.search;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
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
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testHighlight() {
        String term = "test";
        int flags = 0;
        List<LineResult> result = searchableTextPane.highlight(term, flags);
        assertNotNull(result);
    }

    @Test
    public void testAddHighlight() {
        int start = 0;
        int length = 5;
        try {
            searchableTextPane.addHighlight(start, length);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
        // No assertion needed as this method only performs actions, not returning any values
    }

    @Test
    public void testSearch() {
        String term = "search";
        int result = searchableTextPane.search(term);
        assertEquals(-1, result);
    }

    @Test
    public void testSearchWithStartPos() {
        String term = "start";
        int startPos = 5;
        int result = searchableTextPane.search(term, startPos);
        assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = null;
        List<LineResult> result = null;
        try {
            result = searchableTextPane.convertWords2Lines(words);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        assertNotNull(result);
    }

    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(5, 10);
        LineResult lineResult = new LineResult(1, 0, 15);
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
        } catch (Exception e) {
            fail("Exception thrown");
        }
        assertEquals(2, wordResult.parent.lineNumber);
        assertEquals(0, wordResult.getLineOffset());
    }
}