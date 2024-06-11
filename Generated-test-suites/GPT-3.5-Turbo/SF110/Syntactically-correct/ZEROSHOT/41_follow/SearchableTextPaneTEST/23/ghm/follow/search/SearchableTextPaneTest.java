package ghm.follow.search;

import org.junit.Assert;
import org.junit.Test;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        
        // Test when parent is null
        boolean result1 = searchableTextPane.getScrollableTracksViewportWidth();
        Assert.assertTrue(result1);
        
        // Test when parent width is smaller than text width
        searchableTextPane.setSize(new Dimension(100, 100));
        boolean result2 = searchableTextPane.getScrollableTracksViewportWidth();
        Assert.assertTrue(result2);
        
        // Test when parent width is greater than text width
        searchableTextPane.setSize(new Dimension(250, 100));
        boolean result3 = searchableTextPane.getScrollableTracksViewportWidth();
        Assert.assertFalse(result3);
    }
    
    @Test
    public void testHighlight() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        
        // Test when term is null
        java.util.List<LineResult> result1 = searchableTextPane.highlight(null, 0);
        Assert.assertTrue(result1.isEmpty());
        
        // Test when term is empty
        java.util.List<LineResult> result2 = searchableTextPane.highlight("", 0);
        Assert.assertTrue(result2.isEmpty());
        
        // Test when term is not null or empty
        java.util.List<WordResult> searchResults = new java.util.ArrayList<>();
        searchResults.add(new WordResult("term", 0, 4));
        searchResults.add(new WordResult("term", 10, 14));
        LineResult lineResult = new LineResult(0, 0, 20);
        lineResult.addWord(new WordResult("term", 0, 4));
        lineResult.addWord(new WordResult("term", 10, 14));
        java.util.List<LineResult> expectedResult = new java.util.ArrayList<>();
        expectedResult.add(lineResult);
        
        java.util.List<LineResult> result3 = searchableTextPane.highlight("term", 0);
        Assert.assertEquals(expectedResult, result3);
    }
    
    @Test
    public void testAddHighlight() throws BadLocationException {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.setText("This is a test");
        
        // Test when start and length are valid
        searchableTextPane.addHighlight(0, 4);
        javax.swing.text.DefaultHighlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        Assert.assertEquals(1, highlights.length);
        
        // Test when start and length are invalid
        try {
            searchableTextPane.addHighlight(-1, 5);
            Assert.fail("Expected BadLocationException to be thrown");
        } catch (BadLocationException e) {
            // Test passed
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.setText("This is a test");
        searchableTextPane.addHighlight(0, 4);
        searchableTextPane.addHighlight(5, 7);
        
        // Test when highlights exist
        searchableTextPane.removeHighlights();
        javax.swing.text.DefaultHighlighter.Highlight[] highlights1 = searchableTextPane.getHighlighter().getHighlights();
        Assert.assertEquals(0, highlights1.length);
        
        // Test when no highlights exist
        searchableTextPane.removeHighlights();
        javax.swing.text.DefaultHighlighter.Highlight[] highlights2 = searchableTextPane.getHighlighter().getHighlights();
        Assert.assertEquals(0, highlights2.length);
    }
    
    @Test
    public void testSearch() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.setText("This is a test");
        searchableTextPane.search("test");
        
        // Test when term is null
        int result1 = searchableTextPane.search(null);
        Assert.assertEquals(-1, result1);
        
        // Test when term is empty
        int result2 = searchableTextPane.search("");
        Assert.assertEquals(-1, result2);
        
        // Test when term is equal to lastSearchTerm
        int result3 = searchableTextPane.search("test");
        Assert.assertEquals(0, result3);
        
        // Test when term is not equal to lastSearchTerm
        int result4 = searchableTextPane.search("new");
        Assert.assertEquals(8, result4);
    }
    
    @Test
    public void testSearchWithStartPosition() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.setText("This is a test");
        searchableTextPane.search("test");
        
        // Test when term is null and startPos is 0
        int result1 = searchableTextPane.search(null, 0);
        Assert.assertEquals(-1, result1);
        
        // Test when term is empty and startPos is 0
        int result2 = searchableTextPane.search("", 0);
        Assert.assertEquals(-1, result2);
        
        // Test when term is equal to lastSearchTerm and startPos is 0
        int result3 = searchableTextPane.search("test", 0);
        Assert.assertEquals(0, result3);
        
        // Test when term is not equal to lastSearchTerm and startPos is 0
        int result4 = searchableTextPane.search("new", 0);
        Assert.assertEquals(8, result4);
        
        // Test when term is equal to lastSearchTerm and startPos is not 0
        int result5 = searchableTextPane.search("test", 4);
        Assert.assertEquals(8, result5);
        
        // Test when term is not equal to lastSearchTerm and startPos is not 0
        int result6 = searchableTextPane.search("new", 4);
        Assert.assertEquals(-1, result6);
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.setText("This is a test");
        
        java.util.List<WordResult> words = new java.util.ArrayList<>();
        words.add(new WordResult("This", 0, 3));
        words.add(new WordResult("test", 10, 13));
        
        LineResult lineResult1 = new LineResult(0, 0, 14);
        LineResult lineResult2 = new LineResult(1, 15, 18);
        lineResult1.addWord(new WordResult("This", 0, 3));
        lineResult2.addWord(new WordResult("test", 10, 13));
        
        java.util.List<LineResult> expectedResult = new java.util.ArrayList<>();
        expectedResult.add(lineResult1);
        expectedResult.add(lineResult2);
        
        java.util.List<LineResult> result = searchableTextPane.convertWords2Lines(words);
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.setText("This is a test");
        
        WordResult wordResult = new WordResult("test", 10, 13);
        LineResult lineResult = new LineResult(1, 15, 18);
        
        searchableTextPane.updateWordResult(wordResult, lineResult);
        
        int lineNumber = wordResult.parent.lineNumber;
        Assert.assertEquals(2, lineNumber);
        
        int lineOffset = wordResult.getLineOffset();
        Assert.assertEquals(15, lineOffset);
    }
}