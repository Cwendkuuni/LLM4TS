package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Font;
import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.text.DefaultHighlighter;

import org.junit.Before;
import org.junit.Test;

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
    public void testGetScrollableTracksViewportWidthShouldReturnTrueWhenParentIsNull() {
        searchableTextPane.setParent(null);
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        
        assertEquals(true, result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidthShouldReturnTrueWhenUIWidthIsLessThanParentWidth() {
        searchableTextPane.setParent(new JTextArea());
        searchableTextPane.setSize(200, 100);
        FontUIResource ui = new FontUIResource();
        ui.width = 150;
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        
        assertEquals(true, result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidthShouldReturnFalseWhenUIWidthIsEqualToParentWidth() {
        searchableTextPane.setParent(new JTextArea());
        searchableTextPane.setSize(200, 100);
        FontUIResource ui = new FontUIResource();
        ui.width = 200;
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        
        assertEquals(false, result);
    }
    
    @Test
    public void testHighlightShouldHighlightWordsInTextAndReturnLineResults() {
        String term = "test";
        int flags = 0;
        
        List<LineResult> result = searchableTextPane.highlight(term, flags);
        
        assertNotNull(result);
    }
    
    @Test
    public void testHighlightShouldNotHighlightWordsInTextWhenTermIsNull() {
        String term = null;
        int flags = 0;
        
        List<LineResult> result = searchableTextPane.highlight(term, flags);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testHighlightShouldNotHighlightWordsInTextWhenTermIsEmpty() {
        String term = "";
        int flags = 0;
        
        List<LineResult> result = searchableTextPane.highlight(term, flags);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAddHighlightShouldAddHighlightToText() {
        int start = 0;
        int length = 5;
        
        try {
            searchableTextPane.addHighlight(start, length);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        assertEquals(1, searchableTextPane.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testRemoveHighlightsShouldRemoveAllHighlightsFromText() {
        int start = 0;
        int length = 5;
        try {
            searchableTextPane.addHighlight(start, length);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        searchableTextPane.removeHighlights();
        
        assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testSearchShouldReturnPositionOfTermInText() {
        String term = "test";
        
        int result = searchableTextPane.search(term);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchShouldSetLastSearchTermToNullWhenSearchPosIsMinus1() {
        String term = "test";
        searchableTextPane.lastSearchTerm = term;
        searchableTextPane.lastSearchPos = 0;
        
        searchableTextPane.search(term);
        
        assertNull(searchableTextPane.lastSearchTerm);
    }
    
    @Test
    public void testSearchShouldSetLastSearchTermToTermWhenSearchPosIsNotMinus1() {
        String term = "test";
        searchableTextPane.lastSearchTerm = null;
        searchableTextPane.lastSearchPos = 0;
        
        searchableTextPane.search(term);
        
        assertEquals(term, searchableTextPane.lastSearchTerm);
    }
    
    @Test
    public void testSearchWithStartPosShouldReturnPositionOfTermInText() {
        String term = "test";
        int startPos = 0;
        
        int result = searchableTextPane.search(term, startPos);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearchWithStartPosShouldSetLastSearchTermToNullWhenSearchPosIsMinus1() {
        String term = "test";
        int startPos = 0;
        searchableTextPane.lastSearchTerm = term;
        searchableTextPane.lastSearchPos = startPos;
        
        searchableTextPane.search(term, startPos);
        
        assertNull(searchableTextPane.lastSearchTerm);
    }
    
    @Test
    public void testSearchWithStartPosShouldSetLastSearchTermToTermWhenSearchPosIsNotMinus1() {
        String term = "test";
        int startPos = 0;
        searchableTextPane.lastSearchTerm = null;
        searchableTextPane.lastSearchPos = startPos;
        
        searchableTextPane.search(term, startPos);
        
        assertEquals(term, searchableTextPane.lastSearchTerm);
    }
    
    @Test
    public void testConvertWords2LinesShouldConvertWordsToLineResults() {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 3));
        words.add(new WordResult(8, 11));
        words.add(new WordResult(14, 17));
        
        try {
            List<LineResult> result = searchableTextPane.convertWords2Lines(words);
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(words, result.get(0).getWordResults());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUpdateWordResultShouldAddWordResultToLineResult() {
        WordResult wordResult = new WordResult(0, 3);
        LineResult lineResult = new LineResult(0, 0, 10);
        
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
            assertNotNull(lineResult.getWordResults());
            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(wordResult, lineResult.getWordResults().get(0));
            assertEquals(1, wordResult.parent.lineNumber);
            assertEquals(0, wordResult.getLineOffset());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}