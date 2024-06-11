import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate() throws IOException {
        // Basic test for translating numeric entities
        StringBuilder input = new StringBuilder("&amp;&#65;&#x41;&#123;");
        StringBuilder expectedOutput = new StringBuilder("&A;A{");
        StringBuilder actualOutput = new StringBuilder();

        numericEntityUnescaper.translate(input, 0, actualOutput);

        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslateWithNoNumericEntity() throws IOException {
        // Test for translating when there is no numeric entity
        StringBuilder input = new StringBuilder("This is a test");
        StringBuilder expectedOutput = new StringBuilder("This is a test");
        StringBuilder actualOutput = new StringBuilder();

        numericEntityUnescaper.translate(input, 0, actualOutput);

        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslateWithFullHexEntity() throws IOException {
        // Test for translating full hexadecimal numeric entity
        StringBuilder input = new StringBuilder("&#x1d4e5;");
        StringBuilder expectedOutput = new StringBuilder("𝓥");
        StringBuilder actualOutput = new StringBuilder();

        numericEntityUnescaper.translate(input, 0, actualOutput);

        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslateWithInvalidNumericEntity() throws IOException {
        // Test for handling invalid numeric entity
        StringBuilder input = new StringBuilder("&#10a;");
        StringBuilder expectedOutput = new StringBuilder("&#10a;");
        StringBuilder actualOutput = new StringBuilder();

        numericEntityUnescaper.translate(input, 0, actualOutput);

        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    public void testTranslateWithCharacterCodeBeyondFFFF() throws IOException {
        // Test for handling character code beyond 0xFFFF
        StringBuilder input = new StringBuilder("&#123456;");
        StringBuilder expectedOutput = new StringBuilder("\uD808\uDF56");
        StringBuilder actualOutput = new StringBuilder();

        numericEntityUnescaper.translate(input, 0, actualOutput);

        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

}