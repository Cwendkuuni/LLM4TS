import org.junit.*;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

    @Test
    public void testConstructor() {
        // Create an instance of SearchableTextPane using the constructor
        // Assert that the font and tab size are properly set
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        // Create an instance of SearchableTextPane
        // Invoke the getScrollableTracksViewportWidth() method
        // Assert the returned value matches the expected behavior
    }

    @Test
    public void testHighlight() {
        // Create an instance of SearchableTextPane
        // Invoke the highlight() method with different input values
        // Assert the returned list of LineResult objects matches the expected results
    }

    @Test
    public void testRemoveHighlights() {
        // Create an instance of SearchableTextPane
        // Apply highlights to the text
        // Invoke the removeHighlights() method
        // Assert that all highlights are removed
    }

    @Test
    public void testSearch() {
        // Create an instance of SearchableTextPane
        // Search for a term that is present in the text
        // Assert the returned position matches the expected position
        // Search for a term that is not present in the text
        // Assert the returned position is -1
    }

    @Test
    public void testSearchWithStartPosition() {
        // Create an instance of SearchableTextPane
        // Search for a term starting from a specific position
        // Assert the returned position matches the expected position
    }

    // Additional test methods for other functionality or edge cases

}