import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator.*; // for CharSequence[][]
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

 private LookupTranslator lookupTranslator;

 @Test
 public void testLookupTranslatorConstructor() {
 CharSequence[][] lookup = new CharSequence[][]{{new CharSequence("A", "Z"), new CharSequence("B", "Y")}, {new CharSequence("C", "X"), new CharSequence("D", "W")}};
 lookupTranslator = new LookupTranslator(lookup);
 assertNotNull(lookupTranslator.lookupMap);
 assertNotNull(lookupTranslator.shortest);
 assertNotNull(lookupTranslator.longest);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupTranslatorNullLookup() {
 lookupTranslator = new LookupTranslator(null);
 fail("It should not accept null lookup table");
 }

 @Test
 public void testTranslateMethodBasicFunctionality() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{new CharSequence("A", "Z"), new CharSequence("1", "2")}, {new CharSequence("B", "3"), new CharSequence("4")}});
 String input = "AB";
 Writer out = new StringWriter();
 int translatedLength = lookupTranslator.translate(new StringCharSequence(input), 0, out);
 String expectedOutput = "13";
 assertEquals(expectedOutput, out.toString());
 assertEquals(2, translatedLength);
 }

 @Test
 public void testTranslateMethodInputOutOfBounds() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{new CharSequence("A", "Z"), new CharSequence("1", "2")}, {new CharSequence("B", "3"), new CharSequence("4")}});
 String input = "ABC";
 Writer out = new StringWriter();
 try {
 lookupTranslator.translate(new StringCharSequence(input), input.length(), out);
 fail("Should have thrown an IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testTranslateMethodEmptyInput() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{new CharSequence("A", "Z"), new CharSequence("1", "2")}, {new CharSequence("B", "3"), new CharSequence("4")}});
 String input = "";
 Writer out = new StringWriter();
 int translatedLength = lookupTranslator.translate(new StringCharSequence(input), 0, out);
 assertEquals(0, translatedLength);
 assertEquals("", out.toString());
 }

 @Test
 public void testTranslateMethodInputShortThanShortestKey() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{new CharSequence("AB", "1234"), new CharSequence("XY", "5678")}, {new CharSequence("CD", "9876"), new CharSequence("MN", "3456")}});
 String input = "AB";
 Writer out = new StringWriter();
 int translatedLength = lookupTranslator.translate(new StringCharSequence(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, translatedLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMethodInputNull() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{new CharSequence("A", "Z"), new CharSequence("1", "2")}, {new CharSequence("B", "3"), new CharSequence("4")}});
 String input = null;
 Writer out = new StringWriter();
 try {
 lookupTranslator.translate(new StringCharSequence(input), 0, out);
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testTranslateMethodWriterNull() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{{new CharSequence("A", "Z"), new CharSequence("1", "2")}, {new CharSequence("B", "3"), new CharSequence("4")}});
 String input = "AB";
 try {
 lookupTranslator.translate(new StringCharSequence(input), 0, null);
 fail("Should have thrown an IOException");
 } catch (IOException e) {
 // expected
 }
 }
}