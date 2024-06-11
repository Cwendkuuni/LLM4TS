
package ghm.follow.search;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Font;
import java.awt.Color;
import javax.swing.text.DefaultHighlighter;
import java.util.List;

public class SearchableTextPaneTest {
  
  private SearchableTextPane searchableTextPane;
  
  @Before
  public void setUp() {
    Font font = new Font("Arial", Font.PLAIN, 12);
    int tabSize = 4;
    searchableTextPane = new SearchableTextPane(font, tabSize);
  }
  
  @Test
  public void testGetScrollableTracksViewportWidth() {
    boolean result = searchableTextPane.getScrollableTracksViewportWidth();
    assertFalse(result);
  }
  
  @Test
  public void testHighlight() {
    String term = "search";
    int flags = 0;
    List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
    assertNotNull(lineResults);
  }
  
  @Test
  public void testAddHighlight() throws BadLocationException {
    int start = 0;
    int length = 5;
    searchableTextPane.addHighlight(start, length);
    DefaultHighlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
    assertNotNull(highlights);
  }
  
  @Test
  public void testRemoveHighlights() {
    searchableTextPane.removeHighlights();
    DefaultHighlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
    assertEquals(0, highlights.length);
  }
  
  @Test
  public void testSearch() {
    String term = "search";
    int result = searchableTextPane.search(term);
    assertEquals(-1, result);
  }
  
  @Test
  public void testSearchWithStartPosition() {
    String term = "search";
    int startPos = 10;
    int result = searchableTextPane.search(term, startPos);
    assertEquals(-1, result);
  }
  
  @Test
  public void testConvertWords2Lines() throws BadLocationException {
    List<WordResult> words = new ArrayList<>();
    WordResult word = new WordResult(0, 5);
    words.add(word);
    List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
    assertNotNull(lineResults);
  }
  
  @Test
  public void testUpdateWordResult() throws BadLocationException {
    WordResult wordResult = new WordResult(0, 5);
    LineResult lineResult = new LineResult(0, 0, 10);
    searchableTextPane.updateWordResult(wordResult, lineResult);
    assertEquals(1, wordResult.parent.lineNumber);
  }
}
