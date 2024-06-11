import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequence input;
 private StringWriter output;

 @Before
 public void setUp() {
 translator = new IdentityTranslator();
 input = " Hello World!";
 output = new StringWriter();
 }

 /**
 * Test identity translator.
 */
 @Test
 public void testIdentityTranslator() {
 int length = translator.translate(input, 0, output);
 assertEquals(input.length(), length);
 assertEquals(input.toString(), output.toString());
 }

 /**
 * Test aggregate translator with hex translator.
 */
 @Test
 public void testAggregateTranslator() {
 CharSequenceTranslator hexTranslator = new HexTranslator();
 translator = translator.with(hexTranslator);

 int length = translator.translate(input, 0, output);
 assertEquals(input.length() * 2, length);
 String expected = StringUtils.replaceEach(input.toString(), new String[]{" "}, new String[]{"%20"});
 assertEquals(expected, output.toString());
 }

 /**
 * Test translate method with Writer.
 */
 @Test
 public void testTranslateMethodWithWriter() throws IOException {
 translator = new HexTranslator();
 int length = translator.translate(input, 0, output);
 assertEquals(input.length() * 2, length);
 String expected = StringUtils.replaceEach(input.toString(), new String[]{" "}, new String[]{"%20"});
 assertEquals(expected, output.getBuffer().toString());
 }

 /**
 * Test translate method with null input.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMethodWithNullInput() {
 translator.translate(null, 0, output);
 }

 /**
 * Test translate method with null Writer.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMethodWithNullWriter() {
 translator.translate(input, 0, null);
 }

}

 /**
 * Identity translator that doesn't change the input.
 */
 private static class IdentityTranslator extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 char c = Character.codePointAt(input, index);
 int consumed = Character.charCount(c) > 0 ? Character.charCount(c) : 1;
 out.write(Character.toChars(c));
 return consumed;
 }
 }

 /**
 * Translator that converts characters to hexadecimal.
 */
 private static class HexTranslator extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 char c = Character.codePointAt(input, index);
 int consumed = Character.charCount(c) > 0 ? Character.charCount(c) : 1;
 String hex = CharSequenceTranslator.hex((int) c);
 out.write(hex.toCharArray(), 0, hex.length());
 return consumed;
 }
 }

 /**
 * Aggregator translator that merges multiple translators.
 */
 private static class AggregateTranslator extends CharSequenceTranslator {
 private final CharSequenceTranslator[] translators;

 public AggregateTranslator(CharSequenceTranslator[] translators) {
 this.translators = translators;
 }

 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 int totalConsumed = 0;
 for (CharSequenceTranslator translator : translators) {
 int consumed = translator.translate(input, index, out);
 totalConsumed += consumed;
 if (consumed == 0) {
 index++;
 continue;
 }
 index += consumed;
 }
 return totalConsumed;
 }
 }