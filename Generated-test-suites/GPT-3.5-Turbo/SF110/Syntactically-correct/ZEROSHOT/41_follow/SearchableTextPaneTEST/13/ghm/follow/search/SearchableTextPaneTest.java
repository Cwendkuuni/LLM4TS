package ghm.follow.search;

import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

    private SearchableTextPane pane;

    @Before
    public void setUp() {
        pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }

    @After
    public void tearDown() {
        pane = null;
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean result = pane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testHighlight() {
        String term = "test";
        int flags = SearchEngine.FLAG_CASE_INSENSITIVE | SearchEngine.FLAG_WHOLE_WORD;
        List<LineResult> lineResults = pane.highlight(term, flags);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        try {
            pane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("Exception thrown");
        }
        Highlighter.Highlight[] highlights = pane.getHighlighter().getHighlights();
        assertNotNull(highlights);
        assertEquals(1, highlights.length);
    }

    @Test
    public void testRemoveHighlights() {
        try {
            pane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("Exception thrown");
        }
        pane.removeHighlights();
        Highlighter.Highlight[] highlights = pane.getHighlighter().getHighlights();
        assertNull(highlights);
    }

    @Test
    public void testSearch() {
        String term = "test";
        int result = pane.search(term);
        assertEquals(-1, result);
    }

    @Test
    public void testSearchWithStartPosition() {
        String term = "test";
        int startPos = 10;
        int result = pane.search(term, startPos);
        assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = new ArrayList<>();
        WordResult word = new WordResult(0, 4);
        words.add(word);
        try {
            List<LineResult> lines = pane.convertWords2Lines(words);
            assertNotNull(lines);
            assertEquals(1, lines.size());
        } catch (BadLocationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testUpdateWordResult() {
        WordResult word = new WordResult(0, 4);
        LineResult line = new LineResult(1, 0, 10);
        try {
            pane.updateWordResult(word, line);
            assertNotNull(line.getWordResults());
            assertEquals(1, line.getWordResults().size());
        } catch (BadLocationException e) {
            fail("Exception thrown");
        }
    }
}