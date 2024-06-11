import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSimpleNumericEntity() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;";
 String expectedOutput = "&amp;";

 try {
 String output = translator.translate(input, 0, new StringWriter()).toString();
 assertEquals(expectedOutput, output);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateSimpleDecimalNumericEntity() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;#160;";
 String expectedOutput = " ";

 try {
 String output = translator.translate(input, 0, new StringWriter()).toString();
 assertEquals(expectedOutput, output);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateSimpleHexNumericEntity() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;#x61;";
 String expectedOutput = "a";

 try {
 String output = translator.translate(input, 0, new StringWriter()).toString();
 assertEquals(expectedOutput, output);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslateComplexNumericEntity() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;#x61;&#160;&#x6c;&#x69;&#x6e;&#x6b;&#x65;&#x65;&#x64;"
 + ";&#x73;&#x63;&#x72;&#x69;&#x70;&#x74;&#x3b;";
 String expectedOutput = "a  licence;";

 try {
 String output = translator.translate(input, 0, new StringWriter()).toString();
 assertEquals(expectedOutput, output);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidDecimalNumericEntity() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;#abc;";

 try {
 String output = translator.translate(input, 0, new StringWriter()).toString();
 } catch (IOException e) {
 // IOException expected
 } catch (NumberFormatException nfe) {
 fail("NumberFormatException should be thrown");
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateInvalidHexNumericEntity() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;#xfabc;";

 try {
 String output = translator.translate(input, 0, new StringWriter()).toString();
 } catch (IOException e) {
 // IOException expected
 } catch (NumberFormatException nfe) {
 fail("NumberFormatException should be thrown");
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testTranslateOutOfBoundIndex() {
 CharSequenceTranslator translator = new NumericEntityUnescaper();
 String input = "&amp;#";

 try {
 translator.translate(input, 1, new StringWriter());
 fail("ArrayIndexOutOfBoundsException should be thrown");
 } catch (IOException e) {
 // IOException expected
 } catch (ArrayIndexOutOfBoundsException aioobe) {
 // expected
 }
 }
}