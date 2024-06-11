import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.TranslatorUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequenceTranslator aggregateTranslator;

 @Test
 public void testTranslate() {
 translator = new MyCharSequenceTranslator();
 String input = "Hello, World!";
 String expectedOutput = "48656c6c6f2c20576f726c6421";
 String actualOutput = translator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTranslateWithAggregate() {
 aggregateTranslator = new AggregateTranslator(new MyCharSequenceTranslator(), new TranslatorUtils());
 String input = "Hello, World!";
 String expectedOutput = "48656c6c6f2c20576f726c6421"; // same as MyCharSequenceTranslator
 String actualOutput = aggregateTranslator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNullWriter() {
 CharSequenceTranslator translator = new MyCharSequenceTranslator();
 Writer out = null;
 try {
 translator.translate(TranslatorTestHelper.getCharSequence("test"), out);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testTranslateWithEmptyInput() {
 CharSequenceTranslator translator = new MyCharSequenceTranslator();
 String input = "";
 String expectedOutput = "";
 String actualOutput = translator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTranslateWithSurrogatePair() {
 CharSequenceTranslator translator = new MyCharSequenceTranslator();
 String input = "𠮷";
 String expectedOutput = "d840\u00dc";
 String actualOutput = translator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testAggregateTranslate() {
 CharSequenceTranslator translator1 = new MyCharSequenceTranslator();
 CharSequenceTranslator translator2 = new TranslatorUtils();
 AggregateTranslator aggregateTranslator = new AggregateTranslator(translator1, translator2);
 String input = "Hello, World! 你好，世界！";
 String expectedOutput = "48656c6c6f2c20576f726c6421 e4bd80a5e5aeac1 e683b3bc8897ba0d";
 String actualOutput = aggregateTranslator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IOException.class)
 public void testTranslateWithIOException() {
 CharSequenceTranslator translator = new MyCharSequenceTranslator();
 Writer out = new StringWriter() {
 @Override
 public void write(int c) throws IOException {
 throw new IOException();
 }
 };
 String input = "Hello, World!";
 try {
 translator.translate(input, out);
 fail("Expected IOException");
 } catch (IOException e) {
 // pass
 }
 }
}