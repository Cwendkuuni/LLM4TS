import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslatorFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequenceTranslator translatorWithEscapes;

 @Test
 public void testInit() {
 // Initialize translator with default configuration
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence cs, int index, Writer writer) throws IOException {
 // Empty implementation, since testing will be done using the translate method
 return 1;
 }
 };

 // Initialize translator with escapes
 translatorWithEscapes = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence cs, int index, Writer writer) throws IOException {
 String input = cs.subSequence(index, index + 1).toString();
 if ("%".equals(input)) {
 writer.write("%");
 index++;
 return 1;
 }
 return translateNextChar(cs, index, writer);
 }

 private int translateNextChar(CharSequence cs, int index, Writer writer) throws IOException {
 int nextChar = Character.codePointAt(cs, index);
 int charsConsumed = translateChar(nextChar, writer);
 index += Character.charCount(nextChar);
 return charsConsumed;
 }

 private int translateChar(int cp, Writer writer) throws IOException {
 switch (cp) {
 case '&':
 writer.write("&amp;");
 break;
 case '<':
 writer.write("&lt;");
 break;
 case '>':
 writer.write("&gt;");
 break;
 case '\'':
 writer.write("&apos;");
 break;
 case '"':
 writer.write("&quot;");
 break;
 default:
 writer.write(Character.toString((char) cp));
 break;
 }
 return 1;
 }
 };
 }

 @Test
 public void testTranslate_Default() {
 String input = "Hello, World!";
 String expectedOutput = "Hello, World!";
 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslate_WithEscapes() {
 String input = "Hello, <World!>";
 String expectedOutput = "Hello, &lt;World!&gt;";
 String output = translatorWithEscapes.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslate_WithAggregateTranslator() {
 // Create translator with default configuration and translator with escapes
 CharSequenceTranslator translator1 = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence cs, int index, Writer writer) throws IOException {
 return 1;
 }
 };
 CharSequenceTranslator translator2 = translatorWithEscapes;
 CharSequenceTranslator translator3 = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence cs, int index, Writer writer) throws IOException {
 String input = cs.subSequence(index, index + 1).toString();
 if ("%".equals(input)) {
 writer.write("%");
 index++;
 return 1;
 }
 return translateNextChar(cs, index, writer);
 }

 private int translateNextChar(CharSequence cs, int index, Writer writer) throws IOException {
 int nextChar = Character.codePointAt(cs, index);
 int charsConsumed = translateChar(nextChar, writer);
 index += Character.charCount(nextChar);
 return charsConsumed;
 }

 private int translateChar(int cp, Writer writer) throws IOException {
 switch (cp) {
 case '&':
 writer.write("&amp;");
 break;
 case '<':
 writer.write("&lt;");
 break;
 case '>':
 writer.write("&gt;");
 break;
 case '\'':
 writer.write("&apos;");
 break;
 case '"':
 writer.write("&quot;");
 break;
 default:
 writer.write(Character.toString((char) cp));
 break;
 }
 return 1;
 }
 };

 // Merge translators and check translation
 CharSequenceTranslator aggregateTranslator = translator1.with(translator2, translator3);
 String input = "Hello, <World!>&Hello, World!>";
 String expectedOutput = "Hello, &lt;World!&gt;&amp;Hello, World!";
 String output = aggregateTranslator.translate(input);
 assertEquals(expectedOutput, output);
 }

}