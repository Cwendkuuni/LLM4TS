
package ghm.follow.search;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Utilities;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SearchableTextPaneTest {
    private static final Font FONT = new Font("Arial", Font.PLAIN, 12);
    private static final int TAB_SIZE = 4;
    private static final String TERM = "term";

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(FONT, TAB_SIZE);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_whenParentIsNull_returnsTrue() {
        searchableTextPane.setParent(null);
        assertEquals(true, searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_whenUIWidthIsLessThanParentSizeWidth_returnsTrue() {
        Component parent = createComponentWithSize(500, 500);
        ComponentUI ui = createComponentUIWithPreferredSize(400, 500);
        searchableTextPane.setParent(parent);
        searchableTextPane.setUI(ui);

        assertEquals(false, searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_whenUIWidthIsEqualToParentSizeWidth_returnsTrue() {
        Component parent = createComponentWithSize(500, 500);
        ComponentUI ui = createComponentUIWithPreferredSize(500, 500);
        searchableTextPane.setParent(parent);
        searchableTextPane.setUI(ui);

        assertEquals(true, searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_whenUIWidthIsGreaterThanParentSizeWidth_returnsTrue() {
        Component parent = createComponentWithSize(500, 500);
        ComponentUI ui = createComponentUIWithPreferredSize(600, 500);
        searchableTextPane.setParent(parent);
        searchableTextPane.setUI(ui);

        assertEquals(true, searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight_whenTermIsNull_thenReturnEmptyList() {
        List<LineResult> lineResults = searchableTextPane.highlight(null, 0);

        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlight_whenTermIsEmpty_thenReturnEmptyList() {
        List<LineResult> lineResults = searchableTextPane.highlight("", 0);

        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlight_whenTermMatchesInText_thenReturnLineResults() throws BadLocationException {
        String text = "This is a term and another term.";
        searchableTextPane.setText(text);

        List<LineResult> lineResults = searchableTextPane.highlight(TERM, 0);

        assertEquals(2, lineResults.size());
        assertEquals(1, lineResults.get(0).getWordResults().size());
        assertEquals(1, lineResults.get(1).getWordResults().size());
    }

    @Test
    public void testHighlight_whenTermDoesNotMatchInText_thenReturnEmptyList() throws BadLocationException {
        String text = "This is some text.";
        searchableTextPane.setText(text);

        List<LineResult> lineResults = searchableTextPane.highlight(TERM, 0);

        assertEquals(0, lineResults.size());
    }

    @Test
    public void testRemoveHighlights_whenHighlightsExist_thenReturnNoHighlights() throws BadLocationException {
        String text = "This is a term and another term.";
        searchableTextPane.setText(text);
        searchableTextPane.highlight(TERM, 0);

        searchableTextPane.removeHighlights();

        assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch_whenTermIsNull_thenReturnMinusOne() {
        int searchPos = searchableTextPane.search(null);

        assertEquals(-1, searchPos);
    }

    @Test
    public void testSearch_whenTermIsEmpty_thenReturnMinusOne() {
        int searchPos = searchableTextPane.search("");

        assertEquals(-1, searchPos);
    }

    @Test
    public void testSearch_whenTermEqualsLastSearchTerm_thenContinueFromLastPosition() {
        searchableTextPane.lastSearchTerm = TERM;
        searchableTextPane.lastSearchPos = 5;

        int searchPos = searchableTextPane.search(TERM);

        assertEquals(10, searchPos);
    }

    @Test
    public void testSearch_whenTermDoesNotEqualsLastSearchTerm_thenStartFromBeginning() {
        searchableTextPane.lastSearchTerm = "other";
        searchableTextPane.lastSearchPos = 5;

        int searchPos = searchableTextPane.search(TERM);

        assertEquals(0, searchPos);
    }

    @Test
    public void testSearch_whenTermExistsInText_thenReturnPositionOfFirstOccurrence() throws BadLocationException {
        String text = "This is a term and another term.";
        searchableTextPane.setText(text);

        int searchPos = searchableTextPane.search(TERM);

        assertEquals(10, searchPos);
    }

    @Test
    public void testSearch_whenTermDoesNotExistInText_thenReturnMinusOne() throws BadLocationException {
        String text = "This is some text.";
        searchableTextPane.setText(text);

        int searchPos = searchableTextPane.search(TERM);

        assertEquals(-1, searchPos);
    }

    @Test
    public void testSearch_withStartPos_whenTermExistsInTextFromStartPos_thenReturnPositionOfFirstOccurrence() throws BadLocationException {
        String text = "This is a term and another term.";
        searchableTextPane.setText(text);

        int searchPos = searchableTextPane.search(TERM, 15);

        assertEquals(24, searchPos);
    }

    @Test
    public void testSearch_withStartPos_whenTermDoesNotExistInTextFromStartPos_thenReturnMinusOne() throws BadLocationException {
        String text = "This is some text.";
        searchableTextPane.setText(text);

        int searchPos = searchableTextPane.search(TERM, 15);

        assertEquals(-1, searchPos);
    }

    @Test
    public void testConvertWords2Lines_whenWordsListIsEmpty_thenReturnEmptyList() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);

        assertEquals(0, lineResults.size());
    }

    @Test
    public void testConvertWords2Lines_whenWordsListHasWordsFromDifferentLines_thenReturnLineResultsForEachLine() throws BadLocationException {
        String text = "This is a term and\nanother term.";
        searchableTextPane.setText(text);
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(8, 12));
        words.add(new WordResult(18, 22));

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);

        assertEquals(2, lineResults.size());
        assertEquals(1, lineResults.get(0).getWordResults().size());
        assertEquals(1, lineResults.get(1).getWordResults().size());
    }

    @Test
    public void testConvertWords2Lines_whenWordsListHasWordsFromSameLine_thenReturnSingleLineResult() throws BadLocationException {
        String text = "This is a term and another term.";
        searchableTextPane.setText(text);
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(8, 12));
        words.add(new WordResult(18, 22));

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);

        assertEquals(1, lineResults.size());
    }

    private Component createComponentWithSize(int width, int height) {
        Component component = new JPanel();
        component.setSize(width, height);
        return component;
    }

    private ComponentUI createComponentUIWithPreferredSize(int width, int height) {
        ComponentUI ui = new ComponentUI() {
        };
        ui.getPreferredSize(searchableTextPane).setSize(width, height);
        return ui;
    }
}
