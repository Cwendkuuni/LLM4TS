import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSimpleNumericEntity() {
 char[] expected = {'&', '#', 'x', '3', ';', 'A', '\u00E2'};
 CharSequence input = "&#[x3]";
 int length = unescaper.translate(input, 0, null);
 char[] output = new char[length];
 try {
 unescaper.translate(input, 0, new StringWriter(output).getBuffer());
 } catch (IOException e) {
 // This should not happen in a JUnit test
 }
 assertEquals(expected, output);
 }

 @Test
 public void testTranslateNumericEntityWithSemicolon() {
 char[] expected = {'&', '#', 'x', '3', ';', 'A', '\u00E2'};
 CharSequence input = "&#[x3];";
 int length = unescaper.translate(input, 0, null);
 char[] output = new char[length];
 try {
 unescaper.translate(input, 0, new StringWriter(output).getBuffer());
 } catch (IOException e) {
 // This should not happen in a JUnit test
 }
 assertEquals(expected, output);
 }

 @Test
 public void testTranslateNumericEntityWithoutSemicolon() {
 char[] expected = {'&', '#', 'x', '3'};
 CharSequence input = "&#[x3]";
 int length = unescaper.translate(input, 0, null);
 char[] output = new char[length];
 try {
 unescaper.translate(input, 0, new StringWriter(output).getBuffer());
 } catch (IOException e) {
 // This should not happen in a JUnit test
 }
 assertEquals(expected, output);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidNumericEntity() {
 CharSequence input = "&#[xyz";
 unescaper.translate(input, 0, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidHexNumericEntity() {
 CharSequence input = "&#[xz";
 unescaper.translate(input, 0, null);
 }

 @Test
 public void testTranslateLargeNumericEntity() {
 char[] expected = {'&', '#', 'x', '110011', ';', 'F', '\uD800', '\uDC00'};
 CharSequence input = "&#[x110011]";
 int length = unescaper.translate(input, 0, null);
 char[] output = new char[length];
 try {
 unescaper.translate(input, 0, new StringWriter(output).getBuffer());
 } catch (IOException e) {
 // This should not happen in a JUnit test
 }
 assertEquals(expected, output);
 }

 @Test
 public void testTranslateInvalidLargeNumericEntity() {
 char[] expected = {'&', '#', 'x', '110011'};
 CharSequence input = "&#[x110011]";
 int length = unescaper.translate(input, 0, null);
 char[] output = new char[length];
 try {
 unescaper.translate(input, 0, new StringWriter(output).getBuffer());
 } catch (IOException e) {
 // This should not happen in a JUnit test
 }
 assertArrayEquals(expected, output);
 }
}