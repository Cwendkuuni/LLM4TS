package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * NumericEntityUnescaperTest
 */
public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSimple() {
 CharSequence input = "&amp;";
 Writer output = new StringBuilder();
 int length = unescaper.translate(input, 0, output);
 assertEquals("&amp;".charAt(0), output.charAt(0));
 assertEquals("amp;".charAt(1), output.charAt(1));
 assertEquals(2, length);
 }

 @Test
 public void testTranslateHex() {
 CharSequence input = "&#x61;";
 Writer output = new StringBuilder();
 int length = unescaper.translate(input, 0, output);
 assertEquals('a', output.charAt(0));
 assertEquals(1, length);
 }

 @Test
 public void testTranslateDecimal() {
 CharSequence input = "&#101;";
 Writer output = new StringBuilder();
 int length = unescaper.translate(input, 0, output);
 assertEquals('e', output.charAt(0));
 assertEquals(1, length);
 }

 @Test
 public void testTranslateInvalidHex() {
 CharSequence input = "&#xaaa;";
 Writer output = new StringBuilder();
 assertThrows(NumberFormatException.class, () -> unescaper.translate(input, 0, output));
 }

 @Test
 public void testTranslateInvalidDecimal() {
 CharSequence input = "&#ab;";
 Writer output = new StringBuilder();
 assertThrows(NumberFormatException.class, () -> unescaper.translate(input, 0, output));
 }

 @Test
 public void testTranslateOutOfBounds() {
 CharSequence input = "&foo;";
 Writer output = new StringBuilder();
 assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(input, 1, output));
 }

 @Test
 public void testTranslateEmpty() {
 CharSequence input = "";
 Writer output = new StringBuilder();
 int length = unescaper.translate(input, 0, output);
 assertEquals(0, length);
 }

 @Test
 public void testTranslateMultipleEntities() {
 CharSequence input = "abc&amp;def&amp;ghi&amp;jkl&amp;mno&amp;pqr&amp;stu&amp;wxyz";
 Writer output = new StringBuilder();
 int length = unescaper.translate(input, 0, output);
 assertEquals("abcdefghijklmnopqrstuwxyz", output.toString());
 assertEquals(21, length);
 }
}