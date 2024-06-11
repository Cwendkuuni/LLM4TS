import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.RegexTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequence input;

 @Test
 public void testInit() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return 1;
 }
 };
 assertEquals(0, translator.translate(null));
 }

 @Test
 public void testTranslateNullInput() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return 1;
 }
 };
 assertThrows(NullPointerException.class, () -> translator.translate(null, 0, null));
 }

 @Test
 public void testTranslateWithWriter() throws IOException {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 out.write('A');
 return 1;
 }
 };
 String result = translator.translate("ABC");
 assertEquals("A", result.substring(0, 1));
 }

 @Test
 public void testTranslateToString() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 out.write('A');
 return 1;
 }
 };
 String result = translator.translate("ABC");
 assertEquals("A", result.substring(0, 1));
 }

 @Test
 public void testTranslateAggregate() throws IOException {
 CharSequenceTranslator translator1 = new RegexTranslator("[-_]", " ");
 CharSequenceTranslator translator2 = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 out.write('A');
 return 1;
 }
 };
 CharSequenceTranslator translator3 = translator1.with(translator2);
 String result = translator3.translate("_-a-B-C-");
 assertEquals(" A A B A C ", result);
 }

 @Test
 public void testTranslateWith() {
 CharSequenceTranslator translator1 = new RegexTranslator("[-_]", " ");
 CharSequenceTranslator translator2 = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 out.write('A');
 return 1;
 }
 };
 CharSequenceTranslator translator3 = new AggregateTranslator(new CharSequenceTranslator[]{translator1, translator2});
 String result = translator3.translate("_-a-B-C-");
 assertEquals(" A A B A C ", result);
 }
}