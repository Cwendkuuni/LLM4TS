import org.junit.Test;
import java.awt.Font;
import java.util.List;
import javax.swing.text.BadLocationException;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertNotNull(lineResults);
    }
    
    @Test
    public void testAddHighlight() throws BadLocationException {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.addHighlight(10, 5);
        // assertion for added highlight
    }
    
    @Test
    public void testRemoveHighlights() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.removeHighlights();
        // assertion for removed highlights
    }
    
    @Test
    public void testSearch() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        int position = searchableTextPane.search("test");
        assertEquals(-1, position);
    }
    
    @Test
    public void testSearchWithStartPosition() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        int position = searchableTextPane.search("test", 5);
        // assertion
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(new ArrayList<WordResult>());
        assertNotNull(lineResults);
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        WordResult wordResult = new WordResult("", 0, 5);
        LineResult lineResult = new LineResult(1, 0, 10);
        searchableTextPane.updateWordResult(wordResult, lineResult);
        // assertion for updated word result
    }
}