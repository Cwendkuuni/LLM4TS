
package ghm.follow.search;

import org.junit.*;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;

public class SearchableTextPaneTest {
    
    private SearchableTextPane textPane;
    
    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        textPane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Typical use case: Component is null
        assertEquals(true, textPane.getScrollableTracksViewportWidth());
        
        // Typical use case: UI preferred width is less than parent size
        JPanel parent = new JPanel();
        parent.setSize(800, 600);
        textPane.setParent(parent);
        UIManager.put("Viewport.width", 500);
        assertEquals(true, textPane.getScrollableTracksViewportWidth());
        
        // Edge case: UI preferred width is equal to parent size
        parent.setSize(500, 600);
        assertEquals(true, textPane.getScrollableTracksViewportWidth());
        
        // Edge case: UI preferred width is greater than parent size
        parent.setSize(300, 600);
        assertEquals(false, textPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() {
        // Typical use case: Term is null
        assertEquals(new ArrayList<LineResult>(), textPane.highlight(null, 0));
        
        // Typical use case: Term is empty
        assertEquals(new ArrayList<LineResult>(), textPane.highlight("", 0));
        
        // Typical use case: Term is found in text
        textPane.setText("This is a test sentence.");
        LineResult lineResult = new LineResult(0, 0, 27);
        WordResult wordResult = new WordResult("test", 10, 13);
        lineResult.addWord(wordResult);
        List<LineResult> expectedLineResults = new ArrayList<LineResult>();
        expectedLineResults.add(lineResult);
        assertEquals(expectedLineResults, textPane.highlight("test", 0));
        
        // Edge case: Term is not found in text
        assertEquals(new ArrayList<LineResult>(), textPane.highlight("apple", 0));
        
        // Edge case: Flags are invalid
        assertEquals(new ArrayList<LineResult>(), textPane.highlight("test", -1));
    }
    
    @Test
    public void testRemoveHighlights() {
        // Typical use case: Remove all highlights
        textPane.addHighlight(0, 5);
        textPane.addHighlight(10, 15);
        textPane.removeHighlights();
        assertEquals(0, textPane.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testSearch() {
        // Typical use case: Term is null
        assertEquals(-1, textPane.search(null));
        
        // Typical use case: Term is empty
        assertEquals(-1, textPane.search(""));
        
        // Typical use case: Term is found in text
        textPane.setText("This is a test sentence.");
        assertEquals(10, textPane.search("test"));
        
        // Edge case: Term is not found in text
        assertEquals(-1, textPane.search("apple"));
    }
    
    @Test
    public void testSearchWithStartPos() {
        // Typical use case: Term is null
        assertEquals(-1, textPane.search(null, 0));
        
        // Typical use case: Term is empty
        assertEquals(-1, textPane.search("", 0));
        
        // Typical use case: Term is found in text starting at specified position
        textPane.setText("This is a test sentence.");
        assertEquals(10, textPane.search("test", 10));
        
        // Edge case: Term is not found in text starting at specified position
        assertEquals(-1, textPane.search("apple", 10));
    }
    
    @Test
    public void testConvertWords2Lines() {
        // Typical use case: Convert words to lines
        WordResult word1 = new WordResult("This", 0, 3);
        WordResult word2 = new WordResult("is", 5, 6);
        WordResult word3 = new WordResult("a", 8, 8);
        List<WordResult> words = new ArrayList<WordResult>();
        words.add(word1);
        words.add(word2);
        words.add(word3);
        LineResult lineResult = new LineResult(0, 0, 10);
        lineResult.addWord(word1);
        lineResult.addWord(word2);
        lineResult.addWord(word3);
        List<LineResult> expectedLineResults = new ArrayList<LineResult>();
        expectedLineResults.add(lineResult);
        try {
            assertEquals(expectedLineResults, textPane.convertWords2Lines(words));
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
    
    @Test
    public void testUpdateWordResult() {
        // Typical use case: Update the word result's line information
        WordResult wordResult = new WordResult("test", 10, 13);
        LineResult lineResult = new LineResult(0, 0, 27);
        try {
            textPane.updateWordResult(wordResult, lineResult);
            assertEquals(11, wordResult.parent.lineNumber);
            assertEquals(10, wordResult.getLineOffset());
        } catch (BadLocationException e) {
            fail("BadLocationException should not be thrown");
        }
    }
    
}
