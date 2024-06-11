import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_ValidDecimalEntity_ReturnsEntityValue() {
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(3, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_ValidHexadecimalEntity_ReturnsEntityValue() {
        StringBuilder input = new StringBuilder("&#x41;");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(4, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_InvalidEntity_ReturnsZero() {
        StringBuilder input = new StringBuilder("&#invalid;");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_EmptyInput_ReturnsZero() {
        StringBuilder input = new StringBuilder("");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_NoEntity_ReturnsZero() {
        StringBuilder input = new StringBuilder("No entity");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

}