import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.util.*;
import java.util.logging.*;

public class SearchableTextPaneTest {

    private SearchableTextPane searchPane;
    
    @Before
    public void setUp() {
        searchPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_nullParent_returnTrue() {
        assertNull(searchPane.getParent());
        assertTrue(searchPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_parentWidthLessThanUIPreferredSize_returnTrue() {
        Component parent = new JPanel();
        parent.setSize(new Dimension(200, 100));
        searchPane.setParent(parent);
        ComponentUI ui = new BasicTextAreaUI();
        ui.addPropertyChangeListener(searchPane);
        ui.installUI(searchPane);
        int uiPreferredSizeWidth = ui.getPreferredSize(searchPane).width;
        assertTrue(uiPreferredSizeWidth > parent.getSize().width);
        assertTrue(searchPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_parentWidthEqualToUIPreferredSize_returnFalse() {
        Component parent = new JPanel();
        parent.setSize(new Dimension(200, 100));
        searchPane.setParent(parent);
        ComponentUI ui = new BasicTextAreaUI();
        ui.addPropertyChangeListener(searchPane);
        ui.installUI(searchPane);
        int uiPreferredSizeWidth = ui.getPreferredSize(searchPane).width;
        assertTrue(uiPreferredSizeWidth == parent.getSize().width);
        assertFalse(searchPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight_nullTerm_noHighlightsAndEmptyLineResults() {
        assertNull(searchPane.highlight(null, 1));
        assertEquals(0, searchPane.getLineCount());
    }
    
    @Test
    public void testHighlight_emptyTerm_noHighlightsAndEmptyLineResults() {
        assertNull(searchPane.highlight("", 1));
        assertEquals(0, searchPane.getLineCount());
    }
    
    @Test
    public void testHighlight_termNotPresent_noHighlightsAndEmptyLineResults() {
        assertTrue(searchPane.getText().isEmpty());
        assertEquals(0, searchPane.getLineCount());
        List<LineResult> lineResults = searchPane.highlight("apple", 1);
        assertEquals(0, lineResults.size());
    }
    
    @Test
    public void testHighlight_termPresent_singleHighlightAndLineResult() {
        searchPane.setText("apple");
        List<LineResult> lineResults = searchPane.highlight("apple", 1);
        assertEquals(1, lineResults.size());
        assertEquals(1, searchPane.getLineCount());
        Highlighter.Highlight[] highlights = searchPane.getHighlighter().getHighlights();
        assertEquals(1, highlights.length);
        assertEquals(0, highlights[0].getStartOffset());
        assertEquals(5, highlights[0].getEndOffset());
    }
    
    @Test
    public void testHighlight_multipleTermsPresent_multipleHighlightsAndLineResults() {
        searchPane.setText("apple apple apple");
        List<LineResult> lineResults = searchPane.highlight("apple", 1);
        assertEquals(3, lineResults.size());
        assertEquals(1, searchPane.getLineCount());
        Highlighter.Highlight[] highlights = searchPane.getHighlighter().getHighlights();
        assertEquals(3, highlights.length);
        assertEquals(0, highlights[0].getStartOffset());
        assertEquals(5, highlights[0].getEndOffset());
        assertEquals(6, highlights[1].getStartOffset());
        assertEquals(11, highlights[1].getEndOffset());
        assertEquals(12, highlights[2].getStartOffset());
        assertEquals(17, highlights[2].getEndOffset());
    }
    
    @Test(expected = BadLocationException.class)
    public void testAddHighlight_badLocationExceptionThrown() throws BadLocationException {
        searchPane.addHighlight(-1, 5);
    }
    
    @Test
    public void testRemoveHighlights_noHighlights_noHighlightsAfterRemoval() {
        assertEquals(0, searchPane.getHighlighter().getHighlights().length);
        searchPane.removeHighlights();
        assertEquals(0, searchPane.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testRemoveHighlights_existingHighlights_allHighlightsRemoved() {
        searchPane.setText("apple apple apple");
        searchPane.highlight("apple", 1);
        assertEquals(3, searchPane.getHighlighter().getHighlights().length);
        searchPane.removeHighlights();
        assertEquals(0, searchPane.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testSearch_nullTerm_noSearchPerformedAndLastSearchPosInitialisedToMinusOne() {
        assertEquals(-1, searchPane.search(null));
        assertNull(searchPane.lastSearchTerm);
    }
    
    @Test
    public void testSearch_emptyTerm_noSearchPerformedAndLastSearchPosInitialisedToMinusOne() {
        assertEquals(-1, searchPane.search(""));
        assertNull(searchPane.lastSearchTerm);
    }
    
    @Test
    public void testSearch_termEqualToLastSearchTerm_lastSearchPosStartsFromLastPosition() {
        searchPane.search("apple");
        int lastSearchPos = searchPane.lastSearchPos;
        assertTrue(lastSearchPos >= 0);
        assertEquals(lastSearchPos + 5, searchPane.search("apple"));
    }
    
    @Test
    public void testSearch_termDifferentFromLastSearchTerm_lastSearchPosStartsFromBeginning() {
        searchPane.search("apple");
        searchPane.lastSearchPos = 5;
        assertEquals(0, searchPane.search("orange"));
    }
    
    @Test
    public void testSearch_termPresent_searchPerformedAndLastSearchPosUpdated() {
        searchPane.setText("apple apple apple");
        assertEquals(0, searchPane.search("apple"));
        assertEquals("apple", searchPane.lastSearchTerm);
        int lastSearchPos = searchPane.lastSearchPos;
        assertTrue(lastSearchPos > 0);
        assertEquals(lastSearchPos + 5, searchPane.search("apple"));
        assertEquals(lastSearchPos + 10, searchPane.search("apple"));
    }
    
    @Test
    public void testSearch_termNotPresent_searchPerformedAndLastSearchPosRemainsMinusOne() {
        assertNull(searchPane.search("orange"));
        assertNull(searchPane.lastSearchTerm);
        assertEquals(-1, searchPane.lastSearchPos);
    }
    
    @Test(expected = BadLocationException.class)
    public void testSearch_badLocationExceptionThrown() throws BadLocationException {
        searchPane.search("apple", -1);
    }
    
    @Test
    public void testConvertWords2Lines_noWords_emptyLineResults() throws BadLocationException {
        List<LineResult> lineResults = searchPane.convertWords2Lines(Collections.emptyList());
        assertEquals(0, lineResults.size());
    }
    
    @Test
    public void testConvertWords2Lines_singleWord_singleLineResult() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 5));
        List<LineResult> lineResults = searchPane.convertWords2Lines(words);
        assertEquals(1, lineResults.size());
        assertEquals(1, lineResults.get(0).getWordResults().size());
        assertEquals(0, lineResults.get(0).getLine());
        assertEquals(0, lineResults.get(0).getLineStart());
        assertEquals(5, lineResults.get(0).getLineEnd());
    }
    
    @Test
    public void testConvertWords2Lines_multipleWords_multipleLineResults() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 5));
        words.add(new WordResult(6, 11));
        words.add(new WordResult(12, 17));
        List<LineResult> lineResults = searchPane.convertWords2Lines(words);
        assertEquals(3, lineResults.size());
        assertEquals(1, lineResults.get(0).getWordResults().size());
        assertEquals(1, lineResults.get(1).getWordResults().size());
        assertEquals(1, lineResults.get(2).getWordResults().size());
        assertEquals(0, lineResults.get(0).getLine());
        assertEquals(0, lineResults.get(0).getLineStart());
        assertEquals(5, lineResults.get(0).getLineEnd());
        assertEquals(0, lineResults.get(1).getLine());
        assertEquals(6, lineResults.get(1).getLineStart());
        assertEquals(11, lineResults.get(1).getLineEnd());
        assertEquals(0, lineResults.get(2).getLine());
        assertEquals(12, lineResults.get(2).getLineStart());
        assertEquals(17, lineResults.get(2).getLineEnd());
    }
    
    @Test(expected = BadLocationException.class)
    public void testUpdateWordResult_badLocationExceptionThrown() throws BadLocationException {
        searchPane.updateWordResult(new WordResult(-1, 5), new LineResult(0, 0, 10));
    }
}