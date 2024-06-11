import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.TextUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequence input;
 private StringWriter writer;

 @Before
 public void setUp() {
 // Initialize translator and input
 translator = new ASCIIStringTranslator();
 input = "Hello, 世界!";
 writer = new StringWriter();
 }

 @Test
 public void testTranslateNormalCharacters() throws Exception {
 int consumed = translator.translate(input, 0, writer);
 assertEquals("Translated normal characters correctly.", input.subSequence(0, consumed).toString(), writer.toString());
 }

 @Test
 public void testTranslateSurrogatePair() throws Exception {
 input = "\uD840\uDC00";
 writer = new StringWriter();
 int consumed = translator.translate(input, 0, writer);
 assertEquals("Translated surrogate pair correctly.", "\uD83D\uDC00", writer.toString());
 }

 @Test(expected = IOException.class)
 public void testIOException() throws Exception {
 input = "Hello, ";
 writer = new StringWriter() {
 @Override
 public void write(int c) throws IOException {
 throw new IOException("Write operation failed.");
 }
 };
 translator.translate(input, 0, writer);
 fail("IOException should have been thrown.");
 }

 @Test
 public void testTranslateWithAggregateTranslator() throws Exception {
 CharSequenceTranslator translator1 = new ASCIIStringTranslator();
 CharSequenceTranslator translator2 = new HTMLStringTranslator();
 CharSequenceTranslator translator3 = new URLStringTranslator();
 CharSequenceTranslator aggregateTranslator = translator1.with(translator2, translator3);
 int consumed = aggregateTranslator.translate(input, 0, writer);
 String expectedOutput = TextUtils.escapeHtml4(TextUtils.escapeUrl(new String(input.toString().getBytes("UTF-8"))));
 assertEquals("Translated with multiple translators correctly.", expectedOutput, writer.toString());
 }

 @Test
 public void testTranslateWithEmptyInput() throws Exception {
 writer = new StringWriter();
 int consumed = translator.translate(CharSequence.empty(), 0, writer);
 assertEquals("Translated empty input correctly.", "", writer.toString());
 }

 @Test
 public void testTranslateWithNullInput() throws Exception {
 assertNull("Translated null input correctly.", translator.translate(null, 0, writer));
 }

 @Test
 public void testTranslateWithNullWriter() throws Exception {
 try {
 translator.translate(input, 0, null);
 fail("NullPointerException should have been thrown.");
 } catch (NullPointerException e) {
 // Expected
 }
 }
}