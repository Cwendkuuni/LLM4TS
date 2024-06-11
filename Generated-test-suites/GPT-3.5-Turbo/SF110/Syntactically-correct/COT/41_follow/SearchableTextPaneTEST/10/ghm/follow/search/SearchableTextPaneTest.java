import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Utilities;
import javax.swing.text.DefaultHighlighter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        searchableTextPane  = new SearchableTextPane(font, 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        // Test case for when parent is null
        Assert.assertTrue(result);

        // Test case for when parent is not null
        Component parent = new JPanel();
        searchableTextPane.setParent(parent);
        Assert.assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        // Test case for when term is null
        List<LineResult> result = searchableTextPane.highlight(null, 0);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());

        // Test case for when term is empty
        result = searchableTextPane.highlight("", 0);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());

        // Test case for normal functionality
        result = searchableTextPane.highlight("test", 0);
        Assert.assertNotNull(result);
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        // Test case for normal functionality
        searchableTextPane.setText("Hello World");
        searchableTextPane.addHighlight(0, 5);
        Assert.assertEquals(1, searchableTextPane.getHighlighter().getHighlights().length);

        // Test case for BadLocationException
        try {
            searchableTextPane.addHighlight(-1, 5);
            Assert.fail("Expected BadLocationException not thrown");
        } catch (BadLocationException ex) {
            // Exception is expected
        }
    }

    @Test
    public void testRemoveHighlights() {
        // Test case for normal functionality
        searchableTextPane.addHighlight(0, 5);
        searchableTextPane.removeHighlights();
        Assert.assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch() {
        // Test case for when term is null
        int result = searchableTextPane.search(null);
        Assert.assertEquals(-1, result);

        // Test case for when term is empty
        result = searchableTextPane.search("");
        Assert.assertEquals(-1, result);

        // Test case for when term equals lastSearchTerm
        searchableTextPane.lastSearchPos = 3;
        searchableTextPane.lastSearchTerm = "test";
        result = searchableTextPane.search("test");
        Assert.assertEquals(8, result);

        // Test case for normal functionality
        searchableTextPane.lastSearchPos = -1;
        searchableTextPane.lastSearchTerm = null;
        result = searchableTextPane.search("test");
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSearchWithStartPos() {
        // Test case for normal functionality
        searchableTextPane.setText("Hello World");
        int result = searchableTextPane.search("o", 5);
        Assert.assertEquals(7, result);

        // Test case for BadLocationException
        searchableTextPane.setText("Hello World");
        try {
            searchableTextPane.search("o", -1);
            Assert.fail("Expected BadLocationException not thrown");
        } catch (BadLocationException ex) {
            // Exception is expected
        }
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        List<WordResult> words = new ArrayList<WordResult>();
        words.add(new WordResult(0, 5));
        words.add(new WordResult(5, 10));
        words.add(new WordResult(10, 15));

        List<LineResult> result = searchableTextPane.convertWords2Lines(words);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(0, result.get(0).getLineNumber());
        Assert.assertEquals(0, result.get(0).getStartOffset());
        Assert.assertEquals(15, result.get(0).getEndOffset());

        // Test case for multiple lines
        words.add(new WordResult(16, 20));
        words.add(new WordResult(21, 25));
        result = searchableTextPane.convertWords2Lines(words);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(0, result.get(0).getLineNumber());
        Assert.assertEquals(0, result.get(0).getStartOffset());
        Assert.assertEquals(15, result.get(0).getEndOffset());
        Assert.assertEquals(1, result.get(1).getLineNumber());
        Assert.assertEquals(16, result.get(1).getStartOffset());
        Assert.assertEquals(25, result.get(1).getEndOffset());
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        LineResult lineResult = new LineResult(0, 0, 5);
        WordResult wordResult = new WordResult(0, 3);
        searchableTextPane.updateWordResult(wordResult, lineResult);

        Assert.assertEquals(1, lineResult.getWordResults().size());
        Assert.assertEquals(1, wordResult.getParent().getLineNumber());
        Assert.assertEquals(0, wordResult.getLineOffset());
    }

    @Test
    public void testFullCoverage() {
        // Add additional test cases for edge cases or exception handling scenarios
    }
  
    /*  */
    // Merge all the individual test cases
    /*  */

}