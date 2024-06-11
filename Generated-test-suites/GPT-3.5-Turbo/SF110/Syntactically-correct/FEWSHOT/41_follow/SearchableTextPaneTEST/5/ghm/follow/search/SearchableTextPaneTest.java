package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;
    
    @Before
    public void setup() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_withParent_returnsFalse() {
        JTextArea parent = new JTextArea();
        parent.setSize(500, 300);
        searchableTextPane.setParent(parent);
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        
        assertEquals(false, result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_withoutParent_returnsTrue() {
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        
        assertEquals(true, result);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_parentWidthLessThanUIWidth_returnsTrue() {
        JTextArea parent = new JTextArea();
        parent.setSize(200, 300);
        searchableTextPane.setParent(parent);
        
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        
        assertEquals(true, result);
    }
    
    @Test
    public void testHighlight_emptyTerm_returnsEmptyList() {
        String term = "";
        int flags = 0;
        
        List<LineResult> result = searchableTextPane.highlight(term, flags);
        
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testHighlight_nullTerm_returnsEmptyList() {
        String term = null;
        int flags = 0;
        
        List<LineResult> result = searchableTextPane.highlight(term, flags);
        
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testHighlight_termNotFoundInText_returnsEmptyList() {
        String term = "apple";
        int flags = 0;
        searchableTextPane.setText("banana");
        
        List<LineResult> result = searchableTextPane.highlight(term, flags);
        
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testHighlight_singleLineTermFoundInText_returnsLineResultWithWordResult() {
        String term = "apple";
        int flags = 0;
        searchableTextPane.setText("I have an apple");
        
        List<LineResult> result = searchableTextPane.highlight(term, flags);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        
        LineResult lineResult = result.get(0);
        assertNotNull(lineResult);
        
        List<WordResult> wordResults = lineResult.getWordResults();
        assertNotNull(wordResults);
        assertEquals(1, wordResults.size());
        
        WordResult wordResult = wordResults.get(0);
        assertNotNull(wordResult);
        assertEquals(7, wordResult.start);
        assertEquals(12, wordResult.end);
    }
    
    @Test
    public void testHighlight_multiLineTermFoundInText_returnsLineResultsWithWordResults() {
        String term = "apple";
        int flags = 0;
        searchableTextPane.setText("I have an apple\nI love eating apples");
        
        List<LineResult> result = searchableTextPane.highlight(term, flags);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        
        LineResult lineResult1 = result.get(0);
        assertNotNull(lineResult1);
        
        List<WordResult> wordResults1 = lineResult1.getWordResults();
        assertNotNull(wordResults1);
        assertEquals(1, wordResults1.size());
        
        WordResult wordResult1 = wordResults1.get(0);
        assertNotNull(wordResult1);
        assertEquals(7, wordResult1.start);
        assertEquals(12, wordResult1.end);
        
        LineResult lineResult2 = result.get(1);
        assertNotNull(lineResult2);
        
        List<WordResult> wordResults2 = lineResult2.getWordResults();
        assertNotNull(wordResults2);
        assertEquals(1, wordResults2.size());
        
        WordResult wordResult2 = wordResults2.get(0);
        assertNotNull(wordResult2);
        assertEquals(15, wordResult2.start);
        assertEquals(20, wordResult2.end);
    }
    
    @Test
    public void testHighlight_multipleOccurrencesOfTermInText_returnsLineResultsWithWordResults() {
        String term = "apple";
        int flags = 0;
        searchableTextPane.setText("I have an apple and I love eating apples");
        
        List<LineResult> result = searchableTextPane.highlight(term, flags);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        
        LineResult lineResult = result.get(0);
        assertNotNull(lineResult);
        
        List<WordResult> wordResults = lineResult.getWordResults();
        assertNotNull(wordResults);
        assertEquals(2, wordResults.size());
        
        WordResult wordResult1 = wordResults.get(0);
        assertNotNull(wordResult1);
        assertEquals(7, wordResult1.start);
        assertEquals(12, wordResult1.end);
        
        WordResult wordResult2 = wordResults.get(1);
        assertNotNull(wordResult2);
        assertEquals(33, wordResult2.start);
        assertEquals(38, wordResult2.end);
    }
    
    @Test
    public void testRemoveHighlights() {
        searchableTextPane.setText("I have an apple");
        String term = "apple";
        int flags = 0;
        searchableTextPane.highlight(term, flags);
        
        searchableTextPane.removeHighlights();
        
        DefaultHighlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertEquals(0, highlights.length);
    }
    
    @Test
    public void testSearch_emptyTerm_returnsNegativeOne() {
        String term = "";
        
        int result = searchableTextPane.search(term);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_nullTerm_returnsNegativeOne() {
        String term = null;
        
        int result = searchableTextPane.search(term);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_termNotFoundInText_returnsNegativeOne() {
        String term = "apple";
        searchableTextPane.setText("banana");
        
        int result = searchableTextPane.search(term);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testSearch_singleLineTermFoundInText_returnsStartPositionOfTerm() {
        String term = "apple";
        searchableTextPane.setText("I have an apple");
        
        int result = searchableTextPane.search(term);
        
        assertEquals(7, result);
    }
    
    @Test
    public void testSearch_multiLineTermFoundInText_returnsStartPositionOfFirstOccurrenceOfTerm() {
        String term = "apple";
        searchableTextPane.setText("I have an apple\nI love eating apples");
        
        int result = searchableTextPane.search(term);
        
        assertEquals(7, result);
    }
    
    @Test
    public void testSearch_multipleOccurrencesOfTermInText_returnsStartPositionOfFirstOccurrenceOfTerm() {
        String term = "apple";
        searchableTextPane.setText("I have an apple and I love eating apples");
        
        int result = searchableTextPane.search(term);
        
        assertEquals(7, result);
    }
    
    @Test
    public void testSearch_sameTermTwiceInText_returnsStartPositionOfSecondOccurrenceOfTerm() {
        String term1 = "apple";
        String term2 = "apple";
        searchableTextPane.setText("I have an apple and I love eating apples");
        
        searchableTextPane.search(term1);
        int result = searchableTextPane.search(term2);
        
        assertEquals(33, result);
    }
    
    @Test
    public void testSearch_sameTermWithDifferentCaseInText_returnsStartPositionOfTerm() {
        String term1 = "apple";
        String term2 = "Apple";
        searchableTextPane.setText("I have an apple and I love eating apples");
        
        searchableTextPane.search(term1);
        int result = searchableTextPane.search(term2);
        
        assertEquals(7, result);
    }
    
}