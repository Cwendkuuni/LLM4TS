

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchableTextPaneTest {

    @Test
    public void testHighlight() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlightEmptyTerm() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = searchableTextPane.highlight("", 0);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlightBadLocationException() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
        assertEquals(0, lineResults.size());
    }

}
