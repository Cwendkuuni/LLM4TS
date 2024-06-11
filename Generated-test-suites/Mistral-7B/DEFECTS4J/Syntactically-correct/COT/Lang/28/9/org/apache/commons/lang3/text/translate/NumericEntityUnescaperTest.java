package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * NumericEntityUnescaperTest
 */
public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_validHexEntity() {
 CharSequence input = "&#x41;";
 int expectedLen = 1;
 int actualLen = unescaper.translate(input, 0, null);
 assertEquals(expectedLen, actualLen);
 }

 @Test
 public void testTranslate_validDecimalEntity() {
 CharSequence input = "&#171;";
 int expectedLen = 1;
 int actualLen = unescaper.translate(input, 0, null);
 assertEquals(expectedLen, actualLen);
 }

 @Test
 public void testTranslate_validHexEntityWithLeadingZero() {
 CharSequence input = "&#x0A;";
 int expectedLen = 1;
 int actualLen = unescaper.translate(input, 0, null);
 assertEquals(expectedLen, actualLen);
 }

 @Test
 public void testTranslate_validDecimalEntityWithLeadingZero() {
 CharSequence input = "&#032;";
 int expectedLen = 1;
 int actualLen = unescaper.translate(input, 0, null);
 assertEquals(expectedLen, actualLen);
 }

 @Test
 public void testTranslate_validHexEntityWithMultipleDigits() {
 CharSequence input = "&#x1F44D;";
 int expectedLen = 3;
 int actualLen = unescaper.translate(input, 0, null);
 assertEquals(expectedLen, actualLen);
 }

 @Test
 public void testTranslate_validDecimalEntityWithMultipleDigits() {
 CharSequence input = "&#123456;";
 int expectedLen = 3;
 int actualLen = unescaper.translate(input, 0, null);
 assertEquals(expectedLen, actualLen);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_invalidEntity_missingHash() {
 CharSequence input = "&abc;";
 int expectedLen = 0;
 int actualLen = unescaper.translate(input, 0, null);
 assertEquals(expectedLen, actualLen);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslate_invalidEntity_missingSemicolon() {
 CharSequence input = "&#123;";
 int expectedLen = 0;
 int actualLen = unescaper.translate(input, 0, null);
 assertEquals(expectedLen, actualLen);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_invalidEntity_invalidHexDigit() {
 CharSequence input = "&#xAB;";
 int expectedLen = 0;
 int actualLen = unescaper.translate(input, 0, null);
 assertEquals(expectedLen, actualLen);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_invalidEntity_invalidDecimalNumber() {
 CharSequence input = "&#123AB;";
 int expectedLen = 0;
 int actualLen = unescaper.translate(input, 0, null);
 assertEquals(expectedLen, actualLen);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslate_inputIndexOutOfBounds() {
 CharSequence input = "&abcd;&#123;ef";
 int index = input.length() + 1;
 int expectedLen = 0;
 int actualLen = unescaper.translate(input, index, null);
 assertEquals(expectedLen, actualLen);
 }
}