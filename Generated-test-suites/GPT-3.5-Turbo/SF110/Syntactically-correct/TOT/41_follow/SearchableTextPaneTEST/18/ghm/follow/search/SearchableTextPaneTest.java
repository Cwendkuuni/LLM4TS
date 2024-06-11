import org.junit.*;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        
        assertEquals(font, textPane.getFont());
        assertEquals(tabSize, textPane.getTabSize());
    }
    
    // Test case for getScrollableTracksViewportWidth() method with parent and UI dimensions
    @Test
    public void testGetScrollableTracksViewportWidthWithDimensions() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        textPane.setPreferredSize(new Dimension(200, 100));
        textPane.setSize(new Dimension(150, 100));
        
        boolean result = textPane.getScrollableTracksViewportWidth();
        
        assertTrue(result);
    }
    
    // Test case for getScrollableTracksViewportWidth() method without parent
    @Test
    public void testGetScrollableTracksViewportWidthWithoutParent() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        textPane.setPreferredSize(new Dimension(200, 100));
        textPane.setSize(new Dimension(300, 100));
        
        boolean result = textPane.getScrollableTracksViewportWidth();
        
        assertFalse(result);
    }
    
    // Test case for highlight() method with valid term and flags
    @Test
    public void testHighlightWithValidTermAndFlags() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        
        String term = "test";
        int flags = 1;
        
        List<LineResult> lineResults = textPane.highlight(term, flags);
        
        assertNotNull(lineResults);
    }
    
    // Test case for highlight() method with null term
    @Test
    public void testHighlightWithNullTerm() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        
        int flags = 1;
        
        List<LineResult> lineResults = textPane.highlight(null, flags);
        
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }
    
    // Test case for highlight() method with empty term
    @Test
    public void testHighlightWithEmptyTerm() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        
        String term = "";
        int flags = 1;
        
        List<LineResult> lineResults = textPane.highlight(term, flags);
        
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }
    
    // Test case for addHighlight() method with valid start and length
    @Test
    public void testAddHighlightWithValidStartAndLength() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        
        int start = 0;
        int length = 5;
        
        try {
            textPane.addHighlight(start, length);
        } catch(BadLocationException e) {
            fail("Exception should not be thrown");
        }
    }
    
    // Test case for removeHighlights() method
    @Test
    public void testRemoveHighlights() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        
        // Adding highlights
        int start = 0;
        int length = 5;
        try {
            textPane.addHighlight(start, length);
        } catch(BadLocationException e) {
            fail("Exception should not be thrown");
        }
        
        // Removing highlights
        textPane.removeHighlights();
        
        // Checking if highlights are removed
        Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
        
        assertEquals(0, highlights.length);
    }
    
    // Test case for search() method with matching term
    @Test
    public void testSearchWithMatchingTerm() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        
        String term = "test";
        
        int result = textPane.search(term);
        
        assertEquals(0, result);
        assertEquals(term, textPane.lastSearchTerm);
        assertEquals(term.length(), textPane.lastSearchPos);
    }
    
    // Test case for search() method with non-matching term
    @Test
    public void testSearchWithNonMatchingTerm() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        
        String term = "nonmatching";
        
        int result = textPane.search(term);
        
        assertEquals(-1, result);
        assertNull(textPane.lastSearchTerm);
        assertEquals(-1, textPane.lastSearchPos);
    }
    
    // Test case for search() method with empty term
    @Test
    public void testSearchWithEmptyTerm() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        
        String term = "";
        
        int result = textPane.search(term);
        
        assertEquals(-1, result);
        assertNull(textPane.lastSearchTerm);
        assertEquals(0, textPane.lastSearchPos);
    }
    
    // Test case for search() method with null term
    @Test
    public void testSearchWithNullTerm() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        
        int result = textPane.search(null);
        
        assertEquals(-1, result);
        assertNull(textPane.lastSearchTerm);
        assertEquals(-1, textPane.lastSearchPos);
    }
    
    // Test case for search() method with matching term and startPos
    @Test
    public void testSearchWithMatchingTermAndStartPos() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        textPane.setText("This is a test");
        
        String term = "test";
        int startPos = 5;
        
        int result = textPane.search(term, startPos);
        
        assertEquals(10, result);
        assertEquals(term, textPane.lastSearchTerm);
        assertEquals(result + term.length(), textPane.lastSearchPos);
    }
    
    // Test case for search() method with non-matching term and startPos
    @Test
    public void testSearchWithNonMatchingTermAndStartPos() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        textPane.setText("This is a test");
        
        String term = "nonmatching";
        int startPos = 0;
        
        int result = textPane.search(term, startPos);
        
        assertEquals(-1, result);
        assertNull(textPane.lastSearchTerm);
        assertEquals(-1, textPane.lastSearchPos);
    }
    
    // Test case for search() method with empty term and startPos
    @Test
    public void testSearchWithEmptyTermAndStartPos() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        textPane.setText("This is a test");
        
        String term = "";
        int startPos = 6;
        
        int result = textPane.search(term, startPos);
        
        assertEquals(-1, result);
        assertNull(textPane.lastSearchTerm);
        assertEquals(0, textPane.lastSearchPos);
    }
    
    // Test case for search() method with null term and startPos
    @Test
    public void testSearchWithNullTermAndStartPos() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        textPane.setText("This is a test");
        
        int startPos = 0;
        
        int result = textPane.search(null, startPos);
        
        assertEquals(-1, result);
        assertNull(textPane.lastSearchTerm);
        assertEquals(-1, textPane.lastSearchPos);
    }
    
    // Test case for convertWords2Lines() method with valid words
    @Test
    public void testConvertWords2LinesWithValidWords() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        textPane.setText("This is a test");
        
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(8, 12));
        words.add(new WordResult(14, 18));
        
        try {
            List<LineResult> lineResults = textPane.convertWords2Lines(words);
            
            assertNotNull(lineResults);
            assertEquals(1, lineResults.size());
            assertEquals(1, lineResults.get(0).getLine());
            assertEquals(0, lineResults.get(0).getLineStartOffset());
            assertEquals(15, lineResults.get(0).getLineEndOffset());
            assertEquals(1, lineResults.get(0).getWordResults().size());
            assertEquals(8, lineResults.get(0).getWordResults().get(0).start);
            assertEquals(12, lineResults.get(0).getWordResults().get(0).end);
        } catch(BadLocationException e) {
            fail("Exception should not be thrown");
        }
    }
    
    // Test case for updateWordResult() method with valid wordResult and lineResult
    @Test
    public void testUpdateWordResultWithValidWordResultAndLineResult() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane textPane = new SearchableTextPane(font, tabSize);
        textPane.setText("This is a test");
        
        WordResult wordResult = new WordResult(8, 12);
        LineResult lineResult = new LineResult(1, 0, 15);
        
        try {
            textPane.updateWordResult(wordResult, lineResult);
            
            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(1, wordResult.parent.lineNumber);
            assertEquals(1, wordResult.getLineOffset());
        } catch(BadLocationException e) {
            fail("Exception should not be thrown");
        }
    }
}