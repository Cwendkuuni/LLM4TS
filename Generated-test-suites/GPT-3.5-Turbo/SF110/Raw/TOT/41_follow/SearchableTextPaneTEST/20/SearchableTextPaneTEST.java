package ghm.follow.search;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Utilities;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        searchableTextPane = new SearchableTextPane(font, 4);
        searchableTextPane.setText("This is a test text.\nHello, World!");
    }

    @After
    public void tearDown() {
        searchableTextPane = null;
    }

    @Test
    public void testGetScrollableTracksViewportWidth_whenGreaterThanParent_shouldReturnFalse() {
        // Arrange
        JPanel parent = new JPanel();
        parent.setPreferredSize(new Dimension(100, 100));
        searchableTextPane.setParent(parent);

        // Act
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_whenLessThanParent_shouldReturnTrue() {
        // Arrange
        JPanel parent = new JPanel();
        parent.setPreferredSize(new Dimension(500, 100));
        searchableTextPane.setParent(parent);

        // Act
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_whenEqualParent_shouldReturnTrue() {
        // Arrange
        JPanel parent = new JPanel();
        parent.setPreferredSize(new Dimension(500, 100));
        searchableTextPane.setParent(parent);

        // Act
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHighlight_whenTermIsNull_shouldReturnEmptyLineResults() {
        // Act
        List<LineResult> lineResults = searchableTextPane.highlight(null, 0);

        // Assert
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlight_whenTermIsEmpty_shouldReturnEmptyLineResults() {
        // Act
        List<LineResult> lineResults = searchableTextPane.highlight("", 0);

        // Assert
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlight_whenTermDoesNotExistInText_shouldReturnEmptyLineResults() {
        // Act
        List<LineResult> lineResults = searchableTextPane.highlight("apple", 0);

        // Assert
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlight_whenTermExistsInText_shouldReturnLineResultsWithCorrectWordResults() {
        // Act
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);

        // Assert
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLine());
        List<WordResult> wordResults = lineResult.getWordResults();
        assertEquals(1, wordResults.size());
        WordResult wordResult = wordResults.get(0);
        assertEquals(10, wordResult.getStart());
        assertEquals(14, wordResult.getEnd());
    }

    @Test
    public void testAddHighlight_whenStartAndLengthAreValid_shouldAddHighlightToHighlighter() throws BadLocationException {
        // Arrange
        DefaultHighlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

        // Act
        searchableTextPane.addHighlight(20, 5);
        Object[] highlights = searchableTextPane.getHighlighter().getHighlights();

        // Assert
        assertEquals(1, highlights.length);
        DefaultHighlighter.DefaultHighlight painterFromHighlighter = (DefaultHighlighter.DefaultHighlight) highlights[0];
        assertEquals(20, painterFromHighlighter.getStartOffset());
        assertEquals(25, painterFromHighlighter.getEndOffset());
        assertEquals(painter, painterFromHighlighter.getPainter());
    }

    @Test
    public void testRemoveHighlights_shouldRemoveAllHighlightsFromHighlighter() {
        // Arrange
        DefaultHighlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        searchableTextPane.getHighlighter().addHighlight(20, 25, painter);

        // Act
        searchableTextPane.removeHighlights();
        Object[] highlights = searchableTextPane.getHighlighter().getHighlights();

        // Assert
        assertEquals(0, highlights.length);
    }

    @Test
    public void testSearch_whenTermIsNull_shouldReturnLastSearchPosMinusOne() {
        // Arrange
        searchableTextPane.search("test");
        int lastSearchPos = searchableTextPane.getLastSearchPos();

        // Act
        int result = searchableTextPane.search(null);

        // Assert
        assertEquals(lastSearchPos - 1, result);
    }

    @Test
    public void testSearch_whenTermIsEmpty_shouldReturnLastSearchPosMinusOne() {
        // Arrange
        searchableTextPane.search("test");
        int lastSearchPos = searchableTextPane.getLastSearchPos();

        // Act
        int result = searchableTextPane.search("");

        // Assert
        assertEquals(lastSearchPos - 1, result);
    }

    @Test
    public void testSearch_whenTermEqualsLastSearchTerm_shouldReturnNextPositionAfterLastSearchPos() {
        // Arrange
        searchableTextPane.search("test");
        int lastSearchPos = searchableTextPane.getLastSearchPos();
        String lastSearchTerm = searchableTextPane.getLastSearchTerm();

        // Act
        int result = searchableTextPane.search(lastSearchTerm);

        // Assert
        assertEquals(lastSearchPos + lastSearchTerm.length(), result);
    }

    @Test
    public void testSearch_whenTermDoesNotEqualLastSearchTerm_shouldReturnPositionOfFirstOccurrenceOfTerm() {
        // Arrange
        searchableTextPane.search("test");
        int lastSearchPos = searchableTextPane.getLastSearchPos();
        String lastSearchTerm = searchableTextPane.getLastSearchTerm();

        // Act
        int result = searchableTextPane.search("text");

        // Assert
        assertEquals(10, result);
    }

    @Test
    public void testSearch_whenTermDoesNotOccurInText_shouldReturnMinusOne() {
        // Arrange
        searchableTextPane.search("test");
        int lastSearchPos = searchableTextPane.getLastSearchPos();
        String lastSearchTerm = searchableTextPane.getLastSearchTerm();

        // Act
        int result = searchableTextPane.search("apple");

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_whenTextIsNotEmptyAndStartPositionIsZero_shouldReturnPositionOfFirstOccurrenceOfTerm() {
        // Act
        int result = searchableTextPane.search("This");

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testSearch_whenTextIsEmptyAndStartPositionIsZero_shouldReturnMinusOne() {
        // Arrange
        searchableTextPane.setText("");

        // Act
        int result = searchableTextPane.search("test");

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines_whenWordsIsEmpty_shouldReturnEmptyLines() throws BadLocationException {
        // Arrange
        List<WordResult> words = new ArrayList<>();

        // Act
        List<LineResult> lines = searchableTextPane.convertWords2Lines(words);

        // Assert
        assertTrue(lines.isEmpty());
    }

    @Test
    public void testConvertWords2Lines_whenWordsHasOneWord_shouldReturnLineWithWordResult() throws BadLocationException {
        // Arrange
        List<WordResult> words = new ArrayList<>();
        WordResult wordResult = new WordResult("test", 10, 14);
        words.add(wordResult);

        // Act
        List<LineResult> lines = searchableTextPane.convertWords2Lines(words);

        // Assert
        assertEquals(1, lines.size());
        LineResult lineResult = lines.get(0);
        assertEquals(0, lineResult.getLine());
        List<WordResult> lineWordResults = lineResult.getWordResults();
        assertEquals(1, lineWordResults.size());
        assertEquals(wordResult, lineWordResults.get(0));
    }

    @Test
    public void testUpdateWordResult_shouldUpdateWordResultWithLineOffsetAndParentLineNumber() throws BadLocationException {
        // Arrange
        WordResult wordResult = new WordResult("test", 10, 14);
        LineResult lineResult = new LineResult(0, 0, 15);

        // Act
        searchableTextPane.updateWordResult(wordResult, lineResult);

        // Assert
        assertEquals(0, wordResult.getLine());
        assertEquals(10, wordResult.getLineOffset());
        assertEquals(1, wordResult.getParent().getLineNumber());
    }

    @Test
    public void testSearch_whenTermExistsAtLastIndex_shouldReturnPositionOfLastOccurrenceOfTerm() {
        // Act
        int result = searchableTextPane.search("World!");

        // Assert
        assertEquals(17, result);
    }
}