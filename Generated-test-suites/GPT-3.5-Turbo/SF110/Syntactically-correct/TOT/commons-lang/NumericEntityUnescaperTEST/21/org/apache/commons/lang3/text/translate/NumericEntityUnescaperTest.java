import org.junit.*;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    // Test case for isSet() method
    @Test
    public void testIsSet() {
        NumericEntityUnescaper neUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        assertTrue(neUnescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(neUnescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));        
        assertFalse(neUnescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    // Test case for translate() method with typical use case
    @Test
    public void testTranslateTypicalUseCase() {
        NumericEntityUnescaper neUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();
        int result = neUnescaper.translate(input, 0, new java.io.StringWriter(output));
        
        assertEquals(6, input.length());
        assertEquals(1, output.length());
        assertEquals('A', output.charAt(0));
        assertEquals(6, result);
    }

    // Test case for translate() method with edge case
    @Test
    public void testTranslateEdgeCase() {
        NumericEntityUnescaper neUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder("&#65536;");
        StringBuilder output = new StringBuilder();
        int result = neUnescaper.translate(input, 0, new java.io.StringWriter(output));
        
        assertEquals(8, input.length());
        assertEquals(2, output.length());
        assertEquals('\ud800', output.charAt(0));
        assertEquals('\udc00', output.charAt(1));
        assertEquals(8, result);
    }

    // Test case for translate() method with error scenario
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateErrorScenario() throws java.io.IOException {
        NumericEntityUnescaper neUnescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder output = new StringBuilder();
        neUnescaper.translate(input, 0, new java.io.StringWriter(output));
    }
}