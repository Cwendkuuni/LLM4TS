import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.CharSequenceUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testNumericEntityUnescaper_Decimal_Simple() {
 CharSequence input = "&amp;#128;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char[] cbuf = new char[1];
 cbuf[0] = (char) c;
 append(cbuf);
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, outputWriter);

 assertEquals(1, length);
 assertEquals("\u0080", outputWriter.toString());
 }

 @Test
 public void testNumericEntityUnescaper_Decimal_Multiple() {
 CharSequence input = "&amp;#128;&amp;#159;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char[] cbuf = new char[1];
 cbuf[0] = (char) c;
 append(cbuf);
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, outputWriter);

 assertEquals(3, length);
 assertEquals("\u0080\u0095", outputWriter.toString());
 }

 @Test
 public void testNumericEntityUnescaper_Hex_Simple() {
 CharSequence input = "&amp;#x64;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char[] cbuf = new char[1];
 cbuf[0] = (char) c;
 append(cbuf);
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, outputWriter);

 assertEquals(1, length);
 assertEquals("d", outputWriter.toString());
 }

 @Test
 public void testNumericEntityUnescaper_Hex_Multiple() {
 CharSequence input = "&amp;#x41;&amp;#x42;&amp;#x43;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char[] cbuf = new char[1];
 cbuf[0] = (char) c;
 append(cbuf);
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, outputWriter);

 assertEquals(3, length);
 assertEquals("ABC", outputWriter.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNumericEntityUnescaper_InvalidHex_Start() {
 CharSequence input = "&amp;#xX5;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char[] cbuf = new char[1];
 cbuf[0] = (char) c;
 append(cbuf);
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, outputWriter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNumericEntityUnescaper_InvalidHex_End() {
 CharSequence input = "&amp;#x5;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char[] cbuf = new char[1];
 cbuf[0] = (char) c;
 append(cbuf);
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, outputWriter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNumericEntityUnescaper_InvalidDecimal_Start() {
 CharSequence input = "&amp;# invalid;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char[] cbuf = new char[1];
 cbuf[0] = (char) c;
 append(cbuf);
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, outputWriter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNumericEntityUnescaper_InvalidDecimal_End() {
 CharSequence input = "&amp;#12;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char[] cbuf = new char[1];
 cbuf[0] = (char) c;
 append(cbuf);
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, outputWriter);
 }

 @Test
 public void testNumericEntityUnescaper_Empty() {
 CharSequence input = "&amp;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char[] cbuf = new char[1];
 cbuf[0] = (char) c;
 append(cbuf);
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, outputWriter);

 assertEquals(0, length);
 assertEquals("", outputWriter.toString());
 }

 @Test
 public void testNumericEntityUnescaper_NoMatch() {
 CharSequence input = "&amp;example;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char[] cbuf = new char[1];
 cbuf[0] = (char) c;
 append(cbuf);
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, outputWriter);

 assertEquals(0, length);
 assertEquals("", outputWriter.toString());
 }
}