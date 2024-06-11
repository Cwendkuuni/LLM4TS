package org.apache.commons.lang3.text.translate;

 import org.apache.commons.lang3.CharSequenceUtils;
 import org.apache.commons.lang3.text.translate.AggregateTranslator;
 import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
 import org.junit.Before;
 import org.junit.Test;
 import static org.junit.Assert.*;

 /**
 * Tests for {@link CharSequenceTranslator} class.
 */
 public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequenceTranslator escapedTranslator;
 private CharSequenceTranslator unescapedTranslator;

 @Before
 public void setUp() {
 translator = new Transliterator(); // default translator
 escapedTranslator = new TransliteratorWithEscapes(); // translator with escapes
 unescapedTranslator = new TransliteratorWithoutEscapes(); // translator without escapes
 }

 /**
 * Test for translate method with default translator.
 */
 @Test
 public void testTranslate_DefaultTranslator() {
 String text = "Hello,  Welt &amp; World!";
 String expected = "Hallo,  Welt & Welt; World!";
 String actual = translator.translate(text);
 assertEquals(expected, actual);
 }

 /**
 * Test for translate method with translator with escapes.
 */
 @Test
 public void testTranslate_WithEscapesTranslator() {
 String text = "Hello,  Welt &amp; World!";
 String expected = "Hallo,  Welt &amp;amp; World!";
 String actual = escapedTranslator.translate(text);
 assertEquals(expected, actual);
 }

 /**
 * Test for translate method with translator without escapes.
 */
 @Test
 public void testTranslate_WithoutEscapesTranslator() {
 String text = "Hello,  Welt &amp; World!";
 String expected = "Hallo,  Welt & World!";
 String actual = unescapedTranslator.translate(text);
 assertEquals(expected, actual);
 }

 /**
 * Test for translate method with empty input.
 */
 @Test
 public void testTranslate_EmptyInput() {
 String text = "";
 String actual = translator.translate(text);
 assertEquals("", actual);
 }

 /**
 * Test for translate method with null input.
 */
 @Test(expected = NullPointerException.class)
 public void testTranslate_NullInput() {
 translator.translate(null);
 }

 /**
 * Test for translate method with Writer argument.
 */
 @Test
 public void testTranslate_ToWriter() {
 String text = "Hello,  Welt &amp; World!";
 StringWriter writer = new StringWriter();
 translator.translate(text, writer);
 String expected = "Hallo,  Welt & Welt; World!";
 String actual = writer.toString();
 assertEquals(expected, actual);
 }

 /**
 * Test for with method.
 */
 @Test
 public void testWith() {
 CharSequenceTranslator merger = translator.with(escapedTranslator, unescapedTranslator);
 String text = "Hello,  Welt &amp; World!";
 String expected = "Hallo,  Welt &amp;amp; World! & Hallo,  Welt & World!";
 String actual = merger.translate(text);
 assertEquals(expected, actual);
 }

 /**
 * Test for translate method with invalid input - empty CharSequence.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_EmptyCharSequence() {
 translator.translate(CharSequenceUtils.EMPTY_STRING, null);
 }

 }