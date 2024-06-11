
package ghm.follow.search;

import org.junit.Before;
import org.junit.Test;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

public class SearchableTextPaneTest {
    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_WhenParentIsNull_ExpectTrue() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_WhenUIWidthIsLessThanParentSize_ExpectTrue() {
        MockComponent parent = new MockComponent();
        MockComponentUI ui = new MockComponentUI((int) parent.getSize().getWidth() - 1);
        searchableTextPane.setParent(parent);
        searchableTextPane.setUI(ui);

        boolean result = searchableTextPane.getScrollableTracksViewportWidth();

        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_WhenUIWidthIsEqualToParentSize_ExpectTrue() {
        MockComponent parent = new MockComponent();
        MockComponentUI ui = new MockComponentUI((int) parent.getSize().getWidth());
        searchableTextPane.setParent(parent);
        searchableTextPane.setUI(ui);

        boolean result = searchableTextPane.getScrollableTracksViewportWidth();

        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_WhenUIWidthIsGreaterThanParentSize_ExpectFalse() {
        MockComponent parent = new MockComponent();
        MockComponentUI ui = new MockComponentUI((int) parent.getSize().getWidth() + 1);
        searchableTextPane.setParent(parent);
        searchableTextPane.setUI(ui);

        boolean result = searchableTextPane.getScrollableTracksViewportWidth();

        assertFalse(result);
    }

    @Test
    public void testHighlight_WhenTermIsNull_ExpectEmptyLineResults() {
        String term = null;
        int flags = 0;

        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);

        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlight_WhenTermIsEmpty_ExpectEmptyLineResults() {
        String term = "";
        int flags = 0;

        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);

        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }

    // Add more test cases to cover different scenarios

    @Test
    public void testRemoveHighlights_WhenHighlightsExist_ExpectNoHighlights() {
        DefaultHighlighter highlighter = new DefaultHighlighter();
        searchableTextPane.setHighlighter(highlighter);
        MockHighlightPainter painter = new MockHighlightPainter();
        highlighter.addHighlight(0, 5, painter);

        searchableTextPane.removeHighlights();

        assertEquals(0, highlighter.getHighlights().length);
    }

    @Test
    public void testRemoveHighlights_WhenNoHighlights_ExpectNoChange() {
        DefaultHighlighter highlighter = new DefaultHighlighter();
        searchableTextPane.setHighlighter(highlighter);

        searchableTextPane.removeHighlights();

        assertEquals(0, highlighter.getHighlights().length);
    }

    @Test
    public void testSearch_WhenTermIsNull_ExpectLastSearchPosToBeEqualToLastSearchTermLength() {
        String term = null;

        int result = searchableTextPane.search(term);

        assertEquals(5, result);
        assertEquals(null, searchableTextPane.getLastSearchTerm());
    }

    @Test
    public void testSearch_WhenTermIsEmpty_ExpectLastSearchPosToBeEqualToLastSearchTermLength() {
        String term = "";

        int result = searchableTextPane.search(term);

        assertEquals(5, result);
        assertEquals("", searchableTextPane.getLastSearchTerm());
    }

    // Add more test cases to cover different scenarios

    @Test
    public void testSearch_WhenLastSearchPosIsNotNegative_ExpectLastSearchPosToBeEqualToLastSearchTermLength() {
        String term = "test";
        searchableTextPane.setLastSearchTerm(term);
        searchableTextPane.setLastSearchPos(5);

        int result = searchableTextPane.search(term);

        assertEquals(9, result);
        assertEquals(term, searchableTextPane.getLastSearchTerm());
        assertEquals(9, searchableTextPane.getLastSearchPos());
    }

    @Test
    public void testSearch_WhenTermDoesNotExist_ExpectLastSearchPosToBeNegative() {
        String term = "test";

        int result = searchableTextPane.search(term);

        assertEquals(-1, result);
        assertEquals(term, searchableTextPane.getLastSearchTerm());
        assertEquals(-1, searchableTextPane.getLastSearchPos());
    }

    @Test
    public void testSearch_WhenTermExists_ExpectLastSearchPosToBeFirstIndexOfTerm() {
        String term = "test test test";

        int result = searchableTextPane.search(term);

        assertEquals(0, result);
        assertEquals(term, searchableTextPane.getLastSearchTerm());
        assertEquals(0, searchableTextPane.getLastSearchPos());
    }

    @Test
    public void testSearch_WhenTermExistsAfterLastSearchPos_ExpectLastSearchPosToBeNextIndexOfTerm() {
        String term = "test";
        searchableTextPane.setLastSearchTerm(term);
        searchableTextPane.setLastSearchPos(5);

        int result = searchableTextPane.search(term);

        assertEquals(9, result);
        assertEquals(term, searchableTextPane.getLastSearchTerm());
        assertEquals(9, searchableTextPane.getLastSearchPos());
    }

    @Test
    public void testSearch_WhenTermExistsAtLastSearchPos_ExpectLastSearchPosToBeNextIndexOfTerm() {
        String term = "test";
        searchableTextPane.setLastSearchTerm(term);
        searchableTextPane.setLastSearchPos(4);

        int result = searchableTextPane.search(term);

        assertEquals(9, result);
        assertEquals(term, searchableTextPane.getLastSearchTerm());
        assertEquals(9, searchableTextPane.getLastSearchPos());
    }

    // Add more test cases to cover different scenarios

    @Test
    public void testSearch_WhenBadLocationExceptionOccurs_ExpectLastSearchPosToBeNegative() {
        String term = "test";
        searchableTextPane.setLastSearchTerm(term);

        int result = searchableTextPane.search(term, -1);

        assertEquals(-1, result);
        assertEquals(term, searchableTextPane.getLastSearchTerm());
        assertEquals(-1, searchableTextPane.getLastSearchPos());
    }

    @Test
    public void testConvertWords2Lines_WhenWordsIsEmpty_ExpectEmptyLineResults() throws BadLocationException {
        List<WordResult> words = List.of();

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);

        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testConvertWords2Lines_WhenWordsExist_ExpectLineResultsWithWordsAdded() throws BadLocationException {
        List<WordResult> words = List.of(new WordResult(0, 3), new WordResult(5, 7), new WordResult(9, 11));

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);

        assertNotNull(lineResults);
        assertEquals(1, lineResults.size());
        assertEquals(0, lineResults.get(0).getLineNumber());
        assertEquals(0, lineResults.get(0).getLineStartOffset());
        assertEquals(13, lineResults.get(0).getLineEndOffset());
        assertEquals(words, lineResults.get(0).getWordResults());
    }

