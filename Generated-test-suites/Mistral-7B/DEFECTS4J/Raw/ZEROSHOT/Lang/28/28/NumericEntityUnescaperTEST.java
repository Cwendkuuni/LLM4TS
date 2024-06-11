import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateNumericEntity() {
 char[] input = {'&', '#', 'x', '3', '1', '3', ';'};
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 int outputLength = unescaper.translate(new String(input), 0, new StringBuilder()).length();
 char[] output = new char[outputLength];
 unescaper.translate(new String(input), 0, new StringBuilder()).toString().toCharArray();
 unescaper.translate(new String(input), 0, new StringBuilder(), 0, output, 0);

 assertEquals((char)0x313, output[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateInvalidEntity() {
 char[] input = {'&', '#', 'x', '3', 'a', ';'};
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 unescaper.translate(new String(input), 0, new StringBuilder());
 }

 @Test
 public void testTranslateDecimalEntity() {
 char[] input = {'&', '#', '0', '3', '5', ';'};
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 int outputLength = unescaper.translate(new String(input), 0, new StringBuilder()).length();
 char[] output = new char[outputLength];
 unescaper.translate(new String(input), 0, new StringBuilder(), 0, output, 0);

 assertEquals((char)0x0035, output[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateEntityWithInvalidStart() {
 char[] input = {'&', '#', 'x', '3', '1', '3', ';', '&'};
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 unescaper.translate(new String(input), 0, new StringBuilder());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateEntityWithInvalidEnd() {
 char[] input = {'&', '#', 'x', '3', '1', '3', ';', ';'};
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 unescaper.translate(new String(input), 0, new StringBuilder());
 }

 @Test
 public void testTranslateEntityWithEmptyInput() {
 char[] input = {};
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 int outputLength = unescaper.translate(new String(input), 0, new StringBuilder()).length();
 char[] output = new char[outputLength];
 unescaper.translate(new String(input), 0, new StringBuilder(), 0, output, 0);

 assertEquals(0, outputLength);
 }

 @Test
 public void testTranslateEntityWithNullInput() {
 char[] input = null;
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 int outputLength = unescaper.translate(new String(input), 0, new StringBuilder()).length();
 char[] output = new char[outputLength];
 unescaper.translate(new String(input), 0, new StringBuilder(), 0, output, 0);

 assertEquals(0, outputLength);
 }
}