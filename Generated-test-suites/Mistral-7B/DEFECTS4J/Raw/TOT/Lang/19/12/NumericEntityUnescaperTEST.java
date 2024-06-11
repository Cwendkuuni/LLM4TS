import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link NumericEntityUnescaper} class.
 */
public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslationWithValidNumericEntities() {
 String input = "&amp;#10; hello &#13; world &#x27; &#x61; &#xFFFF;";
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("&amp;");
 expectedOutput.append((char) 10);
 expectedOutput.append(" hello ");
 expectedOutput.append((char) 13);
 expectedOutput.append(" world ");
 expectedOutput.append((char) '\'');
 expectedOutput.append((char) 0x61);
 expectedOutput.append((char) 0xFFFF);

 StringBuilder actualOutput = new StringBuilder();
 try {
 unescaper.translate(new StringReader(input), 0, new StringWriter(actualOutput));
 } catch (IOException e) {
 throw new AssertionError("Unexpected IOException: " + e.getMessage());
 }

 assertEquals(expectedOutput.toString(), actualOutput.toString());
 }

 @Test
 public void testTranslationWithValidNumericEntitiesWithSemicolon() {
 String input = "hello &amp;#x1F44D; world";
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("hello ");
 expectedOutput.append((char) 0x1F44D);
 expectedOutput.append(" world");

 StringBuilder actualOutput = new StringBuilder();
 try {
 unescaper.translate(new StringReader(input), 0, new StringWriter(actualOutput));
 } catch (IOException e) {
 throw new AssertionError("Unexpected IOException: " + e.getMessage());
 }

 assertEquals(expectedOutput.toString(), actualOutput.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslationWithInvalidCharacterSequence() {
 String input = "hello &amp;#xabcd";
 StringBuilder actualOutput = new StringBuilder();
 try {
 unescaper.translate(new StringReader(input), 0, new StringWriter(actualOutput));
 } catch (IOException e) {
 throw new AssertionError("Unexpected IOException: " + e.getMessage());
 }

 // Should throw an IllegalArgumentException
 }
}