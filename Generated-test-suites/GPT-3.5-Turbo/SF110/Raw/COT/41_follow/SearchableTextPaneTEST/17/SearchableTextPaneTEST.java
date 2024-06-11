import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.util.List;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    private SearchableTextPane testPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        testPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(testPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        List<LineResult> lineResults = testPane.highlight("text", 1);
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        testPane.setText("This is a sample text");
        testPane.addHighlight(0, 4);
        testPane.addHighlight(8, 11);
        testPane.addHighlight(15, 18);
        assertEquals(3, testPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testRemoveHighlights() {
        testPane.setText("This is a sample text");
        testPane.addHighlight(0, 4);
        testPane.addHighlight(8, 11);
        testPane.addHighlight(15, 18);
        testPane.removeHighlights();
        assertEquals(0, testPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch() {
        testPane.setText("This is a sample text");
        int pos = testPane.search("is");
        assertEquals(2, pos);
        pos = testPane.search("text");
        assertEquals(15, pos);
        pos = testPane.search("notfound");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchWithStartPos() {
        testPane.setText("This is a sample text");
        int pos = testPane.search("is", 3);
        assertEquals(5, pos);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        testPane.setText("This is a sample text");
        Element element = Utilities.getParagraphElement(testPane, 0);
        LineResult lineResult = new LineResult(0, element.getStartOffset(), element.getEndOffset());
        WordResult wordResult1 = new WordResult("This", 0, 4);
        WordResult wordResult2 = new WordResult("is", 5, 7);

        testPane.updateWordResult(wordResult1, lineResult);
        testPane.updateWordResult(wordResult2, lineResult);

        List<LineResult> lineResults = testPane.convertWords2Lines(List.of(wordResult1, wordResult2));
        assertEquals(1, lineResults.size());
        assertEquals(2, lineResults.get(0).getWordResults().size());
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        testPane.setText("This is a sample text");
        Element element = Utilities.getParagraphElement(testPane, 0);
        LineResult lineResult = new LineResult(0, element.getStartOffset(), element.getEndOffset());
        WordResult wordResult1 = new WordResult("This", 0, 4);

        testPane.updateWordResult(wordResult1, lineResult);
        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(1, wordResult1.parent.lineNumber);
        assertEquals(element.getStartOffset(), wordResult1.getLineOffset());
    }
}