import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequence lookUpKey1;
 private CharSequence lookUpValue1;
 private CharSequence lookUpKey2;
 private CharSequence lookUpValue2;
 private CharSequence input;
 private Writer outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {}

 @Override
 public void write(String s, int off, int len) throws IOException {
 // save the translated string for assertions
 }
 };

 @Test
 public void testEmptyConstructor() {
 translator = new LookupTranslator(null);
 assertEquals(0, translator.translate(input, 0, outWriter));
 }

 @Test
 public void testSimpleLookup() {
 // Initialize LookupTranslator with a lookup table
 lookUpKey1 = "KEY1";
 lookUpValue1 = "VALUE1";
 translator = new LookupTranslator(new CharSequence[][] { { lookUpKey1, lookUpValue1 } });

 // Test translation with a matching sequence
 input = lookUpKey1;
 int length = lookUpKey1.length();
 assertEquals(length, translator.translate(input, 0, outWriter));
 // Check the output
 assertEquals(lookUpValue1.toString(), outWriter.toString());
 outWriter.toString().isEmpty(); // Reset the Writer for the next test

 // Test translation with a non-matching sequence
 input = "OTHER_KEY";
 assertEquals(0, translator.translate(input, 0, outWriter));
 }

 @Test
 public void testMultipleLookups() {
 // Initialize LookupTranslator with multiple lookup sequences
 lookUpKey1 = "KEY1";
 lookUpValue1 = "VALUE1";
 lookUpKey2 = "KEY2";
 lookUpValue2 = "VALUE2";
 translator = new LookupTranslator(new CharSequence[][] { { lookUpKey1, lookUpValue1 }, { lookUpKey2, lookUpValue2 } });

 // Test translation with a matching sequence
 input = lookUpKey1;
 int length = lookUpKey1.length();
 assertEquals(length, translator.translate(input, 0, outWriter));
 // Check the output
 assertEquals(lookUpValue1.toString(), outWriter.toString());
 outWriter.toString().isEmpty(); // Reset the Writer for the next test

 // Test translation with another matching sequence
 input = lookUpKey2;
 length = lookUpKey2.length();
 assertEquals(length, translator.translate(input, 0, outWriter));
 // Check the output
 assertEquals(lookUpValue2.toString(), outWriter.toString());
 outWriter.toString().isEmpty(); // Reset the Writer for the next test

 // Test translation with a non-matching sequence
 input = "OTHER_KEY";
 assertEquals(0, translator.translate(input, 0, outWriter));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 translator = new LookupTranslator(null);
 input = null;
 translator.translate(input, 0, outWriter);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBounds() {
 translator = new LookupTranslator(new CharSequence[][] { { lookUpKey1, lookUpValue1 } });
 input = lookUpKey1;
 int length = lookUpKey1.length();
 translator.translate(input, length, outWriter);
 }

 @Test(expected = IOException.class)
 public void testWriterException() throws IOException {
 translator = new LookupTranslator(new CharSequence[][] { { lookUpKey1, lookUpValue1 } });
 input = lookUpKey1;
 int length = lookUpKey1.length();
 outWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 throw new IOException("Writer Exception");
 }
 };
 translator.translate(input, 0, outWriter);
 }
}