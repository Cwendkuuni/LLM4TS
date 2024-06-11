import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSimpleNumericEntity() {
 char[] expected = {'&', '#', '3', ';'};
 char[] actual = new char[4];
 int length = unescaper.translate(new String("&#3;"), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 actual[0] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }
 });
 assertEquals(expected, actual, 0, 4);
 assertEquals(4, length);
 }

 @Test
 public void testTranslateSimpleHexadecimalEntity() {
 char[] expected = {'&', '#', 'x', '3', 'a', ';'};
 char[] actual = new char[6];
 int length = unescaper.translate(new String("&#x3A;"), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 actual[0] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }
 });
 assertEquals(expected, actual, 0, 6);
 assertEquals(6, length);
 }

 @Test
 public void testTranslateLargeNumericEntity() {
 char[] expected = {'&', '#', 'x', '1', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', ';'};
 char[] actual = new char[13];
 int length = unescaper.translate(new String("&#x1FFF;"), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 actual[0] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }
 });
 assertEquals(expected, actual, 0, 13);
 assertEquals(13, length);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidNumericEntity() {
 unescaper.translate(new String("&#abc;"), 0, new Writer() {
 // Do not write anything to the writer as this test is only for checking the exception
 // Do nothing
 }, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidHexadecimalEntity() {
 unescaper.translate(new String("&#xabc;"), 0, new Writer() {
 // Do not write anything to the writer as this test is only for checking the exception
 // Do nothing
 }, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateInvalidInput() {
 unescaper.translate(new String("&#"), 3, new Writer() {
 // Do not write anything to the writer as this test is only for checking the exception
 // Do nothing
 }, null);
 }

 @Test(expected = IOException.class)
 public void testTranslateInvalidWriter() {
 char[] actual = new char[1];
 int length = unescaper.translate(new String("&#1;"), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 throw new IOException();
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }
 });
 assertEquals(0, length);
 }

}