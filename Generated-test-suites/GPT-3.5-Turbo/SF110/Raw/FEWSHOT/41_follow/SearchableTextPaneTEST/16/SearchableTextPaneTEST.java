
package ghm.follow.search;

import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth_noParent_returnsFalse() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertEquals(false, result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_parentWidthGreaterThanTextPaneWidth_returnsFalse() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        textPane.setSize(100, 100);
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertEquals(false, result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_parentWidthLessThanTextPaneWidth_returnsFalse() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        textPane.setSize(10, 100);
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertEquals(false, result);
    }

    @Test
    public void testGetScrollableTracksViewportWidth_parentWidthEqualToTextPaneWidth_returnsTrue() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        textPane.setSize(100, 100);
        boolean result = textPane.getScrollableTracksViewportWidth();
        assertEquals(true, result);
    }

    @Test
    public void testHighlight_noTerm_returnsEmptyList() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        List<LineResult> result = textPane.highlight(null, 0);
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testHighlight_emptyTerm_returnsEmptyList() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        List<LineResult> result = textPane.highlight("", 0);
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testHighlight_termNotFound_returnsEmptyList() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        List<LineResult> result = textPane.highlight("foo", 0);
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testHighlight_singleLineTermFound_returnsLineResultListWithOneElement() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        List<LineResult> result = textPane.highlight("test", 0);
        List<LineResult> expected = new ArrayList<>();
        LineResult lineResult = new LineResult(1, 11, 15);
        expected.add(lineResult);
        assertEquals(expected, result);
    }

    @Test
    public void testHighlight_multiLineTermFound_returnsLineResultListWithMultipleElements() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test\nThis is another test");
        List<LineResult> result = textPane.highlight("test", 0);
        List<LineResult> expected = new ArrayList<>();
        LineResult lineResult1 = new LineResult(1, 11, 15);
        LineResult lineResult2 = new LineResult(2, 24, 28);
        expected.add(lineResult1);
        expected.add(lineResult2);
        assertEquals(expected, result);
    }

    @Test
    public void testHighlight_caseSensitiveTermFound_returnsLineResultListWithOneElement() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        List<LineResult> result = textPane.highlight("Test", LineResult.CASE_SENSITIVE);
        List<LineResult> expected = new ArrayList<>();
        LineResult lineResult = new LineResult(1, 11, 15);
        expected.add(lineResult);
        assertEquals(expected, result);
    }

    @Test
    public void testAddHighlight_validStartAndLength_highlightsText() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        try {
            textPane.addHighlight(5, 2);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
        assertEquals(1, highlights.length);
        assertEquals(5, highlights[0].getStartOffset());
        assertEquals(7, highlights[0].getEndOffset());
    }

    @Test
    public void testRemoveHighlights_existingHighlights_removesHighlights() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        try {
            textPane.addHighlight(5, 2);
            textPane.removeHighlights();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
        assertEquals(0, highlights.length);
    }

    @Test
    public void testSearch_noTerm_returnsMinusOne() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        int result = textPane.search(null);
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_termNotInText_returnsMinusOne() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        int result = textPane.search("foo");
        assertEquals(-1, result);
    }

    @Test
    public void testSearch_termInTextStartingFromStartPos_returnsCorrectIndex() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        textPane.setText("This is a test");
        int result = textPane.search("is", 0);
        assertEquals(2, result);
    }

    @Test
    public void testConvertWords2Lines_emptyList_returnsEmptyList() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        try {
            List<LineResult> result = textPane.convertWords2Lines(new ArrayList<>());
            assertEquals(new ArrayList<>(), result);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertWords2Lines_singleLineWordResult_returnsLineResultListWithOneElement() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        try {
            List<WordResult> wordResults = new ArrayList<>();
            WordResult wordResult = new WordResult(0, 4, 1);
            wordResults.add(wordResult);
            List<LineResult> result = textPane.convertWords2Lines(wordResults);
            List<LineResult> expected = new ArrayList<>();
            LineResult lineResult = new LineResult(0, 0, 5);
            lineResult.addWord(wordResult);
            expected.add(lineResult);
            assertEquals(expected, result);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertWords2Lines_multiLineWordResult_returnsLineResultListWithMultipleElements() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        try {
            List<WordResult> wordResults = new ArrayList<>();
            WordResult wordResult1 = new WordResult(0, 4, 1);
            WordResult wordResult2 = new WordResult(10, 14, 2);
            wordResults.add(wordResult1);
            wordResults.add(wordResult2);
            List<LineResult> result = textPane.convertWords2Lines(wordResults);
            List<LineResult> expected = new ArrayList<>();
            LineResult lineResult1 = new LineResult(0, 0, 5);
            LineResult lineResult2 = new LineResult(1, 10, 15);
            lineResult1.addWord(wordResult1);
            lineResult2.addWord(wordResult2);
            expected.add(lineResult1);
            expected.add(lineResult2);
            assertEquals(expected, result);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateWordResult_updatesWordResultAndLineResult() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 4);
        try {
            List<WordResult> wordResults = new ArrayList<>();
            WordResult wordResult1 = new WordResult(0, 4, 1);
            wordResults.add(wordResult1);
            LineResult lineResult = new LineResult(0, 0, 5);
            textPane.updateWordResult(wordResult1, lineResult);
            assertEquals(lineResult, wordResult1.parent);
            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(wordResult1, lineResult.getWordResults().get(0));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

}

