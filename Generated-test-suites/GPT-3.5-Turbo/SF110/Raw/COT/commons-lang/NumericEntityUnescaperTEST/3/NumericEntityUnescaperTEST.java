
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test valid hexadecimal entity
        StringBuilder input1 = new StringBuilder("&x41;");
        StringWriter out1 = new StringWriter();
        int translated1 = unescaper.translate(input1, 0, out1);
        assertEquals("A", out1.toString());
        assertEquals(5, translated1);

        // Test valid decimal entity
        StringBuilder input2 = new StringBuilder("&#65;");
        StringWriter out2 = new StringWriter();
        int translated2 = unescaper.translate(input2, 0, out2);
        assertEquals("A", out2.toString());
        assertEquals(5, translated2);

        // Test invalid entity with missing semi-colon
        StringBuilder input3 = new StringBuilder("&#65");
        StringWriter out3 = new StringWriter();
        int translated3 = unescaper.translate(input3, 0, out3);
        assertEquals(0, translated3);

        // Test invalid entity with non-numeric characters
        StringBuilder input4 = new StringBuilder("&#xINVALID;");
        StringWriter out4 = new StringWriter();
        int translated4 = unescaper.translate(input4, 0, out4);
        assertEquals(0, translated4);

        // Test entity value greater than 65535
        StringBuilder input5 = new StringBuilder("&#65536;");
        StringWriter out5 = new StringWriter();
        int translated5 = unescaper.translate(input5, 0, out5);
        assertEquals("𐀀", out5.toString());
        assertEquals(8, translated5);

        // Test entity value less than or equal to 65535
        StringBuilder input6 = new StringBuilder("&#65;");
        StringWriter out6 = new StringWriter();
        int translated6 = unescaper.translate(input6, 0, out6);
        assertEquals("A", out6.toString());
        assertEquals(5, translated6);
    }
}

