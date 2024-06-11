import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper testObj;

    @Before
    public void setup() {
        testObj = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_validDecimalEntity_writesCharacterToOutput() throws Exception {
        StringBuilder input = new StringBuilder("&#65;");

        StringBuilder output = new StringBuilder();
        int result = testObj.translate(input, 0, output);

        assertEquals(5, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_validHexEntity_writesCharacterToOutput() throws Exception {
        StringBuilder input = new StringBuilder("&#x41;");

        StringBuilder output = new StringBuilder();
        int result = testObj.translate(input, 0, output);

        assertEquals(7, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_validDecimalEntityWithoutSemicolon_writesCharacterToOutput() throws Exception {
        StringBuilder input = new StringBuilder("&#65");

        StringBuilder output = new StringBuilder();
        int result = testObj.translate(input, 0, output);

        assertEquals(4, result);
        assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_invalidEntity_doesNotWriteCharacterToOutput() throws Exception {
        StringBuilder input = new StringBuilder("&#ABC;");

        StringBuilder output = new StringBuilder();
        int result = testObj.translate(input, 0, output);

        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_invalidEntityWithoutSemicolon_doesNotWriteCharacterToOutput() throws Exception {
        StringBuilder input = new StringBuilder("&#ABC");

        StringBuilder output = new StringBuilder();
        int result = testObj.translate(input, 0, output);

        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_invalidEntityWithNumericValue_doesNotWriteCharacterToOutput() throws Exception {
        StringBuilder input = new StringBuilder("&#123;");

        StringBuilder output = new StringBuilder();
        int result = testObj.translate(input, 0, output);

        assertEquals(0, result);
        assertEquals("", output.toString());
    }

}