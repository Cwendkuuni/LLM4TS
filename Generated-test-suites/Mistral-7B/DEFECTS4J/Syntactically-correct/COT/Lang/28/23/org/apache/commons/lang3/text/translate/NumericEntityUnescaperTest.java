package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for NumericEntityUnscaper
 */
public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with valid hex entity.
 */
 @Test
 public void testTranslateValidHexEntity() {
 String input = "&#[xAA];";
 StringBuilder output = new StringBuilder(1);
 int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
 assertEquals("Expected length is 2", 2, length);
 assertEquals("Expected output is &a", String.valueOf(0xaa), output.toString());
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with valid decimal entity.
 */
 @Test
 public void testTranslateValidDecimalEntity() {
 String input = "&#[123];";
 StringBuilder output = new StringBuilder(1);
 int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
 assertEquals("Expected length is 2", 2, length);
 assertEquals("Expected output is &", (char) 123, output.charAt(0));
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid entity.
 */
 @Test(expected = IOException.class)
 public void testTranslateInvalidEntity() {
 String input = "&[&];";
 StringBuilder output = new StringBuilder(1);
 unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with empty CharSequence.
 */
 @Test
 public void testTranslateEmptyCharSequence() {
 StringBuilder output = new StringBuilder(1);
 int length = unescaper.translate(CharSequenceUtils.EMPTY_CHAR_SEQUENCE, 0, output);
 assertEquals("Expected length is 0", 0, length);
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with out of bounds index.
 */
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testTranslateOutOfBoundsIndex() {
 String input = "&AAA;";
 StringBuilder output = new StringBuilder(1);
 unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 1, output);
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entity without a semicolon.
 */
 @Test(expected = IOException.class)
 public void testTranslateMissingSemicolon() {
 String input = "&#[xAAA";
 StringBuilder output = new StringBuilder(1);
 unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
 }

 /**
 * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entity without a pound sign.
 */
 @Test(expected = IOException.class)
 public void testTranslateMissingPoundSign() {
 String input = "A#xA";
 StringBuilder output = new StringBuilder(1);
 unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
 }

}