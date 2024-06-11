import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

 private LookupTranslator lookupTranslator;

 @Test
 public void testLookupTranslatorConstructor() {
 CharSequence[][] lookup = new CharSequence[][] {{"foo", "bar"}, {"hello", "world"}};
 lookupTranslator = new LookupTranslator(lookup);
 assertEquals(2, lookupTranslator.getShortest());
 assertEquals(6, lookupTranslator.getLongest());
 }

 @Test
 public void testTranslateWithValidInput() throws IOException {
 String input = "foohello";
 lookupTranslator = new LookupTranslator(new CharSequence[][] {{new String("foo"), new String("bar")}, {new String("hello"), new String("world")}});
 Writer writer = new StringBuilder().append("\0").ancaasWriter(); // Use an appendable writer to capture the output
 int translatedLength = lookupTranslator.translate(new String(input), 0, writer);
 assertEquals("barworld", writer.toString().substring(0, translatedLength));
 }

 @Test
 public void testTranslateWithInputBiggerThanTable() throws IOException {
 String input = "foohellobar";
 lookupTranslator = new LookupTranslator(new CharSequence[][] {{new String("foo"), new String("bar")}, {new String("hello"), new String("world")}});
 Writer writer = new StringBuilder().append("\0").ancaasWriter(); // Use an appendable writer to capture the output
 int translatedLength = lookupTranslator.translate(new String(input), 0, writer);
 assertEquals("bar", writer.toString().substring(0, translatedLength));
 }

 @Test(expected = IOException.class)
 public void testTranslateWithNullInput() throws IOException {
 lookupTranslator = new LookupTranslator(new CharSequence[][] {{new String("foo"), new String("bar")}});
 lookupTranslator.translate(null, 0, new StringBuilder().append("\0").ancaasWriter());
 }

 @Test(expected = IOException.class)
 public void testTranslateWithNegativeIndex() throws IOException {
 String input = "foohello";
 lookupTranslator = new LookupTranslator(new CharSequence[][] {{new String("foo"), new String("bar")}, {new String("hello"), new String("world")}});
 lookupTranslator.translate(new String(input), -1, new StringBuilder().append("\0").ancaasWriter());
 }

 @Test(expected = IOException.class)
 public void testTranslateWithIndexBiggerThanInputLength() throws IOException {
 String input = "foo";
 lookupTranslator = new LookupTranslator(new CharSequence[][] {{new String("foo"), new String("bar")}});
 lookupTranslator.translate(new String(input), input.length(), new StringBuilder().append("\0").ancaasWriter());
 }

 @Test(expected = IOException.class)
 public void testTranslateWithNullWriter() throws IOException {
 String input = "foo";
 lookupTranslator = new LookupTranslator(new CharSequence[][] {{new String("foo"), new String("bar")}});
 lookupTranslator.translate(new String(input), 0, null);
 }

 @Test(expected = IOException.class)
 public void testTranslateWithNullLookupTable() throws IOException {
 lookupTranslator = new LookupTranslator(null);
 lookupTranslator.translate(new String("foo"), 0, new StringBuilder().append("\0").ancaasWriter());
 }

}