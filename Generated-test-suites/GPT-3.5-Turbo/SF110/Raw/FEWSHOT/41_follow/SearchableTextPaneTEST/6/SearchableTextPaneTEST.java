package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchableTextPaneTest {
    private SearchableTextPane textPane;
    private Font font;
    private int tabSize;

    @Before
    public void setUp() {
        font = new Font("Arial", Font.PLAIN, 12);
        tabSize = 4;
        textPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertEquals(true, textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        List<LineResult> lineResults = new ArrayList<>();
        assertEquals(lineResults, textPane.highlight(null, 0));

        lineResults = new ArrayList<>();
        lineResults.add(new LineResult(0, 0, 5));
        assertEquals(lineResults, textPane.highlight("hello", 0));

        lineResults = new ArrayList<>();
        lineResults.add(new LineResult(0, 0, 5));
        lineResults.add(new LineResult(1, 6, 11));
        assertEquals(lineResults, textPane.highlight("hello world", 0));

        lineResults = new ArrayList<>();
        lineResults.add(new LineResult(1, 6, 11));
        assertEquals(lineResults, textPane.highlight("world", 0));
    }

    @Test
    public void testRemoveHighlights() {
        textPane.removeHighlights();
        assertEquals(null, textPane.getHighlighter().getHighlights());
    }

    @Test
    public void testSearch() {
        assertEquals(-1, textPane.search(null));
        assertEquals(0, textPane.search(""));
        assertEquals(0, textPane.search("hello"));

        textPane.search("hello");
        assertEquals(6, textPane.search("world"));
        assertEquals(12, textPane.search("hello"));
    }

    @Test
    public void testSearchWithStartPosition() {
        assertEquals(-1, textPane.search(null, 0));
        assertEquals(0, textPane.search("", 0));
        assertEquals(0, textPane.search("hello", 0));

        textPane.search("hello");
        assertEquals(6, textPane.search("world", 0));
        assertEquals(12, textPane.search("hello", 0));
    }

    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = new ArrayList<>();
        List<LineResult> lineResults = new ArrayList<>();
        assertEquals(lineResults, textPane.convertWords2Lines(words));

        words.add(new WordResult(0, 5));
        lineResults = new ArrayList<>();
        lineResults.add(new LineResult(0, 0, 5));
        assertEquals(lineResults, textPane.convertWords2Lines(words));

        words.add(new WordResult(6, 11));
        lineResults = new ArrayList<>();
        lineResults.add(new LineResult(0, 0, 5));
        lineResults.add(new LineResult(1, 6, 11));
        assertEquals(lineResults, textPane.convertWords2Lines(words));

        words.add(new WordResult(6, 11));
        lineResults = new ArrayList<>();
        lineResults.add(new LineResult(0, 0, 5));
        lineResults.add(new LineResult(1, 6, 11));
        lineResults.add(new LineResult(1, 6, 11));
        assertEquals(lineResults, textPane.convertWords2Lines(words));
    }

    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(0, 0, 5);
        try {
            textPane.updateWordResult(wordResult, lineResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, wordResult.parent.lineNumber);
        assertEquals(0, wordResult.getLineOffset());
    }
}