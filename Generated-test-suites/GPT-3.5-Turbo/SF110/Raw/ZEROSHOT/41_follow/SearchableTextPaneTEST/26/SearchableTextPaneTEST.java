import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Mock
    private Document document;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Logger logger = Logger.getLogger(SearchableTextPane.class.getName());
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        searchableTextPane.setDocument(document);
        searchableTextPane.log = logger;
    }

    @Test
    public void getScrollableTracksViewportWidth_parentNull_returnsTrue() {
        Component parent = null;
        ComponentUI ui = mock(ComponentUI.class);
        when(searchableTextPane.getParent()).thenReturn(parent);
        when(searchableTextPane.getUI()).thenReturn(ui);
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertEquals(true, result);
    }

    @Test
    public void getScrollableTracksViewportWidth_uiPreferredWidthLessThanParentWidth_returnsTrue() {
        Component parent = mock(Component.class);
        ComponentUI ui = mock(ComponentUI.class);
        when(searchableTextPane.getParent()).thenReturn(parent);
        when(searchableTextPane.getUI()).thenReturn(ui);
        when(ui.getPreferredSize(searchableTextPane)).thenReturn(new Dimension(100, 200));
        when(parent.getSize()).thenReturn(new Dimension(300, 200));
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertEquals(true, result);
    }

    @Test
    public void getScrollableTracksViewportWidth_uiPreferredWidthGreaterThanParentWidth_returnsFalse() {
        Component parent = mock(Component.class);
        ComponentUI ui = mock(ComponentUI.class);
        when(searchableTextPane.getParent()).thenReturn(parent);
        when(searchableTextPane.getUI()).thenReturn(ui);
        when(ui.getPreferredSize(searchableTextPane)).thenReturn(new Dimension(400, 200));
        when(parent.getSize()).thenReturn(new Dimension(300, 200));
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertEquals(false, result);
    }

    @Test
    public void highlight_termNull_noHighlightsAdded() throws BadLocationException {
        String term = null;
        int flags = 0;
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
        verify(searchableTextPane, never()).removeHighlights();
        assertEquals(0, lineResults.size());
    }

    @Test
    public void highlight_termEmpty_noHighlightsAdded() throws BadLocationException {
        String term = "";
        int flags = 0;
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
        verify(searchableTextPane, never()).removeHighlights();
        assertEquals(0, lineResults.size());
    }

    @Test
    public void highlight_termValidHighlightsAdded() throws BadLocationException {
        String term = "test";
        int flags = 0;
        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
        verify(searchableTextPane).removeHighlights();
        assertEquals(0, lineResults.size());
    }

    @Test
    public void addHighlight_validStartAndLength_highlightAdded() throws BadLocationException {
        int start = 0;
        int length = 5;
        DefaultHighlighter.DefaultHighlightPainter wordPainter = mock(DefaultHighlighter.DefaultHighlightPainter.class);
        searchableTextPane.wordPainter = wordPainter;
        searchableTextPane.addHighlight(start, length);
        verify(searchableTextPane.getHighlighter()).addHighlight(start, start + length, wordPainter);
    }

    @Test
    public void removeHighlights_allHighlightsRemoved() {
        searchableTextPane.removeHighlights();
        verify(searchableTextPane.getHighlighter()).removeAllHighlights();
    }

    @Test
    public void search_samelastSearchTerm_searchStartPositionUpdated() {
        String term = "test";
        searchableTextPane.lastSearchTerm = term;
        searchableTextPane.lastSearchPos = 5;
        int result = searchableTextPane.search(term);
        assertEquals(10, result);
    }

    @Test
    public void search_differentlastSearchTerm_searchStartPositionReset() {
        String term = "test";
        searchableTextPane.lastSearchTerm = "different";
        int result = searchableTextPane.search(term);
        assertEquals(0, result);
    }

    @Test
    public void search_termValid_searchStartPositionReturned() {
        String term = "test";
        int result = searchableTextPane.search(term);
        assertEquals(0, result);
    }

    @Test
    public void search_BadLocationException_searchStartPositionErrorLogged() {
        String term = "test";
        when(document.getText(0, document.getLength())).thenThrow(new BadLocationException("Test", 0));
        int result = searchableTextPane.search(term);
        verify(searchableTextPane.log).log(Level.WARNING, "BadLocationException in SearchableTextPane", any(BadLocationException.class));
        assertEquals(-1, result);
    }

    @Test
    public void convertWords2Lines_wordsValid_linesConverted() throws BadLocationException {
        List<WordResult> words = mock(List.class);
        List<LineResult> result = searchableTextPane.convertWords2Lines(words);
        assertEquals(0, result.size());
    }

    @Test
    public void updateWordResult_wordResultAndLineResultValid_wordResultUpdatedAndLineOffsetSet() throws BadLocationException {
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(0, 0, 10);
        searchableTextPane.updateWordResult(wordResult, lineResult);
        verify(lineResult).addWord(wordResult);
        assertEquals(1, wordResult.parent.lineNumber);
        assertEquals(0, wordResult.getLineOffset());
    }

}