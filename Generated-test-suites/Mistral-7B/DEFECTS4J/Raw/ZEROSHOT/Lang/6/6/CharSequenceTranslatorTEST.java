import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharArrayTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequenceTranslator hexTranslator;

 @Test
 public void testTranslate() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 out.write(input.charAt(index));
 return Character.isSurrogate(input.charAt(index)) ? 2 : 1;
 }
 };

 String input = " TEST String";
 String expectedOutput = " %65%65%65%20%54%65%63%68%61%72%6B%65%65%20%53%65%72%76%65%72%73%69%6F%6E";

 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAggregateTranslator() {
 hexTranslator = new CharArrayTranslator() {
 @Override
 protected char[] toChars(String hex) {
 return new char[]{'\\u' + hex.charAt(0), '\\u' + hex.charAt(1)};
 }
 };

 translator = new AggregateTranslator(new CharSequenceTranslator[] {translator, hexTranslator});
 input = "TEST String";
 expectedOutput = " %65%65%65%20%54%65%63%68%61%72%6B%65%65%20%53%65%72%76%65%72%73%69%6F%6E%5C%5C%68%65%61%64%65%64";

 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWriter() {
 translator.translate(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 translator.translate(null, 0, null);
 }
}