
import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Font;
import java.util.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertFalse(pane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() throws BadLocationException {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = pane.highlight("test", 0);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        pane.getDocument().insertString(0, "Testing addHighlight method", null);
        pane.addHighlight(8, 14);
        assertEquals(1, pane.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testRemoveHighlights() throws BadLocationException {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        pane.getDocument().insertString(0, "Testing removeHighlights method", null);
        pane.addHighlight(8, 14);
        pane.removeHighlights();
        assertEquals(0, pane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch() {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        int pos = pane.search("test");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchWithStartPos() throws BadLocationException {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        pane.getDocument().insertString(0, "Testing search method with start position", null);
        int pos = pane.search("test", 8);
        assertEquals(29, pos);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<WordResult> words = new ArrayList<>();
        WordResult word1 = new WordResult(0, 5);
        WordResult word2 = new WordResult(10, 15);
        words.add(word1);
        words.add(word2);
        List<LineResult> lines = pane.convertWords2Lines(words);
        assertNotNull(lines);
        assertEquals(1, lines.size());
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        LineResult lineResult = new LineResult(0, 0, 10);
        WordResult wordResult = new WordResult(5, 8);
        pane.updateWordResult(wordResult, lineResult);
        assertEquals(1, lineResult.getWordResults().size());
    }
}
