import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateNumericEntityWithHexValue() {
 String input = "&xe2ff;"; // Hex value for character U+FBFF
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals("FA FE", out.getBuffer().toString());
 assertEquals(6, length);
 }

 @Test
 public void testTranslateNumericEntityWithDecValue() {
 String input = "&#128512;"; // Decimal value for character U+2105 (Å)
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals("Å", out.getBuffer().toString());
 assertEquals(1, length);
 }

 @Test
 public void testTranslateNumericEntityWithHexValueMissingSemicolon() {
 String input = "&xe2ff"; // Hex value for character U+FBFF, missing semicolon
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals("", out.getBuffer().toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslateNumericEntityWithInvalidHexValue() {
 String input = "&xABC;"; // Invalid hex value
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals("", out.getBuffer().toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslateNumericEntityWithInvalidDecValue() {
 String input = "&#zab;"; // Invalid decimal value
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer out = new StringWriter();
 int length = unescaper.translate(input, 0, out);
 assertEquals("", out.getBuffer().toString());
 assertEquals(0, length);
 }
}