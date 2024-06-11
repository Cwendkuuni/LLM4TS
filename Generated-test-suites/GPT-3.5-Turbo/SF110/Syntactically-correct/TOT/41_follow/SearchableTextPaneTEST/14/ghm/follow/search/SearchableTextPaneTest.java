import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Typical use case
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());

        // Edge case with null parent
        searchableTextPane.setParent(null);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());

        // Edge case with smaller width
        JPanel parent = new JPanel();
        parent.setSize(200, 400);
        searchableTextPane.setParent(parent);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());

        // Edge case with larger width
        parent.setSize(800, 400);
        searchableTextPane.setParent(parent);
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        // Typical use case
        List<LineResult> lineResults = searchableTextPane.highlight("search", 1);
        assertEquals(0, lineResults.size());

        // Edge case with empty term
        lineResults = searchableTextPane.highlight("", 1);
        assertEquals(0, lineResults.size());
        
        // Error scenario with null term
        lineResults = searchableTextPane.highlight(null, 1);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() {
        // Typical use case
        try {
            searchableTextPane.addHighlight(0, 5);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Edge case with negative start
        try {
            searchableTextPane.addHighlight(-1, 5);
            fail("No exception thrown.");
        } catch (BadLocationException e) {
            // Exception expected
        }

        // Edge case with start larger than length
        try {
            searchableTextPane.addHighlight(100, 5);
            fail("No exception thrown.");
        } catch (BadLocationException e) {
            // Exception expected
        }
    }

    @Test
    public void testRemoveHighlights() {
        // Typical use case
        searchableTextPane.removeHighlights();

        // Edge case with highlights present
        try {
            searchableTextPane.addHighlight(0, 5);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        searchableTextPane.removeHighlights();
    }

    @Test
    public void testSearch() {
        // Typical use case
        int result = searchableTextPane.search("search");
        assertEquals(-1, result);

        // Edge case with empty term
        result = searchableTextPane.search("");
        assertEquals(-1, result);
        
        // Error scenario with null term
        result = searchableTextPane.search(null);
        assertEquals(-1, result);
    }

    @Test
    public void testSearchWithStartPos() {
        // Typical use case
        int result = searchableTextPane.search("search", 10);
        assertEquals(-1, result);

        // Edge case with empty term
        result = searchableTextPane.search("", 10);
        assertEquals(-1, result);
        
        // Error scenario with null term
        result = searchableTextPane.search(null, 10);
        assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines() {
        // Typical use case
        List<WordResult> words = new ArrayList<>();
        WordResult wordResult = new WordResult(0, 5);
        words.add(wordResult);
        try {
            List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
            assertEquals(1, lineResults.size());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        // Edge case with empty word list
        words = new ArrayList<>();
        try {
            List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
            assertEquals(0, lineResults.size());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateWordResult() {
        // Typical use case
        List<WordResult> words = new ArrayList<>();
        WordResult wordResult = new WordResult(0, 5);
        words.add(wordResult);
        LineResult lineResult = new LineResult(0, 0, 5);
        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);
            assertEquals(1, lineResult.getWordResults().size());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        // Edge case with empty word list
        words = new ArrayList<>();
        try {
            searchableTextPane.updateWordResult(null, lineResult);
            fail("No exception thrown.");
        } catch (BadLocationException e) {
            // Exception expected
        }
    }
}