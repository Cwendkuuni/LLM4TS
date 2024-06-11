import org.junit.*;
import java.awt.*;
import javax.swing.text.*;
import java.util.*;

public class SearchableTextPaneTest {
   
   @Test
   public void testGetScrollableTracksViewportWidth() {
       SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
       
       boolean result = textPane.getScrollableTracksViewportWidth();
       
       Assert.assertTrue(result);
   }
   
   @Test
   public void testHighlight() {
       SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
       List<LineResult> expectedResult = new ArrayList<LineResult>();
       
       List<LineResult> result = textPane.highlight("test", 0);
       
       Assert.assertEquals(expectedResult, result);
   }
   
   @Test
   public void testAddHighlight() throws BadLocationException {
       SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
       
       textPane.addHighlight(5, 10);
       
       Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
       
       Assert.assertEquals(1, highlights.length);
       Assert.assertEquals(5, highlights[0].getStartOffset());
       Assert.assertEquals(15, highlights[0].getEndOffset());
   }
   
   @Test
   public void testRemoveHighlights() throws BadLocationException {
       SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
       
       textPane.addHighlight(5, 10);
       textPane.removeHighlights();
       
       Highlighter.Highlight[] highlights = textPane.getHighlighter().getHighlights();
       
       Assert.assertEquals(0, highlights.length);
   }
   
   @Test
   public void testSearch_sameTerm() {
       SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
       textPane.search("test");
       
       int result = textPane.search("test");
       
       Assert.assertEquals(5, result);
   }
   
   @Test
   public void testSearch_differentTerm() {
       SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
       textPane.search("test");
       
       int result = textPane.search("different");
       
       Assert.assertEquals(0, result);
   }
   
   @Test
   public void testSearch_noTerm() {
       SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
       
       int result = textPane.search("");
       
       Assert.assertEquals(-1, result);
   }
   
   @Test
   public void testSearch_startPos() {
       SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
       textPane.search("test");
       
       int result = textPane.search("test", 5);
       
       Assert.assertEquals(-1, result);
   }
   
   @Test
   public void testConvertWords2Lines() throws BadLocationException {
       SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
       List<WordResult> words = new ArrayList<WordResult>();
       WordResult wordResult = new WordResult(10, 15);
       words.add(wordResult);
       
       List<LineResult> expectedResult = new ArrayList<LineResult>();
       LineResult lineResult = new LineResult(0, 0, 20);
       lineResult.addWord(wordResult);
       expectedResult.add(lineResult);
       
       List<LineResult> result = textPane.convertWords2Lines(words);
       
       Assert.assertEquals(expectedResult, result);
   }
   
   @Test
   public void testUpdateWordResult() throws BadLocationException {
       SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
       LineResult lineResult = new LineResult(0, 0, 20);
       WordResult wordResult = new WordResult(10, 15);
       
       textPane.updateWordResult(wordResult, lineResult);
       
       Assert.assertEquals(1, wordResult.parent.lineNumber);
       Assert.assertEquals(0, wordResult.lineOffset);
   }
   
}