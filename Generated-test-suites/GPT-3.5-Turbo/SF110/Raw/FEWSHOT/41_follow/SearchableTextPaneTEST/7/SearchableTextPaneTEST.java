package ghm.follow.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SearchableTextPaneTest {
    
    private SearchableTextPane searchableTextPane;
    private Font font;
    private int tabSize;
    private Logger log;
    private DefaultHighlighter.DefaultHighlightPainter wordPainter;
    private Document document;
    private SearchEngine searchEngine;
    private Highlighter highlighter;
    private Element element;

    @BeforeEach
    public void setUp() {
        font = mock(Font.class);
        tabSize = 4;
        log = mock(Logger.class);
        wordPainter = mock(DefaultHighlighter.DefaultHighlightPainter.class);
        searchableTextPane = new SearchableTextPane(font, tabSize);
        searchableTextPane.log = log;
        searchableTextPane.wordPainter = wordPainter;

        document = mock(Document.class);
        searchEngine = mock(SearchEngine.class);
        highlighter = mock(Highlighter.class);
        element = mock(Element.class);

        when(searchableTextPane.getDocument()).thenReturn(document);
        when(searchableTextPane.getHighlighter()).thenReturn(highlighter);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withoutParentUI_shouldReturnTrue() {
        Component parent = null;
        ComponentUI ui = mock(ComponentUI.class);

        when(searchableTextPane.getParent()).thenReturn(parent);
        when(searchableTextPane.getUI()).thenReturn(ui);
        when(ui.getPreferredSize(searchableTextPane)).thenReturn(new Dimension(100, 50));
        when(parent.getSize()).thenReturn(new Dimension(100, 100));

        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentUI_shouldReturnFalse() {
        Component parent = mock(Component.class);
        ComponentUI ui = mock(ComponentUI.class);

        when(searchableTextPane.getParent()).thenReturn(parent);
        when(searchableTextPane.getUI()).thenReturn(ui);
        when(ui.getPreferredSize(searchableTextPane)).thenReturn(new Dimension(200, 50));
        when(parent.getSize()).thenReturn(new Dimension(100, 100));

        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight_noTerm_shouldReturnEmptyListAndNotCallMethods() throws BadLocationException {
        String term = null;
        int flags = 0;

        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);

        assertTrue(lineResults.isEmpty());
        verify(searchableTextPane, never()).removeHighlights();
        verify(document, never()).getText(anyInt(), anyInt());
        verify(searchEngine, never()).search(anyString(), anyString());
        verifyZeroInteractions(wordPainter);
    }

    @Test
    public void testHighlight_emptyTerm_shouldReturnEmptyListAndNotCallMethods() throws BadLocationException {
        String term = "";
        int flags = 0;

        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);

        assertTrue(lineResults.isEmpty());
        verify(searchableTextPane, never()).removeHighlights();
        verify(document, never()).getText(anyInt(), anyInt());
        verify(searchEngine, never()).search(anyString(), anyString());
        verifyZeroInteractions(wordPainter);
    }

    @Test
    public void testHighlight_withTerm_shouldReturnLineResultsAndCallMethodsInOrder() throws BadLocationException {
        String term = "search";
        int flags = 0;

        List<WordResult> wordResults = new ArrayList<>();
        wordResults.add(new WordResult("search", 10, 15));
        wordResults.add(new WordResult("search", 20, 25));

        List<LineResult> lineResults = new ArrayList<>();
        lineResults.add(new LineResult(1, 0, 20));
        lineResults.add(new LineResult(2, 21, 40));

        when(document.getText(0, document.getLength())).thenReturn("Test document");
        when(searchEngine.search(term, "Test document")).thenReturn(wordResults);
        when(searchableTextPane.convertWords2Lines(wordResults)).thenReturn(lineResults);

        List<LineResult> result = searchableTextPane.highlight(term, flags);

        assertEquals(lineResults, result);
        verify(searchableTextPane).removeHighlights();
        verify(document).getText(0, document.getLength());
        verify(searchEngine).search(term, "Test document");
        verify(wordPainter, times(2)).addHighlight(anyInt(), anyInt(), eq(searchableTextPane));
        verifyZeroInteractions(log);
    }

    @Test
    public void testHighlight_withException_shouldLogErrorAndReturnEmptyList() throws BadLocationException {
        String term = "search";
        int flags = 0;

        when(document.getText(0, document.getLength())).thenThrow(new BadLocationException("Test exception", 0));

        List<LineResult> result = searchableTextPane.highlight(term, flags);

        assertTrue(result.isEmpty());
        verify(log).log(eq(Level.SEVERE), eq("BadLocationException in SearchableTextPane"), any(BadLocationException.class));
    }

    @Test
    public void testAddHighlight_withValidInputs_shouldAddHighlight() throws BadLocationException {
        int start = 10;
        int length = 5;

        searchableTextPane.addHighlight(start, length);

        verify(highlighter).addHighlight(start, start + length, wordPainter);
    }

    @Test
    public void testRemoveHighlights_shouldRemoveAllHighlights() {
        searchableTextPane.removeHighlights();

        verify(highlighter).removeAllHighlights();
    }

    @Test
    public void testSearch_validTerm_shouldReturnPositionAndUpdateLastSearchPos() {
        String term = "search";
        int startPos = 0;

        int result = searchableTextPane.search(term, startPos);

        assertEquals(10, result);
        assertEquals(10 + term.length(), searchableTextPane.lastSearchPos);
        assertEquals(term, searchableTextPane.lastSearchTerm);
    }

    @Test
    public void testSearch_validTermWithStartPos_shouldReturnPositionAndUpdateLastSearchPos() {
        String term = "search";
        int startPos = 5;

        searchableTextPane.lastSearchPos = 5;
        searchableTextPane.lastSearchTerm = "old";

        int result = searchableTextPane.search(term, startPos);

        assertEquals(10, result);
        assertEquals(10 + term.length(), searchableTextPane.lastSearchPos);
        assertEquals(term, searchableTextPane.lastSearchTerm);
    }

    @Test
    public void testSearch_emptyTerm_shouldSetLastSearchPosToNegativeAndLastSearchTermToNull() {
        String term = "";

        searchableTextPane.lastSearchPos = 5;
        searchableTextPane.lastSearchTerm = "old";

        int result = searchableTextPane.search(term);

        assertEquals(-1, result);
        assertEquals(-1, searchableTextPane.lastSearchPos);
        assertNull(searchableTextPane.lastSearchTerm);
    }

    @Test
    public void testSearch_nullTerm_shouldSetLastSearchPosToNegativeAndLastSearchTermToNull() {
        String term = null;

        int result = searchableTextPane.search(term);

        assertEquals(-1, result);
        assertEquals(-1, searchableTextPane.lastSearchPos);
        assertNull(searchableTextPane.lastSearchTerm);
    }

    @Test
    public void testSearch_withException_shouldLogErrorAndReturnNegativePosition() {
        String term = "search";
        int startPos = 5;

        when(document.getText(0, document.getLength())).thenThrow(new BadLocationException("Test exception", 0));

        int result = searchableTextPane.search(term, startPos);

        assertEquals(-1, result);
        assertEquals(-1, searchableTextPane.lastSearchPos);
        verify(log).log(eq(Level.WARNING), eq("BadLocationException in SearchableTextPane"), any(BadLocationException.class));
    }

    @Test
    public void testConvertWords2Lines_withWords_shouldReturnLineResults() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult("search", 10, 15));
        words.add(new WordResult("search", 20, 25));
        words.add(new WordResult("other", 30, 35));

        when(searchableTextPane.getLineOfOffset(10)).thenReturn(0);
        when(searchableTextPane.getLineOfOffset(20)).thenReturn(1);
        when(searchableTextPane.getLineOfOffset(30)).thenReturn(1);
        when(searchableTextPane.getLineStartOffset(0)).thenReturn(0);
        when(searchableTextPane.getLineStartOffset(1)).thenReturn(20);
        when(Utilities.getParagraphElement(searchableTextPane, 10)).thenReturn(element);
        when(Utilities.getParagraphElement(searchableTextPane, 20)).thenReturn(element);
        when(Utilities.getParagraphElement(searchableTextPane, 30)).thenReturn(element);
        when(element.getStartOffset()).thenReturn(0, 20);
        when(element.getEndOffset()).thenReturn(20, 40);

        List<LineResult> result = searchableTextPane.convertWords2Lines(words);

        assertEquals(2, result.size());
        assertEquals(0, result.get(0).getLineNumber());
        assertEquals(20, result.get(1).getLineNumber());
        verify(searchableTextPane).getLineOfOffset(10);
        verify(searchableTextPane).getLineOfOffset(20);
        verify(searchableTextPane).getLineOfOffset(30);
        verify(searchableTextPane).getLineStartOffset(0);
        verify(searchableTextPane).getLineStartOffset(1);
        verify(Utilities, times(3)).getParagraphElement(searchableTextPane, anyInt());
        verify(element, times(2)).getStartOffset();
        verify(element, times(2)).getEndOffset();
    }

    @Test
    public void testUpdateWordResult_withValidInputs_shouldAddWordAndUpdateLineNumber() throws BadLocationException {
        WordResult wordResult = new WordResult("search", 10, 15);
        LineResult lineResult = mock(LineResult.class);

        when(searchableTextPane.getLineOfOffset(10)).thenReturn(1);
        when(searchableTextPane.getLineStartOffset(1)).thenReturn(20);

        searchableTextPane.updateWordResult(wordResult, lineResult);

        assertEquals(2, wordResult.parent.lineNumber);
        verify(lineResult).addWord(wordResult);
        verify(searchableTextPane).getLineOfOffset(10);
        verify(searchableTextPane).getLineStartOffset(1);
    }

    @Test
    public void testUpdateWordResult_withException_shouldThrowException() throws BadLocationException {
        WordResult wordResult = new WordResult("search", 10, 15);
        LineResult lineResult = mock(LineResult.class);

        when(searchableTextPane.getLineOfOffset(10)).thenThrow(new BadLocationException("Test exception", 0));

        assertThrows(BadLocationException.class, () -> searchableTextPane.updateWordResult(wordResult, lineResult));
        assertEquals(0, wordResult.parent.lineNumber);
        verify(lineResult, never()).addWord(any(WordResult.class));
        verify(searchableTextPane).getLineOfOffset(10);
        verify(searchableTextPane, never()).getLineStartOffset(anyInt());
    }
}