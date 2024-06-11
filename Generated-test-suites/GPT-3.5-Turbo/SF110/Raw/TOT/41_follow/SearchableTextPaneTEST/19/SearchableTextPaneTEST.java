import org.junit.Before;
import org.junit.Test;
import javax.swing.text.BadLocationException;
import java.awt.Font;
import java.util.List;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {
    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentNull_returnsTrue() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentNotNullAndUIWidthLessThanParentWidth_returnsTrue() {
        JPanel parent = new JPanel();
        parent.setSize(200, 200);
        searchableTextPane.setParent(parent);

        // Set UI width less than parent width
        ComponentUI ui = new ComponentUI() {
            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(150, 100);
            }
        };
        searchableTextPane.setUI(ui);

        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentNotNullAndUIWidthEqualToParentWidth_returnsTrue() {
        JPanel parent = new JPanel();
        parent.setSize(200, 200);
        searchableTextPane.setParent(parent);

        // Set UI width equal to parent width
        ComponentUI ui = new ComponentUI() {
            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(200, 100);
            }
        };
        searchableTextPane.setUI(ui);

        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_withParentNotNullAndUIWidthGreaterThanParentWidth_returnsFalse() {
        JPanel parent = new JPanel();
        parent.setSize(200, 200);
        searchableTextPane.setParent(parent);

        // Set UI width greater than parent width
        ComponentUI ui = new ComponentUI() {
            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(250, 100);
            }
        };
        searchableTextPane.setUI(ui);

        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight_withNullTerm_returnsEmptyList() {
        List<LineResult> lineResults = searchableTextPane.highlight(null, 0);

        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlight_withEmptyTerm_returnsEmptyList() {
        List<LineResult> lineResults = searchableTextPane.highlight("", 0);

        assertEquals(0, lineResults.size());
    }

    // ... continue with complete test cases for each method ...

    @Test
    public void testConvertWords2Lines_withEmptyWordsList_returnsEmptyList() throws BadLocationException {
        List<LineResult> lines = searchableTextPane.convertWords2Lines(new ArrayList<>());

        assertEquals(0, lines.size());
    }

    @Test
    public void testUpdateWordResult_withValidWordResult_updatesLineResultAndWordResult() throws BadLocationException {
        LineResult lineResult = new LineResult(1, 0, 10);
        WordResult wordResult = new WordResult(3, 5);
        wordResult.parent = lineResult;

        searchableTextPane.updateWordResult(wordResult, lineResult);

        assertEquals(1, wordResult.parent.lineNumber);
        assertEquals(0, wordResult.lineOffset);
    }

    // ... continue with complete test cases for each method ...

}