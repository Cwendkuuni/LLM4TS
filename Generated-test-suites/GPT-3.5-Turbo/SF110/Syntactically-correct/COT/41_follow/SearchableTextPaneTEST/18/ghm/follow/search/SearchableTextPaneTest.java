
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        boolean scrollableTracksViewportWidth = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(scrollableTracksViewportWidth);
    }

    @Test
    public void testHighlight() {
        String term = "test";
        int flags = 0;
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() {
        int start = 0;
        int length = 5;
        try {
            searchableTextPane.addHighlight(start, length);
        } catch (Exception e) {
            Assert.fail("Should not throw any exception");
        }
    }

    @Test
    public void testRemoveHighlights() {
        try {
            searchableTextPane.removeHighlights();
        } catch (Exception e) {
            Assert.fail("Should not throw any exception");
        }
    }

    @Test
    public void testSearch() {
        String term = "test";
        int expectedPos = -1;
        int pos = searchableTextPane.search(term);
        assertEquals(expectedPos, pos);
    }
    
    @Test
    public void testSearchWithStartPosition() {
        String term = "test";
        int startPos = 5;
        int expectedPos = -1;
        int pos = searchableTextPane.search(term, startPos);
        assertEquals(expectedPos, pos);
    }

    @Test
    public void testConvertWords2Lines() {
        WordResult wordResult = new WordResult(0, 5);
        List<LineResult> lines = null;
        try {
            lines = searchableTextPane.convertWords2Lines(List.of(wordResult));
        } catch (Exception e) {
            Assert.fail("Should not throw any exception");
        }
        assertEquals(1, lines.size());
        LineResult lineResult = lines.get(0);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(0, lineResult.getStartOffset());
        assertEquals(5, lineResult.getEndOffset());
        assertEquals(1, lineResult.getWordResults().size());
        WordResult result = lineResult.getWordResults().get(0);
        assertEquals(0, result.start);
        assertEquals(5, result.end);
    }

    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(1, 0, 5);
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
        } catch (Exception e) {
            Assert.fail("Should not throw any exception");
        }
        assertEquals(1, lineResult.getWordResults().size());
        WordResult result = lineResult.getWordResults().get(0);
        assertEquals(0, result.start);
        assertEquals(5, result.end);
        assertEquals(1, result.parent.lineNumber);
        assertEquals(0, result.getLineOffset());
    }
}

