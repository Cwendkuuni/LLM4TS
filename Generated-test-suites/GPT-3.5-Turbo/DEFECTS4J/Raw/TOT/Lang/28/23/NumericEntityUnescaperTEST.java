import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_withValidInput_shouldTranslateNumericEntity() {
        StringBuilder input = new StringBuilder();
        input.append("This is a test string &#65;. It should be converted to 'A'.");
        
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a test string A. It should be converted to 'A'.");

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 21, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }

        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testTranslate_withIncompleteNumericEntity_shouldNotTranslate() {
        StringBuilder input = new StringBuilder();
        input.append("This is a test string &#. It should not be converted.");

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a test string &#. It should not be converted.");

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 21, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }

        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testTranslate_withInvalidHexNumericEntity_shouldNotTranslate() {
        StringBuilder input = new StringBuilder();
        input.append("This is a test string &#xZZ;. It should not be converted.");

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a test string &#xZZ;. It should not be converted.");

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 21, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }

        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testTranslate_withInvalidDecNumericEntity_shouldNotTranslate() {
        StringBuilder input = new StringBuilder();
        input.append("This is a test string &#99. It should not be converted.");

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a test string &#99. It should not be converted.");

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 21, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }

        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testTranslate_withEmptyInput_shouldNotTranslate() {
        StringBuilder input = new StringBuilder();
        input.append("");

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("");

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 0, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }

        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testTranslate_withInvalidIndex_shouldNotTranslate() {
        StringBuilder input = new StringBuilder();
        input.append("This is a test string &#65;. It should be converted to 'A'.");

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("This is a test string &#65;. It should be converted to 'A'.");

        StringBuilder output = new StringBuilder();
        try {
            unescaper.translate(input, 50, output);
        } catch (IOException e) {
            fail("IOException occurred");
        }

        assertEquals(expectedOutput.toString(), output.toString());
    }

}