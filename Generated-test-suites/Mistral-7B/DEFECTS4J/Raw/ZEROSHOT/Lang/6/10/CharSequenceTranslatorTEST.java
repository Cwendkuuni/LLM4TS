import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharacterMapTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.StringWriter;
import java.util.Locale;

public class CharSequenceTranslatorTest {

 @Test
 public void testCharSequenceTranslator() {
 CharSequenceTranslator translator = new CharacterMapTranslator();
 CharSequence input = "Test String  aanm & ee's";
 String expectedOutput = "Test String %61%6E%64%20%61%6E%64%20%61%6D%65%20%61%6D%20%65%6E%63%6F%64%65%20%61%67%65%20%65%6E%27%20%65%73%27%20%65%73%20%65%73%20%65%6E%63%6F%64%65%64";
 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAggregateTranslator() {
 CharSequenceTranslator translator1 = new CharacterMapTranslator();
 CharSequenceTranslator translator2 = new ToUpperCaseTranslator();
 CharSequenceTranslator translator = translator1.with(translator2);
 CharSequence input = "Test String  aanm & ee's";
 String expectedOutput = "TEST STRING %61%6E%64%20%61%6E%64%20%61%6D%65%20%61%6D%20%54%68%69%73%20%61%67%65%20%53%65%71%75%65%20%65%73%20%65%73%20%65%73%20%53%65%71%75%65%64";
 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testWriterTranslator() throws IOException {
 CharSequenceTranslator translator = new CharacterMapTranslator();
 CharSequence input = "Test String  aanm & ee's";
 StringWriter writer = new StringWriter();
 translator.translate(input, writer);
 String expectedOutput = "Test String %61%6E%64%20%61%6E%64%20%61%6D%65%20%61%6D%20%54%68%69%73%20%61%67%65%20%53%65%71%75%65%20%65%73%20%65%73%20%65%73%20%53%65%71%75%65%64";
 String output = writer.toString();
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWriter() {
 CharSequenceTranslator translator = new CharacterMapTranslator();
 CharSequence input = "Test String  aanm & ee's";
 Writer out = null;
 translator.translate(input, out);
 }
}

class CharacterMapTranslator extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 char c = Character.codePointAt(input, index);
 String hex = CharSequenceTranslator.hex((int) c);
 out.write(hex);
 return Character.charCount(c);
 }
}

class ToUpperCaseTranslator extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 char c = Character.codePointAt(input, index);
 out.write(Character.toUpperCase(c));
 return Character.charCount(c);
 }
}

class AggregateTranslator extends CharSequenceTranslator {
 private CharSequenceTranslator[] translators;

 public AggregateTranslator(CharSequenceTranslator[] translators) {
 this.translators = translators;
 }

 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 int totalConsumed = 0;
 for (CharSequenceTranslator translator : translators) {
 int consumed = translator.translate(input, index, out);
 if (consumed > 0) {
 totalConsumed += consumed;
 index += consumed;
 }
 if (consumed == 0) {
 char c = Character.codePointAt(input, index);
 out.write(c);
 index += Character.charCount(c);
 }
 if (consumed < 0) {
 throw new IllegalStateException("Invalid number of codepoints consumed by translator: " + consumed);
 }
 }
 return totalConsumed;
 }
}