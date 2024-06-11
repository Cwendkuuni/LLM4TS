import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

 private LookupTranslator lookupTranslator;

 @Test
 public void testLookupTranslatorConstructor() {
 CharSequence[][] lookup = new CharSequence[][]{{"apple", "banana"}, {"cat", "dog"}, {"hello", "world"}};
 lookupTranslator = new LookupTranslator(lookup);
 assertNotNull(lookupTranslator.getLookupMap());
 assertEquals(3, lookupTranslator.getShortest());
 assertEquals(6, lookupTranslator.getLongest());
 }

 @Test
 public void testTranslate_InputSequenceIsInLookupTable() throws Exception {
 CharSequence[][] lookup = new CharSequence[][]{{"apple", "banana"}, {"cat", "dog"}, {"hello", "world"}};
 lookupTranslator = new LookupTranslator(lookup);
 String expected = "banana";
 CharSequence input = "apple";
 Writer writer = new StringWriter();
 int actual = lookupTranslator.translate(input, 0, writer);
 assertEquals(5, actual);
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testTranslate_InputSequenceIsPartialMatch() throws Exception {
 CharSequence[][] lookup = new CharSequence[][]{{"apple", "banana"}, {"cat", "dog"}, {"hello", "world"}};
 lookupTranslator = new LookupTranslator(lookup);
 String expected = "banana";
 CharSequence input = "appl";
 Writer writer = new StringWriter();
 int actual = lookupTranslator.translate(input, 0, writer);
 assertEquals(4, actual);
 assertEquals(expected, writer.toString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslate_InputSequenceIsTooLong() throws Exception {
 CharSequence[][] lookup = new CharSequence[][]{{"apple", "banana"}, {"cat", "dog"}, {"hello", "world"}};
 lookupTranslator = new LookupTranslator(lookup);
 CharSequence input = "applebanana";
 Writer writer = new StringWriter();
 lookupTranslator.translate(input, 0, writer);
 fail("Expected IndexOutOfBoundsException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslate_InputSequenceIsTooShort() throws Exception {
 CharSequence[][] lookup = new CharSequence[][]{{"apple", "banana"}, {"cat", "dog"}, {"hello", "world"}};
 lookupTranslator = new LookupTranslator(lookup);
 CharSequence input = "app";
 Writer writer = new StringWriter();
 lookupTranslator.translate(input, 0, writer);
 fail("Expected IndexOutOfBoundsException");
 }

 @Test(expected = IOException.class)
 public void testTranslate_WriterIsNull() throws Exception {
 CharSequence[][] lookup = new CharSequence[][]{{"apple", "banana"}, {"cat", "dog"}, {"hello", "world"}};
 lookupTranslator = new LookupTranslator(lookup);
 CharSequence input = "apple";
 Writer writer = null;
 lookupTranslator.translate(input, 0, writer);
 fail("Expected IOException");
 }
}