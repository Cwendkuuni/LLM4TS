import org.junit.*;
import static org.junit.Assert.*;
import java.awt.Font;
import java.util.List;
import javax.swing.text.BadLocationException;

public class SearchableTextPaneTest {

    /**
     * Test the SearchableTextPane constructor
     */
    @Test
    public void testConstructor() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);

        assertEquals(font, textPane.getFont());
        assertEquals(4, textPane.getTabSize());
    }

    /**
     * Test the getScrollableTracksViewportWidth method
     */
    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);

        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    /**
     * Test the highlight method
     */
    @Test
    public void testHighlight() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        String term = "example";
        int flags = 0;

        List<LineResult> lineResults = textPane.highlight(term, flags);

        assertEquals(0, lineResults.size());
    }

    /**
     * Test the addHighlight method
     */
    @Test
    public void testAddHighlight() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);

        int start = 0;
        int length = 5;
        textPane.addHighlight(start, length);

        assertEquals(1, textPane.getHighlighter().getHighlights().length);
    }

    /**
     * Test the removeHighlights method
     */
    @Test
    public void testRemoveHighlights() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        int start = 0;
        int length = 5;
        try {
            textPane.addHighlight(start, length);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        textPane.removeHighlights();

        assertEquals(0, textPane.getHighlighter().getHighlights().length);
    }

    /**
     * Test the search method with matching term
     */
    @Test
    public void testSearchMatching() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        String term = "example";
        
        int result = textPane.search(term);

        assertEquals(0, result);
        assertEquals(term, textPane.lastSearchTerm);
        assertEquals(0, textPane.lastSearchPos);
    }

    /**
     * Test the search method with non-matching term
     */
    @Test
    public void testSearchNonMatching() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        String term = "foobar";
        
        int result = textPane.search(term);

        assertEquals(-1, result);
        assertNull(textPane.lastSearchTerm);
        assertEquals(-1, textPane.lastSearchPos);
    }

    /**
     * Test the search method with matching term and start position
     */
    @Test
    public void testSearchWithStartPosition() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        String term = "example";
        int startPos = 5;

        int result = textPane.search(term, startPos);

        assertEquals(0, result);
        assertEquals(term, textPane.lastSearchTerm);
        assertEquals(0, textPane.lastSearchPos);
    }

    /**
     * Test the search method with non-matching term and start position
     */
    @Test
    public void testSearchWithStartPositionNonMatching() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        String term = "foobar";
        int startPos = 5;

        int result = textPane.search(term, startPos);

        assertEquals(-1, result);
        assertNull(textPane.lastSearchTerm);
        assertEquals(-1, textPane.lastSearchPos);
    }

    /**
     * Test the convertWords2Lines method
     */
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);

        List<LineResult> lineResults = textPane.convertWords2Lines(null);

        assertEquals(0, lineResults.size());
    }

    /**
     * Test the updateWordResult method
     */
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(0, 0, 10);

        textPane.updateWordResult(wordResult, lineResult);

        assertEquals(1, lineResult.getWordResults().size());
    }

}