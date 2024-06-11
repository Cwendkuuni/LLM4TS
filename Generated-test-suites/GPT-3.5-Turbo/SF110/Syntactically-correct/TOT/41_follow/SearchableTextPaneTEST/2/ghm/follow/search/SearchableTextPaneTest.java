import org.junit.Before;
import org.junit.Test;
import javax.swing.text.BadLocationException;
import java.awt.Font;
import java.util.List;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;
    
    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }
    
    @Test
    public void testHighlight_WithValidTermAndFlags_ShouldReturnLineResults() {
        List<LineResult> lineResults = searchableTextPane.highlight("Hello", SearchEngine.CASE_SENSITIVE);
        
        assertNotNull(lineResults);
        assertEquals(2, lineResults.size());
    }
    
    @Test
    public void testHighlight_WithEmptyTerm_ShouldReturnEmptyLineResults() {
        List<LineResult> lineResults = searchableTextPane.highlight("", SearchEngine.CASE_INSENSITIVE);
        
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_WithNullTerm_ShouldReturnEmptyLineResults() {
        List<LineResult> lineResults = searchableTextPane.highlight(null, SearchEngine.CASE_SENSITIVE);

        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testAddHighlight_WithValidStartAndLength_ShouldHighlightTexts() throws BadLocationException {
        searchableTextPane.addHighlight(10, 5);
        
        DefaultHighlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        
        assertNotNull(highlights);
        assertEquals(1, highlights.length);
        assertEquals(10, highlights[0].getStartOffset());
        assertEquals(15, highlights[0].getEndOffset());
    }
    
    @Test(expected = BadLocationException.class)
    public void testAddHighlight_WithInvalidStartAndLength_ShouldThrowBadLocationException() throws BadLocationException {
        searchableTextPane.addHighlight(-1, 5);
    }
    
    @Test
    public void testRemoveHighlights_ShouldRemoveAllHighlights() throws BadLocationException {
        searchableTextPane.addHighlight(10, 5);
        
        DefaultHighlighter.Highlight[] highlightsBeforeRemove = searchableTextPane.getHighlighter().getHighlights();
        
        assertNotNull(highlightsBeforeRemove);
        assertEquals(1, highlightsBeforeRemove.length);
        
        searchableTextPane.removeHighlights();
        
        DefaultHighlighter.Highlight[] highlightsAfterRemove = searchableTextPane.getHighlighter().getHighlights();
        
        assertNotNull(highlightsAfterRemove);
        assertEquals(0, highlightsAfterRemove.length);
    }
    
    @Test
    public void testSearch_WithValidTermAndExistingLastSearchTerm_ShouldReturnCorrectPosition() {
        searchableTextPane.search("Hello");
        int result = searchableTextPane.search("World");
        
        assertEquals(6, result);
    }
    
    @Test
    public void testSearch_WithValidTermAndNewLastSearchTerm_ShouldReturnCorrectPosition() {
        int result = searchableTextPane.search("Hello");
        
        assertEquals(0, result);
    }
    
    @Test
    public void testSearch_WithEmptyTerm_ShouldResetLastSearchTermAndReturnMinusOne() {
        searchableTextPane.search("Hello");
        int result = searchableTextPane.search("");
        
        assertEquals(-1, result);
        assertNull(searchableTextPane.getLastSearchTerm());
    }
    
    @Test
    public void testSearch_WithNullTerm_ShouldResetLastSearchTermAndReturnMinusOne() {
        searchableTextPane.search("Hello");
        int result = searchableTextPane.search(null);
        
        assertEquals(-1, result);
        assertNull(searchableTextPane.getLastSearchTerm());
    }
    
    @Test
    public void testSearch_WithInvalidStartPos_ShouldReturnMinusOne() {
        int result = searchableTextPane.search("Hello", -1);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testConvertWords2Lines_WithValidWords_ShouldReturnConvertedLines() throws BadLocationException {
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(MockDataGenerator.generateWords());
        
        assertNotNull(lineResults);
        assertEquals(2, lineResults.size());
        assertEquals(1, lineResults.get(0).getLineNumber());
        assertEquals(2, lineResults.get(1).getLineNumber());
    }
    
    @Test(expected = BadLocationException.class)
    public void testConvertWords2Lines_WithInvalidWords_ShouldThrowBadLocationException() throws BadLocationException {
        searchableTextPane.convertWords2Lines(MockDataGenerator.generateInvalidWords());
    }
    
    private static class MockDataGenerator {
        
        public static List<WordResult> generateWords() {
            List<WordResult> words = new ArrayList<>();
            
            WordResult word1 = new WordResult(0, 5);
            WordResult word2 = new WordResult(10, 15);
            
            words.add(word1);
            words.add(word2);
            
            return words;
        }
        
        public static List<WordResult> generateInvalidWords() {
            return null; // Return null to trigger BadLocationException
        }
    }
}