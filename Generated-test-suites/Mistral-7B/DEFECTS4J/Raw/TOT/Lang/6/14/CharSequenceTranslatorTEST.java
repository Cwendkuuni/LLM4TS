package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;

 @Test
 public void testTranslate() throws Exception {
 CharSequence input = "test String with special characters:  più ▶";
 String expectedOutput = "test%20String%20with%20special%20characters%3A%20%25%20%C3%9E%20%2560";
 translator = new IdentityTranslator(); // Identity translator for testing
 String actualOutput = translator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWriter() {
 translator = new IdentityTranslator();
 try {
 translator.translate(null, new StringWriter(10));
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testIOException() throws Exception {
 StringWriter writer = new StringWriter(10);
 writer.write("failed");
 translator = new IdentityTranslator();
 try {
 translator.translate(null, writer);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testWriteSingleCharacter() throws Exception {
 translator = new IdentityTranslator();
 StringWriter writer = new StringWriter(10);
 CharSequence input = "a";
 int expectedConsumed = 1;
 int actualConsumed = translator.translate(input, 0, writer);
 assertEquals(expectedConsumed, actualConsumed);
 String expectedOutput = "a";
 String actualOutput = translator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testWriteSurrogatePair() throws Exception {
 translator = new IdentityTranslator();
 StringWriter writer = new StringWriter(10);
 CharSequence input = "\uD800\uDC00";
 int expectedConsumed = 2;
 int actualConsumed = translator.translate(input, 0, writer);
 assertEquals(expectedConsumed, actualConsumed);
 String expectedOutput = "\uD800\uDC00";
 String actualOutput = translator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testWriteMultipleCharacters() throws Exception {
 translator = new IdentityTranslator();
 StringWriter writer = new StringWriter(10);
 CharSequence input = "abcd";
 int expectedConsumed = 4;
 int actualConsumed = translator.translate(input, 0, writer);
 assertEquals(expectedConsumed, actualConsumed);
 String expectedOutput = "abcd";
 String actualOutput = translator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testMergeTranslators() throws Exception {
 CharSequenceTranslator translator1 = new IdentityTranslator();
 CharSequenceTranslator translator2 = new HexTranslator();
 CharSequenceTranslator translator3 = new UpperCaseTranslator();
 CharSequenceTranslator translator = translator1.with(translator2, translator3);
 String input = "test String with special characters:  più ▶";
 String expectedOutput = "test%20STRING%20WITH%20SPECIAL%20CHARACTERS%3A%20%25%20%C3%9E%20%2560";
 String actualOutput = translator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

}