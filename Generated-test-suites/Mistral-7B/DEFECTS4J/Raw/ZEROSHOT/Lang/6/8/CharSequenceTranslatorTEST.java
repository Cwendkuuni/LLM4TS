import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.SimpleTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharSequenceTranslatorTest {

 @Test
 public void testSimpleTranslator() {
 CharSequenceTranslator translator = new SimpleTranslator();
 CharSequence input = "abcdef";
 String output = translator.translate(input);
 assertEquals("%61%62%63%64%65%66", output);
 }

 @Test
 public void testAggregateTranslator() {
 CharSequenceTranslator translator1 = new SimpleTranslator();
 CharSequenceTranslator translator2 = new SimpleTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 if (index >= input.length() / 2) {
 return 0;
 }
 out.write(input.charAt(index));
 out.write(input.charAt(index + 1));
 return 2;
 }
 };
 CharSequenceTranslator finalTranslator = translator1.with(translator2);
 CharSequence input = "abcdef";
 String output = finalTranslator.translate(input);
 assertEquals("%61%62%63%64%61%62", output);
 }

 @Test
 public void testNullInput() {
 CharSequenceTranslator translator = new SimpleTranslator();
 String output = translator.translate((CharSequence) null);
 assertNull(output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWriter() {
 CharSequenceTranslator translator = new SimpleTranslator();
 translator.translate((CharSequence) null, (Writer) null);
 }
}