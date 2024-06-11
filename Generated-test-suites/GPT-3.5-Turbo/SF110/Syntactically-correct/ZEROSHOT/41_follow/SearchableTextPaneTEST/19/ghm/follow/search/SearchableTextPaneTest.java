import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;
import javax.swing.text.*;
import javax.swing.text.DefaultHighlighter.*;

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
    public void testHighlight() {
        String term = "test";
        int flags = 1;
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
        assertNotNull(lineResults);
    }
    
    @Test
    public void testAddHighlight() {
        int start = 0;
        int length = 5;
        try {
            searchableTextPane.addHighlight(start, length);
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
        Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertEquals(1, highlights.length);
    }
    
    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
        Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertEquals(0, highlights.length);
    }
    
    @Test
    public void testSearchReturnsPosition() {
        String term = "test";
        int startPos = 0;
        int position = searchableTextPane.search(term, startPos);
        assertEquals(-1, position);
    }
    
    @Test
    public void testSearchSetsLastSearchTerm() {
        String term = "test";
        int startPos = 0;
        searchableTextPane.search(term, startPos);
        assertEquals(term, searchableTextPane.getLastSearchTerm());
    }
    
    @Test
    public void testSearchSetsLastSearchPos() {
        String term = "test";
        int startPos = 0;
        searchableTextPane.search(term, startPos);
        assertEquals(-1, searchableTextPane.getLastSearchPos());
    }
    
    @Test
    public void testSearchWithLastSearchTerm() {
        searchableTextPane.setLastSearchTerm("test");
        searchableTextPane.setLastSearchPos(0);
        String term = "test";
        int position = searchableTextPane.search(term);
        assertEquals(-1, position);
    }
    
    @Test
    public void testSearchWithDifferentTerm() {
        searchableTextPane.setLastSearchTerm("test");
        searchableTextPane.setLastSearchPos(0);
        String term = "hello";
        int position = searchableTextPane.search(term);
        assertEquals(-1, position);
    }
    
    @Test
    public void testSearchWithValidTerm() {
        searchableTextPane.setLastSearchTerm(null);
        searchableTextPane.setLastSearchPos(0);
        String term = "test";
        int position = searchableTextPane.search(term);
        assertEquals(-1, position);
    }
    
    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = new ArrayList<>();
        WordResult wordResult = new WordResult("test", 0, 4);
        words.add(wordResult);
        try {
            List<LineResult> lines = searchableTextPane.convertWords2Lines(words);
            assertNotNull(lines);
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
    
    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult("test", 0, 4);
        LineResult lineResult = new LineResult(0, 0, 10);
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
            assertEquals(0, wordResult.getLineOffset());
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
}