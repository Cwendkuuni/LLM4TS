import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;
    private Font font;
    private int tabSize;

    @Before
    public void setUp() {
        font = new Font("Arial", Font.PLAIN, 12);
        tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        boolean tracksViewportWidth = searchableTextPane.getScrollableTracksViewportWidth();
        assertFalse(tracksViewportWidth);
    }

    @Test
    public void testHighlight() {
        String searchTerm = "test";
        int flags = 0;
        java.util.List<LineResult> lineResults = searchableTextPane.highlight(searchTerm, flags);
        assertNotNull(lineResults);
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        int start = 0;
        int length = 5;
        searchableTextPane.addHighlight(start, length);
        Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertEquals(1, highlights.length);
        
        Highlighter.Highlight highlight = highlights[0];
        assertEquals(start, highlight.getStartOffset());
        assertEquals(start + length, highlight.getEndOffset());
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
        Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertEquals(0, highlights.length);
    }

    @Test
    public void testSearch() {
        String searchTerm = "test";
        int searchPos = searchableTextPane.search(searchTerm);
        assertEquals(-1, searchPos);
    }

    @Test
    public void testSearchWithStartPos() {
        String searchTerm = "test";
        int startPos = 0;
        int searchPos = searchableTextPane.search(searchTerm, startPos);
        assertEquals(-1, searchPos);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        java.util.List<WordResult> words = new java.util.ArrayList<WordResult>();
        WordResult word = new WordResult(0, 5);
        words.add(word);
        java.util.List<LineResult> lines = searchableTextPane.convertWords2Lines(words);
        assertEquals(1, lines.size());
        
        LineResult line = lines.get(0);
        assertEquals(0, line.getLine());
        assertEquals(0, line.getLineStart());
        assertEquals(1, line.getLineEnd());
        
        java.util.List<WordResult> lineWords = line.getWordResults();
        assertEquals(1, lineWords.size());
        assertEquals(word, lineWords.get(0));
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(0, 0, 1);
        searchableTextPane.updateWordResult(wordResult, lineResult);

        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(wordResult, lineResult.getWordResults().get(0));
        
        int lineNumber = lineResult.getLineNumber();
        assertEquals(1, lineNumber);
        
        int lineOffset = wordResult.getLineOffset();
        assertEquals(0, lineOffset);
    }
}