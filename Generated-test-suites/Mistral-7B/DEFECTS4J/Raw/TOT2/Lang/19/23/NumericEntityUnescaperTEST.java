import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        StringWriter out = new StringWriter();

        // Basic test
        input.append("&amp;");
        unescaper.translate(input, 0, out);
        assertEquals("&amp;", input.toString());
        assertEquals("&amp;", out.toString());

        // Test hex and dec entity values
        input.setLength(0);
        input.append("&#[x41];");
        unescaper.translate(input, 0, out);
        assertEquals("&A;", input.toString());
        assertEquals('A', out.toString().charAt(0));
        assertEquals('', out.toString().charAt(1));

        input.setLength(0);
        input.append("&#[x27A2];");
        unescaper.translate(input, 0, out);
        assertEquals("&feeff;u27A2;", input.toString());
        assertEquals('\u27A2', out.toString().charAt(0));
        assertEquals('\ufffe', out.toString().charAt(1));
        assertEquals('\ufffe', out.toString().charAt(2));
        assertEquals('\u27a2', out.toString().charAt(3));

        input.setLength(0);
        input.append("&#[x1F600];");
        unescaper.translate(input, 0, out);
        assertEquals("&hexF09F&x98&x80;", input.toString());
        assertEquals('\uD83C', out.toString().charAt(0));
        assertEquals('\uDF09', out.toString().charAt(1));
        assertEquals('\uD83E', out.toString().charAt(2));
        assertEquals('\uDD14', out.toString().charAt(3));

        // Test entity value out of range
        input.setLength(0);
        input.append("&#[100000];");
        unescaper.translate(input, 0, out);
        assertEquals("", input.toString());
        assertEquals("", out.toString());
    }
}