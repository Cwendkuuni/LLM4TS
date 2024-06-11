import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with valid numeric entities.
 */
 @Test
 public void testTranslateValidNumericEntities() {
 char[] expected = {'&', '#', 'x', '3', 'F', ';'};
 CharSequence input = "&#[x3F];";
 try (Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 assertEquals("Unexpected character written: " + ch, expected[testWriterIndex++], ch);
 }

 @Override
 public void flush() throws IOException {
 // Flush is not used in this test
 }

 @Override
 public void close() throws IOException {
 // Close is not used in this test
 }
 }) {
 int length = unescaper.translate(input, 0, out);
 assertEquals("Incorrect length of translated output: " + length, expected.length, length);
 }
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with numeric entities without semicolon.
 */
 @Test
 public void testTranslateNumericEntitiesWithoutSemicolon() {
 char[] expected = {'&', '#', 'x', '3', 'F'};
 CharSequence input = "&#[x3F]";
 try (Writer out = new Writer() {
 // Same as testTranslateValidNumericEntities(), but without checking the semicolon
 }) {
 int length = unescaper.translate(input, 0, out);
 assertEquals("Incorrect length of translated output: " + length, expected.length, length);
 }
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with numeric entities with decimal system.
 */
 @Test
 public void testTranslateNumericEntitiesWithDecimalSystem() {
 char[] expected = {'&', '#', '1', '4', '9', ';'};
 CharSequence input = "&#[149];";
 try (Writer out = new Writer() {
 // Same as testTranslateValidNumericEntities(), but with a decimal entity
 }) {
 int length = unescaper.translate(input, 0, out);
 assertEquals("Incorrect length of translated output: " + length, expected.length, length);
 }
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid input.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testTranslateInvalidInput() {
 CharSequence input = "&";
 unescaper.translate(input, 0, new Writer() {
 // Does not check the length of the input or the Writer
 });
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with negative index.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateNegativeIndex() {
 CharSequence input = "&#[x3F];";
 unescaper.translate(input, -1, new Writer() {
 // Does not check the Writer
 });
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with input out of bounds.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateInputOutOfBounds() {
 char[] input = {'&', '#', 'x', '3', 'F', ';'};
 int index = 3;
 Writer out = new Writer() {
 // Does not check the Writer
 };
 try {
 unescaper.translate(new String(input), index, out);
 } catch (IOException e) {
 // IOException is not expected
 }
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with null Writer.
 */
 @Test(expected = NullPointerException.class)
 public void testTranslateNullWriter() {
 CharSequence input = "&#[x3F];";
 unescaper.translate(input, 0, null);
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid hexadecimal character.
 */
 @Test
 public void testTranslateInvalidHexadecimal() {
 char[] expected = {'&', '#', 'z', '3', 'F'}; // z is not a valid hexadecimal character
 Charsequence input = "&#[z3F];";
 try (Writer out = new Writer() {
 // Same as testTranslateValidNumericEntities(), but with an invalid hexadecimal character
 }) {
 int length = unescaper.translate(input, 0, out);
 assertEquals("Incorrect length of translated output: " + length, expected.length, length);
 }
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entity value out of range.
 */
 @Test
 public void testTranslateEntityValueOutOfRange() {
 char[] expected = {(char) 0xFFFE, (char) 0xFFFE};
 Charsequence input = "&#[65534];";
 try (Writer out = new Writer() {
 // Same as testTranslateValidNumericEntities(), but with an entity value out of range
 }) {
 int length = unescaper.translate(input, 0, out);
 assertEquals("Incorrect length of translated output: " + length, expected.length, length);
 }
 }
}