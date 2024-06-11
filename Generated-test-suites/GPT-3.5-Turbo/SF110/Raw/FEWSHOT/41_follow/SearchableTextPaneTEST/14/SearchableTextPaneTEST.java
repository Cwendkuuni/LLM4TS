package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;
    private final String sampleText = "This is a sample text";
    private final String searchTerm = "sample";
    
    @Before
    public void setUp() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        int tabSize = 4;
        searchableTextPane = new SearchableTextPane(font, tabSize);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        searchableTextPane.setSize(100, 50);
        assertNotNull(searchableTextPane.getScrollableTracksViewportWidth());
    }
    
    @Test
    public void testHighlight() throws BadLocationException {
        Document document = searchableTextPane.getDocument();
        document.insertString(0, sampleText, null);
        
        List<LineResult> lineResults = searchableTextPane.highlight(searchTerm, 0);
        
        assertEquals(1, lineResults.size());
        assertEquals(0, lineResults.get(0).getLine());
        assertEquals(10, lineResults.get(0).getStartOffset());
        assertEquals(16, lineResults.get(0).getEndOffset());
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        Document document = searchableTextPane.getDocument();
        document.insertString(0, sampleText, null);
        
        searchableTextPane.addHighlight(0, 4);
        
        DefaultHighlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertEquals(1, highlights.length);
        assertEquals(0, highlights[0].getStartOffset());
        assertEquals(4, highlights[0].getEndOffset());
        assertEquals(DefaultHighlighter.DefaultHighlightPainter.class, highlights[0].getPainter().getClass());
    }
    
    @Test
    public void testRemoveHighlights() throws BadLocationException {
        Document document = searchableTextPane.getDocument();
        document.insertString(0, sampleText, null);
        
        searchableTextPane.addHighlight(0, 4);
        searchableTextPane.addHighlight(6, 9);
        
        searchableTextPane.removeHighlights();
        
        DefaultHighlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
        assertEquals(0, highlights.length);
    }
    
    @Test
    public void testSearch() throws BadLocationException {
        Document document = searchableTextPane.getDocument();
        document.insertString(0, sampleText, null);
        
        int searchPos = searchableTextPane.search(searchTerm);
        
        assertEquals(10, searchPos);
        assertNotNull(searchableTextPane.getLastSearchTerm());
        assertEquals(searchTerm, searchableTextPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithStartPos() throws BadLocationException {
        Document document = searchableTextPane.getDocument();
        document.insertString(0, sampleText, null);
        
        int searchPos = searchableTextPane.search(searchTerm, 11);
        
        assertEquals(-1, searchPos);
        assertNull(searchableTextPane.getLastSearchTerm());
    }
    
    @Test
    public void testSearchWithNullTerm() throws BadLocationException {
        Document document = searchableTextPane.getDocument();
        document.insertString(0, sampleText, null);
        
        int searchPos = searchableTextPane.search(null);
        
        assertEquals(-1, searchPos);
        assertNull(searchableTextPane.getLastSearchTerm());
    }
    
    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        Document document = searchableTextPane.getDocument();
        document.insertString(0, sampleText, null);
        
        List<WordResult> wordResults = new ArrayList<>();
        int startOffset = 0;
        int endOffset = 4;
        WordResult wordResult = new WordResult(startOffset, endOffset);
        wordResults.add(wordResult);
        
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
        
        assertEquals(1, lineResults.size());
        assertEquals(0, lineResults.get(0).getLine());
        assertEquals(0, lineResults.get(0).getStartOffset());
        assertEquals(16, lineResults.get(0).getEndOffset());
        assertEquals(1, lineResults.get(0).getWordResults().size());
        assertEquals(wordResult, lineResults.get(0).getWordResults().get(0));
    }
    
    @Test
    public void testUpdateWordResult() throws BadLocationException {
        Document document = searchableTextPane.getDocument();
        document.insertString(0, sampleText, null);
        
        LineResult lineResult = new LineResult(0, 0, 16);
        
        int startOffset = 0;
        int endOffset = 4;
        WordResult wordResult = new WordResult(startOffset, endOffset);
        
        searchableTextPane.updateWordResult(wordResult, lineResult);
        
        assertEquals(0, wordResult.getLineOffset());
        assertEquals(lineResult, wordResult.getParent());
    }
}