
import org.junit.*;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.ComponentUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.LineResult;
import ghm.follow.search.WordResult;

public class SearchableTextPaneTest {

    private SearchableTextPane pane;
    
    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        pane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Testing when parent is null
        boolean result = pane.getScrollableTracksViewportWidth();
        assertTrue(result);
        
        // Testing when parent is not null
        pane.setParent(new JPanel());
        result = pane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testHighlight() throws BadLocationException {
        // Testing when term is null
        List<LineResult> lineResults = pane.highlight(null, 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
        
        // Testing when term is empty
        lineResults = pane.highlight("", 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
        
        // Testing with valid term and flags
        String term = "Lorem";
        int flags = 0;
        lineResults = pane.highlight(term, flags);
        assertNotNull(lineResults);
        assertFalse(lineResults.isEmpty());
    }
    
    @Test
    public void testRemoveHighlights() {
        // No specific edge or exception scenario to test
        pane.removeHighlights();
        // Asserting no exceptions are thrown
    }
    
    @Test
    public void testSearch() {
        // Testing when term is null
        int result = pane.search(null);
        assertEquals(-1, result);
        
        // Testing when term is empty
        result = pane.search("");
        assertEquals(-1, result);
        
        // Testing with valid term
        String term = "Lorem";
        result = pane.search(term);
        assertEquals(-1, result); // Assuming -1 means term not found
        
        // Testing with valid term and startPos
        int startPos = 0;
        result = pane.search(term, startPos);
        assertEquals(-1, result); // Assuming -1 means term not found
    }
    
    @Test
    public void testSearchWithStartPos() {
        // Testing when term is null
        int result = pane.search(null, 0);
        assertEquals(-1, result);
        
        // Testing when term is empty
        result = pane.search("", 0);
        assertEquals(-1, result);
        
        // Testing with valid term
        String term = "Lorem";
        result = pane.search(term, 0);
        assertEquals(-1, result); // Assuming -1 means term not found
        
        // Testing with valid term and negative startPos
        int startPos = -1;
        result = pane.search(term, startPos);
        assertEquals(-1, result); // Assuming -1 means term not found
        
        // Testing with valid term and startPos greater than text length
        startPos = 100; // Assuming text length is less than 100
        result = pane.search(term, startPos);
        assertEquals(-1, result); // Assuming -1 means term not found
    }
    
    @Test(expected = BadLocationException.class)
    public void testHighlightWithBadLocationException() throws BadLocationException {
        // Testing BadLocationException scenario
        String term = "Lorem";
        int flags = 0;
        Document doc = pane.getDocument();
        // Assuming doc.getLength() returns invalid length
        pane.highlight(term, flags);
    }
    
    @Test(expected = BadLocationException.class)
    public void testSearchWithBadLocationException() throws BadLocationException {
        // Testing BadLocationException scenario
        String term = "Lorem";
        int startPos = 0;
        Document doc = pane.getDocument();
        // Assuming doc.getLength() returns invalid length
        pane.search(term, startPos);
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        // No specific edge or exception scenario to test
        List<WordResult> words = new ArrayList<WordResult>();
        LineResult lineResult = new LineResult(0, 0, 10);
        WordResult wordResult = new WordResult(0, 5);
        words.add(wordResult);
        
        List<LineResult> lineResults = pane.convertWords2Lines(words);
        assertNotNull(lineResults);
        assertFalse(lineResults.isEmpty());
        assertEquals(1, lineResults.size());
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        // No specific edge or exception scenario to test
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(0, 0, 10);
        
        pane.updateWordResult(wordResult, lineResult);
        assertNotNull(lineResult.getWordResults());
        assertFalse(lineResult.getWordResults().isEmpty());
        assertEquals(1, lineResult.getWordResults().size());
    }
    
    @After
    public void tearDown() {
        pane = null;
    }
}
