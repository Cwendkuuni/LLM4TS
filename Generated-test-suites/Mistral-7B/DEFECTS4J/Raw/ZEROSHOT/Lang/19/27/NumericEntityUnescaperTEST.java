import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSimpleNumericEntity() {
 CharSequence input = "&amp;";
 try {
 int length = unescaper.translate(input, 0, new StringWriter());
 assertEquals(1, length);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateSimpleNumericEntityWithSemicolon() {
 CharSequence input = "&amp;#123;";
 try {
 int length = unescaper.translate(input, 0, new StringWriter());
 assertEquals(2, length);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateHexNumericEntity() {
 CharSequence input = "&amp;#x7f";
 try {
 int length = unescaper.translate(input, 0, new StringWriter());
 assertEquals(1, length);
 assertEquals('\u007f', ((StringWriter) output).toString().charAt(0));
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateInvalidNumericEntity() {
 CharSequence input = "&amp;#abc";
 try {
 int length = unescaper.translate(input, 0, new StringWriter());
 assertEquals(0, length);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateLongNumericEntity() {
 CharSequence input = "&amp;#12345678;";
 try {
 int length = unescaper.translate(input, 0, new StringWriter());
 assertEquals(5, length);
 assertEquals('\u0123', ((StringWriter) output).toString().charAt(0));
 assertEquals('\u0124', ((StringWriter) output).toString().charAt(1));
 assertEquals('\u0125', ((StringWriter) output).toString().charAt(2));
 assertEquals('\u0126', ((StringWriter) output.toString().charAt(3)));
 assertEquals('\u0127', ((StringWriter) output).toString().charAt(4));
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateLongHexNumericEntity() {
 CharSequence input = "&amp;#x10FFFE;";
 try {
 int length = unescaper.translate(input, 0, new StringWriter());
 assertEquals(3, length);
 assertEquals('\uD7FF', ((StringWriter) output).toString().charAt(0));
 assertEquals('\uD800', ((StringWriter) output).toString().charAt(1));
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateLongHexNumericEntityWithSemicolon() {
 CharSequence input = "&amp;#x10FFFE;";
 try {
 int length = unescaper.translate(input, 0, new StringWriter());
 assertEquals(3, length);
 assertEquals('\uD7FF', ((StringWriter) output).toString().charAt(0));
 assertEquals('\uD800', ((StringWriter) output).toString().charAt(1));
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateCharSequenceTranslator() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 CharSequence input = "&amp;#123;&amp;#x7f;&amp;#x10FFFE;";
 try {
 String result = translator.translate(input);
 assertEquals("ABC", result);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test (expected = NumberFormatException.class)
 public void testTranslateInvalidHexNumericEntity() {
 CharSequence input = "&amp;#xabc";
 try {
 int length = unescaper.translate(input, 0, new StringWriter());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test (expected = NumberFormatException.class)
 public void testTranslateInvalidNumericEntityNoSemicolon() {
 CharSequence input = "&amp;#123";
 try {
 int length = unescaper.translate(input, 0, new StringWriter());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test (expected = IllegalArgumentException.class)
 public void testTranslateNullInput() {
 CharSequence input = null;
 try {
 int length = unescaper.translate(input, 0, new StringWriter());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test (expected = IllegalArgumentException.class)
 public void testTranslateNullOutput() {
 Writer output = null;
 CharSequence input = "&amp;#123";
 try {
 int length = unescaper.translate(input, 0, output);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test (expected = IOException.class)
 public void testTranslateInvalidOutput() {
 Writer output = new StringWriter() {
 @Override
 public void write(int c, IOException e) {
 throw new IOException("Invalid Output");
 }
 };
 CharSequence input = "&amp;#123";
 try {
 int length = unescaper.translate(input, 0, output);
 } catch (IOException e) {
 }
 }

 private static StringWriter output = new StringWriter();
}