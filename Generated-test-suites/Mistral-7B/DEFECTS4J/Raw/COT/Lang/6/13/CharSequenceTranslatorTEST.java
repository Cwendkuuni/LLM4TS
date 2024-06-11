import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequence input;
 private String expectedOutput;
 private StringWriter outputWriter;

 @Before
 public void setUp() {
 translator = new MyCharSequenceTranslator(); // replace MyCharSequenceTranslator with the actual class to be tested
 input = "Hello World!";
 expectedOutput = "H&o&l&l&o&space;W&o&r&l&d&space;&#033;!";
 outputWriter = new StringWriter();
 }

 @Test
 public void testTranslateCharSequence() {
 String actualOutput = translator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTranslateWriter() throws IOException {
 translator.translate(input, outputWriter);
 assertEquals(expectedOutput, outputWriter.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWriter() {
 translator.translate(input, null);
 }

 @Test
 public void testEmptyInput() {
 String actualOutput = translator.translate(Character.toString(new char[0]));
 assertEquals("", actualOutput);
 }

 @Test
 public void testTranslateSingleCharacter() {
 char c = 'A';
 String actualOutput = translator.translate(Character.toString(c), outputWriter);
 assertEquals(hex(c.codePointAt(0)), actualOutput);
 }

 @Test
 public void testTranslateSurrogatePair() {
 char c1 = '\uD800';
 char c2 = '\uDC00';
 String actualOutput = translator.translate(Character.toString(c1) + Character.toString(c2), outputWriter);
 assertEquals(hex(Character.codePointAt(Character.toString(c1).toCharArray(), 0)) + hex(Character.codePointAt(Character.toString(c2).toCharArray(), 0)), actualOutput);
 }

 @Test
 public void testTranslateWithAggregate() {
 CharSequenceTranslator translator1 = new MyCharSequenceTranslator1();
 CharSequenceTranslator translator2 = new MyCharSequenceTranslator2();
 CharSequenceTranslator mergedTranslator = translator.with(translator1, translator2);
 String actualOutput = mergedTranslator.translate(input);
 assertEquals(expectedOutput + "123", actualOutput); // add some specific output from translator1 and translator2
 }

}