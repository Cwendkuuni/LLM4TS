import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

    private SearchableTextPane searchableTextPane;

    @Before
    public void setUp() {
        searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
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
        assertThrows(BadLocationException.class, () -> searchableTextPane.addHighlight(-1, 10));
    }

    @Test
    public void testRemoveHighlights() {
        searchableTextPane.removeHighlights();
    }

    @Test
    public void testSearch() {
        assertEquals(-1, searchableTextPane.search("example"));
    }

    @Test
    public void testSearchWithStartPosition() {
        assertEquals(-1, searchableTextPane.search("example", 10));
    }

    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = new ArrayList<>();
        words.add(new WordResult(0, 5));
        assertDoesNotThrow(() -> searchableTextPane.convertWords2Lines(words));
    }

    @Test
    public void testUpdateWordResult() {
        WordResult wordResult = new WordResult(0, 5);
        LineResult lineResult = new LineResult(0, 0, 10);
        assertDoesNotThrow(() -> searchableTextPane.updateWordResult(wordResult, lineResult));
    }

    @Test
    public void testSearchWithBadLocationException() {
        Logger mockedLogger = mock(Logger.class);
        when(mockedLogger.log(Level.WARNING, "BadLocationException in SearchableTextPane", any(Exception.class)))
            .thenReturn(null);

        searchableTextPane.log = mockedLogger;
        assertEquals(-1, searchableTextPane.search("example", -1));
    }
}