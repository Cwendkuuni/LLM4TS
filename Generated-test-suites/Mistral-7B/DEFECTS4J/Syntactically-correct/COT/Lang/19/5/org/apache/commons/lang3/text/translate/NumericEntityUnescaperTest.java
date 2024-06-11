import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_SimpleNumericEntity_Decimal() {
 String input = "&#123;";
 char expected = '\u007B';

 int actual = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());

 assertEquals(expected, actual);
 }

 @Test
 public void testTranslate_SimpleNumericEntity_Hexadecimal() {
 String input = "&#x7B;";
 char expected = '\u007B';

 int actual = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());

 assertEquals(expected, actual);
 }

 @Test
 public void testTranslate_ComplexNumericEntity_Decimal() {
 String input = "&#1234;";
 int expected = 0x1234;
 char[] actualChars = new char[2];
 int actualLength = unescaper.translate(new String(input), 0, new StringWriter(actualChars, 1024).getBuffer());

 assertEquals(expected, Integer.valueOf(new String(actualChars), 10).intValue());
 assertEquals(2, actualLength);
 }

 @Test
 public void testTranslate_ComplexNumericEntity_Hexadecimal() {
 String input = "&#x1234;";
 int expected = 0x1234;
 char[] actualChars = new char[2];
 int actualLength = unescaper.translate(new String(input), 0, new StringWriter(actualChars, 1024).getBuffer());

 assertEquals(expected, Integer.valueOf(new String(actualChars), 16).intValue());
 assertEquals(2, actualLength);
 }

 @Test(expected = IOException.class)
 public void testTranslate_NumericEntityWithInvalidFormat() {
 String input = "&#abc;";
 unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 }

 @Test(expected = IOException.class)
 public void testTranslate_NumericEntityWithNoSemicolon() {
 String input = "&abc;";
 unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_NumericEntityWithInvalidValue_Decimal() {
 String input = "&#123456789012345678901234567890;";
 unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_NumericEntityWithInvalidValue_Hexadecimal() {
 String input = "&#xabcdefghijklmnopqrstuvwxyz;";
 unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_NumericEntityWithInvalidValue_InvalidLength() {
 String input = "&#[xAB];";
 unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());
 }

 @Test
 public void testTranslate_EmptyInput() {
 String input = "";
 int actual = unescaper.translate(new String(input), 0, new StringWriter(null).getBuffer());

 assertEquals(0, actual);
 }

 @Test
 public void testTranslate_NullInput() {
 char[] actualChars = new char[2];
 int actualLength = unescaper.translate(null, 0, new StringWriter(actualChars, 1024).getBuffer());

 assertEquals(0, actualLength);
 }
}