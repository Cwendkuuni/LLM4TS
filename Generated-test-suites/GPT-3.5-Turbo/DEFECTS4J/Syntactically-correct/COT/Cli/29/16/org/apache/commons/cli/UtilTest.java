import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Arrange
        String input = "--test";
        String expectedOutput = "test";

        // Execute
        String actualOutput = Util.stripLeadingHyphens(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingHyphens_NoHyphens() {
        // Arrange
        String input = "test";
        String expectedOutput = "test";

        // Execute
        String actualOutput = Util.stripLeadingHyphens(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
  
    @Test
    public void testStripLeadingHyphens_EmptyString() {
        // Arrange
        String input = "";
        String expectedOutput = "";

        // Execute
        String actualOutput = Util.stripLeadingHyphens(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
  
    @Test
    public void testStripLeadingHyphens_NullString() {
        // Arrange
        String input = null;
        String expectedOutput = null;

        // Execute
        String actualOutput = Util.stripLeadingHyphens(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
  
    @Test
    public void testStripLeadingHyphens_MultipleHyphens() {
        // Arrange
        String input = "--test";
        String expectedOutput = "test";

        // Execute
        String actualOutput = Util.stripLeadingHyphens(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Arrange
        String input = "\"test\"";
        String expectedOutput = "test";

        // Execute
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        // Arrange
        String input = "test";
        String expectedOutput = "test";

        // Execute
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
  
    @Test
    public void testStripLeadingAndTrailingQuotes_EmptyString() {
        // Arrange
        String input = "";
        String expectedOutput = "";

        // Execute
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
  
    @Test
    public void testStripLeadingAndTrailingQuotes_NullString() {
        // Arrange
        String input = null;
        String expectedOutput = null;

        // Execute
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
  
    @Test
    public void testStripLeadingAndTrailingQuotes_InsideQuotes() {
        // Arrange
        String input = "\"te\"st\"";
        String expectedOutput = "te\"st";

        // Execute
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
  
    @Test
    public void testStripLeadingAndTrailingQuotes_TrailingQuoteOnly() {
        // Arrange
        String input = "test\"";
        String expectedOutput = "test\"";

        // Execute
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
  
    @Test
    public void testStripLeadingAndTrailingQuotes_OnlyQuotes() {
        // Arrange
        String input = "\"test\"";
        String expectedOutput = "test";

        // Execute
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

}