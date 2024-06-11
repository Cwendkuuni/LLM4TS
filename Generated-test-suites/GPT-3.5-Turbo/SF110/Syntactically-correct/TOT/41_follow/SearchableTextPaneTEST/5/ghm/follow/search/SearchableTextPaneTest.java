import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {
    // Test for the constructor
    @Test
    public void testConstructor() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane stp = new SearchableTextPane(font, tabSize);
        
        assertEquals(font, stp.getFont());
        assertEquals(tabSize, stp.getTabSize());
        assertEquals(-1, stp.lastSearchPos);
        assertNotNull(stp.wordPainter);
    }
    
    // Test for getScrollableTracksViewportWidth method
    @Test
    public void testGetScrollableTracksViewportWidth() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane stp1 = new SearchableTextPane(font, 4);
        SearchableTextPane stp2 = new SearchableTextPane(font, 4);
        JScrollPane scrollPane = new JScrollPane(stp1);
        
        assertEquals(true, stp1.getScrollableTracksViewportWidth());
        
        stp1.setPreferredSize(new Dimension(500, 500));
        assertEquals(true, stp1.getScrollableTracksViewportWidth());
        
        scrollPane.setPreferredSize(new Dimension(300, 500));
        stp2.setPreferredSize(new Dimension(500, 500));
        scrollPane.setViewportView(stp2);
        assertEquals(true, stp2.getScrollableTracksViewportWidth());
        
        stp2.setPreferredSize(new Dimension(700, 500));
        assertEquals(false, stp2.getScrollableTracksViewportWidth());
    }
    
    // Test for highlight method
    @Test
    public void testHighlight() throws BadLocationException {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane stp = new SearchableTextPane(font, 4);
        
        // Test when term is null
        List<LineResult> lineResults = stp.highlight(null, 0);
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
        
        // Test when term is empty
        lineResults = stp.highlight("", 0);
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
        
        // Test when term is present in text
        String text = "This is a test text. The term 'test' should be highlighted.";
        stp.setText(text);
        
        lineResults = stp.highlight("test", 0);
        assertNotNull(lineResults);
        assertEquals(1, lineResults.size());
        
        // Test when term is not present in text
        lineResults = stp.highlight("example", 0);
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
    }
    
    // Test for addHighlight method
    @Test
    public void testAddHighlight() throws BadLocationException {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane stp = new SearchableTextPane(font, 4);
        
        stp.setText("This is a test text.");
        
        // Test when start and length are valid
        stp.addHighlight(2, 4);
        Highlighter.Highlight[] highlights = stp.getHighlighter().getHighlights();
        assertEquals(1, highlights.length);
        assertEquals(2, highlights[0].getStartOffset());
        assertEquals(6, highlights[0].getEndOffset());
        
        // Test when start and length are invalid
        try {
            stp.addHighlight(-1, 8);
            fail("Expected BadLocationException to be thrown");
        } catch (BadLocationException e) {
            assertTrue(e.getMessage().contains("BadLocationException"));
        }
    }
    
    // Test for removeHighlights method
    @Test
    public void testRemoveHighlights() throws BadLocationException {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane stp = new SearchableTextPane(font, 4);
        
        stp.setText("This is a test text.");
        stp.addHighlight(2, 4);
        
        stp.removeHighlights();
        
        Highlighter.Highlight[] highlights = stp.getHighlighter().getHighlights();
        assertEquals(0, highlights.length);
    }
    
    // Test for search method
    @Test
    public void testSearch() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane stp = new SearchableTextPane(font, 4);
        
        assertNull(stp.lastSearchTerm);
        
        // Test when term is null
        int result = stp.search(null);
        assertEquals(-1, result);
        assertNull(stp.lastSearchTerm);
        
        // Test when term is empty
        result = stp.search("");
        assertEquals(-1, result);
        assertNull(stp.lastSearchTerm);
        
        // Test when term is present without previous search
        stp.setText("This is a test text.");
        
        result = stp.search("test");
        assertEquals(10, result);
        assertEquals("test", stp.lastSearchTerm);
        
        // Test when term is present with previous search
        result = stp.search("is");
        assertEquals(2, result);
        assertEquals("is", stp.lastSearchTerm);
        
        // Test when term is not present
        result = stp.search("example");
        assertEquals(-1, result);
        assertEquals("example", stp.lastSearchTerm);
    }
    
    // Test for search method with startPos
    @Test
    public void testSearchWithStartPos() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane stp = new SearchableTextPane(font, 4);
        
        assertNull(stp.lastSearchTerm);
        
        // Test when term is null and startPos is 0
        int result = stp.search(null, 0);
        assertEquals(-1, result);
        assertNull(stp.lastSearchTerm);
        
        // Test when term is empty and startPos is 0
        result = stp.search("", 0);
        assertEquals(-1, result);
        assertNull(stp.lastSearchTerm);
        
        // Test when term is present without previous search and startPos is 0
        stp.setText("This is a test text.");
        
        result = stp.search("test", 0);
        assertEquals(10, result);
        assertEquals("test", stp.lastSearchTerm);
        
        // Test when term is present with previous search and startPos is 10
        result = stp.search("is", 10);
        assertEquals(-1, result);
        assertEquals("is", stp.lastSearchTerm);
        
        // Test when term is not present and startPos is 0
        result = stp.search("example", 0);
        assertEquals(-1, result);
        assertEquals("example", stp.lastSearchTerm);
        
        // Test when term is present with previous search and startPos is 0
        result = stp.search("is", 0);
        assertEquals(2, result);
        assertEquals("is", stp.lastSearchTerm);
    }
    
    // Test for convertWords2Lines method
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane stp = new SearchableTextPane(font, 4);
        stp.setText("This is a test text.");
        
        // Test when words have multiple lines
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(2, 6, null));
        words.add(new WordResult(10, 14, null));
        words.add(new WordResult(16, 20, null));
        
        List<LineResult> lineResults = stp.convertWords2Lines(words);
        
        assertEquals(3, lineResults.size());
        assertEquals(0, lineResults.get(0).getLine());
        assertEquals(0, lineResults.get(0).getLineStartOffset());
        assertEquals(19, lineResults.get(0).getLineEndOffset());
        
        // Test when words have same line
        words.clear();
        words.add(new WordResult(2, 6, null));
        words.add(new WordResult(8, 12, null));
        words.add(new WordResult(16, 20, null));
        
        lineResults = stp.convertWords2Lines(words);
        
        assertEquals(1, lineResults.size());
        assertEquals(0, lineResults.get(0).getLine());
        assertEquals(0, lineResults.get(0).getLineStartOffset());
        assertEquals(19, lineResults.get(0).getLineEndOffset());
        
        // Test when words have single line
        words.clear();
        words.add(new WordResult(2, 6, null));
        
        lineResults = stp.convertWords2Lines(words);
        
        assertEquals(1, lineResults.size());
        assertEquals(0, lineResults.get(0).getLine());
        assertEquals(0, lineResults.get(0).getLineStartOffset());
        assertEquals(19, lineResults.get(0).getLineEndOffset());
    }
    
    // Test for updateWordResult method
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane stp = new SearchableTextPane(font, 4);
        stp.setText("This is a test text.");
        
        LineResult lineResult = new LineResult(0, 0, 19);
        WordResult wordResult = new WordResult(2, 6, null);
        
        stp.updateWordResult(wordResult, lineResult);
        
        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(1, wordResult.parent.lineNumber);
        assertEquals(0, wordResult.lineOffset);
    }
    
    // Additional test cases can be added to test all methods and edge cases
    
}