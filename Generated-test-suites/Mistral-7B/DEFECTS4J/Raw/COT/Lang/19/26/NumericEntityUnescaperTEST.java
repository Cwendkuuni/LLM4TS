import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 /**
 * Test basic functionality of the translate method with decimal entities
 */
 @Test
 public void testTranslateDecimalEntity() {
 CharSequence input = "&amp;#10; Hello World &#45; &#123; &#125;";
 String expectedOutput = "&amp; &#45; [[[[";
 String actualOutput = "";
 try {
 Writer out = new StringWriter();
 unescaper.translate(input, 0, out);
 actualOutput = out.toString();
 } catch (IOException e) {
 e.printStackTrace();
fail("IOException thrown in testTranslateDecimalEntity");
 }
 assertEquals(expectedOutput, actualOutput);
 }

 /**
 * Test basic functionality of the translate method with hex entities
 */
 @Test
 public void testTranslateHexEntity() {
 CharSequence input = "&amp;#x41; &amp;#x6C; &amp;#x6C; &amp;#x6F; &amp;#x20;";
 String expectedOutput = "ABC ";
 String actualOutput = "";
 try {
 Writer out = new StringWriter();
 unescaper.translate(input, 0, out);
 actualOutput = out.toString();
 } catch (IOException e) {
 e.printStackTrace();
fail("IOException thrown in testTranslateHexEntity");
 }
 assertEquals(expectedOutput, actualOutput);
 }

 /**
 * Test translate method with invalid input
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateInvalidInput() {
 CharSequence input = "&amp;#10; Hello World &amp;#45; &amp;#123; &amp;#125;";
 unescaper.translate(input, 10, null);
 }

 /**
 * Test translate method with invalid entity format
 */
 @Test
 public void testTranslateInvalidEntityFormat() {
 CharSequence input = "&amp;#abc; Hello World &amp;#45; &amp;#123; &amp;#125;";
 int index = 0;
 String expectedOutput = "";
 String actualOutput = "";
 try {
 Writer out = new StringWriter();
 unescaper.translate(input, index++, out);
 assertEquals("", actualOutput); // First entity is invalid
 actualOutput = out.toString();
 unescaper.translate(input, index, out);
 assertEquals("", actualOutput); // Subsequent entities are ignored
 } catch (IOException e) {
 e.printStackTrace();
fail("IOException thrown in testTranslateInvalidEntityFormat");
 }
 }

 /**
 * Test translate method with entity outside of Unicode range
 */
 @Test
 public void testTranslateEntityOutsideUnicodeRange() {
 CharSequence input = "&amp;#65536;";
 int index = 0;
 String expectedOutput = "";
 String actualOutput = "";
 try {
 Writer out = new StringWriter();
 unescaper.translate(input, index++, out);
 // Unicode character U+FFFF is not printable
 assertEquals("", actualOutput);
 } catch (IOException e) {
 e.printStackTrace();
fail("IOException thrown in testTranslateEntityOutsideUnicodeRange");
 }
 }
}