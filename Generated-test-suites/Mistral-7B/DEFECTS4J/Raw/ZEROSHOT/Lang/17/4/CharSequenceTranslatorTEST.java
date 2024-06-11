import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

 @Test
 public void testTranslate() throws Exception {
 CharSequenceTranslator translator = new MyCharSequenceTranslator();
 CharSequence input = "hello world";
 String output = translator.translate(input);
 assertEquals("expected output: 48656c6c6f20776f726c64", output);
 }

 @Test
 public void testTranslateWithAggregate() throws Exception {
 CharSequenceTranslator translator1 = new MyCharSequenceTranslator1();
 CharSequenceTranslator translator2 = new MyCharSequenceTranslator2();
 CharSequenceTranslator compositeTranslator = translator1.with(translator2);
 CharSequence input = "hello world";
 String output = compositeTranslator.translate(input);
 assertEquals("expected output: 48656c6c6f20776f726c64_translation1_translation2", output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWriter() throws Exception {
 CharSequenceTranslator translator = new MyCharSequenceTranslator();
 CharSequence input = "hello world";
 Writer out = null;
 try {
 translator.translate(input, out);
 fail("expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IOException.class)
 public void testIOException() throws Exception {
 Writer out = new StringWriter() {
 @Override
 public void write(int c) throws IOException {
 if (c == 'x') {
 throw new IOException("expected IOException");
 }
 super.write(c);
 }
 };
 CharSequenceTranslator translator = new MyCharSequenceTranslator();
 CharSequence input = "hello world";
 translator.translate(input, out);
 fail("expected IOException");
 }

 @Test
 public void testTranslateWithSurrogatePair() throws Exception {
 CharSequenceTranslator translator = new MyCharSequenceTranslator();
 CharSequence input = "\uD800\uDC00";
 String output = translator.translate(input);
 assertEquals("\uD800\uDC00", output);
 }

 @Test
 public void testTranslateToWriter() throws Exception {
 CharSequenceTranslator translator = new MyCharSequenceTranslator();
 CharSequence input = "hello world";
 StringWriter out = new StringWriter();
 translator.translate(input, out);
 String output = out.toString();
 assertEquals("expected output: 48656c6c6f20776f726c64", output);
 }

 @Test
 public void testTranslateToWriterWithSurrogatePair() throws Exception {
 CharSequenceTranslator translator = new MyCharSequenceTranslator();
 CharSequence input = "\uD800\uDC00";
 StringWriter out = new StringWriter();
 translator.translate(input, out);
 String output = out.toString();
 assertEquals("\uD800\uDC00", output);
 }

 @Test
 public void testTranslateWithAggregateToWriter() throws Exception {
 CharSequenceTranslator translator1 = new MyCharSequenceTranslator1();
 CharSequenceTranslator translator2 = new MyCharSequenceTranslator2();
 CharSequenceTranslator compositeTranslator = translator1.with(translator2);
 CharSequence input = "hello world";
 StringWriter out = new StringWriter();
 compositeTranslator.translate(input, out);
 String output = out.toString();
 assertEquals("expected output: 48656c6c6f20776f726c64_translation1_translation2", output);
 }

 private static class MyCharSequenceTranslator extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 if (index >= input.length()) {
 return 0;
 }
 char c = Character.isSupplementaryCodePoint(input.charAt(index)) ? Character.highSurrogate(input.charAt(index)) : input.charAt(index);
 out.write(c);
 return c == '\uD800' ? 2 : 1;
 }
 }

 private static class MyCharSequenceTranslator1 extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 if (index >= input.length()) {
 return 0;
 }
 char c = input.charAt(index);
 if (c == '_') {
 out.write("translation1_");
 } else {
 out.write(c);
 }
 return 1;
 }
 }

 private static class MyCharSequenceTranslator2 extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 if (index >= input.length()) {
 return 0;
 }
 char c = input.charAt(index);
 if (c == '_') {
 out.write("translation2_");
 } else {
 out.write(c);
 }
 return 1;
 }
 }
}