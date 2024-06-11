import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateValidNumericEntity() {
 char[] expected = {'&', '#', 'x', '3', 'F', ';'};
 CharSequence input = "&#[x3F];";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 CharSequence actual = new String(new StringWriter(null).getBuffer().toString(), 0, length);
 Assert.assertArrayEquals(expected, actual.toCharArray());
 }

 @Test
 public void testTranslateValidNumericEntityWithoutSemicolon() {
 char[] expected = {'&', '#', 'x', '3', 'F'};
 CharSequence input = "&#[x3F]";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 CharSequence actual = new String(new StringWriter(null).getBuffer().toString(), 0, length);
 Assert.assertArrayEquals(expected, actual.toCharArray());
 }

 @Test
 public void testTranslateValidNumericEntityWithLeadingSpace() {
 char[] expected = {'&', '#', 'x', '3', 'F', ';'};
 CharSequence input = " &#[x3F];";
 int length = unescaper.translate(input, 1, new StringWriter(null).getBuffer());
 CharSequence actual = new String(new StringWriter(null).getBuffer().toString(), 0, length);
 Assert.assertArrayEquals(expected, actual.toCharArray());
 }

 @Test
 public void testTranslateValidNumericEntityWithTrailingSpace() {
 char[] expected = {'&', '#', 'x', '3', 'F', ';'};
 CharSequence input = "&#[x3F]; ";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 CharSequence actual = new String(new StringWriter(null).getBuffer().toString(), 0, length);
 Assert.assertArrayEquals(expected, actual.toCharArray());
 }

 @Test
 public void testTranslateValidDecimalNumericEntity() {
 char[] expected = {'&', '#', '3', '7', ';'};
 CharSequence input = "&#[37];";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 CharSequence actual = new String(new StringWriter(null).getBuffer().toString(), 0, length);
 Assert.assertArrayEquals(expected, actual.toCharArray());
 }

 @Test(expected = IOException.class)
 public void testTranslateInvalidInput() {
 unescaper.translate(new StringBuilder(), 0, new StringWriter(null).getBuffer());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidHexCodePoint() {
 char[] expected = {'&', '#', 'x', 'z'};
 CharSequence input = "&#[xz];";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 CharSequence actual = new String(new StringWriter(null).getBuffer().toString(), 0, length);
 Assert.assertArrayEquals(expected, actual.toCharArray());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidDecimalCodePoint() {
 char[] expected = {'&', '#', '-', '1'};
 CharSequence input = "&#[-1];";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 CharSequence actual = new String(new StringWriter(null).getBuffer().toString(), 0, length);
 Assert.assertArrayEquals(expected, actual.toCharArray());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidNumericEntityWithLeadingSpace() {
 char[] expected = {'&', '#', ' '};
 CharSequence input = " &#[x3F];";
 int length = unescaper.translate(input, 1, new StringWriter(null).getBuffer());
 CharSequence actual = new String(new StringWriter(null).getBuffer().toString(), 0, length);
 Assert.assertArrayEquals(expected, actual.toCharArray());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidNumericEntityWithTrailingSpace() {
 char[] expected = {'&', '#', ' '};
 CharSequence input = "&#[x3F]; ";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 CharSequence actual = new String(new StringWriter(null).getBuffer().toString(), 0, length);
 Assert.assertArrayEquals(expected, actual.toCharArray());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidNumericEntityWithoutSemicolon() {
 char[] expected = {'&', '#', 'x', '3', 'F'};
 Charsequence input = "&#[x3F]";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 CharSequence actual = new String(new StringWriter(null).getBuffer().toString(), 0, length);
 Assert.assertArrayEquals(expected, actual.toCharArray());
 // This test case is intentionally incomplete as the method should throw an IOException in this scenario
 }

 @Test(expected = IOException.class)
 public void testTranslateInvalidInputWithLeadingSpace() {
 char[] expected = {'&', '#', ' '};
 Charsequence input = " &#[x3F];";
 int length = unescaper.translate(input, 1, new StringWriter(null).getBuffer());
 }

 @Test(expected = IOException.class)
 public void testTranslateInvalidInputWithTrailingSpace() {
 char[] expected = {'&', '#', ' '};
 Charsequence input = "&#[x3F]; ";
 int length = unescaper.translate(input, 0, new StringWriter(null).getBuffer());
 }

}