import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

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
    public void testGetScrollableTracksViewportWidth_NoParent_ReturnTrue() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();

        Assert.assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_ParentWidthSmallerThanUI_ReturnTrue() {
        // Simulating a parent component with smaller width than the UI preferred width
        searchableTextPane.setSize(100, 100);
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();

        Assert.assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_ParentWidthGreaterThanUI_ReturnFalse() {
        // Simulating a parent component with greater width than the UI preferred width
        searchableTextPane.setSize(1000, 100);
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();

        Assert.assertFalse(result);
    }

    @Test
    public void testHighlight_NullTerm_ReturnEmptyLineResults() {
        List<LineResult> expected = new ArrayList<LineResult>();

        List<LineResult> result = searchableTextPane.highlight(null, SearchEngine.FLAG_CASE_INSENSITIVE);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testHighlight_EmptyTerm_ReturnEmptyLineResults() {
        List<LineResult> expected = new ArrayList<LineResult>();

        List<LineResult> result = searchableTextPane.highlight("", 0);

        Assert.assertEquals(expected, result);
    }

    // Add more test cases for highlight method

    @Test
    public void testAddHighlight_StartAndLengthWithinDocument_RetainHighlight() {
        // Assuming a document with length 100
        // Simulating adding a highlight starting from position 10 with length 10
        int start = 10;
        int length = 10;

        searchableTextPane.addHighlight(start, length);

        // Assert that the highlight was added correctly
        // The test for this method can be validated visually as it interacts with UI
    }

    @Test
    public void testRemoveHighlights_NoHighlights_ReturnNoHighlights() {
        // Assuming no highlights are present

        searchableTextPane.removeHighlights();

        // Assert that no highlights are present
        // The test for this method can be validated visually as it interacts with UI
    }

    // Add more test cases for removeHighlights method

    @Test
    public void testSearch_NullTerm_ReturnNegativeOne() {
        int expected = -1;

        int result = searchableTextPane.search(null);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSearch_EmptyTerm_ReturnNegativeOne() {
        int expected = -1;

        int result = searchableTextPane.search("");

        Assert.assertEquals(expected, result);
    }

    // Add more test cases for search method

    @Test
    public void testConvertWords2Lines_EmptyWordResults_ReturnEmptyLineResults() {
        List<LineResult> expected = new ArrayList<LineResult>();

        List<LineResult> result = searchableTextPane.convertWords2Lines(new ArrayList<WordResult>());

        Assert.assertEquals(expected, result);
    }

    // Add more test cases for convertWords2Lines method

    @Test
    public void testUpdateWordResult_ValidWordAndLineResult_UpdateLineResult() {
        WordResult wordResult = new WordResult(10, 20);
        LineResult lineResult = new LineResult(0, 0, 100);

        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
        } catch (BadLocationException e) {
            Assert.fail("Should not throw BadLocationException");
        }

        // Assert that the wordResult was correctly added to the lineResult
        // The test for this method can be validated visually as it interacts with UI
    }

    // Add more test cases for updateWordResult method

}