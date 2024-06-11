import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.CharSequenceUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

 private CharSequence[][] lookupTable = new CharSequence[][]{{"one", "uno"}, {"two", "dos"}, {"three", "tres"}, {"four", "cuatro"}, {"five", "cinco"}};

 @Test
 public void testLookupTranslatorConstruction() {
 LookupTranslator translator = new LookupTranslator(lookupTable);
 assertNotNull(translator.lookupMap);
 assertEquals(5, translator.lookupMap.size());
 }

 @Test
 public void testTranslate() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookupTable);
 Writer out = new StringWriter();

 // Test translating valid sequences
 for (CharSequence input : lookupTable) {
 int length = input.length();
 int index = 0;
 int expectedOutputLength = length;
 CharSequence expectedOutput = input;

 CharSequence translatedSequence = translator.translate(input, index, out);
 String actualOutput = out.toString();

 assertEquals(expectedOutputLength, translatedSequence.length());
 assertEquals(expectedOutput.toString(), actualOutput);
 }

 // Test translating invalid sequences
 CharSequence invalidInput = "six";
 int invalidInputLength = invalidInput.length();
 int invalidIndex = 0;

 CharSequence translatedInvalidSequence = translator.translate(invalidInput, invalidIndex, out);
 String actualInvalidOutput = out.toString();

 assertEquals(0, translatedInvalidSequence.length());
 assertEquals("", actualInvalidOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNullInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookupTable);
 Writer out = new StringWriter();

 CharSequence nullInput = null;
 int nullIndex = 0;

 translator.translate(nullInput, nullIndex, out);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateInputBeyondBounds() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookupTable);
 Writer out = new StringWriter();

 CharSequence input = "onetwothree";
 int index = input.length();

 translator.translate(input, index, out);
 }

 @Test(expected = IOException.class)
 public void testTranslateWriterNull() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookupTable);

 CharSequence input = "one";
 int index = 0;
 Writer nullWriter = null;

 translator.translate(input, index, nullWriter);
 }

 @Test
 public void testTranslateLargeInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookupTable);
 Writer out = new StringWriter();

 CharSequence input = CharSequenceUtils.repeat("one", 1000);
 int index = 0;
 int expectedOutputLength = 1000;

 CharSequence translatedSequence = translator.translate(input, index, out);
 String actualOutput = out.toString();

 assertEquals(expectedOutputLength, translatedSequence.length());
 for (int i = 0; i < expectedOutputLength; i++) {
 assertEquals("o", String.valueOf(translatedSequence.charAt(i)));
 }
 }
}