package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 /**
 * Test translating a simple numeric entity.
 */
 @Test
 public void testTranslateSimpleEntity() {
 CharSequence input = "&#x41;";
 Writer out = new StringBuilder();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("A", out.toString());
 assertEquals(1, length);
 }

 /**
 * Test translating a numeric entity with leading whitespace.
 */
 @Test
 public void testTranslateEntityWithLeadingWhitespace() {
 CharSequence input = " &#x65;";
 Writer out = new StringBuilder();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("e", out.toString());
 assertEquals(1, length);
 }

 /**
 * Test translating a numeric entity with trailing whitespace.
 */
 @Test
 public void testTranslateEntityWithTrailingWhitespace() {
 CharSequence input = "&#x66; ";
 Writer out = new StringBuilder();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("f", out.toString());
 assertEquals(1, length);
 }

 /**
 * Test translating a numeric entity outside input sequence.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateEntityOutsideInputSequence() {
 CharSequence input = "& entities &";
 Writer out = new StringBuilder();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, input.length(), out);
 }

 /**
 * Test translating a numeric entity with invalid format.
 */
 @Test
 public void testTranslateEntityInvalidFormat() {
 CharSequence input = "&&#x65;";
 Writer out = new StringBuilder();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 /**
 * Test translating a numeric entity with multiple entities.
 */
 @Test
 public void testTranslateMultipleEntities() {
 CharSequence input = "&#x41;&#x65;&#10;";
 Writer out = new StringBuilder();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 assertEquals("Ae\n", out.toString());
 assertEquals(3, length);
 }

 /**
 * Test translating a numeric entity with a negative value.
 */
 @Test(expected = NumberFormatException.class)
 public void testTranslateEntityNegativeValue() {
 CharSequence input = "&#-1;";
 Writer out = new StringBuilder();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(input, 0, out);
 }
}