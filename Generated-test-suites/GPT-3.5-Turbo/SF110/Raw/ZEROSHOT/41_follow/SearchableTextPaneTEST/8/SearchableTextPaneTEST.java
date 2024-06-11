
package ghm.follow.search;

import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

  @Test
  public void testGetScrollableTracksViewportWidth() {
    Font font = new Font("Arial", Font.PLAIN, 12);
    SearchableTextPane textPane = new SearchableTextPane(font, 8);
    boolean result = textPane.getScrollableTracksViewportWidth();
    assertTrue(result);
  }

  @Test
  public void testHighlight() {
    Font font = new Font("Arial", Font.PLAIN, 12);
    SearchableTextPane textPane = new SearchableTextPane(font, 8);
    List<LineResult> lineResults = textPane.highlight("test", 0);
    assertNotNull(lineResults);
  }

  @Test
  public void testAddHighlight() {
    Font font = new Font("Arial", Font.PLAIN, 12);
    SearchableTextPane textPane = new SearchableTextPane(font, 8);
    try {
      textPane.addHighlight(0, 5);
    } catch (BadLocationException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testRemoveHighlights() {
    Font font = new Font("Arial", Font.PLAIN, 12);
    SearchableTextPane textPane = new SearchableTextPane(font, 8);
    textPane.removeHighlights();
    // If no exception is thrown, test passes
  }

  @Test
  public void testSearch() {
    Font font = new Font("Arial", Font.PLAIN, 12);
    SearchableTextPane textPane = new SearchableTextPane(font, 8);
    int result = textPane.search("test");
    assertEquals(-1, result);
  }

  @Test
  public void testSearchWithPosition() {
    Font font = new Font("Arial", Font.PLAIN, 12);
    SearchableTextPane textPane = new SearchableTextPane(font, 8);
    int result = textPane.search("test", 0);
    assertEquals(-1, result);
  }

  @Test
  public void testConvertWords2Lines() {
    Font font = new Font("Arial", Font.PLAIN, 12);
    SearchableTextPane textPane = new SearchableTextPane(font, 8);
    List<WordResult> words = new ArrayList<>();
    WordResult word1 = new WordResult(0, 5);
    WordResult word2 = new WordResult(10, 15);
    words.add(word1);
    words.add(word2);
    List<LineResult> lineResults;
    try {
      lineResults = textPane.convertWords2Lines(words);
      assertNotNull(lineResults);
      assertEquals(2, lineResults.size());
    } catch (BadLocationException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testUpdateWordResult() {
    Font font = new Font("Arial", Font.PLAIN, 12);
    SearchableTextPane textPane = new SearchableTextPane(font, 8);
    LineResult lineResult = new LineResult(1, 0, 5);
    WordResult wordResult = new WordResult(0, 5);
    try {
      textPane.updateWordResult(wordResult, lineResult);
      assertEquals(1, wordResult.parent.lineNumber);
      assertEquals(0, wordResult.lineOffset);
    } catch (BadLocationException e) {
      fail("Exception should not be thrown");
    }
  }

}
