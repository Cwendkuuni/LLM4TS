import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Mock
    private DefaultHighlighter.DefaultHighlightPainter wordPainter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.wordPainter = wordPainter;
    }

    @Test
    public void testGetScrollableTracksViewportWidthWithParentAndUI() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidthWithoutParent() {
        searchableTextPane.setParent(null);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidthWithExtendedWidth() {
        Component parent = mock(Component.class);
        when(parent.getSize()).thenReturn(new Dimension(100, 100));
        ComponentUI ui = mock(ComponentUI.class);
        when(ui.getPreferredSize(searchableTextPane)).thenReturn(new Dimension(200, 100));
        when(searchableTextPane.getParent()).thenReturn(parent);
        when(searchableTextPane.getUI()).thenReturn(ui);
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightWithNullTerm() {
        List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
        assertTrue(lineResults.isEmpty());
        verify(wordPainter, never()).addHighlight(anyInt(), anyInt());
    }

    @Test
    public void testHighlightWithEmptyTerm() {
        List<LineResult> lineResults = searchableTextPane.highlight("", 0);
        assertTrue(lineResults.isEmpty());
        verify(wordPainter, never()).addHighlight(anyInt(), anyInt());
    }

    @Test
    public void testHighlightWithValidTerm() throws BadLocationException {
        Document document = mock(Document.class);
        String text = "Lorem ipsum dolor sit amet";
        when(document.getLength()).thenReturn(text.length());
        when(document.getText(0, document.getLength())).thenReturn(text);
        searchableTextPane.setDocument(document);

        WordResult wordResult = new WordResult(6, 11);
        List<WordResult> searchResults = new ArrayList<>();
        searchResults.add(wordResult);
        SearchEngine searchEngine = mock(SearchEngine.class);
        when(searchEngine.search(anyString(), anyString())).thenReturn(searchResults);
        searchableTextPane.searchEngine = searchEngine;

        LineResult lineResult = new LineResult(0, 0, text.length());
        lineResult.addWord(wordResult);
        List<LineResult> expectedLineResults = new ArrayList<>();
        expectedLineResults.add(lineResult);

        List<LineResult> lineResults = searchableTextPane.highlight("ipsum", 0);
        assertEquals(expectedLineResults, lineResults);
        verify(wordPainter).addHighlight(6, 5);
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        Highlighter highlighter = mock(Highlighter.class);
        when(searchableTextPane.getHighlighter()).thenReturn(highlighter);

        searchableTextPane.addHighlight(10, 5);

        verify(highlighter).addHighlight(10, 15, wordPainter);
    }

    @Test
    public void testRemoveHighlights() {
        Highlighter highlighter = mock(Highlighter.class);
        when(searchableTextPane.getHighlighter()).thenReturn(highlighter);

        searchableTextPane.removeHighlights();

        verify(highlighter).removeAllHighlights();
    }

    @Test
    public void testSearchWithNullTerm() {
        int result = searchableTextPane.search(null);
        assertEquals(-1, result);
    }

    @Test
    public void testSearchWithEmptyTerm() {
        int result = searchableTextPane.search("");
        assertEquals(-1, result);
    }

    @Test
    public void testSearchWithSameTerm() {
        searchableTextPane.lastSearchTerm = "Lorem";
        searchableTextPane.lastSearchPos = 0;

        int result = searchableTextPane.search("Lorem");

        assertEquals(5, result);
        assertEquals(10, searchableTextPane.lastSearchPos);
        assertEquals("Lorem", searchableTextPane.lastSearchTerm);
    }

    @Test
    public void testSearchWithDifferentTerm() {
        searchableTextPane.lastSearchTerm = "Lorem";
        searchableTextPane.lastSearchPos = 0;

        int result = searchableTextPane.search("ipsum");

        assertEquals(6, result);
        assertEquals(6, searchableTextPane.lastSearchPos);
        assertEquals("ipsum", searchableTextPane.lastSearchTerm);
    }

    @Test
    public void testSearchWithInvalidPosition() throws BadLocationException {
        Document document = mock(Document.class);
        when(document.getLength()).thenReturn(0);
        searchableTextPane.setDocument(document);

        int result = searchableTextPane.search("Lorem", 5);

        assertEquals(-1, result);
    }

    @Test
    public void testSearchWithValidPosition() throws BadLocationException {
        Document document = mock(Document.class);
        String text = "Lorem ipsum dolor sit amet";
        when(document.getLength()).thenReturn(text.length());
        when(document.getText(0, document.getLength())).thenReturn(text);
        searchableTextPane.setDocument(document);

        int result = searchableTextPane.search("sit", 18);

        assertEquals(18, result);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        Document document = mock(Document.class);
        when(document.getLength()).thenReturn(0);
        searchableTextPane.setDocument(document);

        WordResult wordResult1 = new WordResult(6, 11);
        WordResult wordResult2 = new WordResult(18, 21);
        List<WordResult> words = new ArrayList<>();
        words.add(wordResult1);
        words.add(wordResult2);

        List<LineResult> expectedLineResults = new ArrayList<>();
        LineResult lineResult1 = new LineResult(0, 0, 11);
        lineResult1.addWord(wordResult1);
        LineResult lineResult2 = new LineResult(1, 12, 24);
        lineResult2.addWord(wordResult2);
        expectedLineResults.add(lineResult1);
        expectedLineResults.add(lineResult2);

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);

        assertEquals(expectedLineResults, lineResults);
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        WordResult wordResult = new WordResult(6, 11);
        LineResult lineResult = new LineResult(0, 0, 11);

        searchableTextPane.updateWordResult(wordResult, lineResult);

        assertEquals(7, wordResult.lineOffset);
        assertEquals(1, wordResult.parent.lineNumber);
    }
}