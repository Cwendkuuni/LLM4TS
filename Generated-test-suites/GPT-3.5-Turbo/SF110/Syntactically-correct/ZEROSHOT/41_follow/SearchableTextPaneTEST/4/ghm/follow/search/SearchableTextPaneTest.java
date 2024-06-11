import org.junit.*;
import java.awt.*;
import javax.swing.text.*;
import java.util.*;
import javax.swing.plaf.*;
import java.util.logging.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        searchableTextPane = new SearchableTextPane(font, 4);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        Component parent = searchableTextPane.getParent();
        ComponentUI ui = searchableTextPane.getUI();

        boolean result = searchableTextPane.getScrollableTracksViewportWidth();

        if(parent == null || ui.getPreferredSize(searchableTextPane).width <= parent.getSize().width) {
            Assert.assertTrue(result);
        } else {
            Assert.assertFalse(result);
        }
    }

    @Test
    public void testHighlight() {
        String term = "search";
        int flags = 0;

        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);

        Assert.assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() {
        int start = 0;
        int length = 5;

        try {
            searchableTextPane.addHighlight(start, length);
        } catch (BadLocationException e) {
            Assert.fail("BadLocationException thrown");
        }

        Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        Assert.assertEquals(1, highlights.length);
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.addHighlight(0, 5);
        searchableTextPane.removeHighlights();

        Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        Assert.assertEquals(0, highlights.length);
    }

    @Test
    public void testSearch() {
        String term = "search";

        int result = searchableTextPane.search(term);

        Assert.assertEquals(-1, result);
    }

    @Test
    public void testSearchWithStartPosition() {
        String term = "search";
        int startPos = 5;

        int result = searchableTextPane.search(term, startPos);

        Assert.assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = new ArrayList<>();
        WordResult word = new WordResult(0, 5);
        words.add(word);

        try {
            List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);

            Assert.assertEquals(1, lineResults.size());
        } catch (BadLocationException e) {
            Assert.fail("BadLocationException thrown");
        }
    }

    @Test
    public void testUpdateWordResult() {
        LineResult lineResult = new LineResult(0, 0, 10);
        WordResult wordResult = new WordResult(0, 5);

        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);

            Assert.assertEquals(1, lineResult.getWordResults().size());
            Assert.assertEquals(1, wordResult.parent.lineNumber);
            Assert.assertEquals(0, wordResult.getLineOffset());
        } catch (BadLocationException e) {
            Assert.fail("BadLocationException thrown");
        }
    }
}