import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 @Test
 public void testNumericEntityUnescaperNoHex() {
 char[] input = {'&', '#', '3', ';'};
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 System.out.print(ch);
 }

 @Override
 public void write(char[] c, int off, int len) throws IOException {
 for (int i = off; i < off + len; i++) {
 System.out.print(c[i]);
 }
 }

 @Override
 public void flush() throws IOException {
 // empty
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 };

 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 int length = translator.translate(new String(input), 0, outputWriter);
 assertEquals("3", new String(input, 0, 3));
 assertEquals(1, length);
 }

 @Test
 public void testNumericEntityUnescaperHex() {
 char[] input = {'&', 'x', '#', '2', '7', '1', ';'};
 Writer outputWriter = new Writer() {
 // ... same as above
 };

 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 int length = translator.translate(new String(input), 0, outputWriter);
 assertEquals("\u001d", new String(input, 0, 6));
 assertEquals(2, length);
 }

 @Test (expected = AssertionError.class)
 public void testNumericEntityUnescaperInvalidInput() {
 char[] input = {'&', '#', 'a', ';'};
 Writer outputWriter = new Writer() {
 // ... same as above
 };

 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 int length = translator.translate(new String(input), 0, outputWriter);
 }

 @Test
 public void testNumericEntityUnescaperMultiEntity() {
 char[] input = {'&', '#', '3', ';', '&', '#', 'x', '4', ';', '&', '#', '7', ';'};
 Writer outputWriter = new Writer() {
 // ... same as above
 };

 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 int length = translator.translate(new String(input), 0, outputWriter);
 assertEquals("\u0003\u0004\u0007", new String(input, 0, input.length));
 assertEquals(7, length);
 }
}