import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.List;

public class SearchableTextPaneTest {
    private SearchableTextPane textPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        textPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean result = textPane.getScrollableTracksViewportWidth();
        Assert.assertTrue(result);
    }

    @Test
    public void testHighlight() {
        String searchTerm = "test";
        int flags = 0;
        List<LineResult> lineResults = textPane.highlight(searchTerm, flags);
        Assert.assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        int start = 0;
        int length = 5;

        try {
            textPane.addHighlight(start, length);
        } catch (BadLocationException e) {
            Assert.fail("BadLocationException was thrown");
        }

        // TODO: Verify the highlight is added
    }

    @Test
    public void testRemoveHighlights() {
        // TODO: Add highlights and then test removing them
    }

    @Test
    public void testSearch() {
        String searchTerm = "test";
        int result = textPane.search(searchTerm);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testSearchWithStartPos() {
        String searchTerm = "test";
        int startPos = 5;
        int result = textPane.search(searchTerm, startPos);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines() {
        // TODO: Implement test for convertWords2Lines method
    }

    @Test
    public void testUpdateWordResult() {
        // TODO: Implement test for updateWordResult method
    }
}