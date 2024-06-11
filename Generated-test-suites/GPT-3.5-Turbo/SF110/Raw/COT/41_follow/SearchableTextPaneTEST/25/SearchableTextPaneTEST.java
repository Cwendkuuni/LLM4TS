import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.setText("This is a test text.");
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertEquals(1, lineResults.size());
        assertEquals(1, lineResults.get(0).getWordResults().size());
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.highlight("test", 0);
        searchableTextPane.removeHighlights();
        assertNotNull(searchableTextPane.getHighlighter());
        assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch() {
        int pos = searchableTextPane.search("test");
        assertEquals(10, pos);
    }

    @Test
    public void testSearchWithStartPosition() {
        int pos = searchableTextPane.search("is", 3);
        assertEquals(5, pos);
    }

    @Test
    public void testSearchWithNonexistentTerm() {
        int pos = searchableTextPane.search("nonexistent");
        assertEquals(-1, pos);
    }

    @Test
    public void testConvertWords2Lines() {
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertEquals(1, lineResults.size());
        assertEquals(1, lineResults.get(0).getWordResults().size());
        assertEquals(0, lineResults.get(0).getLine());
        assertEquals(10, lineResults.get(0).getLineStart());
        assertEquals(14, lineResults.get(0).getLineEnd());
    }

    @Test
    public void testUpdateWordResult() {
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertEquals(1, lineResults.size());
        assertEquals(1, lineResults.get(0).getWordResults().size());
        assertEquals(0, lineResults.get(0).getLine());
        assertEquals(10, lineResults.get(0).getLineStart());
        assertEquals(14, lineResults.get(0).getLineEnd());
        assertEquals(1, lineResults.get(0).getWordResults().get(0).getLineNumber());
        assertEquals(10, lineResults.get(0).getWordResults().get(0).getLineOffset());
    }
}