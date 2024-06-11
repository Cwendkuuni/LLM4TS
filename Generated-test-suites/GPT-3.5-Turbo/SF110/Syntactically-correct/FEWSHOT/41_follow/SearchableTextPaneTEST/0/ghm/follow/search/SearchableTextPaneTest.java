package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        searchableTextPane = new SearchableTextPane(font, 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        List<LineResult> lineResults = searchableTextPane.highlight("term", 0);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() {
        try {
            searchableTextPane.addHighlight(0, 5);
        } catch (Exception e) {
            // exception not expected
        }
        assertEquals(1, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.addHighlight(0, 5);
        searchableTextPane.removeHighlights();
        assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearchWithSameTerm() {
        int pos = searchableTextPane.search("term");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearch() {
        int pos = searchableTextPane.search("term", 0);
        assertEquals(-1, pos);
    }

    @Test
    public void testConvertWords2Lines() {
        List<LineResult> lines = searchableTextPane.convertWords2Lines(null);
        assertEquals(0, lines.size());
    }

    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult();
        LineResult lineResult = new LineResult(0, 0, 10);
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
        } catch (Exception e) {
            // exception not expected
        }
        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(1, wordResult.parent.lineNumber);
        assertEquals(0, wordResult.getLineOffset());
    }

}