import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_ValidNumericEntity_SingleCharacter() {
 char[] expected = {'&', '#', 'x', '3', ';', 'A'};
 char[] input = {'&', '#', 'x', '3', ';'};
 char[] output = new char[expected.length];

 try {
 int length = unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 output[0] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Not used in this test case
 }
 });
 assertEquals(expected.length, length);
 assertArrayEquals(expected, output);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTranslate_ValidNumericEntity_TwoCharacter() {
 char[] expected = {'&', '#', 'x', '3', '5', 'B'};
 char[] input = {'&', '#', 'x', '3', '5'};
 char[] output = new char[expected.length];

 try {
 int length = unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 output[0] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Not used in this test case
 }
 });
 assertEquals(expected.length, length);
 assertArrayEquals(expected, output);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTranslate_ValidNumericEntity_MultipleCharactersHex() {
 char[] expected = {'&', '#', 'x', '3', '3', 'E', 'F'};
 char[] input = {'&', '#', 'x', '3', '3', 'E', 'F', ';'};
 char[] output = new char[expected.length];

 try {
 int length = unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 output[0] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Not used in this test case
 }
 });
 assertEquals(expected.length, length);
 assertArrayEquals(expected, output);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTranslate_ValidNumericEntity_MultipleCharactersDec() {
 char[] expected = {'&', '#', '1', '4', '9', '1', '5', ';'};
 char[] input = {'&', '#', '1', '4', '9', '1', '5'};
 char[] output = new char[expected.length];

 try {
 int length = unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 output[0] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Not used in this test case
 }
 });
 assertEquals(expected.length, length);
 assertArrayEquals(expected, output);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidNumericEntity() {
 char[] input = {'&', '#', 'x', '3', 'a'};
 try {
 unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 // Not used in this test case
 }

 @Override
 public void flush() throws IOException {
 // Not used in this test case
 }
 });
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidNumericEntity_MissingSemicolon() {
 char[] input = {'&', '#', 'x', '3', '5'};
 try {
 unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 // Not used in this test case
 }

 @Override
 public void flush() throws IOException {
 // Not used in this test case
 }
 });
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTranslate_InvalidCharacter_BeforeAmpersand() {
 char[] input = {'@', '&', '#', 'x', '3', ';'};
 assertNull(unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 // Not used in this test case
 }

 @Override
 public void flush() throws IOException {
 // Not used in this test case
 }
 }));
 }

 @Test
 public void testTranslate_InvalidCharacter_AfterSemicolon() {
 char[] input = {'&', '#', 'x', '3', ';', 'B'};
 assertNull(unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 // Not used in this test case
 }

 @Override
 public void flush() throws IOException {
 // Not used in this test case
 }
 }));
 }

 @Test
 public void testTranslate_EmptyInput() {
 assertNull(unescaper.translate(new String(""), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 // Not used in this test case
 }

 @Override
 public void flush() throws IOException {
 // Not used in this test case
 }
 }));
 }

 @Test
 public void testTranslate_NullInput() {
 assertNull(unescaper.translate(null, 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 // Not used in this test case
 }

 @Override
 public void flush() throws IOException {
 // Not used in this test case
 }
 }));
 }

 @Test
 public void testTranslate_NegativeIndex() {
 assertNull(unescaper.translate(new String(""), -1, new Writer() {
 @Override
 public void write(int c) throws IOException {
 // Not used in this test case
 }

 @Override
 public void flush() throws IOException {
 // Not used in this test case
 }
 }));
 }
}