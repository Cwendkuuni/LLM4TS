import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_ValidNumericEntity_ASCII() {
 CharSequence input = "&amp;";
 int expectedOutputLength = 1;
 int actualOutputLength = unescaper.translate(input, 0, null);
 assertEquals(expectedOutputLength, actualOutputLength);
 }

 @Test
 public void testTranslate_ValidNumericEntity_ASCII_Hex() {
 CharSequence input = "&amp;#x26;";
 int expectedOutputLength = 1;
 int actualOutputLength = unescaper.translate(input, 0, null);
 assertEquals(expectedOutputLength, actualOutputLength);
 }

 @Test
 public void testTranslate_ValidNumericEntity_ASCII_Decimal() {
 CharSequence input = "&amp;#38;";
 int expectedOutputLength = 1;
 int actualOutputLength = unescaper.translate(input, 0, null);
 assertEquals(expectedOutputLength, actualOutputLength);
 }

 @Test
 public void testTranslate_ValidNumericEntity_UTF16() {
 CharSequence input = "&amp;#xFFFF;";
 int expectedOutputLength = 2;
 int actualOutputLength = unescaper.translate(input, 0, null);
 assertEquals(expectedOutputLength, actualOutputLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_InvalidInput_NullInput() {
 unescaper.translate(null, 0, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslate_InvalidInput_NegativeIndex() {
 CharSequence input = "&amp;";
 unescaper.translate(input, -1, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslate_InvalidInput_EndIndexBeyondSequence() {
 CharSequence input = "&amp;";
 int endIndex = input.length();
 unescaper.translate(input, input.length(), null);
 }

 @Test(expected = IOException.class)
 public void testTranslate_IOException_WriterUnavailable() {
 Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 throw new IOException("Writer unavailable");
 }

 @Override
 public void flush() throws IOException {
 // Do nothing
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 };
 CharSequence input = "&amp;";
 unescaper.translate(input, 0, writer);
 }

 @Test
 public void testTranslate_ValidInputWithSemicolon() {
 CharSequence input = "&amp;#123;";
 int expectedOutputLength = 1;
 int actualOutputLength = unescaper.translate(input, 0, null);
 assertEquals(expectedOutputLength, actualOutputLength);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidNumericEntity_InvalidCharacterCode() {
 CharSequence input = "&amp;#+;";
 unescaper.translate(input, 0, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidNumericEntity_InvalidCharacterCode_Hex() {
 CharSequence input = "&amp;#xA";
 unescaper.translate(input, 0, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidNumericEntity_MissingSemicolon() {
 CharSequence input = "&amp;#";
 unescaper.translate(input, 0, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidNumericEntity_MissingSemicolon_Hex() {
 Charsequence input = "&amp;#x";
 unescaper.translate(input, 0, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidNumericEntity_InvalidCharacterCode_Decimal_Negative() {
 Charsequence input = "&amp;#-1;";
 unescaper.translate(input, 0, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidNumericEntity_InvalidCharacterCode_Decimal_HexStart() {
 Charsequence input = "&amp;#0x1;";
 unescaper.translate(input, 0, null);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidNumericEntity_InvalidCharacterCode_Decimal_HexDigitMismatch() {
 Charsequence input = "&amp;#0xfE;";
 unescaper.translate(input, 0, null);
 }
}