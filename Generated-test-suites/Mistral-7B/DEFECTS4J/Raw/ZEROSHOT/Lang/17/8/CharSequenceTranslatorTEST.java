import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharacterEscapes;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Locale;

/**
 * Test cases for CharSequenceTranslator class.
 */
public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequenceTranslator characterEscapes;

 @Test
 public void testInit() {
 translator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 // implementation not tested here
 return 0;
 }
 };
 characterEscapes = new CharacterEscapes();
 }

 @Test
 public void testTranslateCharSequence() {
 String input = "test input with µ, é, ß, ∫, √, ∑, ∂, ∫, ∞, π, ♥, ♥, ♥";
 String expectedOutput = "test input with \\u00BC, \\u00E9, \\u00DF, \\u220F, \\u2212, \\u2202, \\u2203, \\u2202, \\u221E, \\u03C0, \\u2665, \\u2665, \\u2665";
 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslateCharSequenceWithCharacterEscapes() {
 String input = "test input with µ, é, ß, ∫, √, ∑, ∂, ∫, ∞, π, ♥, ♥, ♥";
 String expectedOutput = "test input with \\u00BC, \\u00E9, \\u00DF, \\u220F, \\u2212, \\u2202, \\u2203, \\u2202, \\u221E, \\u03C0, \\u2665, \\u2665, \\u2665";
 String output = characterEscapes.translate(input);
 String translatedOutput = translator.translate(output);
 assertEquals(expectedOutput, translatedOutput);
 }

 @Test
 public void testTranslateCharSequenceWithAggregateTranslator() {
 CharSequenceTranslator translatorWithEscapes = translator.with(characterEscapes);
 String input = "test input with µ, é, ß, ∫, √, ∑, ∂, ∫, ∞, π, ♥, ♥, ♥";
 String expectedOutput = "test input with \\u00BC, \\u00E9, \\u00DF, \\u220F, \\u2212, \\u2202, \\u2203, \\u2202, \\u221E, \\u03C0, \\u2665, \\u2665, \\u2665";
 String output = translatorWithEscapes.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslateWriter() throws IOException {
 StringWriter writer = new StringWriter();
 String input = "test input with µ, é, ß, ∫, √, ∑, ∂, ∫, ∞, π, ♥, ♥, ♥";
 translator.translate(input, writer);
 String output = writer.toString();
 String expectedOutput = "test input with \\u00BC, \\u00E9, \\u00DF, \\u220F, \\u2212, \\u2202, \\u2203, \\u2202, \\u221E, \\u03C0, \\u2665, \\u2665, \\u2665";
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslateEmptyCharSequence() {
 String output = translator.translate(StringUtils.EMPTY);
 assertEquals(StringUtils.EMPTY, output);
 }

}