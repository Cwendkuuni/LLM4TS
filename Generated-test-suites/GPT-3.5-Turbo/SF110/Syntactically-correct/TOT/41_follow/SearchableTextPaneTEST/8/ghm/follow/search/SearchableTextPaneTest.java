import org.junit.Test;

import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth_parentIsNull() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);

        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_parentIsNotNull() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        JPanel parent = new JPanel();
        parent.setSize(500, 500);
        searchableTextPane.setParent(parent);

        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testGetScrollableTracksViewportWidth_widthExceedsPreferredWidth() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        JPanel parent = new JPanel();
        parent.setSize(200, 200);
        searchableTextPane.setParent(parent);
        searchableTextPane.setUI(new TestComponentUI());

        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight_termIsNull() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);

        assertEquals(0, searchableTextPane.highlight(null, 0).size());
    }

    @Test
    public void testHighlight_termIsEmpty() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);

        assertEquals(0, searchableTextPane.highlight("", 0).size());
    }

    @Test
    public void testHighlight_noMatch() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        searchableTextPane.setText("This is a test");

        assertEquals(0, searchableTextPane.highlight("not found", 0).size());
    }

    @Test
    public void testHighlight_singleMatch() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        searchableTextPane.setText("This is a test");

        assertEquals(1, searchableTextPane.highlight("test", 0).size());
    }

    @Test
    public void testHighlight_multipleMatches() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        searchableTextPane.setText("This is a test. This is another test.");

        assertEquals(2, searchableTextPane.highlight("test", 0).size());
    }

    @Test
    public void testAddHighlight_invalidPosition() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);

        try {
            searchableTextPane.addHighlight(-1, 5);
            fail("Expected BadLocationException to be thrown");
        } catch (BadLocationException e) {
            // Expected exception
        }
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        searchableTextPane.setText("This is a test");
        searchableTextPane.highlight("test", 0);

        searchableTextPane.removeHighlights();

        assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch_termIsNull() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);

        assertEquals(-1, searchableTextPane.search(null));
    }

    @Test
    public void testSearch_termIsEmpty() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);

        assertEquals(-1, searchableTextPane.search(""));
    }

    @Test
    public void testSearch_lastSearchTermIsNull() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        searchableTextPane.setText("This is a test");
        searchableTextPane.search("is");

        int result = searchableTextPane.search("is");

        assertEquals(2, result);
    }

    @Test
    public void testSearch_differentTermSamePosition() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        searchableTextPane.setText("This is a test");

        int result = searchableTextPane.search("is");

        assertEquals(2, result);
    }

    @Test
    public void testSearch_sameTermAfterPreviousMatch() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        searchableTextPane.setText("This is a test");
        searchableTextPane.search("is");

        int result = searchableTextPane.search("is");

        assertEquals(5, result);
    }

    @Test
    public void testSearch_noMatch() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        searchableTextPane.setText("This is a test");

        int result = searchableTextPane.search("not found");

        assertEquals(-1, result);
    }

    @Test
    public void testSearch_invalidPosition() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);

        int result = searchableTextPane.search("test", -1);

        assertEquals(-1, result);
    }

    @Test
    public void testConvertWords2Lines_emptyWordsList() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);

        try {
            searchableTextPane.convertWords2Lines(new ArrayList<>());
        } catch (BadLocationException e) {
            fail("Expected BadLocationException not to be thrown");
        }
    }

    @Test
    public void testConvertWords2Lines_singleWord() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        searchableTextPane.setText("This is a test");

        List<LineResult> lineResults = new ArrayList<>();
        WordResult wordResult = new WordResult("test");
        lineResults.add(new LineResult(0, 0, 14));
        lineResults.get(0).addWord(wordResult);

        try {
            List<LineResult> result = searchableTextPane.convertWords2Lines(lineResults);
            assertEquals(1, result.size());
            assertEquals(1, result.get(0).getWordResults().size());
            assertEquals(0, result.get(0).getLineNumber());
            assertEquals(0, result.get(0).getLineStart());
            assertEquals(15, result.get(0).getLineEnd());
        } catch (BadLocationException e) {
            fail("Expected BadLocationException not to be thrown");
        }
    }

    @Test
    public void testConvertWords2Lines_multipleWords() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        searchableTextPane.setText("This is a test. This is another test.");

        List<LineResult> lineResults = new ArrayList<>();
        WordResult wordResult1 = new WordResult("test");
        WordResult wordResult2 = new WordResult("another");

        lineResults.add(new LineResult(0, 0, 15));
        lineResults.get(0).addWord(wordResult1);
        lineResults.get(0).addWord(wordResult2);

        try {
            List<LineResult> result = searchableTextPane.convertWords2Lines(lineResults);
            assertEquals(1, result.size());
            assertEquals(2, result.get(0).getWordResults().size());
            assertEquals(0, result.get(0).getLineNumber());
            assertEquals(0, result.get(0).getLineStart());
            assertEquals(29, result.get(0).getLineEnd());
        } catch (BadLocationException e) {
            fail("Expected BadLocationException not to be thrown");
        }
    }

    @Test
    public void testUpdateWordResult() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(null, 4);
        WordResult wordResult = new WordResult("test");
        LineResult lineResult = new LineResult(0, 0, 14);

        try {
            searchableTextPane.updateWordResult(wordResult, lineResult);

            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(1, wordResult.getParent().getLineNumber());
            assertEquals(0, wordResult.getLineOffset());
        } catch (BadLocationException e) {
            fail("Expected BadLocationException not to be thrown");
        }
    }
}