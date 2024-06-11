import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 @Test
 public void testNumericEntityUnescaper_ValidCases() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 // Test valid numeric entities with semicolon
 String input1 = "&nbsp; &amp; &copy; &iexcl; &cent; &pound; &curren; &yen; &euro; &reg;";
 String expectedOutput1 = "    &amp; © ! ° ° C £ £ £ £ € ©";
 try (Writer out = new java.io.StringWriter()) {
 unescaper.translate(new StringReader(input1), 0, out);
 assertEquals(expectedOutput1, out.toString());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Test failed due to IOException: " + e.getMessage());
 }

 // Test valid numeric entities without semicolon
 String input2 = "&x202F; &#x2122;";
 String expectedOutput2 = "  ⁝";
 try (Writer out = new java.io.StringWriter()) {
 unescaper.translate(new StringReader(input2), 0, out);
 assertEquals(expectedOutput2, out.toString());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Test failed due to IOException: " + e.getMessage());
 }
 }

 @Test
 public void testNumericEntityUnescaper_InvalidCases() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 // Test invalid character sequence
 String input1 = "&asd;";
 String expectedOutput1 = "";
 try (Writer out = new java.io.StringWriter()) {
 unescaper.translate(new StringReader(input1), 0, out);
 assertEquals(expectedOutput1, out.toString());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Test failed due to IOException: " + e.getMessage());
 }

 // Test invalid numeric entity with x but no hex value
 String input2 = "&x;";
 String expectedOutput2 = "";
 try (Writer out = new java.io.StringWriter()) {
 unescaper.translate(new StringReader(input2), 0, out);
 assertEquals(expectedOutput2, out.toString());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Test failed due to IOException: " + e.getMessage());
 }

 // Test invalid numeric entity without x but with hex value
 String input3 = "&#xfasd;";
 String expectedOutput3 = "";
 try (Writer out = new java.io.StringWriter()) {
 unescaper.translate(new StringReader(input3), 0, out);
 assertEquals(expectedOutput3, out.toString());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Test failed due to IOException: " + e.getMessage());
 }
 }
}