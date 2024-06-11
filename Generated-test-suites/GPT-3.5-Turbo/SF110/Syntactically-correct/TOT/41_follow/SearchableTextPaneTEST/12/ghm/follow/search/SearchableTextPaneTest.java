import org.junit.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;


public class SearchableTextPaneTest {
    
    private SearchableTextPane searchableTextPane;
    private Font font;
    private int tabSize;
    
    @Before
    public void setUp() {
        font = new Font("Arial", Font.PLAIN, 12);
        tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidthWithParent() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.add(searchableTextPane);
        frame.getContentPane().add(panel);
        frame.setSize(500, 500);
        frame.setVisible(true);
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        
        Assert.assertTrue(result);
        
        frame.dispose();
    }
    
    @Test
    public void testGetScrollableTracksViewportWidthWithoutParent() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testHighlightWithTermAndFlags() {
        List<LineResult> lineResults = searchableTextPane.highlight("Lorem", SearchEngine.CASE_INSENSITIVE);
        
        Assert.assertEquals(0, lineResults.size());
    }
    
    @Test
    public void testHighlightWithNullTerm() {
        List<LineResult> lineResults = searchableTextPane.highlight(null, SearchEngine.CASE_INSENSITIVE);
        
        Assert.assertEquals(0, lineResults.size());
    }
    
    @Test
    public void testHighlightWithEmptyTerm() {
        List<LineResult> lineResults = searchableTextPane.highlight("", SearchEngine.CASE_INSENSITIVE);
        
        Assert.assertEquals(0, lineResults.size());
    }
    
    @Test
    public void testAddHighlight() {
        try {
            searchableTextPane.addHighlight(0, 5);
            
            // If no exception is thrown, the test passed
            Assert.assertTrue(true);
        } catch (BadLocationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        try {
            searchableTextPane.addHighlight(0, 5);
            searchableTextPane.removeHighlights();
            
            // If no exception is thrown, the test passed
            Assert.assertTrue(true);
        } catch (BadLocationException e) {
            Assert.fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testSearchWithTerm() {
        int result = searchableTextPane.search("Lorem");
        
        Assert.assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithNullTerm() {
        int result = searchableTextPane.search(null);
        
        Assert.assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithEmptyTerm() {
        int result = searchableTextPane.search("");
        
        Assert.assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithLastSearchTerm() {
        searchableTextPane.lastSearchPos = 0;
        searchableTextPane.lastSearchTerm = "Lorem";
        
        int result = searchableTextPane.search("Lorem");
        
        Assert.assertEquals(5, result);
    }
    
    @Test
    public void testSearchWithDifferentTerm() {
        int result = searchableTextPane.search("Lorem");
        
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testSearchWithStartPos() {
        int result = searchableTextPane.search("Lorem", 7);
        
        Assert.assertEquals(-1, result);
    }
    
    @Test
    public void testConvertWords2Lines() {
        List<WordResult> wordResults = new ArrayList<>();
        WordResult wordResult = new WordResult(0, 5);
        wordResults.add(wordResult);
        
        List<LineResult> lineResults = null;
        try {
            lineResults = searchableTextPane.convertWords2Lines(wordResults);
        } catch (BadLocationException e) {
            Assert.fail("Exception should not be thrown");
        }
        
        Assert.assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        Assert.assertEquals(0, lineResult.getLine());
        Assert.assertEquals(0, lineResult.getLineStart());
        Assert.assertEquals(5, lineResult.getLineEnd());
        Assert.assertEquals(1, lineResult.getWordResults().size());
    }
    
    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(0, 0, 5);
        
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
        } catch (BadLocationException e) {
            Assert.fail("Exception should not be thrown");
        }
        
        Assert.assertEquals(1, lineResult.getWordResults().size());
        Assert.assertEquals(1, wordResult.getParent().getLineNumber());
        Assert.assertEquals(0, wordResult.getLineOffset());
    }
    
    @After
    public void tearDown() {
        searchableTextPane = null;
        font = null;
    }
}