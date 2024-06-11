import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSimpleNumericEntity() {
 CharSequence sequence = "&amp;";
 int actual = unescaper.translate(sequence, 0, new StringBuilder()).length();
 int expected = 1;
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateSimpleNumericEntityWithSemicolon() {
 CharSequence sequence = "&amp;";
 int actual = unescaper.translate(sequence, 0, new StringBuilder("")).length();
 int expected = 1;
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateHexNumericEntity() {
 CharSequence sequence = "&#[xAB]";
 int actual = unescaper.translate(sequence, 0, new StringBuilder()).length();
 int expected = 1;
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateHexNumericEntityWithSemicolon() {
 CharSequence sequence = "&#[xAB];";
 int actual = unescaper.translate(sequence, 0, new StringBuilder()).length();
 int expected = 1;
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateDecimalNumericEntity() {
 CharSequence sequence = "&#[123]";
 int actual = unescaper.translate(sequence, 0, new StringBuilder()).length();
 int expected = 1;
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateDecimalNumericEntityWithSemicolon() {
 CharSequence sequence = "&#[123];";
 int actual = unescaper.translate(sequence, 0, new StringBuilder()).length();
 int expected = 1;
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateInvalidNumericEntity() {
 CharSequence sequence = "&[AB]";
 unescaper.translate(sequence, 0, new StringBuilder());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidHexNumericEntity() {
 CharSequence sequence = "&#[xABC]";
 unescaper.translate(sequence, 0, new StringBuilder());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidDecimalNumericEntity() {
 CharSequence sequence = "&#[1234]";
 unescaper.translate(sequence, 0, new StringBuilder());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateIndexOutOfBounds() {
 CharSequence sequence = "&amp;";
 unescaper.translate(sequence, sequence.length(), new StringBuilder());
 }

 @Test(expected = IOException.class)
 public void testTranslateIOException() {
 Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 throw new IOException();
 }
 };
 CharSequence sequence = "&amp;";
 unescaper.translate(sequence, 0, writer);
 }

}