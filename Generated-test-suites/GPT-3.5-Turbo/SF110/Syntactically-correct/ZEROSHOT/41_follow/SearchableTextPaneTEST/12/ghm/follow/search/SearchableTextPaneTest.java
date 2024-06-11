package ghm.follow.search;

import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class SearchableTextPaneTest {

  // Initialize SearchableTextPane object
  SearchableTextPane searchableTextPane;

  // Initialize Mockito annotation
  @Mock
  JTextArea textArea;

  @Before
  public void setUp() {
    // Create instance of SearchableTextPane with mock font and tab size
    searchableTextPane = new SearchableTextPane(Mockito.mock(Font.class), 4);
  }

  @After
  public void tearDown() {
    searchableTextPane = null;
  }

  @Test
  public void testGetScrollableTracksViewportWidth() {
    // Test when parent component is null
    assertTrue(searchableTextPane.getScrollableTracksViewportWidth());

    // Test when parent component is not null, and text width is less than or equal to parent component width
    Mockito.when(searchableTextPane.getParent()).thenReturn(Mockito.mock(Component.class));
    ComponentUI ui = Mockito.mock(ComponentUI.class);
    Mockito.when(searchableTextPane.getUI()).thenReturn(ui);
    Mockito.when(ui.getPreferredSize(searchableTextPane)).thenReturn(new Dimension(100, 50));
    Mockito.when(searchableTextPane.getParent().getSize()).thenReturn(new Dimension(100, 50));
    assertTrue(searchableTextPane.getScrollableTracksViewportWidth());

    // Test when parent component is not null, and text width is greater than parent component width
    Mockito.when(ui.getPreferredSize(searchableTextPane)).thenReturn(new Dimension(200, 50));
    assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
  }

  @Test
  public void testHighlight() {
    // Test when term is null or empty
    assertTrue(searchableTextPane.highlight(null, 0).isEmpty());
    assertTrue(searchableTextPane.highlight("", 0).isEmpty());

    // Test when term is not null or empty
    String term = "test";
    assertFalse(searchableTextPane.highlight(term, 0).isEmpty());
  }

  @Test
  public void testAddHighlight() {
    // Test adding highlight with valid start and length
    try {
      searchableTextPane.addHighlight(0, 5);
    } catch (BadLocationException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testRemoveHighlights() {
    // Test remove all highlights
    searchableTextPane.removeHighlights();
  }

  @Test
  public void testSearch() {
    // Test when term is null or empty
    assertEquals(-1, searchableTextPane.search(null));
    assertEquals(-1, searchableTextPane.search(""));

    // Test when term is not null or empty
    String term = "test";
    int startPos = 0;
    assertEquals(0, searchableTextPane.search(term, startPos));
  }

  @Test
  public void testConvertWords2Lines() {
    // Test converting words to lines with an empty list of words
    assertTrue(searchableTextPane.convertWords2Lines(new ArrayList<>()).isEmpty());

    // Test converting words to lines with a list of words
    List<WordResult> words = new ArrayList<>();
    words.add(new WordResult(0, 4));
    try {
      assertFalse(searchableTextPane.convertWords2Lines(words).isEmpty());
    } catch (BadLocationException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testUpdateWordResult() {
    // Test updating word result
    WordResult wordResult = new WordResult(0, 4);
    LineResult lineResult = new LineResult(0, 0, 10);
    try {
      searchableTextPane.updateWordResult(wordResult, lineResult);
    } catch (BadLocationException e) {
      fail("Exception should not be thrown");
    }
  }
}