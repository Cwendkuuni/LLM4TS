package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for CharSequenceTranslator class.
 */
public class CharSequenceTranslatorTest {

 private static final CharSequenceTranslator TRANSLATOR = new Translator();
 private static final CharSequenceTranslator UPPER_CASE = new UpperCaseTranslator();
 private static final CharSequenceTranslator LOWER_CASE = new LowerCaseTranslator();

 @Test
 public void testTranslateSimple() {
 String input = "Hello, World!";
 String expectedOutput = "48656c6c6f2c20576f726c6421";
 String output = TRANSLATOR.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslateEmpty() {
 String input = "";
 String output = TRANSLATOR.translate(input);
 assertEquals("", output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNullWriter() {
 CharSequence input = "Hello, World!";
 try {
 TRANSLATOR.translate(input, null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testTranslateIOException() throws IOException {
 String input = "Hello, World!";
 Writer writer = new StringWriter() {
 @Override
 public void write(int c, java.io.Writer writer) throws IOException {
 throw new IOException("Simulated IOException");
 }
 };
 TRANSLATOR.translate(input, writer);
 fail("IOException should be thrown");
 }

 @Test
 public void testTranslateWithAggregate() {
 CharSequenceTranslator translator = TRANSLATOR.with(UPPER_CASE, LOWER_CASE);
 String input = "Hello, World!";
 String expectedOutput = "HELLO, WORLD!helloworld!";
 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAggregateNullTranslator() {
 CharSequenceTranslator translator = new CharSequenceTranslator() {}; // empty translator
 CharSequenceTranslator[] translators = {null};
 try {
 CharSequenceTranslator merged = translator.with(translators);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTranslateWithEmptyArray() {
 CharSequenceTranslator translator = TRANSLATOR;
 CharSequenceTranslator[] translators = {};
 CharSequenceTranslator merged = translator.with(translators);
 String input = "Hello, World!";
 String expectedOutput = "48656c6c6f2c20576f726c6421";
 String output = merged.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslateWithSingleTranslator() {
 CharSequenceTranslator translator = UPPER_CASE;
 String input = "Hello, World!";
 String expectedOutput = "HELLO, WORLD!";
 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslateWithMultipleTranslators() {
 CharSequenceTranslator translator = new Translator();
 CharSequenceTranslator translator1 = new Translator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 if (index > 0 && Character.isDigit(Character.codePointAt(input, index - 1))) {
 return 1;
 } else {
 return translator.translate(input, index, out);
 }
 }
 };
 CharSequenceTranslator translator2 = new Translator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 if (Character.isDigit(Character.codePointAt(input, index))) {
 out.write(Character.forDigit(Character.codePointAt(input, index), 16));
 return 1;
 } else {
 return translator.translate(input, index, out);
 }
 }
 };
 CharSequenceTranslator merged = translator.with(translator1, translator2);
 String input = "123ABC123";
 String expectedOutput = "312341543236";
 String output = merged.translate(input);
 assertEquals(expectedOutput, output);
 }
}