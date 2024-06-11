import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.logging.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {
    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_parentNull_returnsTrue() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_parentNotNullAndUIPreferredWidthLessThanParentWidth_returnsTrue() {
        searchableTextPane.setParent(new JPanel()); // Set a parent for the text pane
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_parentNotNullAndUIPreferredWidthGreaterThanParentWidth_returnsFalse() {
        searchableTextPane.setParent(new JPanel()); // Set a parent for the text pane
        Dimension parentSize = new Dimension(500, 200); // Parent size greater than UI's preferred width
        searchableTextPane.getParent().setSize(parentSize);
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }

    @Test
    public void testHighlight_termIsNull_returnsEmptyList() {
        List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlight_termIsEmpty_returnsEmptyList() {
        List<LineResult> lineResults = searchableTextPane.highlight("", 0);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_validTermAndFlags_returnsLineResults() {
        searchableTextPane.setText("This is a test text.");
        String term = "test";
        int flags = SearchEngine.FLAG_CASE_INSENSITIVE;
        
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
        
        assertFalse(lineResults.isEmpty());
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLineNumber());
        assertEquals(10, lineResult.getWordResults().size());
    }

    @Test
    public void testAddHighlight_validStartAndLength_highlightAdded() throws BadLocationException {
        searchableTextPane.setText("This is a test text.");
        int start = 0;
        int length = 4;
        
        searchableTextPane.addHighlight(start, length);
        
        assertEquals(1, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testRemoveHighlights_highlightsExist_highlightsRemoved() {
        searchableTextPane.setText("This is a test text.");
        int start = 0;
        int length = 4;
        try {
            searchableTextPane.addHighlight(start, length);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        searchableTextPane.removeHighlights();
        
        assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch_termIsNull_returnsNegOne() {
        int result = searchableTextPane.search(null);
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_termIsEmpty_returnsNegOne() {
        int result = searchableTextPane.search("");
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_termEqualsLastSearchTerm_returnsLastSearchPos() {
        searchableTextPane.setText("This is a test text.");
        searchableTextPane.search("test");
        int lastSearchPos = searchableTextPane.getLastSearchPos();
        
        int result = searchableTextPane.search("test");
        
        assertEquals(lastSearchPos, result);
    }
    
    @Test
    public void testSearch_termDoesNotEqualLastSearchTerm_returnsNewSearchPos() {
        searchableTextPane.setText("This is a test text.");
        int result = searchableTextPane.search("test");
        
        assertNotEquals(-1, result);
    }

    @Test
    public void testSearch_validTermAndStartPos_returnsSearchPos() {
        searchableTextPane.setText("This is a test text.");
        String term = "test";
        int startPos = 10;
        
        int result = searchableTextPane.search(term, startPos);
        
        assertEquals(14, result);
    }

    @Test
    public void testConvertWords2Lines_validWords_returnsLineResults() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        WordResult wordResult1 = new WordResult(0, 3);
        WordResult wordResult2 = new WordResult(5, 7);
        WordResult wordResult3 = new WordResult(10, 13);
        words.add(wordResult1);
        words.add(wordResult2);
        words.add(wordResult3);
        searchableTextPane.setText("This is a test text.");
        
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
        
        assertFalse(lineResults.isEmpty());
        assertEquals(2, lineResults.size());
        assertEquals(1, lineResults.get(0).getLineNumber());
        assertEquals(2, lineResults.get(0).getWordResults().size());
        assertEquals(2, lineResults.get(1).getLineNumber());
        assertEquals(1, lineResults.get(1).getWordResults().size());
    }

    @Test
    public void testUpdateWordResult_validWordAndLineResult_wordAddedToLineResult() throws BadLocationException {
        LineResult lineResult = new LineResult(1, 0, 5);
        WordResult wordResult = new WordResult(2, 4);
        
        searchableTextPane.updateWordResult(wordResult, lineResult);

        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(2, lineResult.getWordResults().get(0).start);
        assertEquals(4, lineResult.getWordResults().get(0).end);
    }

}