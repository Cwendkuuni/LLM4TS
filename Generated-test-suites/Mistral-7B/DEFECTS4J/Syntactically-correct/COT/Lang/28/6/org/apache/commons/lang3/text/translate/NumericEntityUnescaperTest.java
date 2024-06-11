import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 /**
 * Test translate method with valid numeric entity.
 */
 @Test
 public void testTranslateValidNumericEntity() {
 CharSequence input = "&amp;#160;&#x2013;&#123;&#x1F44D;";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 //store the written characters for assertion
 char writtenChar = (char) c;
 }
 };

 int lengthBefore = outWriter.getBuffer().length();
 int translatedChars = unescaper.translate(input, 0, outWriter);
 int lengthAfter = outWriter.getBuffer().length();

 assertEquals("Expected 6 characters to be written.", 6, lengthAfter - lengthBefore);
 }

 /**
 * Test translate method with valid numeric entities in the middle of a string.
 */
 @Test
 public void testTranslateValidNumericEntitiesMiddleOfString() {
 CharSequence input = "Hello &amp;#160;&#x2013;World!";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 //store the written characters for assertion
 char writtenChar = (char) c;
 }
 };

 int lengthBefore = outWriter.getBuffer().length();
 int translatedChars = unescaper.translate(input, 7, outWriter);
 int lengthAfter = outWriter.getBuffer().length();

 assertEquals("Expected 2 characters to be written.", 2, lengthAfter - lengthBefore);
 }

 /**
 * Test translate method with invalid numeric entity.
 */
 @Test(expected = IOException.class)
 public void testTranslateInvalidNumericEntity() {
 CharSequence input = "&notAnEntity;";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 }
 };

 unescaper.translate(input, 0, outWriter);
 }

 /**
 * Test translate method with numeric entity that is not followed by semicolon.
 */
 @Test(expected = IOException.class)
 public void testTranslateInvalidNumericEntityNoSemicolon() {
 CharSequence input = "&amp;#160";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 }
 };

 unescaper.translate(input, 0, outWriter);
 }

 /**
 * Test translate method with negative index.
 */
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testTranslateNegativeIndex() {
 CharSequence input = "&amp;#160";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 }
 };

 unescaper.translate(input, -1, outWriter);
 }

 /**
 * Test translate method with out of bounds index.
 */
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testTranslateOutOfBoundsIndex() {
 CharSequence input = "&amp;#160";
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 }
 };

 unescaper.translate(input, input.length(), outWriter);
 }

 /**
 * Test translate method with null input.
 */
 @Test(expected = NullPointerException.class)
 public void testTranslateNullInput() {
 CharSequence input = null;
 Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 }
 };

 unescaper.translate(input, 0, outWriter);
 }

 /**
 * Test translate method with null output writer.
 */
 @Test(expected = NullPointerException.class)
 public void testTranslateNullOutputWriter() {
 CharSequence input = "&amp;#160";
 Writer outWriter = null;

 unescaper.translate(input, 0, outWriter);
 }
}