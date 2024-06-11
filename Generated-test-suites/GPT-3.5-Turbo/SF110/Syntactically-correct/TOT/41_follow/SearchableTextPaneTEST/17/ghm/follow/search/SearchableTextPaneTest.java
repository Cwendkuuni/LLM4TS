import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;
import org.junit.Test;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }

    @After
    public void tearDown() {
        searchableTextPane = null;
    }

    @Test
    public void testGetScrollableTracksViewportWidthWithNullParent() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidthWithParent() {
        JPanel parent = new JPanel();
        parent.setSize(new Dimension(200, 200));
        parent.add(searchableTextPane);

        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidthWithNarrowUI() {
        JPanel parent = new JPanel();
        parent.setSize(new Dimension(100, 200));
        parent.add(searchableTextPane);

        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightWithEmptyTerm() {
        List<LineResult> lineResults = searchableTextPane.highlight("", 0);

        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlightWithTermNotFound() {
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);

        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlightWithSingleLineMatch() {
        searchableTextPane.setText("This is a test string.");

        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);

        assertEquals(1, lineResults.size());
        assertEquals("This is a test string.", lineResults.get(0).getLineText());
        assertEquals(76, lineResults.get(0).getWordResults().get(0).start);
        assertEquals(80, lineResults.get(0).getWordResults().get(0).end);
    }

    @Test
    public void testHighlightWithMultiLineMatch() {
        searchableTextPane.setText("This is\na test\nstring.");

        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);

        assertEquals(1, lineResults.size());
        assertEquals("a test", lineResults.get(0).getLineText());
        assertEquals(2, lineResults.get(0).getLineNumber());
        assertEquals(2, lineResults.get(0).getWordResults().size());
        assertEquals(2, lineResults.get(0).getWordResults().get(0).getLineNumber());
        assertEquals("test", lineResults.get(0).getWordResults().get(0).getWordText());
        assertEquals(2, lineResults.get(0).getWordResults().get(1).getLineNumber());
        assertEquals("test", lineResults.get(0).getWordResults().get(1).getWordText());
    }

    @Test
    public void testHighlightWithCaseSensitiveMatching() {
        searchableTextPane.setText("This is a test string.");

        List<LineResult> lineResults = searchableTextPane.highlight("TEST", 0);

        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        searchableTextPane.setText("This is a test string.");

        searchableTextPane.addHighlight(10, 4);
        Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();

        assertEquals(1, highlights.length);
        assertEquals(10, highlights[0].getStartOffset());
        assertEquals(14, highlights[0].getEndOffset());
    }

    @Test
    public void testRemoveHighlights() throws BadLocationException {
        searchableTextPane.setText("This is a test string.");

        searchableTextPane.addHighlight(10, 4);
        searchableTextPane.removeHighlights();
        Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();

        assertEquals(0, highlights.length);
    }

    @Test
    public void testSearchWithEmptyTerm() {
        int pos = searchableTextPane.search("");

        assertEquals(-1, pos);
        assertNull(searchableTextPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithNotFoundTerm() {
        int pos = searchableTextPane.search("test");

        assertEquals(-1, pos);
        assertNull(searchableTextPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithExistingTerm() {
        searchableTextPane.setText("This is a test string.");

        int pos = searchableTextPane.search("test");

        assertEquals(10, pos);
        assertEquals("test", searchableTextPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithExistingTermAndStartPosition() {
        searchableTextPane.setText("This is a test test string.");
        searchableTextPane.search("test");

        int pos = searchableTextPane.search("test");

        assertEquals(15, pos);
        assertEquals("test", searchableTextPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithExistingTermAndStartPositionOutOfRange() {
        searchableTextPane.setText("This is a test test string.");

        int pos = searchableTextPane.search("test", 100);

        assertEquals(-1, pos);
        assertNull(searchableTextPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithBadLocationException() {
        searchableTextPane.setText("This is a test test string.");
        searchableTextPane.getDocument().setCharacterAttributes(0, searchableTextPane.getDocument().getLength(), new SimpleAttributeSet(), false);

        int pos = searchableTextPane.search("test");

        assertEquals(-1, pos);
        assertNull(searchableTextPane.getLastSearchTerm());
    }

    @Test
    public void testConvertWords2LinesWithEmptyList() throws BadLocationException {
        ArrayList<WordResult> words = new ArrayList<>();

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);

        assertEquals(0, lineResults.size());
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        ArrayList<WordResult> words = new ArrayList<>();
        words.add(new WordResult(10, 14));
        words.add(new WordResult(20, 24));
        words.add(new WordResult(30, 34));

        searchableTextPane.setText("This is a test string.");

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);

        assertEquals(1, lineResults.size());
        assertEquals("This is a test string.", lineResults.get(0).getLineText());
        assertEquals(10, lineResults.get(0).getWordResults().get(0).start);
        assertEquals(14, lineResults.get(0).getWordResults().get(0).end);
        assertEquals(0, lineResults.get(0).getLineNumber());
        assertEquals(34, lineResults.get(0).getWordResults().get(2).end);
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        LineResult lineResult = new LineResult(1, 10, 20);
        WordResult wordResult = new WordResult(10, 14);

        searchableTextPane.setText("This is a test string.");
        searchableTextPane.updateWordResult(wordResult, lineResult);

        assertEquals(10, lineResult.getWordResults().get(0).start);
        assertEquals(14, lineResult.getWordResults().get(0).end);
        assertEquals(1, lineResult.getWordResults().get(0).getLineNumber());
        assertEquals(10, lineResult.getWordResults().get(0).getLineOffset());
    }

}