import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.util.logging.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

    private SearchableTextPane textPane;
    private Font font;
    private int tabSize;
    
    @Before
    public void setUp() {
        font = new Font("Arial", Font.PLAIN, 12);
        tabSize = 4;
        textPane = new SearchableTextPane(font, tabSize);
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Test case for when parent is null
        assertFalse(textPane.getScrollableTracksViewportWidth());

        // Test case for when parent is present and text width is greater than parent width
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        assertTrue(textPane.getScrollableTracksViewportWidth());

        // Test case for when parent is present and text width is less than parent width
        scrollPane.setPreferredSize(new Dimension(500, 200));
        assertFalse(textPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() {
        // Test case when term is null
        assertEquals(0, textPane.highlight(null, 0).size());

        // Test case when term has no match
        String term = "hello";
        int flags = 0;
        assertEquals(0, textPane.highlight(term, flags).size());

        // Test case when term has multiple matches
        term = "test";
        String text = "This is a test. This is another test.";
        textPane.setText(text);
        assertEquals(2, textPane.highlight(term, flags).size());

        // Test case when term has match but is case sensitive
        term = "test";
        flags = Pattern.CASE_INSENSITIVE;
        assertEquals(0, textPane.highlight(term, flags).size());

        // Test case when term has match and is case insensitive
        term = "test";
        flags = Pattern.CASE_INSENSITIVE;
        textPane.setText(text);
        assertEquals(2, textPane.highlight(term, flags).size());

        // Test case when term has match and is case insensitive, also testing word start and end positions
        term = "test";
        flags = Pattern.CASE_INSENSITIVE;
        textPane.setText(text);
        List<LineResult> lineResults = textPane.highlight(term, flags);
        assertEquals(2, lineResults.size());
        assertEquals(10, lineResults.get(0).getWordResults().get(0).getStart());
        assertEquals(14, lineResults.get(0).getWordResults().get(0).getEnd());
        assertEquals(26, lineResults.get(1).getWordResults().get(0).getStart());
        assertEquals(30, lineResults.get(1).getWordResults().get(0).getEnd());
    }
    
    @Test
    public void testRemoveHighlights() {
        // Add highlights first
        String term = "test";
        String text = "This is a test. This is another test.";
        textPane.setText(text);
        textPane.highlight(term, 0);
        
        // Test case for removing highlights
        textPane.removeHighlights();
        assertEquals(0, textPane.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testSearch() {
        // Test case when term is null
        assertEquals(-1, textPane.search(null));
        
        // Test case when term has no match
        String term = "hello";
        assertEquals(-1, textPane.search(term));
        
        // Test case when term has a match
        term = "test";
        String text = "This is a test. This is another test.";
        textPane.setText(text);
        assertEquals(10, textPane.search(term));
        
        // Test case when term has a match and start position is provided
        int startPos = 15;
        assertEquals(26, textPane.search(term, startPos));
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        // Add highlights first
        String term = "test";
        String text = "This is a test. This is another test.";
        textPane.setText(text);
        textPane.highlight(term, 0);
        
        // Test case for converting words to lines
        assertEquals(2, textPane.convertWords2Lines(textPane.getHighlighter().getHighlights()).size());
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        // Add highlights first
        String term = "test";
        String text = "This is a test. This is another test.";
        textPane.setText(text);
        textPane.highlight(term, 0);
        
        // Test case for updating word result
        WordResult wordResult = new WordResult(10, 14);
        LineResult lineResult = new LineResult(0, 0, 20);
        textPane.updateWordResult(wordResult, lineResult);
        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(1, lineResult.getWordResults().get(0).getParent().getLineNumber());
        assertEquals(0, lineResult.getWordResults().get(0).getLineOffset());

    }
}