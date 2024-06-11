import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Font;
import java.util.List;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        searchableTextPane = new SearchableTextPane(font, 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_whenNoParentAndUIReturnsTrue_shouldReturnTrue() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_whenNoParentAndUIReturnsFalse_shouldReturnFalse() {
        // Create a UI mock that returns false for getPreferredSize
        ComponentUI uiMock = new ComponentUI() {
            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(500, 0);
            }
        };

        searchableTextPane.setUI(uiMock);

        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight_whenTermIsNull_shouldReturnEmptyList() {
        List<LineResult> lineResults = searchableTextPane.highlight(null, 0);

        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlight_whenTermIsEmpty_shouldReturnEmptyList() {
        List<LineResult> lineResults = searchableTextPane.highlight("", 0);

        assertTrue(lineResults.isEmpty());
    }

    // Add more test cases for highlight method

    @Test
    public void testAddHighlight_whenStartAndLengthAreValid_shouldAddHighlight() throws BadLocationException {
        searchableTextPane.addHighlight(0, 5);

        // Check if highlight is added successfully
        // ...
    }

    // Add more test cases for addHighlight method

    @Test
    public void testRemoveHighlights_shouldRemoveAllHighlights() {
        // Add some highlights to the text pane
        // ...

        searchableTextPane.removeHighlights();

        // Check if all highlights are removed
        // ...
    }

    @Test
    public void testSearch_whenTermIsNull_shouldReturnNegativeOne() {
        int searchPos = searchableTextPane.search(null);

        assertEquals(-1, searchPos);
    }

    @Test
    public void testSearch_whenTermIsEmpty_shouldReturnNegativeOne() {
        int searchPos = searchableTextPane.search("");

        assertEquals(-1, searchPos);
    }

    // Add more test cases for search method

    @Test
    public void testSearch_whenTermEqualsLastSearchTerm_shouldUpdateLastSearchPos() {
        // Set last search term to "hello"
        searchableTextPane.lastSearchTerm = "hello";

        // Set last search pos to 5
        searchableTextPane.lastSearchPos = 5;

        int searchPos = searchableTextPane.search("hello");

        assertEquals(10, searchPos);
        assertEquals("hello", searchableTextPane.lastSearchTerm);
        assertEquals(10, searchableTextPane.lastSearchPos);
    }

    // Add more test cases for search method

    @Test
    public void testConvertWords2Lines_shouldConvertWordsToLinesCorrectly() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 5));
        words.add(new WordResult(10, 15));
        // Add more word results

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);

        // Check if line results are converted correctly
        // ...
    }

    // Add more test cases for other private methods
    
}