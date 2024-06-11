package ghm.follow.search;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.logging.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        // case 1: parent is null
        stp.setParent(null);
        assertFalse(stp.getScrollableTracksViewportWidth());
        
        // case 2: parent is not null but preferred width is less than size width
        stp.setParent(new JPanel());
        stp.setSize(new Dimension(100, 100));
        stp.setPreferredSize(new Dimension(200, 100));
        assertFalse(stp.getScrollableTracksViewportWidth());
        
        // case 3: parent is not null and preferred width is greater than size width
        stp.setSize(new Dimension(200, 100));
        assertTrue(stp.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        // case 1: term is null
        List<LineResult> lineResults = stp.highlight(null, 0);
        assertEquals(0, lineResults.size());
        
        // case 2: term is empty
        lineResults = stp.highlight("", 0);
        assertEquals(0, lineResults.size());
        
        // case 3: term length is greater than 0 and results are found
        lineResults = stp.highlight("hello", 0);
        assertFalse(lineResults.isEmpty());
        assertEquals(1, lineResults.size());
        
        // case 4: term length is greater than 0 and results are not found
        lineResults = stp.highlight("world", 0);
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testAddHighlight() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        try {
            // case 1: valid start and length
            stp.addHighlight(0, 5);
            assertTrue(stp.getHighlighter().getHighlights().length > 0);
            
            // case 2: start and length that exceeds the document length
            stp.addHighlight(100, 100);
            assertTrue(stp.getHighlighter().getHighlights().length > 0);
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        // case 1: highlights exist
        try {
            stp.addHighlight(0, 5);
            stp.addHighlight(10, 15);
            stp.removeHighlights();
            assertEquals(0, stp.getHighlighter().getHighlights().length);
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
        
        // case 2: no highlights exist
        assertEquals(0, stp.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testSearch() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        // case 1: term is null
        int searchPos = stp.search(null);
        assertEquals(-1, searchPos);
        
        // case 2: term is empty
        searchPos = stp.search("");
        assertEquals(-1, searchPos);
        
        // case 3: term equals to lastSearchTerm
        try {
            Field lastSearchPosField = stp.getClass().getDeclaredField("lastSearchPos");
            lastSearchPosField.setAccessible(true);
            lastSearchPosField.set(stp, 5);
            Field lastSearchTermField = stp.getClass().getDeclaredField("lastSearchTerm");
            lastSearchTermField.setAccessible(true);
            lastSearchTermField.set(stp, "hello");
            searchPos = stp.search("hello");
            assertEquals(10, searchPos);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Unexpected exception");
        }
        
        // case 4: term not equals to lastSearchTerm
        searchPos = stp.search("world");
        assertEquals(0, searchPos);
    }
    
    @Test
    public void testSearchWithStartPos() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        // case 1: term is null
        int searchPos = stp.search(null, 0);
        assertEquals(-1, searchPos);
        
        // case 2: term is empty
        searchPos = stp.search("", 0);
        assertEquals(0, searchPos);
        
        // case 3: term is found starting from startPos
        searchPos = stp.search("hello", 0);
        assertEquals(0, searchPos);
        
        // case 4: term is not found starting from startPos
        searchPos = stp.search("world", 8);
        assertEquals(-1, searchPos);
    }
    
    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        // case 1: empty word list
        List<LineResult> lines = null;
        try {
            lines = stp.convertWords2Lines(new ArrayList<WordResult>());
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
        assertTrue(lines.isEmpty());
        
        // case 2: non-empty word list
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult("hello", 0, 5));
        words.add(new WordResult("world", 5, 10));
        
        try {
            lines = stp.convertWords2Lines(words);
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
        assertFalse(lines.isEmpty());
        assertEquals(1, lines.size());
    }
    
    @Test
    public void testUpdateWordResult() {
        SearchableTextPane stp = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        // case 1: non-null wordResult and lineResult
        WordResult wordResult = new WordResult("hello", 0, 5);
        LineResult lineResult = new LineResult(0, 0, 10);
        
        try {
            stp.updateWordResult(wordResult, lineResult);
            
            List<WordResult> wordResults = lineResult.getWordResults();
            assertFalse(wordResults.isEmpty());
            assertEquals(1, wordResults.size());
            assertEquals(1, wordResult.getParent().getLineNumber());
            assertEquals(0, wordResult.getLineOffset());
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
        
        // case 2: null wordResult and lineResult
        try {
            stp.updateWordResult(null, null);
            List<WordResult> wordResults = lineResult.getWordResults();
            assertTrue(wordResults.isEmpty());
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }
    }
}