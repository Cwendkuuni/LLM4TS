
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;
    private Font font;
    private int tabSize;

    @Before
    public void setUp() throws Exception {
        font = new FontUIResource(new Font("Arial", Font.PLAIN, 12));
        tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }

    @After
    public void tearDown() throws Exception {
        searchableTextPane = null;
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Test when parent component is null
        assertEquals(true, searchableTextPane.getScrollableTracksViewportWidth());

        // Test when preferred width of UI is less than parent width
        Component parent = new JPanel();
        ComponentUI ui = new MetalLookAndFeel().getDefaults().getUI(new JEditorPane());
        parent.setSize(500, 300);
        assertEquals(true, searchableTextPane.getScrollableTracksViewportWidth());

        // Test when preferred width of UI is greater than parent width
        parent.setSize(100, 300);
        assertEquals(false, searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        // Test when term is null
        List<LineResult> result = searchableTextPane.highlight(null, 0);
        assertEquals(0, result.size());

        // Test when term is empty
        result = searchableTextPane.highlight("", 0);
        assertEquals(0, result.size());

        // Test when term is present in the text
        String term = "Test";
        String text = "This is a Test string.";
        searchableTextPane.setText(text);
        result = searchableTextPane.highlight(term, 0);
        assertEquals(1, result.size());
        LineResult lineResult = result.get(0);
        assertEquals(text, "This is a Test string.");
        assertEquals(10, lineResult.getWordResults().get(0).start);
        assertEquals(14, lineResult.getWordResults().get(0).end);

        // Test when term is not present in the text
        term = "Lorem";
        result = searchableTextPane.highlight(term, 0);
        assertEquals(0, result.size());
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        // Add highlight to a specific part of the text
        String text = "This is a Test";
        searchableTextPane.setText(text);
        searchableTextPane.addHighlight(10, 4);
        assertEquals(1, searchableTextPane.getHighlighter().getHighlights().length);

        // Add highlight to a different part of the text
        searchableTextPane.addHighlight(2, 7);
        assertEquals(2, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testRemoveHighlights() {
        // Add some highlights
        String text = "This is a Test";
        searchableTextPane.setText(text);
        try {
            searchableTextPane.addHighlight(10, 4);
            searchableTextPane.addHighlight(2, 7);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        assertEquals(2, searchableTextPane.getHighlighter().getHighlights().length);

        // Remove highlights
        searchableTextPane.removeHighlights();
        assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
    }

    @Test
    public void testSearch() {
        // Test when term is null
        assertEquals(-1, searchableTextPane.search(null));

        // Test when term is empty
        assertEquals(-1, searchableTextPane.search(""));

        // Test when term is present in the text
        String term = "Test";
        String text = "This is a Test string.";
        searchableTextPane.setText(text);
        int pos = searchableTextPane.search(term);
        assertEquals(10, pos);

        // Test when term is not present in the text
        term = "Lorem";
        pos = searchableTextPane.search(term);
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchWithStartPos() {
        // Test when term is null
        assertEquals(-1, searchableTextPane.search(null, 0));

        // Test when term is empty
        assertEquals(-1, searchableTextPane.search("", 0));

        // Test when term is present after start position
        String term = "This";
        String text = "This is a Test string.";
        searchableTextPane.setText(text);
        int pos = searchableTextPane.search(term, 10);
        assertEquals(0, pos);

        // Test when term is not present after start position
        term = "string";
        pos = searchableTextPane.search(term, 10);
        assertEquals(-1, pos);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        // Test when words are present on multiple lines
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 5));
        words.add(new WordResult(10, 15));
        words.add(new WordResult(25, 30));
        words.add(new WordResult(40, 45));
        String text = "This is a Test\nThis is another Test\nThis is a third Test\nThis is the fourth Test";
        searchableTextPane.setText(text);
        List<LineResult> result = searchableTextPane.convertWords2Lines(words);
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).getWordResults().size());
        assertEquals(2, result.get(1).getWordResults().size());
        assertEquals(1, result.get(2).getWordResults().size());
        assertEquals(1, result.get(3).getWordResults().size());
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        // Test when word is present on a single line
        LineResult lineResult = new LineResult(0, 0, 10);
        WordResult wordResult = new WordResult(5, 9);
        searchableTextPane.setText("This is a Test");
        searchableTextPane.updateWordResult(wordResult, lineResult);
        assertEquals(wordResult, lineResult.getWordResults().get(0));
        assertEquals(1, wordResult.getParent().getLineNumber());
        assertEquals(0, wordResult.getLineOffset());

        // Test when word is present on a different line
        lineResult = new LineResult(1, 13, 26);
        wordResult = new WordResult(18, 24);
        searchableTextPane.setText("This is a Test\nThis is another Test");
        searchableTextPane.updateWordResult(wordResult, lineResult);
        assertEquals(wordResult, lineResult.getWordResults().get(0));
        assertEquals(2, wordResult.getParent().getLineNumber());
        assertEquals(13, wordResult.getLineOffset());
    }
}

