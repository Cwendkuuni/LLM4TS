
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {
    private SearchableTextPane textPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        textPane = new SearchableTextPane(font, 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean tracksViewportWidth = textPane.getScrollableTracksViewportWidth();
        Assert.assertTrue(tracksViewportWidth);
    }

    @Test
    public void testHighlight() {
        String term = "example";
        int flags = 0;
        List<LineResult> lineResults = textPane.highlight(term, flags);
        Assert.assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        int start = 0;
        int length = 10;
        textPane.addHighlight(start, length);
        // Assert that no exception is thrown
    }

    @Test
    public void testRemoveHighlights() {
        textPane.removeHighlights();
        // Assert that no exception is thrown
    }

    @Test
    public void testSearch() {
        String term = "example";
        int startPos = 0;
        int pos = textPane.search(term, startPos);
        Assert.assertEquals(-1, pos);
    }

    @Test
    public void testSearchWithLastSearchTerm() {
        textPane.search("example");
        String term = "example";
        int pos = textPane.search(term);
        Assert.assertEquals(-1, pos);
    }

    @Test
    public void testSearchWithInvalidTerm() {
        String term = "";
        int pos = textPane.search(term);
        Assert.assertEquals(-1, pos);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        List<WordResult> words = List.of(
                new WordResult(0, 5),
                new WordResult(10, 15)
        );
        List<LineResult> lineResults = textPane.convertWords2Lines(words);
        Assert.assertNotNull(lineResults);
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(0, 0, 10);
        textPane.updateWordResult(wordResult, lineResult);
        // Assert that no exception is thrown
    }
}