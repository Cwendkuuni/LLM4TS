import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSimple() {
 CharSequence input = "&amp;";
 int expectedLength = 4;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testTranslateValidNumericEntityWithSemicolon() {
 CharSequence input = "&#123;";
 int expectedLength = 2;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testTranslateValidNumericEntityWithoutSemicolon() {
 CharSequence input = "&#123";
 int expectedLength = 2;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testTranslateInvalidCharacter() {
 CharSequence input = "&abc;";
 int expectedLength = 3;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test(expected = IOException.class)
 public void testTranslateNegativeInputIndex() {
 CharSequence input = "&abc;";
 int index = -1;
 unescaper.translate(input, index, null);
 }

 @Test(expected = IOException.class)
 public void testTranslateNullInput() {
 CharSequence input = null;
 int index = 0;
 unescaper.translate(input, index, null);
 }

 @Test(expected = IOException.class)
 public void testTranslateNullOutput() {
 CharSequence input = "&abc;";
 int index = 0;
 Writer output = null;
 unescaper.translate(input, index, output);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidHexNumber() {
 CharSequence input = "&#xz";
 int index = 0;
 unescaper.translate(input, index, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidDecimalNumber() {
 CharSequence input = "&#-123";
 int index = 0;
 unescaper.translate(input, index, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidDecimalNumberWithSemicolon() {
 CharSequence input = "&#-123;";
 int index = 0;
 unescaper.translate(input, index, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidHexNumberWithSemicolon() {
 CharSequence input = "&#xz;";
 int index = 0;
 unescaper.translate(input, index, null);
 }

 @Test
 public void testTranslateLargeCharacterCode() {
 CharSequence input = "&#[1114111];";
 int expectedLength = 6;
 int actualLength = unescaper.translate(input, 0, null);
 assertEquals(expectedLength, actualLength);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testTranslateBeyondSequenceEnd() {
 CharSequence input = "&abc;";
 int index = input.length();
 unescaper.translate(input, index, null);
 }
}