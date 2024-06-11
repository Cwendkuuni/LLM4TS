import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;

public class SearchableTextPaneTest {

    private SearchableTextPane stp;
    
    @Before
    public void setUp() {
        stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_NullParent_ReturnsTrue() {
        stp.setParent(null);
        assertTrue(stp.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_NonNullParent_ReturnsTrueIfUIWidthLessThanOrEqualToParentWidth() {
        stp.setPreferredSize(new Dimension(100, 50));
        stp.setParent(new JPanel());
        assertTrue(stp.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_NonNullParent_ReturnsFalseIfUIWidthGreaterThanParentWidth() {
        stp.setPreferredSize(new Dimension(300, 50));
        stp.setParent(new JPanel());
        assertFalse(stp.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight_EmptyTerm_ReturnsEmptyLineResults() {
        List<LineResult> lineResults = stp.highlight("", 0);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_NullTerm_ReturnsEmptyLineResults() {
        List<LineResult> lineResults = stp.highlight(null, 0);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_TermNotFound_ReturnsEmptyLineResults() {
        stp.setText("This is a test string");
        List<LineResult> lineResults = stp.highlight("apple", 0);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_TermFoundInSingleLine_ReturnsCorrectLineResults() {
        stp.setText("This is a test string");
        List<LineResult> lineResults = stp.highlight("test", 0);
        assertEquals(1, lineResults.size());
        assertEquals(0, lineResults.get(0).getLine());
        assertEquals("test", lineResults.get(0).getWordResults().get(0).getWord());
    }
    
    @Test
    public void testAddHighlight_ValidPositions_AddsHighlights() {
        stp.setText("This is a test string");
        try {
            stp.addHighlight(5, 2);
            stp.addHighlight(10, 3);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testAddHighlight_InvalidPositions_ThrowsBadLocationException() {
        stp.setText("This is a test string");
        try {
            stp.addHighlight(-1, 2);
            fail("BadLocationException should be thrown");
        } catch (BadLocationException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testRemoveHighlights_RemovesAllHighlights() {
        stp.setText("This is a test string");
        try {
            stp.addHighlight(5, 2);
            stp.addHighlight(10, 3);
            stp.removeHighlights();
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testSearch_EmptyTerm_ReturnsNegativeOne() {
        int result = stp.search("");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_NullTerm_ReturnsNegativeOne() {
        int result = stp.search(null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_TermEqualsLastSearchTerm_ReturnsLastSearchPosPlusTermLength() {
        stp.lastSearchTerm = "test";
        stp.lastSearchPos = 5;
        int result = stp.search("test");
        assertEquals(9, result);
    }
    
    @Test
    public void testSearch_TermNotEqualsLastSearchTerm_ReturnsResultsFromSearchMethod() {
        stp.lastSearchTerm = "apple";
        stp.lastSearchPos = 3;
        int result = stp.search("test");
        assertEquals(10, result);
    }
    
    @Test
    public void testSearch_TermNotEqualsLastSearchTerm_ReturnsNegativeOneIfResultFromSearchMethodIsNegativeOne() {
        stp.lastSearchTerm = "apple";
        stp.lastSearchPos = 3;
        int result = stp.search("pineapple");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_ValidTermAndValidStartPos_ReturnsCorrectResult() {
        stp.setText("This is a test string");
        int result = stp.search("test", 3);
        assertEquals(10, result);
    }
    
    @Test
    public void testSearch_TermNotFound_ReturnsNegativeOne() {
        stp.setText("This is a test string");
        int result = stp.search("apple");
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_ValidTermAndInvalidStartPos_ThrowsBadLocationException() {
        stp.setText("This is a test string");
        try {
            stp.search("test", -1);
            fail("BadLocationException should be thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testConvertWords2Lines_EmptyWords_ReturnsEmptyLines() {
        List<LineResult> lineResults = new ArrayList<>();
        try {
            lineResults = stp.convertWords2Lines(new ArrayList<WordResult>());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testConvertWords2Lines_NonEmptyWords_ReturnsCorrectLines() {
        stp.setText("This is a test string");
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(2, 4, "is"));
        words.add(new WordResult(8, 11, "test"));
        words.add(new WordResult(14, 20, "string"));
        List<LineResult> lineResults = new ArrayList<>();
        try {
            lineResults = stp.convertWords2Lines(words);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals(2, lineResults.size());
        assertEquals(0, lineResults.get(0).getLine());
        assertEquals(1, lineResults.get(1).getLine());
        assertEquals("is", lineResults.get(0).getWordResults().get(0).getWord());
        assertEquals("test", lineResults.get(1).getWordResults().get(0).getWord());
        assertEquals("string", lineResults.get(1).getWordResults().get(1).getWord());
    }
    
    @Test
    public void testConvertWords2Lines_InvalidOffsets_ThrowsBadLocationException() {
        stp.setText("This is a test string");
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(2, 4, "is"));
        words.add(new WordResult(8, 14, "This is"));
        words.add(new WordResult(14, 20, "string"));
        try {
            stp.convertWords2Lines(words);
            fail("BadLocationException should be thrown");
        } catch (BadLocationException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testUpdateWordResult_ValidWordAndLineResult_UpdatesWordResult() {
        stp.setText("This is a test string");
        WordResult wordResult = new WordResult(5, 7, "is");
        LineResult lineResult = new LineResult(0, 0, 4);
        try {
            stp.updateWordResult(wordResult, lineResult);
            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(1, wordResult.parent.lineNumber);
            assertEquals(0, wordResult.getLineOffset());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testUpdateWordResult_NullWordResult_ThrowsNullPointerException() {
        LineResult lineResult = new LineResult(0, 0, 4);
        try {
            stp.updateWordResult(null, lineResult);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testUpdateWordResult_NullLineResult_ThrowsNullPointerException() {
        WordResult wordResult = new WordResult(5, 7, "is");
        try {
            stp.updateWordResult(wordResult, null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}