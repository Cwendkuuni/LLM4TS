import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean scrollable = searchableTextPane.getScrollableTracksViewportWidth();
        // Test scrollable tracks viewport width functionality
        // ...

    }

    @Test
    public void testHighlight() {
        String term = "example";
        int flags = 1;
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
        // Test highlight functionality
        // ...

    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        int start = 10;
        int length = 5;
        searchableTextPane.addHighlight(start, length);
        // Test addHighlight functionality
        // ...

    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
        // Test removeHighlights functionality
        // ...

    }

    @Test
    public void testSearch() {
        String term = "search term";
        int position = searchableTextPane.search(term);
        // Test search functionality
        // ...

    }

    @Test
    public void testSearchWithStartPosition() {
        String term = "search term";
        int startPos = 10;
        int position = searchableTextPane.search(term, startPos);
        // Test search with startPosition functionality
        // ...

    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testHighlightWithEmptyTerm() {
        String term = "";
        int flags = 1;
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
        // Test highlight with empty term functionality
        // ...

    }

    @Test
    public void testAddHighlightInvalidLength() throws BadLocationException {
        int start = 10;
        int length = -5;
        // Test addHighlight with invalid length functionality
        // ...

    }

    @Test(expected = BadLocationException.class)
    public void testAddHighlightInvalidStart() throws BadLocationException {
        int start = -10;
        int length = 5;
        searchableTextPane.addHighlight(start, length);
        // Test addHighlight with invalid start position functionality
        // ...

    }

    @Test
    public void testSearchWithNullTerm() {
        String term = null;
        int position = searchableTextPane.search(term);
        // Test search with null term functionality
        // ...

    }

    @Test
    public void testSearchWithStartPositionOutOfBounds() {
        String term = "search term";
        int startPos = 1000;
        int position = searchableTextPane.search(term, startPos);
        // Test search with start position out of bounds functionality
        // ...

    }

    @Test(expected = BadLocationException.class)
    public void testConvertWords2LinesInvalidOffset() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        // Add words with invalid offset values
        searchableTextPane.convertWords2Lines(words);
        // Test convertWords2Lines with invalid offset values functionality
        // ...

    }

    // ... Add more test cases for all methods in the SearchableTextPane class

}



// Insert the code from the previous section here

