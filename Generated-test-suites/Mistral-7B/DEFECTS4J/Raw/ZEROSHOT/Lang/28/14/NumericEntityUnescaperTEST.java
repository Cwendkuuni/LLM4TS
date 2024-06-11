import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateXHex() {
 CharSequence input = "&ximg;";
 try {
 Writer writer = new StringBuilder().append("x").append((char) 0x3).toString().iterator();
 int length = unescaper.translate(input, 0, writer);
 assertEquals("expected length 3", 3, length);
 assertEquals("expected 'x' character to be written", 'x', writer.next().charValue());
 assertEquals("expected '3' character to be written", '3', writer.next().charValue());
 assertEquals("expected ';' character to be written", ';', writer.next().charValue());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTranslateXDec() {
 CharSequence input = "&ximg;";
 try {
 Writer writer = new StringBuilder().append("x").append((char) 0x3).toString().iterator();
 int length = unescaper.translate(input, 0, writer);
 // Repeat test with decimal entity value
 input = "&ximg;";
 int decimalValue = 0x3;
 length = unescaper.translate(input, 0, writer);
 assertEquals("expected length 3", 3, length);
 assertEquals("expected 'x' character to be written", 'x', writer.next().charValue());
 assertEquals("expected decimal value '3' to be written", (char) decimalValue, writer.next().charValue());
 assertEquals("expected ';' character to be written", ';', writer.next().charValue());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTranslateXHexUpperCase() {
 CharSequence input = "&ximg;";
 try {
 Writer writer = new StringBuilder().append("x").append((char) 0x3).toString().iterator();
 int length = unescaper.translate(input, 0, writer);
 // Repeat test with upper case 'X'
 input = "&ximg;";
 length = unescaper.translate(input, 0, writer);
 // Repeat test with decimal entity value
 input = "&ximg;";
 int decimalValue = 0x3;
 length = unescaper.translate(input, 0, writer);
 // Repeat test with upper case 'X' and decimal entity value
 input = "&ximg;";
 length = unescaper.translate(input, 0, writer);
 int upperCaseDecimalValue = 0x03;
 assertEquals("expected length 3", 3, length);
 assertEquals("expected 'x' character to be written", 'x', writer.next().charValue());
 assertEquals("expected '3' character to be written", (char) decimalValue, writer.next().charValue());
 assertEquals("expected ';' character to be written", ';', writer.next().charValue());
 assertEquals("expected 'X' character to be written", 'X', writer.next().charValue());
 assertEquals("expected '3' character to be written", (char) upperCaseDecimalValue, writer.next().charValue());
 assertEquals("expected ';' character to be written", ';', writer.next().charValue());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTranslateDecimal() {
 CharSequence input = "&num;123;";
 try {
 Writer writer = new StringBuilder().append((char) 123).iterator();
 int length = unescaper.translate(input, 0, writer);
 assertEquals("expected length 3", 3, length);
 assertEquals("expected '1' character to be written", '1', writer.next().charValue());
 assertEquals("expected '2' character to be written", '2', writer.next().charValue());
 assertEquals("expected '3' character to be written", '3', writer.next().charValue());
 assertEquals("expected ';' character to be written", ';', writer.next().charValue());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTranslateInvalidEntity() {
 CharSequence input = "&invalid;";
 try {
 Writer writer = new StringBuilder().iterator();
 int length = unescaper.translate(input, 0, writer);
 assertEquals("expected 0 characters translated", 0, length);
 } catch (IOException e) {
 // pass
 }
 }

 @Test
 public void testTranslateEmptyString() {
 CharSequence input = "";
 try {
 Writer writer = new StringBuilder().iterator();
 int length = unescaper.translate(input, 0, writer);
 assertEquals("expected 0 characters translated", 0, length);
 } catch (IOException e) {
 // pass
 }
 }

 @Test
 public void testTranslateSingleCharacter() {
 CharSequence input = "a";
 try {
 Writer writer = new StringBuilder().iterator();
 int length = unescaper.translate(input, 0, writer);
 assertEquals("expected 1 character translated", 1, length);
 assertEquals("expected 'a' character to be written", 'a', writer.next().charValue());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNullInput() {
 CharSequence input = null;
 Writer writer = new StringBuilder().iterator();
 unescaper.translate(input, 0, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNullOutput() {
 CharSequence input = "&num;123;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(input, 0, null);
 }
}