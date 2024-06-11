package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private static final CharSequenceTranslator TRANSLATOR = new Transliterator();

 /**
 * Tests the translation of a simple CharSequence using the default translator.
 */
 @Test
 public void testSimpleTranslation() {
 String input = "Hello World!";
 String expectedOutput = "Hello%20World%21";
 String output = TRANSLATOR.translate(input);
 assertEquals(expectedOutput, output);
 }

 /**
 * Tests the translation of a CharSequence with special characters using the default translator.
 */
 @Test
 public void testSpecialCharacterTranslation() {
 String input = "Hello 世界!";
 String expectedOutput = "Hello%20%E4%B8%96%E7%95%A7%21";
 String output = TRANSLATOR.translate(input);
 assertEquals(expectedOutput, output);
 }

 /**
 * Tests the translation of an empty CharSequence using the default translator.
 */
 @Test
 public void testEmptyTranslation() {
 String input = "";
 String expectedOutput = "";
 String output = TRANSLATOR.translate(input);
 assertEquals(expectedOutput, output);
 }

 /**
 * Tests the translation of a null CharSequence using the default translator.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testNullTranslation() {
 CharSequence input = null;
 TRANSLATOR.translate(input, 0, new StringWriter());
 }

 /**
 * Tests the combination of multiple translators.
 */
 @Test
 public void testCombinedTranslation() {
 CharSequenceTranslator translator1 = new Transliterator();
 CharSequenceTranslator translator2 = new ReplaceWithSpace();
 CharSequenceTranslator finalTranslator = translator1.with(translator2);
 String input = "Hello World!";
 String expectedOutput = "Hello %20World %21";
 String output = finalTranslator.translate(input);
 assertEquals(expectedOutput, output);
 }

}