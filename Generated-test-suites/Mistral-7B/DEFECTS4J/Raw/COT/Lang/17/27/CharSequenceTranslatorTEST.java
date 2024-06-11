import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequence input;
 private Writer out;
 private StringWriter stringWriter;

 @Before
 public void setUp() {
 translator = new MyCharSequenceTranslator();
 input = "test";
 stringWriter = new StringWriter();
 out = new StringWriter(stringWriter);
 }

 @Test
 public void testTranslateAbstractMethod() throws IOException {
 int consumed = translator.translate(input, 0, out);
 assertEquals(input.length(), consumed);
 }

 @Test
 public void testTranslateFinalMethod() throws IOException {
 String output = translator.translate(input);
 assertNotNull(output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNullWriter() {
 translator.translate(input, null);
 }

 @Test
 public void testTranslateNullInput() {
 translator.translate(null, out);
 }

 @Test
 public void testTranslateWith() throws IOException {
 CharSequenceTranslator translator2 = new AnotherCharSequenceTranslator();
 CharSequenceTranslator mergedTranslator = translator.with(translator2);
 int consumed = mergedTranslator.translate(input, 0, out);
 assertEquals(input.length() * 2, consumed); // both translators should process the input sequence
 }

 @Test(expected = RuntimeException.class)
 public void testIOExceptionFromTranslate() throws IOException {
 StringWriter stringWriterWithException = new StringWriter() {
 @Override
 public void write(int c, Writer writer) throws IOException {
 throw new IOException();
 }
 };
 translator.translate(input, 0, stringWriterWithException);
 }

 @Test
 public void testHexMethod() {
 assertEquals("41", CharSequenceTranslator.hex(65));
 }

}

class MyCharSequenceTranslator extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return out.write(Character.codePointAt(input, index)) > 0 ? 1 : 0;
 }
}

class AnotherCharSequenceTranslator extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return 2;
 }
}