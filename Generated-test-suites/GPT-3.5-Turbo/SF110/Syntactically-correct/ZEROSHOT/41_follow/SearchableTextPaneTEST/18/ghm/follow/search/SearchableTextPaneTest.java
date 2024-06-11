import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;
import javax.swing.plaf.*;
import javax.swing.*;

import org.junit.*;

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
    assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
  }
  
  @Test
  public void testHighlight() {
    List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
    assertNotNull(lineResults);
  }
  
  @Test
  public void testAddHighlight() {
    try {
      searchableTextPane.addHighlight(0, 5);
    } catch (BadLocationException e) {
      fail("Exception thrown");
    }
  }
  
  @Test
  public void testRemoveHighlights() {
    searchableTextPane.removeHighlights();
  }
  
  @Test
  public void testSearch() {
    int searchPos = searchableTextPane.search("test");
    assertEquals(-1, searchPos);
  }
  
  @Test
  public void testSearchWithStartPos() {
    int searchPos = searchableTextPane.search("test", 0);
    assertEquals(-1, searchPos);
  }
  
  @Test
  public void testConvertWords2Lines() {
    List<WordResult> words = new ArrayList<WordResult>();
    LineResult lineResult = new LineResult(0, 0, 10);
    words.add(new WordResult(0, 5));
    
    try {
      List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
      assertNotNull(lineResults);
    } catch (BadLocationException e) {
      fail("Exception thrown");
    }
  }
  
  @Test
  public void testUpdateWordResult() {
    WordResult wordResult = new WordResult(0, 5);
    LineResult lineResult = new LineResult(0, 0, 10);
    
    try {
      searchableTextPane.updateWordResult(wordResult, lineResult);
      assertEquals(1, wordResult.parent.lineNumber);
      assertEquals(0, wordResult.lineOffset);
    } catch (BadLocationException e) {
      fail("Exception thrown");
    }
  }
}