
package ghm.follow.search;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Utilities;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        Component parent = null;
        ComponentUI ui = searchableTextPane.getUI();

        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());

        parent = new JPanel();
        parent.setSize(500, 500);
        assertEquals(500, parent.getSize().width);

        assertEquals(10, ui.getPreferredSize(searchableTextPane).width);

        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        String term = "searchTerm";
        int flags = 0;

        List<LineResult> lineResults = new ArrayList<LineResult>();

        lineResults = searchableTextPane.highlight(term, flags);

        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        int start = 0;
        int length = 10;

        searchableTextPane.addHighlight(start, length);

        assertEquals(1, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();

        assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch() {
        String term = "searchTerm";

        int pos = searchableTextPane.search(term);

        assertEquals(-1, pos);
    }

    @Test
    public void testSearchWithStartPos() {
        String term = "searchTerm";
        int startPos = 5;

        int pos = searchableTextPane.search(term, startPos);

        assertEquals(-1, pos);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        List<WordResult> words = new ArrayList<WordResult>();
        LineResult lineResult = new LineResult(0, 0, 10);

        List<LineResult> lines = new ArrayList<LineResult>();

        lines = searchableTextPane.convertWords2Lines(words);

        assertNotNull(lines);
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        WordResult wordResult = new WordResult(0, 5, null);
        LineResult lineResult = new LineResult(0, 0, 10);

        searchableTextPane.updateWordResult(wordResult, lineResult);

        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(1, wordResult.getParent().getLineNumber());
        assertEquals(0, wordResult.getLineOffset());
    }
}

