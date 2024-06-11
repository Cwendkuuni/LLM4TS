
import org.junit.Before;
import org.junit.Test;
import javax.swing.plaf.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.logging.*;
import java.util.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Test when parent is null
        searchableTextPane.getParent();
        ComponentUI ui = searchableTextPane.getUI();
        assertEquals(true, searchableTextPane.getScrollableTracksViewportWidth());
        
        // Test when parent is not null
        JPanel panel = new JPanel();
        panel.add(searchableTextPane);
        searchableTextPane.getParent();
        ui = searchableTextPane.getUI();
        assertEquals(true, searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        // Test when term is null
        assertEquals(new ArrayList<LineResult>(), searchableTextPane.highlight(null, 0));
        
        // Test when term is empty
        assertEquals(new ArrayList<LineResult>(), searchableTextPane.highlight("", 0));
        
        // Test when term is not null and not empty
        String term = "test";
        String text = "This is a test text";
        Document doc = searchableTextPane.getDocument();
        try {
            doc.insertString(0, text, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        List<LineResult> lineResults = new ArrayList<LineResult>();
        LineResult lineResult = new LineResult(0, 0, text.length());
        WordResult wordResult = new WordResult(10, 14);
        lineResult.addWord(wordResult);
        lineResults.add(lineResult);
        assertEquals(lineResults, searchableTextPane.highlight(term, 0));
    }
    
    @Test
    public void testAddHighlight() {
        // Test when start and length are valid
        int start = 0;
        int length = 5;
        try {
            searchableTextPane.addHighlight(start, length);
            Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
            assertEquals(start, highlights[0].getStartOffset());
            assertEquals(start + length, highlights[0].getEndOffset());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        // Test when start and length are invalid
        start = -1;
        length = -5;
        try {
            searchableTextPane.addHighlight(start, length);
        } catch (BadLocationException e) {
            assertTrue(e instanceof BadLocationException);
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        // Test when there are highlights
        int start = 0;
        int length = 5;
        try {
            searchableTextPane.addHighlight(start, length);
            searchableTextPane.removeHighlights();
            Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
            assertEquals(0, highlights.length);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        // Test when there are no highlights
        Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertEquals(0, highlights.length);
    }
    
    @Test
    public void testSearch() {
        // Test when term is null
        assertEquals(-1, searchableTextPane.search(null));
        
        // Test when term is empty
        assertEquals(-1, searchableTextPane.search(""));
        
        // Test when term is not null and not empty
        String term = "test";
        String text = "This is a test text";
        Document doc = searchableTextPane.getDocument();
        try {
            doc.insertString(0, text, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        assertEquals(10, searchableTextPane.search(term));
        
        // Test when lastSearchTerm is not equal to term
        term = "text";
        assertEquals(11, searchableTextPane.search(term));
    }
    
    @Test
    public void testSearchWithStartPos() {
        // Test when term is null
        assertEquals(-1, searchableTextPane.search(null, 0));
        
        // Test when term is empty
        assertEquals(-1, searchableTextPane.search("", 0));
        
        // Test when term is not null and not empty
        String term = "test";
        String text = "This is a test text";
        Document doc = searchableTextPane.getDocument();
        try {
            doc.insertString(0, text, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        assertEquals(10, searchableTextPane.search(term, 0));
        
        // Test when startPos is greater than 0
        assertEquals(-1, searchableTextPane.search(term, 11));
    }
    
    @Test
    public void testConvertWords2Lines() {
        // Test when words is empty
        List<WordResult> words = new ArrayList<WordResult>();
        assertEquals(new ArrayList<LineResult>(), searchableTextPane.convertWords2Lines(words));
        
        // Test when words is not empty
        String term = "test";
        String text = "This is a test text";
        Document doc = searchableTextPane.getDocument();
        try {
            doc.insertString(0, text, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        List<LineResult> lineResults = new ArrayList<LineResult>();
        LineResult lineResult = new LineResult(0, 0, text.length());
        WordResult wordResult = new WordResult(10, 14);
        lineResult.addWord(wordResult);
        lineResults.add(lineResult);
        assertEquals(lineResults, searchableTextPane.convertWords2Lines(words));
    }
    
    @Test
    public void testUpdateWordResult() {
        // Test when wordResult and lineResult are valid
        WordResult wordResult = new WordResult(10, 14);
        LineResult lineResult = new LineResult(0, 0, 20);
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(1, wordResult.parent.lineNumber);
            assertEquals(0, wordResult.getLineOffset());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        // Test when wordResult and lineResult are invalid
        wordResult = null;
        lineResult = null;
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
        } catch (BadLocationException e) {
            assertTrue(e instanceof NullPointerException);
        }
    }
}
