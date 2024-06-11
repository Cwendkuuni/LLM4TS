import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;
import java.util.List;
import javax.swing.text.BadLocationException;

public class SearchableTextPaneTest {

    private SearchableTextPane textPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        textPane = new SearchableTextPane(font, 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Test when parent is null
        assertTrue(textPane.getScrollableTracksViewportWidth());
        
        // Test when parent size is smaller than viewport width
        textPane.setSize(300, 300);
        assertFalse(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() throws BadLocationException {
        // Test highlighting with valid term
        List<LineResult> lineResults = textPane.highlight("test", 0);
        assertEquals(1, lineResults.size());
        
        // Test highlighting with empty term
        lineResults = textPane.highlight("", 0);
        assertEquals(0, lineResults.size());
        
        // Test highlighting with null term
        lineResults = textPane.highlight(null, 0);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        // Test adding highlight with valid start and length
        textPane.addHighlight(5, 3);
        
        // Test adding highlight with illegal arguments
        try {
            textPane.addHighlight(-1, 10);
            fail("Expected BadLocationException");
        } catch (BadLocationException e) {
            // Test passed
        }
    }

    @Test
    public void testRemoveHighlights() {
        // Test removing highlights
        textPane.addHighlight(5, 3);
        textPane.removeHighlights();
    }

    @Test
    public void testSearch() {
        // Test searching for term that exists in text
        assertEquals(0, textPane.search("Test"));
        
        // Test searching for term that does not exist in text
        assertEquals(-1, textPane.search("Hello"));
        
        // Test searching with empty term
        assertEquals(-1, textPane.search(""));
        
        // Test searching with null term
        assertEquals(-1, textPane.search(null));
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        // Test converting WordResults to LineResults
        textPane.highlight("test", 0);
        List<WordResult> wordResults = new ArrayList<WordResult>();
        LineResult lineResult = textPane.convertWords2Lines(wordResults).get(0);
        assertNotNull(lineResult);
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        // Test updating WordResult in LineResult
        WordResult wordResult = new WordResult();
        LineResult lineResult = new LineResult(1, 0, 5);
        textPane.updateWordResult(wordResult, lineResult);
        assertEquals(1, wordResult.parent.lineNumber);
    }

}