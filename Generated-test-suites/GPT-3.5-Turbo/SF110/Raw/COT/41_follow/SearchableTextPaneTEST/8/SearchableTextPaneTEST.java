import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {
    
    @Test
    public void testGetScrollableTracksViewportWidth_WithNullParent_ReturnsTrue() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        boolean result = pane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_WithParentAndUI_ReturnsTrueIfUIWidthLessThanOrEqualToParentWidth() {
        JTextArea parent = new JTextArea();
        ComponentUI ui = parent.getUI();
        
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        pane.setParent(parent);
        pane.setUI(ui);
        
        boolean result = pane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_WithParentAndUI_ReturnsFalseIfUIWidthGreaterThanParentWidth() {
        JTextArea parent = new JTextArea();
        ComponentUI ui = parent.getUI();
        
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        pane.setParent(parent);
        pane.setUI(ui);
        pane.setSize(new Dimension(500, 200));
        
        boolean result = pane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }
    
    @Test
    public void testHighlight_WithNullTerm_ReturnsEmptyLineResultList() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        List<LineResult> result = pane.highlight(null, 0);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testHighlight_WithEmptyTerm_ReturnsEmptyLineResultList() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        List<LineResult> result = pane.highlight("", 0);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testRemoveHighlights_ShouldRemoveAllHighlights() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        
        try {
            pane.addHighlight(0, 5);
            pane.addHighlight(10, 15);
            pane.removeHighlights();
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
        
        Highlighter.Highlight[] highlights = pane.getHighlighter().getHighlights();
        assertEquals(0, highlights.length);
    }
    
    @Test
    public void testSearch_WithNullTerm_ReturnsNegativeOne() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        int result = pane.search(null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_WithEmptyTerm_ReturnsNegativeOne() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        int result = pane.search("");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_WithSameTermAsLastSearchTerm_ReturnsNextSearchPosition() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        pane.lastSearchTerm = "test";
        pane.lastSearchPos = 5;
        int result = pane.search("test");
        assertEquals(9, result);
    }
    
    @Test
    public void testSearch_WithDifferentTermAsLastSearchTerm_ReturnsSearchPositionFromStart() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        pane.lastSearchTerm = "test";
        pane.lastSearchPos = 5;
        int result = pane.search("search");
        assertEquals(0, result);
    }
    
    @Test
    public void testSearch_WithInvalidStartPos_ReturnsNegativeOne() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        int result = pane.search("test", -1);
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_WithValidStartPos_ReturnsSearchPosition() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        try {
            pane.getDocument().insertString(0, "This is a test", null);
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
        
        int result = pane.search("test", 0);
        assertEquals(10, result);
    }
    
    @Test
    public void testConvertWords2Lines_WithEmptyWordsList_ReturnsEmptyLineResultList() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        List<WordResult> words = new ArrayList<WordResult>();
        List<LineResult> result = null;
        
        try {
            result = pane.convertWords2Lines(words);
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testConvertWords2Lines_WithSingleWordInSingleLine_ReturnsSingleLineResultWithSingleWordResult() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        List<WordResult> words = new ArrayList<WordResult>();
        words.add(new WordResult("test", 5, 9));
        
        List<LineResult> result = null;
        
        try {
            result = pane.convertWords2Lines(words);
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
        
        assertEquals(1, result.size());
        LineResult lineResult = result.get(0);
        assertEquals(1, lineResult.getLine());
        assertEquals(0, lineResult.getLineStartOffset());
        assertEquals(14, lineResult.getLineEndOffset());
        
        List<WordResult> wordResults = lineResult.getWordResults();
        assertEquals(1, wordResults.size());
        WordResult wordResult = wordResults.get(0);
        assertEquals(5, wordResult.getStart());
        assertEquals(9, wordResult.getEnd());
    }
  
    @Test
    public void testUpdateWordResult_WithWordResultAndLineResult_AddsWordResultToLineResultAndSetsLineOffset() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Times New Roman", Font.PLAIN, 12), 4);
        LineResult lineResult = new LineResult(1, 0, 14);
        WordResult wordResult = new WordResult("test", 5, 9);
        
        try {
            pane.updateWordResult(wordResult, lineResult);
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
        
        List<WordResult> wordResults = lineResult.getWordResults();
        assertEquals(1, wordResults.size());
        WordResult result = wordResults.get(0);
        assertEquals(5, result.getStart());
        assertEquals(9, result.getEnd());
        
        assertEquals(1, wordResult.parent.lineNumber);
        assertEquals(0, wordResult.getLineOffset());
    }
}