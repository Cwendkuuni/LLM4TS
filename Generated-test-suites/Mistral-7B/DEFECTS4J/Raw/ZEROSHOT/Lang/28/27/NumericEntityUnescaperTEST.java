import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateXHex() {
 CharSequence input = "&amp;#x41;#x42;#x43;";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char charValue = (char) c;
 System.out.print(charValue);
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("ABC", input.subSequence(0, 6).toString());
 assertEquals(3, length);
 }

 @Test
 public void testTranslateXDec() {
 CharSequence input = "&amp;#x2A;#x3B;#x2C;"; // hex to dec = 42, 59, 44
 Writer out = new Writer() {
 // Same as above
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("BC", input.subSequence(0, 3).toString());
 assertEquals(3, length);
 }

 @Test
 public void testTranslateDecimal() {
 CharSequence input = "&amp;#10;#13;#10;";
 Writer out = new Writer() {
 // Same as above
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("\n", input.subSequence(0, 2).toString());
 assertEquals(3, length);
 }

 @Test (expected = NumberFormatException.class)
 public void testTranslateInvalidEntity() {
 CharSequence input = "&amp;#abc;";
 Writer out = new Writer() {
 // Same as above
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(input, 0, out);
 }

 @Test
 public void testTranslateEmptyInput() {
 CharSequence input = "";
 Writer out = new Writer() {
 // Same as above
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals(0, length);
 }
}