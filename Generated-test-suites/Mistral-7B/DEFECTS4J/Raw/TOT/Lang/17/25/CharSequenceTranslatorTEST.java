package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

 private static final CharSequenceTranslator identityTranslator = new IdentityTranslator();
 private static final CharSequenceTranslator hexTranslator = new HexTranslator();

 /**
 * Test identity translator
 */
 @Test
 public void testIdentityTranslator() {
 String input = "Hello World!";
 String expectedOutput = "Hello World!";
 String actualOutput = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return identityTranslator.translate(input, index, out);
 }
 }.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 /**
 * Test hex translator
 */
 @Test
 public void testHexTranslator() {
 String input = "abcdef";
 String expectedOutput = "616263646566";
 String actualOutput = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return hexTranslator.translate(input, index, out);
 }
 }.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 /**
 * Test aggregated translators
 */
 @Test
 public void testAggregateTranslator() {
 CharSequenceTranslator[] translators = {identityTranslator, hexTranslator};
 CharSequenceTranslator aggregatedTranslator = new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 return new AggregateTranslator(translators).translate(input, index, out);
 }
 }.translate(CharSequenceUtils.repeat("abc", 10));
 String expectedOutput = "abcabcabcabcabcabcabcabcabc";
 String actualOutput = "";
 try (Writer writer = new StringWriter()) {
 aggregatedTranslator.translate(CharSequenceUtils.repeat("abc", 10), writer);
 actualOutput = writer.toString();
 }
 assertEquals(expectedOutput, actualOutput);
 }

 /**
 * Test exception handling
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 new CharSequenceTranslator() {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 throw new IllegalArgumentException("Writer must not be null");
 }
 }.translate(CharSequenceUtils.repeat("abc", 10), null);
 }

}