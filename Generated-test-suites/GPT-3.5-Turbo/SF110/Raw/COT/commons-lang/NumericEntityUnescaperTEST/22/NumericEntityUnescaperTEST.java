
import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        sb.append("&amp;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_WithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder sb = new StringBuilder();
        sb.append("&#65;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_WithErrorIfNoSemiColonOption() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder sb = new StringBuilder();
        sb.append("&#65");
        StringWriter writer = new StringWriter();
        unescaper.translate(sb, 0, writer);
    }

    @Test
    public void testTranslate_WithHexadecimalValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        sb.append("&#x41;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_WithLargeEntityValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        sb.append("&#65536;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(10, result);
        assertEquals("\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslate_WithInvalidEntityValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        sb.append("&#123456;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(sb, 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

}
