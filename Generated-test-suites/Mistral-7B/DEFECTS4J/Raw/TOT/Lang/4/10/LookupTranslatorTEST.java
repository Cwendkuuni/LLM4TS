package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator
 */
public class LookupTranslatorTest {

 private static final String LOOKUP_TABLE[][] = new String[][] {
 {"hello", "hi"}, {"world", "planet"}, {"java", "programming language"}, {"goodbye", "farewell"},
 {"apache commons lang", "APACHE COMMONS LANG"}
 };

 @Test
 public void testTranslateWithValidInput() {
 LookupTranslator translator = new LookupTranslator(LOOKUP_TABLE);
 String input = "hello world java goodbye apache commons lang";
 char[] expectedOutput = {'h', 'i', ' ', 'h', 'e', 'l', 'l', 'o', ' ', 'p', 'l', 'a', 'n', 'e', 't', ' ',
 'p', 'r', 'o', 'g', 'r', 'a', 'm', 'm', 'i', 'n', 'g', ' ', 'l', 'a', 'n', 'g', 'u', 'a', 'g', 'e', ' ',
 'A', 'P', 'A', 'C', 'H', 'E', ' ', 'C', 'O', 'M', 'M', 'o', 'n', 's', ' ', 'L', 'a', 'n', 'g'} ;
 try (Writer writer = new java.io.StringWriter()) {
 char[] output = new char[100];
 int translated = translator.translate(StringUtils.substring(input, 0, input.length()), 0, writer);
 writer.getBuffer().getChars(0, translated, output, 0);
 assertArrayEquals(expectedOutput, output);
 } catch (IOException e) {
 e.printStackTrace();
 fail("testTranslateWithValidInput: Unexpected IOException");
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateWithInputLongerThanLookedUpSequence() {
 LookupTranslator translator = new LookupTranslator(LOOKUP_TABLE);
 String input = "hi hello world java goodbye apache commons langhello";
 try {
 translator.translate(StringUtils.substring(input, 0, input.length()), 0, new java.io.StringWriter());
 fail("testTranslateWithInputLongerThanLookedUpSequence: Expected IndexOutOfBoundsException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testTranslateWithNullInput() {
 LookupTranslator translator = new LookupTranslator(LOOKUP_TABLE);
 try {
 translator.translate(null, 0, new java.io.StringWriter());
 fail("testTranslateWithNullInput: Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testTranslateWithEmptyInput() {
 LookupTranslator translator = new LookupTranslator(LOOKUP_TABLE);
 char[] expectedOutput = {};
 try (Writer writer = new java.io.StringWriter()) {
 char[] output = new char[100];
 int translated = translator.translate(StringUtils.EMPTY, 0, writer);
 writer.getBuffer().getChars(0, translated, output, 0);
 assertArrayEquals(expectedOutput, output);
 } catch (IOException e) {
 e.printStackTrace();
 fail("testTranslateWithEmptyInput: Unexpected IOException");
 }
 }
}