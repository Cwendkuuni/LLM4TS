import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateNumericEntityWithDecimal() {
 CharSequence input = "&#128;";
 try {
 int result = unescaper.translate(input, 0, new StringBuilder());
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals((byte) 0xE2, result);
 }

 @Test
 public void testTranslateNumericEntityWithHex() {
 CharSequence input = "&#x41;";
 try {
 int result = unescaper.translate(input, 0, new StringBuilder());
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals('A', result);
 }

 @Test
 public void testTranslateNumericEntityWithHexLongerThanFourDigits() {
 CharSequence input = "&#xCAFEBABE;";
 try {
 int result = unescaper.translate(input, 0, new StringBuilder());
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals((int) 0xCAFEBABE, result);
 }

 @Test
 public void testTranslateInvalidNumericEntity() {
 CharSequence input = "&invalid;";
 try {
 int result = unescaper.translate(input, 0, new StringBuilder());
 } catch (IOException e) {
 // expected
 }
 assertEquals(0, result);
 }

 @Test
 public void testTranslateEmptyInput() {
 CharSequence input = "";
 try {
 int result = unescaper.translate(input, 0, new StringBuilder());
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(0, result);
 }

 @Test
 public void testTranslateNullInput() {
 CharSequence input = null;
 try {
 int result = unescaper.translate(input, 0, new StringBuilder());
 } catch (NullPointerException e) {
 // expected
 }
 assertEquals(0, result);
 }

 @Test
 public void testTranslateInputWithMultipleEntities() {
 CharSequence input = "abc&#128;&#x41;def";
 try {
 StringBuilder output = new StringBuilder();
 int totalCharsWritten = unescaper.translate(input, 0, output);
 } catch (IOException e) {
 e.printStackTrace();
 }
 String expectedOutput = "abc" + (char) 0xE2 + "A" + "def";
 assertEquals(expectedOutput.length(), totalCharsWritten);
 }
}