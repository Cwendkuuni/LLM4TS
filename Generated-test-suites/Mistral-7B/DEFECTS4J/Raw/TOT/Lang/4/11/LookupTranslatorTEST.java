package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

 private final CharSequence[][] lookup = new CharSequence[][] {{"hello", "hi"}, {"world", "planet"}, {"test", "sample"}};

 @Test
 public void testLookupTranslator() {
 LookupTranslator translator = new LookupTranslator(lookup);
 String expected = "hi";
 String input = "hello";
 int length = input.length();
 int index = 0;
 try (Writer out = new StringWriter()) {
 translator.translate(new StringCharSequence(input), index, out);
 String result = out.toString();
 assertEquals(expected, result);
 }

 expected = "planet";
 input = "world";
 index = 0;
 try (Writer out = new StringWriter()) {
 translator.translate(new StringCharSequence(input), index, out);
 result = out.toString();
 assertEquals(expected, result);
 }

 expected = "sample";
 input = "test";
 index = 0;
 try (Writer out = new StringWriter()) {
 translator.translate(new StringCharSequence(input), index, out);
 result = out.toString();
 assertEquals(expected, result);
 }

 // Test empty input
 index = 0;
 try (Writer out = new StringWriter()) {
 translator.translate(CharSequence.EMPTY, index, out);
 assertEquals("", out.toString());
 }

 // Test input longer than lookup sequence
 input = "helloworldtest";
 index = 0;
 try (Writer out = new StringWriter()) {
 translator.translate(new StringCharSequence(input), index, out);
 assertEquals("hiplanetsample", out.toString());
 }

 // Test input shorter than lookup sequence
 input = "test";
 index = 0;
 try (Writer out = new StringWriter()) {
 translator.translate(new StringCharSequence(input), index, out);
 assertEquals("sample", out.toString());
 }
 }
}