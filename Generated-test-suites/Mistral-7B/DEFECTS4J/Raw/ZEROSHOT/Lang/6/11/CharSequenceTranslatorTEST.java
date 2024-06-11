import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequence input;
 private String expectedOutput;
 private StringWriter writer;

 @Test
 public void testTranslateWithEmptyInput() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence cs, int index, Writer writer) throws IOException {
 return 0;
 }
 };
 input = "";
 expectedOutput = "";
 testTranslate(translator, input, expectedOutput);
 }

 @Test
 public void testTranslateWithNullInput() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence cs, int index, Writer writer) throws IOException {
 return 0;
 }
 };
 input = null;
 testTranslate(translator, input, null);
 }

 @Test
 public void testTranslateSingleCharacter() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence cs, int index, Writer writer) throws IOException {
 char c = Character.codePointAt(cs, index);
 writer.write(c);
 return Character.charCount(c);
 }
 };
 input = "A";
 expectedOutput = "A";
 testTranslate(translator, input, expectedOutput);
 }

 @Test
 public void testTranslateMultipleCharacters() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence cs, int index, Writer writer) throws IOException {
 int consumed = 0;
 for (int i = index; i < cs.length(); i++) {
 consumed += translateSingleCharacter(cs, i, writer);
 }
 return consumed;
 }

 private int translateSingleCharacter(CharSequence cs, int index, Writer writer) throws IOException {
 char c = Character.codePointAt(cs, index);
 writer.write(c);
 return Character.charCount(c);
 }
 };
 input = "AB";
 expectedOutput = "AB";
 testTranslate(translator, input, expectedOutput);
 }

 @Test
 public void testTranslateWithSurrogatePair() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence cs, int index, Writer writer) throws IOException {
 int consumed = 0;
 for (int i = index; i < cs.length(); i += 2) {
 consumed += translateSurrogatePair(cs, i, writer);
 }
 return consumed;
 }

 private int translateSurrogatePair(CharSequence cs, int index, Writer writer) throws IOException {
 char first = Character.codePointAt(cs, index);
 char second = Character.codePointAt(cs, index + 1);
 writer.write(Character.toChars(Character.toIntBitmap(first) << 10 | Character.toIntBitmap(second)));
 return 2;
 }
 };
 input = "𠮷";
 expectedOutput = "\uD840\uDC2C";
 testTranslate(translator, input, expectedOutput);
 }

 @Test
 public void testTranslateWithAggregateTranslator() {
 CharSequenceTranslator translator1 = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence cs, int index, Writer writer) throws IOException {
 writer.write("X");
 return 1;
 }
 };
 CharSequenceTranslator translator2 = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence cs, int index, Writer writer) throws IOException {
 writer.write("Y");
 return 1;
 }
 };
 translator = new AggregateTranslator(translator1, translator2);
 input = "AB";
 expectedOutput = "XY";
 testTranslate(translator, input, expectedOutput);
 }

 private void testTranslate(CharSequenceTranslator translator, CharSequence input, String expectedOutput) {
 writer = new StringWriter();
 translator.translate(input, writer);
 assertEquals("Output does not match expected", expectedOutput, writer.toString());
 }

}