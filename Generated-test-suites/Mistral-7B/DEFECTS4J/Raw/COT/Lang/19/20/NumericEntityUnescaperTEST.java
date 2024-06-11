import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_ValidNumericEntity_ReturnsCorrectCodepoint() {
 char[] expected = {'&', '#', 'x', '3', '5', ';', 'A', 'C', 'E', 'C'};
 char[] input = {'&', '#', 'x', '3', '5'};
 try {
 char[] output = new char[10];
 int length = unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 output[length++] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }
 });
 assertEquals(expected.length, length);
 for (int i = 0; i < length; i++) {
 assertEquals(expected[i], output[i]);
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslate_ValidNumericEntityWithSemicolon_ReturnsCorrectCodepoint() {
 char[] expected = {'&', '#', 'x', '3', '5', ';', 'A', 'C', 'E', 'C'};
 char[] input = {'&', '#', 'x', '3', '5', ';'};
 try {
 char[] output = new char[expected.length];
 int length = unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 output[length++] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }
 });
 assertEquals(expected.length, length);
 for (int i = 0; i < length; i++) {
 assertEquals(expected[i], output[i]);
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslate_ValidNumericEntity_WithoutSemicolon_ThrowsException() {
 char[] input = {'&', '#', 'x', '3', '5'};
 try {
 int length = unescaper.translate(new String(input), 0, null);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 } catch (IOException e) {
 e.printStackTrace();
 fail("Unexpected IOException");
 }
 }

 @Test
 public void testTranslate_InvalidNumericEntity_ThrowsException() {
 char[] input = {'&', '#', 'a', 'b', 'c'};
 try {
 int length = unescaper.translate(new String(input), 0, null);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 } catch (IOException e) {
 e.printStackTrace();
 fail("Unexpected IOException");
 }
 }

 @Test
 public void testTranslate_ValidNumericEntity_WithNegativeValue_ThrowsException() {
 char[] input = {'&', '#', '-', 'x', 'F', 'F', ';'};
 try {
 int length = unescaper.translate(new String(input), 0, null);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 } catch (IOException e) {
 e.printStackTrace();
 fail("Unexpected IOException");
 }
 }

 @Test
 public void testTranslate_ValidNumericEntity_WithValueOutsideUTF16Range() {
 char[] expected = {'&', '#', 'x', '1', '1', '0', '0', '0', 'D', '8', '3', '5', '7'};
 char[] input = {'&', '#', 'x', '1', '1', '0', '0', '0', 'D', '8', '3', '5', '7', ';'};
 try {
 char[] output = new char[expected.length];
 int length = unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 output[length++] = (char) c;
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }
 });
 assertEquals(expected.length, length);
 for (int i = 0; i < length; i++) {
 assertEquals(expected[i], output[i]);
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

}