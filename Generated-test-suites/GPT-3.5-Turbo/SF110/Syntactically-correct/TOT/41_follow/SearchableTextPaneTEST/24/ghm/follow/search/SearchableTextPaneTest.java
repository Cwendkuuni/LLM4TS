
package ghm.follow.search;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.logging.*;
import java.util.*;
import javax.swing.text.*;

import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane searchPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertTrue(searchPane.getScrollableTracksViewportWidth());
        
        // Test when parent is null
        searchPane.setParent(null);
        assertTrue(searchPane.getScrollableTracksViewportWidth());
        
        // Test when parent is not null and viewport width is smaller than the preferred size
        JPanel parent = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(searchPane);
        parent.add(scrollPane);
        parent.setPreferredSize(new Dimension(100, 100));
        assertTrue(searchPane.getScrollableTracksViewportWidth());
        
        // Test when parent is not null and viewport width is larger than the preferred size
        parent.setPreferredSize(new Dimension(200, 100));
        assertFalse(searchPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        SearchableTextPane searchPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);

        // Test highlighting with a non-null and non-empty term
        List<LineResult> lineResults = searchPane.highlight("banana", SearchEngine.CASE_SENSITIVE);
        assertEquals(0, lineResults.size());

        // Test highlighting with a null term
        lineResults = searchPane.highlight(null, SearchEngine.CASE_SENSITIVE);
        assertEquals(0, lineResults.size());

        // Test highlighting with an empty term
        lineResults = searchPane.highlight("", SearchEngine.CASE_SENSITIVE);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() {
        SearchableTextPane searchPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);

        // Test adding a highlight with a valid start and length
        try {
            searchPane.addHighlight(3, 5);
        } catch (BadLocationException e) {
            fail("Exception should not be thrown");
        }
        
        // Test adding a highlight with a negative start
        try {
            searchPane.addHighlight(-1, 5);
            fail("Exception should be thrown for negative start");
        } catch (BadLocationException e) {
            // Exception should be thrown
        }
        
        // Test adding a highlight with a negative length
        try {
            searchPane.addHighlight(3, -5);
            fail("Exception should be thrown for negative length");
        } catch (BadLocationException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane searchPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        
        // Test removing highlights when there are no highlights
        searchPane.removeHighlights();

        // Test removing highlights when there are existing highlights
        try {
            searchPane.addHighlight(3, 5);
            assertEquals(1, searchPane.getHighlighter().getHighlights().length);
            searchPane.removeHighlights();
            assertEquals(0, searchPane.getHighlighter().getHighlights().length);
        } catch (BadLocationException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSearch() {
        SearchableTextPane searchPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        
        // Test searching with a non-null and non-empty term
        int pos = searchPane.search("banana");
        assertEquals(-1, pos);
        assertEquals(null, searchPane.getLastSearchTerm());

        // Test searching with a null term
        pos = searchPane.search(null);
        assertEquals(-1, pos);
        assertEquals(null, searchPane.getLastSearchTerm());

        // Test searching with an empty term
        pos = searchPane.search("");
        assertEquals(-1, pos);
        assertEquals(null, searchPane.getLastSearchTerm());
    }
    
    @Test
    public void testSearchWithStartPosition() {
        SearchableTextPane searchPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);

        // Test searching with a non-null and non-empty term and a valid start position
        int pos = searchPane.search("banana", 5);
        assertEquals(-1, pos);
        assertEquals(null, searchPane.getLastSearchTerm());
        
        // Test searching with a non-null and non-empty term and a negative start position
        pos = searchPane.search("banana", -5);
        assertEquals(-1, pos);
        assertEquals(null, searchPane.getLastSearchTerm());
    }
    
    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane searchPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult("apple", 1, 1));

        try {
            // Test converting words to lines when the line matches the previous line
            List<LineResult> lines = searchPane.convertWords2Lines(words);
            assertEquals(1, lines.size());
            assertEquals(1, lines.get(0).getWordResults().size());
            assertEquals(1, lines.get(0).getLineNumber());
            
            // Test converting words to lines when the line does not match the previous line
            words.add(new WordResult("banana", 2, 2));
            lines = searchPane.convertWords2Lines(words);
            assertEquals(2, lines.size());
            assertEquals(1, lines.get(0).getWordResults().size());
            assertEquals(2, lines.get(1).getWordResults().size());
        } catch (BadLocationException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testUpdateWordResult() {
        SearchableTextPane searchPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        WordResult wordResult = new WordResult("apple", 1, 1);
        LineResult lineResult = new LineResult(1, 1, 2);

        try {
            // Test updating word result with a valid word and line result
            searchPane.updateWordResult(wordResult, lineResult);
            assertEquals(1, lineResult.getWordResults().size());
            assertEquals(2, wordResult.getLineOffset());
            
            // Test updating word result with a null word result
            searchPane.updateWordResult(null, lineResult);
            assertEquals(1, lineResult.getWordResults().size());
            
            // Test updating word result with a null line result
            searchPane.updateWordResult(wordResult, null);
            assertEquals(0, lineResult.getWordResults().size());
        } catch (BadLocationException e) {
            fail("Exception should not be thrown");
        }
    }

}
