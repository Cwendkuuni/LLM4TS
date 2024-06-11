import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private final CharSequenceTranslator translator = new Translator();
 private final CharSequenceTranslator aggregator = new AggregatorTranslator(new Translator(), new Translator());

 @Test
 public void testTranslateNormalCase() throws IOException {
 String input = "Hello World!";
 String expectedOutput = "Hello%20World%21";
 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslateEmptyString() throws IOException {
 String input = "";
 String output = translator.translate(input);
 assertEquals(input, output);
 }

 @Test
 public void testTranslateNullInput() throws IOException {
 assertNull(translator.translate(null));
 }

 @Test
 public void testTranslateWithAggregator() throws IOException {
 String input = "Hello %World%!";
 String expectedOutput = "Hello %25World%25%21";
 String output = aggregator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNullWriter() throws IOException {
 CharSequenceTranslator translator = new Translator();
 Writer writer = null;
 int len = Character.codePointCount("Hello");
 int consumed = 0;
 for (int i = 0; i < len; i++) {
 consumed += translator.translate(Character.toString(Character.toChars(Character.codePointAt("Hello", i))), i, writer);
 }
 fail("IOException expected");
 }

 @Test
 public void testTranslateWithWriter() throws IOException {
 StringWriter writer = new StringWriter();
 String input = "Hello World!";
 int len = Character.codePointCount(input, 0, input.length());
 int pos = 0;
 while (pos < len) {
 int consumed = translator.translate(Character.toString(Character.toChars(Character.codePointAt(input, pos))), pos, writer);
 pos += consumed;
 }
 String output = writer.toString();
 String expectedOutput = "Hello%20World%21";
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslateWithWriterIOException() throws IOException {
 StringWriter writer = new StringWriter() {
 @Override
 public void write(int c, Writer writer) throws IOException {
 throw new IOException();
 }
 };
 String input = "Hello World!";
 try {
 translator.translate(Character.toString(Character.toChars(Character.codePointAt(input, 0))), 0, writer);
 fail("IOException not thrown");
 } catch (IOException e) {
 // expected
 }
 }

 class Translator extends CharSequenceTranslator {
 @Override
 protected int translate(CharSequence cs, int begin, Writer writer) throws IOException {
 for (int i = begin, len = cs.length(); i < len; i++) {
 char c = cs.charAt(i);
 writer.write(Character.isDefinition(c) ? hex(Character.codePointAt(cs, i)) : c);
 }
 return cs.length();
 }
 }

 class AggregateTranslator extends CharSequenceTranslator {
 private final CharSequenceTranslator[] translators;

 public AggregateTranslator(CharSequenceTranslator... translators) {
 this.translators = translators;
 }

 @Override
 protected int translate(CharSequence cs, int begin, Writer writer) throws IOException {
 int totalConsumed = 0;
 for (CharSequenceTranslator translator : translators) {
 int consumed = translator.translate(cs, begin, writer);
 totalConsumed += consumed;
 begin += consumed;
 }
 return totalConsumed;
 }
 }

}