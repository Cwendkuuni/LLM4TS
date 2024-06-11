import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withoutParent_ReturnsTrue() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentAndLessWidthThanPreferred_ReturnsTrue() {
        // Create a parent component with width less than the preferred width of the text pane
        Component parent = new JPanel();
        parent.setSize(100, 100);
        searchableTextPane.setParent(parent);

        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentAndEqualWidthAsPreferred_ReturnsTrue() {
        // Create a parent component with width equal to the preferred width of the text pane
        Component parent = new JPanel();
        parent.setSize(200, 100);
        searchableTextPane.setParent(parent);

        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentAndGreaterWidthThanPreferred_ReturnsFalse() {
        // Create a parent component with width greater than the preferred width of the text pane
        Component parent = new JPanel();
        parent.setSize(300, 100);
        searchableTextPane.setParent(parent);

        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight_withEmptyTerm_ReturnsEmptyList() {
        assertTrue(searchableTextPane.highlight("", 0).isEmpty());
    }

    @Test
    public void testHighlight_withNullTerm_ReturnsEmptyList() {
        assertTrue(searchableTextPane.highlight(null, 0).isEmpty());
    }

    @Test
    public void testHighlight_withTermNotPresentInText_ReturnsEmptyList() {
        searchableTextPane.setText("This is a test");
        assertTrue(searchableTextPane.highlight("xyz", 0).isEmpty());
    }

    @Test
    public void testHighlight_withTermPresentInText_ReturnsNonEmptyList() {
        searchableTextPane.setText("This is a test");
        assertFalse(searchableTextPane.highlight("test", 0).isEmpty());
    }

    @Test
    public void testAddHighlight_withValidStartAndLength_ReturnsNoException() {
        assertDoesNotThrow(() -> searchableTextPane.addHighlight(0, 5));
    }

    @Test
    public void testAddHighlight_withNegativeStart_ThrowsBadLocationException() {
        assertThrows(BadLocationException.class, () -> searchableTextPane.addHighlight(-5, 5));
    }

    @Test
    public void testAddHighlight_withNegativeLength_ThrowsBadLocationException() {
        assertThrows(BadLocationException.class, () -> searchableTextPane.addHighlight(0, -5));
    }

    @Test
    public void testRemoveHighlights_withSomeHighlightsPresent_RemovesAllHighlights() {
        assertDoesNotThrow(() -> {
            searchableTextPane.setText("This is a test");
            searchableTextPane.highlight("test", 0);
            searchableTextPane.removeHighlights();
        });
        assertTrue(searchableTextPane.getHighlighter().getHighlights().length == 0);
    }

    @Test
    public void testSearch_withEmptyTerm_ReturnsNegativeOne() {
        assertEquals(-1, searchableTextPane.search(""));
    }

    @Test
    public void testSearch_withNullTerm_ReturnsNegativeOne() {
        assertEquals(-1, searchableTextPane.search(null));
    }

    @Test
    public void testSearch_withTermNotPresentInText_ReturnsNegativeOne() {
        searchableTextPane.setText("This is a test");
        assertEquals(-1, searchableTextPane.search("xyz"));
    }

    @Test
    public void testSearch_withTermPresentInText_ReturnsCorrectPosition() {
        searchableTextPane.setText("This is a test");
        assertEquals(10, searchableTextPane.search("test"));
    }

    @Test
    public void testSearch_withPreviousSearchTerm_ReturnsNextPosition() {
        searchableTextPane.setText("This is a test. This is another test.");
        searchableTextPane.search("test");
        assertEquals(28, searchableTextPane.search("test"));
    }

    @Test
    public void testSearch_withNotPreviousSearchTerm_ReturnsStartPositionOfTerm() {
        searchableTextPane.setText("This is a test. This is another test.");
        searchableTextPane.search("another");
        assertEquals(15, searchableTextPane.search("test"));
    }

    @Test
    public void testSearch_withInvalidStartPos_ReturnsNegativeOne() {
        searchableTextPane.setText("This is a test");
        assertEquals(-1, searchableTextPane.search("test", -5));
    }

    @Test
    public void testSearch_withValidStartPos_ReturnsCorrectPosition() {
        searchableTextPane.setText("This is a test. This is another test.");
        assertEquals(28, searchableTextPane.search("test", 15));
    }

    @Test
    public void testConvertWords2Lines_withEmptyWords_ReturnsEmptyList() {
        assertTrue(searchableTextPane.convertWords2Lines(Collections.emptyList()).isEmpty());
    }

    @Test
    public void testConvertWords2Lines_withNonEmptyWords_ReturnsValidLines() throws BadLocationException {
        searchableTextPane.setText("This is a test. This is another test.");
        WordResult wordResult1 = new WordResult(5, 7);
        WordResult wordResult2 = new WordResult(12, 15);
        WordResult wordResult3 = new WordResult(23, 28);

        LineResult lineResult1 = new LineResult(0, 0, 16);
        LineResult lineResult2 = new LineResult(1, 17, 40);

        lineResult1.addWord(wordResult1);
        lineResult1.addWord(wordResult2);
        lineResult2.addWord(wordResult3);

        assertEquals(2, searchableTextPane.convertWords2Lines(Arrays.asList(wordResult1, wordResult2, wordResult3)).size());
        assertTrue(searchableTextPane.convertWords2Lines(Arrays.asList(wordResult1, wordResult2, wordResult3))
                .containsAll(Arrays.asList(lineResult1, lineResult2)));
    }

    @Test
    public void testUpdateWordResult_withValidWordAndLineResult_AddsWordToLineResult() throws BadLocationException {
        LineResult lineResult = new LineResult(0, 0, 16);
        WordResult wordResult = new WordResult(5, 7);
        searchableTextPane.updateWordResult(wordResult, lineResult);

        assertEquals(1, lineResult.getWordResults().size());
        assertTrue(lineResult.getWordResults().contains(wordResult));
    }

    @Test
    public void testUpdateWordResult_withValidWordAndLineResult_SetsLineNumberCorrectly() throws BadLocationException {
        LineResult lineResult = new LineResult(0, 0, 16);
        WordResult wordResult = new WordResult(5, 7);
        searchableTextPane.updateWordResult(wordResult, lineResult);

        assertEquals(1, wordResult.getParent().getLineNumber());
    }

    @Test
    public void testUpdateWordResult_withValidWordAndLineResult_SetsLineOffsetCorrectly() throws BadLocationException {
        searchableTextPane.setText("This is a test");
        LineResult lineResult = new LineResult(0, 0, 16);
        WordResult wordResult = new WordResult(5, 7);
        searchableTextPane.updateWordResult(wordResult, lineResult);
        
        assertEquals(0, wordResult.getLineOffset());
    }


}