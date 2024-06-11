import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateSimple() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;&#x00A1;&#128;&#160;";
 String expectedOutput = "&amp;&A1;&Ĝ;&sp;";
 try {
 Writer outWriter = new java.io.StringWriter();
 int length = translator.translate(input, 0, outWriter);
 assertEquals("Unexpected length of output", expectedOutput.length(), outWriter.toString().length());
 assertEquals("Unexpected output", expectedOutput, outWriter.toString());
 } catch (IOException e) {
 // Since we are using a StringWriter here, there should be no IOException
 }
 }

 @Test
 public void testTranslateDecimal() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;&#10;";
 String expectedOutput = "\n";
 try {
 Writer outWriter = new java.io.StringWriter();
 int length = translator.translate(input, 0, outWriter);
 assertEquals("Unexpected length of output", expectedOutput.length(), outWriter.toString().length());
 assertEquals("Unexpected output", expectedOutput, outWriter.toString());
 } catch (IOException e) {
 // Since we are using a StringWriter here, there should be no IOException
 }
 }

 @Test
 public void testTranslateHex() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;&#x0061;&#x0062;&#x0063;";
 String expectedOutput = "&amp;abc";
 try {
 Writer outWriter = new java.io.StringWriter();
 int length = translator.translate(input, 0, outWriter);
 assertEquals("Unexpected length of output", expectedOutput.length(), outWriter.toString().length());
 assertEquals("Unexpected output", expectedOutput, outWriter.toString());
 } catch (IOException e) {
 // Since we are using a StringWriter here, there should be no IOException
 }
 }

 @Test
 public void testTranslateInvalidDecimal() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;&#xyz;";
 try {
 Writer outWriter = new java.io.StringWriter();
 int length = translator.translate(input, 0, outWriter);
 // Since the test fails below, the length and output should be 0
 assertEquals("Unexpected length of output", 0, length);
 assertEquals("Unexpected output", "", outWriter.toString());
 } catch (IOException e) {
 // Since we are using a StringWriter here, there should be no IOException
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidHex() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;&#xz;";
 try {
 Writer outWriter = new java.io.StringWriter();
 int length = translator.translate(input, 0, outWriter);
 // Since an exception should be thrown, the length and output should be 0
 assertEquals("Unexpected length of output", 0, length);
 assertEquals("Unexpected output", "", outWriter.toString());
 } catch (IOException e) {
 // Since we are using a StringWriter here, there should be no IOException
 }
 }

 @Test
 public void testTranslateComplex() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;#x00A1;&#x03B4;&#xA8;&#x1F5;";
 String expectedOutput = "&amp;A1;&pi;&ø;&p";
 try {
 Writer outWriter = new java.io.StringWriter();
 int length = translator.translate(input, 0, outWriter);
 assertEquals("Unexpected length of output", expectedOutput.length(), outWriter.toString().length());
 assertEquals("Unexpected output", expectedOutput, outWriter.toString());
 } catch (IOException e) {
 // Since we are using a StringWriter here, there should be no IOException
 }
 }

 @Test
 public void testTranslateWithSemicolon() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;#x00A1;&#x03B4;&#xA8;&#x1F5;";
 String expectedOutput = "&amp;A1;&pi;&ø;&p";
 try {
 Writer outWriter = new java.io.StringWriter();
 int length = translator.translate(input, 0, outWriter);
 assertEquals("Unexpected length of output", expectedOutput.length(), outWriter.toString().length());
 assertEquals("Unexpected output", expectedOutput, outWriter.toString());
 } catch (IOException e) {
 // Since we are using a StringWriter here, there should be no IOException
 }
 }

 @Test
 public void testTranslateWithSemicolonMissing() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;#x00A1;&#x03B4;&#xA8;";
 String expectedOutput = "&amp;A1&pi;&ø";
 try {
 Writer outWriter = new java.io.StringWriter();
 int length = translator.translate(input, 0, outWriter);
 assertEquals("Unexpected length of output", expectedOutput.length(), outWriter.toString().length());
 assertEquals("Unexpected output", expectedOutput, outWriter.toString());
 } catch (IOException e) {
 // Since we are using a StringWriter here, there should be no IOException
 }
 }
}