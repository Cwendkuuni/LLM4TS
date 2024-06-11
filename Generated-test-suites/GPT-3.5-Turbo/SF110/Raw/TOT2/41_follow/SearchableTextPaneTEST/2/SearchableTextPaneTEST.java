
import static org.junit.Assert.*;
import org.junit.Test;

public class SearchableTextPaneTest {
  
  @Test
  public void testGetScrollableTracksViewportWidth() {
    SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    assertTrue(pane.getScrollableTracksViewportWidth());
  }
  
  @Test
  public void testHighlight() {
    SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    assertTrue(pane.highlight("test", 0).isEmpty());
  }
  
  @Test
  public void testAddHighlight() {
    SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    assertThrows(BadLocationException.class, () -> pane.addHighlight(0, 5));
  }
  
  @Test
  public void testRemoveHighlights() {
    SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    pane.removeHighlights();
    // test removed successfully
  }
  
  @Test
  public void testSearch() {
    SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    assertEquals(-1, pane.search("test"));
  }
  
  @Test
  public void testConvertWords2Lines() {
    SearchableTextPane pane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    assertThrows(BadLocationException.class, () -> pane.convertWords2Lines(new ArrayList<>()));
  }
  
}
