import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequenceTranslator escaper;
 private CharSequenceTranslator unescaper;

 @Test
 public void testCharSequenceTranslator() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return 0;
 }
 };
 assertEquals("", translator.translate(String.valueOf('')));
 }

 @Test
 public void testAggregateTranslator() {
 escaper = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 if (Character.isAlphabetic(Character.codePointAt(input, index))) {
 out.write(hex(Character.codePointAt(input, index)).toCharArray());
 return Character.charCount(Character.codePointAt(input, index));
 } else {
 return 0;
 }
 }
 };

 unescaper = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 if (input.charAt(index) == '\\') {
 index++;
 String hex = StringUtils.substring(input, index, index + 4);
 out.write(Integer.parseInt(hex, 16));
 return 4;
 } else {
 return Character.isWhitespace(input.charAt(index)) ? 0 : Character.charCount(Character.codePointAt(input, index));
 }
 }
 };

 translator = escaper.with(unescaper);

 String testString = "Hello \\u0041\\u0042\\u0043 World!";
 String expected = "Hello %&HH%&H1%&H2%&H2%&H3%&H3%&H3 World!";
 String result = translator.translate(testString);
 assertEquals(expected, result);
 }

 @Test
 public void testNullInput() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return 0;
 }
 };
 assertNull(translator.translate(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWriter() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return 0;
 }
 };
 translator.translate(String.valueOf(''), null);
 }
}