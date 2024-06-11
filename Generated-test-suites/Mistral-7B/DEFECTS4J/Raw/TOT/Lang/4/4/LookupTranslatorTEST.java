import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

 private static final CharSequence[][] LOOKUP = new CharSequence[][] {
 {"hello", "hi"}, {"world", "planet"}, {"test", "check"}, {"error", "issue"}, {"abcd", "xyz"}
 };

 @Test
 public void testTranslate() throws Exception {
 LookupTranslator translator = new LookupTranslator(LOOKUP);
 Writer writer = new StringWriter();

 // typical use case
 CharSequence input = "hello world test";
 int index = 0;
 int translatedLength = translator.translate(input, index, writer);
 assertEquals("hi planet check", writer.toString());
 index += translatedLength;

 // check if all input was translated
 assertEquals(input.length(), index);

 // edge case: empty lookup table
 LookupTranslator emptyTranslator = new LookupTranslator(new CharSequence[][] {});
 writer.getBufferedWriter().reset();
 input = "abcdef";
 index = 0;
 translatedLength = emptyTranslator.translate(input, index, writer);
 assertEquals("", writer.toString());
 index += translatedLength;

 // edge case: empty input
 input = "";
 index = 0;
 translatedLength = translator.translate(input, index, writer);
 assertEquals("", writer.toString());

 // edge case: input longer than lookup table
 input = "abcdefghijklmnopqrst";
 index = 0;
 translatedLength = translator.translate(input, index, writer);
 assertEquals("", writer.toString());

 // edge case: input shorter than lookup table
 input = "abc";
 index = 0;
 translatedLength = translator.translate(input, index, writer);
 assertEquals("xyz", writer.toString());

 // error scenario: null input
 try {
 translator.translate(null, 0, writer);
 fail();
 } catch (IOException e) {
 // expected
 }
 }
}