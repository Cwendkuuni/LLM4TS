import org.junit.Before;
import org.junit.Test;
import javax.swing.text.BadLocationException;
import java.awt.Font;
import java.util.List;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {
    
    private SearchableTextPane searchPane;
    
    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        searchPane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_WhenParentIsNull_ReturnsTrue() {
        boolean result = searchPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_WhenUIPreferredSizeLessThanParentWidth_ReturnsTrue() {
        searchPane.setSize(500, 500);
        boolean result = searchPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_WhenUIPreferredSizeEqualParentWidth_ReturnsTrue() {
        searchPane.setSize(500, 500);
        searchPane.setPreferredSize(searchPane.getSize());
        boolean result = searchPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_WhenUIPreferredSizeGreaterThanParentWidth_ReturnsFalse() {
        searchPane.setSize(500, 500);
        searchPane.setPreferredSize(700, 500);
        boolean result = searchPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }
    
    @Test
    public void testHighlight_WhenTermIsNull_ReturnsEmptyList() {
        List<LineResult> result = searchPane.highlight(null, 0);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testHighlight_WhenTermIsEmpty_ReturnsEmptyList() {
        List<LineResult> result = searchPane.highlight("", 0);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testHighlight_WhenTermExistsInText_ReturnsNonEmptyList() {
        searchPane.setText("This is a sample text. This is a sample search term.");
        List<LineResult> result = searchPane.highlight("sample", 0);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testHighlight_WhenTermDoesNotExistInText_ReturnsEmptyList() {
        searchPane.setText("This is a sample text. This is a sample search term.");
        List<LineResult> result = searchPane.highlight("nonexistent", 0);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAddHighlight_WhenStartAndLengthAreValid_DoesNotThrowException() {
        try {
            searchPane.addHighlight(0, 10);
        } catch (BadLocationException e) {
            fail("Should not throw BadLocationException");
        }
    }
    
    @Test
    public void testRemoveHighlights_RemovesAllHighlights() {
        try {
            searchPane.addHighlight(0, 10);
            searchPane.addHighlight(15, 20);
            searchPane.removeHighlights();
            assertEquals(0, searchPane.getHighlighter().getHighlights().length);
        } catch (BadLocationException e) {
            fail("Should not throw BadLocationException");
        }
    }
    
    @Test
    public void testSearch_WhenTermIsNull_ReturnsNegativeOne() {
        int result = searchPane.search(null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_WhenTermIsEmpty_ReturnsNegativeOne() {
        int result = searchPane.search("");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_WhenTermEqualsLastSearchTerm_StartsFromLastSearchPos() {
        searchPane.setText("This is a sample text. This is a sample search term.");
        searchPane.search("sample");
        int result = searchPane.search("sample");
        assertEquals(15, result);
    }
    
    @Test
    public void testSearch_WhenTermNotEqualsLastSearchTerm_StartsFromBeginning() {
        searchPane.setText("This is a sample text. This is a sample search term.");
        searchPane.search("sample");
        int result = searchPane.search("text");
        assertEquals(10, result);
    }
    
    @Test
    public void search_WhenTermExistsFromStart_ReturnsCorrectPosition() {
        searchPane.setText("This is a sample text. This is a sample search term.");
        int result = searchPane.search("This");
        assertEquals(0, result);
    }
    
    @Test
    public void search_WhenTermExistsInMiddle_ReturnsCorrectPosition() {
        searchPane.setText("This is a sample text. This is a sample search term.");
        int result = searchPane.search("sample");
        assertEquals(10, result);
    }
    
    @Test
    public void search_WhenTermExistsAtEnd_ReturnsCorrectPosition() {
        searchPane.setText("This is a sample text. This is a sample search term.");
        int result = searchPane.search("term.");
        assertEquals(40, result);
    }
    
    @Test
    public void search_WhenTermDoesNotExist_ReturnsNegativeOne() {
        searchPane.setText("This is a sample text. This is a sample search term.");
        int result = searchPane.search("nonexistent");
        assertEquals(-1, result);
    }
    
    @Test
    public void search_WhenBadLocationExceptionOccurs_ReturnsNegativeOne() {
        int result = searchPane.search("term", -1);
        assertEquals(-1, result);
    }
    
    @Test
    public void testConvertWords2Lines_ReturnsNonEmptyList() {
        List<LineResult> result = null;
        try {
            searchPane.setText("This is a sample text. This is a sample search term.");
            List<WordResult> wordResults = new ArrayList<>();
            wordResults.add(new WordResult("sample", 10, 15));
            wordResults.add(new WordResult("sample", 25, 30));
            result = searchPane.convertWords2Lines(wordResults);
        } catch (BadLocationException e) {
            fail("Should not throw BadLocationException");
        }
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void updateWordResult_AddsWordToLineResult() {
        try {
            LineResult lineResult = new LineResult(0, 0, 100);
            WordResult wordResult = new WordResult("sample", 10, 15);
            searchPane.updateWordResult(wordResult, lineResult);
            assertEquals(1, lineResult.getWordResults().size());
            assertEquals("sample", lineResult.getWordResults().get(0).word);
        } catch (BadLocationException e) {
            fail("Should not throw BadLocationException");
        }
    }
    
    @Test
    public void updateWordResult_SetsLineNumberCorrectly() {
        try {
            LineResult lineResult = new LineResult(0, 0, 100);
            WordResult wordResult = new WordResult("sample", 10, 15);
            searchPane.updateWordResult(wordResult, lineResult);
            assertEquals(1, lineResult.lineNumber);
        } catch (BadLocationException e) {
            fail("Should not throw BadLocationException");
        }
    }
    
    @Test
    public void updateWordResult_SetsLineOffsetCorrectly() {
        try {
            LineResult lineResult = new LineResult(0, 0, 100);
            WordResult wordResult = new WordResult("sample", 10, 15);
            searchPane.updateWordResult(wordResult, lineResult);
            assertEquals(0, wordResult.lineOffset);
        } catch (BadLocationException e) {
            fail("Should not throw BadLocationException");
        }
    }
}