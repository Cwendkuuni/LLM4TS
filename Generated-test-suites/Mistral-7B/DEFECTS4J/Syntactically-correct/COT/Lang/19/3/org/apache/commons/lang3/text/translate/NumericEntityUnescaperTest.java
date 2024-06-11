import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;

 @Before
 public void setUp() {
 unescaper = new NumericEntityUnescaper();
 }

 // Testing the translate method with valid inputs
 @Test
 public void testTranslateValidInput() {
 CharSequence input = "&amp;&#32;&#10;&#13;&#35;&#xA9;&#x1F4;&#x1F1F;";
 Writer out = new StringBuilder();
 int length = unescaper.translate(input, 0, out);
 assertEquals("& amp;  \n \r % © ✇;", out.toString());
 assertEquals(11, length);
 }

 // Testing the translate method with hexadecimal input
 @Test
 public void testTranslateHexInput() {
 CharSequence input = "&amp;&#x26;&#x0A;&#x0D;&#x23;&#xA9;&#x1F4;&#x1F1F;";
 Writer out = new StringBuilder();
 int length = unescaper.translate(input, 0, out);
 assertEquals("& amp;  \n \r % © ✇;", out.toString());
 assertEquals(11, length);
 }

 // Testing the translate method with invalid input - missing &
 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMissingAmpersand() {
 CharSequence input = "#32;";
 Writer out = new StringBuilder();
 int length = unescaper.translate(input, 0, out);
 fail("Expected IllegalArgumentException but got: " + length);
 }

 // Testing the translate method with invalid input - missing semicolon
 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMissingSemicolon() {
 CharSequence input = "&amp;&#32#";
 Writer out = new StringBuilder();
 int length = unescaper.translate(input, 0, out);
 fail("Expected IllegalArgumentException but got: " + length);
 }

 // Testing the translate method with invalid input - non hexadecimal character
 @Test(expected = IllegalArgumentException.class)
 public void testTranslateInvalidCharacterInput() {
 CharSequence input = "&amp;&#abc;";
 Writer out = new StringBuilder();
 int length = unescaper.translate(input, 0, out);
 fail("Expected IllegalArgumentException but got: " + length);
 }

 // Testing the translate method with invalid input - negative number
 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNegativeNumberInput() {
 CharSequence input = "&amp;&#-1;";
 Writer out = new StringBuilder();
 int length = unescaper.translate(input, 0, out);
 fail("Expected IllegalArgumentException but got: " + length);
 }

 // Testing the translate method with invalid input - number larger than 0xFFFF
 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNumberLargerThan0xFFFF() {
 CharSequence input = "&amp;&#65536;";
 Writer out = new StringBuilder();
 int length = unescaper.translate(input, 0, out);
 fail("Expected IllegalArgumentException but got: " + length);
 }
}