
package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentWidth() {
        searchableTextPane.setSize(150, 200);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withChildWidth() {
        JPanel parent = new JPanel();
        parent.setSize(100, 200);
        parent.add(searchableTextPane);
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        List<LineResult> lineResults = searchableTextPane.highlight("term", 0);
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() {
        try {
            searchableTextPane.addHighlight(0, 5);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testRemoveHighlights() {
        try {
            searchableTextPane.removeHighlights();
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSearch() {
        int pos = searchableTextPane.search("term");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearch_withStartPos() {
        int pos = searchableTextPane.search("term", 5);
        assertEquals(-1, pos);
    }

    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = new ArrayList<>();
        try {
            List<LineResult> lines = searchableTextPane.convertWords2Lines(words);
            assertNotNull(lines);
            assertEquals(0, lines.size());
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(0, 0, 10);
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
            assertNotNull(lineResult.getWords());
            assertEquals(1, lineResult.getWords().size());
        } catch (Exception e) {
            assertFalse(true);
        }
    }
}
