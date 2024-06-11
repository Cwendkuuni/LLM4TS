import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * JUnit 4 test file for NumericEntityUnescaper class.
 */
public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateWithDecimalEntity() throws Exception {
 String input = "&amp;#128;";
 String expectedOutput = "\u0080";

 CharSequence cs = new String(input);
 Writer out = new StringWriter();
 int length = unescaper.translate(cs, 0, out);
 assertEquals("Unexpected output length.", expectedOutput.length(), out.toString().length());
 assertEquals("Unexpected translation result.", expectedOutput, out.toString());
 }

 @Test
 public void testTranslateWithHexEntity() throws Exception {
 String input = "&amp;#x41;";
 String expectedOutput = "\u002A";

 CharSequence cs = new String(input);
 Writer out = new StringWriter();
 int length = unescaper.translate(cs, 0, out);
 assertEquals("Unexpected output length.", expectedOutput.length(), out.toString().length());
 assertEquals("Unexpected translation result.", expectedOutput, out.toString());
 }

 @Test
 public void testTranslateWithMiddleOfStringEntity() throws Exception {
 String input = "abc&amp;#128def";
 String expectedOutput = "abc\u0080def";

 CharSequence cs = new String(input);
 Writer out = new StringWriter();
 int length = unescaper.translate(cs, 3, out);
 assertEquals("Unexpected output length.", expectedOutput.length() - 3, out.toString().length());
 assertEquals("Unexpected translation result.", expectedOutput.substring(0, 3) + out.toString() + expectedOutput.substring(expectedOutput.length() - 1), cs.subSequence(0, 3).toString() + out.toString() + cs.subSequence(cs.length() - 1).toString());
 }

 @Test(expected = IOException.class)
 public void testTranslateWithInvalidEntity() throws Exception {
 String input = "&amp;#INVALID;";
 CharSequence cs = new String(input);
 Writer out = new StringWriter();
 unescaper.translate(cs, 0, out);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateWithInvalidCharAtIndex() throws Exception {
 String input = "&amp;";
 CharSequence cs = new String(input);
 Writer out = new StringWriter();
 unescaper.translate(cs, 1, out);
 }

 @Test
 public void testTranslateWithEmptyInput() throws Exception {
 String input = "";
 CharSequence cs = new String(input);
 Writer out = new StringWriter();
 int length = unescaper.translate(cs, 0, out);
 assertEquals("Unexpected output length.", 0, length);
 assertEquals("Unexpected translation result.", "", out.toString());
 }

 @Test
 public void testTranslateWithEmptyInputAndOutOfBoundsIndex() throws Exception {
 String input = "";
 CharSequence cs = new String(input);
 Writer out = new StringWriter();
 int length = unescaper.translate(cs, cs.length(), out);
 assertEquals("Unexpected output length.", 0, length);
 assertEquals("Unexpected translation result.", "", out.toString());
 }

 @Test
 public void testTranslateWithInputAsWriter() throws Exception {
 Writer input = new StringWriter();
 input.write("abc&amp;#128def");
 Writer output = new StringWriter();
 unescaper.translate(input, 0, output);
 String expectedOutput = "abc\u0080def";
 assertEquals("Unexpected translation result.", expectedOutput, output.toString());
 }

 @Test
 public void testTranslateWithInputAsCharSequence() throws Exception {
 CharSequence input = new StringBuilder("abc&amp;#128def").toString();
 Writer output = new StringWriter();
 unescaper.translate(input, 0, output);
 String expectedOutput = "abc\u0080def";
 assertEquals("Unexpected translation result.", expectedOutput, output.toString());
 }

 @Test
 public void testTranslateWithInputAsCharSequenceAndOutOfBoundsIndex() throws Exception {
 CharSequence input = new StringBuilder("abc&amp;#128def").toString();
 Writer output = new StringWriter();
 int length = unescaper.translate(input, input.length(), output);
 assertEquals("Unexpected output length.", 0, length);
 assertEquals("Unexpected translation result.", "", output.toString());
 }
}