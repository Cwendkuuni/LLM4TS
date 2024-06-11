import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.ComponentUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.LineResult;
import ghm.follow.search.WordResult;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;
    
    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_WhenParentIsNull_ShouldReturnTrue() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_WhenUIPreferredWidthLessThanOrEqualToParentWidth_ShouldReturnTrue() {
        ComponentUI ui = new ComponentUI() {
            @Override
            public Dimension getPreferredSize(JComponent arg0) {
                return new Dimension(100, 50);
            }
        };
        searchableTextPane.setUI(ui);
        searchableTextPane.setSize(new Dimension(200, 50));
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_WhenUIPreferredWidthGreaterThanParentWidth_ShouldReturnFalse() {
        ComponentUI ui = new ComponentUI() {
            @Override
            public Dimension getPreferredSize(JComponent arg0) {
                return new Dimension(250, 50);
            }
        };
        searchableTextPane.setUI(ui);
        searchableTextPane.setSize(new Dimension(200, 50));
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }
    
    @Test
    public void testHighlight_WhenTermIsNull_ShouldReturnEmptyList() {
        List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_WhenTermIsEmpty_ShouldReturnEmptyList() {
        List<LineResult> lineResults = searchableTextPane.highlight("", 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_WhenTermIsNotFoundInText_ShouldReturnEmptyList() {
        String text = "This is a sample text.";
        searchableTextPane.setText(text);
        
        List<LineResult> lineResults = searchableTextPane.highlight("notfound", 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_WhenTermIsFoundInText_ShouldReturnLineResultsWithCorrectWordResults() {
        String text = "This is a sample text.";
        searchableTextPane.setText(text);
        
        List<LineResult> lineResults = searchableTextPane.highlight("sample", 0);
        assertNotNull(lineResults);
        assertFalse(lineResults.isEmpty());
        assertEquals(1, lineResults.size());
        
        LineResult lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getLineNumber());
        
        List<WordResult> wordResults = lineResult.getWordResults();
        assertNotNull(wordResults);
        assertFalse(wordResults.isEmpty());
        assertEquals(1, wordResults.size());
        
        WordResult wordResult = wordResults.get(0);
        assertEquals(10, wordResult.getStart());
        assertEquals(16, wordResult.getEnd());
    }
    
    @Test
    public void testAddHighlight_WhenStartAndLengthIsOutOfBounds_ShouldThrowBadLocationException() {
        assertThrows(BadLocationException.class, () -> {
            searchableTextPane.addHighlight(-1, 10);
        });
    }
    
    @Test
    public void testAddHighlight_WhenStartAndLengthIsWithinBounds_ShouldNotThrowException() {
        assertDoesNotThrow(() -> {
            searchableTextPane.addHighlight(0, 5);
        });
    }
    
    @Test
    public void testRemoveHighlights_ShouldRemoveAllHighlights() {
        searchableTextPane.setText("Sample text");
        searchableTextPane.addHighlight(0, 6);
        searchableTextPane.addHighlight(7, 11);
        
        searchableTextPane.removeHighlights();
        
        int highlightCount = searchableTextPane.getHighlighter().getHighlights().length;
        assertEquals(0, highlightCount);
    }
    
    @Test
    public void testSearch_WhenTermIsNull_ShouldReturnNegativeOne() {
        int result = searchableTextPane.search(null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_WhenTermIsEmpty_ShouldReturnNegativeOne() {
        int result = searchableTextPane.search("");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_WhenTermDoesNotMatchLastSearchTerm_ShouldReturnCorrectStartPosition() {
        searchableTextPane.setText("This is a sample text.");
        searchableTextPane.search("sample");
        
        int result = searchableTextPane.search("is");
        assertEquals(2, result);
    }
    
    @Test
    public void testSearch_WhenTermMatchesLastSearchTerm_ShouldReturnCorrectStartPositionAfterLastSearchTermEnd() {
        searchableTextPane.setText("This is a sample text.");
        searchableTextPane.search("sample");
        
        int result = searchableTextPane.search("sample");
        assertEquals(10, result);
    }
    
    @Test
    public void testSearch_WhenTermMatchesLastSearchTermAndLastSearchTermIsNull_ShouldReturnCorrectStartPosition() {
        searchableTextPane.setText("This is a sample text.");
        searchableTextPane.search("sample");
        searchableTextPane.lastSearchTerm = null;
        
        int result = searchableTextPane.search("sample");
        assertEquals(10, result);
    }
    
    @Test
    public void testSearch_WhenLastSearchPositionIsNegativeOne_ShouldReturnNegativeOne() {
        searchableTextPane.setText("This is a sample text.");
        searchableTextPane.lastSearchPos = -1;
        
        int result = searchableTextPane.search("sample");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_WhenLastSearchPositionPlusLastSearchTermLengthIsOutOfBounds_ShouldReturnNegativeOne() {
        searchableTextPane.setText("This is a sample text.");
        searchableTextPane.search("sample");
        searchableTextPane.lastSearchPos = 11;
        
        int result = searchableTextPane.search("sample");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_WhenTermIsFound_ShouldReturnCorrectStartPosition() {
        String text = "This is a sample text.";
        searchableTextPane.setText(text);
        
        int result = searchableTextPane.search("sample");
        assertEquals(10, result);
    }
    
    @Test
    public void testSearch_WhenTermIsNotFound_ShouldReturnNegativeOne() {
        String text = "This is a sample text.";
        searchableTextPane.setText(text);
        
        int result = searchableTextPane.search("notfound");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_WhenTermIsFoundAtStartPosition_ShouldReturnCorrectStartPosition() {
        String text = "sample text sample";
        searchableTextPane.setText(text);
        
        int result = searchableTextPane.search("sample");
        assertEquals(0, result);
    }
    
    @Test
    public void testSearch_WhenStartPosIsLessThanZero_ShouldThrowBadLocationException() {
        assertThrows(BadLocationException.class, () -> {
            searchableTextPane.search("sample", -1);
        });
    }
    
    @Test
    public void testSearch_WhenStartPosIsOutOfBounds_ShouldThrowBadLocationException() {
        assertThrows(BadLocationException.class, () -> {
            searchableTextPane.search("sample", 100);
        });
    }
    
    @Test
    public void testSearch_WhenTermIsFoundAfterStartPos_ShouldReturnCorrectStartPosition() {
        String text = "This is a sample text with multiple samples.";
        searchableTextPane.setText(text);
        
        int result = searchableTextPane.search("sample", 24);
        assertEquals(31, result);
    }
    
    @Test
    public void testSearch_WhenTermIsFoundAtStartPos_ShouldReturnCorrectStartPosition() {
        String text = "This is a sample text with multiple samples.";
        searchableTextPane.setText(text);
        
        int result = searchableTextPane.search("This", 0);
        assertEquals(0, result);
    }
    
    @Test
    public void testConvertWords2Lines_WhenWordsIsEmpty_ShouldReturnEmptyList() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
        
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testConvertWords2Lines_WhenWordsHaveMultipleLines_ShouldReturnCorrectLineResults() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(10, 16));
        words.add(new WordResult(18, 21));
        words.add(new WordResult(25, 31));
        
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
        
        assertNotNull(lineResults);
        assertFalse(lineResults.isEmpty());
        assertEquals(2, lineResults.size());
        
        LineResult lineResult1 = lineResults.get(0);
        assertEquals(1, lineResult1.getLineNumber());
        assertEquals(10, lineResult1.getLineStart());
        assertEquals(22, lineResult1.getLineEnd());
        
        LineResult lineResult2 = lineResults.get(1);
        assertEquals(2, lineResult2.getLineNumber());
        assertEquals(25, lineResult2.getLineStart());
        assertEquals(37, lineResult2.getLineEnd());
    }
    
    @Test
    public void testConvertWords2Lines_WhenWordsHaveSingleLine_ShouldReturnCorrectLineResults() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(5, 10));
        words.add(new WordResult(12, 18));
        
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
        
        assertNotNull(lineResults);
        assertFalse(lineResults.isEmpty());
        assertEquals(1, lineResults.size());
        
        LineResult lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(5, lineResult.getLineStart());
        assertEquals(20, lineResult.getLineEnd());
    }
    
    @Test
    public void testUpdateWordResult_ShouldUpdateWordResultLineOffsetAndAddItToLineResult() throws BadLocationException {
        WordResult wordResult = new WordResult(10, 16);
        LineResult lineResult = new LineResult(1, 5, 20);
        
        searchableTextPane.updateWordResult(wordResult, lineResult);
        
        assertEquals(15, wordResult.getLineOffset());
        assertNotNull(lineResult.getWordResults());
        assertFalse(lineResult.getWordResults().isEmpty());
        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(wordResult, lineResult.getWordResults().get(0));
        assertEquals(2, wordResult.getParent().getLineNumber());
    }
}