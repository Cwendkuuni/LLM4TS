
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.awt.Font;
import java.util.List;
import java.util.ArrayList;
import javax.swing.text.BadLocationException; 

public class SearchableTextPaneTest {
    
    private Font font;
    private int tabSize;
    private SearchableTextPane searchableTextPane;
    
    @Before
    public void setUp() {
        font = new Font("Arial", Font.PLAIN, 12);
        tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        Assert.assertTrue(result);
    }
    
    @Test
    public void testHighlight() {
        String term = "test";
        int flags = 0;
        
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
        Assert.assertEquals(new ArrayList<LineResult>(), lineResults);
        
        // Add test case with non-empty text and search term
        // Add test case with different search flags
        // Add test case with text and search results
    }
    
    @Test
    public void testAddHighlight() {
        try {
            searchableTextPane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            Assert.fail("Exception not expected");
        }
        
        // Add test case with invalid start position
        // Add test case with invalid length
    }
    
    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
        
        // Add test case where highlights are removed and no highlights existed before
        // Add test case where highlights are removed and highlights already existed before
    }
    
    @Test
    public void testSearch() {
        String term = "test";
        
        int result = searchableTextPane.search(term);
        Assert.assertEquals(-1, result);
        
        // Add test case where term equals lastSearchTerm
        // Add test case with same term but different lastSearchPos
        // Add test case with different term and different lastSearchPos
    }
    
    @Test
    public void testSearchWithStartPos() {
        String term = "test";
        int startPos = 10;
        
        int result = searchableTextPane.search(term, startPos);
        Assert.assertEquals(-1, result);
        
        // Add test case where startPos is within valid range
        // Add test case where startPos is larger than the text length
    }
    
    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = new ArrayList<WordResult>();
        
        try {
            List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
            Assert.assertEquals(new ArrayList<LineResult>(), lineResults);
        } catch (BadLocationException e) {
            Assert.fail("Exception not expected");
        }
        
        // Add test case with valid word results
        // Add test case with word results spanning multiple lines
    }
    
    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(1, 0, 10);
        
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
        } catch (BadLocationException e) {
            Assert.fail("Exception not expected");
        }
        
        // Add test case where wordResult and lineResult are null
        // Add test case where lineResult is null
    }
}
