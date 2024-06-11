package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

    private SearchableTextPane pane;

    @Before
    public void setup() {
        pane = new SearchableTextPane(null, 0);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(pane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() throws BadLocationException {
        String term = "example";
        int flags = 0;
        List<LineResult> lineResults = pane.highlight(term, flags);
        assertTrue(lineResults.isEmpty());

        pane.setText("This is an example");
        lineResults = pane.highlight(term, flags);

        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLine());
        assertEquals(11, lineResult.getLineStart());
        assertEquals(21, lineResult.getLineEnd());

        List<WordResult> wordResults = lineResult.getWordResults();
        assertEquals(1, wordResults.size());
        WordResult wordResult = wordResults.get(0);
        assertEquals(11, wordResult.getStart());
        assertEquals(18, wordResult.getEnd());
        assertEquals(1, wordResult.getParent().getLineNumber());

    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        pane.setText("This is an example");

        Document doc = pane.getDocument();
        assertEquals(0, pane.getHighlighter().getHighlights().length);

        pane.addHighlight(0, 4);
        assertEquals(1, pane.getHighlighter().getHighlights().length);

        pane.addHighlight(8, 9);
        assertEquals(2, pane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testRemoveHighlights() throws BadLocationException {
        pane.setText("This is an example");

        Document doc = pane.getDocument();
        pane.addHighlight(0, 4);
        pane.addHighlight(8, 9);
        assertEquals(2, pane.getHighlighter().getHighlights().length);

        pane.removeHighlights();
        assertEquals(0, pane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch() throws BadLocationException {
        String term = "example";
        assertNull(pane.getLastSearchTerm());

        int pos = pane.search(term);
        assertEquals(-1, pos);

        pane.setText("This is an example");
        pos = pane.search(term);
        assertEquals(11, pos);
        assertEquals(term, pane.getLastSearchTerm());

        pos = pane.search(term);
        assertEquals(11 + term.length(), pos);
        assertEquals(term, pane.getLastSearchTerm());

        term = "nonexistent";
        pos = pane.search(term);
        assertEquals(-1, pos);
        assertEquals(term, pane.getLastSearchTerm());
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        List<LineResult> lines;

        words.add(new WordResult(0, 3, new LineResult(0, 0, 4)));
        words.add(new WordResult(7, 10, new LineResult(0, 0, 4)));
        words.add(new WordResult(10, 13, new LineResult(1, 5, 15)));

        lines = pane.convertWords2Lines(words);
        assertEquals(2, lines.size());

        LineResult lineResult = lines.get(0);
        assertEquals(0, lineResult.getLine());
        assertEquals(0, lineResult.getLineStart());
        assertEquals(4, lineResult.getLineEnd());
        assertEquals(2, lineResult.getWordResults().size());

        lineResult = lines.get(1);
        assertEquals(1, lineResult.getLine());
        assertEquals(5, lineResult.getLineStart());
        assertEquals(15, lineResult.getLineEnd());
        assertEquals(1, lineResult.getWordResults().size());
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        LineResult lineResult = new LineResult(0, 0, 10);
        WordResult wordResult = new WordResult(2, 4, lineResult);

        pane.updateWordResult(wordResult, lineResult);
        assertEquals(1, lineResult.getWordResults().size());

        assertEquals(1, wordResult.getParent().getLineNumber());
        assertEquals(0, wordResult.getLineOffset());
    }
}