    // Add more test cases to cover different scenarios

    @Test
    public void testUpdateWordResult_WhenLineIsDifferentFromLastLine_ExpectNewLineResultAddedToLines() throws BadLocationException {
        List<LineResult> lines = List.of(new LineResult(0, 0, 4));
        searchableTextPane.setLines(lines);
        WordResult wordResult = new WordResult(5, 8);
        LineResult lineResult = new LineResult(1, 5, 10);

        searchableTextPane.updateWordResult(wordResult, lineResult);

        assertEquals(2, searchableTextPane.getLines().size());
        assertEquals(lineResult, searchableTextPane.getLines().get(1));
    }

    @Test
    public void testUpdateWordResult_WhenLineIsSameAsLastLine_ExpectWordAddedToLine() throws BadLocationException {
        List<LineResult> lines = List.of(new LineResult(0, 0, 4));
        searchableTextPane.setLines(lines);
        WordResult wordResult = new WordResult(5, 8);
        LineResult lineResult = new LineResult(0, 5, 10);

        searchableTextPane.updateWordResult(wordResult, lineResult);

        assertEquals(1, searchableTextPane.getLines().size());
        assertEquals(wordResult, searchableTextPane.getLines().get(0).getWordResults().get(0));
    }

    // Add more test cases to cover different scenarios

    @Test
    public void testAddHighlight_WhenHighlightDoesNotExist_ExpectHighlightAdded() throws BadLocationException {
        DefaultHighlighter highlighter = new DefaultHighlighter();
        searchableTextPane.setHighlighter(highlighter);
        MockHighlightPainter painter = new MockHighlightPainter();

        searchableTextPane.addHighlight(0, 5);

        assertEquals(1, highlighter.getHighlights().length);
        assertEquals(0, highlighter.getHighlights()[0].getStartOffset());
        assertEquals(5, highlighter.getHighlights()[0].getEndOffset());
    }

    @Test
    public void testAddHighlight_WhenHighlightExists_ExpectHighlightAdded() throws BadLocationException {
        DefaultHighlighter highlighter = new DefaultHighlighter();
        searchableTextPane.setHighlighter(highlighter);
        MockHighlightPainter painter = new MockHighlightPainter();
        highlighter.addHighlight(0, 3, painter);

        searchableTextPane.addHighlight(4, 7);

        assertEquals(2, highlighter.getHighlights().length);
        assertEquals(0, highlighter.getHighlights()[0].getStartOffset());
        assertEquals(3, highlighter.getHighlights()[0].getEndOffset());
        assertEquals(4, highlighter.getHighlights()[1].getStartOffset());
        assertEquals(11, highlighter.getHighlights()[1].getEndOffset());
    }
}
