import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslateWithDecimalEntity() throws Exception {
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals("The result value is incorrect.", 4, result);
        assertEquals("The output value is incorrect.", "A", output.toString());
    }

    @Test
    public void testTranslateWithHexEntity() throws Exception {
        StringBuilder input = new StringBuilder("&#x41;");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals("The result value is incorrect.", 5, result);
        assertEquals("The output value is incorrect.", "A", output.toString());
    }

    @Test
    public void testTranslateWithIncompleteEntity() throws Exception {
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals("The result value is incorrect.", 0, result);
        assertEquals("The output value is incorrect.", "", output.toString());
    }

    @Test
    public void testTranslateWithNonNumericEntity() throws Exception {
        StringBuilder input = new StringBuilder("&#AB;");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals("The result value is incorrect.", 0, result);
        assertEquals("The output value is incorrect.", "", output.toString());
    }
}