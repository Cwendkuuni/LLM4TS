import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.awt.Font;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SearchableTextPaneTest {

    private SearchableTextPane searchPane;
  
    @Parameter(0)
    public String term;

    @Parameter(1)
    public int flags;

    @Parameter(2)
    public List<LineResult> expectedLineResults;

    @Parameters
    public static Collection<Object[]> data() {
        // Test data for highlighting method
        Object[][] data = new Object[][] {
            {"hello", 0, new ArrayList<LineResult>()},
            {"hello", 1, Arrays.asList(new LineResult(0, 0, 5))},
            {"World", 0, new ArrayList<LineResult>()},
            {"World", 1, Arrays.asList(new LineResult(1, 6, 11))},
            {null, 0, new ArrayList<LineResult>()}
        };
        return Arrays.asList(data);
    }

    @Before
    public void setUp() {
        searchPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
    }

    @Test
    public void testHighlight() {
        List<LineResult> lineResults = searchPane.highlight(term, flags);
        assertEquals(expectedLineResults, lineResults);
    }

    // Test cases for addHighlight method
    @Test
    public void testAddHighlight() {
        // Edge case: length is zero
        try {
            searchPane.addHighlight(0, 0);
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }

        // Typical case: length is non-zero
        try {
            searchPane.addHighlight(0, 5);
        } catch (BadLocationException e) {
            fail("Unexpected BadLocationException");
        }

        // Error scenario: start is greater than end
        try {
            searchPane.addHighlight(5, 0);
            fail("Expected BadLocationException");
        } catch (BadLocationException e) {
            // Exception expected
        }
    }

    @Test
    public void testRemoveHighlights() {
        searchPane.removeHighlights();
        // TODO: Add assertions to verify if highlights have been removed
    }

    // Test cases for search method
    @Test
    public void testSearchString() {
        int pos = searchPane.search(term);
        // TODO: Add assertions to verify if pos is correct based on test data
    }

    @Test
    public void testSearchStringWithStartPos() {
        int startPos = 5;
        int pos = searchPane.search(term, startPos);
        // TODO: Add assertions to verify if pos is correct based on test data
    }

    // Test cases for convertWords2Lines method
    @Test
    public void testConvertWords2Lines() {
        // TODO: Add test cases to verify the conversion of WordResult list to LineResult list
    }

    // Test case for updateWordResult method
    @Test
    public void testUpdateWordResult() {
        // TODO: Add test case to verify the update of WordResult and LineResult objects
    }

    // TODO: Add more test cases for other methods in the class

}