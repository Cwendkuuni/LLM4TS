import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testNumericEntityUnescaper_Decimal_Small() throws IOException {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#128;"); // ampersand & character

 Writer outputWriter = new StringBuilder();
 int charactersTranslated = unescaper.translate(input, 0, outputWriter);

 assertEquals("Character '&' should have been translated to its corresponding Unicode character. Found: ", (char) 128, outputWriter.toString().charAt(0));
 assertEquals("Expected 1 character to be translated. Found: ", 1, charactersTranslated);
 }

 @Test
 public void testNumericEntityUnescaper_Hex_Small() throws IOException {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#x41;"); // ampersand & character in hexadecimal

 Writer outputWriter = new StringBuilder();
 int charactersTranslated = unescaper.translate(input, 0, outputWriter);

 assertEquals("Character '&' should have been translated to its corresponding Unicode character. Found: ", 'A', outputWriter.toString().charAt(0));
 assertEquals("Expected 1 character to be translated. Found: ", 1, charactersTranslated);
 }

 @Test
 public void testNumericEntityUnescaper_Decimal_Large() throws IOException {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#65533;"); // character with codepoint 65533 (unicode character: PARASOL WINDSOR)

 Writer outputWriter = new StringBuilder();
 int charactersTranslated = unescaper.translate(input, 0, outputWriter);

 assertNotEquals("Expected a character with a different codepoint to be translated. Found: ", (char) 128, outputWriter.toString().charAt(0));
 assertEquals("Expected 1 character to be translated. Found: ", 1, charactersTranslated);
 }

 @Test
 public void testNumericEntityUnescaper_Hex_Large() throws IOException {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#x10FFFF;"); // highest unicode codepoint

 Writer outputWriter = new StringBuilder();
 int charactersTranslated = unescaper.translate(input, 0, outputWriter);

 fail("Expected a NumberFormatException to be thrown. Found: "); // This test should fail since the highest unicode codepoint (1114111) is out of the range of the method
 }

 @Test(expected = NumberFormatException.class)
 public void testNumericEntityUnescaper_Invalid() throws IOException {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#abcd;"); // invalid entity

 Writer outputWriter = new StringBuilder();
 int charactersTranslated = unescaper.translate(input, 0, outputWriter);
 }

 @Test
 public void testNumericEntityUnescaper_MissingSemicolon() throws IOException {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 input.append("&amp;#123;#"); // entity with missing semicolon

 Writer outputWriter = new StringBuilder();
 int charactersTranslated = unescaper.translate(input, 0, outputWriter);

 fail("Expected a NumberFormatException to be thrown. Found: "); // This test should fail since the method correctly handles entities without a semicolon on the end
 }

 @Test
 public void testNumericEntityUnescaper_Empty() throws IOException {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 StringBuilder input = new StringBuilder();
 Writer outputWriter = new StringBuilder();

 int charactersTranslated = unescaper.translate(input, 0, outputWriter);

 assertEquals("Expected 0 characters to be translated. Found: ", 0, charactersTranslated);
 }
}