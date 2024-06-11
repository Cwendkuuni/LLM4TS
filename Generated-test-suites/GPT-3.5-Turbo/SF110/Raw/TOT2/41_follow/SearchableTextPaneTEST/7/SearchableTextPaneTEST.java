import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;
import java.util.List;

public class SearchableTextPaneTest {
    
    private SearchableTextPane searchableTextPane;
    private Font font;
    
    @Before
    public void setUp() {
        font = new Font("Arial", Font.PLAIN, 12);
        searchableTextPane = new SearchableTextPane(font, 4);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
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
            assertTrue(true);
        } catch (BadLocationException e) {
            fail("Failed to add highlight");
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
        assertTrue(true);
    }
    
    @Test
    public void testSearch() {
        int result = searchableTextPane.search("test");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchFromStartPos() {
        int result = searchableTextPane.search("test", 5);
        assertEquals(-1, result);
    }
    
    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 3));
        try {
            List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
            assertNotNull(lineResults);
        } catch (BadLocationException e) {
            fail("Failed to convert words to lines");
        }
    }
    
    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 3);
        LineResult lineResult = new LineResult(1, 0, 5);
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
            assertEquals(1, wordResult.parent.lineNumber);
            assertEquals(0, wordResult.lineOffset);
        } catch (BadLocationException e) {
            fail("Failed to update word result");
        }
    }
}