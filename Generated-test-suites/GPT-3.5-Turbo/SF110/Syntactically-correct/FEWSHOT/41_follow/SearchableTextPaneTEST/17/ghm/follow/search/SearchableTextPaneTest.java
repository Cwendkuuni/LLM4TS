
package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

public class SearchableTextPaneTest {
    private SearchableTextPane textPane;

    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        textPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidthWithNullParentAndNullUI() {
        textPane.setParent(null);
        textPane.setUI(null);
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidthWithNullParent() {
        textPane.setParent(null);
        ComponentUI ui = new TextComponentUI();
        textPane.setUI(ui);
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidthWithViewportWidthLargerThanPreferredWidth() {
        JPanel parent = new JPanel();
        parent.setSize(new Dimension(500, 500));
        textPane.setParent(parent);
        ComponentUI ui = new TextComponentUI() {
            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(200, 200);
            }
        };
        textPane.setUI(ui);
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidthWithViewportWidthSmallerThanPreferredWidth() {
        JPanel parent = new JPanel();
        parent.setSize(new Dimension(200, 200));
        textPane.setParent(parent);
        ComponentUI ui = new TextComponentUI() {
            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(500, 500);
            }
        };
        textPane.setUI(ui);
        assertFalse(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightWithNullTerm() {
        List<LineResult> lineResults = textPane.highlight(null, 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlightWithEmptyTerm() {
        List<LineResult> lineResults = textPane.highlight("", 0);
        assertNotNull(lineResults);
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlightWithValidTerm() {
        List<LineResult> lineResults = textPane.highlight("test", 0);
        assertNotNull(lineResults);
        assertFalse(lineResults.isEmpty());
    }

    @Test
    public void testAddHighlight() {
        try {
            textPane.addHighlight(0, 10);
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
        Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
        assertNotNull(highlights);
        assertEquals(1, highlights.length);
    }

    @Test
    public void testRemoveHighlights() {
        try {
            textPane.addHighlight(0, 10);
            textPane.addHighlight(20, 30);
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
        textPane.removeHighlights();
        Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
        assertNotNull(highlights);
        assertEquals(0, highlights.length);
    }

    @Test
    public void testSearchWithNullTerm() {
        int result = textPane.search(null);
        assertEquals(-1, result);
        assertNull(textPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithEmptyTerm() {
        int result = textPane.search("");
        assertEquals(-1, result);
        assertNull(textPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithExistingTerm() {
        textPane.setText("This is a test");
        int result = textPane.search("test");
        assertEquals(10, result);
        assertEquals("test", textPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithNonExistingTerm() {
        textPane.setText("This is a test");
        int result = textPane.search("foo");
        assertEquals(-1, result);
        assertEquals("foo", textPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithExistingTermAndStartPosition() {
        textPane.setText("This is a test test test");
        int result = textPane.search("test", 8);
        assertEquals(10, result);
        assertEquals("test", textPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithNonExistingTermAndStartPosition() {
        textPane.setText("This is a test test test");
        int result = textPane.search("foo", 8);
        assertEquals(-1, result);
        assertEquals("foo", textPane.getLastSearchTerm());
    }

    @Test
    public void testConvertWords2Lines() {
        WordResult wordResult1 = new WordResult(0, 3);
        WordResult wordResult2 = new WordResult(5, 8);
        List<WordResult> words = Arrays.asList(wordResult1, wordResult2);

        try {
            List<LineResult> lineResults = textPane.convertWords2Lines(words);

            assertNotNull(lineResults);
            assertFalse(lineResults.isEmpty());
            assertEquals(2, lineResults.size());

            LineResult lineResult1 = lineResults.get(0);
            LineResult lineResult2 = lineResults.get(1);

            assertNotNull(lineResult1);
            assertNotNull(lineResult2);

            List<WordResult> line1WordResults = lineResult1.getWordResults();
            List<WordResult> line2WordResults = lineResult2.getWordResults();

            assertNotNull(line1WordResults);
            assertNotNull(line2WordResults);
            assertFalse(line1WordResults.isEmpty());
            assertFalse(line2WordResults.isEmpty());
            assertEquals(1, line1WordResults.size());
            assertEquals(1, line2WordResults.size());

            WordResult line1WordResult = line1WordResults.get(0);
            WordResult line2WordResult = line2WordResults.get(0);

            assertNotNull(line1WordResult);
            assertNotNull(line2WordResult);
            assertEquals(0, line1WordResult.start);
            assertEquals(3, line1WordResult.end);
            assertEquals(5, line2WordResult.start);
            assertEquals(8, line2WordResult.end);

            int line1Offset = textPane.getLineStartOffset(0);
            int line2Offset = textPane.getLineStartOffset(1);

            assertEquals(line1Offset, line1WordResult.getLineOffset());
            assertEquals(line2Offset, line2WordResult.getLineOffset());

        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }

    @Test
    public void testUpdateWordResult() {
        WordResult wordResult1 = new WordResult(0, 3);
        WordResult wordResult2 = new WordResult(5, 8);
        List<WordResult> words = Arrays.asList(wordResult1, wordResult2);

        try {
            LineResult lineResult = new LineResult(0, 0, 10);

            textPane.updateWordResult(wordResult1, lineResult);
            textPane.updateWordResult(wordResult2, lineResult);

            assertNotNull(lineResult.getWordResults());
            assertFalse(lineResult.getWordResults().isEmpty());
            assertEquals(2, lineResult.getWordResults().size());

            WordResult updatedWordResult1 = lineResult.getWordResults().get(0);
            WordResult updatedWordResult2 = lineResult.getWordResults().get(1);

            assertNotNull(updatedWordResult1);
            assertNotNull(updatedWordResult2);
            assertEquals(0, updatedWordResult1.start);
            assertEquals(3, updatedWordResult1.end);
            assertEquals(5, updatedWordResult2.start);
            assertEquals(8, updatedWordResult2.end);

            int lineOffset = textPane.getLineStartOffset(0);
            assertEquals(lineOffset, updatedWordResult1.getLineOffset());
            assertEquals(lineOffset, updatedWordResult2.getLineOffset());

        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
}
