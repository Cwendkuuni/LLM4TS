import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Font;
import java.util.ArrayList;
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
    public void testGetScrollableTracksViewportWidth() {
        // Test when parent is null
        boolean expectedResult = true;
        boolean actualResult = textPane.getScrollableTracksViewportWidth();
        assertEquals(expectedResult, actualResult);
        
        // Test when parent size is greater than preferred size
        textPane.setPreferredSize(new Dimension(200, 300));
        JPanel parent = new JPanel();
        parent.add(textPane);
        expectedResult = true;
        actualResult = textPane.getScrollableTracksViewportWidth();
        assertEquals(expectedResult, actualResult);
        
        // Test when parent size is smaller than preferred size
        parent.setPreferredSize(new Dimension(150, 250));
        expectedResult = false;
        actualResult = textPane.getScrollableTracksViewportWidth();
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testHighlight() {
        String term = "hello";
        int flags = 1;
        List<LineResult> expectedResult = new ArrayList<>();
        List<LineResult> actualResult = textPane.highlight(term, flags);
        assertEquals(expectedResult, actualResult);
        
        term = "world";
        flags = 2;
        actualResult = textPane.highlight(term, flags);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testAddHighlight() {
        int start = 0;
        int length = 5;
        try {
            textPane.addHighlight(start, length);
            // Check if the highlight is added successfully
        } catch (BadLocationException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testRemoveHighlights() {
        textPane.removeHighlights();
        // Check if all highlights are removed successfully
    }
    
    @Test
    public void testSearch() {
        String term = "hello";
        int expectedResult = -1;
        int actualResult = textPane.search(term);
        assertEquals(expectedResult, actualResult);
        
        term = "";
        expectedResult = -1;
        actualResult = textPane.search(term);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testSearchWithStartPosition() {
        String term = "hello";
        int startPos = 5;
        int expectedResult = -1;
        int actualResult = textPane.search(term, startPos);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = new ArrayList<>();
        List<LineResult> expectedResult = new ArrayList<>();
        List<LineResult> actualResult = null;
        try {
            actualResult = textPane.convertWords2Lines(words);
            assertEquals(expectedResult, actualResult);
        } catch (BadLocationException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(0, 0, 10);
        try {
            textPane.updateWordResult(wordResult, lineResult);
            // Check if wordResult is correctly updated with line information
        } catch (BadLocationException e) {
            fail("Exception thrown");
        }
    }
}