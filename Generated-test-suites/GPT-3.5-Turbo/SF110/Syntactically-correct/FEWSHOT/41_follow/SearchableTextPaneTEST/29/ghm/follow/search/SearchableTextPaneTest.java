package ghm.follow.search;

import static org.junit.Assert.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import org.junit.Before;
import org.junit.Test;

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
    public void testGetScrollableTracksViewportWidth_parentNull() {
        assertNull(textPane.getParent());
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_widthLessThanOrEqualToParent() {
        JPanel parent = new JPanel();
        Dimension parentSize = new Dimension(500, 200);
        parent.setSize(parentSize);
        textPane.setParent(parent);
        ComponentUI ui = textPane.getUI();
        int textPaneWidth = parentSize.width - 200;
        ui.setPreferredSize(textPane, new Dimension(textPaneWidth, 100));
        
        assertEquals(textPaneWidth, ui.getPreferredSize(textPane).width);
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testGetScrollableTracksViewportWidth_widthGreaterThanParent() {
        JPanel parent = new JPanel();
        Dimension parentSize = new Dimension(200, 200);
        parent.setSize(parentSize);
        textPane.setParent(parent);
        ComponentUI ui = textPane.getUI();
        int textPaneWidth = parentSize.width + 200;
        ui.setPreferredSize(textPane, new Dimension(textPaneWidth, 100));
        
        assertEquals(textPaneWidth, ui.getPreferredSize(textPane).width);
        assertFalse(textPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight_noTerm() {
        String term = null;
        int flags = 0;
        
        List<LineResult> lineResults = textPane.highlight(term, flags);
        
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_emptyTerm() {
        String term = "";
        int flags = 0;
        
        List<LineResult> lineResults = textPane.highlight(term, flags);
        
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_termNotFound() {
        String term = "search";
        int flags = 0;
        
        textPane.setText("This is a test");
        List<LineResult> lineResults = textPane.highlight(term, flags);
        
        assertTrue(lineResults.isEmpty());
    }
    
    @Test
    public void testHighlight_termFound() {
        String term = "search";
        int flags = 0;
        
        textPane.setText("This is a test search term");
        List<LineResult> lineResults = textPane.highlight(term, flags);
        
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getLine());
        assertEquals(15, lineResult.getLineStartOffset());
        assertEquals(31, lineResult.getLineEndOffset());
        assertEquals(1, lineResult.getWordResults().size());
        WordResult wordResult = lineResult.getWordResults().get(0);
        assertEquals(15, wordResult.getStart());
        assertEquals(21, wordResult.getEnd());
        assertEquals(1, wordResult.getLineNumber());
        assertEquals(15, wordResult.getLineOffset());
    }
    
    @Test
    public void testRemoveHighlights() {
        String term = "search";
        int flags = 0;
        
        textPane.setText("This is a test search term");
        textPane.highlight(term, flags);
        
        textPane.removeHighlights();
        
        assertEquals(0, textPane.getHighlighter().getHighlights().length);
    }
    
    @Test
    public void testSearch_nullTerm() {
        String term = null;
        
        assertEquals(-1, textPane.search(term));
        assertNull(textPane.getLastSearchTerm());
        assertEquals(-1, textPane.getLastSearchPos());
    }
    
    @Test
    public void testSearch_emptyTerm() {
        String term = "";
        
        assertEquals(-1, textPane.search(term));
        assertNull(textPane.getLastSearchTerm());
        assertEquals(-1, textPane.getLastSearchPos());
    }
    
    @Test
    public void testSearch_sameTermAsLastSearchTerm() {
        String term = "search";
        int startPos = 0;
        
        textPane.setLastSearchTerm(term);
        textPane.setLastSearchPos(10);
        
        int expectedPos = 10 + term.length();
        assertEquals(expectedPos, textPane.search(term, startPos));
        assertEquals(term, textPane.getLastSearchTerm());
        assertEquals(expectedPos, textPane.getLastSearchPos());
    }
    
    @Test
    public void testSearch_newTerm() {
        String term = "search";
        int startPos = 0;
        
        textPane.setLastSearchTerm("test");
        textPane.setLastSearchPos(5);
        
        int expectedPos = textPane.getText().indexOf(term, startPos);
        assertEquals(expectedPos, textPane.search(term, startPos));
        assertEquals(term, textPane.getLastSearchTerm());
        assertEquals(expectedPos, textPane.getLastSearchPos());
    }
    
    @Test
    public void testSearch_noMatch() {
        String term = "search";
        
        textPane.setText("This is a test");
        assertEquals(-1, textPane.search(term));
        assertNull(textPane.getLastSearchTerm());
        assertEquals(-1, textPane.getLastSearchPos());
    }
    
    @Test
    public void testSearch_match() {
        String term = "search";
        
        textPane.setText("This is a test search term");
        assertEquals(15, textPane.search(term));
        assertEquals(term, textPane.getLastSearchTerm());
        assertEquals(15, textPane.getLastSearchPos());
    }
    
    @Test
    public void testConvertWords2Lines_emptyWords() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        
        List<LineResult> lines = textPane.convertWords2Lines(words);
        
        assertTrue(lines.isEmpty());
    }
    
    @Test
    public void testConvertWords2Lines_singleLine() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(15, 21));
        words.add(new WordResult(27, 32));
        
        textPane.setText("This is a test search term");
        List<LineResult> lines = textPane.convertWords2Lines(words);
        
        assertEquals(1, lines.size());
        LineResult lineResult = lines.get(0);
        assertEquals(1, lineResult.getLine());
        assertEquals(0, lineResult.getLineStartOffset());
        assertEquals(textPane.getText().length(), lineResult.getLineEndOffset());
        assertEquals(2, lineResult.getWordResults().size());
        assertEquals(1, lineResult.getWordResults().get(0).getLineNumber());
        assertEquals(1, lineResult.getWordResults().get(0).getLineOffset());
        assertEquals(1, lineResult.getWordResults().get(1).getLineNumber());
        assertEquals(1, lineResult.getWordResults().get(1).getLineOffset());
    }
    
    @Test
    public void testConvertWords2Lines_multipleLines() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(15, 21));
        words.add(new WordResult(27, 32));
        words.add(new WordResult(37, 41));
        
        textPane.setText("This is a test\nsearch term\nin multiple lines");
        List<LineResult> lines = textPane.convertWords2Lines(words);
        
        assertEquals(3, lines.size());
        LineResult lineResult1 = lines.get(0);
        assertEquals(1, lineResult1.getLine());
        assertEquals(0, lineResult1.getLineStartOffset());
        assertEquals(15, lineResult1.getLineEndOffset());
        assertEquals(1, lineResult1.getWordResults().size());
        assertEquals(1, lineResult1.getWordResults().get(0).getLineNumber());
        assertEquals(1, lineResult1.getWordResults().get(0).getLineOffset());
        LineResult lineResult2 = lines.get(1);
        assertEquals(2, lineResult2.getLine());
        assertEquals(16, lineResult2.getLineStartOffset());
        assertEquals(29, lineResult2.getLineEndOffset());
        assertEquals(1, lineResult2.getWordResults().size());
        assertEquals(2, lineResult2.getWordResults().get(0).getLineNumber());
        assertEquals(16, lineResult2.getWordResults().get(0).getLineOffset());
        LineResult lineResult3 = lines.get(2);
        assertEquals(3, lineResult3.getLine());
        assertEquals(30, lineResult3.getLineStartOffset());
        assertEquals(textPane.getText().length(), lineResult3.getLineEndOffset());
        assertEquals(1, lineResult3.getWordResults().size());
        assertEquals(3, lineResult3.getWordResults().get(0).getLineNumber());
        assertEquals(30, lineResult3.getWordResults().get(0).getLineOffset());
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        WordResult wordResult = new WordResult(15, 21);
        LineResult lineResult = new LineResult(1, 0, 30);
        
        textPane.updateWordResult(wordResult, lineResult);
        
        assertEquals(1, lineResult.getWordResults().size());
        assertEquals(1, wordResult.getLineNumber());
        assertEquals(1, wordResult.getLineOffset());
    }
}