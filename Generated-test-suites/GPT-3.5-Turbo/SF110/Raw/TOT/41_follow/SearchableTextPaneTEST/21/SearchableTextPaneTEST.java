import org.junit.Assert;
import org.junit.Test;
import java.awt.Font;
import java.util.List;
import javax.swing.text.BadLocationException;
import java.awt.Color;

public class SearchableTextPaneTest {

    @Test
    public void testHighlightMethod() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        String term = "Lorem";
        int flags = 0;
        
        SearchableTextPane searchableTextPane = new SearchableTextPane(font, tabSize);
        
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
        
        Assert.assertEquals(lineResults.size(), 0); // No matches found
        
        // Test with a matching term
        term = "ipsum";
        lineResults = searchableTextPane.highlight(term, flags);
        
        Assert.assertEquals(lineResults.size(), 1); // One matching line found
        Assert.assertEquals(lineResults.get(0).getWordResults().size(), 1); // One matching word found
        
        // Test with multiple matching terms
        term = "e";
        lineResults = searchableTextPane.highlight(term, flags);
        
        Assert.assertEquals(lineResults.size(), 4); // Four matching lines found
        Assert.assertEquals(lineResults.get(0).getWordResults().size(), 7); // Seven matching words found
        
        // Test with flags
        flags = Pattern.CASE_INSENSITIVE;
        term = "i";
        lineResults = searchableTextPane.highlight(term, flags);
        
        Assert.assertEquals(lineResults.size(), 2); // Two matching lines found (case-insensitive)
        Assert.assertEquals(lineResults.get(0).getWordResults().size(), 8); // Eight matching words found (case-insensitive)
    }

    @Test
    public void testRemoveHighlightsMethod() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane searchableTextPane = new SearchableTextPane(font, tabSize);
        
        String term = "ipsum";
        int flags = 0;
        searchableTextPane.highlight(term, flags); // Highlight some text
        
        searchableTextPane.removeHighlights(); // Remove the highlights
        
        // Assertion to check if highlights are removed
        Assert.assertEquals(searchableTextPane.getHighlighter().getHighlights().length, 0);
    }

    @Test
    public void testSearchMethod() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane searchableTextPane = new SearchableTextPane(font, tabSize);
        
        // Test with a non-existent term
        String term = "nonexistent";
        int result = searchableTextPane.search(term);
        
        Assert.assertEquals(result, -1); // Term not found
        
        // Test with an empty term
        term = "";
        result = searchableTextPane.search(term);
        
        Assert.assertEquals(result, 0); // Empty term returns the start position
        
        // Test with a valid term
        term = "Lorem";
        result = searchableTextPane.search(term);
        
        Assert.assertEquals(result, 0); // First occurrence of the term is at position 0
        
        // Test with a term that occurs multiple times
        term = "ips";
        result = searchableTextPane.search(term);
        
        Assert.assertEquals(result, 6); // Second occurrence of the term is at position 6
    }
    
    @Test
    public void testConvertWords2LinesMethod() throws BadLocationException {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        
        SearchableTextPane searchableTextPane = new SearchableTextPane(font, tabSize);
        
        String term = "ipsum";
        int flags = 0;
        searchableTextPane.highlight(term, flags); // Highlight some text
        
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(searchableTextPane.getHighlightWordResults());
        
        Assert.assertEquals(lineResults.size(), 1); // One line result
        
        LineResult lineResult = lineResults.get(0);
        
        Assert.assertEquals(lineResult.getLine(), 0); // Line number is 0
        Assert.assertEquals(lineResult.getLineStart(), 0); // Line start offset is 0
        Assert.assertEquals(lineResult.getLineEnd(), 12); // Line end offset is 12
        Assert.assertEquals(lineResult.getWordResults().size(), 1); // One word result
        
        WordResult wordResult = lineResult.getWordResults().get(0);
        
        Assert.assertEquals(wordResult.getStart(), 6); // Word start position is at 6
        Assert.assertEquals(wordResult.getEnd(), 11); // Word end position is at 11
        
        // Test with multiple lines and word results
        term = "e";
        searchableTextPane.highlight(term, flags); // Highlight some text
        
        lineResults = searchableTextPane.convertWords2Lines(searchableTextPane.getHighlightWordResults());
        
        Assert.assertEquals(lineResults.size(), 4); // Four line results
        
        lineResult = lineResults.get(0);
        
        Assert.assertEquals(lineResult.getLine(), 0); // Line number is 0
        Assert.assertEquals(lineResult.getLineStart(), 0); // Line start offset is 0
        Assert.assertEquals(lineResult.getLineEnd(), 12); // Line end offset is 12
        Assert.assertEquals(lineResult.getWordResults().size(), 2); // Two word results
        
        wordResult = lineResult.getWordResults().get(0);
        
        Assert.assertEquals(wordResult.getStart(), 1); // Word start position is at 1
        Assert.assertEquals(wordResult.getEnd(), 2); // Word end position is at 2
        
        // Test with a line result with no word results
        term = "nonexistent";
        searchableTextPane.highlight(term, flags); // Highlight some text
        
        lineResults = searchableTextPane.convertWords2Lines(searchableTextPane.getHighlightWordResults());
        
        Assert.assertEquals(lineResults.size(), 0); // No line results
    }

}