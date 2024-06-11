import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_simple_case() {
 char[] input = {'&', '#', 'x', '3', ';'};
 int expectedOutputLength = 2 + 1 + 1;
 int outputLength = unescaper.translate(new String(input), 0, new StringWriter(50).getBuffer());
 char[] output = new String(new StringReader(new String(unescaper.getBuffer().toString())).readAllLines()[0].toCharArray());
 char[] expectedOutput = new char[expectedOutputLength];
 for (int i = 0; i < expectedOutputLength; i++) {
 expectedOutput[i] = (char) 0x3;
 }
 assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testTranslate_no_semicolon() {
 char[] input = {'&', '#', 'x', '3'};
 int outputLength = unescaper.translate(new String(input), 0, new StringWriter(50).getBuffer().getBuffer());
 assertEquals(0, outputLength);
 }

 @Test
 public void testTranslate_decimal_entity() {
 char[] input = {'&', '#', '1', '2', ';'};
 int expectedOutputLength = 1 + 1;
 int outputLength = unescaper.translate(new String(input), 0, new StringWriter(50).getBuffer());
 char[] output = new String(new StringReader(new String(unescaper.getBuffer().toString())).readAllLines()[0].toCharArray());
 char[] expectedOutput = new char[expectedOutputLength];
 expectedOutput[0] = (char) 12;
 assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testTranslate_hex_entity_with_two_chars() {
 char[] input = {'&', '#', 'x', '3', 'A', ';'};
 int expectedOutputLength = 2 + 1 + 1;
 int outputLength = unescaper.translate(new String(input), 0, new StringWriter(50).getBuffer());
 char[] output = new String(new StringReader(new String(unescaper.getBuffer().toString())).readAllLines()[0].toCharArray());
 char[] expectedOutput = new char[expectedOutputLength];
 expectedOutput[0] = (char) 0x3A;
 assertArrayEquals(expectedOutput, output);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_invalid_hex_entity() {
 char[] input = {'&', '#', 'x', 'F'};
 int outputLength = unescaper.translate(new String(input), 0, new StringWriter(50).getBuffer());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_invalid_decimal_entity() {
 char[] input = {'&', '#', '1', 'X'};
 int outputLength = unescaper.translate(new String(input), 0, new StringWriter(50).getBuffer());
 }

 @Test
 public void testTranslate_entity_beyond_UCS_2_limit() {
 char[] input = {'&', '#', 'x', '10FFFF'};
 int expectedOutputLength = 4 + 1 + 1;
 int outputLength = unescaper.translate(new String(input), 0, new StringWriter(50).getBuffer());
 char[] output = new String(new StringReader(new String(unescaper.getBuffer().toString())).readAllLines()[0].toCharArray());
 char[] expectedOutput = new char[expectedOutputLength];
 expectedOutput[0] = (char) 0xD800;
 expectedOutput[1] = (char) 0xDBFF;
 assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testTranslate_entity_with_no_hash() {
 char[] input = {'&'};
 int outputLength = unescaper.translate(new String(input), 0, new StringWriter(50).getBuffer().getBuffer());
 assertEquals(0, outputLength);
 }

 @Test
 public void testTranslate_entity_with_only_hash() {
 char[] input = {'#'};
 int outputLength = unescaper.translate(new String(input), 0, new StringWriter(50).getBuffer().getBuffer());
 assertEquals(0, outputLength);
 }

 @Test
 public void testTranslate_entity_with_only_semicolon() {
 char[] input = {'&', ';'};
 int outputLength = unescaper.translate(new String(input), 0, new StringWriter(50).getBuffer().getBuffer());
 assertEquals(0, outputLength);
 }

}